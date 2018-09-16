package ru.ipolynkina.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import ru.ipolynkina.server.db.DBController;
import ru.ipolynkina.server.db.DBManager;

public class Server {

    private static final Logger LOGGER = LogManager.getLogger("Server");

    public static void main(String[] args) {

        DBManager dbManager = new DBManager("limiter");
        DBController dbController = new DBController(dbManager);

        LOGGER.info("server: start");

        while(true) {
            try(ServerSocket server = new ServerSocket(4444)) {
                Socket socket = server.accept();
                Runnable runnable = new ServerHandler(socket, dbController);
                Thread thread = new Thread(runnable);
                thread.start();
            } catch(IOException exc) {
                LOGGER.error(exc.getMessage());
                exc.printStackTrace();
            }
        }
    }
}
