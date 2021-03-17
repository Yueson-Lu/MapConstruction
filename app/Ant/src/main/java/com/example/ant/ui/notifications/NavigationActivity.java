package com.example.ant.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ant.Main;
import com.example.ant.R;
import com.example.ant.Utils.NavigationSet;
import com.example.ant.Utils.Tips;
import com.example.ant.Utils.activityUtils.ActivityUtils;
import com.example.ant.dao.impl.MapPointDaoImpl;
import com.example.ant.dto.MyMap;
import com.example.ant.dto.User;
import com.example.ant.ui.dashboard.ComposeActivity;
import com.example.ant.ui.dashboard.ListCompose;

import java.util.ArrayList;

public class NavigationActivity extends AppCompatActivity {
    private ListView listView;
    private Button cancel;
    private Button compose;
    private Switch resource;
    ArrayList<MyMap> selectMaps;
    User user;
    public MyMap navigationMap;
    private static Integer NAVIGATIONMAP_REQUEST = 0;
    private static Integer NAVIGATIONMAP_RESULT = 1;
    private ListNavigation listNavigation;

    //    数据库操作
    public static Handler mainHandler;
    public static MapPointDaoImpl mapPointDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        ActivityUtils.addActivity_(this);
        findByMe();
        selectMaps = (ArrayList<MyMap>) this.getIntent().getSerializableExtra("myMaps");
        user = (User) this.getIntent().getSerializableExtra("user");
        listNavigation = new ListNavigation(this, selectMaps);
        navigationMap = null;
        listView.setAdapter(listNavigation);
        listView.setChoiceMode(listView.CHOICE_MODE_MULTIPLE);
        listener();
        mainHandler = new Handler();
        mapPointDao = new MapPointDaoImpl();

    }

    public void findByMe() {
        listView = findViewById(R.id.list_view);
        cancel = findViewById(R.id.cancel);
        compose = findViewById(R.id.compose);
        resource = findViewById(R.id.resource);

    }

    private void listener() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        resource.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<MyMap> myMaps = mapPointDao.selectAllMap(user.getId());
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (null != myMaps) {
                                        selectMaps = myMaps;
                                        listNavigation =new ListNavigation(NavigationActivity.this, selectMaps);
                                        listView.setAdapter(listNavigation);
                                    } else {
                                        Tips.showShortMsg(NavigationActivity.this, "地图库为空");
                                    }
                                }
                            });
                        }
                    }).start();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<MyMap> myMaps = mapPointDao.selectAllMap();
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (null != myMaps) {
                                        selectMaps = myMaps;
                                        listNavigation = new ListNavigation(NavigationActivity.this, selectMaps);
                                        listView.setAdapter(listNavigation);
                                    } else {
                                        Tips.showShortMsg(NavigationActivity.this, "地图库为空");
                                    }
                                }
                            });
                        }
                    }).start();
                }
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
                    for (int i = 0; i < selectMaps.size(); i++) {
                        if (checkedItemPositions.get(i)) {
                            navigationMap = selectMaps.get(i);
                        }
                    }
                    setNavigationMap(navigationMap);
//                    Log.i("Map", getNavigationMap().toString());
                    if (null != getNavigationMap()) {
                        Intent intent = new Intent(NavigationActivity.this, Main.class);
                        //设置启动intent以及对应的请求码
                        intent.putExtra("navigationMap", getNavigationMap());
                        setResult(NAVIGATIONMAP_RESULT, intent);
                        finish();
                    } else {
                        Tips.showLongMsg(getApplication(), "导航模块初始化失败");
                    }
                }
            }
        });
    }

    public MyMap getNavigationMap() {
        return this.navigationMap;
    }

    public void setNavigationMap(MyMap myMap) {
        this.navigationMap = myMap;
    }
}
