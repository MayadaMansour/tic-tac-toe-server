package tictacteo_server.data;

import TicTacToeCommon.models.UserModel;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface UserDAO {

    

   
     ResultSet findById(String id) throws Exception;

boolean insert(String id, String name, String password) throws Exception;    
    ResultSet findByUsernameAndPassword(String name,String password)throws Exception;

    

}
