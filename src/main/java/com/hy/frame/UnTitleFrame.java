package com.hy.frame;

import com.hy.model.unit.Pixel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;



public class UnTitleFrame extends JFrame{

    Toolkit toolkit = Toolkit.getDefaultToolkit();
    private int DEFAULE_WIDTH = (int) (toolkit.getScreenSize().getWidth() * 0.5);
    private int DEFAULE_HEIGH = (int) (toolkit.getScreenSize().getHeight() * 0.03);
    private int DEFAULE_X = 0;
    private int DEFAULE_Y = 0;

    int mouseAtX = 0;
    int mouseAtY = 0;


    final int pxy = (int) (DEFAULE_HEIGH * 0.4) / 2; // 坐标
    final int pwh = (int) (DEFAULE_HEIGH * 0.6);   //直径

    private int tab = 0;
    private String keyList = "12";

    public UnTitleFrame(){

    }

    public UnTitleFrame(List<Pixel> colorList,boolean visible){
        drawing(colorList,visible);
    }

    public UnTitleFrame(List<Pixel> colorList,String keyList,boolean visible){
        System.out.println(keyList);
        JLabel jLabel = new JLabel(keyList);
        jLabel.setForeground(Color.BLUE);
        jLabel.setBounds(pxy, pxy, DEFAULE_WIDTH, pwh);
//        jLabel.setFont(new Font("MS Song", Font.PLAIN, pwh));
        add(jLabel);
        drawing(colorList,visible);
    }

    private void drawing(List<Pixel> colorList,boolean visible) {
        setUndecorated(true);//设置窗体的标题栏不可见

        //设置窗体可拖动
        addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                //获取点击鼠标时的坐标
                mouseAtX = e.getPoint().x;
                mouseAtY = e.getPoint().y;
            }
        });
        addMouseMotionListener(new MouseMotionAdapter(){
            public void mouseDragged(MouseEvent e){
                setLocation((e.getXOnScreen()-mouseAtX),(e.getYOnScreen()-mouseAtY));//设置拖拽后，窗口的位置
            }
        });


        JPanel jPanel = new JPanel(){
            public void paint(Graphics g){
                if(null != colorList){
                    for (int i = 0; i < colorList.size(); i++) {
                        g.setColor(colorList.get(i).getColor());
                        g.fillOval(pxy+(pxy + pwh)*i, pxy, pwh, pwh);
                        g.setColor(Color.BLACK);
                    }
                }
            }
        };
        add(jPanel);

        setSize(DEFAULE_WIDTH,DEFAULE_HEIGH);// 设置窗体默认大小,使其适应屏幕大小
        setLocation(DEFAULE_X, DEFAULE_Y);//设置窗体在屏幕中的位置
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(visible); //设置窗体可见
        setAlwaysOnTop(true);
    }


//    public void paint(Graphics g){
//        super.paint(g);
//        Graphics g1 = this.getGraphics();
//        g1.setColor(Color.black);
//        for(int i=0;i<4;i++){
//            g.drawLine(pxy+(pxy + pwh)*(i+1),DEFAULE_HEIGH/2,pxy+(pxy + pwh)*(i+2),DEFAULE_HEIGH/2);
//        }
//    }


    public void tabFunc(UnTitleFrame u,int i){
        JLabel jLabel = new JLabel();
        jLabel = new JLabel("->");
        jLabel.setForeground(Color.BLUE);
        jLabel.setFont(new Font("宋体", Font.PLAIN, 20));
        jLabel.setBounds(pxy+(pxy + pwh)*i, pxy, pwh, pwh);
        u.add(jLabel);
    }

    public String getKeyList() {
        return keyList;
    }

    public void setKeyList(String keyList) {
        this.keyList = keyList;
    }

}