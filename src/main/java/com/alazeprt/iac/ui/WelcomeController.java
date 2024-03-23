package com.alazeprt.iac.ui;

import com.alazeprt.iac.config.ApplicationConfig;
import com.alazeprt.iac.config.ProjectConfig;
import com.alazeprt.iac.utils.Project;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static com.alazeprt.iac.ui.ProjectUIController.projectCount;

public class WelcomeController {
    private static final Logger logger = LoggerFactory.getLogger(WelcomeController.class.toString());

    @FXML
    private TextField projectFilter;

    @FXML
    private ImageView iacIcon;

    @FXML
    private AnchorPane projectListPane;

    public void initialize() {
        iacIcon.setImage(new Image(WelcomeController.class.getResource("image/icon.png").toString()));
        ProjectUIController.injectProjectListPane(projectListPane);
        ApplicationConfig.initializeContent();
    }

    public void onCreateProject() throws IOException {
        logger.info("Opening new project creation stage...");
        Project.showCreateStage();
    }

    public void onOpenProject() throws IOException {
        logger.info("Opening project chooser stage...");
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose a folder...");
        File file = chooser.showDialog(new Stage());
        if(file != null) {
            logger.info("Opening project: " + file.getAbsolutePath());
            Project project = ProjectConfig.getProject(file.getAbsolutePath());
            if(project == null) {
                project = new Project(file.getName(), Path.of(file.getAbsolutePath()));
                ProjectConfig.create(project);
                ApplicationConfig.writeRecentContent(project);
            } else if(!ApplicationConfig.existRecentProject(project)) {
                System.out.println(2);
                ApplicationConfig.writeRecentContent(project);
            }
            MainUI.showMainStage(file.getAbsolutePath());
            ProjectUIController.addProjects(project);
            WelcomeUI.closeWelcomeStage();
        }
    }

    public static void openProject(Project project) throws IOException {
        logger.info("Opening project: " + project.getNamespace());
        MainUI.showMainStage(project.getPath().toString());
        WelcomeUI.closeWelcomeStage();
    }

    public void onSearch() {
        projectListPane.getChildren().clear();
        projectListPane.setPrefHeight(515);
        List<Project> projectList = ApplicationConfig.getProjects(projectFilter.getText());
        projectCount = 0;
        for(Project project : projectList) {
            ProjectUIController.addProjects(project);
        }
    }
}
