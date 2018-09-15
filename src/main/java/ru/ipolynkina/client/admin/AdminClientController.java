package ru.ipolynkina.client.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.ipolynkina.json.JSONRequest;
import ru.ipolynkina.client.admin.dialogs.AddEditDialog;
import ru.ipolynkina.client.admin.dialogs.DialogInfo;
import ru.ipolynkina.client.admin.dialogs.OkCancelDialog;
import ru.ipolynkina.entity.ProgramEntity;

public class AdminClientController implements Initializable {

    @FXML private Button btnSend;
    @FXML private Button btnAdd;
    @FXML private Button btnEdit;
    @FXML private Button btnDelete;
    @FXML private Button btnUpdate;
    @FXML private PasswordField txtLogin;
    @FXML private PasswordField txtPassword;
    @FXML private Label errorText;

    @FXML private TableView<ProgramEntity> tblLimiter;
    @FXML private TableColumn<ProgramEntity, Integer> versionId;
    @FXML private TableColumn<ProgramEntity, String> versionText;
    @FXML private TableColumn<ProgramEntity, String> programName;
    @FXML private TableColumn<ProgramEntity, Boolean> isFree;

    private BufferedReader in;
    private PrintWriter out;
    private ObservableList<ProgramEntity> programs;

    private static final Logger LOGGER = LogManager.getLogger(AdminClientController.class.getSimpleName());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        programs = FXCollections.observableArrayList();
        setButtonVisibility(false);
    }

    @FXML private void sendLoginAndPassword() {
        try {
            selectAndUpdateTable();
            setButtonVisibility(true);
            errorText.setText("");
        } catch(JSONException exc) {
            errorText.setText("incorrect login or password");
        }
    }

    @FXML private void addVersion() {
        try(Socket socket = new Socket("localhost", 4444)) {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            DialogInfo.setDefaultInfo();
            AddEditDialog dialog = new AddEditDialog(AdminClient.getPrimaryStage());

            if(DialogInfo.isSuccessful()) {
                JSONRequest request = new JSONRequest(txtLogin.getText());
                request.addPassword(txtPassword.getText());
                request.addCommand("addVersion");
                request.addProgram(DialogInfo.getProgramEntity().getProgramName());
                request.addVersion(DialogInfo.getProgramEntity().getVersionText());
                request.addIsFree(DialogInfo.getProgramEntity().getIsFree());
                out.println(request);
                out.println("end");
                LOGGER.info("out: " + request);
                selectAndUpdateTable();
            }

            dialog.close();
            out.close();
            in.close();

        }  catch(ConnectException exc) {
            errorText.setText("sorry. connection error");
        } catch(IOException exc) {
            errorText.setText(exc.getMessage());
            LOGGER.error(exc.getMessage());
            exc.printStackTrace();
        }
    }

    @FXML private void editVersion() {
        try(Socket socket = new Socket("localhost", 4444)) {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            ProgramEntity selectProgram = tblLimiter.getSelectionModel().getSelectedItem();
            if(selectProgram != null) {
                DialogInfo.setProgramEntity(selectProgram);
                AddEditDialog dialog = new AddEditDialog(AdminClient.getPrimaryStage());
                
                if(DialogInfo.isSuccessful()) {
                    JSONRequest request = new JSONRequest(txtLogin.getText());
                    request.addPassword(txtPassword.getText());
                    request.addCommand("editVersion");
                    request.addVersionId(DialogInfo.getProgramEntity().getVersionId());
                    request.addProgram(DialogInfo.getProgramEntity().getProgramName());
                    request.addVersion(DialogInfo.getProgramEntity().getVersionText());
                    request.addIsFree(DialogInfo.getProgramEntity().getIsFree());
                    out.println(request);
                    out.println("end");
                    LOGGER.info("out: " + request);
                    selectAndUpdateTable();
                }

                dialog.close();
                out.close();
                in.close();
            }
        } catch(ConnectException exc) {
            errorText.setText("sorry. connection error");
        } catch(IOException exc) {
            errorText.setText(exc.getMessage());
            LOGGER.error(exc.getMessage());
            exc.printStackTrace();
        }
    }

    @FXML private void deleteVersion() {
        try(Socket socket = new Socket("localhost", 4444)) {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            ProgramEntity selectProgram = tblLimiter.getSelectionModel().getSelectedItem();
            if(selectProgram != null) {
                DialogInfo.setProgramEntity(selectProgram);
                OkCancelDialog dialog = new OkCancelDialog(AdminClient.getPrimaryStage());

                if(DialogInfo.isSuccessful()) {
                    JSONRequest request = new JSONRequest(txtLogin.getText());
                    request.addPassword(txtPassword.getText());
                    request.addCommand("deleteVersion");
                    request.addVersionId(selectProgram.getVersionId());
                    out.println(request);
                    out.println("end");
                    LOGGER.info("out: " + request);
                    selectAndUpdateTable();
                }

                dialog.close();
                out.close();
                in.close();
            }
        } catch(ConnectException exc) {
            errorText.setText("sorry. connection error");
        } catch(IOException exc) {
            errorText.setText(exc.getMessage());
            LOGGER.error(exc.getMessage());
            exc.printStackTrace();
        }
    }

    @FXML private void selectAndUpdateTable() {
        selectAllVersions();
        updateTable();
    }

    private void setButtonVisibility(boolean isVisible) {
        btnAdd.setVisible(isVisible);
        btnEdit.setVisible(isVisible);
        btnDelete.setVisible(isVisible);
        btnUpdate.setVisible(isVisible);
        tblLimiter.setVisible(isVisible);
    }

    private void selectAllVersions() {
        try(Socket socket = new Socket("localhost", 4444)) {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            JSONRequest request = new JSONRequest(txtLogin.getText());
            request.addPassword(txtPassword.getText());
            request.addCommand("select all");
            out.println(request);
            out.println("end");
            LOGGER.info("out: " + request);

            StringBuilder builder = new StringBuilder();
            String inputLine;
            while(!(inputLine = in.readLine()).equals("end")) {
                builder.append(inputLine);
            }
            LOGGER.info("in: " + builder.toString());

            programs = FXCollections.observableArrayList();
            JSONArray array = new JSONArray(builder.toString());
            JSONObject object;
            int counter = 0;
            while(counter < array.toList().size() && (object = array.getJSONObject(counter++)) != null) {
                programs.add(new ProgramEntity(object.getInt("versionId"), object.getString("versionText"),
                        object.getString("programName"), object.getBoolean("isFree")));
            }

            out.close();
            in.close();

        } catch(ConnectException exc) {
            errorText.setText("sorry. connection error");
        } catch(IOException exc) {
            errorText.setText(exc.getMessage());
            LOGGER.error(exc.getMessage());
            exc.printStackTrace();
        }
    }

    private void updateTable() {
        versionId.setCellValueFactory(new PropertyValueFactory<>("versionId"));
        versionText.setCellValueFactory(new PropertyValueFactory<>("versionText"));
        programName.setCellValueFactory(new PropertyValueFactory<>("programName"));
        isFree.setCellValueFactory(new PropertyValueFactory<>("isFree"));
        tblLimiter.setItems(programs);
    }
}
