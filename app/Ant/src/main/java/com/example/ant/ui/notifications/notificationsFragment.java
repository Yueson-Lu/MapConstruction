package com.example.ant.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ant.R;
import com.example.ant.Utils.DirectionSet;
import com.example.ant.Utils.NavigationSet;
import com.example.ant.dao.impl.MapPointDaoImpl;
import com.example.ant.dto.MyMap;
import com.example.ant.dto.User;
import com.example.ant.ui.dashboard.ComposeActivity;

import java.util.ArrayList;

public class notificationsFragment extends Fragment implements SensorEventListener {
    //   用户
    private User user;

    //    XML组件
    private LinearLayout linearLayout;
    private FrameLayout frameLayout;
    private ImageView compass;
    private TextView tvStep;
    private TextView tvDirection;
    private Button btnStart;
    private Button btnSelectNavigation;
    private Button btnSelectMap;
    private TextView begining;
    private TextView ending;
    //    传感器
    private SensorManager sManager;
    private Sensor mSensorAccelerometer;
    private Sensor mSensorMagnetic;

    //    数据库操作
    public static Handler mainHandler;
    public static MapPointDaoImpl mapPointDao;

    //    初始化页面
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        return view;
    }

    //    页面构建
    @Override
    public void onStart() {
        super.onStart();
        findView();
        listener();
        initialization();
    }

    public void initialization() {
//        获取用户
        if (null == user) {
            user = new User();
            user = (User) getActivity().getIntent().getSerializableExtra("user");
        }
//        获取数据库操作对象
        mainHandler = new Handler();
        mapPointDao = new MapPointDaoImpl();

    }

    //注册组件
    public void findView() {
        linearLayout = getActivity().findViewById(R.id.linearLayout);
        frameLayout = getActivity().findViewById(R.id.frameLayout);
        compass = getActivity().findViewById(R.id.compass);
        tvStep = getActivity().findViewById(R.id.tv_step);
        tvDirection = getActivity().findViewById(R.id.tv_direction);
        btnStart = getActivity().findViewById(R.id.btn_start);
        btnSelectNavigation = getActivity().findViewById(R.id.btn_selectNavigation);
        btnSelectMap = getActivity().findViewById(R.id.btn_selectMap);
        begining = getActivity().findViewById(R.id.begining);
        ending = getActivity().findViewById(R.id.ending);
    }


    //监听器注册
    public void listener() {
//        传感器
        sManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mSensorAccelerometer = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorMagnetic = sManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sManager.registerListener(this, mSensorAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        sManager.registerListener(this, mSensorMagnetic, SensorManager.SENSOR_DELAY_UI);

//        按钮注册
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnSelectNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnSelectMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<MyMap> myMaps = mapPointDao.selectAllMap();
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getActivity(), NavigationActivity.class);
                                intent.putExtra("myMaps", myMaps);
                                intent.putExtra("user", user);
                                startActivity(intent);
                            }
                        });
                    }
                }).start();
            }
        });
    }

    //注销
    @Override
    public void onDestroy() {
        super.onDestroy();
        sManager.unregisterListener(this);
    }


    //    传感器监听器
    @Override
    public void onSensorChanged(SensorEvent event) {
//        指南针
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            float[] value1 = event.values;
            float direction = DirectionSet.directionSet(value1);
            direction = (float) (Math.round(direction * 100)) / 100;
            compass.setPivotX(compass.getWidth() / 2);
            compass.setPivotY(compass.getHeight() / 2);//支点在图片中心
            compass.setRotation(direction);
            tvDirection.setText("方位：" + String.format("%.2f", direction) + "°");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
