package com.minesweeper.MineSweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class SaoLei implements ActionListener {
    JFrame frame=new JFrame();
    ImageIcon bannerIcon=new ImageIcon("banner.png");//头部图片（可用于reset）
    ImageIcon guessIcon=new ImageIcon("guess.png");//未开区域的图片
    ImageIcon bombIcon=new ImageIcon("bomb.png");
    ImageIcon failIcon=new ImageIcon("fail.png");
    ImageIcon winIcon=new ImageIcon("win.png");
    ImageIcon win_flagIcon=new ImageIcon("win_flag.png");
    JButton bannerBtn=new JButton(bannerIcon);

    JLabel label1=new JLabel("待开："+80);
    JLabel label2=new JLabel("已开："+0);
    JLabel label3=new JLabel("用时："+2+"s");

    //数据结构
    int ROW=20;//行数
    int COL=20;//列数
    int[][] data=new int[ROW][COL];//记录每格的数据
    JButton[][] buttons=new JButton[ROW][COL];//按钮
    int LeiCount=30;//雷的数量
    int LeiCode=-1;//-1代表是雷
    int unopened=ROW*COL;//未开的数量
    int opened=0;//已开的数量

    public SaoLei(){
        frame.setSize(960,960);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        setHeader();//设置头部

        addLei();//放雷

        setButtons();//设置按钮和未开的图标

        frame.setVisible(true);
    }

    private void addLei(){
        Random rand=new Random();
        for (int i = 0; i < LeiCount; ) {
            int r=rand.nextInt(ROW);//0-19的整数
            int c=rand.nextInt(COL);
            if(data[r][c]!=LeiCode){
                data[r][c]=LeiCode;
                i++;
            }
        }

        //计算周边的雷的数量
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {

                if(data[i][j]==LeiCode)
                    continue;

                int tempCount=0;//周围的雷数
                if (i>0 && j>0 && data[i-1][j-1] == LeiCode) tempCount++;
                if (i>0 && data[i-1][j] == LeiCode) tempCount++;
                if (i>0 && j<19 && data[i-1][j+1] == LeiCode) tempCount++;
                if (j>0 && data[i][j-1] ==  LeiCode) tempCount++;
                if (j<19 && data[i][j+1] == LeiCode) tempCount++;
                if (i<19 && j>0 && data[i+1][j-1] == LeiCode) tempCount++;
                if (i<19 && data[i+1][j] == LeiCode) tempCount++;
                if (i<19 && j<19 && data[i+1][j+1] == LeiCode) tempCount++;

                data[i][j]=tempCount;
            }
        }
    }

    public void setButtons(){
        Container con=new Container();//小容器，可以放入图片和按钮
        con.setLayout(new GridLayout(ROW,COL));//用于排布相同的容器

        for (int i = 0; i < ROW; i++) {
            for (int i1 = 0; i1 < COL; i1++) {
                JButton btn=new JButton(guessIcon);//设置按钮
                btn.setOpaque(true);
                btn.setBackground(new Color(244,183,113));//设置背景色
                btn.addActionListener(this);
                //JButton btn=new JButton(data[i][i1]+"");
                con.add(btn);//将按钮放在容器中
                buttons[i][i1]=btn;//将按钮放入数据结构中
            }
        }

        frame.add(con,BorderLayout.CENTER);//将容器（们）放在中心位置
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



    public void actionPerformed(ActionEvent e) {
        JButton btn=(JButton) e.getSource();
        for (int i = 0; i < ROW; i++) {
            for (int i1 = 0; i1 < COL; i1++) {
                if(btn.equals(buttons[i][i1])){
                    if(data[i][i1]==LeiCode){//判断输赢
                        lose();
                    }else{
                        openCell(i,i1);
                        checkWin();//判断胜利
                    }
                    return;
                }
            }
        }
    }

    private void checkWin(){
        int count=0;
        for(int i=0;i<ROW;i++){
            for(int i1=0;i1<COL;i1++){
                if(buttons[i][i1].isEnabled()) count++;
            }
        }
        if(count==LeiCount){
            for(int i=0;i<ROW;i++){
                for(int i1=0;i1<COL;i1++){
                    if(buttons[i][i1].isEnabled()){
                        buttons[i][i1].setIcon(win_flagIcon);
                    }
                }
            }
            bannerBtn.setIcon(winIcon);
            JOptionPane.showMessageDialog(frame,"你赢了，Yeah\n点击Banner重新开始","赢了",JOptionPane.PLAIN_MESSAGE);
        }

    }

    private void lose(){//踩到雷后爆雷
        bannerBtn.setIcon(failIcon);
        for(int i=0;i<ROW;i++){
            for(int i1=0;i1<COL;i1++){
                if(buttons[i][i1].isEnabled()){
                    JButton btn=buttons[i][i1];
                    if(data[i][i1]==LeiCode){
                        btn.setEnabled(false);
                        btn.setIcon(bombIcon);
                        btn.setDisabledIcon(bombIcon);
                    }else{btn.setIcon(null);//清除icon
                        btn.setEnabled(false);
                        btn.setOpaque(true);//设置不透明
                        btn.setText(data[i][i1]+"");//填入数字
                    }
                }
            }
        }
        JOptionPane.showMessageDialog(frame,"可惜你暴雷了！\n你可以点击上面的Banner重新开始","暴雷啦",JOptionPane.PLAIN_MESSAGE);//显示暴雷提示框
    }



    private void openCell(int i,int j){
        JButton btn=buttons[i][j];
        if(!btn.isEnabled()) return;

        btn.setIcon(null);//清除icon
        btn.setEnabled(false);
        btn.setOpaque(true);//设置不透明
        btn.setBackground(Color.GREEN);//背景换为绿色
        btn.setText(data[i][j]+"");//填入数字

        //实现连续打开
        if(data[i][j] == 0) {
            if (i>0 && j>0 && data[i-1][j-1] == 0) openCell(i-1, j-1);
            if (i>0 && data[i-1][j] == 0) openCell(i-1, j);
            if (i>0 && j<19 && data[i-1][j+1] == 0) openCell(i-1, j+1);
            if (j>0 && data[i][j-1] == 0) openCell(i, j-1);
            if (j<19 && data[i][j+1] == 0) openCell(i, j+1);
            if (i<19 && j>0 && data[i+1][j-1] == 0) openCell(i+1, j-1);
            if (i<19 && data[i+1][j] == 0) openCell(i+1, j);
            if (i<19 && j<19 && data[i+1][j+1] == 0) openCell(i+1, j+1);
        }
    }
}
