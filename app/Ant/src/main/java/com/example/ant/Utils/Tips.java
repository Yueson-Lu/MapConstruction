package com.example.ant.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ant.Login;
import com.example.ant.Main;
import com.example.ant.R;
import com.example.ant.Utils.activityUtils.ActivityUtils;
import com.example.ant.dao.impl.MapPointDaoImpl;
import com.example.ant.dao.impl.UserDaoImpl;
import com.example.ant.dto.MyMap;
import com.example.ant.dto.User;
import com.mysql.jdbc.StringUtils;

import org.json.JSONObject;

import static android.os.Looper.getMainLooper;

public class Tips {

    public static Handler mainHandler;
    public static MapPointDaoImpl mapPointDao;

    public Tips() {

    }

    public static void showShortMsg(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showLongMsg(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showDlgMsg(Context context, String title, String msg) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("确定", null)
                .setPositiveButton("取消", null)
                .create().show();
    }

    public static void exitDlg(Context context, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle("保存");
        builder.setMessage(msg);
        builder.setTitle("提示");
        builder.setPositiveButton("确认",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        android.os.Process.killProcess(android.os.Process.myPid());
                        ActivityUtils.exitApp();
                    }
                });
        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    public static void saveDlg(Context context, MyMap myMap) {
        EditText mapName;
        TextView mapId;
        TextView author;
        TextView createTime;
        Button cancel;
        Button save;
        Switch isPub;
// 获取布局
        View dialog = View.inflate(context, R.layout.save_dialog, null);
        mapName = dialog.findViewById(R.id.mapName);
        mapId = dialog.findViewById(R.id.mapId);
        author = dialog.findViewById(R.id.author);
        createTime = dialog.findViewById(R.id.createTime);
        cancel = dialog.findViewById(R.id.cancel);
        save = dialog.findViewById(R.id.save);
        isPub = dialog.findViewById(R.id.isPub);
        mapId.setText(myMap.getId() + "");
        author.setText(myMap.getAuthor() + "");
        createTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(myMap.getCreateTime()));
        Log.i("savedlg", "savedlg");
// 创建对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setView(dialog).setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        isPub.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myMap.setPub(true);
                    isPub.setTextOn("公开");
                } else {
                    myMap.setPub(false);
                    isPub.setTextOn("不公开");
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
//                Log.i("cancel", "cancel");
                alertDialog.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapPointDao = new MapPointDaoImpl();
                mainHandler = new Handler(getMainLooper());
                if (mapName.getText().toString().isEmpty()) {
                    Tips.showShortMsg(context, "地图名不能为空");
                } else {
                    myMap.setMapName(mapName.getText().toString());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            boolean b = mapPointDao.addMap(myMap);
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                   if (b){
                                       alertDialog.dismiss();
                                       Tips.showLongMsg(context,"保存成功");
                                   }else{
                                       Tips.showLongMsg(context,"保存失败");
                                   }
                                }
                            });
                        }
                    }).start();
                }
            }
        });
    }
}
