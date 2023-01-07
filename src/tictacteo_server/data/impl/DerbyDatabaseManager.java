/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictacteo_server.data.impl;

import tictacteo_server.data.DatabaseManager;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Represents a Singleton class of a DerbyDatabaseManager
 * that is thread and reflection safe but not serialization safe.
 *
 * @author Moustafa
 * @author Karim
 * @version 1.0
 * @since 1.0
 * */
public class DerbyDatabaseManager implements DatabaseManager, Serializable {

    private static volatile DerbyDatabaseManager derbyDatabaseManager;
    private static Connection connection;
    private final static String DATABASE_URL = "jdbc:derby://localhost:1527/database";
    private final static String USERNAME = "root";
    private final static String PASSWORD = "root";

    private DerbyDatabaseManager(){

        // Prevent instantiation using reflection.
        if(connection != null){
            throw new RuntimeException("Can't instantiate using the default constructor. Use getInstance()");
        }
    }

    public static DerbyDatabaseManager getInstance() throws SQLException {

        // Double-checked locking.
        if(derbyDatabaseManager == null){

            synchronized(DerbyDatabaseManager.class){

                if(derbyDatabaseManager == null){
                    derbyDatabaseManager =  new DerbyDatabaseManager();
                    DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
                }
            }
        }

        return derbyDatabaseManager;
    }
    @Override
    public void start() throws SQLException {

        // If there's an instance of the manager, but it's not connected to the database.
        if(derbyDatabaseManager != null && connection == null){
            connection = DriverManager.getConnection(DerbyDatabaseManager.DATABASE_URL,DerbyDatabaseManager.USERNAME
            ,DerbyDatabaseManager.PASSWORD);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {

        // If a connection already exists.
        if(connection != null){
            return connection;
        }

        // If there's an instance of the manager, but it's not connected to the database.
        // Create a connection and return it.
        else if(derbyDatabaseManager != null){
            start();
            return connection;
        }

        // There isn't an instance nor a connection. Create both and return a connection.
        else{
            getInstance();
            start();
            return connection;
        }
    }

    @Override
    public void stop() throws SQLException {
        if(connection != null){
            connection.close();
        }
    }

}
