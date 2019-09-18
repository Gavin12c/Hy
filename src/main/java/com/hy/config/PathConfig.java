package com.hy.config;

import com.hy.utils.FileIO;

public class PathConfig {
    // 装配文件路径
    final static String path = System.getProperty("user.dir");

    public static String[] io(String filePath) {
        String t = FileIO.readFile(path + filePath);
        return t.split(";");
    }
}
