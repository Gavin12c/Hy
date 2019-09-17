package com.hy.frame;

import lombok.Data;

import javax.swing.*;
import java.awt.*;

public class CircleJPanel extends JPanel {

    private int x;
    private int y;
    private int w;
    private int h;

    public CircleJPanel(int x,int y,int w,int h){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public void paint(Graphics g){
        g.fillOval(x,y,w,h);
    }

}
