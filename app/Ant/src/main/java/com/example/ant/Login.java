package com.example.ant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    private ImageView imageView;
    private TextView textView;
    private EditText nameText;
    private EditText pswText;
    private Button login;
    private Button register;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        imageView = (ImageView) findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.textView);
        nameText = (EditText) findViewById(R.id.nameText);
        pswText = (EditText) findViewById(R.id.pswText);
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
        frameLayout = findViewById(R.id.frameLayout);

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

}
