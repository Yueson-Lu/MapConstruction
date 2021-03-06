package com.example.ant.Utils;

import android.util.Log;

public class StepCountJudgment {
    //    手持状态
    private static final int STATU_HANDHELD = 0;
    //    平握状态
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
    public static double magnitude(float[] value) {
        float x = value[0];
        float y = value[1];
        float z = value[2];
//        Log.i("x",String.valueOf(x));
//        Log.i("y",String.valueOf(y));
//        Log.i("z",String.valueOf(z));
        double magnitude = 0;
        magnitude = Math.sqrt(x * x + y * y + z * z);
        return magnitude;
    }


    //  statu为选择计步的方式 0-手持 1-平握 2-正常（放在裤袋中）
//  value为传入的数据
    public static Integer judgment(int statu, float[] value, boolean processState) {
//        Log.i("tag","进入判断");
        double range = 0;   //设定一个精度范围
        if (!processState) {
            step = 0;
            return 0;
        } else {
            switch (statu) {
                case STATU_HANDHELD:
//                Log.i("STATU_HANDHELD",String.valueOf(STATU_HANDHELD));
                    if (Math.abs(value[2]) < 4.9) {
                        range = 22;
                        return handheldJudgment(range, value, processState);
                    }
                    break;
                case STATU_FLAT:
                    if (Math.abs(value[0]) < 5 && Math.abs(value[1]) < 5) {
                        range = 15;   //设定一个精度范围
                        return flatJudgment(range, value, processState);
                    }
                    break;
                case STATU_NORMAL:
//                Log.i("STATU_NORMAL",String.valueOf(STATU_NORMAL));
                    if (Math.abs(value[2]) < 4.9) {
                        range = 12;
                        return normalJudgment(range, value, processState);
                    }
                    break;
                default:
                    return null;
            }
        }
        return null;
    }


    public static Integer handheldJudgment(double range, float[] value, boolean processState) {
        curValue = magnitude(value);   //计算当前的模
        //向上加速的状态
        if (motiveState == true) {
            if (curValue >= lstValue) lstValue = curValue;
            else {
                upTime = TimeCalculate.getNowTime();
                //检测到一次峰值
                if (Math.abs(curValue - lstValue) > range) {
//                    Log.i("curValue - lstValue",(curValue - lstValue)+"");
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
                oriValue = curValue;
//                限定一步完成的时间必须大于0.2s
                if (processState == true && disTime > 200) {
                    step = step + 1;  //步数 + 1
//                    Log.i("step+++++++++", String.valueOf(step));
//                    if (processState == true) {
//                        Log.d("step",String.valueOf(step));
                    motiveState = true;
//                    Log.i("value[0]", value[0] + "");
//                    Log.i("value[1]", value[1] + "");
//                    Log.i("value[2]", value[2] + "");
                    return step;   //读数更新
//                    }
                }
            }
        }
        return null;
    }

    public static Integer flatJudgment(double range, float[] value, boolean processState) {
        return handheldJudgment(range, value, processState);
    }

    public static Integer normalJudgment(double range, float[] value, boolean processState) {
        return handheldJudgment(range, value, processState);
    }
}
