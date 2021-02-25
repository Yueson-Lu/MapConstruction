package com.example.ant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ant.mysql.MysqlHelp;

public class Login extends AppCompatActivity implements SensorEventListener {
    private ImageView earth;
    private TextView textView;
    private EditText nameText;
    private EditText pswText;
    private Button login;
    private Button register;
    private FrameLayout frameLayout;
    private SensorManager sManager;
    private Sensor mSensorAccelerometer;
    private MysqlHelp mysqlHelp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        earth = (ImageView) findViewById(R.id.earth);
        textView = (TextView) findViewById(R.id.textView);
        nameText = (EditText) findViewById(R.id.nameText);
        pswText = (EditText) findViewById(R.id.pswText);
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
        frameLayout = findViewById(R.id.frameLayout);
        sManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorAccelerometer = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sManager.registerListener(this, mSensorAccelerometer, SensorManager.SENSOR_DELAY_UI);
        mysqlHelp=new MysqlHelp();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameText.getText().toString();
                String pws = pswText.getText().toString();
                    Intent intent = new Intent(Login.this, Main.class);
                    startActivity(intent);
//                if ("".equals(name) || "".equals(pws)) {
//                    //通过makeText方法创建消息提示框
//                    Toast.makeText(Login.this, "用户名和密码不能为空", Toast.LENGTH_LONG).show();
//                } else if (!name.equals("lys2021") || !pws.equals("lys2021")) {
//                    Toast.makeText(Login.this, "用户名或密码错误", Toast.LENGTH_LONG).show();
//                } else {
//                    Intent intent = new Intent(Login.this, Main.class);
//                    startActivity(intent);
//                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            earthRotate();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    int ratate = 0;

    private void earthRotate() {
        ratate = ratate + 2;
        earth.setRotation(ratate);
        if (ratate == 360) {
            ratate = 0;
        }
    }
}
