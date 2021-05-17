package com.minesweeper.MineSweeper;

import java.io.*;

public class Demo1 {
    static int i=0;
    public static void main(String[] args) {
        try{
            BufferedWriter writer=new BufferedWriter(new FileWriter("out.txt"));
            writer.write(11);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            BufferedReader reader=new BufferedReader(new FileReader("out.txt"));
            i=reader.read();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(i);
    }
}
