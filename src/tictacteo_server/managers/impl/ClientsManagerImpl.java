/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictacteo_server.managers.impl;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.String;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import tictacteo_server.handlers.ClientHandler;
import tictacteo_server.managers.*;

public class ClientsManagerImpl implements ClientsManager {

    private final ArrayList<ClientHandler> activeClients = new ArrayList<>();
    private final Map<String, ClientHandler> authClients = new HashMap<>();
    private final ServerSocketManager socketManager;
    
    public ClientsManagerImpl(ServerSocketManager socketManager) {
        this.socketManager = socketManager;
    }
    
    @Override
    public void accept(Socket socket) {
        ClientHandler client = null;
        activeClients.add(client);
    }

    @Override
    public void send(String userId, Serializable data) throws Exception {
        ClientHandler client = authClients.get(userId);
        if (client != null) {
            client.send(data);
        }
    }

    @Override
    public void authenticateHandler(String userId, ClientHandler handler) {
        authClients.put(userId, handler);
    }

    @Override
    public void unathenticateHandler(String userId) {
        authClients.remove(userId);
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
    }
}
