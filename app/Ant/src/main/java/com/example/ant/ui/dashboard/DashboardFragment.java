package com.example.ant.ui.dashboard;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.ant.R;
import com.example.ant.Utils.DirectionSet;
import com.example.ant.Utils.PointSet;
import com.example.ant.Utils.StepCountJudgment;

import java.util.ArrayList;

import static android.content.Context.SENSOR_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

public class DashboardFragment extends Fragment implements SensorEventListener {
    //    xml组件
    private LinearLayout linearLayout;
    private FrameLayout frameLayout;
    private TextView tvStep;
    private TextView tvDirection;
    private Button btnStart;


    //    传感器
    private SensorManager sManager;
    private Sensor mSensorAccelerometer;
    private Sensor mSensorMagnetic;


    //    方向设定
    private float direction = 0;
    //    步数设定
    private Integer step = 0;
    private Integer beforeStep = 0;


    //状态
    //    是否开始构建
    private boolean processState = false;
    //    计步姿态 0-手握 1-平握 2-放在口袋中
    private int statu;

    //    位置设定
    private PointSet pointSet;
    private float currentAngle = 0;
    private float currentX = 0;
    private float currentY = 0;
    private float nextX = 0;
    private float nextY = 0;
    private ArrayList points;

    //    制图类
    private MapSetView mapSetView;

    //    计步类
    private StepCountJudgment stepCountJudgment;

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
    }

    //注册组件
    public void findView() {
        linearLayout = getView().findViewById(R.id.linearLayout);
        frameLayout = getView().findViewById(R.id.frameLayout);
        tvStep = getView().findViewById(R.id.tv_step);
        tvDirection = getView().findViewById(R.id.tv_direction);
        btnStart = getView().findViewById(R.id.btn_start);
    }

    //监听器注册
    public void listener() {
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvStep.setText("0");
                if (processState == true) {
                    btnStart.setText("开始");
                    processState = false;
                } else {
                    btnStart.setText("停止");
                    processState = true;
                }
            }
        });

//        设定开始点
        pointSet = new PointSet(0, 0, 0, 30);
        points = new ArrayList<>();
//
        sManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mSensorAccelerometer = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorMagnetic = sManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sManager.registerListener(this, mSensorAccelerometer, SensorManager.SENSOR_DELAY_UI);
        sManager.registerListener(this, mSensorMagnetic, SensorManager.SENSOR_DELAY_UI);
        mapSetView = new MapSetView(getActivity());
        frameLayout.addView(mapSetView);

    }

    //注销
    @Override
    public void onDestroy() {
        super.onDestroy();
        sManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        statu = 1;
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float[] accelerometer = event.values;
            step = stepCountJudgment.judgment(statu, accelerometer, processState);
        }
        if (null != step && null != beforeStep && step > beforeStep) {
            tvStep.setText(String.valueOf(step));
            Log.i(":step", step + "");
            beforeStep = step;
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                float[] magnetic = event.values;
                Log.i(":direction", direction + "");
                direction = DirectionSet.directionSet(magnetic);
                tvDirection.setText(String.valueOf(direction));
                float[] floats = pointSet.calculatePoint(currentX, currentY, direction);
                nextX = floats[0];
                nextY = floats[1];
                currentX = nextX;
                currentY = nextY;
                points.add(nextX);
                points.add(nextY);
                if (null != points && points.size() >= 2) {
                    mapSetView.repaint(points);
                }
//                Log.i("t","+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//                for (int i = 0; i < points.size() - 2; i=i+2) {
//                    Log.i("XY",points.get(i)+"    "+points.get(i+1)+"");
//                }
//                Log.i("XY",nextX+"    "+nextY+"");
            } else {
                if (beforeStep!=1){
                    direction = 0;
                    beforeStep = 0;
                    tvDirection.setText(String.valueOf(direction));
                    tvStep.setText(String.valueOf(step));
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
