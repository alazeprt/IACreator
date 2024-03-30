package com.alazeprt.iac.ui;

import com.alazeprt.iac.config.IAConfig;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class ProjectUI {
    private static Stage mainStage;
    protected static String path = "";
    private static final Logger logger = LogManager.getLogger();
    protected static void showMainStage(String folder, String projectName) {
        if(mainStage != null) {
            if(!mainStage.isShowing()) {
                path = folder;
                showMainStageHandler(projectName);
            } else {
                mainStage.close();
                path = folder;
                showMainStageHandler(projectName);
            }
        } else {
            path = folder;
            showMainStageHandler(projectName);
        }
    }

    private static void showMainStageHandler(String projectName) {
        logger.info("Loading Project Page...");
        IAConfig iaConfig = new IAConfig(projectName, new File(path));
        iaConfig.generateDefaultConfig();
        Stage projectStage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(ProjectUI.class.getResource("Project.fxml"));
        } catch (IOException e) {
            logger.fatal("Failed to Project Page!", e);
        }
        Scene scene = new Scene(root, 960, 640);
        projectStage.getIcons().add(new Image(ProjectUI.class.getResource("image/icon.png").toString()));
        projectStage.setTitle("Project - " + projectName);
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
