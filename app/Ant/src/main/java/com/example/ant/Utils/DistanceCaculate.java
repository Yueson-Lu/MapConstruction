package com.example.ant.Utils;

import android.util.Log;

import com.example.ant.Main;

import java.util.ArrayList;
import java.util.HashMap;

public class DistanceCaculate {

    public static float diatance(int dis) {
        return (float) (dis * 0.35 + ((Math.random() * 2 - 1) * Math.random() * 0.12));
    }

    static HashMap minPoint = new HashMap();

    public static HashMap minDis(int startPoint, int endPoint, float ncurrentX, float ncurrentY, ArrayList<Float> navigationMapPoint) {
        minPoint.put("normal", startPoint);
        minPoint.put("deviate", null);
        minPoint.put("finish", null);
        float min = 20;
        float temp;
        for (int i = startPoint; i < endPoint - 2; i++) {
            temp = Math.abs(navigationMapPoint.get(i * 2) - ncurrentX) + Math.abs(navigationMapPoint.get(i * 2 + 1) - ncurrentY);
            Log.i("temp",temp+"");
            Log.i("disEnd",Math.abs(Math.abs(navigationMapPoint.get(endPoint * 2) - ncurrentX) + Math.abs(navigationMapPoint.get(endPoint * 2 + 1) - ncurrentY))+" ");
            if (Math.abs(Math.abs(navigationMapPoint.get(endPoint * 2) - ncurrentX) + Math.abs(navigationMapPoint.get(endPoint * 2 + 1) - ncurrentY)) < 20) {
                minPoint.put("finish", "到达目的地附近，导航结束");
            } else if (temp < min) {
                min = temp;
                minPoint.put("normal", i);
            } else if (min > 30) {
                minPoint.put("deviate", "偏离路线");
            }
        }
        return minPoint;
    }
}
