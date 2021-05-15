package com.minesweeper.MineSweeper;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

public class Save {
    public static void main(String[] args) {
        FileSystemView fsv = FileSystemView.getFileSystemView();

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
        fileChooser.setDialogTitle("请选择文件...");
        fileChooser.setApproveButtonText("确定");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int result = fileChooser.showOpenDialog(null);

        if (JFileChooser.APPROVE_OPTION == result) {
            String path=fileChooser.getSelectedFile().getPath();
            System.out.println(path);
            //此处path为该文件路径
        }
    }
}
