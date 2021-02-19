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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.ant.R;
import com.example.ant.Utils.StepCountJudgment;

public class homeFragment extends Fragment implements SensorEventListener {

    private SensorManager sManager;
    private Sensor mSensorAccelerometer;
    private TextView tv_step;
    private Integer step = 0;
    private Button btn_start;
    private boolean processState = false;   //标记当前是否已经在计步
    private int statu = 1; //默认为平卧状态
    private RadioGroup radioGroup;
    private StepCountJudgment stepCountJudgment;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        findView();
        listener();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //        Log.i("date", String.valueOf(System.currentTimeMillis()/10000));
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float[] value = event.values;
            step = stepCountJudgment.judgment(statu, value, processState);
            if (null != step && processState) {
                Log.i("statu", statu + "");
                tv_step.setText(String.valueOf(step));
            }
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //注册组件
    private void findView() {
        tv_step = getView().findViewById(R.id.tv_step);
        btn_start = getView().findViewById(R.id.btn_start);
        radioGroup = getView().findViewById(R.id.radioGroup);
        radioGroup.check(radioGroup.getChildAt(1).getId());
    }

    //    监听器
    public void listener() {
        stepCountJudgment = new StepCountJudgment();
        sManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mSensorAccelerometer = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sManager.registerListener(this, mSensorAccelerometer, SensorManager.SENSOR_DELAY_UI);
        btn_start.setOnClickListener(new View.OnClickListener() {
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
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                Log.i("id",group.getCheckedRadioButtonId()+"");
                switch (group.getCheckedRadioButtonId()) {
                    case 1:
                        statu = 0;
                        Toast.makeText(getActivity(), "切换至手持模式", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        statu = 1;
                        Toast.makeText(getActivity(), "切换至平握模式", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        statu = 2;
                        Toast.makeText(getActivity(), "切换至正常模式", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        statu = 1;
                        break;
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sManager.unregisterListener(this);
    }
}