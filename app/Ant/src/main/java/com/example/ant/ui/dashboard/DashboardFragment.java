package com.example.ant.ui.dashboard;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
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

public class DashboardFragment extends Fragment implements View.OnClickListener,SensorEventListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_dashboard,container,false);
        return view;
    }

    //    传感器
    private SensorManager sManager;
    private Sensor mSensorAccelerometer;
    private Sensor mSensorMagnetic;
    //    xml组件
    private TextView tv_step;
    private TextView rotation;
    private LinearLayout linearLayout;
    private FrameLayout frameLayout;
    private Button btn_start;

    //    方向设定
    private Integer beforeStep = 0;
    private Integer afterStep = 0;
    private float direction = 0;

    //状态
    //    是否开始局部
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


    @Override
    public void onStart(){
        super.onStart();
//        设定开始点
        pointSet = new PointSet(0, 0, 0, 30);
        points = new ArrayList<>();
        bindViews();
//
        sManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mSensorAccelerometer = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorMagnetic = sManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sManager.registerListener(this, mSensorAccelerometer, SensorManager.SENSOR_DELAY_UI);
        sManager.registerListener(this, mSensorMagnetic, SensorManager.SENSOR_DELAY_UI);
        mapSetView = new MapSetView( getActivity());
        frameLayout.addView(mapSetView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sManager.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        statu = 1;
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float[] value = event.values;
            afterStep = StepCountJudgment.judgment(statu, value, processState);
            if (null != afterStep) {
                tv_step.setText(afterStep + "");
            }
        }
        if (afterStep != null) {
            if (afterStep > beforeStep && event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD && processState) {
                float[] value1 = event.values;
                direction = DirectionSet.directionSet(value1);
                float[] floats = pointSet.calculatePoint(currentX, currentY, direction);
                nextX = floats[0];
                nextY = floats[1];
                currentX = nextX;
                currentY = nextY;
                points.add(nextX);
                points.add(nextY);
                if(null!=points&&points.size()>=2){
                    mapSetView.repaint(points);
                }
//                Log.i("t","+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//                for (int i = 0; i < points.size() - 2; i=i+2) {
//                    Log.i("XY",points.get(i)+"    "+points.get(i+1)+"");
//                }
//                Log.i("XY",nextX+"    "+nextY+"");
            } else {
                direction = 0;
                rotation.setText(0 + "");
                beforeStep = 0;
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onClick(View v) {
        tv_step.setText("0");
        if (processState == true) {
            btn_start.setText("开始");
            processState = false;
        } else {
            btn_start.setText("停止");
            processState = true;
        }
    }

    private void bindViews() {
        tv_step = getActivity().findViewById(R.id.tv_step);
        btn_start = getActivity().findViewById(R.id.btn_start);
        rotation = getActivity().findViewById(R.id.rotation);
        linearLayout = getActivity().findViewById(R.id.linearLayout);
        btn_start.setOnClickListener(this);
        frameLayout = getActivity().findViewById(R.id.frameLayout);
    }
}
