package com.hy.utils;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;

import static com.sun.jna.platform.win32.WinUser.SW_HIDE;

public class TestClass {
    public static void main(String[] args) {
        WinDef.HWND hwnd = com.sun.jna.platform.win32.User32.INSTANCE.FindWindow(null, "微信");

        User32.INSTANCE.ShowWindow(hwnd , SW_HIDE);
    }
}
