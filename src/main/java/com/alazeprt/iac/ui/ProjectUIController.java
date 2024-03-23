package com.alazeprt.iac.ui;

import com.alazeprt.iac.config.ApplicationConfig;
import com.alazeprt.iac.config.ProjectConfig;
import com.alazeprt.iac.utils.Project;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.regex.Pattern;

public class ProjectUIController {
    private static final Logger logger = LoggerFactory.getLogger(ProjectUIController.class.toString());

    @FXML
    private ImageView folderIcon;

    @FXML
    private Label nameError;

    @FXML
    private Label locationWarn;

    @FXML
    private TextField namespace;

    private static AnchorPane projectListPane;

    protected static int projectCount = 0;

    @FXML
    private TextField folder;

    public void initialize() {
        folderIcon.setImage(new Image(ProjectUIController.class.getResource("image/folder.png").toString()));
        nameError.setVisible(true);
    }

    protected static void injectProjectListPane(AnchorPane anchorPane) {
        projectListPane = anchorPane;
    }

    public void create() throws IOException {
        logger.info("Creating new project...");
        if(nameError.isVisible()) {
            return;
        }
        if(namespace.getText().isEmpty() || folder.getText().isEmpty()) {
            return;
        }
        if(locationWarn.isVisible()) {
            WarningUI.showWarningStage("The location looks like a directory rather than a file name. " +
                    "Are you sure you want to continue?", () -> {
                locationWarn.setVisible(false);
                try {
                    create();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, () -> locationWarn.setVisible(true));
        }
        Path path = Path.of(folder.getText());
        if(!path.toFile().exists() || !path.toFile().isDirectory()) {
            path.toFile().mkdirs();
        }
        Project project = new Project(namespace.getText(), Path.of(folder.getText()));
        ApplicationConfig.writeRecentContent(project);
        addProjects(project);
        ProjectConfig.create(project);
        MainUI.showMainStage(folder.getText());
        Project.closeCreateStage();
        WelcomeUI.closeWelcomeStage();
    }

    public void cancel() {
        logger.info("Cancelling project creation...");
        Project.closeCreateStage();
    }

    public void openFolderChooser() {
        logger.info("Opening project's folder chooser stage...");
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
        if(file.getName().contains(".")) {
            locationWarn.setVisible(true);
        } else {
            locationWarn.setVisible(false);
        }
    }

    public void onInputName() {
        Pattern pattern = Pattern.compile("^[0-9a-zA-Z_]{1,}$");
        if(pattern.matcher(namespace.getText()).matches()) {
            nameError.setVisible(false);
        } else {
            nameError.setVisible(true);
        }
    }

    public static void addProjects(Project project) {
        logger.info("Adding recent project to welcome page: " + project.getNamespace());
        if(projectCount > 4) {
            int addHeight = 105 + 120 * (projectCount - 5);
            projectListPane.setPrefHeight(projectListPane.getPrefHeight() + addHeight);
        }
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefWidth(525);
        anchorPane.setPrefHeight(100);
        anchorPane.setLayoutX(23);
        anchorPane.setLayoutY(20 + 120 * projectCount);
        anchorPane.setStyle("-fx-border-color: #526D82;");
        Label projectName = new Label(project.getNamespace());
        projectName.setLayoutX(14);
        projectName.setLayoutY(14);
        projectName.setFont(Font.font("System", FontWeight.BOLD, 24));
        projectName.setTextFill(Paint.valueOf("#27374d"));
        projectName.setMaxWidth(300);
        anchorPane.getChildren().add(projectName);
        Label projectPath = new Label(project.getPath().toString());
        projectPath.setLayoutX(14);
        projectPath.setLayoutY(67);
        projectPath.setTextFill(Paint.valueOf("#9db2bf"));
        projectPath.setMaxWidth(450);
        anchorPane.getChildren().add(projectPath);
        anchorPane.setOnMouseClicked(event -> {
            try {
                WelcomeController.openProject(project);
            } catch (IOException e) {
                logger.error("Failed to open project: " + e);
                e.printStackTrace();
            }
        });
        projectListPane.getChildren().add(anchorPane);
        projectCount++;
    }
}
