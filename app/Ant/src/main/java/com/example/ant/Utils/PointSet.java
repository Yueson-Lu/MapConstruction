package com.example.ant.Utils;

public class PointSet {
    //    步伐长度
    private float precision;
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

    public PointSet(float currentX, float currentY, float currentAngle, float precision) {
        this.currentX = currentX;
        this.currentY = currentY;
        this.currentAngle = currentAngle;
        this.precision = precision;

    }

    public float[] calculatePoint(float currentX, float currentY, float angle) {
//        弧度
        double radian = 0;
        float[] XY = new float[2];
        radian = (Math.PI * angle) / 180;
        if (currentX==0&currentY==0){
            currentX=this.currentX;
            currentY=this.currentY;
        }
            nextX = (float) (currentX + Math.sin(radian) * precision);
            nextY = (float) (currentY + Math.cos(radian) * precision);
        XY[0] = nextX;
        XY[1] = nextY;
//        Log.i("x:",nextX+"");
//        Log.i("y:",nextY+"");
        return XY;
    }
}
