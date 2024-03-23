package com.alazeprt.iac.ui;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class WelcomeUI extends Application {
    private static final Logger logger = LoggerFactory.getLogger(WelcomeUI.class.toString());
    private static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        logger.info("Loading welcome page...");
        Parent root = FXMLLoader.load(WelcomeUI.class.getResource("WelcomePage.fxml"));
        Scene scene = new Scene(root, 800, 600);
        primaryStage.getIcons().add(new Image(WelcomeUI.class.getResource("image/icon.png").toString()));
        primaryStage.setTitle("IACreator");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        stage = primaryStage;
    }

    public static void closeWelcomeStage() {
        stage.close();
    }
}
