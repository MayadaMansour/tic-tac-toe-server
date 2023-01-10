
package tictacteo_server.data.impl;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import tictacteo_server.data.DatabaseManager;
import tictacteo_server.data.GameDAO;
import tictacteo_server.data.ResultPacket;

/**
 *
 * @author Karim
 * @version 1.1
 * @since 1.0
 */
public class GameDAOImpl implements GameDAO{
    
    private final DatabaseManager databaseManager;
    
    public GameDAOImpl(DatabaseManager databaseManager) throws Exception{
        this.databaseManager = databaseManager;
    }

    @Override
    public ResultPacket getGameById(String id) throws SQLException{
        
        String queryString = "SELECT * FROM GAME WHERE ? = ?";
        PreparedStatement statement = databaseManager.getConnection().prepareStatement(queryString,ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_UPDATABLE);
        statement.setString(1, "GAME");
        statement.setString(2, id);
        
        return new ResultPacket(statement.executeQuery(),statement);
    }

    @Override
    public ResultPacket getGamesByPlayerId(String id) throws SQLException{
        
        String queryString = "SELECT * FROM GAME WHERE PLAYER1 = ? OR PLAYER2 = ?";
        PreparedStatement statement = databaseManager.getConnection().prepareStatement(queryString,ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY);
        statement.setString(1, id);
        statement.setString(2, id);
        
        return new ResultPacket(statement.executeQuery(),statement);
    }

    @Override
    public int createGame(String gameId, String player1Id, String player2Id, Date startingDate, 
        String nextPlayerId) throws SQLException{
        
        String queryString = "INSERT INTO GAME (ID,PLAYER1,PLAYER2,STARTEDAT,NEXTPLAYER) VALUES(?,?,?,?,?)";
        PreparedStatement statement = databaseManager.getConnection().prepareStatement(queryString,ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_UPDATABLE);
        statement.setString(1, gameId);
        statement.setString(2, player1Id);
        statement.setString(3, player2Id);
        statement.setObject(4, startingDate);
        statement.setString(5, nextPlayerId);
        
        return statement.executeUpdate();
    }
    
    
}
