package com.hy.model.unit;

import lombok.Data;

@Data
public class MousePointEvent {
    private int x;
    private int y;
    private int type; // 1 左键 2右键
}
