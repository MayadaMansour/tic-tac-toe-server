/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictacteo_server.handlers;

import TicTacToeCommon.models.UserModel;
import TicTacToeCommon.models.base.RemoteSendable;

public interface ClientHandler{

    void send(RemoteSendable data) throws Exception;

    boolean isAuthenticated();

    boolean isPlaying();
    
    void setIsPlaying(boolean isPlaying);

    UserModel getUser();

    void stop() throws Exception;
}
