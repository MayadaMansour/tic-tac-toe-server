/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tictacteo_server.handlers;

import java.io.Serializable;


public interface GameHandler {

    boolean canProcess(String userId, Serializable data);

    void process(String userId, Serializable data);
}
