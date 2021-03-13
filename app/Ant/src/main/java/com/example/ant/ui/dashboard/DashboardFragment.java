package com.example.ant.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.example.ant.Utils.BlobUtil;
import com.example.ant.Utils.DirectionSet;
import com.example.ant.Utils.DistanceCaculate;
import com.example.ant.Utils.NavigationSet;
import com.example.ant.Utils.PointSet;
import com.example.ant.Utils.StepCountJudgment;
import com.example.ant.Utils.Tips;
import com.example.ant.dao.impl.MapPointDaoImpl;
import com.example.ant.dto.MyMap;
import com.example.ant.dto.User;


import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import static android.os.Looper.getMainLooper;

public class DashboardFragment extends Fragment implements SensorEventListener {
    //   用户
    private User user;


    //    xml组件
    private LinearLayout linearLayout;
    private FrameLayout frameLayout;
    private TextView tvStep;
    private TextView tvDirection;
    private Button btnStart;
    private ImageView compass;
    private Button navigation;
    private Button compose;


    //    传感器
    private SensorManager sManager;
    private Sensor mSensorAccelerometer;
    private Sensor mSensorMagnetic;
    //    方向设定
    private float direction = 0;
    //    步数设定
    private Integer step = 0;
    //状态
    //    是否开始构建
    private boolean processState = false;
    //    计步姿态 0-手握 1-平握 2-放在口袋中
    private int statu;

    //    位置设定
    private PointSet pointSet;
    private float currentX = 0;
    private float currentY = 0;
    private float nextX = 0;
    private float nextY = 0;
    private ArrayList<Float> points;
    private ArrayList navigations;
    private int countPoint = 0;
    private int countNavigation = 0;

    //    制图类
    private MapSetViewCompose mapSetViewCompose;

    //    计步类
    private StepCountJudgment stepCountJudgment;

    //    数据库操作
    public static Handler mainHandler;
    public static MapPointDaoImpl mapPointDao;

    //    初始化页面
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        return view;
    }

    //    页面构建
    @Override
    public void onStart() {
        super.onStart();
        findView();
        listener();
        stepCountJudgment = new StepCountJudgment();
        if (null == user) {
            user = new User();
            user = (User) getActivity().getIntent().getSerializableExtra("user");
        }
    }

    //注册组件
    public void findView() {
        linearLayout = getView().findViewById(R.id.linearLayout);
        frameLayout = getView().findViewById(R.id.frameLayout);
        tvStep = getView().findViewById(R.id.tv_step);
        tvDirection = getView().findViewById(R.id.tv_direction);
        btnStart = getView().findViewById(R.id.btn_start);
        compass = getView().findViewById(R.id.compass);
        navigation = getView().findViewById(R.id.navigation);
        compose = getView().findViewById(R.id.compose);
    }


    //监听器注册
    public void listener() {
        //        设定开始点
        pointSet = new PointSet(0, 0, 0, 10);

        points = new ArrayList<>();
//  导航点
        navigations = new ArrayList<>();

//                制图类
        mapSetViewCompose = new MapSetViewCompose(getActivity());
        frameLayout.addView(mapSetViewCompose);

        mainHandler = new Handler(getMainLooper());
        mapPointDao = new MapPointDaoImpl();

//        传感器
        sManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mSensorAccelerometer = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorMagnetic = sManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sManager.registerListener(this, mSensorAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        sManager.registerListener(this, mSensorMagnetic, SensorManager.SENSOR_DELAY_UI);
//点击监听
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //地图类
                MyMap myMap = new MyMap();
                tvStep.setText("本次行走距离0米");
                tvDirection.setText("当前方位");
                if (processState == true) {
                    if (countPoint >= 2) {
                        Intent intent = getActivity().getIntent();
                        User user = (User) intent.getSerializableExtra("user");
                        myMap.setId(new Random().nextInt(Integer.MAX_VALUE));
                        myMap.setMapName("");
                        myMap.setPoints(BlobUtil.setObject(points));
                        myMap.setNavigation(BlobUtil.setObject(navigations));
                        myMap.setAuthorId(user.getId());
                        myMap.setAuthor(user.getUsername());
                        myMap.setCanNavigation(true);
                        myMap.setCreateTime(new Date());
                        Tips.saveDlg(getActivity(), myMap);
                    } else {
                        Tips.showShortMsg(getActivity(), "地图为空，不能保存");
                    }
                    processState = false;
                    btnStart.setText("开始构建");
                    direction = 0;
                    step = 0;
                    currentX = 0;
                    currentY = 0;
                    points.clear();
                    navigations.clear();
                    countPoint = 0;
                    countNavigation = 0;
                    mapSetViewCompose.repaint(points, navigations);
                } else {
                    points.add(0f);
                    points.add(0f);
                    countPoint++;
                    NavigationSet.setStartNavigation(navigations);
                    btnStart.setText("停止");
                    processState = true;
                }
            }
        });

        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.i("points",points.toString());
                if (null != points || !points.isEmpty()) {
                    if (navigations.contains(String.valueOf(countPoint))) {
                        Tips.showShortMsg(getActivity(), "导航点已经存在");
                    } else if ((countPoint - (Integer) (navigations.get(countNavigation * 2)) < 5)) {
                        Tips.showShortMsg(getActivity(), "导航点过近");
                    } else {
//                        Log.i("count",navigations.get(countNavigation * 2));
                        countNavigation++;
                        Tips.NavigationSetDlg(getActivity(), countPoint, navigations);
                    }
                } else {
                    Tips.showShortMsg(getActivity(), "请先开始地图构建");
                }
            }
        });

        compose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<MyMap> myMaps = mapPointDao.composeAllMap();
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getActivity(), ComposeActivity.class);
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
        statu = 1;
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float[] value = event.values;
            step = stepCountJudgment.judgment(statu, value, processState);
            if (null != step && processState) {
                tvStep.setText("本次行走距离" + String.format("%.2f", (DistanceCaculate.diatance(points.size() / 2))) + "米");
            }
        }
        if (step != null) {
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD && processState) {
                float[] value1 = event.values;
                direction = DirectionSet.directionSet(value1);
                float[] floats = pointSet.calculatePoint(currentX, currentY, direction);
//                格式化 位置参数保留小数点后一位
                nextX = floats[0];
                nextY = floats[1];
                currentX = nextX;
                currentY = nextY;
                points.add(nextX);
                points.add(nextY);
                countPoint++;
                if (null != points && points.size() >= 2) {
                    mapSetViewCompose.repaint(points, navigations);
                }
            }
        }
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
