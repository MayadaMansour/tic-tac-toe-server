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


public class DerbyDatabaseManager implements DatabaseManager, Serializable {

    private static DerbyDatabaseManager instance;
    private static Connection connection;
    private final static String DATABASE_URL = "jdbc:derby://localhost:1527/database";
    private final static String USERNAME = "root";
    private final static String PASSWORD = "root";

    private DerbyDatabaseManager() throws SQLException {
        // DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
    }

    public static synchronized DerbyDatabaseManager getInstance() throws SQLException {

        if(instance == null){
            instance =  new DerbyDatabaseManager();
        }
        return instance;
    }


    @Override
    public void start() throws SQLException {

        if(connection == null){
            connection = DriverManager.getConnection(DerbyDatabaseManager.DATABASE_URL,DerbyDatabaseManager.USERNAME
            ,DerbyDatabaseManager.PASSWORD);
        }
    }

    @Override
    public Connection getConnection(){
        return connection;
    }

    @Override
    public void stop() throws SQLException {
        connection.close();
        connection = null;
    }

}
