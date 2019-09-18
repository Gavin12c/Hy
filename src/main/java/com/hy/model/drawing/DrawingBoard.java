package com.hy.model.drawing;

import com.hy.frame.EditFrame;
import com.hy.config.KeyConstants;
import com.hy.utils.EhcacheUtil;

import java.util.ArrayList;

public class DrawingBoard {
    private static EhcacheUtil manager =  EhcacheUtil.getInstance();
    static {
        manager.put("drawing",KeyConstants.BOARD_VISIBLE,true);
    }

    public static void delCache(String key) {
        manager.remove("drawing", key);
    }

    public static void updateCache(String key,Object o) {
        manager.put("drawing", key,o);
    }

    public static Object findCache(String key,Class<?> c) throws IllegalAccessException, InstantiationException {
        Object o = manager.get("drawing", key);
        if(null == o){
            o = c.newInstance();
            manager.put("drawing", key,o);
        }
        return o;
    }

    /**
     * 重置keyBuff画板
     */
    public static void reBoardKeyList() throws Exception {
        Object o = findCache(KeyConstants.BOARD,EditFrame.class);
        EditFrame editFrame = (EditFrame) o;
        editFrame.dispose();
        editFrame = new EditFrame(new ArrayList<>(),new String(findCache(KeyConstants.KEYBUFF,StringBuilder.class).toString().getBytes("GBK"),"utf8"),getVisible());
        manager.put("drawing", KeyConstants.BOARD, editFrame);
    }

    /**
     *  重置colorList标点
     */
    public static void reBoardColorList() throws InstantiationException, IllegalAccessException {
        Object o = findCache(KeyConstants.BOARD,EditFrame.class);
        EditFrame editFrame = (EditFrame) o;
        editFrame.dispose();
        editFrame = new EditFrame((ArrayList)findCache(KeyConstants.COLOR,ArrayList.class),getVisible());
        manager.put("drawing", KeyConstants.BOARD, editFrame);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////是否显示窗口
    public static  boolean getVisible(){
        Object drawing = manager.get("drawing", KeyConstants.BOARD_VISIBLE);
        return null == drawing ? false : (boolean) drawing;
    }
    public static void setVisible(boolean visible){
        manager.put("drawing", KeyConstants.BOARD_VISIBLE,visible);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////是否开启编辑
    public static  boolean getEdit(){
        Object drawing = manager.get("drawing", KeyConstants.BOARD_EDIT);
        return null == drawing ? false : (boolean) drawing;
    }
    public static void setEdit(boolean edit){
        manager.put("drawing", KeyConstants.BOARD_EDIT,edit);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////// colorList
}
