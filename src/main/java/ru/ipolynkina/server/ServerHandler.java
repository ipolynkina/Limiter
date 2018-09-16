package ru.ipolynkina.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import ru.ipolynkina.json.JSONResponse;
import ru.ipolynkina.server.db.DBController;
import ru.ipolynkina.entity.ProgramEntity;

public class ServerHandler implements Runnable {

    private Socket socket;
    private DBController dbController;
    private static final Logger LOGGER = LogManager.getLogger("Server");

    public ServerHandler(Socket socket, DBController dbController) {
        this.socket = socket;
        this.dbController = dbController;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            StringBuilder builder = new StringBuilder();
            String inputLine;
             while(!(inputLine = in.readLine()).equals("end")) {
                 builder.append(inputLine);
            }
            LOGGER.info("in: " + builder.toString());

            JSONObject request = new JSONObject(builder.toString());
            if(request.getString("profile").equals("user")) {
                workWithUser(request, out);
            } else if(request.getString("profile").equals("admin") &&
                    request.getString("password").equals("password")) {
                if(!request.has("command")) out.println("end");
                else workWithAdmin(request, out);
            } else out.println("end");

            in.close();
            out.close();
            socket.close();

        } catch(IOException exc) {
            LOGGER.error(exc.getMessage());
            exc.printStackTrace();
        }
    }

    private void workWithUser(JSONObject request, PrintWriter out) {
        if(request.get("command").equals("select")) {
            ProgramEntity entity = dbController.selectProgramByParameters(
                    request.get("programName").toString(),
                    request.get("versionText").toString());

            JSONResponse response = new JSONResponse(entity);
            out.println(response);
            out.println("end");
            LOGGER.info("out: " + response.toString());
        } else out.println("end");
    }

    private void workWithAdmin(JSONObject request, PrintWriter out) {
        switch(request.getString("command")) {
            case "select all":    selectAllVersions(out);           break;
            case "addVersion":    addProgramVersion(request);    break;
            case "editVersion":   editProgramVersion(request);   break;
            case "deleteVersion": deleteProgramVersion(request); break;
            default: out.println("end");
        }
    }

    private void selectAllVersions(PrintWriter out) {
        List<ProgramEntity> entityList = dbController.selectAllVersions();
        try {
            JSONResponse response = new JSONResponse(entityList.get(0));
            for(int i = 1; i < entityList.size(); i++) response.addEntity(entityList.get(i));
            out.println(response);
            LOGGER.info("out: " + response);
        } catch(IndexOutOfBoundsException exc) {
            // Ignore it. DB is empty.
        }
        out.println("end");
    }

    private void addProgramVersion(JSONObject request) {
        dbController.addProgramVersion(request.getString("programName"),
                request.getString("versionText"),
                request.getBoolean("isFree"));
    }

    private void editProgramVersion(JSONObject request) {
        dbController.editProgramVersion(request.getInt("versionId"),
                request.getString("programName"),
                request.getString("versionText"),
                request.getBoolean("isFree"));
    }

    private void deleteProgramVersion(JSONObject request) {
        dbController.deleteProgramVersion(request.getInt("versionId"));
    }
}
