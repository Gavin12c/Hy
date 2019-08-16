package com.hy.utils;

import java.util.*;

public class RoundUtil {

    /**
     *  8点
     * @param centerX 中心x
     * @param centerY 中心y
     * @param radius 半径
     */
    public static List createRound(int centerX,int centerY, int radius){
        List<Map> list = new ArrayList<>(4);
        list.add(pointByCir(centerX,centerY,radius,45));
        list.add(pointByCir(centerX,centerY,radius,-45));
        list.add(pointByCir(centerX,centerY,radius,135));
        list.add(pointByCir(centerX,centerY,radius,-135));
        return list;
    }

    public static void main(String[] args) {

        System.out.println(System.currentTimeMillis());
        List<String> list = new ArrayList<>(160);
//        list.add(1);
        System.out.println(list.size());
        System.out.println(System.currentTimeMillis());
    }


    /**
     * 圆点坐标和半径，得到圆上的点
     * @param centerX 圆点X
     * @param centerY 圆点Y
     * @param radius  半径
     * @return
     */
    private static List initPointsCircular(int centerX,int centerY, int radius) {
        List points = new LinkedList<Map>();
        for (int i = 0; i < 360; i += 1) {//得出下半圆
            Map<Integer, Integer> mapxy = new HashMap<>();
            int x = (int)Math.round(centerX + radius * Math.cos(i * Math.PI / 180));
            int y = (int)Math.round(centerY + radius * Math.sin(i * Math.PI / 180));
            mapxy.put(x, y);
            points.add(mapxy);
        }
        return points;
    }

    /**
     *  返回指定角度圆上的点
     * @param centerX 圆心X
     * @param centerY 圆心Y
     * @param radius 半径
     * @param angle  角度
     * @return
     */
    public static Map pointByCir(double centerX,double centerY, double radius,double angle){
        Map<Integer, Integer> map = new HashMap<>();
        int x = (int)Math.round(centerX + radius * Math.cos(angle * Math.PI / 180));
        int y = (int)Math.round(centerY + radius * Math.sin(angle * Math.PI / 180));
        map.put(x, y);
        return map;
    }
}


