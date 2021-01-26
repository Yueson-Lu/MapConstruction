package com.example.stepcount.Utils;

import android.util.Log;

public class StepCountJudgment {
    //    手持状态
    private static final int STATU_HANDHELD = 0;
    //    平放状态
    private static final int STATU_FLAT = 1;
    //    正常状态（放在口袋里）
    private static final int STATU_NORMAL = 2;

    private static int step = 0;   //步数
    private static double oriValue = 0;  //原始值
    private static double lstValue = 0;  //上次的值
    private static double curValue = 0;  //当前值
    private static boolean motiveState = true;   //是否处于运动状态
    private static Long upTime;//手机向上时间
    private static Long downTime;//手机向下时间
    private static Long disTime;//间隔时间

    public StepCountJudgment() {

    }

    //向量求模
    public static double magnitude(float x, float y, float z) {
        double magnitude = 0;
        magnitude = Math.sqrt(x * x + y * y + z * z);
        return magnitude;
    }

    //  statu为选择计步的方式 0-手持 1-平放 2-正常（放在裤袋中）
//  value为传入的数据
    public static Integer judgment(int statu, float[] value, boolean processState) {
        switch (statu) {
            case STATU_HANDHELD:
                Log.i("STATU_HANDHELD",String.valueOf(STATU_HANDHELD));
                return handheldJudgment(value,processState);
            case STATU_FLAT:
                Log.i("STATU_FLAT",String.valueOf(STATU_FLAT));
                return flatJudgment(value,processState);
            case STATU_NORMAL:
                Log.i("STATU_NORMAL",String.valueOf(STATU_NORMAL));
                return normalJudgment(value,processState);
            default:
                return null;
        }
    }

    public static Integer handheldJudgment(float[] value, boolean processState) {
        if (!processState) {
            step = 0;
        }
        double range = 25;   //设定一个精度范围
        curValue = magnitude(value[0], value[1], value[2]);   //计算当前的模
        //向上加速的状态
        if (motiveState == true) {
            if (curValue >= lstValue) lstValue = curValue;
            else {
                upTime = TimeCalculate.getNowTime();
                //检测到一次峰值
                if (Math.abs(curValue - lstValue) > range) {
                    oriValue = curValue;
                    motiveState = false;
                }
            }
        }
        //向下加速的状态
        if (motiveState == false) {
            if (curValue >= range) {
                //检测到一次峰值
                downTime = TimeCalculate.getNowTime();
                disTime = TimeCalculate.getDisTime(upTime, downTime);
                Log.i("uptime", String.valueOf(upTime));
                Log.i("downTime", String.valueOf(downTime));
                Log.i("diaTime", String.valueOf(disTime));
                oriValue = curValue;
//                限定一步完成的时间必须大于0.2s
                if (processState == true && disTime > 200) {
                    step++;  //步数 + 1
                    Log.i("step+++++++++", String.valueOf(step));
                    if (processState == true) {
//                        Log.d("step",String.valueOf(step));
                        motiveState = true;
                        return step;   //读数更新
                    }
                }
            }
        }
        return null;
    }

    public static Integer flatJudgment(float[] value, boolean processState){
        return null;
    }
    public static Integer normalJudgment(float[] value, boolean processState){
        return null;
    }
}
