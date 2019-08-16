package com.hy.service;

import java.awt.Robot;

import com.hy.utils.Common;
import com.hy.utils.FileIO;
import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;

/**
 * @author John 键盘输入-账号密码登录
 */
public class Login implements Runnable {
	private static WinUser.INPUT input = new WinUser.INPUT();
	private volatile static boolean flag = true;

	public void stop() {
		flag = false;
	}

	public void start() {
		flag = true;
	}

	// 装配文件路径
	final static String path = System.getProperty("user.dir");

	static String[] io = io("/login.txt");
	private static boolean weixin = io[0].equals("1");
	private static boolean qq = io[0].equals("2");

	/**
	 * 登录
	 */
	public void login() {
		try {
			if (weixin) {
				Common.clickLMouseIfYes(new Robot(), 591, 547, 58, 128, 66, 1,
						0);
			} else if (qq) {
				Common.clickLMouseIfYes(new Robot(), 790, 560, 220, 108, 108,
						1, 0);
			}
			// 是微信登录页面 - 录字
			if (weixin && Common.IfYes(752, 163, 53, 53, 53)) {

				Robot r = new Robot();
				Common.clickLMouseIfYes(r, 823, 398, 81, 193, 81, 1, 0);// 密码错误点击继续
				// 账号
				Common.clickLMouseIfYes(r, 835, 213, 242, 242, 242, 1, 0);// 点击账号行上
				Thread.sleep(2000);
				Common.clickLMouseIfYes(r, 881, 212, 184, 184, 184, 1, 0);// 点击×
				Thread.sleep(2000);
				insertStr("18318583005");
				Thread.sleep(2000);
				// 密码
				Common.clickLMouseIfYes(r, 838, 249, 242, 242, 242, 1, 0);// 点击密码行上
				Thread.sleep(2000);
				Common.clickLMouseIfYes(r, 880, 245, 181, 181, 181, 1, 0);// 点击×
				Thread.sleep(2000);
				insertStr("woshiyao");

				Thread.sleep(2000);
				Common.clickLMouseIfYes(r, 786, 289, 26, 173, 25, 1, 0);// 点击登录
			} else if (weixin && Common.IfYes(618, 272, 104, 104, 104)) {
				Robot r = new Robot();
				Common.clickLMouseIfYes(r, 806, 273, 242, 242, 242, 1, 0);
				Thread.sleep(2000);
				insertStr("woshiyao");
			}

			// 是否qq页面 - 录字 470,148,0,0,0
			if (qq && Common.IfYes(470, 148, 0, 0, 0)) {
				Robot r = new Robot();

				// 点击登录
				Common.clickLMouseIfYes(r, 906, 449, 18, 184, 246, 1, 0);

				// 账号
				Common.clickLMouseIfYes(r, 810, 267, 255, 255, 255, 1, 0);// 点击账号行上
				Thread.sleep(2000);
				Common.clickLMouseIfYes(r, 1212, 257, 189, 187, 190, 1, 0);// 点击×
				Thread.sleep(2000);
				insertStr("601801874");
				Thread.sleep(2000);
				// 密码
				Common.clickLMouseIfYes(r, 812, 329, 255, 255, 255, 1, 0);// 点击密码行上
				Thread.sleep(2000);
				Common.clickLMouseIfYes(r, 1211, 324, 185, 186, 187, 1, 0);// 点击×
				Thread.sleep(2000);
				insertStr("huang6885635");

				Thread.sleep(2000);
				Common.clickLMouseIfYes(r, 886, 401, 18, 184, 246, 1, 0);// 点击登录
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			while (flag) {
				login();
				Thread.sleep(7000);
			}
		} catch (InterruptedException e) {
		}
	}

	/**
	 * @param ch
	 *            要输出的char值
	 */
	private static void sendChar(char ch) {
		input.type = new WinDef.DWORD(WinUser.INPUT.INPUT_KEYBOARD);
		input.input.setType("ki");
		input.input.ki.wScan = new WinDef.WORD(0);
		input.input.ki.time = new WinDef.DWORD(0);
		input.input.ki.dwExtraInfo = new BaseTSD.ULONG_PTR(0);
		// Press
		input.input.ki.wVk = new WinDef.WORD(Character.toUpperCase(ch)); // 0x41
		input.input.ki.dwFlags = new WinDef.DWORD(0); // keydown
		User32.INSTANCE.SendInput(new WinDef.DWORD(1),
				(WinUser.INPUT[]) input.toArray(1), input.size());
		// Release
		input.input.ki.wVk = new WinDef.WORD(Character.toUpperCase(ch)); // 0x41
		input.input.ki.dwFlags = new WinDef.DWORD(2); // keyup
		User32.INSTANCE.SendInput(new WinDef.DWORD(1),
				(WinUser.INPUT[]) input.toArray(1), input.size());
	}

	/**
	 * 键盘输出字符串
	 */
	private static void insertStr(String str) {
		try {
			char[] charArray = str.toCharArray();
			for (char c : charArray) {
				sendChar(c);
				Thread.sleep(100);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static String[] io(String filePath) {
		String t = FileIO.readFile(path + filePath);
		return t.split(";");
	}

}
