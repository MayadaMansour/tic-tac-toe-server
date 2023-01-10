package tictacteo_server.data;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Karim
 */
public interface GameDAO {
    
    int createGame(String gameId,String player1Id,String player2Id,Date startingDate,
            String nextPlayerId) throws SQLException;
            
    ResultPacket getGameById(String id) throws SQLException;
    
    ResultPacket getGamesByPlayerId(String id) throws SQLException;
    
   
    
}
