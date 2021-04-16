package com.example.ant.ui.notifications;

import android.app.Activity;
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
import com.example.ant.Utils.RouteSet;
import com.example.ant.Utils.StepCountJudgment;
import com.example.ant.Utils.TimeCalculate;
import com.example.ant.Utils.Tips;
import com.example.ant.dao.impl.MapPointDaoImpl;
import com.example.ant.dto.MyMap;
import com.example.ant.dto.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

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
    private boolean processState = false;
    private Integer step;
    //    传感器
    private SensorManager sManager;
    private Sensor mSensorAccelerometer;
    private Sensor mSensorMagnetic;

    //    数据库操作
    public static Handler mainHandler;
    public static MapPointDaoImpl mapPointDao;


    //    请求码和返回码
    private static Integer NAVIGATIONMAP_REQUEST = 0;
    private static Integer NAVIGATIONMAP_RESULT = 1;
    private static Integer STARTANDEND_REQUEST = 2;
    private static Integer STARTANDEND_RESULT = 3;

    Integer[] startAndEnd = new Integer[]{null, null};


    //需要导航的地图
    private MyMap navigationMap = null;


    //    地图构建类
    private MapSetViewNavigation mapSetViewNavigation;
    //    地图坐标和导航点实体类
    private Object navigationMapPoint;
    private Object navigationMapNavigation;

    //    是否选择了导航点标志
    private boolean selected = false;

    //    导航坐标类
    private RouteSet routeSet;

    float compassDirection;
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
//        地图框
        mapSetViewNavigation = new MapSetViewNavigation(getActivity());
        frameLayout.addView(mapSetViewNavigation);
    }

    //注册组件
    public void findView() {
        linearLayout = getActivity().findViewById(R.id.linearLayout);
        frameLayout = getActivity().findViewById(R.id.frameLayout);
        compass = getActivity().findViewById(R.id.compass);
        tvStep = getActivity().findViewById(R.id.tv_step);
        tvDirection = getActivity().findViewById(R.id.tv_direction);
        btnStart = getActivity().findViewById(R.id.btn_startNavigation);
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
                btnStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("click", "点击");
                        if (processState) {
                            btnStart.setText("开始导航");
                            processState = false;
                            mapSetViewNavigation.repaint(null, null, null, null,0,0,2);
                        } else {
                            if (null != navigationMap && selected) {
                                navigationMapPoint = BlobUtil.getObject(navigationMap.getPoints());
                                navigationMapNavigation = BlobUtil.getObject(navigationMap.getNavigation());
                                if (navigationMapPoint instanceof List) {
                                    mapSetViewNavigation.repaint((ArrayList) navigationMapPoint, (ArrayList) navigationMapNavigation, 0);
                                    routeSet = new RouteSet(getActivity(),(ArrayList) navigationMapPoint, (ArrayList) navigationMapNavigation, startAndEnd);
                                    pointSet = new PointSet(0, 0, 0, 10);
                                    currentX = (float) ((ArrayList) navigationMapPoint).get((int) ((ArrayList) navigationMapNavigation).get(startAndEnd[0].intValue()) * 2);
                                    currentY = (float) ((ArrayList) navigationMapPoint).get((int) ((ArrayList) navigationMapNavigation).get(startAndEnd[0].intValue()) * 2 + 1);
                                } else {
                                    mapSetViewNavigation.repaint((HashMap) navigationMapPoint, (HashMap) navigationMapNavigation, navigationMap.getDisx(), navigationMap.getDisy(), 1);
                                }
                                btnStart.setText("停止");
                                processState = true;
                            } else {
                                Tips.showShortMsg(getActivity(), "请选择导航地图");
                            }
                        }
                    }
                });
            }
        });
        btnSelectNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (processState) {
                    Tips.showShortMsg(getActivity(), "请先结束本次导航");
                } else {
                    if (null != navigationMapNavigation) {
//                        navigationMap.isCanNavigation()
                        if ( navigationMap.isCanNavigation()) {
                            Intent intent = new Intent(getActivity(), SelectAvtivity.class);
                            intent.putExtra("navigationMapNavigation", (ArrayList) navigationMapNavigation);
                            startActivityForResult(intent, STARTANDEND_REQUEST);
                        } else {
                            selected=true;
                            Tips.showShortMsg(getActivity(), "该地图不能导航");
                        }
                    } else {
                        Tips.showShortMsg(getActivity(), "请选择导航地图");
                    }
                }
            }
        });

        btnSelectMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (processState) {
                    Tips.showShortMsg(getActivity(), "请先结束本次导航");
                } else {
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
                                    startActivityForResult(intent, NAVIGATIONMAP_REQUEST);
                                }
                            });
                        }
                    }).start();
                }
            }
        });
    }

    //注销
    @Override
    public void onDestroy() {
        super.onDestroy();
        sManager.unregisterListener(this);
    }

    //计算坐标类
    private PointSet pointSet;
    private float currentX;
    private float currentY;
    private float nextX;
    private float nextY;

    //    传感器监听器
    @Override
    public void onSensorChanged(SensorEvent event) {
        int statu = 1;
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER && processState) {
            float[] value = event.values;
            step = StepCountJudgment.judgment(statu, value, processState);
        }
        if (step != null) {
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD && processState) {
                float[] value1 = event.values;
                float direction = DirectionSet.directionSet(value1);
                float[] floats = pointSet.calculatePoint(currentX, currentY, direction);
                nextX = floats[0];
                nextY = floats[1];
                if (routeSet.statu()){
                    routeSet.routePaint(currentX, currentY, mapSetViewNavigation,direction);
                }else {
                    btnStart.setText("开始导航");
                    processState = false;
                    ArrayList point =new ArrayList();
                    point.add(0);
                    mapSetViewNavigation.repaint(point, null, 0);
                   Tips.finishNavigationDlg(getActivity(),"到达目的地附近，导航结束");
                }
                currentX = nextX;
                currentY = nextY;
            }
        }
        //        指南针
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            float[] value1 = event.values;
            compassDirection = DirectionSet.directionSet(value1);
            compassDirection = (float) (Math.round(compassDirection * 100)) / 100;
            compass.setPivotX(compass.getWidth() / 2);
            compass.setPivotY(compass.getHeight() / 2);//支点在图片中心
            compass.setRotation(-compassDirection);
            tvDirection.setText("方位：" + String.format("%.2f", compassDirection) + "°");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == NAVIGATIONMAP_RESULT) {
            if (requestCode == NAVIGATIONMAP_REQUEST) {
                if (null == navigationMap) {
                    navigationMap = new MyMap();
                }
                navigationMap = (MyMap) data.getSerializableExtra("navigationMap");
                if (null != navigationMap) {
                    navigationMapPoint = BlobUtil.getObject(navigationMap.getPoints());
                    navigationMapNavigation = BlobUtil.getObject(navigationMap.getNavigation());
                    if (navigationMapPoint instanceof List) {
                        mapSetViewNavigation.repaint((ArrayList) navigationMapPoint, (ArrayList) navigationMapNavigation, 0);
                    } else {
                        mapSetViewNavigation.repaint((HashMap) navigationMapPoint, (HashMap) navigationMapNavigation, navigationMap.getDisx(), navigationMap.getDisy(), 1);
                    }
                }
            }
        }
        if (resultCode == STARTANDEND_RESULT) {
            if (requestCode == STARTANDEND_REQUEST) {
                startAndEnd = (Integer[]) data.getSerializableExtra("startAndEnd");
                begining.setText(((ArrayList) navigationMapNavigation).get(startAndEnd[0] + 1).toString());
                ending.setText(((ArrayList) navigationMapNavigation).get(startAndEnd[1] + 1).toString());
                selected = true;
                if (null != navigationMap) {
                    navigationMapPoint = BlobUtil.getObject(navigationMap.getPoints());
                    navigationMapNavigation = BlobUtil.getObject(navigationMap.getNavigation());
                    if (navigationMapPoint instanceof List) {
                        mapSetViewNavigation.repaint((ArrayList) navigationMapPoint, (ArrayList) navigationMapNavigation, 0);
                    } else {
                        mapSetViewNavigation.repaint((HashMap) navigationMapPoint, (HashMap) navigationMapNavigation, navigationMap.getDisx(), navigationMap.getDisy(), 1);
                    }
                }
            }
        }
    }
}
