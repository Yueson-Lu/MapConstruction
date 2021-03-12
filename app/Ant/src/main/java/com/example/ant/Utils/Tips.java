package com.example.ant.Utils;

import android.app.Activity;
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
import com.example.ant.ui.dashboard.ComposeActivity;
import com.mysql.jdbc.StringUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

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

    public static void exitComposeDlg(Activity activity, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity).setTitle("保存");
        builder.setMessage(msg);
        builder.setTitle("提示");
        builder.setPositiveButton("确认",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityUtils.removeActivity_(activity);
                        activity.finish();
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
//        Log.i("savedlg", "savedlg");
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
                                    if (b) {
                                        alertDialog.dismiss();
                                        Tips.showLongMsg(context, "保存成功");
                                    } else {
                                        Tips.showLongMsg(context, "保存失败");
                                    }
                                }
                            });
                        }
                    }).start();
                }
            }
        });
    }

    public static void composeDlg(Activity activity, ArrayList<MyMap> myMaps, User user, String msg) {
        EditText mapName;
        TextView mapId;
        TextView author;
        TextView createTime;
        Button cancel;
        Button save;
        Switch isPub;
        TextView Msg;
        EditText dir;
        EditText dis;


// 获取布局
        View dialog = View.inflate(activity, R.layout.compose_dialog, null);
        mapName = dialog.findViewById(R.id.mapName);
        mapId = dialog.findViewById(R.id.mapId);
        author = dialog.findViewById(R.id.author);
        createTime = dialog.findViewById(R.id.createTime);
        cancel = dialog.findViewById(R.id.cancel);
        save = dialog.findViewById(R.id.save);
        isPub = dialog.findViewById(R.id.isPub);
        Msg = dialog.findViewById(R.id.Msg);
        dir = dialog.findViewById(R.id.dir);
        dis = dialog.findViewById(R.id.dis);
        Msg.setText(msg);

        MyMap myMap1 = myMaps.get(0);
        MyMap myMap2 = myMaps.get(1);
        MyMap myMapNew = new MyMap();


        myMapNew.setId(new Random().nextInt(Integer.MAX_VALUE));
        myMapNew.setAuthorId(user.getId());
        myMapNew.setAuthor(user.getUsername() + "(" + myMap1.getAuthor() + "/" + myMap2.getAuthor() + ")");
        myMapNew.setCreateTime(new Date());
        mapId.setText(myMapNew.getId() + "");
        author.setText(myMapNew.getAuthor() + "");
        createTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(myMapNew.getCreateTime()));
//        Log.i("savedlg", "savedlg");
// 创建对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(activity).setView(dialog).setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        isPub.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myMapNew.setPub(true);
                    isPub.setTextOn("公开");
                } else {
                    myMapNew.setPub(false);
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
                    Tips.showShortMsg(activity, "地图名不能为空");
                } else {
                    myMapNew.setMapName(mapName.getText().toString());
                    if (dir.getText().toString().isEmpty()||dis.getText().toString().isEmpty()){
                        Tips.showLongMsg(activity,"方位和距离不能为空");
                    }else if (Float.valueOf(dir.getText().toString())>360||Float.valueOf(dir.getText().toString())<=0){
                        Tips.showLongMsg(activity,"方位在0-360之间");
                    }else if (Float.valueOf(dis.getText().toString())>100||Float.valueOf(dis.getText().toString())<=0){
                        Tips.showLongMsg(activity,"距离再1-100之间");
                    }else {
                        Compose.composeMap(myMaps, myMapNew, Float.valueOf(dir.getText().toString()), Float.valueOf(dis.getText().toString()));
//                        Log.i("mapx",myMapNew.getDisx()+"");
//                        Log.i("mapy",myMapNew.getDisy()+"");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                boolean b = mapPointDao.addMap(myMapNew);
                                mainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                    if (b) {
                                        alertDialog.dismiss();
                                        Tips.showLongMsg(activity, "保存成功");
                                        activity.finish();
                                    } else {
                                        Tips.showLongMsg(activity, "保存失败");
                                    }
                                    }
                                });
                            }
                        }).start();
                    }
                }
            }
        });
    }
}
