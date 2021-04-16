package com.example.ant.Utils;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.example.ant.ui.notifications.MapSetViewNavigation;

import java.util.ArrayList;
import java.util.HashMap;

public class RouteSet {
    int startPoint;
    int endPoint;
    float startX;
    float startY;
    float endX;
    float endY;
    float currentX;
    float currentY;
    ArrayList navigationMapPoint;
    ArrayList navigationMapNavigation;
    ArrayList info;

    Activity activity;

    boolean statu = true;

    public RouteSet(Activity activity, ArrayList navigationMapPoint, ArrayList navigationMapNavigation, Integer[] startAndEnd) {
//        Log.i("navigationMapPoint",navigationMapPoint.toString());
//        Log.i("navigationMapNavigation",navigationMapNavigation.toString());
//        int o =(int) navigationMapNavigation.get(startAndEnd[0]);
//        int o1 = (int)navigationMapNavigation.get(startAndEnd[1]);
//        Log.i("startX",navigationMapPoint.get(o*2)+"");
//        Log.i("startY",navigationMapPoint.get(o*2+1)+"");
//        Log.i("endX",navigationMapPoint.get(o1*2)+"");
//        Log.i("endY",navigationMapPoint.get(o1*2+1)+"");
        this.activity = activity;
        this.startPoint = (int) navigationMapNavigation.get(startAndEnd[0]);
        this.endPoint = (int) navigationMapNavigation.get(startAndEnd[1]);
        this.navigationMapPoint = navigationMapPoint;
        this.navigationMapNavigation = navigationMapNavigation;
        this.startX = (float) navigationMapPoint.get((int) navigationMapNavigation.get(startAndEnd[0]) * 2);
        this.startY = (float) navigationMapPoint.get((int) navigationMapNavigation.get(startAndEnd[0]) * 2 + 1);
        this.endX = (float) navigationMapPoint.get((int) navigationMapNavigation.get(startAndEnd[1]) * 2);
        this.endY = (float) navigationMapPoint.get((int) navigationMapNavigation.get(startAndEnd[1]) * 2 + 1);
        this.currentX = (float) navigationMapPoint.get((int) navigationMapNavigation.get(startAndEnd[0]) * 2);
        this.currentY = (float) navigationMapPoint.get((int) navigationMapNavigation.get(startAndEnd[0]) * 2 + 1);
        info = new ArrayList();
        info.add(startPoint);
        info.add(endPoint);
        info.add(startX);
        info.add(startY);
        info.add(endX);
        info.add(endY);
        info.add(currentX);
        info.add(currentY);
    }

    public void routePaint(float currentX, float currentY, MapSetViewNavigation mapSetViewNavigation, float direction) {
        HashMap thePoint = DistanceCaculate.minDis(startPoint, endPoint, currentX, currentY, navigationMapPoint);
        info.set(6, currentX);
        info.set(7, currentY);
        int point = (int) thePoint.get("normal");
        if (null == thePoint.get("finish")) {
            if (null == thePoint.get("deviate")) {
                mapSetViewNavigation.repaint(navigationMapPoint, navigationMapNavigation, info, thePoint, direction, point, 3);
            } else {
                Tips.showShortMsg(activity, "偏离路线");
                point = (int) thePoint.get("normal");
                mapSetViewNavigation.repaint(navigationMapPoint, navigationMapNavigation, info, thePoint, direction, point, 3);
            }
        } else {
            this.statu = false;
        }
    }

    public  boolean statu() {
        return this.statu;
    }
}
