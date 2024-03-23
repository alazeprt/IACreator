package com.alazeprt.iac.ui;

import com.alazeprt.iac.config.ApplicationConfig;
import com.alazeprt.iac.config.ProjectConfig;
import com.alazeprt.iac.utils.Project;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class WelcomeController {
    @FXML
    private ImageView iacIcon;

    @FXML
    private ImageView searchIcon;

    @FXML
    private AnchorPane projectListPane;

    public void initialize() {
        iacIcon.setImage(new Image(WelcomeController.class.getResource("image/icon.png").toString()));
        searchIcon.setImage(new Image(WelcomeController.class.getResource("image/search.png").toString()));
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
        if(file != null) {
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
            ProjectUIController.addRecentProjects(project);
            WelcomeUI.closeWelcomeStage();
        }
    }
}
