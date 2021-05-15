package com.minesweeper.MineSweeper;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Random;

public class SaoLei implements ActionListener, MouseListener {
    JFrame frame = new JFrame();
    ImageIcon bannerIcon = new ImageIcon("banner.png");//头部图片（可用于reset）
    ImageIcon guessIcon = new ImageIcon("guess.png");//未开区域的图片
    ImageIcon bombIcon = new ImageIcon("bomb.png");
    ImageIcon failIcon = new ImageIcon("fail.png");
    ImageIcon winIcon = new ImageIcon("win.png");
    ImageIcon win_flagIcon = new ImageIcon("win_flag.png");
    ImageIcon flagIcon = new ImageIcon("goldAfterOpen.png");
    ImageIcon afterOpen = new ImageIcon("afterOpen.png");
    ImageIcon afterOpen1 = new ImageIcon("1.png");
    ImageIcon afterOpen2 = new ImageIcon("2.png");
    ImageIcon afterOpen3 = new ImageIcon("3.png");
    ImageIcon afterOpen4 = new ImageIcon("4.png");
    ImageIcon afterOpen5 = new ImageIcon("5.png");
    ImageIcon afterOpen6 = new ImageIcon("6.png");
    ImageIcon afterOpen7 = new ImageIcon("7.png");
    ImageIcon afterOpen8 = new ImageIcon("8.png");
    ImageIcon man= new ImageIcon("man.png");
    ImageIcon woman= new ImageIcon("woman.png");


    //数据结构
    int ROW = 20;//行数
    int COL = 20;//列数
    int[][] data = new int[ROW][COL];//记录每格的数据
    boolean[][] canBeOpen = new boolean[ROW][COL];   //!!!! 改变难度后不会随难度改变，要换位置 zfh
    JButton[][] buttons = new JButton[ROW][COL];//按钮
    int LeiCount = 30;//雷的数量
    int LeiCode = -1;//-1代表是雷
    int unopened = ROW * COL;//未开的数量
    int opened = 0;//已开的数量
    int seconds = 0;//计时器的时间
    int actionCount = 0;
    int maxAction = 5;
    int player = 0;
    int clickTimes = 0;//用于判断是否是第一次点击
    int score1 = 0;//玩家1的分数
    int score2 = 0;//玩家2的分数
    int brokenPickaxe1 = 0;//玩家1损坏的⛏
    int brokenPickaxe2 = 0;//玩家1损坏的⛏
    JButton bannerBtn = new JButton(bannerIcon);
    JButton southTestBtn = new JButton(bannerIcon);//调试中 ZFH

    JLabel label1 = new JLabel("待开：" + unopened);
    JLabel label2 = new JLabel("已开：" + opened);
    JLabel label3 = new JLabel("用时：" + seconds + "s");
    Timer timer = new Timer(1000, this);
    Container con=new Container();

    public SaoLei() {
        new Thread(()->{while(true) {playMusic();}
        }).start();

        frame.setSize(1400, 850);//宽度调试中 ZFH
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        //pageJumps();

        setMenu();//设置菜单

        setHeader();//设置头部

        setEast();//调试中 ZFH

        setWest();//调试中 ZFH

        //setSouth();//调试中 ZFH

        addLei();//放雷

        setButtons();//设置按钮和未开的图标

        timer.start();//别忘了最开始也要开始Timer

        frame.setVisible(true);
    }

