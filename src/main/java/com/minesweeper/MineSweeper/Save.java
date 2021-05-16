package com.minesweeper.MineSweeper;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.Random;

public class Save {
    public static void main(String[] args) {


        int[][] test=new int[20][20];
        for (int i = 0; i < 20; i++) {
            for (int i1 = 0; i1 < 20; i1++) {
                Random r2=new Random();
                test[i][i1]=r2.nextInt(10);
            }
        }

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                System.out.print(test[i][j]+" ");
            }
            System.out.println();
        }

        String path="";
        FileSystemView fsv = FileSystemView.getFileSystemView();

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
        fileChooser.setDialogTitle("请选择文件...");
        fileChooser.setApproveButtonText("确定");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int result = fileChooser.showOpenDialog(null);

        if (JFileChooser.APPROVE_OPTION == result) {
            path=fileChooser.getSelectedFile().getPath();
            System.out.println(path);
            //此处path为该文件路径
        }




        int[][] test2=new int[20][20];

        try{
            BufferedReader in=new BufferedReader(new FileReader(path));
            for (int i = 0; i < 20; i++) {
                for (int j = 0; j < 20; j++) {
                    test2[i][j]=in.read();
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



        try{
            BufferedWriter writer=new BufferedWriter(new FileWriter("Out.txt"));
            for (int i = 0; i < 20; i++) {
                for (int j = 0; j < 20; j++) {
                    writer.write(test[i][j]);
                }
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
