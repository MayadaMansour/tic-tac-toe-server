package tictacteo_server.data;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Karim
 */
public interface GameDAO {
    
    ResultSet createGame(String gameId,String player1Id,String player2Id,Date startingDate,
            String nextPlayerId) throws SQLException;
            
    ResultSet getGameById(String id) throws SQLException;
    
    ResultSet getGamesByPlayerId(String id) throws SQLException;
    
   
    
}
