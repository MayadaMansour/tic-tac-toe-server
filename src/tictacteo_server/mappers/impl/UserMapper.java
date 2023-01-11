/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictacteo_server.mappers.impl;

import TicTacToeCommon.models.UserModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import tictacteo_server.mappers.EntityMapper;

public class UserMapper implements EntityMapper<UserModel> {

    @Override
    public UserModel mapToEntity(ResultSet resultSet) throws SQLException {
        UserModel userModel = new UserModel();
        userModel.setId(resultSet.getString("id"));
        userModel.setName(resultSet.getString("username"));
        userModel.setCreatedAt(resultSet.getLong("createdAt"));
        return userModel;
    }

    @Override
    public void writeToSet(UserModel entity, ResultSet resultSet) throws SQLException {
        resultSet.updateString("id", entity.getId());
        resultSet.updateString("username", entity.getName());
        resultSet.updateLong("createdAt", entity.getCreatedAt());
    }
}
