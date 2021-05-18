package com.minesweeper.MineSweeper;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Random;

public class SaoLei implements ActionListener, MouseListener {
    JFrame frame = new JFrame("Golden Rush");
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
    ImageIcon image101 = new ImageIcon("101.png");
    ImageIcon image102 = new ImageIcon("102.png");
    ImageIcon image103 = new ImageIcon("103.png");
    ImageIcon image104 = new ImageIcon("104.png");
    ImageIcon image105 = new ImageIcon("105.png");
    ImageIcon image106 = new ImageIcon("106.png");
    ImageIcon image107 = new ImageIcon("107.png");
    ImageIcon character1Icon = new ImageIcon("圣骑士2.png");
    ImageIcon character2Icon = new ImageIcon("术士2.png");
    ImageIcon character3Icon = new ImageIcon("牧师2.png");
    ImageIcon character4Icon = new ImageIcon("盗贼2.png");
    ImageIcon player1Icon = new ImageIcon();
    ImageIcon player2Icon = new ImageIcon();


    //数据结构
    int ROW = 24;//行数
    int COL = 30;//列数
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
    int actionLeft = maxAction - actionCount;
    int player = 0;
    int clickTimes = 0;//用于判断是否是第一次点击
    int score1 = 0;//玩家1的分数
    int score2 = 0;//玩家2的分数
    int brokenPickaxe1 = 0;//玩家1损坏的⛏
    int brokenPickaxe2 = 0;//玩家1损坏的⛏
    int borderWestWidth;
    int borderEastWidth;
    int borderHeadHeight;
    int fontSize;
    int picSize;
    int frameWidth;
    int frameHeight;
    int skillCD = 0;
    JButton bannerBtn = new JButton(bannerIcon);
    JButton southTestBtn = new JButton(bannerIcon);//调试中 ZFH
    Boolean isPvE= false;

    JLabel label1 = new JLabel("待开：" + unopened);
    JLabel label2 = new JLabel("已开：" + opened);
    JLabel label3 = new JLabel("用时：" + seconds + "s");
    JLabel a1 = new JLabel();
    JLabel b1 = new JLabel();
    JLabel c11 = new JLabel();
    JLabel d1 = new JLabel();
    JLabel a2 = new JLabel();
    JLabel b2 = new JLabel();
    JLabel c22 = new JLabel();
    JLabel d2 = new JLabel();
    Timer timer = new Timer(1000, this);
    Container con = new Container();

    public SaoLei() throws MalformedURLException, InterruptedException {
        /*
        new Thread(()->{while(true) {playMusic();}
        }).start();
         */

        setFrame1();
        music();

    }

    /*
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
     */

    public void music() throws InterruptedException, MalformedURLException {
        File file = new File("D:/MineSweeper/Music1.wav");
        //创建audioclip对象
        AudioClip audioClip = null;
        //将file转换为url
        audioClip = Applet.newAudioClip(file.toURI().toURL());
        //循环播放	播放一次可以使用audioClip.play
        audioClip.loop();
        Thread.sleep(5000);
    }

    public void shortMusic() throws MalformedURLException, InterruptedException {
        File file = new File("D:/MineSweeper/short.wav");
        AudioClip audioClip = null;
        audioClip = Applet.newAudioClip(file.toURI().toURL());
        audioClip.play();
    }

