package ru.ipolynkina.client.admin.dialogs;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class OkCancelDialogController implements Initializable {

    @FXML private Label text;
    @FXML private Button ok;
    @FXML private Button cancel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        text.setText("Delete version " + DialogInfo.getProgramEntity().getVersionText() +
                "\nof the program " + DialogInfo.getProgramEntity().getProgramName() +
                "\n(is free: " + DialogInfo.getProgramEntity().getIsFree() + ") ?");
    }

    @FXML private void setStatusOk() {
        DialogInfo.setStatusDialog(true);
        Stage stage = (Stage) ok.getScene().getWindow();
        stage.close();
    }

    @FXML private void setStatusCancel() {
        DialogInfo.setStatusDialog(false);
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }
}
