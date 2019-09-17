package com.hy.utils;

import javax.imageio.ImageIO;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;


/**
 *  鼠标
 */
public final class Common {

	/**
	 * 鼠标单击(左击),双击就连续调用
	 * 
	 * @param x
	 *            x坐标
	 * @param y
	 *            y坐标
	 * @param delay
	 *            该操作后的延迟时间
	 */
	public synchronized static void clickLMouse(Robot r, int x, int y, int delay) {
		r.mouseMove(x, y);
		r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		/*
		 * Random random = new Random(); double v = random.nextDouble(); v = 300
		 * + v*300;
		 */
		r.delay(1);
		r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		r.delay(delay);
	}

	// 测试
	public static void main(String[] args) {
//		Robot r;
//		try {
//			r = new Robot();
//			while (true) {
//				Common.clickLMove(r, 710, 600, 510, 180, 1000);
//				Thread.sleep(3000);
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	/**
	 * 鼠标按住移动
	 */
	public synchronized static void clickLMove(Robot r, int startX, int startY,
			int endX, int endY, int delay) {
		r.mouseMove(startX, startY);
		r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		r.delay(500);
		r.mouseMove(endX, endY);
		r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		r.delay(500);
		r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		r.delay(delay);
	}

	/**
	 *  当前坐标颜色
	 * @param x
	 * @param y
	 * @return
	 */
	public static Color findScreenPixel(int x, int y) throws AWTException { // 函数返回值为颜色的RGB值。
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		BufferedImage bi = ScreenCaptureUtils.getScreenshot(new Rectangle(0, 0, d.width, d.height));
		int pixelColor = bi.getRGB(x, y);
		Color color = new Color(16777216 + pixelColor);
		bi = null;
		d = null;
		return color; // pixelColor的值为负，经过实践得出：加上颜色最大值就是实际颜色值。
	}

	/**
	 * 自定义截图
	 */
	public static BufferedImage capturePartScreen(int x, int y, int width,int height) {
		return ScreenCaptureUtils.getScreenshot(new Rectangle(x, y, width, height));
	}

	/**
	 * 全屏截图
	 */
	public static BufferedImage fullScreen() {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		return ScreenCaptureUtils.getScreenshot(new Rectangle(0, 0, d.width, d.height));
	}

	/**
	 * 判断是否点击坐标
	 * 
	 * @param x
	 *            点击x坐标
	 * @param y
	 *            点击y坐标
	 * @param delay
	 *            延迟时间
	 * @param red
	 *            红
	 * @param g
	 *            绿
	 * @param b
	 *            蓝
	 * @throws IOException
	 */
	public static void clickLMouseIfNo(Robot r, int x, int y, int red, int g,
			int b, int delay, int num) throws IOException {
		Color c;
		try {
			c = findScreenPixel(x, y);

			int getRed = Math.abs(c.getRed() - red);
			int getGreen = Math.abs(c.getGreen() - g);
			int getBlue = Math.abs(c.getBlue() - b);

			if (getRed > 10 || getGreen > 10 || getBlue > 10) {
				clickLMouse(r, x, y, delay);
			}
			c = null;
		} catch (AWTException e) {
			System.out.println("屏幕获取失败");
			e.printStackTrace();
		}

		// BufferedImage c = captureFullScreen2(r);
		// int pixel = c.getRGB(x, y);
		// int getRed = (pixel & 0xff0000) >> 16;
		// int getGreen = (pixel & 0xff00) >> 8;
		// int getBlue = (pixel & 0xff);
	}

	/**
	 * 判断是否点击坐标
	 * 
	 * @param x
	 *            点击x坐标
	 * @param y
	 *            点击y坐标
	 * @param delay
	 *            延迟时间
	 * @param red
	 *            红
	 * @param g
	 *            绿
	 * @param b
	 *            蓝
	 * @throws IOException
	 */
	public static void clickLMouseIfYes(Robot r, int x, int y, int red, int g,
			int b, int delay, int num) throws Exception {
		Color c = findScreenPixel(x, y);

		int getRed = Math.abs(c.getRed() - red);
		int getGreen = Math.abs(c.getGreen() - g);
		int getBlue = Math.abs(c.getBlue() - b);

		if (getRed < 10 && getGreen < 10 && getBlue < 10) {
			clickLMouse(r, x, y, delay);
		}
		c = null;

	}

	/**
	 * 鼠标右击
	 */
	public static void clickRMouse(Robot r, int x, int y, int delay) {
		r.mouseMove(x, y);
		r.mousePress(InputEvent.BUTTON3_DOWN_MASK);
		r.delay(10);
		r.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
		r.delay(delay);
	}



	/**
	 * 如果颜色变化，则点击
	 * 
	 * @param r
	 * @param xpan
	 * @param ypan
	 * @throws Exception
	 */
	public static void ifChange(Robot r, int xpan, int ypan) throws Exception {
		HashSet<Integer> hashSet = new HashSet<Integer>();
		for (int i = 0; i < 5; i++) {
			int rgb = findScreenPixel(xpan, ypan).getRGB();
			Thread.sleep(1);
			hashSet.add(rgb);
		}
		if (hashSet.size() != 1) {
			clickLMouse(r, xpan, ypan, 300);
		}
	}

	/**
	 * 判断颜色是否变化
	 * 
	 * @param r
	 * @param x
	 * @param y
	 * @param xpan
	 * @param ypan
	 */
	public static boolean ifChangeT(Robot r, int xpan, int ypan) {
		try {
			HashSet<Integer> hashSet = new HashSet<Integer>();
			for (int i = 0; i < 10; i++) {
				int rgb = findScreenPixel(xpan, ypan).getRGB();
				Thread.sleep(100);
				hashSet.add(rgb);
			}
			if (hashSet.size() != 1) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean IfNo(int xpan, int ypan, int red, int g, int b)throws IOException {
		Color c;
		try {
			c = findScreenPixel(xpan, ypan);
			int getRed = Math.abs(c.getRed() - red);
			int getGreen = Math.abs(c.getGreen() - g);
			int getBlue = Math.abs(c.getBlue() - b);
			if (getRed > 10 || getGreen > 10 || getBlue > 10) {
				return true;
			}
			c = null;
		} catch (AWTException e) {
			e.printStackTrace();
		}
		return false;

	}

	public static boolean IfYes(int xpan, int ypan, int red, int g, int b) throws Exception {
		Color c = findScreenPixel(xpan, ypan);
			Thread.sleep(1);
			int getRed = Math.abs(c.getRed() - red);
			int getGreen = Math.abs(c.getGreen() - g);
			int getBlue = Math.abs(c.getBlue() - b);
			if (getRed < 10 && getGreen < 10 && getBlue < 10) {
				return true;
			}
		return false;
	}

	public static boolean IfYes(int xpan, int ypan,BufferedImage bi, Color toColor) throws Exception {
		Color nowColor = new Color(16777216 + bi.getRGB(xpan, ypan));
		int getRed = Math.abs(nowColor.getRed() - toColor.getRed());
		int getGreen = Math.abs(nowColor.getGreen() - toColor.getGreen());
		int getBlue = Math.abs(nowColor.getBlue() - toColor.getBlue());
		if (getRed < 10 && getGreen < 10 && getBlue < 10) {
			return true;
		}
		return false;
	}

}
