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
public class GameMapper implements EntityMapper<GameModel> {

    @Override
    public GameModel mapToEntity(ResultSet resultSet) throws SQLException {
        String gameId = resultSet.getString("id");
        String player1Id = resultSet.getString("player1Id");
        String player2Id = resultSet.getString("player2Id");
        Long createdAt = resultSet.getLong("createdAt");
        return new GameModel(gameId, player1Id, player2Id, createdAt);
    }

    @Override
    public void writeToSet(GameModel entity, ResultSet resultSet) throws SQLException {
        resultSet.updateString("id", entity.getGameId());
        resultSet.updateString("player1Id", entity.getPlayer1Id());
        resultSet.updateString("player2Id", entity.getPlayer2Id());
        resultSet.updateLong("createdAt", entity.getCreatedAt());
    }

}
