package com.example.ant.Utils.activityUtils;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.mysql.jdbc.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ActivityUtils extends Application {
    private static List<Activity> oList;//用于存放所有启动的Activity的集合

    public void onCreate() {
        super.onCreate();
    }

    /**
     * 添加Activity
     */
    public static void addActivity_(Activity activity) {
        if (null==oList){
            oList=new ArrayList<>();
        }
// 判断当前集合中不存在该Activity
        if (!oList.contains(activity)) {
            oList.add(activity);//把当前Activity添加到集合中
        }
    }

    /**
     * 销毁单个Activity
     */
    public static void removeActivity_(Activity activity) {
//判断当前集合中存在该Activity
        if (oList.contains(activity)) {
            oList.remove(activity);//从集合中移除
            activity.finish();//销毁当前Activity
        }
    }

    /**
     * 销毁所有的Activity
     */
    public static void removeALLActivity_() {
        //通过循环，把集合中的所有Activity销毁
        for (Activity activity : oList) {
            activity.finish();
        }
    }
    public static void exitApp() {
        try {
            removeALLActivity_();
            // 退出JVM,释放所占内存资源,0表示正常退出
            System.exit(0);
            // 从系统中kill掉应用程序
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}