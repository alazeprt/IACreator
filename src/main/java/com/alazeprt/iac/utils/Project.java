package com.alazeprt.iac.utils;

import com.alazeprt.iac.ui.WelcomeUI;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

public class Project {
    private static Stage createStage;
    private final String namespace;
    private final Path path;
    private final UUID uuid;
    private static final Logger logger = LogManager.getLogger();
    public Project(String namespace, Path path) {
        this.namespace = namespace;
        this.path = path;
        this.uuid = UUID.randomUUID();
    }

    public Project(String namespace, Path path, UUID uuid) {
        this.namespace = namespace;
        this.path = path;
        this.uuid = uuid;
    }

    public static void showCreateStage() throws IOException {
        if(createStage != null) {
            if(!createStage.isShowing()) {
                showCreateStageHandler();
            }
            createStage.setAlwaysOnTop(true);
        } else {
            showCreateStageHandler();
        }
    }

    private static void showCreateStageHandler() throws IOException {
        logger.info("Loading Create New Project Page...");
        Stage createProjectStage = new Stage();
        Parent root = FXMLLoader.load(WelcomeUI.class.getResource("NewProject.fxml"));
        Scene scene = new Scene(root, 450, 400);
        createProjectStage.getIcons().add(new Image(WelcomeUI.class.getResource("image/icon.png").toString()));
        createProjectStage.setTitle("New Project...");
        createProjectStage.setScene(scene);
        createProjectStage.setResizable(false);
        createProjectStage.show();
        createStage = createProjectStage;
    }

    public static void closeCreateStage() {
        createStage.close();
        createStage = null;
    }

    public String getNamespace() {
        return namespace;
    }

    public Path getPath() {
        return path;
    }

    public UUID getUuid() {
        return uuid;
    }
}
