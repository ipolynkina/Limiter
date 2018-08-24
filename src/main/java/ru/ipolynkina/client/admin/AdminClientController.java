package ru.ipolynkina.client.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.ipolynkina.server.entity.ProgramEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminClientController implements Initializable {

    @FXML private Button btnSend;
    @FXML private Button btnAdd;
    @FXML private Button btnEdit;
    @FXML private Button btnDelete;
    @FXML private TextField txtLogin;
    @FXML private PasswordField txtPassword;

    @FXML private TableView<ProgramEntity> tblLimiter;
    @FXML private TableColumn<ProgramEntity, Integer> idVersion;
    @FXML private TableColumn<ProgramEntity, String> textVersion;
    @FXML private TableColumn<ProgramEntity, String> nameProgram;
    @FXML private TableColumn<ProgramEntity, Boolean> isFree;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnAdd.setVisible(false);
        btnEdit.setVisible(false);
        btnDelete.setVisible(false);
        tblLimiter.setVisible(false);
    }

    @FXML private void send() {
        try(Socket socket = new Socket("localhost", 4444)) {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            out.println("admin");
            String response = in.readLine();
            if(response.equals("input: login;password")) {
                out.println(txtLogin.getText() + ";" + txtPassword.getText());
            }

            response = in.readLine();
            if(response.equals("true")) {
                btnAdd.setVisible(true);
                btnEdit.setVisible(true);
                btnDelete.setVisible(true);
                tblLimiter.setVisible(true);

                ObservableList<ProgramEntity> programs = FXCollections.observableArrayList();
                String line;
                while((line = in.readLine()) != null) {
                    String[] parameters = line.split(";");
                    programs.add(new ProgramEntity(Integer.parseInt(parameters[0]), parameters[1].trim(),
                            parameters[2].trim(), Boolean.parseBoolean(parameters[3].trim())));
                }

                idVersion.setCellValueFactory(new PropertyValueFactory<>("idVersion"));
                textVersion.setCellValueFactory(new PropertyValueFactory<>("textVersion"));
                nameProgram.setCellValueFactory(new PropertyValueFactory<>("nameProgram"));
                isFree.setCellValueFactory(new PropertyValueFactory<>("isFree"));
                tblLimiter.setItems(programs);
            }

            in.close();
            out.close();
        } catch(ConnectException exc) {
            System.out.println("sorry. connection error");
        } catch(IOException exc) {
            exc.printStackTrace();
        }
    }
}