    public void setFrame1() {
        JFrame frame1 = new JFrame("Golden Rush");

        frame1.setLayout(null);
        JPanel imagePanel;
        ImageIcon background = new ImageIcon("D:\\MineSweeper\\进入界面 (1).png");
        JLabel label = new JLabel(background);
        label.setBounds(0, 0, background.getIconWidth(), background.getIconHeight());
        imagePanel = (JPanel) frame1.getContentPane();
        imagePanel.setOpaque(false);
        imagePanel.setLayout(null);
        frame1.getLayeredPane().setLayout(null);
        frame1.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));


        frame1.setSize(800, 650);
        frame1.setResizable(false);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setLocationRelativeTo(null);
        frame1.setVisible(true);

        JButton pvp = new JButton("双人对战");
        JButton pve = new JButton("人机对战");
        JButton backgroundButton = new JButton("背景故事");
        JButton loadSaving = new JButton("载入存档");
        Font font1 = new Font("等线", Font.BOLD, 20);
        pvp.setFont(font1);
        pve.setFont(font1);
        backgroundButton.setFont(font1);
        loadSaving.setFont(font1);
        pvp.setBounds(300, 270, 200, 50);
        pve.setBounds(300, 350, 200, 50);
        loadSaving.setBounds(300, 430, 200, 50);
        backgroundButton.setBounds(300, 510, 200, 50);
        frame1.add(pvp);
        frame1.add(pve);
        frame1.add(backgroundButton);
        frame1.add(loadSaving);
        pvp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame1.dispose();
                setFrame2();
            }
        });
        pve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isPvE=true;
                player=1;
                frame1.dispose();
                setFrame2();
            }
        });
        backgroundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    setBackground();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        loadSaving.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Read();

                for (int i = 0; i < ROW; i++) {
                    for (int i1 = 0; i1 < COL; i1++) {
                        if(data[i][i1]==65535){
                            data[i][i1]=-1;
                        }
                    }
                }

                if(checkData()){
                    frame.setSize(frameWidth, frameHeight);
                    frame.setResizable(false);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setLayout(new BorderLayout());

                    player1Icon=character1Icon;
                    player2Icon=character2Icon;

                    setMenu();//设置菜单

                    setHeader();//设置头部

                /*
                if(p1==1){
                    player1Icon=character1Icon;
                }else if(p1==2){
                    player1Icon=character2Icon;
                }else if(p1==3){
                    player1Icon=character3Icon;
                }else if(p1==4){
                    player1Icon=character4Icon;
                }

                if(p2==1){
                    player2Icon=character1Icon;
                }else if(p2==2){
                    player2Icon=character2Icon;
                }else if(p2==3){
                    player2Icon=character3Icon;
                }else if(p2==4){
                    player2Icon=character4Icon;
                }

                 */

                    con=new Container();
                    con.setLayout(new GridLayout(ROW,COL));
                    for (int i = 0; i < ROW; i++) {
                        for (int i1 = 0; i1 < COL; i1++) {
                            JButton btn=new JButton();
                            btn.setOpaque(true);
                            btn.setBackground(Color.GRAY);//设置背景色
                            if(canBeOpen[i][i1]){
                                btn.setIcon(guessIcon);
                            }else{
                                if(data[i][i1]==-1){
                                    btn.setIcon(flagIcon);
                                }else{
                                    if (data[i][i1] == 0) {
                                        btn.setIcon(afterOpen);//背景换为碎石
                                    } else if (data[i][i1] == 1) {
                                        btn.setIcon(afterOpen1);
                                    } else if (data[i][i1] == 2) {
                                        btn.setIcon(afterOpen2);
                                    } else if (data[i][i1] == 3) {
                                        btn.setIcon(afterOpen3);
                                    } else if (data[i][i1] == 4) {
                                        btn.setIcon(afterOpen4);
                                    } else if (data[i][i1] == 5) {
                                        btn.setIcon(afterOpen5);
                                    } else if (data[i][i1] == 6) {
                                        btn.setIcon(afterOpen6);
                                    } else if (data[i][i1] == 7) {
                                        btn.setIcon(afterOpen7);
                                    } else if (data[i][i1] == 8) {
                                        btn.setIcon(afterOpen8);
                                    }
                                }
                            }
                            con.add(btn);
                            buttons[i][i1]=btn;
                            setListener(btn);
                        }
                    }
                    frame.add(con,BorderLayout.CENTER);



                    setEast();//调试中 ZFH

                    setWest();//调试中 ZFH

                    timer.start();//别忘了最开始也要开始Timer

                    frame.setVisible(true);
                }else{

                }

            }
        });
    }

    public boolean checkData(){
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if(canBeOpen.length != data.length||canBeOpen[i].length!=data[i].length){
                    return false;
                }else if(data[i][j]>8||data[i][j]<-1){
                    return false;
                }else if(frameWidth<0&&frameHeight<0){
                    return false;
                }
            }
        }
        return true;
    }

    public void setListener(JButton btn){
        btn.addActionListener(this);
        btn.addMouseListener(this);
    }

    public void setFrame2() {
        JFrame frame2 = new JFrame("Golden Rush");
        frame2.setLayout(null);

        JPanel imagePanel;
        ImageIcon background = new ImageIcon("D:\\MineSweeper\\进入界面 (1).png");
        JLabel label = new JLabel(background);
        label.setBounds(0, 0, background.getIconWidth(), background.getIconHeight());
        imagePanel = (JPanel) frame2.getContentPane();
        imagePanel.setOpaque(false);
        imagePanel.setLayout(null);
        frame2.getLayeredPane().setLayout(null);
        frame2.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));

        frame2.setSize(800, 650);
        frame2.setResizable(false);
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.setLocationRelativeTo(null);
        frame2.setVisible(true);

        JTextField jt = new JTextField();
        jt.setBounds(430, 250, 100, 50);
        frame2.add(jt);

        Font font1 = new Font("等线", Font.BOLD, 20);
        JLabel jl = new JLabel("请设置每局限定步数(1-5):");
        jl.setOpaque(true);
        jl.setBackground(Color.WHITE);
        jl.setBounds(150, 250, 250, 50);
        frame2.add(jt);
        jl.setFont(font1);
        frame2.add(jl);

        JButton jb = new JButton("确定");
        jb.setBounds(350, 450, 100, 50);
        frame2.add(jb);
        jb.setFont(font1);
        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = jt.getText();
                if (str.equals("")) {
                    Object[] options = {"OK ", "CANCEL "};
                    JOptionPane.showOptionDialog(null, "您还没有输入 ", "提示", JOptionPane.DEFAULT_OPTION,
                            JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                } else if (Integer.parseInt(str) > 5 || Integer.parseInt(str) < 0) {
                    Object[] options = {"OK ", "CANCEL "};
                    JOptionPane.showOptionDialog(null, "您的输入不正确 ", "提示", JOptionPane.DEFAULT_OPTION,
                            JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                } else {
                    maxAction = Integer.parseInt(str);
                    frame2.dispose();
                    setFrame3();
                }
            }
        });

        JButton back = new JButton("back");
        back.setFont(font1);
        back.setBounds(10, 10, 100, 50);
        frame2.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame2.dispose();
                setFrame1();
            }
        });

    }

    public void setFrame3() {
        JFrame frame3 = new JFrame("Golden Rush");
        frame3.setLayout(null);

        frame3.setLayout(null);
        JPanel imagePanel;
        ImageIcon background = new ImageIcon("D:\\MineSweeper\\进入界面 (1).png");
        JLabel label = new JLabel(background);
        label.setBounds(0, 0, background.getIconWidth(), background.getIconHeight());
        imagePanel = (JPanel) frame3.getContentPane();
        imagePanel.setOpaque(false);
        imagePanel.setLayout(null);
        frame3.getLayeredPane().setLayout(null);
        frame3.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));

        frame3.setSize(800, 650);
        frame3.setResizable(false);
        frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame3.setLocationRelativeTo(null);
        frame3.setVisible(true);

        Font font1 = new Font("等线", Font.BOLD, 50);
        JLabel jLabel = new JLabel("player1   请选择你的英雄");
        jLabel.setBounds(50,25,500,50);
        jLabel.setOpaque(true);
        //jLabel.setBackground();
        jLabel.setFont(font1);
        frame3.add(jLabel);

        JButton character1 = new JButton();
        ImageIcon newCharacter1Icon = new ImageIcon("圣骑士3.png");
        character1.setIcon(newCharacter1Icon);
        character1.setBounds(40,200,150,350);
        character1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player1Icon = character1Icon;
                frame3.dispose();
                setFrame4();
            }
        });
        frame3.add(character1);

        JButton character2 = new JButton();
        ImageIcon newCharacter2Icon = new ImageIcon("术士3.png");
        character2.setIcon(newCharacter2Icon);
        character2.setBounds(230,200,150,350);
        character2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player1Icon = character2Icon;
                frame3.dispose();
                setFrame4();
            }
        });
        frame3.add(character2);

        JButton character3 = new JButton();
        ImageIcon newCharacter3Icon = new ImageIcon("牧师3.png");
        character3.setIcon(newCharacter3Icon);
        character3.setBounds(420,200,150,350);
        character3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player1Icon = character3Icon;
                frame3.dispose();
                setFrame4();
            }
        });
        frame3.add(character3);

        JButton character4 = new JButton();
        ImageIcon newCharacter4Icon = new ImageIcon("盗贼3.png");
        character4.setIcon(newCharacter4Icon);
        character4.setBounds(610,200,150,350);
        character4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player1Icon = character4Icon;
                frame3.dispose();
                setFrame4();
            }
        });
        frame3.add(character4);


    }

    public void setFrame4(){
        JFrame frame3 = new JFrame("Golden Rush");
        frame3.setLayout(null);

        frame3.setLayout(null);
        JPanel imagePanel;
        ImageIcon background = new ImageIcon("D:\\MineSweeper\\进入界面 (1).png");
        JLabel label = new JLabel(background);
        label.setBounds(0, 0, background.getIconWidth(), background.getIconHeight());
        imagePanel = (JPanel) frame3.getContentPane();
        imagePanel.setOpaque(false);
        imagePanel.setLayout(null);
        frame3.getLayeredPane().setLayout(null);
        frame3.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));

        frame3.setSize(800, 650);
        frame3.setResizable(false);
        frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame3.setLocationRelativeTo(null);
        frame3.setVisible(true);

        Font font1 = new Font("等线", Font.BOLD, 50);
        JLabel jLabel = new JLabel("player2   请选择你的英雄");
        jLabel.setBounds(50,25,500,50);
        jLabel.setOpaque(true);
        //jLabel.setBackground();
        jLabel.setFont(font1);
        frame3.add(jLabel);

        JButton character1 = new JButton();
        ImageIcon newCharacter1Icon = new ImageIcon("圣骑士3.png");
        character1.setIcon(newCharacter1Icon);
        character1.setBounds(40,200,150,350);
        character1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player2Icon = character1Icon;
                frame3.dispose();
                setFrame5();
            }
        });
        frame3.add(character1);

        JButton character2 = new JButton();
        ImageIcon newCharacter2Icon = new ImageIcon("术士3.png");
        character2.setIcon(newCharacter2Icon);
        character2.setBounds(230,200,150,350);
        character2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player2Icon = character2Icon;
                frame3.dispose();
                setFrame5();
            }
        });
        frame3.add(character2);

        JButton character3 = new JButton();
        ImageIcon newCharacter3Icon = new ImageIcon("牧师3.png");
        character3.setIcon(newCharacter3Icon);
        character3.setBounds(420,200,150,350);
        character3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player2Icon = character3Icon;
                frame3.dispose();
                setFrame5();
            }
        });
        frame3.add(character3);

        JButton character4 = new JButton();
        ImageIcon newCharacter4Icon = new ImageIcon("盗贼3.png");
        character4.setIcon(newCharacter4Icon);
        character4.setBounds(610,200,150,350);
        character4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player2Icon = character4Icon;
                frame3.dispose();
                setFrame5();
            }
        });
        frame3.add(character4);

    }

    public void setFrame5() {
        JFrame frame5 = new JFrame("Golden Rush");
        frame5.setLayout(null);

        JPanel imagePanel;
        ImageIcon background = new ImageIcon("D:\\MineSweeper\\进入界面 (1).png");
        JLabel label = new JLabel(background);
        label.setBounds(0, 0, background.getIconWidth(), background.getIconHeight());
        imagePanel = (JPanel) frame5.getContentPane();
        imagePanel.setOpaque(false);
        imagePanel.setLayout(null);
        frame5.getLayeredPane().setLayout(null);
        frame5.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));

        frame5.setSize(800, 650);
        frame5.setResizable(false);
        frame5.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame5.setLocationRelativeTo(null);
        frame5.setVisible(true);

        JButton difficulty1 = new JButton("简单难度");
        JButton difficulty2 = new JButton("中等难度");
        JButton difficulty3 = new JButton("困难难度");
        JButton back = new JButton("返回");
        Font font1 = new Font("等线", Font.BOLD, 20);
        difficulty1.setFont(font1);
        difficulty2.setFont(font1);
        difficulty3.setFont(font1);
        back.setFont(font1);
        difficulty1.setBounds(300, 125, 200, 50);
        difficulty2.setBounds(300, 275, 200, 50);
        difficulty3.setBounds(300, 425, 200, 50);
        back.setBounds(10, 10, 100, 50);

        frame5.add(difficulty1);
        frame5.add(difficulty2);
        frame5.add(difficulty3);
        frame5.add(back);

        difficulty1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame5.dispose();

                ROW = 9;
                COL = 9;
                LeiCount = 10;
                borderWestWidth = 160;
                borderEastWidth = 160;
                borderHeadHeight = 100;
                fontSize = 13;
                picSize = 3;
                frameWidth = 680;
                frameHeight = 560;

                frame.setSize(frameWidth, frameHeight);
                frame.setResizable(false);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());

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
        });

        difficulty2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame5.dispose();

                ROW = 16;
                COL = 16;
                LeiCount = 40;
                borderWestWidth = 200;
                borderEastWidth = 200;
                borderHeadHeight = 100;
                fontSize = 15;
                picSize = 2;
                frameWidth = 1040;
                frameHeight = 740;

                frame.setSize(frameWidth, frameHeight);
                frame.setResizable(false);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());

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
        });

        difficulty3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame5.dispose();

                ROW = 16;
                COL = 30;
                LeiCount = 99;
                borderWestWidth = 250;
                borderEastWidth = 250;
                borderHeadHeight = 100;
                fontSize = 20;
                picSize = 2;
                frameWidth = 1700;
                frameHeight = 740;

                frame.setSize(frameWidth, frameHeight);
                frame.setResizable(false);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());

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
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame5.dispose();
                setFrame2();
            }
        });
    }

    public void setBackground() throws IOException {
        JFrame backgroundFrame = new JFrame("Background");
        //backgroundFrame.setLayout(null);
        backgroundFrame.setSize(700, 550);
        backgroundFrame.setResizable(false);
        backgroundFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        backgroundFrame.setLocationRelativeTo(null);
        backgroundFrame.setVisible(true);


        Container container = backgroundFrame.getContentPane();

        BufferedReader reader = new BufferedReader(new FileReader("D:\\MineSweeper\\src\\main\\java\\com\\minesweeper\\MineSweeper\\背景故事"));
        String line = reader.readLine();
        String all = "";
        while (line != null) {
            all += line + "\n";

            line = reader.readLine();
        }
        JTextArea backgroundText = new JTextArea(all);
        Font font1 = new Font("等线", Font.BOLD, 20);
        backgroundText.setFont(font1);
        backgroundText.setBounds(50, 30, 600, 450);
        backgroundText.setLineWrap(true);


        JScrollPane sp = new JScrollPane(backgroundText);
        container.add(sp);


    }

    public void addLei() {
        Random rand = new Random();
        for (int i = 0; i < LeiCount; ) {
            int r = rand.nextInt(ROW);//0-19的整数
            int c = rand.nextInt(COL);
            if (data[r][c] != LeiCode && setTempCount(r, c) != 8) {
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
        if (i < ROW - 1 && j < COL - 1 && data[i + 1][j + 1] == LeiCode) tempCount++;

        return tempCount;
    }

    public void setButtons() {
        //Container con = new Container();//小容器，可以放入图片和按钮
        frame.remove(this.con);

        con.setLayout(new GridLayout(ROW, COL));//用于排布相同的容器
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
                canBeOpen[i][i1] = true;
            }
        }
        frame.add(con, BorderLayout.CENTER);//将容器（们）放在中心位置
    }

    public void setMenu() {
        JMenuBar menuBar = new JMenuBar();
        Font font1 = new Font("等线", Font.BOLD, 20);

        JMenu cheatingMenu = new JMenu("作弊开关");
        cheatingMenu.setFont(font1);
        menuBar.add(cheatingMenu);

        JMenu savingMenu = new JMenu("保存开关");
        savingMenu.setFont(font1);
        menuBar.add(savingMenu);

        JMenuItem cheatingButton = new JMenuItem("确认作弊");
        cheatingMenu.add(cheatingButton);
        cheatingButton.setFont(font1);
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

        JMenuItem savingButton = new JMenuItem("确认保存");
        savingMenu.add(savingButton);
        savingButton.setFont(font1);
        savingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Save();
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

        panel.setPreferredSize(new Dimension(100, borderHeadHeight));
        frame.add(panel, BorderLayout.NORTH);
    }

    public void setEast() {
        Image image = player2Icon.getImage();
        image = image.getScaledInstance(player2Icon.getIconWidth() / picSize, player2Icon.getIconHeight() / picSize, Image.SCALE_SMOOTH);
        player2Icon = new ImageIcon(image);
        JButton eastTestBtn = new JButton(player2Icon);//调试中 ZFH


        JPanel panel = new JPanel(new GridBagLayout());//设置画布
        panel.setPreferredSize(new Dimension(borderEastWidth, 200));

        GridBagConstraints c1 = new GridBagConstraints(0, 0, 4, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        GridBagConstraints c2 = new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 1, 1);
        GridBagConstraints c3 = new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 1, 1);
        GridBagConstraints c4 = new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 1, 1);
        GridBagConstraints c5 = new GridBagConstraints(0, 4, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 1, 1);


        a1.setText("金子数: " + score1);
        b1.setText("剩余步数: " + 0);
        c11.setText("损坏镐子：" + brokenPickaxe1);
        d1.setText("技能CD（剩余回合数）: " + skillCD);
        Font font1 = new Font("等线", Font.BOLD, fontSize);
        a1.setFont(font1);
        b1.setFont(font1);
        c11.setFont(font1);
        d1.setFont(font1);

        panel.add(eastTestBtn, c1);
        panel.add(a1, c2);
        panel.add(b1, c3);
        panel.add(c11, c4);
        panel.add(d1, c5);
        bannerBtn.addActionListener(this);

        bannerBtn.setOpaque(true);
        bannerBtn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        bannerBtn.setBackground(Color.white);

        frame.add(panel, BorderLayout.EAST);
    }

    public void setWest() {
        Image image = player1Icon.getImage();
        image = image.getScaledInstance(player1Icon.getIconWidth() / picSize, player1Icon.getIconHeight() / picSize, Image.SCALE_SMOOTH);
        player1Icon = new ImageIcon(image);
        JButton westTestBtn = new JButton(player1Icon);//调试中 ZFH


        JPanel panel = new JPanel(new GridBagLayout());//设置画布
        panel.setPreferredSize(new Dimension(borderWestWidth, 100));
        GridBagConstraints c1 = new GridBagConstraints(0, 0, 4, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        GridBagConstraints c2 = new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 1, 1);
        GridBagConstraints c3 = new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 1, 1);
        GridBagConstraints c4 = new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 1, 1);
        GridBagConstraints c5 = new GridBagConstraints(0, 4, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 1, 1);

        a2.setText("金子数: " + score1);
        b2.setText("剩余步数: " + 0);
        c22.setText("损坏镐子：" + brokenPickaxe1);
        d2.setText("技能CD（剩余回合数）: " + skillCD);
        Font font1 = new Font("等线", Font.BOLD, fontSize);
        a2.setFont(font1);
        b2.setFont(font1);
        c22.setFont(font1);
        d2.setFont(font1);

        panel.add(westTestBtn, c1);
        panel.add(a2, c2);
        panel.add(b2, c3);
        panel.add(c22, c4);
        panel.add(d2, c5);
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
                    if (data[i][i1] == LeiCode) {
                        if (clickTimes == 0) {
                            while (data[i][i1] == LeiCode) {
                                restart();
                            }
                            if(canBeOpen[i][i1]){
                                checkActionCount();
                                actionLeft = maxAction - actionCount;
                                if (player == 0){
                                    if(actionCount == 0) {
                                        b1.setText("剩余步数: " + 0);
                                    }
                                    b2.setText("剩余步数: " + actionLeft);
                                }
                                if (player == 1) {
                                    if(actionCount == 0) {
                                        b2.setText("剩余步数: " + 0);
                                    }
                                    b1.setText("剩余步数: " + actionLeft);
                                }
                            }
                            openCell(i, i1);
                            try {
                                shortMusic();
                            } catch (MalformedURLException malformedURLException) {
                                malformedURLException.printStackTrace();
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                            openOpenCell();
                            clickTimes++;
                        } else {
                            lose();
                        }
                    } else {
                        if(canBeOpen[i][i1]){
                            checkActionCount();
                            actionLeft = maxAction - actionCount;
                            if (player == 0){
                                if(actionCount == 0) {
                                    b1.setText("剩余步数: " + 0);
                                }
                                b2.setText("剩余步数: " + actionLeft);
                            }
                            if (player == 1) {
                                if(actionCount == 0) {
                                    b2.setText("剩余步数: " + 0);
                                }
                                b1.setText("剩余步数: " + actionLeft);
                            }
                        }
                        openCell(i, i1);
                        try {
                            shortMusic();
                        } catch (MalformedURLException malformedURLException) {
                            malformedURLException.printStackTrace();
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }
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
        if(isPvE){
            actionCount++;
            if(actionCount==maxAction){
                RandomOpen();
                actionCount=0;
                JDialog dialog = new JDialog();
                dialog.setVisible(true);
                dialog.setBounds(500, 300, 500, 500);
                Container container = dialog.getContentPane();
                JLabel label = new JLabel("请交换玩家", flagIcon, SwingConstants.CENTER);
                container.add(label);

                return true;
            }else{
                return false;
            }
        }else{
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
            if (score1 > score2) {
                bannerBtn.setIcon(winIcon);
                JOptionPane.showMessageDialog(frame, "player1,你赢了 ヽ(✿ﾟ▽ﾟ)ノ\n点击Banner重新开始", "赢了", JOptionPane.PLAIN_MESSAGE);
            }
            if(score2 > score1) {
                bannerBtn.setIcon(winIcon);
                JOptionPane.showMessageDialog(frame, "player2,你赢了 ヽ(✿ﾟ▽ﾟ)ノ\n点击Banner重新开始", "赢了", JOptionPane.PLAIN_MESSAGE);
            }
            if (score1 == score2) {
                if (brokenPickaxe1 < brokenPickaxe2) {
                    bannerBtn.setIcon(winIcon);
                    JOptionPane.showMessageDialog(frame, "player1,你赢了 ヽ(✿ﾟ▽ﾟ)ノ\n点击Banner重新开始", "赢了", JOptionPane.PLAIN_MESSAGE);
                } else if (brokenPickaxe2 < brokenPickaxe1) {
                    bannerBtn.setIcon(winIcon);
                    JOptionPane.showMessageDialog(frame, "player2,你赢了 ヽ(✿ﾟ▽ﾟ)ノ\n点击Banner重新开始", "赢了", JOptionPane.PLAIN_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "你们都这么强的吗？居然打平了 Σ( ° △ °|||)︴\n点击Banner重新开始", "平局", JOptionPane.PLAIN_MESSAGE);

                }
            }
        }

    }

    public void lose() {//踩到雷后爆雷
        timer.stop();//踩雷后时间停止
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

                        if (data[i][i1] == 0) {
                            btn.setIcon(afterOpen);//背景换为碎石
                        } else if (data[i][i1] == 1) {
                            btn.setIcon(afterOpen1);
                        } else if (data[i][i1] == 2) {
                            btn.setIcon(afterOpen2);
                        } else if (data[i][i1] == 3) {
                            btn.setIcon(afterOpen3);
                        } else if (data[i][i1] == 4) {
                            btn.setIcon(afterOpen4);
                        } else if (data[i][i1] == 5) {
                            btn.setIcon(afterOpen5);
                        } else if (data[i][i1] == 6) {
                            btn.setIcon(afterOpen6);
                        } else if (data[i][i1] == 7) {
                            btn.setIcon(afterOpen7);
                        } else if (data[i][i1] == 8) {
                            btn.setIcon(afterOpen8);
                        }
                        //btn.setText(data[i][i1] + "");//填入数字
                    }
                }
            }
        }
        JOptionPane.showMessageDialog(frame, "可惜你把金子敲碎了 ┭┮﹏┭┮", "可惜了", JOptionPane.PLAIN_MESSAGE);
    }

    public void openCell(int i, int j) {
        JButton btn = buttons[i][j];
        if (!canBeOpen[i][j]) return;

        btn.setIcon(null);//清除icon
        canBeOpen[i][j] = false;
        btn.setOpaque(true);//设置不透明


        if (data[i][j] == 0) {
            btn.setIcon(afterOpen);//背景换为碎石
        } else if (data[i][j] == 1) {
            btn.setIcon(afterOpen1);
        } else if (data[i][j] == 2) {
            btn.setIcon(afterOpen2);
        } else if (data[i][j] == 3) {
            btn.setIcon(afterOpen3);
        } else if (data[i][j] == 4) {
            btn.setIcon(afterOpen4);
        } else if (data[i][j] == 5) {
            btn.setIcon(afterOpen5);
        } else if (data[i][j] == 6) {
            btn.setIcon(afterOpen6);
        } else if (data[i][j] == 7) {
            btn.setIcon(afterOpen7);
        } else if (data[i][j] == 8) {
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

    public void openOpenCell() {
        for (int k = 0; k < ROW; k++) {
            for (int l = 0; l < COL; l++) {
                if (data[k][l] == 0 && !canBeOpen[k][l]) {
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

    private void restart() {
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
        score1 = 0;
        score2 = 0;
        brokenPickaxe1 = 0;
        brokenPickaxe2 = 0;

        //状态栏恢复
        unopened = ROW * COL;//未开的数量
        opened = 0;//已开的数量
        seconds = 0;
        label1.setText("待开：" + unopened);
        label2.setText("已开：" + opened);
        label3.setText("用时：" + seconds + "s");

        a1.setText("金子数: " + score1);
        b1.setText("剩余步数: " + 0);
        c11.setText("损坏镐子：" + brokenPickaxe1);
        d1.setText("技能CD（剩余回合数）: " + skillCD);
        a2.setText("金子数: " + score1);
        b2.setText("剩余步数: " + 0);
        c22.setText("损坏镐子：" + brokenPickaxe1);
        d2.setText("技能CD（剩余回合数）: " + skillCD);

        addLei();
        timer.start();
    }

    public void rightClick(Object obj1) {
        for (int i = 0; i < ROW; i++) {
            for (int i1 = 0; i1 < COL; i1++) {
                if (obj1 == buttons[i][i1]) {
                    if (canBeOpen[i][i1] && data[i][i1] == -1) {
                        buttons[i][i1].setIcon(null);
                        JButton btn = buttons[i][i1];
                        canBeOpen[i][i1] = false;
                        btn.setOpaque(true);
                        btn.setIcon(flagIcon);
                        btn.setBackground(null);

                        if (player == 0) {
                            score1++;
                            a2.setText("金子数: " + score1);
                        }
                        if (player == 1) {
                            score2++;
                            a1.setText("金子数: " + score1);
                        }


                        checkActionCount();
                        actionLeft = maxAction - actionCount;
                        if (player == 0){
                            if(actionCount == 0) {
                                b1.setText("剩余步数: " + 0);
                            }
                            b2.setText("剩余步数: " + actionLeft);
                        }
                        if (player == 1) {
                            if(actionCount == 0) {
                                b2.setText("剩余步数: " + 0);
                            }
                            b1.setText("剩余步数: " + actionLeft);
                        }

                        VideoPlayer();

                    }

                    else if (canBeOpen[i][i1]) {
                        buttons[i][i1].setIcon(null);
                        JButton btn = buttons[i][i1];
                        canBeOpen[i][i1] = false;
                        btn.setOpaque(true);
                        if (data[i][i1] == 0) {
                            btn.setIcon(afterOpen);//背景换为碎石
                        } else if (data[i][i1] == 1) {
                            btn.setIcon(afterOpen1);
                        } else if (data[i][i1] == 2) {
                            btn.setIcon(afterOpen2);
                        } else if (data[i][i1] == 3) {
                            btn.setIcon(afterOpen3);
                        } else if (data[i][i1] == 4) {
                            btn.setIcon(afterOpen4);
                        } else if (data[i][i1] == 5) {
                            btn.setIcon(afterOpen5);
                        } else if (data[i][i1] == 6) {
                            btn.setIcon(afterOpen6);
                        } else if (data[i][i1] == 7) {
                            btn.setIcon(afterOpen7);
                        } else if (data[i][i1] == 8) {
                            btn.setIcon(afterOpen8);
                        }
                        btn.setBackground(null);

                        if (player == 0) {
                            brokenPickaxe1++;
                            c22.setText("损坏镐子：" + brokenPickaxe1);
                        }
                        if (player == 1) {
                            brokenPickaxe2++;
                            c11.setText("损坏镐子：" + brokenPickaxe1);
                        }

                        checkActionCount();
                        actionLeft = maxAction - actionCount;
                        if (player == 0){
                            if(actionCount == 0) {
                                b1.setText("剩余步数: " + 0);
                            }
                            b2.setText("剩余步数: " + actionLeft);
                        }
                        if (player == 1) {
                            if(actionCount == 0) {
                                b2.setText("剩余步数: " + 0);
                            }
                            b1.setText("剩余步数: " + actionLeft);
                        }

                        VideoPlayer();

                    }
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int c = e.getButton();
        if (c == MouseEvent.BUTTON3) {
            Object obj1 = e.getSource();
            rightClick(obj1);
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

    public void RandomOpen() {


        int left = (int)(Math.random()*maxAction)+1;//1-5
        int right = maxAction - left;//1-5

        for (int i = 0; i < left; i++) {
            /*
            while (data[row][col] != LeiCode && canBeOpen[row][col]) {
                row = (int)Math.random()*ROW;
                col = (int)Math.random()*COL;
            }

             */
            int row = (int)(Math.random()*ROW);
            int col = (int)(Math.random()*COL);
            while(!canBeOpen[row][col]){
                row = (int)(Math.random()*ROW);
                col = (int)(Math.random()*COL);
            }
            openCell(row, col);
            openOpenCell();
        }

        for (int i = 0; i < right; i++) {
            /*
            while (data[row][col] == LeiCode && canBeOpen[row][col]) {
                row = (int)Math.random()*ROW;
                col = (int)Math.random()*COL;
            }

            */
            int row = (int)(Math.random()*ROW);
            int col = (int)(Math.random()*COL);
            while(!canBeOpen[row][col]){
                row = (int)(Math.random()*ROW);
                col = (int)(Math.random()*COL);
            }
            rightClick(buttons[row][col]);

        }
    }

    public void Read() {
        String path = "";
        FileSystemView fsv = FileSystemView.getFileSystemView();

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
        fileChooser.setDialogTitle("请选择文件...");
        fileChooser.setApproveButtonText("确定");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int result = fileChooser.showOpenDialog(null);

        if (JFileChooser.APPROVE_OPTION == result) {
            path = fileChooser.getSelectedFile().getPath();
            //此处path为该文件路径
        }

        try {
            BufferedReader in = new BufferedReader(new FileReader(path));
            ROW = in.read();
            COL = in.read();
            LeiCount = in.read();
            unopened = in.read();
            opened = in.read();
            seconds = in.read();
            actionCount = in.read();
            maxAction = in.read();
            player = in.read();
            clickTimes = in.read();
            score1 = in.read();
            score2 = in.read();
            brokenPickaxe1 = in.read();
            brokenPickaxe2 = in.read();
            borderWestWidth = in.read();
            borderEastWidth = in.read();
            borderHeadHeight = in.read();
            fontSize = in.read();
            picSize = in.read();
            frameWidth = in.read();
            frameHeight = in.read();
            /*
            p1=in.read();
            p2=in.read();

             */

            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    data[i][j] = in.read();
                }
            }
            for (int i = 0; i < ROW; i++) {
                for (int i1 = 0; i1 < COL; i1++) {
                    int tempInt=in.read();
                    if (tempInt == 1) {
                        canBeOpen[i][i1] = true;
                    } else {
                        canBeOpen[i][i1] = false;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void Save() {
        String path = "";
        FileSystemView fsv = FileSystemView.getFileSystemView();

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
        fileChooser.setDialogTitle("请选择文件...");
        fileChooser.setApproveButtonText("确定");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = fileChooser.showOpenDialog(null);

        if (JFileChooser.APPROVE_OPTION == result) {
            path = fileChooser.getSelectedFile().getPath();
            //此处path为该文件路径
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path + "/Out.txt"));
            //需要对接！
            writer.write(ROW);
            writer.write(COL);
            writer.write(LeiCount);
            writer.write(unopened);
            writer.write(opened);
            writer.write(seconds);
            writer.write(actionCount);
            writer.write(maxAction);
            writer.write(player);
            writer.write(clickTimes);
            writer.write(score1);
            writer.write(score2);
            writer.write(brokenPickaxe1);
            writer.write(brokenPickaxe2);
            writer.write(borderWestWidth);
            writer.write(borderEastWidth);
            writer.write(borderHeadHeight);
            writer.write(fontSize);
            writer.write(picSize);
            writer.write(frameWidth);
            writer.write(frameHeight);
            /*
            if(player1Icon.getImage().equals(character1Icon)){
                writer.write(1);
            }else if(player1Icon.getImage().equals(character2Icon)){
                writer.write(2);
            }else if(player1Icon.getImage().equals(character3Icon)){
                writer.write(3);
            }else if(player1Icon.getImage().equals(character4Icon)){
                writer.write(4);
            }

            if(player2Icon.getImage().equals(character1Icon)){
                writer.write(1);
            }else if(player2Icon.getImage().equals(character2Icon)){
                writer.write(2);
            }else if(player2Icon.getImage().equals(character3Icon)){
                writer.write(3);
            }else if(player2Icon.getImage().equals(character4Icon)){
                writer.write(4);
            }

             */

            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    writer.write(data[i][j]);
                }
            }
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    if (canBeOpen[i][j]) {
                        writer.write(1);
                    } else {
                        writer.write(0);
                    }
                }
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void VideoPlayer() {
        JDialog dialog = new JDialog();
        dialog.setVisible(true);
        dialog.setBounds(500, 300, 500, 500);
        Container container = dialog.getContentPane();


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int count = 0;
        try {
            count++;
            JLabel label = new JLabel("", image101, SwingConstants.CENTER);
            container.add(label);
            Thread.sleep(3 * 100);
            label = new JLabel("", image102, SwingConstants.CENTER);
            container.add(label);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
