package com.hy.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *  dos 命令类
 */
public class DosCommand {

    /** 运行cmd */
    public static Process exe(String path) {
        Runtime rt = Runtime.getRuntime();
        try {
            Process exec = rt.exec(path);
            return exec;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
}
