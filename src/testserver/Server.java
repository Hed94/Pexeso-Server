/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hra;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testserver.Lobby;


public class Server 
{
     ArrayList<Socket> clients = new ArrayList<Socket>();
 
    public static void main(String[] args) throws IOException, ClassNotFoundException 
    {
       new Server().runServer();
    }

public static Logger LOGGER = LoggerFactory.getLogger(Server.class);    
    
    public void runServer() throws IOException, ClassNotFoundException 
    {
       ServerSocket serverSocket = new ServerSocket(33);
       LOGGER.info("Server je ready a čeká na hráče");
       while(true)
       {
       Socket socket = serverSocket.accept();
       clients.add(socket);
            if(clients.size() == 2)
            {
            LOGGER.info("Server vytváří pro dva hráče lobby");
            new Lobby(clients.get(0),clients.get(1)).start();
            clients.clear();
            }
        }
    }
}
