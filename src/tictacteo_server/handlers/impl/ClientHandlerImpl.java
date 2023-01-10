/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictacteo_server.handlers.impl;

import java.io.Serializable;
import tictacteo_server.handlers.ClientHandler;


public  class ClientHandlerImpl implements ClientHandler {

    @Override
    public void send(Serializable data) throws Exception {
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public Object getUser() {
        return null;
    }

    @Override
    public void stop() throws Exception {
    }

    
}
