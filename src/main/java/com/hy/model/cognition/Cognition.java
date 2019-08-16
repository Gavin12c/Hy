package com.hy.model.cognition;

import lombok.Data;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.List;

/**
 *  认知
 */
@Data
public class Cognition implements Serializable {
    private BufferedImage bi;   //图片
    private List<Judge> judgeList; //多条件判断
}
