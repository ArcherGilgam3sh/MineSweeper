package com.minesweeper.MineSweeper;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class Player extends JFrame {
    public static void main(String args[]) {
        bf();
    }
    public static void bf(){
        try {
            URL cb;
            File f = new File("bgm/tankeyidon.wav"); //引号里面的是音乐文件所在的绝对路径
            cb = f.toURL();
            AudioClip aau;
            aau = Applet.newAudioClip(cb);//加载音频
            aau.play(); //播放音频
            Player frame=new Player();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}