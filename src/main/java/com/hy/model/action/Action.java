package com.hy.model.action;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *  行为
 */
@Data
public class Action implements Serializable {
    private List<MouseEvent> mouseEventList;
    private List<KeyEvent> keyEventList;
}
