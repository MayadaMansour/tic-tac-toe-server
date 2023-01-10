/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictacteo_server.managers.impl;

import TicTacToeCommon.models.requests.GameMoveRequest;
import TicTacToeCommon.models.requests.GameWithdrawRequest;
import TicTacToeCommon.models.requests.StartGameRequest;
import TicTacToeCommon.models.responses.StartGameResponse;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import tictacteo_server.handlers.GameHandler;
import tictacteo_server.managers.ServerSocketManager;


public abstract class GamesManagerImpl {
     private final ServerSocketManager serverSocketManager;

    public GamesManagerImpl(ServerSocketManager serverSocketManager) {
        this.serverSocketManager = serverSocketManager;
    }
    List<PendingRequest> pendingRequests = new LinkedList<>();
    List<GameHandler> gameHandlers = new LinkedList<>();
    Map<String, GameHandler> activeGames = new HashMap<>();

    boolean canProcess(String userId, Serializable data) {
        return data instanceof GameMoveRequest || data instanceof GameWithdrawRequest || data instanceof StartGameRequest;
    }

    void process(String userId, Serializable data) throws Exception {
        if (data instanceof StartGameRequest) {
            StartGameRequest startGameRequest = (StartGameRequest) data;
            String player1 = userId;
            String player2 = startGameRequest.getPlayerId();
            // TODO check players arent in any game
            PendingRequest foundPendingRequest = null;
            for (PendingRequest pendingRequest : pendingRequests) {
                if ((pendingRequest.player1 == player1 && pendingRequest.player2 == player2) || (pendingRequest.player1 == player2 && pendingRequest.player2 == player1)) {
                    foundPendingRequest = pendingRequest;
                    break;
                }
            }
            if (foundPendingRequest == null) {
                PendingRequest pendingRequest = new PendingRequest(player1, player2);
                pendingRequests.add(pendingRequest);
                serverSocketManager.getClientsManager().send(player1, new StartGameResponse(true, true));
                serverSocketManager.getClientsManager().send(player2, new StartGameRequest(player1));
            } else {
                GameHandler gameHandler = null;
                gameHandlers.add(gameHandler);
                pendingRequests.remove(foundPendingRequest);
            }
        } else {
            for (GameHandler handler : activeGames.values()) {
                if (handler.canProcess(userId, data)) {
                    handler.process(userId, data);
                }
            }
        }
    }

     void setOngoingHandler(GameHandler handler) {
        activeGames.put(handler.getGameId(), handler);
    }

    void removeHandler(GameHandler handler) {
        activeGames.remove(handler.getGameId());
        gameHandlers.remove(handler);
    }

     void stop() throws Exception {
        for (GameHandler handler : gameHandlers) {
            handler.stop();
        }
        gameHandlers.clear();
        activeGames.clear();
        pendingRequests.clear();
    }

    static class PendingRequest {
        private final String player1;
        private final String player2;

        public PendingRequest(String player1, String player2) {
            this.player1 = player1;
            this.player2 = player2;
        }
    }
}
