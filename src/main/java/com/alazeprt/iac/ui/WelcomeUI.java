package com.alazeprt.iac.ui;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Objects;

public class WelcomeUI extends Application {
    private static final Logger logger = LogManager.getLogger();
    private static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Loading Welcome Page...");
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(WelcomeUI.class.getResource("Welcome.fxml")));
        } catch (IOException e) {
            logger.fatal("Failed to load Welcome Page!", e);
        }
        Scene scene = new Scene(Objects.requireNonNull(root), 800, 600);
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(WelcomeUI.class.getResource("image/icon.png")).toString()));
        primaryStage.setTitle("IACreator");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        stage = primaryStage;
    }

    public static void start() {
        Stage primaryStage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(WelcomeUI.class.getResource("Welcome.fxml")));
        } catch (IOException e) {
            logger.fatal("Failed to load Welcome Page!", e);
        }
        Scene scene = new Scene(Objects.requireNonNull(root), 800, 600);
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(WelcomeUI.class.getResource("image/icon.png")).toString()));
        primaryStage.setTitle("IACreator");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        stage = primaryStage;
    }

    public static void closeWelcomeStage() {
        try {
            stage.close();
        } catch (Exception e) {
            logger.warn("Failed to close Welcome Stage!", e);
        }
    }
}
