package com.minesweeper.MineSweeper;

import javax.swing.*;
import java.awt.*;

public class SaoLei {
    JFrame frame=new JFrame();
    ImageIcon bannerIcon=new ImageIcon("banner.png");//头部图片（可用于reset）
    JButton bannerBtn=new JButton(bannerIcon);
    JLabel label1=new JLabel("待开："+80);
    JLabel label2=new JLabel("已开："+0);
    JLabel label3=new JLabel("用时："+2+"s");

    public SaoLei(){
        frame.setSize(600,700);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        setHeader();

        frame.setVisible(true);
    }

    //设计框体头部
    public void setHeader(){
        JPanel panel = new JPanel(new GridBagLayout());//设置画布

        GridBagConstraints c1=new GridBagConstraints(0,0,3,1,1.0,1.0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
        panel.add(bannerBtn,c1);

        label1.setOpaque(true);//设置透明度-不透明
        label1.setBackground(Color.white);//设置背景色
        label1.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));//设置边框

        label2.setOpaque(true);
        label2.setBackground(Color.white);
        label2.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        label3.setOpaque(true);
        label3.setBackground(Color.white);
        label3.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        bannerBtn.setOpaque(true);
        bannerBtn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        bannerBtn.setBackground(Color.white);

        GridBagConstraints c2=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
        GridBagConstraints c3=new GridBagConstraints(1,1,1,1,1.0,1.0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
        GridBagConstraints c4=new GridBagConstraints(2,1,1,1,1.0,1.0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);

        panel.add(label1,c2);
        panel.add(label2,c3);
        panel.add(label3,c4);

        frame.add(panel,BorderLayout.NORTH);
    }

    public static void main(String[] args) {
        new SaoLei();
    }
}
