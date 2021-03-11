package com.example.ant.ui.notifications;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ant.R;
import com.example.ant.Utils.Tips;
import com.example.ant.Utils.activityUtils.ActivityUtils;
import com.example.ant.dto.MyMap;
import com.example.ant.dto.User;

import java.util.ArrayList;

public class NavigationActivity extends AppCompatActivity {
    private ListView listView;
    private Button cancel;
    private Button compose;
    ArrayList<MyMap> myMaps;
    User user;
    public MyMap navigationMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        ActivityUtils.addActivity_(this);
        findByMe();
        myMaps = (ArrayList<MyMap>) this.getIntent().getSerializableExtra("myMaps");
        user = (User) this.getIntent().getSerializableExtra("user");
        ListNavigation listNavigation = new ListNavigation(this, myMaps);
        navigationMap=null;
        listView.setAdapter(listNavigation);
        listView.setChoiceMode(listView.CHOICE_MODE_MULTIPLE);
        listener();

    }

    public void findByMe() {
        listView = findViewById(R.id.list_view);
        cancel = findViewById(R.id.cancel);
        compose = findViewById(R.id.compose);
    }

    private void listener() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tips.exitComposeDlg(NavigationActivity.this, "确定退出");
            }
        });

        compose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listView.getCheckedItemCount() != 1) {
                    Tips.showShortMsg(NavigationActivity.this, "需要选择一张地图");
                } else {
                    SparseBooleanArray checkedItemPositions = listView.getCheckedItemPositions();
                    MyMap navigationMap = new MyMap();
                    for (int i = 0; i < myMaps.size(); i++) {
                        if (checkedItemPositions.get(i)) {
                            navigationMap = myMaps.get(i);
                        }
                    }
                    setNavigationMap(navigationMap);
                    Log.i("Map",getNavigationMap().toString());
                    if (null!=getNavigationMap()){
                      finish();
                    }else {
                        Tips.showLongMsg(getApplication(),"导航模块初始化失败");
                    }
                }
            }
        });
    }

    public MyMap getNavigationMap() {
        return this.navigationMap;
    }

    public void setNavigationMap(MyMap myMap){
        this.navigationMap=myMap;
    }
}
