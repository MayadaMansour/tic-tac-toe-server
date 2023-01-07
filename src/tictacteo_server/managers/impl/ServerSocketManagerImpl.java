package tictacteo_server.managers.impl;

import com.sun.security.ntlm.Server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Future;
import java.util.function.Supplier;
import tictacteo_server.managers.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.SocketHandler;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.T;
import static sun.swing.SwingUtilities2.submit;

public class ServerSocketManagerImpl implements ServerSocketManager {

    private volatile boolean Shutdown;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final Observable observable = new WorkingObservable();
    private GamesManager GamesManager;
    private ClientsManager ClientsManager;

    private ServerSocketManagerImpl() throws IOException {
        int port = 1000;
        ServerSocket Server = null;
        while (Server == null && port++ < (Short.MAX_VALUE * 2 - 1)) {
            Server = new ServerSocket(port);
        }
        if (Server != null) {
            Socket Socket = null;
            Socket = Server.accept();
            System.out.println("Got a client " + Socket.getInetAddress());
            SocketHandler ClientThread = null;
            // ClientThread = new SocketHandle(Socket);
            registerClientThread(ClientThread);
            startClientThread(ClientThread);
        } else {
            System.out.println("There isnot available port");
        }
    }

    public ServerSocketManagerImpl(GamesManager GamesManager, ClientsManager ClientsManager) {
        this.GamesManager = GamesManager;
        this.ClientsManager = ClientsManager;
    }

    public static void main(String args[]) throws IOException {
        ServerSocketManagerImpl server = new ServerSocketManagerImpl();
        System.out.println("FINISHED! " + server.Shutdown);
    }

    @Override
    public ClientsManager getClientsManager() {
        ClientsManager ClientsManager = null;
        return ClientsManager;
    }

    public ServerSocketManagerImpl(boolean Shutdown) {
        this.Shutdown = Shutdown;
    }

    @Override
    public GamesManager getGamesManager() {
        GamesManager GamesManager = null;
        return GamesManager;
    }

    @Override
    public <T> Future<T> submitJob(Supplier<T> job) {

        Future<T> submitJob = null;
        return submitJob;
    }

    @Override
    public Future<?> submitJob(Runnable job) {
        Future<?> submitJob = null;
        return submitJob;
    }

    private void registerClientThread(SocketHandler ClientThread) {
        observable.addObserver((Observer) ClientThread);
    }

    private void unregisterClientThread(SocketHandle ClientThread) {
        observable.deleteObserver(ClientThread);
    }

    final private void startClientThread(SocketHandler ClientThread) {
        executorService.submit((Runnable) ClientThread);
    }

    private void Shutdown() {
        Shutdown = true;
    }

    private boolean isShutdown() {
        return Shutdown;
    }

    private void handleMessage(String fromClient) {
        String message = null;
        System.out.println("Received message " + message);
        notifyAllClients(message);
    }

    private void notifyAllClients(String message) {
        ExecutorService submit;
        submit = (ExecutorService) executorService.submit(new MessageBroadcaster(message) {
            @Override
            public void run() {
                observable.notifyObservers(message);
                System.out.println("Notified observors " + observable.countObservers());
            }
        });
    }

    @Override
    public void start() throws Exception {
        Thread th = null;
        th.start();
    }

    @Override
    public void stop() throws Exception {
        Thread th = null;
        th.stop();
    }

    private static class SocketHandle implements Runnable, Observer {

        private Socket ClientSocket;
        private DataInputStream inFromClient;
        private DataOutputStream outToClient;
        private ServerSocketManagerImpl ServerSocketManagerImpl;
        private Socket Socket;

        public SocketHandle(Socket ClientSocket, ServerSocketManagerImpl ServerSocketManagerImpl) throws IOException {
            this.ClientSocket = ClientSocket;
            this.ServerSocketManagerImpl = ServerSocketManagerImpl;
            this.inFromClient = new DataInputStream(ClientSocket.getInputStream());
            this.outToClient = new DataOutputStream(ClientSocket.getOutputStream());

        }

        /*  private SocketHandle(Socket Socket) {
           * this.Socket=Socket;
        }*/
        @Override
        public void run() {
            try (Socket ClientSocket = this.ClientSocket) {
                String fromClient;
                while (!ServerSocketManagerImpl.isShutdown() && (fromClient = inFromClient.readUTF()) != null) {
                    ServerSocketManagerImpl.handleMessage(fromClient);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                ServerSocketManagerImpl.unregisterClientThread(this);
            }
        }

        @Override
        public void update(Observable o, Object message) {
            System.out.println("Updating " + ClientSocket.getInetAddress());

            if (message instanceof String) {
                try {
                    System.out.println("Sending " + message);
                    outToClient.writeUTF((String) message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private class WorkingObservable extends Observable {

        @Override
        public void notifyObservers(Object data) {
            setChanged();
            super.notifyObservers(data);
        }
    }

    private static abstract class MessageBroadcaster implements Runnable {

        private final String message;

        public MessageBroadcaster(String message) {
            this.message = message;

        }
    }

}
