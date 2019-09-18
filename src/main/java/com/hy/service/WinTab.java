package com.hy.service;

import com.hy.config.PathConfig;
import com.hy.utils.StringUtils;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser;
import com.hy.utils.FileIO;

import static com.sun.jna.platform.win32.WinUser.SW_HIDE;

/**
 * @author John 窗口切换
 */
public class WinTab implements Runnable {

	private volatile static boolean flag = true;

	public void stop() {
		flag = false;
	}

	public void start() {
		flag = true;
	}


	static String[] io = PathConfig.io("/WinTab.txt");
	static String[] split = null;
	static int delay = StringUtils.isEmpty(io[0]) ? 2000 : Integer.parseInt(io[0].trim());

	/**
	 * @param delay
	 *            切换间隔时间
	 */
	public static void go(int delay) throws Exception {
		if(null == io){
			return;
		}
		split = io[1].split(",");
		System.out.println(split.length);
		for (int i = 0; i < split.length; i++) {
			tab(split[i], delay);
		}
	}


	public static synchronized void tab(String name, int delay) throws InterruptedException {
		String[] windowAndRect = name.split("&");
		HWND hwnd = User32.INSTANCE.FindWindow(null, windowAndRect[0]);
		if (hwnd == null) {
			System.out.println("window not find");
			Thread.sleep(delay);
		} else {
			User32.INSTANCE.ShowWindow(hwnd, WinUser.SW_RESTORE); // SW_RESTORE
																	// 9 窗口恢复
			User32.INSTANCE.SetForegroundWindow(hwnd); // bring to front
			String[] rect = windowAndRect[1].split("-");
			User32.INSTANCE.MoveWindow(hwnd, Integer.parseInt(rect[0].trim()), Integer.parseInt(rect[1].trim()), Integer.parseInt(rect[2].trim()), Integer.parseInt(rect[3].trim()), true);
			Thread.sleep(delay);
			if (WinTab.split.length > 1) {
				User32.INSTANCE.ShowWindow(hwnd, WinUser.SW_MINIMIZE);// 窗口最小化
			}
			System.out.println(name + " window");
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

}