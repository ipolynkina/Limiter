package ru.ipolynkina.client.admin.dialogs;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class AddEditDialog extends Stage {

    public AddEditDialog(Stage parentStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/addEditDialog.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 200);
        Stage stage = new Stage();
        stage.initOwner(parentStage);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("Add Edit Dialog");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
