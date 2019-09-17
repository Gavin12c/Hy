package com.hy.model;

import com.hy.model.unit.Action;
import com.hy.model.unit.Pixel;
import lombok.Data;

import java.util.List;

@Data
public class HyModel {
//    private List<Cognition> cognitions;
//    private List<Action> actions;
    private List<Pixel> pixelList;
    private List<Action> handleList;
//    private String handle;
}
