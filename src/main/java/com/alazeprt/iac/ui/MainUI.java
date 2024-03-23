package com.alazeprt.iac.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MainUI {
    private static final Logger logger = LoggerFactory.getLogger(MainUI.class.toString());
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
        logger.info("Opening main page...");
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
