package com.alazeprt.iac.ui;

import com.alazeprt.iac.config.ApplicationConfig;
import com.alazeprt.iac.config.ProjectConfig;
import com.alazeprt.iac.utils.RecentProject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;
import java.util.regex.Pattern;

public class CreateProjectController {

    @FXML
    private ImageView folderIcon;

    @FXML
    private Label nameError;

    @FXML
    private Label locationWarn;

    @FXML
    private TextField namespace;

    protected static int projectCount = 0;

    @FXML
    private TextField folder;

    public void initialize() {
        folderIcon.setImage(new Image(Objects.requireNonNull(CreateProjectController.class.getResource("image/folder.png")).toString()));
        nameError.setVisible(true);
    }

    public void create() {
        if(nameError.isVisible()) {
            return;
        }
        if(namespace.getText().isEmpty() || folder.getText().isEmpty()) {
            return;
        }
        if(locationWarn.isVisible()) {
            locationWarn.setVisible(false);
        }
        Path path = Path.of(folder.getText());
        if(!path.toFile().exists() || !path.toFile().isDirectory()) {
            path.toFile().mkdirs();
        }
        RecentProject recentProject = new RecentProject(namespace.getText(), Path.of(folder.getText()));
        if(ApplicationConfig.existRecentProject(recentProject)) {
            ApplicationConfig.writeRecentContent(recentProject);
        }
        WelcomeController.addProjects(recentProject);
        ProjectConfig.create(recentProject);
        ProjectUI.showMainStage(folder.getText(), recentProject.getNamespace());
        RecentProject.closeCreateStage();
        WelcomeUI.closeWelcomeStage();
    }

    public void cancel() {
        namespace.clear();
        folder.clear();
        RecentProject.closeCreateStage();
    }

    public void openFolderChooser() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose a folder...");
        File file = chooser.showDialog(new Stage());
        if(file != null) {
            folder.setText(file.getAbsolutePath());
            onChangeFolder();
        }
    }

    public void onChangeFolder() {
        File file = new File(folder.getText());
        if(file.isDirectory()) {
            locationWarn.setVisible(false);
            return;
        }
        locationWarn.setVisible(file.getName().contains("."));
    }

    public void onInputName() {
        Pattern pattern = Pattern.compile("^[0-9a-zA-Z_]+$");
        nameError.setVisible(!pattern.matcher(namespace.getText()).matches());
    }
}
