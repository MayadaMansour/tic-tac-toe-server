/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictacteo_server.handlers.impl;

import TicTacToeCommon.models.UserModel;
import TicTacToeCommon.models.base.RemoteMessage;
import TicTacToeCommon.models.base.RemoteSendable;
import TicTacToeCommon.models.requests.LoginRequest;
import TicTacToeCommon.models.requests.OnlinePlayersRequest;
import TicTacToeCommon.models.requests.SignUpRequest;
import TicTacToeCommon.models.responses.LoginResponse;
import TicTacToeCommon.models.responses.OnlinePlayersResponse;
import TicTacToeCommon.models.responses.SignUpResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import tictacteo_server.data.DatabaseManager;
import tictacteo_server.data.ResultPacket;
import tictacteo_server.data.impl.UserDAOImpl;
import tictacteo_server.handlers.ClientHandler;
import tictacteo_server.managers.ServerSocketManager;
import tictacteo_server.mappers.EntityMapper;

class ClientHandlerImpl implements ClientHandler, Runnable {

    private final ServerSocketManager serverSocketManager;
    private final Socket socket;
    private final ObjectOutputStream objectOutputStream;
    private final ObjectInputStream objectInputStream;
    private final DatabaseManager databaseManager;
    private final Connection connection;
    private final UserDAOImpl userDao;
    private ResultPacket resultPacket;
    private UserModel userModel;
    private EntityMapper<UserModel> entityMapper;
    private boolean isPlaying;

    public ClientHandlerImpl(ServerSocketManager serverSocketManager, Socket socket, DatabaseManager databaseManager)
            throws IOException, SQLException {
        this.serverSocketManager = serverSocketManager;
        this.socket = socket;
        this.databaseManager = databaseManager;
        this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        this.connection = databaseManager.getConnection();
        this.userDao = new UserDAOImpl(databaseManager);
        // Submit yourself in the thread pool
        serverSocketManager.submitJob(this);
    }

    public void handleMessage(RemoteMessage remoteMessage) {
        if (remoteMessage.getMessage(LoginRequest.class) != null) {
            handleLoginMessage(remoteMessage);
        } else if (remoteMessage.getMessage(SignUpRequest.class) != null) {
            handleSignUpRequest(remoteMessage);
        } else if (remoteMessage.getMessage(OnlinePlayersRequest.class) != null) {
            handleOnlinePlayersRequest();
        }
    }

    private void handleLoginMessage(RemoteMessage remoteMessage) {
        try {
            LoginRequest loginRequest = (LoginRequest) remoteMessage.getMessage();
            // In the case of multiple threads accessing this method
            ResultPacket resultPacket = userDao.
                    findByUsernameAndPassword(loginRequest.getUsername(), loginRequest.getPassword());
            ResultSet resultSet = resultPacket.getResultSet();
            // Check if the user account is in the database or not.
            // If the result set is empty (The client hasn't signed up before)
            if (!resultSet.next()) {
                send(new LoginResponse(false));
                resultPacket.close();
            } else { // already signed up before
                try {
                    userModel = entityMapper.mapToEntity(resultSet);
                } catch (SQLException e) {
                    resultPacket.close();
                    throw e;
                }
                this.resultPacket = resultPacket;
                send(new LoginResponse(true, userModel)); // TODO wrap the user model inside the login response
                serverSocketManager.getClientsManager().authenticateHandler(false, resultSet.getString(1), this);
            }
        } catch (SQLException e) {
            send(new LoginResponse(false)); // TODO wrap the user model inside the login response
        }
    }

    private void handleSignUpRequest(RemoteMessage remoteMessage) {
        /*
            Check if the user has signed up before by getting a ResultSet.
         */
        SignUpRequest signUpRequest = (SignUpRequest) remoteMessage.getMessage();
        try {
            UserModel userModel = new UserModel(UUID.randomUUID().toString(), signUpRequest.getUserName(), null, new Date().getTime());
            String insertedId = userDao.createUser(userModel, signUpRequest.getPassword());
            if (insertedId == null) {
                throw new Exception();
            }
            ResultPacket resultPacket = userDao.findById(insertedId);
            try {
                this.userModel = entityMapper.mapToEntity(resultPacket.getResultSet());
            } catch (SQLException e) {
                resultPacket.close();
                throw e;
            }
            this.resultPacket = resultPacket;
            serverSocketManager.getClientsManager().authenticateHandler(true, userModel.getId(), this);
            send(new SignUpResponse(true, this.userModel));
        } catch (Exception ex) { // Unable to access the database to retrieve the user's data
            send(new SignUpResponse(false));
            // once a SignUpResponse with a failure status is sent. Terminate the method.
        }
    }

    private void handleOnlinePlayersRequest() {
        send(new OnlinePlayersResponse(true, serverSocketManager.getClientsManager().getAvailablePlayers())); // If unable to send an OnlinePlayersResponse.
    }

    /**
     * Constantly listen for messages from the client on a thread
     */
    @Override
    public void run() {

        try (Socket socket = this.socket;
                ObjectInputStream in = this.objectInputStream;
                ObjectOutputStream out = this.objectOutputStream) {
            while (true) {
                RemoteMessage message = RemoteMessage.readFrom(in);
                serverSocketManager.submitJob(() -> {
                    handleMessage(message);
                });
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ClientHandlerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (userModel != null) {
                serverSocketManager.getClientsManager().unathenticateHandler(userModel.getId());
            }
            serverSocketManager.getClientsManager().removeHandler(this);
            if (resultPacket != null) {
                try {
                    resultPacket.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ClientHandlerImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Sends Serializable messages to the client
     */
    @Override
    public void send(RemoteSendable data) {
        serverSocketManager.submitJob(() -> {
            try {
                new RemoteMessage(data).writeInto(objectOutputStream);
            } catch (IOException ex) {
                Logger.getLogger(ClientHandlerImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }

    @Override
    public boolean isAuthenticated() {
        return userModel != null;
    }

    @Override
    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    public UserModel getUser() {
        return this.userModel;
    }

    @Override
    public void stop() throws IOException {
        socket.close();
    }

    @Override
    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

}
