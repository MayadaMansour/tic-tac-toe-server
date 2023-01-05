/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictacteo_server.managers;

public interface ServerSocketManager {

    ClientsManager getClientsManager();

    GamesManager getGamesManager();

    void start() throws Exception;

    void stop() throws Exception;
}
