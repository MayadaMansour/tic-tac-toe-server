/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictacteo_server.managers;

import TicTacToeCommon.models.UserModel;
import TicTacToeCommon.models.base.RemoteSendable;
import java.net.Socket;
import java.util.ArrayList;
import tictacteo_server.handlers.ClientHandler;


public interface ClientsManager {

    void accept(Socket socket);

    void send(String userId, RemoteSendable data) throws Exception;
    
    boolean isAvailableToPlay(String userId);
    
    boolean isOnline(String userId);
    
    void setIsPlaying(String userId, boolean isPlaying);
    
    ArrayList<UserModel> getAvailablePlayers();
    
    UserModel getUser(String userId);
    
    void authenticateHandler(String userId, ClientHandler handler);
    
    void unathenticateHandler(String userId);
    
    void removeHandler(ClientHandler handler);
    
    void stop() throws Exception;
    
}
