/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictacteo_server.data.impl;

import TicTacToeCommon.models.MoveModel;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import tictacteo_server.data.DatabaseManager;
import tictacteo_server.data.MoveDAO;
import tictacteo_server.data.ResultPacket;

/**
 *
 * @author ITI
 */
public class MoveDAOImpl implements MoveDAO {

    private final DatabaseManager dbm;

    public MoveDAOImpl(DatabaseManager dbm) {
        this.dbm = dbm;
    }

    @Override
    public ResultPacket findMoveById(String id) throws SQLException {
        String SEARCH_MOVE_SQL = "SELECT INTO MOVE WHERE ID = ? ";
        PreparedStatement ps = dbm.getConnection().prepareStatement(SEARCH_MOVE_SQL);
        ps.setString(1, id);
        ResultSet result = ps.executeQuery();
        return new ResultPacket(result, ps);
    }

    @Override
    public ResultPacket findMoveByGameId(String gameId) throws SQLException {
        String SEARCH_MOVE_SQL = "SELECT INTO MOVE WHERE ID = ? ";
        PreparedStatement ps = dbm.getConnection().prepareStatement(SEARCH_MOVE_SQL);
        ps.setString(1, gameId);
        ResultSet result = ps.executeQuery();
        return new ResultPacket(result, ps);
    }

    @Override
    public String createMove(MoveModel moveModel) throws SQLException {
        String INSERT_MOVE_SQL = "INSERT INTO MOVE(playerId, gameId, spacePosition, createdAt) VALUES(?,?,?,?) ";
        try ( // TODO add id to model
                PreparedStatement ps = dbm.getConnection().prepareStatement(INSERT_MOVE_SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, moveModel.getPlayerId());
            ps.setString(2, moveModel.getGameId());
            ps.setByte(3, moveModel.getSpacePosition());
            ps.setLong(4, moveModel.getCreatedAt());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                return keys.getString("id");
            }
            return null;
        }
    }

}
