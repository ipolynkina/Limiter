package ru.ipolynkina.server;

import ru.ipolynkina.server.db.DBController;
import ru.ipolynkina.server.entity.ProgramEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;

public class ServerHandler implements Runnable {

    private Socket socket;
    private DBController dbController;

    public ServerHandler(Socket socket, DBController dbController) {
        this.socket = socket;
        this.dbController = dbController;
    }

    @Override
    public void run() {
        try {
            BufferedReader in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String line;
            if((line = in.readLine()) != null) {
                if(line.equals("user")) {
                    workWithUser(out, in);
                } else if(line.equals("admin")) {
                    workWithAdmin(out, in);
                }
            }

            in.close();
            out.close();
            socket.close();
        } catch(IOException exc) {
            exc.printStackTrace();
        }
    }

    private void workWithUser(PrintWriter out, BufferedReader in) throws IOException {
        String line;
        out.println("input: program;version");
        while((line = in.readLine()) != null) {
            String[] parameters = line.split(";");
            if(parameters.length != 2) break;
            out.println("Server: " + dbController.isFree(parameters[0], parameters[1]));
        }
    }

    private void workWithAdmin(PrintWriter out, BufferedReader in) throws IOException {
        out.println("input: login;password");
        String[] parameters = in.readLine().split(";");
        if(parameters.length == 2 && parameters[0].equals("admin") && parameters[1].equals("admin")) {
            out.println("true");
            Iterator<ProgramEntity> iterator = dbController.getAllProgram().iterator();
            while(iterator.hasNext()) {
                ProgramEntity entity = iterator.next();
                out.println(entity.getIdVersion() + ";" + entity.getTextVersion() + ";" +
                        entity.getNameProgram() + ";" + entity.getIsFree());
            }
        } else {
            out.println("false");
        }
    }
}
