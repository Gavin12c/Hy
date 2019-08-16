package com.hy.model;

import com.hy.model.action.Action;
import com.hy.model.cognition.Cognition;
import lombok.Data;

import java.util.List;

@Data
public class HyModel {
    private List<Cognition> cognitions;
    private List<Action> actions;
}
