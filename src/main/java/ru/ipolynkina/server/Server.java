package ru.ipolynkina.server;

import ru.ipolynkina.server.db.DBController;
import ru.ipolynkina.server.db.DBManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {

        DBManager dbManager = new DBManager("limiter");
        DBController dbController = new DBController(dbManager);

        System.out.println("Server start");

        while(true) {
            try(ServerSocket server = new ServerSocket(4444)) {
                Socket socket = server.accept();
                Runnable runnable = new RunnableHandler(socket, dbController);
                Thread thread = new Thread(runnable);
                thread.start();
            } catch(IOException exc) {
                exc.printStackTrace();
            }
        }
    }
}
