package com.example.ant.ui.notifications;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.example.ant.Main;
import com.example.ant.R;
import com.example.ant.Utils.Tips;
import com.example.ant.dto.MyMap;

import java.util.ArrayList;

public class SelectAvtivity extends Activity {

    private static Integer NAVIGATIONMAP_REQUEST = 0;
    private static Integer NAVIGATIONMAP_RESULT = 1;
    private static Integer STARTANDEND_REQUEST = 2;
    private static Integer STARTANDEND_RESULT = 3;
    Spinner selectstart;
    Spinner selectend;
    Button cancel;
    Button select;
    ArrayList navigations;
    ArrayList items;
    Integer[] startAndEnd = new Integer[]{null, null};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.select_dialog);
        findByMe();
        select();
    }

    public void findByMe() {
        selectstart = this.findViewById(R.id.selectstart);
        selectend = this.findViewById(R.id.selectend);
        cancel = this.findViewById(R.id.cancel);
        select = this.findViewById(R.id.select);

    }

    private void select(){
        items = new ArrayList();
        navigations= (ArrayList) this.getIntent().getSerializableExtra("navigationMapNavigation");
        System.out.println(navigations.toString() + "++++++++++++++++++++++++++++++++");
        for (int i = 0; i < navigations.size() - 1; i = i + 2) {
            items.add(("导航点" + navigations.get(i) + ":" + navigations.get(i + 1)));
        }
//        System.out.println(items.toString());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectstart.setAdapter(adapter);
        selectend.setAdapter(adapter);

        selectstart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                startAndEnd[0]=position*2;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        selectend.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                startAndEnd[1] = position * 2;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
               finish();
            }
        });
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("11111111111111111111111111111111111",startAndEnd[0]+"");
                if (startAndEnd[0] == startAndEnd[1]) {
                    Tips.showShortMsg(SelectAvtivity.this, "起点和终点不能一样");
                } else {
                    if (null!=startAndEnd[0]&&null!=startAndEnd[1]){
                        Intent intent = new Intent(SelectAvtivity.this, Main.class);
                        //设置启动intent以及对应的请求码
                        intent.putExtra("startAndEnd", startAndEnd);
                        setResult(STARTANDEND_RESULT, intent);
                        finish();
                    }else {
                        Tips.showShortMsg(SelectAvtivity.this, "请选择导航点");
                    }
                }
            }
        });
    }
}