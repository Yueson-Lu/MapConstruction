package com.example.ant.ui.home;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.ant.R;
import com.example.ant.Utils.StepCountJudgment;

public class HomeFragment extends Fragment implements View.OnClickListener, SensorEventListener{

    private SensorManager sManager;
    private Sensor mSensorAccelerometer;
    private TextView tv_step;
    private Button btn_start;
    private boolean processState = false;   //标记当前是否已经在计步
    private int statu;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home,container,false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        sManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mSensorAccelerometer = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sManager.registerListener(this, mSensorAccelerometer, SensorManager.SENSOR_DELAY_UI);
        bindViews();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //        Log.i("date", String.valueOf(System.currentTimeMillis()/10000));
        statu = 2;
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float[] value = event.values;
            Integer step = StepCountJudgment.judgment(statu, value, processState);
            if (null != step) {
//                Log.i("tag","可计步");
//                tv_step.setText(step + " ");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onClick(View v) {
//        tv_step.setText("0"+" ");
//        if (processState == true) {
//            btn_start.setText("开始");
//            processState = false;
//        } else {
//            btn_start.setText("停止");
//            processState = true;
//        }
    }

    private void bindViews() {
//        tv_step = getView(). findViewById(R.id.tv_step);
//        btn_start = getView(). findViewById(R.id.btn_start);
//        btn_start.setOnClickListener(this);
    }
}