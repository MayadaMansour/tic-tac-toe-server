package tictacteo_server.managers;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public interface ServerSocketManager {

    ClientsManager getClientsManager();

    GamesManager getGamesManager();

    <T> Future<T> submitJob(Callable<T> job);

    Future<?> submitJob(Runnable job);

    Future<?> start() throws Exception;

    Future<?> stop() throws Exception;
}
