/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictacteo_server.managers;

import java.util.concurrent.Future;
import java.util.function.Supplier;

public interface ServerSocketManager {

    ClientsManager getClientsManager();

    GamesManager getGamesManager();

    <T> Future<T> submitJob(Supplier<T> job);

    Future<?> submitJob(Runnable job);

    void start() throws Exception;

    void stop() throws Exception;
}
