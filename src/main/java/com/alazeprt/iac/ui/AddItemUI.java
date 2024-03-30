package com.alazeprt.iac.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class AddItemUI {
    private static Stage stage;
    private static final Logger logger = LogManager.getLogger();

    public static void showAddItemStage() {
        if(stage != null) {
            if(!stage.isShowing()) {
                showAddItemStageHandler();
            }
            stage.setAlwaysOnTop(true);
        } else {
            showAddItemStageHandler();
        }
    }

    private static void showAddItemStageHandler() {
        logger.info("Loading Create Item Page...");
        Stage createProjectStage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(AddItemUI.class.getResource("AddItem.fxml"));
        } catch (IOException e) {
            logger.fatal("Failed to load Create Item Page!", e);
        }
        Scene scene = new Scene(root, 450, 400);
        createProjectStage.getIcons().add(new Image(AddItemUI.class.getResource("image/icon.png").toString()));
        createProjectStage.setTitle("New Project...");
        createProjectStage.setScene(scene);
        createProjectStage.setResizable(false);
        createProjectStage.show();
        stage = createProjectStage;
    }

    public static void closeAddItemStage() {
        stage.close();
        stage = null;
    }
}
