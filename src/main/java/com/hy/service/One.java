package com.hy.service;

import com.hy.model.HyModel;
import com.hy.model.unit.Action;
import com.hy.model.unit.MousePointEvent;
import com.hy.model.unit.Pixel;
import com.hy.model.unit.UnitEvent;
import com.hy.utils.Common;
import com.hy.utils.FileIO;
import com.hy.utils.StringUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author John 脚本开启
 */
public class One implements Runnable {

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

	public static void main(String[] args) {
		String[] oneSplit = io("/ifYesXY.txt");
		int delay = (StringUtils.isEmpty(oneSplit[0])) ? 2000 : Integer.parseInt(oneSplit[0]); //第一个参数为 间隔时间 ， 默认2秒
		System.out.println("ifYes执行间隔： " + delay + " 秒");
		List<HyModel> hyList = new ArrayList<>(); // 存储单元
		for (int i = 1; i < oneSplit.length; i++) {
			String[] unit = oneSplit[i].split("&"); //oneSplit[i]为一个单元 &分为-> 判断 ： 操作
			HyModel hyModel = new HyModel();
			hyModel.setPixelList(changePixelList(unit[0].split("-")));// [0] 判断
			hyModel.setHandleList(handleStr2List(unit[1]));// [1] 操作
			hyList.add(hyModel);
		}

		try {
			BufferedImage bi = Common.fullScreen(); //全屏截图
			for (HyModel hyModel : hyList) {
				if(unitJudge(bi,hyModel.getPixelList())){ //判断成立，则执行操作
					unitHandle(hyModel.getHandleList()); //执行操作
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *  判断该judgeList 转换成 pixelList
	 */
	public static List<Pixel> changePixelList(String... judgeList){
		List<Pixel> pixelList = new ArrayList<>();
		for (String judge : judgeList) {
			String[] attr = judge.split(",");
			Pixel pixel = new Pixel();
			pixel.setX(Integer.parseInt(attr[0].trim()));
			pixel.setY(Integer.parseInt(attr[1].trim()));
			pixel.setColor(new Color(Integer.parseInt(attr[2].trim()),Integer.parseInt(attr[3].trim()),Integer.parseInt(attr[4].trim())));
			pixelList.add(pixel);
		}
		return pixelList;
	}

	/**
	 *  操作字符串 转换 List
	 */
	public static List<Action> handleStr2List(String keyString){
		char[] chars = keyString.toCharArray();
		boolean flag = false;
		List<Action> keyList = new ArrayList<>();
		StringBuffer sBuffer = new StringBuffer();
		for (char c : chars) {
			Action action = new Action();
			if(c=='('){
				flag = true;
			}else if(c==')'){
				flag = false;
				String s = sBuffer.toString(); //存取的点
				MousePointEvent mousePointEvent = new MousePointEvent();
				String prefix = s.substring(0, 1);
				String[] body = s.substring(1).split(",");
				mousePointEvent.setX(Integer.parseInt(body[0].trim()));
				mousePointEvent.setY(Integer.parseInt(body[1].trim()));
				mousePointEvent.setType(prefix.equals("L") ? 1 : 2); // 1左键 2右键
				action.setMousePointEvent(mousePointEvent);
				keyList.add(action);
				sBuffer.delete(0,sBuffer.length());
			}else if(!flag){
				action.setKey(""+c);
				keyList.add(action);
			}else if(flag){
				sBuffer.append(c);
			}
		}
		System.out.println(keyList);
		return keyList;

	}

	/**
	 *  单元操作
	 */
	public static void unitHandle(List<Action> handleList){
		for (Action a : handleList) {
			if(null != a.getKey()){

			}
			if(null != a.getMousePointEvent()){

			}
		}
	}

	/**
	 *  单元判断
	 */
	public static boolean unitJudge(BufferedImage bi,List<Pixel> pixelList) throws Exception {
		for (Pixel pixel : pixelList) {
			if(!Common.IfYes(pixel.getX(),pixel.getY(),bi,pixel.getColor())){ //只要有一个不等，则不成立
				return false;
			}
		}
		return true;
	}

	/** 过滤汉字 */
	public static String[] io(String filePath) {
		String t = FileIO.readFile(path + filePath);
		// 过滤汉字
		Pattern pattern = Pattern.compile("[^0-9,;@()-^A-Z]"); //只保留数字、大写字母、,;@()-
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
				for (Integer[] integers : list) {
					Common.clickLMouseIfYes(r, integers[0], integers[1],
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
		One.list3 = list3;
	}

}
