package ru.ipolynkina.server;

import ru.ipolynkina.server.db.DBController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class RunnableHandler implements Runnable {

    private Socket socket;
    private DBController controller;

    public RunnableHandler(Socket socket, DBController controller) {
        this.socket = socket;
        this.controller = controller;
    }

    @Override
    public void run() {
        try {
            BufferedReader in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("hello, client");
            String line;
            while((line = in.readLine()) != null) {
                String[] parameters = line.split(";");
                if(parameters.length != 2) break;
                out.println("Server: " + controller.isFree(parameters[0], parameters[1]));
            }
            in.close();
            out.close();
            socket.close();
        } catch(IOException exc) {
            exc.printStackTrace();
        }
    }
}
