package com.example.stepcount;

import android.graphics.Canvas;
import android.graphics.Path;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.stepcount.Utils.DirectionSet;
import com.example.stepcount.Utils.PointSet;
import com.example.stepcount.Utils.StepCountJudgment;

import java.util.ArrayList;


public class MapSet extends AppCompatActivity implements View.OnClickListener, SensorEventListener {
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
    private  ArrayList points;

    //    制图类
    private MapSetView mapSetView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapset);
        pointSet = new PointSet(currentX, currentY, 0, 10);
        points=new ArrayList<>();
        points.add(currentX);
        points.add(currentY);
        bindViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorAccelerometer = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorMagnetic = sManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sManager.registerListener(this, mSensorAccelerometer, SensorManager.SENSOR_DELAY_UI);
        sManager.registerListener(this, mSensorMagnetic, SensorManager.SENSOR_DELAY_UI);
        mapSetView = new MapSetView(this);
        frameLayout.addView(mapSetView);
    }

    @Override
    protected void onDestroy() {
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
                nextX=floats[0];
                nextY=floats[1];
                points.add(nextX);
                points.add(nextY);
//                mapSetView.draw(points);
                Log.i("XY",nextX+"    "+nextY+"");
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
        tv_step = findViewById(R.id.tv_step);
        btn_start = findViewById(R.id.btn_start);
        rotation = findViewById(R.id.rotation);
        linearLayout = findViewById(R.id.linearLayout);
        btn_start.setOnClickListener(this);
        frameLayout = findViewById(R.id.frameLayout);
    }
}
