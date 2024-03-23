package com.alazeprt.iac.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AboutUIController {
    private static Stage aboutStage;

    private static void showAboutStageHandler() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(AboutUIController.class.getResource("About.fxml"));
        Scene scene = new Scene(root, 300, 200);
        stage.setTitle("About");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        aboutStage = stage;
    }

    public static void showAboutStage() throws IOException {
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
