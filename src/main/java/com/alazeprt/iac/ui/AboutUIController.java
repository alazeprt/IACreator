package com.alazeprt.iac.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class AboutUIController {
    private static Stage aboutStage;

    private static final Logger logger = LogManager.getLogger();

    private static void showAboutStageHandler() {
        logger.info("Loading About Page...");
        Stage stage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(AboutUIController.class.getResource("About.fxml"));
        } catch (IOException e) {
            logger.fatal("Failed to load About Page!", e);
        }
        Scene scene = new Scene(root, 300, 200);
        stage.getIcons().add(new Image(AboutUIController.class.getResource("image/icon.png").toString()));
        stage.setTitle("About");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        aboutStage = stage;
    }

    public static void showAboutStage() {
        if(aboutStage != null) {
            if(!aboutStage.isShowing()) {
                showAboutStageHandler();
            }
            aboutStage.setAlwaysOnTop(true);
        } else {
            showAboutStageHandler();
        }
    }

    public void exitAction() {
        aboutStage.close();
    }
}
