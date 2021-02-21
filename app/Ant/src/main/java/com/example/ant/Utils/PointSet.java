package com.example.ant.Utils;

import java.util.ArrayList;

public class PointSet {
    //    控制精度阀值
    private float Threshold;
    //    当前坐标（开始坐标）
    private float currentX;
    private float currentY;
    //    开始时的角度(开始方向)
    private float currentAngle;
    //    偏转角度
    private float angle;

    //下一步的坐标
    private float nextX;
    private float nextY;
    //    中心位置
    private Float[] center;
    private Float centerX;
    private Float centerY;

    public PointSet(float currentX, float currentY, float currentAngle, float Threshold) {
        this.currentX = currentX;
        this.currentY = currentY;
        this.currentAngle = currentAngle;
        this.Threshold = Threshold;

    }

    public float[] calculatePoint(float currentX, float currentY, float angle) {
//        弧度
        double radian = 0;
        float[] XY = new float[2];
        radian = (Math.PI * angle) / 180;
        if (currentX == 0 & currentY == 0) {
            currentX = this.currentX;
            currentY = this.currentY;
        }
        nextX = (float) (currentX + Math.sin(radian) * Threshold);
        nextY = (float) (currentY + Math.cos(radian) * Threshold);
        XY[0] = nextX;
        XY[1] = nextY;
//        Log.i("x:",nextX+"");
//        Log.i("y:",nextY+"");
        return XY;
    }
}
