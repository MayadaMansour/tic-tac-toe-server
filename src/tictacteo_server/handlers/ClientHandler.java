/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictacteo_server.handlers;

import TicTacToeCommon.models.UserModel;
import java.io.Serializable;

public interface ClientHandler {

    void send(Serializable data) throws Exception;

    boolean isAuthenticated();

    boolean isPlaying();

    UserModel getUser();

    void stop() throws Exception;
}
