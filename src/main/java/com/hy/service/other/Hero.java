//package com.hy.service.other;
//
//import java.awt.Robot;
//import java.awt.event.KeyEvent;
//
//import com.hy.utils.Common;
//import com.hy.utils.CommonKey;
//import com.hy.utils.MouseUtils;
//import com.sun.jna.platform.win32.BaseTSD;
//import com.sun.jna.platform.win32.User32;
//import com.sun.jna.platform.win32.WinDef;
//import com.sun.jna.platform.win32.WinUser;
//
///**
// * @author John 刷英雄熟练度-选择英雄
// */
//public class Hero implements Runnable {
//	private volatile boolean flag = true;
//
//	public void stop() {
//		this.flag = false;
//	}
//
//	public void run() {
//		Robot r;
//		try {
//			r = new Robot();
//			while (flag) {
//				// 展开英雄选择页面
//				if (Common.IfYes(1110, 371, 229, 238, 238)) {
//					Thread.sleep(5800);
//					// Common.clickLMove(r,710,400,700,180,1000);
//					// r.mouseMove(710, 400);
//					// MouseUtils.moveMouse(0, -100);
//					// r.mouseMove(710, 400);
//					// MouseUtils.moveMouse(0, -100);
//					// r.mouseMove(710, 400);
//					// MouseUtils.moveMouse(0, -100);
//					// r.mouseMove(710, 400);
//					// MouseUtils.moveMouse(0, -100); //滑动
//
//					for (int j = 0; j < 5; j++) {
//						for (int i = 0; i < 9; i++) {
//							// 收起标点，英雄确定标点
//							if (Common.IfYes(1110, 371, 229, 238, 238)
//									&& Common.IfNo(1125, 628, 121, 95, 54)) {
//								Thread.sleep(400);
//								Common.clickLMouse(new Robot(), 1050 - i * 100,
//										600 - j * 100, 200);
//							}
//						}
//					}
//				}
//				// 纠正
//				if (Common.IfYes(961, 367, 140, 99, 35)
//						|| Common.IfYes(565, 104, 165, 175, 190)) {
//					Common.clickLMouseIfYes(r, 306, 105, 252, 252, 252, 0, 1);
//				}
//				// 对局操作
//				if (Common.IfYes(1226, 250, 14, 179, 0)) {
//					char d = KeyEvent.VK_D;
//					sendChar(d);
//					CommonKey.insertStr("7 8 b f 3 2 1 4 ");// 装备技能升级
//				}
//				Thread.sleep(100);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	private static WinUser.INPUT input = new WinUser.INPUT();
//
//	/**
//	 * @param ch
//	 *            要输出的char值
//	 * @throws InterruptedException
//	 */
//	private static void sendChar(char ch) throws InterruptedException {
//		input.type = new WinDef.DWORD(WinUser.INPUT.INPUT_KEYBOARD);
//		input.input.setType("ki");
//		input.input.ki.wScan = new WinDef.WORD(0);
//		input.input.ki.time = new WinDef.DWORD(0);
//		input.input.ki.dwExtraInfo = new BaseTSD.ULONG_PTR(0);
//		// Press
//		input.input.ki.wVk = new WinDef.WORD(Character.toUpperCase(ch)); // 0x41
//		input.input.ki.dwFlags = new WinDef.DWORD(0); // keydown
//		User32.INSTANCE.SendInput(new WinDef.DWORD(1),
//				(WinUser.INPUT[]) input.toArray(1), input.size());
//		Thread.sleep(2000);
//		// Release
//		input.input.ki.wVk = new WinDef.WORD(Character.toUpperCase(ch)); // 0x41
//		input.input.ki.dwFlags = new WinDef.DWORD(2); // keyup
//		User32.INSTANCE.SendInput(new WinDef.DWORD(1),
//				(WinUser.INPUT[]) input.toArray(1), input.size());
//	}
//
//}
