package com.hy.frame;

import com.hy.config.PathConfig;
import com.hy.model.unit.Pixel;
import com.hy.utils.FileIO;
import com.hy.utils.StringUtils;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import static com.sun.jna.platform.win32.WinUser.SW_RESTORE;

/**
 *  命令输入框
 */
public class CommandFrame extends JFrame implements KeyListener {

    Toolkit toolkit = Toolkit.getDefaultToolkit();
    int mouseAtX = 0;
    int mouseAtY = 0;
    private int DEFAULE_WIDTH = (int) (toolkit.getScreenSize().getWidth() * 0.33)/2;
    private int DEFAULE_HEIGH = (int) (toolkit.getScreenSize().getHeight() * 0.03);

    final static JTextField text = new JTextField();

    static String[] io = PathConfig.io("/WinTab.txt");


    public CommandFrame(){
        addKeyListener(this);
        text.addKeyListener(this);
        drawing();
    }


    private void drawing() {
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

        Point p = MouseInfo.getPointerInfo().getLocation();

        text.setBounds(p.x, p.y,DEFAULE_WIDTH,DEFAULE_HEIGH);
        getContentPane().add(text, BorderLayout.CENTER);

        setSize(DEFAULE_WIDTH,DEFAULE_HEIGH);// 设置窗体默认大小,使其适应屏幕大小
        setLocation(p.x, p.y);//设置窗体在屏幕中的位置
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true); //设置窗体可见
        setAlwaysOnTop(true);
    }

    public static void main(String[] args) {
        new CommandFrame();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            String text = CommandFrame.text.getText();
            System.out.println(text);
            if(text.toUpperCase().equals("DING")){

                if(null == io || io.length < 1){return;}
                String[] split = io[1].split(",");
                StringBuffer sBuffer = new StringBuffer();
                sBuffer.append(io[0]);
                sBuffer.append(";");
                for (int i = 0; i < split.length; i++) {
                    String[] windowAndRect = split[i].split("&");
                    sBuffer.append(windowAndRect[0]);
                    sBuffer.append("&");
                    Rectangle rectangle = fixed(windowAndRect[0], sBuffer);
                    if(null != rectangle) {
                        sBuffer.append(rectangle.x);
                        sBuffer.append("-");
                        sBuffer.append(rectangle.y);
                        sBuffer.append("-");
                        sBuffer.append(rectangle.width);
                        sBuffer.append("-");
                        sBuffer.append(rectangle.height);
                    }
                    sBuffer.append(",");
                }
                FileIO.wirteTxtWintab(sBuffer);
                this.dispose();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    /**
     *  固定窗口
     */
    private Rectangle fixed(String windowName,StringBuffer sBuffer){
        WinDef.HWND hwnd = User32.INSTANCE.FindWindow(null, windowName);

        if (hwnd == null) {
            System.out.println("window not find " + windowName);
        } else {
            User32.INSTANCE.SetForegroundWindow(hwnd);
            User32.INSTANCE.ShowWindow(hwnd, SW_RESTORE);
            WinDef.RECT rect = new WinDef.RECT();
            User32.INSTANCE.GetWindowRect(hwnd, rect);
            Rectangle rectangle = rect.toRectangle(); //获得尺寸
            return rectangle;
        }
        return null;
    }
}
