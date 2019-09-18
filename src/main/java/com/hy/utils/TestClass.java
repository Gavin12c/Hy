package com.hy.utils;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;

import java.awt.*;

import static com.sun.jna.platform.win32.WinUser.SW_HIDE;
import static com.sun.jna.platform.win32.WinUser.SW_RESTORE;

public class TestClass {
    public static void main(String[] args) {
        while(true) {
            WinDef.HWND hwnd = com.sun.jna.platform.win32.User32.INSTANCE.FindWindow(null, "Hy");
            WinDef.RECT rect = new WinDef.RECT();
            User32.INSTANCE.GetWindowRect(hwnd, rect);
            System.out.println(rect);
            Rectangle rectangle = rect.toRectangle();

            User32.INSTANCE.SetForegroundWindow(hwnd); // bring to front
            User32.INSTANCE.MoveWindow(hwnd, rectangle.x, rectangle.y, rectangle.width, rectangle.height, true);
            User32.INSTANCE.ShowWindow(hwnd, SW_RESTORE);
//        User32.INSTANCE.ShowWindow(hwnd , SW_HIDE); //隐藏
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
