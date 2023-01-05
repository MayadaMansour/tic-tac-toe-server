/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictacteo_server.data;

import java.sql.Connection;


public interface DatabaseManager {

    void start() throws Exception;

    Connection getConnection() throws Exception;

    void stop() throws Exception;
}
