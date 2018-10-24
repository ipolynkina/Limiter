package ru.ipolynkina.client.admin.dialogs;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddEditDialogController implements Initializable {

    @FXML private TextField programName;
    @FXML private TextField versionText;
    @FXML private ChoiceBox<Boolean> isFree;
    @FXML private Button ok;
    @FXML private Button cancel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        programName.setText(DialogInfo.getProgramEntity().getProgramName());
        versionText.setText(DialogInfo.getProgramEntity().getVersionText());
        isFree.setValue(DialogInfo.getProgramEntity().getIsFree());
    }

    @FXML private void setStatusOk() {
        if(!programName.getText().equals("") && !versionText.getText().equals("")) {
            DialogInfo.setStatusIsOk(true);
            DialogInfo.getProgramEntity().setProgramName(programName.getText());
            DialogInfo.getProgramEntity().setVersionText(versionText.getText());
            DialogInfo.getProgramEntity().setIsFree(isFree.getValue());
            Stage stage = (Stage) ok.getScene().getWindow();
            stage.close();
        }
    }

    @FXML private void setStatusCancel() {
        DialogInfo.setStatusIsOk(false);
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }
}