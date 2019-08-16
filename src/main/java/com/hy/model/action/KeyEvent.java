package com.hy.model.action;

import lombok.Data;

import java.io.Serializable;

@Data
public class KeyEvent implements Serializable {
    private String keyStr;
}
