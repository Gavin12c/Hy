package com.hy.service;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser;
import com.hy.utils.FileIO;

import java.awt.*;

/**
 * @author John 窗口切换
 */
public class WinTab implements Runnable {

	private volatile static boolean flag = true;
	private int delay = 3000;// 切换时间

	public void stop() {
		flag = false;
	}

	public void start() {
		flag = true;
	}

	// 装配文件路径
	final static String path = System.getProperty("user.dir");

	static String[] io = io("/winTab.txt");
	static String[] split = io[1].split(",");

	/**
	 * @param delay
	 *            切换间隔时间
	 */
	public static void go(int delay) throws Exception {
		System.out.println(split.length);
		for (int i = 0; i < split.length; i++) {
			tab(split[i], delay);
		}
	}

	public static String[] io(String filePath) {
		String t = FileIO.readFile(path + filePath);
		return t.split(";");
	}

	public static synchronized void tab(String name, int delay)
			throws InterruptedException {
		HWND hwnd = User32.INSTANCE.FindWindow(null, name);
		if (hwnd == null) {
			System.out.println("窗口不存在");
			Thread.sleep(delay);
		} else {
			User32.INSTANCE.ShowWindow(hwnd, WinUser.SW_RESTORE); // SW_RESTORE
																	// 9 窗口恢复
			User32.INSTANCE.SetForegroundWindow(hwnd); // bring to front
			User32.INSTANCE.MoveWindow(hwnd, 230, 50, 1063, 608, true);
			Thread.sleep(delay);
			if (split.length > 1) {
				User32.INSTANCE.ShowWindow(hwnd, WinUser.SW_MINIMIZE);// 窗口最小化
			}
			System.out.println(name + " 窗口");
		}
	}

	public void run() {
		try {
			while (flag) {
				go(delay);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *   固定窗口
	 * @param name
	 * @param delay
	 * @throws InterruptedException
	 */
	public static synchronized void tab2(String name, int delay)
			throws InterruptedException {
		HWND hwnd = User32.INSTANCE.FindWindow(null, name);
		if (hwnd == null) {
			System.out.println("窗口不存在");
			Thread.sleep(delay);
		} else {
			User32.INSTANCE.ShowWindow(hwnd, WinUser.SW_RESTORE); // SW_RESTORE
			// 9 窗口恢复
			User32.INSTANCE.SetForegroundWindow(hwnd); // bring to front
			WinDef.RECT r = new WinDef.RECT();
			User32.INSTANCE.GetWindowRect(hwnd,r);
			Rectangle rectangle = r.toRectangle();
			System.out.println(rectangle);


			User32.INSTANCE.MoveWindow(hwnd, rectangle.x, rectangle.y, rectangle.width, rectangle.height, true);
			Thread.sleep(delay);
			if (split.length > 1) {
				User32.INSTANCE.ShowWindow(hwnd, WinUser.SW_MINIMIZE);// 窗口最小化
			}
			System.out.println(name + " 窗口");
		}
	}

	public static void main(String[] args) {
		try {
			tab2("Hy",2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}