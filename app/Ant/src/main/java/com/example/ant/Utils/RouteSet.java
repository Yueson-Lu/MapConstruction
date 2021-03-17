package com.example.ant.Utils;

import android.util.Log;

import com.example.ant.ui.notifications.MapSetViewNavigation;

import java.util.ArrayList;

public class RouteSet {
    float startX;
    float startY;
    float endX;
    float endY;
    float currentX;
    float currentY;
    ArrayList navigationMapPoint;
    ArrayList navigationMapNavigation;
    ArrayList info;

    public RouteSet(ArrayList navigationMapPoint, ArrayList navigationMapNavigation, Integer[] startAndEnd) {
//        Log.i("navigationMapPoint",navigationMapPoint.toString());
//        Log.i("navigationMapNavigation",navigationMapNavigation.toString());
//        int o =(int) navigationMapNavigation.get(startAndEnd[0]);
//        int o1 = (int)navigationMapNavigation.get(startAndEnd[1]);
//        Log.i("startX",navigationMapPoint.get(o*2)+"");
//        Log.i("startY",navigationMapPoint.get(o*2+1)+"");
//        Log.i("endX",navigationMapPoint.get(o1*2)+"");
//        Log.i("endY",navigationMapPoint.get(o1*2+1)+"");
        this.navigationMapPoint = navigationMapPoint;
        this.navigationMapNavigation = navigationMapNavigation;
        this.startX = (float) navigationMapPoint.get((int) navigationMapNavigation.get(startAndEnd[0]) * 2);
        this.startY = (float) navigationMapPoint.get((int) navigationMapNavigation.get(startAndEnd[0]) * 2 + 1);
        this.endX = (float) navigationMapPoint.get((int) navigationMapNavigation.get(startAndEnd[1]) * 2);
        this.endY = (float) navigationMapPoint.get((int) navigationMapNavigation.get(startAndEnd[1]) * 2 + 1);
        this.currentX = (float) navigationMapPoint.get((int) navigationMapNavigation.get(startAndEnd[0]) * 2);
        this.currentY = (float) navigationMapPoint.get((int) navigationMapNavigation.get(startAndEnd[0]) * 2 + 1);
        info=new ArrayList();
        info.add(navigationMapNavigation.get(startAndEnd[0]));
        info.add(navigationMapNavigation.get(startAndEnd[1]));
        info.add(startX);
        info.add(startY);
        info.add(endX);
        info.add(endY);
        info.add(currentX);
        info.add(currentY);
    }

    public void routePaint(float currentX, float currentY, MapSetViewNavigation mapSetViewNavigation) {
        info.set(6,currentX);
        info.set(7,currentY);
        mapSetViewNavigation.repaint(navigationMapPoint,navigationMapNavigation,info,3);
    }
}
