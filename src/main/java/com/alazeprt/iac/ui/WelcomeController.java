package com.alazeprt.iac.ui;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class WelcomeController {
    @FXML
    public ImageView iacIcon;

    @FXML
    public ImageView searchIcon;

    @FXML
    private VBox projectListPane;

    public void initialize() {
        iacIcon.setImage(new Image(WelcomeController.class.getResource("image/icon.png").toString()));
        searchIcon.setImage(new Image(WelcomeController.class.getResource("image/search.png").toString()));
    }
}
