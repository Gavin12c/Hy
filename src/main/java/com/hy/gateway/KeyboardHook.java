package com.hy.gateway;

import com.hy.config.KeyConstants;
import com.hy.frame.CommandFrame;
import com.hy.frame.EditFrame;
import com.hy.model.unit.Pixel;
import com.hy.model.drawing.DrawingBoard;
import com.hy.service.One;
import com.hy.service.WinTab;
import com.hy.utils.Common;
import com.hy.utils.DosCommand;
import com.hy.utils.FileIO;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.*;
import com.sun.jna.platform.win32.WinUser;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author John 全局监听
 */
public class KeyboardHook implements Runnable {
	private static WinUser.HHOOK hhk;
	private String path = System.getProperty("user.dir");
	private Process game = null;
	private Process mouseInfo = null;

	private volatile boolean oneFlag = true;
	private volatile boolean winTabFlag = true;
//	private volatile boolean editFlag = ; //编辑开关
	private WinTab winTab;// 窗口切换
	private One one;// 通用脚本

	private List<Pixel> colorList = new ArrayList<>();
	private static EditFrame editFrame;
	private volatile boolean  flag = false; //切换开关 false->标识
	private volatile boolean  hideFlag = true; //切换开关 false->标识

	public static void main(String[] args) {
		new KeyboardHook().run();
	}


