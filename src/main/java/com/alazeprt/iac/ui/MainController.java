package com.alazeprt.iac.ui;

import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URI;
import java.util.LinkedList;
import java.util.Queue;

public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class.toString());

    @FXML
    private SplitPane splitPane;

    @FXML
    private AnchorPane objectsPane;

    @FXML
    private TreeView<String> folderTree;

    private void generateTreeView(File dirPath) {
        logger.info("Generating tree view...");
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
        generateTreeView(new File(MainUI.path));
        splitPane.getStylesheets().add(MainUI.class.getResource("style/MainPage.css").toString());
    }

    private String toRelativePath(File path, File root) {
        URI path1 = path.toURI();
        URI path2 = root.toURI();
        URI relativePath = path2.relativize(path1);
        return relativePath.getPath();
    }
}
