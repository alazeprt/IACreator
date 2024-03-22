package com.alazeprt.iac.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;

import java.io.File;

public class MainController {
    @FXML
    public TreeView<String> folderTree;
    private void generateTreeView(File directory) {
        TreeItem<String> rootItem = new TreeItem<>(directory.getName());
        folderTree.setRoot(rootItem);
        folderTree.getStylesheets().add("css/treeview.css");

        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                TreeItem<String> childItem = new TreeItem<>(file.getName());
                rootItem.getChildren().add(childItem);

                generateTreeView(file);
            } else {
                TreeItem<String> childItem = new TreeItem<>(file.getName());
                childItem.setValue(file.getAbsolutePath());
                rootItem.getChildren().add(childItem);
            }
        }

        // 展开所有节点
        folderTree.getRoot().setExpanded(true);

        // 添加双击事件处理
        folderTree.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                TreeItem<String> selectedItem = folderTree.getSelectionModel().getSelectedItem();

                if (selectedItem != null && selectedItem.getValue() != null) {
                    // 打开文件
                    System.out.println("打开文件：" + selectedItem.getValue());
                }
            }
        });
    }

    public void initialize() {
        generateTreeView(new File(MainUI.path));
    }
}
