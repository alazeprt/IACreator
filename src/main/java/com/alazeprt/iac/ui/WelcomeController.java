package com.alazeprt.iac.ui;

import com.alazeprt.iac.config.ApplicationConfig;
import com.alazeprt.iac.config.ProjectConfig;
import com.alazeprt.iac.utils.RecentProject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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

import static com.alazeprt.iac.ui.CreateProjectController.projectCount;

public class WelcomeController {

    @FXML
    private TextField projectFilter;

    @FXML
    private ImageView iacIcon;

    @FXML
    private AnchorPane projectListPane;
    private static final Logger logger = LogManager.getLogger();

    public static void openProject(RecentProject recentProject) {
        ProjectUI.showMainStage(recentProject.getPath().toString(), recentProject.getNamespace());
        WelcomeUI.closeWelcomeStage();
    }

    public void initialize() {
        iacIcon.setImage(new Image(WelcomeController.class.getResource("image/icon.png").toString()));
        CreateProjectController.injectProjectListPane(projectListPane);
        ApplicationConfig.initializeContent();
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
            } else if (!ApplicationConfig.existRecentProject(recentProject)) {
                ApplicationConfig.writeRecentContent(recentProject);
            }
            ProjectUI.showMainStage(file.getAbsolutePath(), recentProject.getNamespace());
            CreateProjectController.addProjects(recentProject);
            WelcomeUI.closeWelcomeStage();
        }
    }

    public void onSearch() {
        projectListPane.getChildren().clear();
        projectListPane.setPrefHeight(515);
        List<RecentProject> recentProjectList = ApplicationConfig.getProjects(projectFilter.getText());
        projectCount = 0;
        for (RecentProject recentProject : recentProjectList) {
            CreateProjectController.addProjects(recentProject);
        }
    }

    public void onViewAbout() {
        AboutUIController.showAboutStage();
    }

    public void openGithub() {
        logger.info("Opening GitHub Link...");
        new Thread(() -> {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/alazeprt/IACreator"));
            } catch (IOException | URISyntaxException e) {
                logger.error("Failed to open GitHub Link!", e);
            }
        }).start();
    }

    public void openIssues() {
        logger.info("Opening Issues Tracker...");
        new Thread(() -> {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/alazeprt/IACreator/issues"));
            } catch (IOException | URISyntaxException e) {
                logger.error("Failed to open Issues Tracker!", e);
            }
        }).start();
    }
}
