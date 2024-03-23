package com.alazeprt.iac.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import static com.alazeprt.iac.ui.WarningUI.*;

public class WarningController {
    @FXML
    private Label warningMessage;

    public void continueAction() {
        closeCreateStage(true);
    }

    public void cancelAction() {
        closeCreateStage(false);
    }

    private void closeCreateStage(boolean runTask) {
        warningStage.close();
        warningStage = null;
        if (runTask) {
            continueTask.run();
        }
    }

    public void initialize() {
        warningMessage.setText(message);
    }
}
