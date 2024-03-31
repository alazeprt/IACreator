package com.alazeprt.iac.ui;

import com.alazeprt.iac.config.ApplicationConfig;
import com.alazeprt.iac.config.IAConfig;
import com.alazeprt.iac.config.ProjectConfig;
import com.alazeprt.iac.utils.IAObject;
import com.alazeprt.iac.utils.ImageObject;
import com.alazeprt.iac.utils.Item;
import com.alazeprt.iac.utils.RecentProject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.Queue;

public class ProjectController {

    @FXML
    private MenuItem functionCopy;

    @FXML
    private MenuItem functionPaste;

    @FXML
    private MenuItem functionCut;

    @FXML
    private SplitPane splitPane;

    @FXML
    private AnchorPane objectsPane;

    private static AnchorPane injectObjectsPane;

    @FXML
    private TreeView<String> folderTree;
    private static final Logger logger = LogManager.getLogger();
    private static int objectCount = 0;

    private void generateTreeView(File dirPath) {
        logger.info("Generating folder tree view...");
        TreeItem<String> root = new TreeItem<>(dirPath.getName());
        Queue<File> queue = new LinkedList<>();
        queue.offer(dirPath);
        while (!queue.isEmpty()) {
            File folder = queue.poll();
            String relativePath = toRelativePath(folder, dirPath);
            TreeItem<String> selectedRoot = root;
            for(int i = 0; i < relativePath.split("/").length; i++) {
                if(relativePath.isEmpty()) {
                    break;
                }
                String name = relativePath.split("/")[i];
                TreeItem<String> thisItem = new TreeItem<>(name);
                if(i == relativePath.split("/").length - 1) {
                    selectedRoot.getChildren().add(thisItem);
                } else {
                    boolean changed = false;
                    for(TreeItem<String> treeItem : selectedRoot.getChildren()) {
                        if(treeItem.getValue().equals(name)) {
                            selectedRoot = treeItem;
                            changed = true;
                            break;
                        }
                    }
                    if(!changed) {
                        selectedRoot.getChildren().add(thisItem);
                    }
                }
                if(folder.listFiles() == null) {
                    continue;
                }
                for(File file : folder.listFiles()) {
                    if(file.isFile()) {
                        thisItem.getChildren().add(new TreeItem<>(file.getName()));
                    }
                }
            }
            if(folder.listFiles() == null) {
                continue;
            }
            for (File child : folder.listFiles()) {
                if (child.isDirectory()) {
                    queue.offer(child);
                }
            }
        }

        folderTree.setRoot(root);
    }

    public void initialize() {
        generateTreeView(ProjectUI.iaConfig.getRoot());
        injectPane(objectsPane);
        ProjectConfig.readProjectObject(ProjectUI.iaConfig).forEach(ProjectController::addObject);
        splitPane.getStylesheets().add(ProjectUI.class.getResource("style/MainPage.css").toString());
    }

    private static void injectPane(AnchorPane anchorPane) {
        injectObjectsPane = anchorPane;
    }

    private String toRelativePath(File path, File root) {
        URI path1 = path.toURI();
        URI path2 = root.toURI();
        URI relativePath = path2.relativize(path1);
        return relativePath.getPath();
    }

    public void onExit() {
        System.exit(0);
    }

    public void onCloseProject() {
        logger.info("Closing project...");
        CreateProjectController.projectCount = 0;
        ProjectController.objectCount = 0;
        ProjectUI.closeMainStage();
        WelcomeUI.start();
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
            WelcomeController.addProjects(recentProject);
            WelcomeUI.closeWelcomeStage();
        }
    }

    public static void addObject(IAObject clazz) {
        logger.debug("Adding object: " + clazz.getName());
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefHeight(150);
        anchorPane.setPrefWidth(200);
        int x = (objectCount % 3) * 240 + 40;
        int y = (objectCount / 3) * 170 + 14;
        int height = (objectCount / 3) * 170 + 170 + 14;
        if(injectObjectsPane.getPrefHeight() < height) {
            injectObjectsPane.setPrefHeight(height);
        }
        anchorPane.setLayoutX(x);
        anchorPane.setLayoutY(y);
        anchorPane.setStyle("-fx-background-color: rgba(0,0,0,0);");
        anchorPane.setStyle("-fx-border-color: #526D82;");
        Label objectName = new Label(clazz.getName());
        objectName.setLayoutX(14);
        objectName.setLayoutY(14);
        objectName.setFont(Font.font("System", FontWeight.BOLD, 24));
        objectName.setTextFill(Paint.valueOf("#27374d"));
        objectName.setMaxWidth(300);
        if(clazz instanceof ImageObject) {
            File file = ((ImageObject) clazz).getResource().toFile();
            InputStream stream = null;
            try {
                stream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                logger.error("Failed to read image file!", e);
            }
            ImageView imageView = new ImageView(new Image(stream));
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);
            imageView.setLayoutX(14);
            imageView.setLayoutY(14);
            anchorPane.getChildren().add(imageView);
            objectName.setLayoutX(74);
        }
        anchorPane.getChildren().add(objectName);
        Label objectType = new Label("Type: " + clazz.getType().name());
        objectType.setLayoutX(14);
        objectType.setLayoutY(117);
        objectType.setTextFill(Paint.valueOf("#9db2bf"));
        objectType.setMaxWidth(450);
        anchorPane.getChildren().add(objectType);
        injectObjectsPane.getChildren().add(anchorPane);
        objectCount++;
    }

    public void onAddItem() {
        AddItemUI.showAddItemStage();
    }
}
