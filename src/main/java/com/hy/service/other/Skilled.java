package com.hy.service.other;

import java.io.IOException;

import com.hy.utils.Common;
import com.hy.utils.CommonKey;
import com.hy.utils.FileIO;

public class Skilled implements Runnable {

	private volatile boolean flag = true;

	public void stop() {
		this.flag = false;
	}

	// 装配文件路径
	final static String path = System.getProperty("user.dir");

	@Override
	public void run() {
		try {
			while (flag) {

				// q e r t 9 0
				String key = FileIO.readFile(path + "/key.txt");

				// 对局操作
				if (Common.IfYes(1226, 250, 14, 179, 0)) {
					CommonKey.insertStr(key); // 技能释放
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		String t = FileIO.readFile(path + "/key.txt");
		System.out.println(t);
	}

}
