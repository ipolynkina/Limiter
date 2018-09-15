package ru.ipolynkina.client.admin.dialogs;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class OkCancelDialog extends Stage {

    public OkCancelDialog(Stage parentStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/okCancelDialog.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 300, 180);
        Stage stage = new Stage();
        stage.initOwner(parentStage);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("Yes No Dialog");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.showAndWait();
    }
}