package com.alazeprt.iac.ui;

import com.alazeprt.iac.config.IAConfig;
import com.alazeprt.iac.config.ProjectConfig;
import com.alazeprt.iac.utils.Item;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Path;

public class AddItemController {
    @FXML
    private TextField displayName;

    @FXML
    private TextField resourceLocation;

    @FXML
    private ImageView folderIcon;

    @FXML
    private Label locationWarn;

    private static final Logger logger = LogManager.getLogger();

    public void initialize() {
        folderIcon.setImage(new Image(AddItemController.class.getResource("image/folder.png").toString()));
    }

    public void openFolderChooser() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select a image...");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image File", "*.png"));
        File file = chooser.showOpenDialog(new Stage());
        if(file != null) {
            resourceLocation.setText(file.getAbsolutePath());
            onChangePath();
        }
    }

    public void create() {
        if(displayName.getText().isEmpty() || resourceLocation.getText().isEmpty()) {
            return;
        }
        if(locationWarn.isVisible()) {
            return;
        }
        logger.info("Creating new item...");
        Item item = new Item(displayName.getText(), Path.of(resourceLocation.getText()));
        ProjectUI.iaConfig.writeItemConfig(item);
        ProjectUI.iaConfig.copyItemResource(item);
        ProjectController.addObject(item);
        AddItemUI.closeAddItemStage();
    }

    public void cancel() {
        displayName.clear();
        resourceLocation.clear();
        AddItemUI.closeAddItemStage();
    }

    public void onChangePath() {
        File file = new File(resourceLocation.getText());
        if(file.exists() && file.isFile()) {
            locationWarn.setVisible(false);
            return;
        }
        locationWarn.setVisible(true);
    }
}
