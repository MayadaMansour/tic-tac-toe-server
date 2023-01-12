package tictacteo_server.managers.impl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import tictacteo_server.managers.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import tictacteo_server.data.DatabaseManager;
import tictacteo_server.data.impl.DerbyDatabaseManager;

public class ServerSocketManagerImpl implements ServerSocketManager {

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final GamesManager gamesManager;
    private final ClientsManager clientsManager;
    private final DatabaseManager databaseManager;
    private ServerSocket server;

    public ServerSocketManagerImpl() throws SQLException {
        this.gamesManager = new GamesManagerImpl(this);
        this.clientsManager = new ClientsManagerImpl(this);
        this.databaseManager = DerbyDatabaseManager.getInstance();
    }

    @Override
    public ClientsManager getClientsManager() {
        return clientsManager;
    }

    @Override
    public GamesManager getGamesManager() {
        return gamesManager;
    }

    @Override
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    @Override
    public <T> Future<T> submitJob(Callable<T> job) {
        return executorService.submit(job);
    }

    @Override
    public Future<?> submitJob(Runnable job) {
        return executorService.submit(job);
    }

    @Override
    public Future<?> start() throws Exception {
        return submitJob(() -> {
            int port = 1000;
            try {
                server = new ServerSocket(port);
                databaseManager.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            while (true) {
                Socket socket = server.accept();
                clientsManager.accept(socket);
            }
        });
    }

    @Override
    public Future<?> stop() throws Exception {
        return submitJob(() -> {
            server.close();
            gamesManager.stop();
            clientsManager.stop();
            databaseManager.stop();
            return 0;
        });
    }
}
