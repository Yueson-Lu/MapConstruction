package com.example.ant.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ant.Login;
import com.example.ant.R;
import com.example.ant.Utils.activityUtils.ActivityUtils;
import com.example.ant.dto.MyMap;
import com.mysql.jdbc.StringUtils;

public class Tips {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(context). setTitle("保存");
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

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
// 获取布局
        View view2 = View.inflate(context, R.layout.save_dialog, null);
        mapName = view2.findViewById(R.id.mapName);
        mapId = view2.findViewById(R.id.mapId);
        author = view2.findViewById(R.id.author);
        createTime = view2.findViewById(R.id.createTime);
        cancel = view2.findViewById(R.id.cancel);
        save = view2.findViewById(R.id.save);
//        mapId.setText(myMap.getId());
//        author.setText(myMap.getAuthor());
//        createTime.setText(myMap.getCreateTime());
        Log.i("savedlg","savedlg");
// 创建对话框
        builder.create().show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                builder.create().dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isNullOrEmpty(mapName.toString())){
                    Tips.showShortMsg(context,"地图名不能为空");
                }else {
                    myMap.setMapName(mapName.toString());
                    Log.i("save","保存成功");
                }
            }
        });
    }
}
