/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictacteo_server.managers;

import java.io.Serializable;
import tictacteo_server.handlers.ClientHandler;
import tictacteo_server.handlers.GameHandler;


public interface GamesManager {

    boolean canProcess(String userId, Serializable data);

    void process(String userId, Serializable data) throws Exception;
    
    void setOngoingHandler(GameHandler handler);
    
    void removeHandler(ClientHandler handler);
    
    void stop() throws Exception;
    
}
