package com.example.ant.Utils;


public class TimeCalculate {

    public TimeCalculate(){

    }
    //获取当前时间
    public static Long getNowTime() {
        return System.currentTimeMillis();
    }

    //获取间隔时间
    public static Long getDisTime(Long firstTime, Long SecondTime) {
        return (SecondTime - firstTime);
    }
}
