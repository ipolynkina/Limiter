package ru.ipolynkina.client.admin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AdminClient extends Application {
    public static void main(String[] args) {

        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent parent = FXMLLoader.load(getClass().getResource("/AdminClient.fxml"));
        primaryStage.setTitle("Admin Client Limiter");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(parent, 600, 400));
        primaryStage.show();
    }
}
