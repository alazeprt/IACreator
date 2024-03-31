package com.alazeprt.iac.ui;

import com.alazeprt.iac.utils.Item;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Objects;

public class EditItemUI {
    private static Stage stage;
    private static final Logger logger = LogManager.getLogger();

    public static void showEditItemStage(Item item) {
        if(stage != null) {
            if(!stage.isShowing()) {
                showEditItemStageHandler(item);
            }
            stage.setAlwaysOnTop(true);
        } else {
            showEditItemStageHandler(item);
        }
    }

    private static void showEditItemStageHandler(Item item) {
        logger.info("Loading Edit Item Page...");
        EditItemController.item = item;
        Stage editItemStage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(EditItemUI.class.getResource("EditItem.fxml")));
        } catch (IOException e) {
            logger.fatal("Failed to load Edit Item Page!", e);
        }
        Scene scene = new Scene(Objects.requireNonNull(root), 450, 400);
        editItemStage.getIcons().add(new Image(Objects.requireNonNull(AddItemUI.class.getResource("image/icon.png")).toString()));
        editItemStage.setTitle("Editing item...");
        editItemStage.setScene(scene);
        editItemStage.setResizable(false);
        editItemStage.show();
        stage = editItemStage;
    }

    public static void closeAddItemStage() {
        stage.close();
        stage = null;
    }
}
