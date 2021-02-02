package com.example.stepcount;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.stepcount.Utils.StepCountJudgment;


public class MapSet extends AppCompatActivity implements View.OnClickListener, SensorEventListener {
    private SensorManager sManager;
    private Sensor mSensorAccelerometer;
    private Sensor mSensorMagnetic;
    private TextView tv_step;
    private TextView rotation;
    private LinearLayout linearLayout;
    private Integer beforeStep = 0;
    private Integer afterStep = 0;
    private int direction = 0;
    private Button btn_start;
    //标记当前是否已经在计步
    private boolean processState = false;
    private int statu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapset);

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
        linearLayout.addView(new MapSetView(this));
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
//                x,y轴的磁场强度
                float x = value1[0];
                float y = value1[1];
//                Log.i("y",y+"");
                if (x <= 0) {
                    if (y >= 0) {
                        direction = (int) (-Math.tanh(x / y) * 90);
                    } else {
                        direction = (int) ((-Math.tanh(x / y) * 90) + 180);
                    }
                } else {
                    if (y >= 0) {
                        direction = (int) ((-Math.tanh(x / y) * 90) + 360);
                    } else {
                        direction = (int) ((-Math.tanh(x / y) * 90) + 180);
                    }
                }
                Log.i("方向", String.valueOf(direction));
                beforeStep = afterStep;
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
        tv_step = (TextView) findViewById(R.id.tv_step);
        btn_start = (Button) findViewById(R.id.btn_start);
        rotation = findViewById(R.id.rotation);
        linearLayout = findViewById(R.id.linearLayout);
        btn_start.setOnClickListener(this);
    }
}
