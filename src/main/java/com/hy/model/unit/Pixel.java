package com.hy.model.unit;

import lombok.Data;

import java.awt.*;
import java.io.Serializable;

/**
 *  像素
 */
@Data
public class Pixel implements Serializable {
    private int x;
    private int y;
    private Color color;

}