    private void playMusic() {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("D:/MineSweeper/Music1.wav"));    //绝对路径
            AudioFormat aif = ais.getFormat();
            final SourceDataLine sdl;
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, aif);
            sdl = (SourceDataLine) AudioSystem.getLine(info);
            sdl.open(aif);
            sdl.start();
            FloatControl fc = (FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN);
            // value可以用来设置音量，从0-2.0
            double value = 2;
            float dB = (float) (Math.log(value == 0.0 ? 0.0001 : value) / Math.log(10.0) * 20.0);
            fc.setValue(dB);
            int nByte = 0;
            final int SIZE = 1024 * 64;
            byte[] buffer = new byte[SIZE];
            while (nByte != -1) {
                nByte = ais.read(buffer, 0, SIZE);
                sdl.write(buffer, 0, nByte);
            }
            sdl.stop();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFrame1() {
        JFrame frame1 = new JFrame("Golden Rush");

        frame1.setLayout(null);

        JLabel pic = new JLabel(bannerIcon);
        pic.setBounds(0,0,900,700);//根据图来调整
        frame1.add(pic);

        frame1.setSize(1000, 750);
        frame1.setResizable(false);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setLocationRelativeTo(null);
        frame1.setVisible(true);

        JButton pvp = new JButton();
        JButton pve = new JButton();
        pvp.setBounds(400,500,200,50);
        pve.setBounds(400,600,200,50);
        frame1.add(pvp);
        frame1.add(pve);
        pvp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame1.dispose();
            }
        });
        pve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame1.dispose();
            }
        });



    }

    public void addLei() {
        Random rand = new Random();
        for (int i = 0; i < LeiCount; ) {
            int r = rand.nextInt(ROW);//0-19的整数
            int c = rand.nextInt(COL);
            if (data[r][c] != LeiCode && setTempCount(r, c) != 8) {  //有待改良 目前只能避免3x3雷区的出现 ZFH
                data[r][c] = LeiCode;
                i++;
            }
        }

        //计算周边的雷的数量
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {

                if (data[i][j] == LeiCode)
                    continue;

                /*
                int tempCount = 0;//周围的雷数
                if (i > 0 && j > 0 && data[i - 1][j - 1] == LeiCode) tempCount++;
                if (i > 0 && data[i - 1][j] == LeiCode) tempCount++;
                if (i > 0 && j < 19 && data[i - 1][j + 1] == LeiCode) tempCount++;
                if (j > 0 && data[i][j - 1] == LeiCode) tempCount++;
                if (j < 19 && data[i][j + 1] == LeiCode) tempCount++;
                if (i < 19 && j > 0 && data[i + 1][j - 1] == LeiCode) tempCount++;
                if (i < 19 && data[i + 1][j] == LeiCode) tempCount++;
                if (i < 19 && j < 19 && data[i + 1][j + 1] == LeiCode) tempCount++;

                此部分提取到了方法中  调试中 ZFH
                 */

                int tempCount = setTempCount(i, j);
                data[i][j] = tempCount;
            }
        }
    }

    public int setTempCount(int i, int j) {
        int tempCount = 0;//周围的雷数
        if (i > 0 && j > 0 && data[i - 1][j - 1] == LeiCode) tempCount++;
        if (i > 0 && data[i - 1][j] == LeiCode) tempCount++;
        if (i > 0 && j < COL - 1 && data[i - 1][j + 1] == LeiCode) tempCount++;
        if (j > 0 && data[i][j - 1] == LeiCode) tempCount++;
        if (j < COL - 1 && data[i][j + 1] == LeiCode) tempCount++;
        if (i < ROW - 1 && j > 0 && data[i + 1][j - 1] == LeiCode) tempCount++;
        if (i < ROW - 1 && data[i + 1][j] == LeiCode) tempCount++;
        if (i < ROW - 1 && j < COL- 1 && data[i + 1][j + 1] == LeiCode) tempCount++;

        return tempCount;
    }

    public void setButtons() {
        //Container con = new Container();//小容器，可以放入图片和按钮
        frame.remove(this.con);

        con.setLayout(new  GridLayout(ROW, COL));//用于排布相同的容器
        for (int i = 0; i < ROW; i++) {
            for (int i1 = 0; i1 < COL; i1++) {
                JButton btn = new JButton(guessIcon);//设置按钮
                btn.setOpaque(true);
                btn.setBackground(Color.GRAY);//设置背景色
                btn.addActionListener(this);
                btn.addMouseListener(this);
                //JButton btn=new JButton(data[i][i1]+"");
                con.add(btn);//将按钮放在容器中
                buttons[i][i1] = btn;//将按钮放入数据结构中
                canBeOpen[i][i1]=true;
            }
        }
        frame.add(con, BorderLayout.CENTER);//将容器（们）放在中心位置
    }

    public void setMenu() {
        JMenuBar menuBar = new JMenuBar();
        Font font1 = new Font("等线", Font.BOLD, 20);
        JMenu difficultyMenu = new JMenu("难度设置");

        JMenu cheatingMenu = new JMenu("作弊开关");
        difficultyMenu.setFont(font1);
        cheatingMenu.setFont(font1);
        menuBar.add(difficultyMenu);
        menuBar.add(cheatingMenu);

        Font font2 = new Font("等线", Font.BOLD, 17);
        JMenuItem difficulty1 = new JMenuItem("简单难度");
        JMenuItem difficulty2 = new JMenuItem("中等难度");
        JMenuItem difficulty3 = new JMenuItem("困难难度");
        JMenuItem cheatingButton =new JMenuItem("确认作弊");
        difficultyMenu.add(difficulty1);
        difficultyMenu.add(difficulty2);
        difficultyMenu.add(difficulty3);
        cheatingMenu.add(cheatingButton);
        difficulty1.setFont(font2);
        difficulty2.setFont(font2);
        difficulty3.setFont(font2);
        cheatingButton.setFont(font2);
        difficulty1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(con);
                Container con=new Container();
                con.setLayout(new GridLayout(ROW,COL));
                COL=9;
                ROW=9;
                LeiCount=10;
                adjustDifficulty();
                restart();
            }
        });
        difficulty2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(con);
                Container con=new Container();
                con.setLayout(new GridLayout(ROW,COL));
                COL=16;
                ROW=16;
                LeiCount=40;
                adjustDifficulty();
                restart();
            }
        });
        difficulty3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(con);
                con.setLayout(new GridLayout(ROW,COL));
                COL=16;
                ROW=30;
                LeiCount=99;
                adjustDifficulty();
                restart();

            }
        });
        cheatingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {//todo: 把雷区全部掀开
                for (int i = 0; i < ROW; i++) {
                    for (int i1 = 0; i1 < COL; i1++) {
                        if (canBeOpen[i][i1]) {
                            JButton btn = buttons[i][i1];
                            if (data[i][i1] == LeiCode) {
                                canBeOpen[i][i1] = false;
                                btn.setIcon(bombIcon);
                                btn.setDisabledIcon(bombIcon);
                            }
                            /*
                            else {
                                btn.setIcon(null);//清除icon
                                canBeOpen[i][i1] = false;
                                btn.setOpaque(true);//设置不透明

                                if(data[i][i1]==0){
                                    btn.setIcon(afterOpen);//背景换为碎石
                                }else if(data[i][i1]==1){
                                    btn.setIcon(afterOpen1);
                                }else if(data[i][i1]==2){
                                    btn.setIcon(afterOpen2);
                                }else if(data[i][i1]==3){
                                    btn.setIcon(afterOpen3);
                                }else if(data[i][i1]==4){
                                    btn.setIcon(afterOpen4);
                                }else if(data[i][i1]==5){
                                    btn.setIcon(afterOpen5);
                                }else if(data[i][i1]==6){
                                    btn.setIcon(afterOpen6);
                                }else if(data[i][i1]==7){
                                    btn.setIcon(afterOpen7);
                                }else if(data[i][i1]==8){
                                    btn.setIcon(afterOpen8);
                                }
                                //btn.setText(data[i][i1] + "");//填入数字
                            }
                            全掀开or只掀雷
                             */
                        }
                    }
                }
            }
        });
        frame.setJMenuBar(menuBar);
        frame.setVisible(true);
    }

    //设计框体头部
    public void setHeader() {
        JPanel panel = new JPanel(new GridBagLayout());//设置画布

        GridBagConstraints c1 = new GridBagConstraints(0, 0, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(bannerBtn, c1);
        bannerBtn.addActionListener(this);//这个按钮点了之后去找this，也就是actionPerformed方法

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

        GridBagConstraints c2 = new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        GridBagConstraints c3 = new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        GridBagConstraints c4 = new GridBagConstraints(2, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);

        panel.add(label1, c2);
        panel.add(label2, c3);
        panel.add(label3, c4);
        frame.add(panel, BorderLayout.NORTH);
    }

    public void setEast() {
        Image image = woman.getImage();
        image = image.getScaledInstance(woman.getIconWidth() / 2,woman.getIconHeight() / 2,Image.SCALE_SMOOTH);
        woman = new ImageIcon(image);
        JButton eastTestBtn = new JButton(woman);//调试中 ZFH


        int skillCD = 3;
        JPanel panel = new JPanel(new GridBagLayout());//设置画布
        panel.setPreferredSize(new Dimension(300, 200));

        GridBagConstraints c1 = new GridBagConstraints(0, 0, 4, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        GridBagConstraints c2 = new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 1, 1);
        GridBagConstraints c3 = new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 1, 1);
        GridBagConstraints c4 = new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 1, 1);
        GridBagConstraints c5 = new GridBagConstraints(0, 4, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 1, 1);

        JLabel a = new JLabel();
        JLabel b = new JLabel();
        JLabel c = new JLabel();
        JLabel d = new JLabel();

        a.setText("金子数: " + score1);
        b.setText("剩余步数: " + actionCount);
        c.setText("损坏镐子：" + brokenPickaxe1);
        d.setText("技能CD（剩余回合数）: " + skillCD );
        Font font1 = new Font("等线", Font.BOLD, 17);
        a.setFont(font1);
        b.setFont(font1);
        c.setFont(font1);
        d.setFont(font1);

        panel.add(eastTestBtn, c1);
        panel.add(a, c2);
        panel.add(b, c3);
        panel.add(c, c4);
        panel.add(d, c5);
        bannerBtn.addActionListener(this);

        bannerBtn.setOpaque(true);
        bannerBtn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        bannerBtn.setBackground(Color.white);

        frame.add(panel, BorderLayout.EAST);
    }

    public void setWest() {
        Image image = man.getImage();
        image = image.getScaledInstance(man.getIconWidth() / 2,man.getIconHeight() / 2,Image.SCALE_SMOOTH);
        man = new ImageIcon(image);
        JButton westTestBtn = new JButton(man);//调试中 ZFHs


        int skillCD = 3;
        JPanel panel = new JPanel(new GridBagLayout());//设置画布
        panel.setPreferredSize(new Dimension(300, 200));
        GridBagConstraints c1 = new GridBagConstraints(0, 0, 4, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        GridBagConstraints c2 = new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 1, 1);
        GridBagConstraints c3 = new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 1, 1);
        GridBagConstraints c4 = new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 1, 1);
        GridBagConstraints c5 = new GridBagConstraints(0, 4, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 1, 1);

        JLabel a = new JLabel();
        JLabel b = new JLabel();
        JLabel c = new JLabel();
        JLabel d = new JLabel();

        a.setText("金子数: " + score1);
        b.setText("剩余步数: " + actionCount);
        c.setText("损坏镐子：" + brokenPickaxe1);
        d.setText("技能CD（剩余回合数）: " + skillCD );
        Font font1 = new Font("等线", Font.BOLD, 17);
        a.setFont(font1);
        b.setFont(font1);
        c.setFont(font1);
        d.setFont(font1);

        panel.add(westTestBtn, c1);
        panel.add(a, c2);
        panel.add(b, c3);
        panel.add(c, c4);
        panel.add(d, c5);
        bannerBtn.addActionListener(this);

        bannerBtn.setOpaque(true);
        bannerBtn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        bannerBtn.setBackground(Color.white);

        frame.add(panel, BorderLayout.WEST);
    }//调试中 DYK

    public void setSouth() {
        JPanel panel = new JPanel(new GridBagLayout());//设置画布
        GridBagConstraints c1 = new GridBagConstraints(0, 0, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        panel.add(southTestBtn, c1);
        bannerBtn.addActionListener(this);

        bannerBtn.setOpaque(true);
        bannerBtn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        bannerBtn.setBackground(Color.white);

        frame.add(panel, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent e) {
        //先判断一下触发这action的是谁

        if (e.getSource() instanceof Timer) {
            seconds++;
            label3.setText("用时：" + seconds + "s");
            timer.start();//每次++完都要再开始一遍
            return;
        }



        JButton btn = (JButton) e.getSource();
        if (btn.equals(bannerBtn)) {
            restart();
            return;
        }
        for (int i = 0; i < ROW; i++) {
            for (int i1 = 0; i1 < COL; i1++) {
                if (btn.equals(buttons[i][i1])) {
                    if (data[i][i1] == LeiCode) {//判断输赢
                        if (clickTimes == 0) {
                            while (data[i][i1] == LeiCode) {
                                restart();
                            }
                            if (canBeOpen[i][i1]) {
                                if (checkActionCount()) {
                                    System.out.println("Player" + (player + 1) + "已经操作" + actionCount + "/" + maxAction + "次");
                                } else {
                                    System.out.println("已经操作" + maxAction + "/" + maxAction + "次");
                                    System.out.println("Player" + (player + 1) + "'s turn!");
                                }
                            }
                            openCell(i, i1);
                            openOpenCell();
                            clickTimes++;
                        } else {
                            lose();
                        }
                    } else {
                        if (canBeOpen[i][i1]) {
                            if (checkActionCount()) {
                                System.out.println("Player" + (player + 1) + "已经操作" + actionCount + "/" + maxAction + "次");
                            } else {
                                System.out.println("已经操作" + maxAction + "/" + maxAction + "次");
                                System.out.println("Player" + (player + 1) + "'s turn!");
                            }
                        }
                        openCell(i, i1);
                        openOpenCell();
                        clickTimes++;
                        checkWin();//判断胜利

                /*
                此处为尝试加入操作次数统计
                以及双人操作的切换
                 */
                        /*
                        if (checkActionCount(i,i1)) {
                            System.out.println("Player" + (player + 1) + "已经操作" + actionCount + "/" + maxAction + "次");
                        } else {
                            System.out.println("已经操作" + maxAction + "/" + maxAction + "次");
                            System.out.println("Player" + (player + 1) + "'s turn!");
                        }

                         */
                    }
                    return;
                }
            }
        }
    }

    public boolean checkActionCount() {
        actionCount++;
        if (actionCount == maxAction) {
            player = (player == 0) ? 1 : 0;
            actionCount = 0;

            JDialog dialog = new JDialog();
            dialog.setVisible(true);
            dialog.setBounds(500, 300, 500, 500);
            Container container = dialog.getContentPane();
            JLabel label = new JLabel("请交换玩家", flagIcon, SwingConstants.CENTER);
            container.add(label);


            return false;
        } else {
            return true;
        }
    }

    public void checkWin() {
        int count = 0;
        for (int i = 0; i < ROW; i++) {
            for (int i1 = 0; i1 < COL; i1++) {
                if (canBeOpen[i][i1]) count++;
            }
        }
        if (count == LeiCount) {
            timer.stop();//胜利后时间停止
            for (int i = 0; i < ROW; i++) {
                for (int i1 = 0; i1 < COL; i1++) {
                    if (canBeOpen[i][i1]) {
                        buttons[i][i1].setIcon(win_flagIcon);
                    }
                }
            }
            bannerBtn.setIcon(winIcon);
            JOptionPane.showMessageDialog(frame, "你赢了，Yeah\n点击Banner重新开始", "赢了", JOptionPane.PLAIN_MESSAGE);
        }

    }

    public void lose() {//踩到雷后爆雷
        timer.stop();//踩雷后时间停止
        bannerBtn.setIcon(failIcon);
        for (int i = 0; i < ROW; i++) {
            for (int i1 = 0; i1 < COL; i1++) {
                if (canBeOpen[i][i1]) {
                    JButton btn = buttons[i][i1];
                    if (data[i][i1] == LeiCode) {
                        canBeOpen[i][i1] = false;
                        btn.setIcon(bombIcon);
                        btn.setDisabledIcon(bombIcon);
                    } else {
                        btn.setIcon(null);//清除icon
                        canBeOpen[i][i1] = false;
                        btn.setOpaque(true);//设置不透明

                        if(data[i][i1]==0){
                            btn.setIcon(afterOpen);//背景换为碎石
                        }else if(data[i][i1]==1){
                            btn.setIcon(afterOpen1);
                        }else if(data[i][i1]==2){
                            btn.setIcon(afterOpen2);
                        }else if(data[i][i1]==3){
                            btn.setIcon(afterOpen3);
                        }else if(data[i][i1]==4){
                            btn.setIcon(afterOpen4);
                        }else if(data[i][i1]==5){
                            btn.setIcon(afterOpen5);
                        }else if(data[i][i1]==6){
                            btn.setIcon(afterOpen6);
                        }else if(data[i][i1]==7){
                            btn.setIcon(afterOpen7);
                        }else if(data[i][i1]==8){
                            btn.setIcon(afterOpen8);
                        }
                        //btn.setText(data[i][i1] + "");//填入数字
                    }
                }
            }
        }
        JOptionPane.showMessageDialog(frame, "可惜你暴雷了！\n你可以点击上面的Banner重新开始", "暴雷啦", JOptionPane.PLAIN_MESSAGE);//显示暴雷提示框
    }

    public void openCell(int i, int j) {
        JButton btn = buttons[i][j];
        if (!canBeOpen[i][j]) return;

        btn.setIcon(null);//清除icon
        canBeOpen[i][j] = false;
        btn.setOpaque(true);//设置不透明

        if(data[i][j]==0){
            btn.setIcon(afterOpen);//背景换为碎石
        }else if(data[i][j]==1){
            btn.setIcon(afterOpen1);
        }else if(data[i][j]==2){
            btn.setIcon(afterOpen2);
        }else if(data[i][j]==3){
            btn.setIcon(afterOpen3);
        }else if(data[i][j]==4){
            btn.setIcon(afterOpen4);
        }else if(data[i][j]==5){
            btn.setIcon(afterOpen5);
        }else if(data[i][j]==6){
            btn.setIcon(afterOpen6);
        }else if(data[i][j]==7){
            btn.setIcon(afterOpen7);
        }else if(data[i][j]==8){
            btn.setIcon(afterOpen8);
        }

        //btn.setText(data[i][j] + "");//填入数字

        addOpenCount();//调用这个方法来更改每一次操作所带来的已开和未开格子数目的变化

        if (data[i][j] == 0) {
            if (i > 0 && j > 0 && data[i - 1][j - 1] == 0) openCell(i - 1, j - 1);
            if (i > 0 && data[i - 1][j] == 0) openCell(i - 1, j);
            if (i > 0 && j < COL - 1 && data[i - 1][j + 1] == 0) openCell(i - 1, j + 1);
            if (j > 0 && data[i][j - 1] == 0) openCell(i, j - 1);
            if (j < COL - 1 && data[i][j + 1] == 0) openCell(i, j + 1);
            if (i < ROW - 1 && j > 0 && data[i + 1][j - 1] == 0) openCell(i + 1, j - 1);
            if (i < ROW - 1 && data[i + 1][j] == 0) openCell(i + 1, j);
            if (i < ROW - 1 && j < COL - 1 && data[i + 1][j + 1] == 0) openCell(i + 1, j + 1);
        }

    }

    public void openOpenCell(){
        for (int k = 0; k < ROW; k++) {
            for (int l = 0; l < COL; l++) {
                if(data[k][l]==0&&!canBeOpen[k][l]){
                    if (k > 0 && l > 0 && data[k - 1][l - 1] != -1) openCell(k - 1, l - 1);
                    if (k > 0 && data[k - 1][l] != -1) openCell(k - 1, l);
                    if (k > 0 && l < COL - 1 && data[k - 1][l + 1] != -1) openCell(k - 1, l + 1);
                    if (l > 0 && data[k][l - 1] != -1) openCell(k, l - 1);
                    if (l < COL - 1 && data[k][l + 1] != -1) openCell(k, l + 1);
                    if (k < ROW - 1 && l > 0 && data[k + 1][l - 1] != -1) openCell(k + 1, l - 1);
                    if (k < ROW - 1 && data[k + 1][l] != -1) openCell(k + 1, l);
                    if (k < ROW - 1 && l < COL - 1 && data[k + 1][l + 1] != -1) openCell(k + 1, l + 1);
                }
            }
        }
    }

    public void addOpenCount() {
        opened++;
        unopened--;
        label1.setText("待开：" + unopened);
        label2.setText("已开：" + opened);

    }

    /*
    restart里要干嘛：
    1.给数据清零
    2.给按钮恢复状态
    3.重新启动时钟
     */
    private void adjustDifficulty() {
        //恢复数据和按钮
        frame.remove(this.con);
        data = new int[ROW][COL];//记录每格的数据
        buttons =new JButton[ROW][COL];
        canBeOpen = new boolean[ROW][COL];
        Container con=new Container();
        con.setLayout(new GridLayout(ROW,COL));
        for (int i = 0; i < ROW; i++) {
            for (int i1 = 0; i1 < COL; i1++) {
                JButton btn = new JButton(guessIcon);//设置按钮
                btn.setOpaque(true);
                btn.setBackground(Color.GRAY);//设置背景色
                btn.addActionListener(this);
                btn.addMouseListener(this);
                //JButton btn=new JButton(data[i][i1]+"");
                con.add(btn);//将按钮放在容器中
                buttons[i][i1] = btn;//将按钮放入数据结构中
                canBeOpen[i][i1]=true;
            }
        }
        frame.add(con,BorderLayout.CENTER);

    }

    private void restart(){
        for (int i = 0; i < ROW; i++) {
            for (int i1 = 0; i1 < COL; i1++) {

                data[i][i1] = 0;
                canBeOpen[i][i1] = true;
                buttons[i][i1].setBackground(Color.GRAY);
                buttons[i][i1].setEnabled(true);
                buttons[i][i1].setText("");
                buttons[i][i1].setIcon(guessIcon);
            }
        }
        //操作次数以及player信息恢复
        player = 0;
        actionCount = 0;
        clickTimes = 0;

        //状态栏恢复
        unopened = ROW * COL;//未开的数量
        opened = 0;//已开的数量
        seconds = 0;
        label1.setText("待开：" + unopened);
        label2.setText("已开：" + opened);
        label3.setText("用时：" + seconds + "s");

        addLei();
        timer.start();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int c = e.getButton();
        if (c == MouseEvent.BUTTON3) {
            Object obj1 = e.getSource();

            for (int i = 0; i < ROW; i++) {
                for (int i1 = 0; i1 < COL; i1++) {
                    if (obj1 == buttons[i][i1]) {
                        if (canBeOpen[i][i1] && data[i][i1] == -1) {
                            buttons[i][i1].setIcon(null);
                            JButton btn = buttons[i][i1];
                            canBeOpen[i][i1]=false;
                            btn.setOpaque(true);
                            btn.setIcon(flagIcon);
                            btn.setBackground(null);

                            if (player == 0) score1++;
                            if (player == 1) score2++;

                            if (checkActionCount()) {
                                System.out.println("Player" + (player + 1) + "已经操作" + actionCount + "/" + maxAction + "次");
                            } else {
                                System.out.println("已经操作" + maxAction + "/" + maxAction + "次");
                                System.out.println("Player" + (player + 1) + "'s turn!");
                            }

                            JDialog dialog = new JDialog();
                            dialog.setVisible(true);
                            dialog.setBounds(500, 300, 500, 500);
                            Container container = dialog.getContentPane();
                            JLabel label = new JLabel("", flagIcon, SwingConstants.CENTER);
                            container.add(label);
                        } else if (canBeOpen[i][i1]){
                            buttons[i][i1].setIcon(null);
                            JButton btn = buttons[i][i1];
                            canBeOpen[i][i1]=false;
                            btn.setOpaque(true);
                            btn.setIcon(flagIcon);
                            btn.setBackground(null);

                            if (player == 0) brokenPickaxe1++;
                            if (player == 1) brokenPickaxe2++;

                            if (checkActionCount()) {
                                System.out.println("Player" + (player + 1) + "已经操作" + actionCount + "/" + maxAction + "次");
                            } else {
                                System.out.println("已经操作" + maxAction + "/" + maxAction + "次");
                                System.out.println("Player" + (player + 1) + "'s turn!");
                            }

                            JDialog dialog = new JDialog();
                            dialog.setVisible(true);
                            dialog.setBounds(500, 300, 500, 500);
                            Container container = dialog.getContentPane();
                            JLabel label = new JLabel("", bombIcon, SwingConstants.CENTER);
                            container.add(label);
                        }


                        /*
                        现存问题：右键后的相关操作未完善
                        右键操作尚未计数
                        积分系统未完成
                        ZFH 21.5.8
                         */
                    }
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }





}
