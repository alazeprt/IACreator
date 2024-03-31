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
import java.util.Objects;
import java.util.UUID;

public class RecentProject {
    private static Stage createStage;
    private final String namespace;
    private final Path path;
    private final UUID uuid;
    private static final Logger logger = LogManager.getLogger();
    public RecentProject(String namespace, Path path) {
        this.namespace = namespace;
        this.path = path;
        this.uuid = UUID.randomUUID();
    }

    public RecentProject(String namespace, Path path, UUID uuid) {
        this.namespace = namespace;
        this.path = path;
        this.uuid = uuid;
    }

    public static void showCreateStage() {
        if(createStage != null) {
            if(!createStage.isShowing()) {
                showCreateStageHandler();
            }
            createStage.setAlwaysOnTop(true);
        } else {
            showCreateStageHandler();
        }
    }

    private static void showCreateStageHandler() {
        logger.info("Loading Create New Project Page...");
        Stage createProjectStage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(WelcomeUI.class.getResource("NewProject.fxml")));
        } catch (IOException e) {
            logger.fatal("Failed to load Create New Project Page!", e);
        }
        Scene scene = new Scene(Objects.requireNonNull(root), 450, 400);
        createProjectStage.getIcons().add(new Image(Objects.requireNonNull(WelcomeUI.class.getResource("image/icon.png")).toString()));
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
