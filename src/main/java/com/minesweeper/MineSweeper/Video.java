package com.minesweeper.MineSweeper;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Video extends javax.swing.JFrame implements Runnable {
    public Video() {
        initComponents();
    }

    String path = "D:/MineSweeper/";
    int idx = 1;

    public void paint(Graphics g) {
        ImageObserver imageObserver = new ImageObserver() {
            @Override
            public boolean imageUpdate(Image img, int infoflags, int x, int y,
                                       int width, int height) {
                return false;
            }
        };
        try {
            String temp = "";
            if (idx <= 9)
                temp = path + "10" + idx + ".png";

            else if (idx >= 10) {
                temp = path + idx + ".jpg";
            }


            g.drawImage(ImageIO.read(new File(temp)), 100, 75, 400, 400,
                    imageObserver);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void initComponents() {
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        jButton1.setText("");
        jButton1.setOpaque(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
                getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(
                javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                javax.swing.GroupLayout.Alignment.TRAILING,
                layout.createSequentialGroup()
                        .addContainerGap(483, Short.MAX_VALUE)
                        .addComponent(jButton1).addGap(35, 35, 35)));
        layout.setVerticalGroup(layout.createParallelGroup(
                javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                layout.createSequentialGroup().addGap(37, 37, 37)
                        .addComponent(jButton1)
                        .addContainerGap(392, Short.MAX_VALUE)));

        pack();
    }// </editor-fold>
    //GEN-END:initComponents


    public static void main(String args[]) {
        Video donttai = new Video();
        donttai.setVisible(true);
        donttai.run();
        donttai.dispose();
    }


    public javax.swing.JButton jButton1;

    @Override
    public void run() {
        // TODO Auto-generated method stub
        int count = 0;
        while (count < 50) {
            repaint();
            if (idx < 7) {
                idx++;
                count++;
            } else {
                idx = 1;
                count++;
            }
            try {
                Thread.sleep(90);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

}