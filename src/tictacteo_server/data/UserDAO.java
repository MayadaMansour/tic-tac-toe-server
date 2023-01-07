package tictacteo_server.data;

import TicTacToeCommon.models.UserModel;
import java.sql.ResultSet;

public interface UserDAO {

    ResultSet getForId(String id);

    public int delete(String id);

    public UserModel findById(String id);

    public int insert(UserModel user);

    public int update(UserModel user);

}
