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

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static com.alazeprt.iac.ui.CreateProjectController.projectCount;

public class WelcomeController {

    @FXML
    private TextField projectFilter;

    @FXML
    private ImageView iacIcon;

    @FXML
    private AnchorPane projectListPane;
    private static AnchorPane staticProjectListPane;
    private static final Logger logger = LogManager.getLogger();

    public static void openProject(RecentProject recentProject) {
        ProjectUI.showMainStage(recentProject.getPath().toString(), recentProject.getNamespace());
        WelcomeUI.closeWelcomeStage();
    }

    public void initialize() {
        iacIcon.setImage(new Image(Objects.requireNonNull(WelcomeController.class.getResource("image/icon.png")).toString()));
        injectProjectListPane(projectListPane);
        ApplicationConfig.initializeContent();
    }

    private static void injectProjectListPane(AnchorPane pane) {
        staticProjectListPane = pane;
    }

    public void onCreateProject() {
        RecentProject.showCreateStage();
    }

    public void onOpenProject() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose a folder...");
        File file = chooser.showDialog(new Stage());
        if (file != null) {
            RecentProject recentProject = ProjectConfig.getProject(file.getAbsolutePath());
            if (recentProject == null) {
                recentProject = new RecentProject(file.getName(), Path.of(file.getAbsolutePath()));
                ProjectConfig.create(recentProject);
                ApplicationConfig.writeRecentContent(recentProject);
            } else if (ApplicationConfig.existRecentProject(recentProject)) {
                ApplicationConfig.writeRecentContent(recentProject);
            }
            ProjectUI.showMainStage(file.getAbsolutePath(), recentProject.getNamespace());
            WelcomeController.addProjects(recentProject);
            WelcomeUI.closeWelcomeStage();
        }
    }

    public void onSearch() {
        projectListPane.getChildren().clear();
        projectListPane.setPrefHeight(515);
        List<RecentProject> recentProjectList = ApplicationConfig.getProjects(projectFilter.getText());
        projectCount = 0;
        for (RecentProject recentProject : recentProjectList) {
            WelcomeController.addProjects(recentProject);
        }
    }

    public void onViewAbout() {
        AboutUIController.showAboutStage();
    }

    public void openGithub() {
        logger.info("Try opening GitHub Link...");
        new Thread(() -> {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/alazeprt/IACreator"));
            } catch (IOException | URISyntaxException e) {
                logger.error("Failed to open GitHub Link!", e);
            }
        }).start();
    }

    public void openIssues() {
        logger.info("Try opening Issues Tracker...");
        new Thread(() -> {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/alazeprt/IACreator/issues"));
            } catch (IOException | URISyntaxException e) {
                logger.error("Failed to open Issues Tracker!", e);
            }
        }).start();
    }

    public static void addProjects(RecentProject recentProject) {
        logger.debug("Adding recentProject: " + recentProject.getNamespace());
        if(projectCount > 4) {
            int addHeight = 105 + 120 * (projectCount - 5);
            staticProjectListPane.setPrefHeight(staticProjectListPane.getPrefHeight() + addHeight);
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
        ImageView removeImage = new ImageView(new Image(Objects.requireNonNull(CreateProjectController.class.getResource("image/remove.png")).toString()));
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
            staticProjectListPane.getChildren().clear();
            staticProjectListPane.setPrefHeight(515);
            List<RecentProject> recentProjectList = ApplicationConfig.getProjects("");
            projectCount = 0;
            for(RecentProject recentProject2 : recentProjectList) {
                addProjects(recentProject2);
            }
        });
        anchorPane.getChildren().add(removeButton);
        anchorPane.setOnMouseClicked(event -> {
            if(event.getX() >= 480 && event.getX() <= 510 && event.getY() >= 35 && event.getY() <= 65) {
                return;
            }
            WelcomeController.openProject(recentProject);
        });
        staticProjectListPane.getChildren().add(anchorPane);
        projectCount++;
    }
}
