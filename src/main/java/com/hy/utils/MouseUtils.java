package com.hy.utils;

import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;

public class MouseUtils {

	private static WinUser.INPUT input = new WinUser.INPUT();

	// 以下鼠标事件码，由系统提供
	public static final int MOUSEEVENT_LEFTDOWN = 0x2;// 左键按下
	public static final int MOUSEEVENT_LEFTUP = 0x4;// 左键释放，左键按下与左键释放组合就是一个有意义的集合，那么就代表着一次左键点击
	public static final int MOUSEEVENT_MIDDLEDOWN = 0x20;// 中键按下
	public static final int MOUSEEVENT_MIDDLEUP = 0x40;// 中键释放
	public static final int MOUSEEVENT_RIGHTDOWN = 0x8;// 右键按下
	public static final int MOUSEEVENT_RIGHTUP = 0x10;// 右键释放
	public static final int MOUSEEVENT_MOVE = 0x1;// 鼠标移动，表示相对于上次移动dx,dy。若与下面一个组合，表示鼠标移到dx,dy绝对坐标处
	public static final int MOUSEEVENT_ABSOLUTE = 0x8000;// 指定dx,dy为绝对坐标值，若未指定，则为上次鼠标坐标值的偏移量
	public static final int MOUSEEVENT_WHELL = 0x800;// 鼠标滚轮，

	/**
	 * 移动当前鼠标位置,xy为增减值
	 * 
	 * @throws InterruptedException
	 */
	public static void moveMouse(long x, long y) throws InterruptedException {

		input.type = new WinDef.DWORD(WinUser.INPUT.INPUT_MOUSE);
		input.input.setType("mi");
		// input.input.mi.time = new WinDef.DWORD(0);
		// input.input.mi.mouseData = new WinDef.DWORD(120);
		// //MOUSE_WHELL中键才有意义;120为一个滚轮，不需要则设置0
		input.input.mi.dwExtraInfo = new BaseTSD.ULONG_PTR(0);

		input.input.mi.dx = new WinDef.LONG(0);
		input.input.mi.dy = new WinDef.LONG(0);
		input.input.mi.dwFlags = new WinDef.DWORD(MOUSEEVENT_LEFTDOWN);
		User32.INSTANCE.SendInput(new WinDef.DWORD(1),
				(WinUser.INPUT[]) input.toArray(1), input.size());

		input.input.mi.dx = new WinDef.LONG(x);
		input.input.mi.dy = new WinDef.LONG(y);
		input.input.mi.dwFlags = new WinDef.DWORD(MOUSEEVENT_MOVE);
		User32.INSTANCE.SendInput(new WinDef.DWORD(1),
				(WinUser.INPUT[]) input.toArray(1), input.size());

		Thread.sleep(1000);
		input.input.mi.dwFlags = new WinDef.DWORD(MOUSEEVENT_LEFTUP);
		User32.INSTANCE.SendInput(new WinDef.DWORD(1),
				(WinUser.INPUT[]) input.toArray(1), input.size());
	}

	public static void main(String[] args) {
		// moveMouse(0, -100);
	}
}
