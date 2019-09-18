package com.hy.config;

import com.google.gson.reflect.TypeToken;
import com.hy.model.HyModel;
import com.hy.utils.FileIO;
import com.hy.utils.JsonUtil;
import lombok.Data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Data
public class Config implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 配置文件为config.json */
    public static final String CONFIG_FILE;
    public List<HyModel> hyModels;

    static {
        // 配置信息存目录
        String dataPath = System.getProperty("user.dir") + "/" + "config/";
        File file = new File(dataPath);
        if (!file.isDirectory()) {
            file.mkdir();
        }
        CONFIG_FILE = dataPath + "/config.json";
    }

    private Config(){
        update(null);
    }

    /**
     * 解析配置文件
     */
    public void update(String configJson) {

        if(configJson == null){
            configJson = FileIO.readFile(CONFIG_FILE);
        }
        List<HyModel> hyModels = JsonUtil.json2object(configJson, new TypeToken<List<HyModel>>() {});
        if (hyModels == null) {
            hyModels = new ArrayList<>();
        }

        this.hyModels = hyModels;
    }
}
