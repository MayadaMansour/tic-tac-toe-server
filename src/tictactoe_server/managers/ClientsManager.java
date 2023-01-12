/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictactoe_server.managers;

import TicTacToeCommon.models.UserModel;
import TicTacToeCommon.models.base.RemoteSendable;
import TicTacToeCommon.utils.ObservableValue;
import java.net.Socket;
import java.util.ArrayList;
import tictactoe_server.handlers.ClientHandler;

public interface ClientsManager {

    ObservableValue<Long> getActiveUsers();

    ObservableValue<Long> getAllUsers();

    void accept(Socket socket);

    void send(String userId, RemoteSendable data);

    boolean isAvailableToPlay(String userId);

    boolean isOnline(String userId);

    void setIsPlaying(String userId, boolean isPlaying);

    ArrayList<UserModel> getAvailablePlayers();

    UserModel getUser(String userId);

    void authenticateHandler(boolean isSignup, String userId, ClientHandler handler);

    void unathenticateHandler(String userId);

    void removeHandler(ClientHandler handler);

    void stop() throws Exception;

}
