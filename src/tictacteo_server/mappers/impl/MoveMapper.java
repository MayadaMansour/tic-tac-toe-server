/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictacteo_server.mappers.impl;

import TicTacToeCommon.models.MoveModel;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import tictacteo_server.mappers.EntityMapper;

/**
 *
 * @author ITI
 */
public class MoveMapper implements EntityMapper<MoveModel> {

    @Override
    public MoveModel mapToEntity(ResultSet resultSet) throws SQLException {
        MoveModel moveModel = new MoveModel();
        moveModel.setId(resultSet.getString("id"));
        moveModel.setGameId(resultSet.getString("gameId"));
        moveModel.setPlayerId(resultSet.getString("playerId"));
        moveModel.setSpacePosition(resultSet.getByte("spacePosition"));
        moveModel.setCreatedAt(resultSet.getLong("createdAt"));
        return moveModel;
    }

    @Override
    public void writeToSet(MoveModel entity, ResultSet resultSet) throws SQLException {
        resultSet.updateString("id", entity.getId());
        resultSet.updateString("playerId", entity.getPlayerId());
        resultSet.updateString("gameId", entity.getGameId());
        resultSet.updateByte("spacePosition", entity.getSpacePosition());
        resultSet.updateLong("createdAt", entity.getCreatedAt());
    }

}
