package com.alazeprt.iac.ui;

import com.alazeprt.iac.config.ApplicationConfig;
import com.alazeprt.iac.config.ProjectConfig;
import com.alazeprt.iac.utils.Project;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;

import static com.alazeprt.iac.ui.ProjectUIController.projectCount;

public class WelcomeController {

    @FXML
    private TextField projectFilter;

    @FXML
    private ImageView iacIcon;

    @FXML
    private AnchorPane projectListPane;

    public static void openProject(Project project) throws IOException {
        MainUI.showMainStage(project.getPath().toString(), project.getNamespace());
        WelcomeUI.closeWelcomeStage();
    }

    public void initialize() {
        iacIcon.setImage(new Image(WelcomeController.class.getResource("image/icon.png").toString()));
        ProjectUIController.injectProjectListPane(projectListPane);
        ApplicationConfig.initializeContent();
    }

    public void onCreateProject() throws IOException {
        Project.showCreateStage();
    }

    public void onOpenProject() throws IOException {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose a folder...");
        File file = chooser.showDialog(new Stage());
        if (file != null) {
            Project project = ProjectConfig.getProject(file.getAbsolutePath());
            if (project == null) {
                project = new Project(file.getName(), Path.of(file.getAbsolutePath()));
                ProjectConfig.create(project);
                ApplicationConfig.writeRecentContent(project);
            } else if (!ApplicationConfig.existRecentProject(project)) {
                System.out.println(2);
                ApplicationConfig.writeRecentContent(project);
            }
            MainUI.showMainStage(file.getAbsolutePath(), project.getNamespace());
            ProjectUIController.addProjects(project);
            WelcomeUI.closeWelcomeStage();
        }
    }

    public void onSearch() {
        projectListPane.getChildren().clear();
        projectListPane.setPrefHeight(515);
        List<Project> projectList = ApplicationConfig.getProjects(projectFilter.getText());
        projectCount = 0;
        for (Project project : projectList) {
            ProjectUIController.addProjects(project);
        }
    }

    public void onViewAbout() throws IOException {
        AboutUIController.showAboutStage();
    }

    public void openGithub() {
        new Thread(() -> {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/alazeprt/IACreator"));
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public void openIssues() {
        new Thread(() -> {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/alazeprt/IACreator/issues"));
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
