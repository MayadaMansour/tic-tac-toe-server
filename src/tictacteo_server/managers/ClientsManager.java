/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictacteo_server.managers;

import java.io.Serializable;
import java.net.Socket;
import tictacteo_server.handlers.ClientHandler;


public interface ClientsManager {

    void accept(Socket socket);

    void send(String userId, Serializable data) throws Exception;
    
    void authenticateHandler(String userId, ClientHandler handler);
    
    void unathenticateHandler(String userId);
    
    void removeHandler(ClientHandler handler);
    
    void stop() throws Exception;
    
}
