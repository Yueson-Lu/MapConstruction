package com.example.ant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.ant.Utils.TimeCalculate;
import com.example.ant.Utils.Tips;
import com.example.ant.Utils.activityUtils.ActivityUtils;
import com.example.ant.dao.impl.UserDaoImpl;
import com.example.ant.dto.User;
import com.mysql.jdbc.StringUtils;

public class Register extends AppCompatActivity {

    private EditText tvUsername;
    private EditText tvPassword;
    private EditText tvPhone;
    private EditText tvEmail;
    private RadioGroup tvGender;
    private Button cancle;
    private Button register;
    private User user;
    private UserDaoImpl userDao;
    private Handler mainHandler;
    private String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActivityUtils.addActivity_(Register.this);
        findByMe();
        btnRegister();
        user = new User();
        userDao = new UserDaoImpl();
        mainHandler = new Handler();
    }


    private void findByMe() {
        tvUsername = (EditText) findViewById(R.id.tv_username);
        tvPassword = (EditText) findViewById(R.id.tv_password);
        tvPhone = (EditText) findViewById(R.id.tv_phone);
        tvEmail = (EditText) findViewById(R.id.tv_email);
        tvGender = (RadioGroup) findViewById(R.id.tv_gender);
        cancle = (Button) findViewById(R.id.cancel);
        register = (Button) findViewById(R.id.register);
    }

    private void btnRegister() {
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tips.exitDlg(Register.this, "确定取消注册吗？");
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isNullOrEmpty(String.valueOf(tvUsername.getText()))
                        || StringUtils.isNullOrEmpty(String.valueOf(tvPassword.getText()))
                        || StringUtils.isNullOrEmpty(String.valueOf(tvEmail.getText()))
                        || StringUtils.isNullOrEmpty(String.valueOf(tvPhone.getText()))
                ) {
                    Tips.showShortMsg(Register.this, "请完善注册信息");
                } else {
                    user.setUsername(tvUsername.getText().toString());
                    user.setPassword(tvPassword.getText().toString());
                    user.setEmail(tvEmail.getText().toString());
                    user.setPassword(tvPhone.getText().toString());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final User adduser = new User();
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    adduser.setUsername(tvUsername.getText().toString());
                                    adduser.setPassword(tvPassword.getText().toString());
                                    adduser.setEmail(tvEmail.getText().toString());
                                    adduser.setPhone(tvPhone.getText().toString());
                                    adduser.setGender(gender);
                                    adduser.setId(null);
                                }
                            });
                            boolean b = userDao.addUser(adduser);
                            if (b){
                                Tips.showShortMsg(Register.this,"注册成功");
                                Long nowTime = TimeCalculate.getNowTime();
                                if (nowTime-TimeCalculate.getNowTime()>1000){
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                }
                            }else {
                                Tips.showShortMsg(Register.this,"注册失败");
                            }
                        }
                    }).start();
                }
            }
        });

        tvGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case 1:
                        gender="男";
                        break;
                    case 2:
                        gender="女";
                        break;
                    case 3:
                        gender="保密";
                        break;
                    default:
                        gender="保密";
                        break;
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
