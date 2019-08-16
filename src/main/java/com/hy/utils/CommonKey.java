package com.hy.utils;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;

public class CommonKey {

    public static WinUser.INPUT input = new WinUser.INPUT();

    public static void sendChar2(char ch) throws InterruptedException {
        input.type = new WinDef.DWORD(WinUser.INPUT.INPUT_KEYBOARD);
        input.input.setType("ki");
        input.input.ki.wScan = new WinDef.WORD(0);
        input.input.ki.time = new WinDef.DWORD(0);
        input.input.ki.dwExtraInfo = new BaseTSD.ULONG_PTR(0);
        // Press
        input.input.ki.wVk = new WinDef.WORD(Character.toUpperCase(ch)); // 0x41
        input.input.ki.dwFlags = new WinDef.DWORD(0); // keydown
        com.sun.jna.platform.win32.User32.INSTANCE.SendInput(new WinDef.DWORD(1),
                (WinUser.INPUT[]) input.toArray(1), input.size());
        // Release
        input.input.ki.wVk = new WinDef.WORD(Character.toUpperCase(ch)); // 0x41
        input.input.ki.dwFlags = new WinDef.DWORD(2); // keyup
        User32.INSTANCE.SendInput(new WinDef.DWORD(1),
                (WinUser.INPUT[]) input.toArray(1), input.size());
    }

    public static void main(String[] args) {
        input.type = new WinDef.DWORD(WinUser.INPUT.INPUT_MOUSE);
        input.input.setType("mi");
        input.input.mi.dx = new WinDef.LONG(0);
        input.input.mi.dy = new WinDef.LONG(0);
        input.input.mi.dwExtraInfo = new BaseTSD.ULONG_PTR(0);
        input.input.mi.dwFlags = new WinDef.DWORD(0);
        input.input.mi.mouseData = new WinDef.DWORD(0);
    }

    /**
     * 键盘输出字符串
     */
    public static void insertStr(String str) {
        try {
            char[] charArray = str.toCharArray();
            for (char c : charArray) {
                sendChar2(c);
                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
