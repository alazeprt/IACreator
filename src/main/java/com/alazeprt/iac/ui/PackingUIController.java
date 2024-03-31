package com.alazeprt.iac.ui;

import com.alazeprt.iac.config.ProjectConfig;
import com.alazeprt.iac.utils.IAObject;
import com.alazeprt.iac.utils.ImageObject;
import com.alazeprt.iac.utils.Item;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

public class PackingUIController {
    @FXML
    private Button OK;

    @FXML
    private Button cancel;

    @FXML
    private Button explorer;

    @FXML
    private Label action;

    @FXML
    private ProgressBar progress;

    private static ProgressBar progressBar;

    private static Label action_;

    private static Button OK_;

    private static Button cancel_;

    private static Button explorer_;

    protected static Path path;

    protected static String namespace;

    private static PackingThread packingThread;

    private static final Logger logger = LogManager.getLogger();

    public void initialize() {
        OK.setDisable(true);
        explorer.setDisable(true);
        inject(progress, action, OK, cancel, explorer);
        packingThread = new PackingThread();
        packingThread.start();
    }

    private static void inject(ProgressBar progress, Label label, Button ok, Button cancel, Button explorer) {
        OK_ = ok;
        cancel_ = cancel;
        explorer_ = explorer;
        progressBar = progress;
        action_ = label;
    }

    public void onClickOK() {
        packingThread = null;
        progress.setProgress(0);
        path = null;
        PackingUI.closePackingStage();
    }

    public void onOpenExplorer() {
        new Thread(() -> {
            try {
                Desktop.getDesktop().open(path.toFile());
            } catch (IOException e) {
                logger.error("Failed to open folder!", e);
            }
        });
    }

    public void onCancel() {
        packingThread.stop();
        packingThread = null;
        progress.setProgress(0);
        path = null;
        PackingUI.closePackingStage();
    }

    static class PackingThread extends Thread {
        private static int step;
        @Override
        public void run() {
            logger.info("Deleting old artifact...");
            Platform.runLater(() -> action_.setText("Deleting old artifact..."));
            try {
                FileUtils.deleteDirectory(path.resolve("artifacts").toFile());
            } catch (IOException e) {
                logger.error("Failed to delete old artifact!", e);
            }
            logger.info("Deleting old textures...");
            Platform.runLater(() -> action_.setText("Deleting old textures..."));
            try {
                FileUtils.deleteDirectory(path.resolve("textures").toFile());
            } catch (IOException e) {
                logger.error("Failed to delete old artifact!", e);
            }
            List<IAObject> objects = ProjectConfig.readProjectObject(ProjectUI.iaConfig);
            step = 40 / objects.size();
            logger.info("Copying images...");
            Platform.runLater(() -> action_.setText("Preparing to copy images..."));
            for(IAObject object : objects) {
                if(object instanceof Item item) {
                    try {
                        logger.debug("Copying image: " + item.getName());
                        Platform.runLater(() -> {
                            action_.setText("Copying " + item.getName() + "...");
                            progressBar.setProgress(progressBar.getProgress() + step);
                        });
                        path.resolve("textures").resolve("item").toFile().mkdirs();
                        Files.copy(item.getResource(), path.resolve("textures").resolve("item").
                                resolve(item.getName() + ".png"), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        logger.error("Failed to copy image!", e);
                    }
                }
            }
            step = 60 / (Objects.requireNonNull(path.toFile().list()).length - 1);
            logger.info("Copying configs, textures and models...");
            Platform.runLater(() -> action_.setText("Preparing to copy configs, textures and models to artifact's folder..."));
            File file = path.resolve("artifacts").resolve(namespace).toFile();
            file.mkdirs();
            try {
                FileUtils.copyDirectory(path.resolve("configs").toFile(),
                        file.toPath().resolve("configs").toFile());
                FileUtils.copyDirectory(path.resolve("textures").toFile(),
                        file.toPath().resolve("textures").toFile());
                FileUtils.copyDirectory(path.resolve("models").toFile(),
                        file.toPath().resolve("models").toFile());
            } catch (IOException e) {
                logger.error("Failed to copy configs, textures or models!", e);
            }
            logger.info("Packing Finished!");
            Platform.runLater(() -> {
                action_.setText("Finished!");
                OK_.setDisable(false);
                cancel_.setDisable(true);
                explorer_.setDisable(false);
            });
        }
    }
}
