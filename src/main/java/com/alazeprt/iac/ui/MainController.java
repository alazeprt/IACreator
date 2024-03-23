package com.alazeprt.iac.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.io.File;
import java.net.URI;
import java.util.LinkedList;
import java.util.Queue;

public class MainController {
    @FXML
    public TreeView<String> folderTree;

    private void generateTreeView(File dirPath) {
        TreeItem<String> root = new TreeItem<>(dirPath.getName());

        // 创建队列
        Queue<File> queue = new LinkedList<>();

        // 将根文件夹入队
        queue.offer(dirPath);

        // 循环遍历队列
        while (!queue.isEmpty()) {
            // 从队列中取出队首元素
            File folder = queue.poll();
            String relativePath = toRelativePath(folder, dirPath);
            // 访问队首元素
            System.out.println(relativePath);
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
                for(File file : folder.listFiles()) {
                    if(file.isFile()) {
                        thisItem.getChildren().add(new TreeItem<>(file.getName()));
                    }
                }
            }
            System.out.println(3);
            // 将队首元素的所有子文件夹入队
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
    }

    private String toRelativePath(File path, File root) {
        //将绝对路径转换为URI
        URI path1 = path.toURI();
        URI path2 = root.toURI();

        //从两个路径创建相对路径
        URI relativePath = path2.relativize(path1);

        //将URI转换为字符串
        return relativePath.getPath();
    }

//    public static void main(String[] args) {
//        generateTreeView(new File("."));
//    }
}
