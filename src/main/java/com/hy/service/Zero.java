package com.hy.service;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hy.Monitor;
import com.hy.utils.Common;
import com.hy.utils.FileIO;
import com.hy.utils.ScreenCaptureUtils;
import com.hy.utils.StringUtils;

/**
 * @author John 脚本开启
 */
public class Zero implements Runnable {

	// 装配文件路径
	private final static String path = System.getProperty("user.dir");

	private static List<Integer[]> list3;// ifYes

	private volatile static boolean flag = true;

	public void stop() {
		flag = false;
	}

	public void start() {
		flag = true;
	}

	public static void main(String[] args) throws Exception {
		// go();
		new Monitor();

	}

	public void run() {
		try {
			// 判断是rbg颜色，就点
			String[] split3 = io("/ifYesXY.txt");
			// LinkedList add,del 有优势,arraylist 适用查找
//			list3 = new ArrayList<>();
			for (int i = 1; i < split3.length; i++) {
				String[] smap3 = split3[i].split(",");
				Integer[] point = { Integer.parseInt(smap3[0]),
						Integer.parseInt(smap3[1]), Integer.parseInt(smap3[2]),
						Integer.parseInt(smap3[3]), Integer.parseInt(smap3[4]) };
				list3.add(point);
			}
			Robot r = new Robot();
			int delay;
			System.out.println("ifYes执行间隔： " + split3[0] + " 秒");
			if (StringUtils.isEmpty(split3[0])) {
				delay = 3000;
			} else {
				delay = Integer.parseInt(split3[0]);
			}
			while (flag) {
				ifYesXY(getList3(), r);
				// System.out.println(1);
				Thread.sleep(delay);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 过滤汉字 */
	public static String[] io(String filePath) {
		String t = FileIO.readFile(path + filePath);
		// 过滤汉字
		Pattern pattern = Pattern.compile("[^0-9,;;]");
		Matcher matcher = pattern.matcher(t);
		String all = matcher.replaceAll("");
		return all.split(";");
	}

	/**
	 * @param list
	 *            坐标集合
	 */
	public synchronized static void ifYesXY(final List<Integer[]> list,
			final Robot r) {
		try {
			if (list != null && list.size() > 0
					&& Common.IfNo(1127, 630, 206, 207, 225)) {
				Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
				BufferedImage bi = ScreenCaptureUtils.getScreenshot(new Rectangle(0, 0, d.width, d.height));
				for (Integer[] integers : list) {
					Color c = Common.findScreenPixel(integers[0], integers[1]);
					Common.clickLMouseIfYes(c,r, integers[0], integers[1],
							integers[2], integers[3], integers[4], 1, 0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<Integer[]> getList3() {
		return list3;
	}

	public static void setList3(List<Integer[]> list3) {
		Zero.list3 = list3;
	}

}
