package com.alazeprt.iac.ui;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class WelcomeUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(WelcomeUI.class.getResource("WelcomePage.fxml"));
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("IACreator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
