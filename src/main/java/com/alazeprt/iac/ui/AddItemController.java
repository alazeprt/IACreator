package com.alazeprt.iac.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class AddItemController {
    @FXML
    private TextField displayName;

    @FXML
    private TextField resourceLocation;

    @FXML
    private ImageView folderIcon;

    @FXML
    private Label locationWarn;

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
