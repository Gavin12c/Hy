package com.hy.frame;

import javax.swing.*;
import java.awt.*;

public class test {

    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        jFrame.setSize(200,100);


        CircleJPanel point = new CircleJPanel(0,0,10,10);
        point.setForeground(Color.BLUE);


        jFrame.add(point);


//        jFrame.setBackground(Color.white);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
        jFrame.setUndecorated(true); //设置隐藏标题栏
    }
}
