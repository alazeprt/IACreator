package com.alazeprt.iac.ui;

import com.alazeprt.iac.config.ProjectConfig;
import com.alazeprt.iac.utils.Item;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;

public class EditItemController {

    @FXML
    protected TextField displayName;

    @FXML
    protected TextField resourceLocation;

    @FXML
    private ImageView folderIcon;

    @FXML
    private Label locationWarn;

    protected static Item item;

    public void initialize() {
        folderIcon.setImage(new Image(AddItemController.class.getResource("image/folder.png").toString()));
        displayName.setText(item.getName());
        resourceLocation.setText(item.getResource().toString());
    }

    public void onChangePath() {
        File file = new File(resourceLocation.getText());
        if(file.exists() && file.isFile()) {
            locationWarn.setVisible(false);
            return;
        }
        locationWarn.setVisible(true);
    }

    public void remove() {
        ProjectUI.iaConfig.removeItemConfig(item);
        ProjectConfig.removeProjectObject(item, ProjectUI.iaConfig);
        ProjectController.updateObject();
        EditItemUI.closeAddItemStage();
    }

    public void cancel() {
        item = null;
        EditItemUI.closeAddItemStage();
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

    public void confirm() {
        if(displayName.getText().isEmpty() || locationWarn.isVisible()) {
            return;
        }
        ProjectUI.iaConfig.removeItemConfig(item);
        ProjectConfig.removeProjectObject(item, ProjectUI.iaConfig);
        item.setName(displayName.getText());
        item.setResource(Path.of(resourceLocation.getText()));
        ProjectUI.iaConfig.writeItemConfig(item);
        ProjectConfig.writeProjectObject(item, ProjectUI.iaConfig);
        ProjectController.updateObject();
        EditItemUI.closeAddItemStage();
    }
}
