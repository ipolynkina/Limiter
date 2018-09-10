package ru.ipolynkina.client.user;

import org.json.JSONArray;
import ru.ipolynkina.json.JSONRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;

public class UserClient {
    public static void main(String[] args) {

        System.out.println("UserClient start");

        try(Socket socket = new Socket("localhost", 4444)) {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            JSONRequest jsonRequest = new JSONRequest("user");
            jsonRequest.addCommand("select");
            jsonRequest.addProgram("GraphsForSapHR");
            jsonRequest.addVersion("3");
            out.println(jsonRequest);
            out.println("end");

            String builder = "";
            String inputLine;
            while(!(inputLine = in.readLine()).equals("end")) {
                builder = builder.concat(inputLine);
            }
            JSONArray array = new JSONArray(builder);

            System.out.println("out: " + jsonRequest);
            System.out.println("in: " + builder);
            System.out.println("in is free: " + array.getJSONObject(0).getBoolean("isFree"));

            in.close();
            out.close();
        } catch(ConnectException exc) {
            System.out.println("sorry. connection error");
        } catch(IOException exc) {
            exc.printStackTrace();
        }

        System.out.println("UserClient end");
    }
}
