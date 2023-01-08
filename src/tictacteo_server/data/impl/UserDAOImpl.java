package tictacteo_server.data.impl;

import TicTacToeCommon.models.UserModel;
import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Int;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import tictacteo_server.data.DatabaseManager;
import tictacteo_server.data.UserDAO;

public abstract class UserDAOImpl implements UserDAO {

    private String ID;

    private String FIND_BY_ID = "SELECT * FROM user WHERE id=?";
    private String INSERT = "INSERT INTO user(name) VALUES(?)";

    private DatabaseManager dbm;
    private Object stmt;

    public UserDAOImpl(DatabaseManager dbm) {
        this.dbm = dbm;
    }

    @Override
    public ResultSet findById(String id) throws Exception {

        Connection conn;
        conn = dbm.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user WHERE id=?", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        stmt.setString(1, id);

        return stmt.executeQuery();
    }

    @Override

    public boolean insert(String id, String name, String password) throws Exception {

        Connection conn;
        conn = dbm.getConnection();
        PreparedStatement preparedStmt = conn.prepareStatement("INSERT INTO Registration VALUES (id, name, password)");
              
        int count= preparedStmt.executeUpdate();
        return  count > 0;

    }

    public ResultSet findByUsernameAndPassword(String name, String password) throws Exception {
        Connection conn;
        conn = dbm.getConnection();
        PreparedStatement stmt = conn.prepareStatement("Select * from user where username=? And password=?", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        stmt.setString(2, name);
        stmt.setString(3, password);
        return stmt.executeQuery();
    }

}
