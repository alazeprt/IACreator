package com.alazeprt.iac.ui;

import com.alazeprt.iac.config.ApplicationConfig;
import com.alazeprt.iac.config.ProjectConfig;
import com.alazeprt.iac.utils.RecentProject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
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

    private static AnchorPane projectListPane;

    protected static int projectCount = 0;
    private static final Logger logger = LogManager.getLogger();

    @FXML
    private TextField folder;

    public void initialize() {
        folderIcon.setImage(new Image(CreateProjectController.class.getResource("image/folder.png").toString()));
        nameError.setVisible(true);
    }

    protected static void injectProjectListPane(AnchorPane anchorPane) {
        projectListPane = anchorPane;
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
        if(!ApplicationConfig.existRecentProject(recentProject)) {
            ApplicationConfig.writeRecentContent(recentProject);
        }
        addProjects(recentProject);
        ProjectConfig.create(recentProject);
        ProjectUI.showMainStage(folder.getText(), recentProject.getNamespace());
        RecentProject.closeCreateStage();
        WelcomeUI.closeWelcomeStage();
    }

    public void cancel() {
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
        Pattern pattern = Pattern.compile("^[0-9a-zA-Z_]{1,}$");
        nameError.setVisible(!pattern.matcher(namespace.getText()).matches());
    }

    public static void addProjects(RecentProject recentProject) {
        logger.debug("Adding recentProject: " + recentProject.getNamespace());
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
        Label projectName = new Label(recentProject.getNamespace());
        projectName.setLayoutX(14);
        projectName.setLayoutY(14);
        projectName.setFont(Font.font("System", FontWeight.BOLD, 24));
        projectName.setTextFill(Paint.valueOf("#27374d"));
        projectName.setMaxWidth(300);
        anchorPane.getChildren().add(projectName);
        Label projectPath = new Label(recentProject.getPath().toString());
        projectPath.setLayoutX(14);
        projectPath.setLayoutY(67);
        projectPath.setTextFill(Paint.valueOf("#9db2bf"));
        projectPath.setMaxWidth(450);
        anchorPane.getChildren().add(projectPath);
        ImageView removeImage = new ImageView(new Image(CreateProjectController.class.getResource("image/remove.png").toString()));
        removeImage.setLayoutX(480);
        removeImage.setLayoutY(35);
        removeImage.setFitWidth(30);
        removeImage.setFitHeight(30);
        anchorPane.getChildren().add(removeImage);
        removeImage.setMouseTransparent(true);
        Button removeButton = new Button();
        removeButton.setStyle("-fx-background-color: rgba(0,0,0,0)");
        removeButton.setLayoutX(480);
        removeButton.setLayoutY(35);
        removeButton.setPrefWidth(30);
        removeButton.setPrefHeight(30);
        removeButton.setOnAction(actionEvent -> {
            ApplicationConfig.unwriteRecentContent(recentProject.getUuid());
            projectListPane.getChildren().clear();
            projectListPane.setPrefHeight(515);
            List<RecentProject> recentProjectList = ApplicationConfig.getProjects("");
            projectCount = 0;
            for(RecentProject recentProject2 : recentProjectList) {
                CreateProjectController.addProjects(recentProject2);
            }
        });
        anchorPane.getChildren().add(removeButton);
        anchorPane.setOnMouseClicked(event -> {
            if(event.getX() >= 480 && event.getX() <= 510 && event.getY() >= 35 && event.getY() <= 65) {
                return;
            }
            WelcomeController.openProject(recentProject);
        });
        projectListPane.getChildren().add(anchorPane);
        projectCount++;
    }
}
