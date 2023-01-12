/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictacteo_server.managers.impl;

import TicTacToeCommon.models.UserModel;
import TicTacToeCommon.models.base.RemoteSendable;
import TicTacToeCommon.models.requests.GameWithdrawRequest;
import TicTacToeCommon.utils.MutableObservableValue;
import TicTacToeCommon.utils.ObservableValue;
import java.io.Serializable;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import tictacteo_server.data.DatabaseManager;
import tictacteo_server.data.ResultPacket;
import tictacteo_server.data.UserDAO;
import tictacteo_server.data.impl.DerbyDatabaseManager;
import tictacteo_server.data.impl.UserDAOImpl;
import tictacteo_server.handlers.ClientHandler;
import tictacteo_server.managers.*;

public class ClientsManagerImpl implements ClientsManager {

    private final MutableObservableValue<Long> onlineUsers = new MutableObservableValue<>(0L);
    private final MutableObservableValue<Long> totalUsers = new MutableObservableValue<>(0L);
    private final ArrayList<ClientHandler> activeClients = new ArrayList<>();
    private final Map<String, ClientHandler> authClients = new HashMap<>();
    private final ServerSocketManager socketManager;
    private final UserDAO userDao;

    public ClientsManagerImpl(ServerSocketManager socketManager,DatabaseManager databaseManager)throws SQLException{
        this.socketManager = socketManager;
        this.userDao = new UserDAOImpl(databaseManager);
        this.totalUsers.setValue(userDao.getUsersCount());
    }

    @Override
    public void accept(Socket socket) {
        ClientHandler client = null;
        activeClients.add(client);
    }

    @Override
    public void send(String userId, RemoteSendable data) throws Exception {
        ClientHandler client = authClients.get(userId);
        if (client != null) {
            client.send(data);
        }
    }

    @Override
    public boolean isAvailableToPlay(String userId) {
        ClientHandler handler = authClients.get(userId);
        return handler != null && !handler.isPlaying();
    }

    @Override
    public boolean isOnline(String userId) {
        ClientHandler handler = authClients.get(userId);
        return handler != null;
    }

    @Override
    public ArrayList<UserModel> getAvailablePlayers() {
        return authClients.values().stream()
                .filter((e) -> !e.isPlaying())
                .map((e) -> e.getUser())
                .collect(Collectors.toCollection(() -> new ArrayList<>()));
    }

    @Override
    public UserModel getUser(String userId) {
        ClientHandler handler = authClients.get(userId);
        if (handler != null) {
            return handler.getUser();
        }
        return null;
    }

    @Override
    public void authenticateHandler(boolean isSignup, String userId, ClientHandler handler) {
        authClients.put(userId, handler);
        onlineUsers.setValue(onlineUsers.getValue() + 1);
        if (isSignup) {
            totalUsers.setValue(totalUsers.getValue() + 1);
        }
    }

    @Override
    public void unathenticateHandler(String userId) {
        authClients.remove(userId);
        onlineUsers.setValue(onlineUsers.getValue() - 1);
        try {
            socketManager.getGamesManager().process(userId, new GameWithdrawRequest());
        } catch (Exception ex) {
            Logger.getLogger(ClientsManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void setIsPlaying(String userId, boolean isPlaying) {
        ClientHandler handler = authClients.get(userId);
        if (handler != null) {
            handler.setIsPlaying(isPlaying);
        }
    }

    @Override
    public void removeHandler(ClientHandler handler) {
        activeClients.remove(handler);
    }

    @Override
    public void stop() throws Exception {
        authClients.clear();
        for (ClientHandler handler : activeClients) {
            handler.stop();
        }
        activeClients.clear();
        onlineUsers.setValue(0L);
    }

    @Override
    public ObservableValue<Long> getActiveUsers() {
        return onlineUsers;
    }

    @Override
    public ObservableValue<Long> getAllUsers() {
        return totalUsers;
    }

}
