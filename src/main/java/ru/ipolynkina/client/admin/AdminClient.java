package ru.ipolynkina.client.admin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AdminClient extends Application {

    private static Stage primaryStage;

    public static void main(String[] args) {

        launch();
    }

    private static void setPrimaryStage(Stage stage) {
        AdminClient.primaryStage = stage;
    }

    public static Stage getPrimaryStage() {
        return AdminClient.primaryStage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setPrimaryStage(primaryStage);
        Parent parent = FXMLLoader.load(getClass().getResource("/adminClient.fxml"));
        primaryStage.setTitle("Admin Client Limiter v.1.0.0");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(parent, 610, 435));
        primaryStage.show();
    }
}
