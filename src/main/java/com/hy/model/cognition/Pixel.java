package com.hy.model.cognition;

import lombok.Data;

import java.io.Serializable;

/**
 *  像素
 */
@Data
public class Pixel implements Serializable {
    private int x;
    private int y;
    private int r;
    private int g;
    private int b;

    public Pixel(int x, int y, int r, int g, int b) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Pixel(){

    }
}
