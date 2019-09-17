package com.hy.model.cognition;

import com.hy.model.unit.Pixel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *  触发判断
 */
@Data
public class Judge implements Serializable {
    private List<Pixel> pixels; //像素集合
}
