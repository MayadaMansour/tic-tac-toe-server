package tictacteo_server.data.impl;

import TicTacToeCommon.models.UserModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import tictacteo_server.data.DatabaseManager;
import tictacteo_server.data.ResultPacket;
import tictacteo_server.data.UserDAO;

public class UserDAOImpl implements UserDAO {

    private final DatabaseManager dbm;

    public UserDAOImpl(DatabaseManager dbm) {
        this.dbm = dbm;
    }

    @Override
    public ResultPacket findById(String id) throws Exception {
        Connection conn = dbm.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user WHERE id=?", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        stmt.setString(1, id);
        ResultSet resultSet = stmt.executeQuery();
        ResultPacket packet = new ResultPacket(resultSet, stmt);
        return packet;
    }

    @Override
    public ResultPacket findByUsernameAndPassword(String name, String password) throws Exception {
        Connection conn = dbm.getConnection();
        PreparedStatement stmt = conn.prepareStatement("Select * from user where Name=? And Password=?", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        stmt.setString(1, name);
        stmt.setString(2, password);
        ResultSet resultSet = stmt.executeQuery();
        return new ResultPacket(resultSet, stmt);
    }

    @Override
    public String createUser(UserModel user, String password) throws Exception {
        Connection conn = dbm.getConnection();
        try (PreparedStatement preparedStmt = conn.prepareStatement("INSERT INTO Registration (id, Name, Password,CreatedAt) VALUES (?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStmt.setString(1, user.getId());
            preparedStmt.setString(2, user.getName());
            preparedStmt.setString(3, password);
            preparedStmt.setObject(4, user.getCreatedAt());
            preparedStmt.executeUpdate();
            ResultSet keys = preparedStmt.getGeneratedKeys();
            return keys.getString("id");
        }
    }

}
