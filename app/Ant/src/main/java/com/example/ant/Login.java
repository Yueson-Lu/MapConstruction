package com.example.ant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ant.Utils.Tips;
import com.example.ant.Utils.activityUtils.ActivityUtils;
import com.example.ant.dao.impl.UserDaoImpl;
import com.example.ant.dto.User;

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
    private UserDaoImpl userDao;
    private Handler mainHandler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        Log.i("this",Login.this.toString());
        ActivityUtils.addActivity_(Login.this);
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
        userDao = new UserDaoImpl();
        mainHandler=new Handler(getMainLooper());
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameText.getText().toString();
                String pws = pswText.getText().toString();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pws)) {
                    //通过makeText方法创建消息提示框
                    Tips.showShortMsg(Login.this,"用户名和密码不能为空");
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final User user = userDao.selectUser(new User(name, pws));
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (user.getUsername()==null||user.getPassword()==null){
                                        Tips.showShortMsg(Login.this,"用户名或密码错误");
                                    }else {
                                        nameText.setText("");
                                        pswText.setText("");
                                        Intent intent = new Intent(Login.this, Main.class);
                                        intent.putExtra("user",user);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    }).start();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
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
        sManager.unregisterListener(this);
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
