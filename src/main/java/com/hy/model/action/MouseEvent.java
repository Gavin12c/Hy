package com.hy.model.action;

import lombok.Data;

import java.io.Serializable;

@Data
public class MouseEvent implements Serializable {
    private int x;
    private int y;
}
