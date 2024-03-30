package com.alazeprt.iac.ui;

import com.alazeprt.iac.config.ApplicationConfig;
import com.alazeprt.iac.config.ProjectConfig;
import com.alazeprt.iac.utils.RecentProject;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
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

    @FXML
    private TreeView<String> folderTree;
    private static final Logger logger = LogManager.getLogger();

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
        generateTreeView(new File(ProjectUI.path));
        splitPane.getStylesheets().add(ProjectUI.class.getResource("style/MainPage.css").toString());
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
            CreateProjectController.addProjects(recentProject);
            WelcomeUI.closeWelcomeStage();
        }
    }


}
