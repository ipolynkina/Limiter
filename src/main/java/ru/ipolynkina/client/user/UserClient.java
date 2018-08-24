package ru.ipolynkina.client.user;

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

            out.println("user");
            if(in.readLine().equals("input: program;version")) {
                out.println("GraphsForSapHR;2");
                System.out.println(in.readLine());
            }

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
