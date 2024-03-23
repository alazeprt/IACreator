package com.alazeprt.iac.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class WarningUI {
    protected static Stage warningStage;
    protected static String message;
    protected static Runnable continueTask;
    private static Runnable cancelTask;

    public static void showWarningStage(String message, Runnable continueTask, Runnable cancelTask) throws IOException {
        WarningUI.message = message;
        WarningUI.continueTask = continueTask;
        WarningUI.cancelTask = cancelTask;
        if(warningStage != null) {
            if(!warningStage.isShowing()) {
                showWarningStageHandler();
            }
            warningStage.setAlwaysOnTop(true);
        } else {
            showWarningStageHandler();
        }
    }

    private static void showWarningStageHandler() throws IOException {
        Stage stage = new Stage();
        stage.getIcons().add(new Image(WarningUI.class.getResource("image/icon.png").toString()));
        Parent root = FXMLLoader.load(WarningUI.class.getResource("WarningWindow.fxml"));
        Scene scene = new Scene(root, 300, 200);
        stage.setTitle("Warning");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        warningStage = stage;
    }
}
