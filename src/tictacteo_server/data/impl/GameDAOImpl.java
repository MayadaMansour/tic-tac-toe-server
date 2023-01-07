/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictacteo_server.data.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import tictacteo_server.data.DatabaseManager;
import tictacteo_server.data.GameDAO;

/**
 *
 * @author Karim
 */
public class GameDAOImpl implements GameDAO{
    
    private DatabaseManager databaseManager;
    private Connection connection;
    private PreparedStatement statement;
    
    public GameDAOImpl(DatabaseManager databaseManager) throws Exception{
        this.databaseManager = databaseManager;
        connection = databaseManager.getConnection();
    }

    @Override
    public ResultSet getGameById(String id) throws SQLException{
        
        String queryString = "SELECT * FROM GAME WHERE ? = ?";
        statement = connection.prepareStatement(queryString,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
        statement.setString(1, "GAME");
        statement.setString(2, id);
        return statement.executeQuery();
    }

    @Override
    public ResultSet getGamesByPlayerId(String id) throws SQLException{
        
        String queryString = "SELECT * FROM GAME WHERE PLAYER1 = ? OR PLAYER2 = ?";
        statement = connection.prepareStatement(queryString,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
        statement.setString(1, id);
        statement.setString(2, id);
        return statement.executeQuery();
    }

    @Override
    public ResultSet createGame(String gameId, String player1Id, String player2Id, Date startingDate, 
        String nextPlayerId) throws SQLException{
        
        String queryString = "INSERT INTO GAME (ID,PLAYER1,PLAYER2,STARTEDAT,NEXTPLAYER) VALUES(?,?,?,?,?)";
        statement = connection.prepareStatement(queryString,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
        statement.setString(1, gameId);
        statement.setString(2, player1Id);
        statement.setString(3, player2Id);
        statement.setObject(4, startingDate);
        statement.setString(5, nextPlayerId);
        
        return statement.executeQuery();
    }
    
    @Override
    public void closeStatement()throws SQLException{
        statement.close();
    }
    
}
