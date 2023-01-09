package tictacteo_server.data;

import TicTacToeCommon.models.UserModel;
import java.sql.ResultSet;

public interface UserDAO {

    ResultPacket findById(String id) throws Exception;

    String createUser(UserModel user, String password) throws Exception;

    ResultPacket findByUsernameAndPassword(String name, String password) throws Exception;

}
