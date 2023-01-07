package tictacteo_server.data.impl;

import TicTacToeCommon.models.UserModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static java.util.jar.Pack200.Packer.PASS;
import tictacteo_server.data.UserDAO;

public abstract class UserDAOImpl implements UserDAO {

    private String ID;

    private String DELETE = "DELETE FROM user WHERE id=?";
    private String FIND_BY_ID = "SELECT * FROM user WHERE id=?";
    private String INSERT = "INSERT INTO user(name) VALUES(?)";
    private String UPDATE = "UPDATE user SET name=? WHERE id=?";

    private Connection getConnection() {
        try {
            String DRIVER_NAME = null;
            Class.forName(DRIVER_NAME);
            String DB_URL = null;
            return DriverManager.getConnection(DB_URL, ID, PASS);
        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

    public UserModel findById(String id) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(FIND_BY_ID);
            stmt.setNString(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                UserModel user = new UserModel();
                user.setId(rs.getString("ID"));
                user.setName(rs.getString("Name"));

                return user;
            } else {
                return null;
            }
        } catch (SQLException e) {

            throw new RuntimeException(e);
        } finally {
            close(stmt);

        }
    }

    @Override
    public int delete(String id) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(DELETE);
            stmt.setNString(1, id);

            return stmt.executeUpdate();
        } catch (SQLException e) {

            throw new RuntimeException(e);
        } finally {
            close(stmt);

        }
    }

    @Override
    public int insert(UserModel user) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getId());

            int result = stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {

                user.setId(rs.getString(ID));

            }

            return result;
        } catch (SQLException e) {

            throw new RuntimeException(e);
        } finally {
            close(stmt);

        }
    }

    @Override
    public int update(UserModel user) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(UPDATE);
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getId());

            return stmt.executeUpdate();
        } catch (SQLException e) {

            throw new RuntimeException(e);
        } finally {
            close(stmt);

        }
    }

    private static void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {

                throw new RuntimeException(e);
            }
        }
    }

    private void close(Connection conn) throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }

    @Override
    public ResultSet getForId(String id) {
        ResultSet Resultset = null;
        return Resultset;

    }
}
