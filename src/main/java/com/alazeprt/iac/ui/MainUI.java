package com.alazeprt.iac.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MainUI {
    private static Stage mainStage;
    protected static String path = "";
    protected static void showMainStage(String folder) throws IOException {
        if(mainStage != null) {
            if(!mainStage.isShowing()) {
                path = folder;
                showMainStageHandler();
            }
            mainStage.setAlwaysOnTop(true);
        } else {
            path = folder;
            showMainStageHandler();
        }
    }

    private static void showMainStageHandler() throws IOException {
        Stage projectStage = new Stage();
        Parent root = FXMLLoader.load(MainUI.class.getResource("MainPage.fxml"));
        Scene scene = new Scene(root, 960, 640);
        projectStage.setTitle("Project - ");
        projectStage.setScene(scene);
        projectStage.setResizable(false);
        projectStage.show();
        mainStage = projectStage;
    }

    protected static void closeMainStage() {
        mainStage.close();
        mainStage = null;
        path = "";
    }
}
