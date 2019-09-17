package com.hy;


import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hy.config.KeyConstants;
import com.hy.model.drawing.DrawingBoard;
import com.hy.utils.EhcacheUtil;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser.HHOOK;
import com.sun.jna.platform.win32.WinUser.HOOKPROC;
import com.sun.jna.platform.win32.WinUser.MSG;
import com.sun.jna.platform.win32.WinDef.LPARAM;

public class MouseHook implements Runnable {

    public static final int WM_MOUSEMOVE = 512;
    public static final int WM_LBUTTONDOWN = 513;
    public static final int WM_LBUTTONUP = 514;
    public static final int WM_RBUTTONDOWN = 516;
    public static final int WM_RBUTTONUP = 517;
    public static final int WM_MBUTTONDOWN = 519;
    public static final int WM_MBUTTONUP = 520;
    private static HHOOK hhk;   //钩子的句柄
    private static LowLevelMouseProc mouseHook;  //
    static User32 lib;   //window应用程序接口
    private boolean isWindows = false;
    private HMODULE hMod;

    public MouseHook() {
        isWindows = Platform.isWindows();
        if (isWindows) {
            lib = User32.INSTANCE;
        }
    }

    /**
     * 定义鼠标钩子,及事件监听回调
     */
    public interface LowLevelMouseProc extends HOOKPROC {
        LRESULT callback(int nCode, WPARAM wParam, MOUSEHOOKSTRUCT lParam);
    }

    public static class MOUSEHOOKSTRUCT extends Structure {
        public static class ByReference extends MOUSEHOOKSTRUCT implements
                Structure.ByReference {
        };
        public User32.POINT pt;  //点坐标
        public HWND hwnd;//窗口句柄
        public int wHitTestCode;
        public User32.ULONG_PTR dwExtraInfo; //扩展信息

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("dwExtraInfo", "hwnd", "pt", "wHitTestCode");
        }
    }

    private boolean LB = false;
    private boolean MB = false;

    public void run() {
//        final ImageApp imageApp = new ImageApp();
        hMod = Kernel32.INSTANCE.GetModuleHandle(null);
        mouseHook = new LowLevelMouseProc() {
            public LRESULT callback(int nCode, WPARAM wParam,
                                    MOUSEHOOKSTRUCT info) {
                if (nCode >= 0) {
                    try {

                        switch (wParam.intValue()) {
                            case MouseHook.WM_LBUTTONDOWN:
                                System.out.println("click left");
                                Point leftP = MouseInfo.getPointerInfo().getLocation();
                                String leftKey = "(L" + leftP.x + "," + leftP.y+")";
                                StringBuffer leftKeyBuff = (StringBuffer) DrawingBoard.findCache(KeyConstants.KEYBUFF, StringBuffer.class); //获取缓存keyBuff
                                DrawingBoard.updateCache(KeyConstants.KEYBUFF, leftKeyBuff.append(leftKey));//添加缓存keyBuff
                                DrawingBoard.reBoardKeyList();//更新画板
                                break;
                            case MouseHook.WM_RBUTTONDOWN:
                                System.out.println("click right");
                                Point rightP = MouseInfo.getPointerInfo().getLocation();
                                String rightKey = "(R" + rightP.x + "," + rightP.y+")";
                                StringBuffer rightKeyBuff = (StringBuffer) DrawingBoard.findCache(KeyConstants.KEYBUFF, StringBuffer.class); //获取缓存keyBuff
                                DrawingBoard.updateCache(KeyConstants.KEYBUFF, rightKeyBuff.append(rightKey));//添加缓存keyBuff
                                DrawingBoard.reBoardKeyList();//更新画板
                                break;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                Pointer pointer = info.getPointer();
                long peer = Pointer.nativeValue(pointer);
                return lib.CallNextHookEx(hhk, nCode, wParam, new LPARAM(peer));
            }
        };
        hhk = lib.SetWindowsHookEx(User32.WH_MOUSE_LL, mouseHook, hMod, 0);
        int result;
        MSG msg = new MSG();
        while ((result = lib.GetMessage(msg, null, 0, 0)) != 0) {
            if (result == -1) {
                System.err.println("error in get message");
                break;
            } else {
                System.err.println("got message");
                lib.TranslateMessage(msg);
                lib.DispatchMessage(msg);
            }
        }
        lib.UnhookWindowsHookEx(hhk);
    }



    public static void main(String[] args) {
        new MouseHook().run();
    }

}


