package com.hy.utils;

import java.io.*;
import java.nio.charset.Charset;

public class FileIO {

	public static String readFile(String filePath){
		File file = new File(filePath);
		try {
			if (file.exists()) {
				InputStream in = new FileInputStream(file);
				byte[] buf = new byte[1024];
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				int readIndex;
				while ((readIndex = in.read(buf)) != -1) {
					out.write(buf, 0, readIndex);
				}

				in.close();
				return new String(out.toByteArray(), Charset.forName("UTF-8"));
			}else {
				System.out.println("找不到指定的文件 : " + filePath);
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			throw new RuntimeException(e);
		}
		return "";
	}

	/**
	 * 写入txt文件
	 */
	public static void wirteTxt(int x, int y, int r, int g, int b) {
		String text = "";
		try {
			File file = new File(System.getProperty("user.dir")+ "/ifYesXY.txt");
			Writer writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file, true), "UTF-8"));
			writer.write(text + "\r\n" + ";" + x + "," + y + "," + r + "," + g
					+ "," + b + "\r\n");
			writer.flush();
			writer.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
