package com.hy.gateway;

import com.hy.model.cognition.Judge;
import com.hy.model.cognition.Pixel;
//import com.hy.service.Login;
import com.hy.service.WinTab;
import com.hy.service.Zero;
import com.hy.utils.Common;
import com.hy.utils.FileIO;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser;
import lombok.Data;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author John 全局监听
 */
@Data
public class InputConfigHook implements Runnable {
    private WinUser.HHOOK hhk;
    private String path = System.getProperty("user.dir");
    private Process game = null;
    private Process mouseInfo = null;

    private volatile boolean zeroFlag = true;
    private volatile boolean winTabFlag = true;
    private WinTab winTab;// 窗口切换
    private Zero zero;// 通用脚本

    private Judge judge;
    private List<Pixel> pi = new ArrayList<>();;

    // 钩子回调函数
    private WinUser.LowLevelKeyboardProc keyboardProc = new WinUser.LowLevelKeyboardProc() {
        public synchronized LRESULT callback(int nCode, WPARAM wParam,
                                             WinUser.KBDLLHOOKSTRUCT event) {
            // 输出按键值和按键时间
            if (nCode >= 0
                    && (WinUser.WM_SYSKEYDOWN == wParam.intValue() || WinUser.WM_KEYDOWN == wParam.intValue())) { // WM_SYSKEYDOWN 系统按键 WM_KEYDOWN 普通按键
                // 按下ESC退出
                switch (event.vkCode) {
                    case 45: // Insert 标记
                        try {
                            // list输入
                            Point point = MouseInfo.getPointerInfo().getLocation();
                            Color c = Common.findScreenPixel(point.x, point.y);
                            pi.add(new Pixel(point.x, point.y, c.getRed(), c.getGreen(),c.getBlue()));
                            judge.setPixels(pi);
                            // 文件输入
                            FileIO.wirteTxt(point.x, point.y, c.getRed(), c.getGreen(),c.getBlue());
                        } catch (AWTException e) {
                            e.printStackTrace();
                        }
                        break;
                }

            }
            return User32.INSTANCE.CallNextHookEx(hhk, nCode, wParam, null);
        }
    };

    public void run() {
        winTab = new WinTab();
        new Thread(winTab).start(); // 窗口切换
        zero = new Zero();
        new Thread(zero).start(); // 脚本
//        new Thread(new Login()).start(); // 自动登录
        setHookOn();
    }

    // 安装钩子
    public void setHookOn() {
        System.out.println("Hook On!");

        HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);
        hhk = User32.INSTANCE.SetWindowsHookEx(User32.WH_KEYBOARD_LL,
                keyboardProc, hMod, 0);

        int result;
        WinUser.MSG msg = new WinUser.MSG();
        while ((result = User32.INSTANCE.GetMessage(msg, null, 0, 0)) != 0) {
            if (result == -1) {
                setHookOff();
                break;
            } else {
                User32.INSTANCE.TranslateMessage(msg);
                User32.INSTANCE.DispatchMessage(msg);
            }
        }
    }

    // 移除钩子并退出
    public void setHookOff() {
        System.out.println("Hook Off!");
        User32.INSTANCE.UnhookWindowsHookEx(hhk);
        System.exit(0);
    }

    /**
     * 运行cmd
     */
    public Process exe(String path) {
        Runtime rt = Runtime.getRuntime();
        try {
            Process exec = rt.exec(path);
            return exec;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * cmd关闭程序
     */
    public static boolean closeProcess(String id) {
        try {
            // Runtime.getRuntime().exec("cmd.exe /C wmic process where processid="+id+" call terminate");
            Runtime.getRuntime().exec("cmd.exe /C taskkill /f /t /pid " + id);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param name 输入需要匹配的名字,来返回该java程序的进程id
     * @return 返回 进程id
     */
    public static String getProcessId(String name) {
        Runtime rt = Runtime.getRuntime(); // 运行时系统获取
        try {
            Process proc = rt
                    .exec("cmd.exe /C wmic process where name=\"javaw.exe\" get processid,CommandLine");// 执行命令
            InputStream stderr = proc.getInputStream();// 执行结果 得到进程的标准输出信息流
            InputStreamReader isr = new InputStreamReader(stderr, "gbk");// 将字节流转化成字符流
            BufferedReader br = new BufferedReader(isr);// 将字符流以缓存的形式一行一行输出
            String line = null;
            while ((line = br.readLine()) != null) {
                if (null != line && !"".equals(line)) {

                    if (line.indexOf(name) != -1) {
                        // System.out.println(line);
                        int end = line.lastIndexOf("\""); // "号最后出现的角标
                        return line.substring(end + 1).trim();
                    }

                }
            }
            br.close();
            isr.close();
            stderr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 获取颜色
     */
//    public static Color getScreenPixel(int x, int y) { // 函数返回值为颜色的RGB值。
//        try {
//            Robot rb = new Robot(); // java.awt.image包中的类，可以用来抓取屏幕，即截屏。
//            Toolkit tk = Toolkit.getDefaultToolkit(); // 获取缺省工具包
//            Dimension di = tk.getScreenSize(); // 屏幕尺寸规格
//            // System.out.println(di.width);
//            // System.out.println(di.height);
//            Rectangle rec = new Rectangle(0, 0, di.width, di.height);
//            BufferedImage bi = rb.createScreenCapture(rec);
//            int pixelColor = bi.getRGB(x, y);
//            Color color = new Color(16777216 + pixelColor);
//            return color; // pixelColor的值为负，经过实践得出：加上颜色最大值就是实际颜色值。
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public Process getGame() {
        return game;
    }

    public void setGame(Process game) {
        this.game = game;
    }

    public Process getMouseInfo() {
        return mouseInfo;
    }

    public void setMouseInfo(Process mouseInfo) {
        this.mouseInfo = mouseInfo;
    }

}