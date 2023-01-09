/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictacteo_server.mappers.impl;

import TicTacToeCommon.models.GameModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import tictacteo_server.mappers.EntityMapper;

/**
 *
 * @author Karim
 */
public class GameMapper implements EntityMapper<GameModel>{

    @Override
    public GameModel mapToEntity(ResultSet resultSet) throws SQLException {
        
        String gameId = resultSet.getString(1);
        String player1Id = resultSet.getString(2);
        String player2Id = resultSet.getString(3);
        return new GameModel(gameId,player1Id,player2Id);
    }

    @Override
    public void writeToSet(GameModel entity, ResultSet resultSet) throws SQLException {
        
        resultSet.updateString(1, entity.getGameId());
        resultSet.updateString(2, entity.getPlayer1Id());
        resultSet.updateString(3, entity.getPlayer2Id());
    }
    
}
