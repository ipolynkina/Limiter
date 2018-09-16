package ru.ipolynkina.client.user;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;

import ru.ipolynkina.json.JSONRequest;

public class UserClient {

    private static final Logger LOGGER = LogManager.getLogger("UserClient");

    public static void main(String[] args) {

        LOGGER.info("user client: start");

        try(Socket socket = new Socket("localhost", 4444)) {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            JSONRequest jsonRequest = new JSONRequest("user");
            jsonRequest.addCommand("select");
            jsonRequest.addProgram("GraphsForSapHR");
            jsonRequest.addVersion("3");
            out.println(jsonRequest);
            out.println("end");
            LOGGER.info("out: " + jsonRequest);

            StringBuilder builder = new StringBuilder();
            String inputLine;
            while(!(inputLine = in.readLine()).equals("end")) {
                builder.append(inputLine);
            }
            LOGGER.info("in: " + builder);

            JSONArray array = new JSONArray(builder.toString());
            System.out.println(array.getJSONObject(0).getBoolean("isFree"));

            in.close();
            out.close();

        } catch(ConnectException exc) {
            System.out.println("sorry. connection error");
        } catch(IOException exc) {
            exc.printStackTrace();
        }

        LOGGER.info("user client: finish");
    }
}
