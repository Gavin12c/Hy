package com.hy;

import com.hy.service.Login;
import com.hy.service.WinTab;
import com.hy.service.Zero;
import com.hy.service.other.Hero;
import com.hy.service.other.Skilled;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.*;
import com.sun.jna.platform.win32.WinUser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

/**
 * @author John 全局监听
 */
public class KeyboardHook implements Runnable {
	private WinUser.HHOOK hhk;
	private String path = System.getProperty("user.dir");
	private Process game = null;
	private Process mouseInfo = null;

	private volatile boolean zeroFlag = true;
	private volatile boolean winTabFlag = true;
	private WinTab winTab;// 窗口切换
	private Zero zero;// 通用脚本

	// 钩子回调函数
	private WinUser.LowLevelKeyboardProc keyboardProc = new WinUser.LowLevelKeyboardProc() {
		public synchronized LRESULT callback(int nCode, WPARAM wParam,
				WinUser.KBDLLHOOKSTRUCT event) {
			// 输出按键值和按键时间
			if (nCode >= 0
					&& (WinUser.WM_SYSKEYDOWN == wParam.intValue() || WinUser.WM_KEYDOWN == wParam
							.intValue())) { // WM_SYSKEYDOWN 系统按键 WM_KEYDOWN
											// 普通按键
				// 按下ESC退出
				switch (event.vkCode) {

				case 27: // ESC
					System.exit(0);
					break;

				case 112: // F1 通用脚本
					if (zeroFlag && null != zero) {
						zero.stop();
						zeroFlag = false;// 开关打开状态，则关闭并修改状态
					} else {
						zero = new Zero();
						zero.start();
						new Thread(zero).start();
						zeroFlag = true;
					}
					break;

				case 113: // F2 窗口切换
					if (winTabFlag && null != winTab) {
						winTab.stop();
						winTabFlag = false;// 开关打开状态，则关闭并修改状态
					} else {
						winTab = new WinTab();
						winTab.start();
						new Thread(winTab).start();
						winTabFlag = true;
					}
					break;

				case 114: // F3 选择英雄
					new Thread(new Hero()).start();
					new Thread(new Skilled()).start();
					break;

				case 115: // F4 全部关闭
					if (null != zero) {
						zero.stop();
						zeroFlag = false;
					}
					if (null != winTab) {
						winTab.stop();
						winTabFlag = false;
					}
					break;

				case 116: // F5 //打开辅助工具
					if (null == getMouseInfo()) {
						Process mouseInfo = exe("cmd.exe /C " + path
								+ "\\MouseInfo.jar");
						setMouseInfo(mouseInfo);
						System.out.println("打开坐标工具");
					}
					break;
				case 117: // F6 关闭辅助工具
					if (null != getMouseInfo()) {
						String id = getProcessId("MouseInfo.jar");
						setMouseInfo(null); // 重置开启按钮
						if (closeProcess(id)) {
							System.out.println("关闭坐标工具");
						}
					}
					break;
				case 45: // Insert 标记
					// list输入
					Point point = java.awt.MouseInfo.getPointerInfo()
							.getLocation();
					Color c = getScreenPixel(point.x, point.y);
					Integer[] p = { point.x, point.y, c.getRed(), c.getGreen(),
							c.getBlue() };
					List<Integer[]> list3 = Zero.getList3();
					list3.add(p);
					Zero.setList3(list3);
					// 文件输入
					wirteTxt(point.x, point.y, c.getRed(), c.getGreen(),
							c.getBlue());
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
		new Thread(new Login()).start(); // 自动登录
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

	/** 运行cmd */
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

	/** cmd关闭程序 */
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
	 * @param name
	 *            输入需要匹配的名字,来返回该java程序的进程id
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
	 * 写入txt文件
	 */
	public static void wirteTxt(int x, int y, int r, int g, int b) {
		String text = "";
		try {
			File file1 = new File(System.getProperty("user.dir")
					+ "/ifYesXY.txt");
			Writer writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file1, true), "UTF-8"));
			writer.write(text + "\r\n" + ";" + x + "," + y + "," + r + "," + g
					+ "," + b + "\r\n");
			writer.flush();
			writer.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 获取颜色
	 */
	public static Color getScreenPixel(int x, int y) { // 函数返回值为颜色的RGB值。
		try {
			Robot rb = new Robot(); // java.awt.image包中的类，可以用来抓取屏幕，即截屏。
			Toolkit tk = Toolkit.getDefaultToolkit(); // 获取缺省工具包
			Dimension di = tk.getScreenSize(); // 屏幕尺寸规格
			// System.out.println(di.width);
			// System.out.println(di.height);
			Rectangle rec = new Rectangle(0, 0, di.width, di.height);
			BufferedImage bi = rb.createScreenCapture(rec);
			int pixelColor = bi.getRGB(x, y);
			Color color = new Color(16777216 + pixelColor);
			return color; // pixelColor的值为负，经过实践得出：加上颜色最大值就是实际颜色值。
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

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