	// 钩子回调函数
	private WinUser.LowLevelKeyboardProc keyboardProc = new WinUser.LowLevelKeyboardProc() {
		public synchronized LRESULT callback(int nCode, WPARAM wParam,WinUser.KBDLLHOOKSTRUCT event) {
			// 输出按键值和按键时间
			if (nCode >= 0
					&& (WinUser.WM_SYSKEYDOWN == wParam.intValue() || WinUser.WM_KEYDOWN == wParam.intValue())) { // WM_SYSKEYDOWN 系统按键 WM_KEYDOWN 普通按键

				try {
					int vkCode = event.vkCode;
					// 按下ESC退出
					switch (vkCode) {
							case 27: // ESC
								System.exit(0);
								break;

							case 112: // F1 通用脚本
								if (oneFlag && null != one) {
									one.stop();
									oneFlag = false;// 开关打开状态，则关闭并修改状态
								} else {
									one = new One();
									one.start();
									new Thread(one).start();
									oneFlag = true;
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

							case 114: // F3 开启编辑
								DrawingBoard.setEdit(!DrawingBoard.getEdit());
								if(!DrawingBoard.getEdit()){ //关闭编辑模式时
									clearAllBoard(); //清除内容
								}
								break;
							case 115: // F4 全部关闭
								if (null != one) {
									one.stop();
									oneFlag = false;
								}
								if (null != winTab) {
									winTab.stop();
									winTabFlag = false;
								}
								break;

							case 116: // F5 开启命令
								new CommandFrame();
								break;
							case 117: // F6 打开、关闭辅助工具
								if (null != getMouseInfo()) {
									String id = DosCommand.getProcessId("MouseInfo.jar");
									setMouseInfo(null); // 重置开启按钮
									if (DosCommand.closeProcess(id)) {
										System.out.println("关闭坐标工具");
									}
								}else{
									Process mouseInfo = DosCommand.exe("cmd.exe /C " + path
											+ "\\MouseInfo.jar");
									setMouseInfo(mouseInfo);
									System.out.println("打开坐标工具");
								}
								break;
						}

						if(DrawingBoard.getEdit()){
							colorSwitch(vkCode);//标识转换
							keySwitch(vkCode);//字母数字转换
						}



				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			return User32.INSTANCE.CallNextHookEx(hhk, nCode, wParam, null);
		}
	};

	/**
	 *  清空board color keybuff
	 */
	private void clearAllBoard() throws IllegalAccessException, InstantiationException {
		if(null != colorList){
			KeyboardHook.this.colorList.clear();
			DrawingBoard.delCache(KeyConstants.COLOR);
		}
		StringBuffer keyBuff = (StringBuffer) DrawingBoard.findCache(KeyConstants.KEYBUFF, StringBuffer.class); //获取缓存keyBuff
		if(null == keyBuff || keyBuff.length()>0){
			keyBuff.delete(0,keyBuff.length());
			DrawingBoard.delCache(KeyConstants.KEYBUFF);
		}
		EditFrame board = (EditFrame) DrawingBoard.findCache(KeyConstants.BOARD,EditFrame.class);
		board.dispose();
	}

	//标识转换
	private void colorSwitch(int code) throws Exception {
		switch (code) {
			case 118: // F7 标记
				this.flag = false;
				Point nowPoint = MouseInfo.getPointerInfo().getLocation();
				Color nowC = Common.findScreenPixel(nowPoint.x, nowPoint.y);
				Pixel pixel = new Pixel();
				pixel.setX(nowPoint.x);pixel.setY(nowPoint.y);pixel.setColor(nowC);
				colorList.add(pixel);
				DrawingBoard.updateCache(KeyConstants.COLOR,colorList);//更新color
				DrawingBoard.reBoardColorList();
				break;
			case 119: // F8 退回
				if(flag){ //true -》key
					StringBuffer keyBuff = (StringBuffer) DrawingBoard.findCache(KeyConstants.KEYBUFF, StringBuffer.class); //获取缓存keyBuff
					if(keyBuff.length()>0){
						keyBuff.deleteCharAt(keyBuff.length()-1);
					}
					DrawingBoard.updateCache(KeyConstants.KEYBUFF,keyBuff);//更新画板keybuff
					DrawingBoard.reBoardKeyList();
				}else{
					if(colorList.size()>0) {
						colorList.remove(colorList.size() - 1);
					}
					DrawingBoard.updateCache(KeyConstants.COLOR,colorList);//更新color
					DrawingBoard.reBoardColorList(); //更新画板
				}
				break;
			case 120: // F9 切换画板
				if(flag = !flag){
					DrawingBoard.reBoardKeyList();
				}else{
					DrawingBoard.reBoardColorList();
				}
				break;
			case 121: // F10 显示隐藏
				EditFrame hideBoard = (EditFrame) DrawingBoard.findCache(KeyConstants.BOARD,EditFrame.class);
				hideBoard.setVisible(hideFlag = !hideFlag);
				DrawingBoard.setVisible(hideFlag); //设置缓存，画板从缓存判断是否显示
				break;
			case 123: // F12 清空
				clearAllBoard();
				break;
			case 35: // End 写入并清空
				FileIO.wirteTxtAction((ArrayList)DrawingBoard.findCache(KeyConstants.COLOR,ArrayList.class),(StringBuffer)DrawingBoard.findCache(KeyConstants.KEYBUFF,StringBuffer.class));
				clearAllBoard();
				break;
		}
	}

	//字母数字转换
	private void keySwitch(int code) throws Exception {
		String keylist = "ZXCVBNMASDFGHJKLQWERTYUIOP1234567890 ";
		for (char c :keylist.toCharArray()) {
			if((int)c == code){
				this.flag = true;
//				keyBuff.append(c);
				StringBuffer keyBuff = (StringBuffer) DrawingBoard.findCache(KeyConstants.KEYBUFF, StringBuffer.class); //获取缓存keyBuff
				DrawingBoard.updateCache(KeyConstants.KEYBUFF, keyBuff.append(c));//添加缓存keyBuff
				DrawingBoard.updateCache(KeyConstants.KEYBUFF,keyBuff);//更新画板keybuff
				DrawingBoard.reBoardKeyList();
				break;
			}
		}
	}

	public void run() {
//		winTab = new WinTab();
//		new Thread(winTab).start(); // 窗口切换
//		zero = new Zero();
//		new Thread(zero).start(); // 脚本
//		new Thread(new Login()).start(); // 自动登录
		setHookOn();
	}

	// 安装钩子
	public void setHookOn() {
		System.out.println("Hook On!");

		HMODULE hModKey = Kernel32.INSTANCE.GetModuleHandle(null);
		hhk = User32.INSTANCE.SetWindowsHookEx(User32.WH_KEYBOARD_LL,
				keyboardProc, hModKey, 0);

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

	public List<Pixel> getColorList() {
		return colorList;
	}

	public void setColorList(List<Pixel> colorList) {
		this.colorList = colorList;
	}

	public EditFrame getUnTitleFrame() {
		return editFrame;
	}

	public void setUnTitleFrame(EditFrame editFrame) {
		this.editFrame = editFrame;
	}
}