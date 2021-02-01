package com.example.stepcount;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.stepcount.Utils.StepCountJudgment;

public class MapSet extends AppCompatActivity implements View.OnClickListener, SensorEventListener {
    private SensorManager sManager;
    private Sensor Sstep;
    private Sensor Scount;
    private Sensor Sdirtion;
    private float stepCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapset);
        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sstep = sManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        Sdirtion = sManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        Scount=sManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
//        sManager.registerListener(this, Sstep, SensorManager.SENSOR_DELAY_FASTEST);
//        sManager.registerListener(this, Sdirtion, SensorManager.SENSOR_DELAY_UI);
        sManager.registerListener(this, Scount, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        stepCount = 0;
//        if (event.sensor == sManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)) {
//            Log.i("step","1111");
//            float[] value = event.values;
//            for (float v : value) {
//                Log.i("step", String.valueOf(stepCount));
//            }
//        }
//        if (event.sensor == sManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)) {
//                float[] value = event.values;
//                for (float v : value) {
//                    Log.i("rotation", String.valueOf(v));
//                }
//        }
        if (event.sensor == sManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)) {
            float[] value = event.values;
            for (float v : value) {
                Log.i("11111", String.valueOf(v));
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onClick(View v) {

    }
}
