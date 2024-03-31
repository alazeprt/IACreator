package com.alazeprt.iac.ui;

import com.alazeprt.iac.config.IAConfig;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public class PackingUI {
    private static Stage stage;
    private static final Logger logger = LogManager.getLogger();

    public static void showPackingItemStage(IAConfig config) {
        if(stage != null) {
            if(!stage.isShowing()) {
                showPackingStageHandler(config);
            }
            stage.setAlwaysOnTop(true);
        } else {
            showPackingStageHandler(config);
        }
    }

    private static void showPackingStageHandler(IAConfig config) {
        logger.info("Loading Edit Item Page...");
        PackingUIController.path = config.root().toPath();
        PackingUIController.namespace = config.projectName();
        Stage editItemStage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(EditItemUI.class.getResource("Packing.fxml")));
        } catch (IOException e) {
            logger.fatal("Failed to load Packing Page!", e);
        }
        Scene scene = new Scene(Objects.requireNonNull(root), 600, 300);
        editItemStage.getIcons().add(new Image(Objects.requireNonNull(AddItemUI.class.getResource("image/icon.png")).toString()));
        editItemStage.setTitle("Package Project...");
        editItemStage.setScene(scene);
        editItemStage.setResizable(false);
        editItemStage.show();
        stage = editItemStage;
    }

    public static void closePackingStage() {
        stage.close();
        stage = null;
    }
}
