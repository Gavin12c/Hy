package com.hy.utils;

import com.sun.jna.platform.win32.BaseTSD.ULONG_PTR;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.platform.win32.WinDef.LONG;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.INPUT;
import java.awt.MouseInfo;
import java.awt.Point;

/**
 *  mouseData -dwFlags  包含MOUSEEVENTF_WHEEL,滚动的量；正值前前，WHEEL_DELTA
 */
public class Mouse {

    public static final int MOUSEEVENTF_MOVE = 1;
    public static final int MOUSEEVENTF_ABSOLUTE = 0x8000; //坐标绝对值

    public static final int MOUSEEVENTF_LEFTDOWN = 0x0002;
    public static final int MOUSEEVENTF_LEFTUP = 0x0004;
    public static final int MOUSEEVENTF_MIDDLEDOWN = 0x0020;
    public static final int MOUSEEVENTF_MIDDLEUP = 0x0040;

    public static final int MOUSEEVENTF_RIGHTDOWN = 0x0008;
    public static final int MOUSEEVENTF_RIGHTUP = 0x0010;

    public static final int MOUSEEVENTF_WHEEL = 0x0800;  // 移动量在dwData中指定-mouseData


    public static void _winEvent_mi_move(int x,int y){
        mouseAction(x,y,MOUSEEVENTF_ABSOLUTE|MOUSEEVENTF_MOVE);//
    }

    public static void mouseAction(int x,int y,int flags){
        INPUT input = new INPUT();
        input.type = new DWORD(INPUT.INPUT_MOUSE); //MOUSEINPUT
        input.input.setType("mi"); //鼠标事件
        if(x!= -1){
            input.input.mi.dx = new LONG(x);
        }
        if(y!= -1){
            input.input.mi.dy = new LONG(y);
        }
        input.input.mi.time = new DWORD(0);
        input.input.mi.dwExtraInfo = new ULONG_PTR(0); //当前线程上下文信息
        input.input.mi.dwFlags = new DWORD(flags);//具体事件 MOUSEEVENTF_ABSOLUTE绝对值0x8000    MOUSEEVENTF_LEFTDOWN |MOUSEEVENTF_LEFTUP
        User32.INSTANCE.SendInput(new DWORD(1),new INPUT [] {input},input.size());//第一个参数为发送个数 ， 可以为2(MOUSEEVENTF_LEFTDOWN |MOUSEEVENTF_LEFTUP)
    }

    public static void forceMove(int x,int y){
        init_abs_move_0_0:
        {
            Point ip = MouseInfo.getPointerInfo().getLocation ();
            _winEvent_mi_move(-ip.x,-ip.y);
        }
        moveX:
        {
//            while(MouseInfo.getPointerInfo().getLocation().x< x  -  1){
            _winEvent_mi_move(1,0) ;
//        }
        }
        moveY:
        {
//            while(MouseInfo.getPointerInfo().getLocation().y< y  -  1){
            _winEvent_mi_move (0,1);
//        }
        }
        System.out.println(MouseInfo.getPointerInfo().getLocation().toString());
    }

    public static void main(String [] args){
//        forceMove(1000,1000);
//        forceMove(2000,1500);
//        _winEvent_mi_move(100,0);
//        Point ip = MouseInfo.getPointerInfo().getLocation ();
//        System.out.println(String.format("[x:%s ; y:%s]",ip.x,ip.y));
//        mouseAction(100,0,MOUSEEVENTF_ABSOLUTE|MOUSEEVENTF_MOVE);//
        mouseAction(100,0,MOUSEEVENTF_MOVE);//
    }

} 