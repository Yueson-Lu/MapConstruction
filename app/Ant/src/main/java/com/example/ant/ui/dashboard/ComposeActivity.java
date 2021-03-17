package com.example.ant.ui.dashboard;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.ant.R;
import com.example.ant.Utils.Compose;
import com.example.ant.Utils.Tips;
import com.example.ant.Utils.activityUtils.ActivityUtils;
import com.example.ant.dao.impl.MapPointDaoImpl;
import com.example.ant.dto.MyMap;
import com.example.ant.dto.User;
import com.google.android.gms.common.util.JsonUtils;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;

public class ComposeActivity extends AppCompatActivity {
    private ListView listView;
    private Button cancel;
    private Button compose;
    private TextView Msg;
    private EditText dir;
    private EditText dis;
    ArrayList<MyMap> selectMaps;
    User user;
    private Switch resource;
    //    数据库操作
    public static Handler mainHandler;
    public static MapPointDaoImpl mapPointDao;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        ActivityUtils.addActivity_(this);
        findByMe();
        listener();
        mainHandler=new Handler();
        mapPointDao=new MapPointDaoImpl();
        user = (User) this.getIntent().getSerializableExtra("user");
        selectMaps = (ArrayList<MyMap>) this.getIntent().getSerializableExtra("myMaps");
        ListCompose listCompose = new ListCompose(ComposeActivity.this, selectMaps);
        listView.setAdapter(listCompose);
        listView.setChoiceMode(listView.CHOICE_MODE_MULTIPLE);
    }

    public void findByMe() {
        listView = findViewById(R.id.list_view);
        cancel = findViewById(R.id.cancel);
        compose = findViewById(R.id.compose);
        resource = findViewById(R.id.resource);
        Msg = findViewById(R.id.Msg);
        dir = findViewById(R.id.dir);
        dis = findViewById(R.id.dis);
    }

    private void listener() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComposeActivity.this.finish();
            }
        });

        resource.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<MyMap> myMaps=mapPointDao.composeAllMyMap(user.getId());
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (null != myMaps) {
                                        selectMaps = myMaps;
                                        ListCompose listCompose = new ListCompose(ComposeActivity.this, selectMaps);
                                        listView.setAdapter(listCompose);
                                    } else {
                                        Tips.showShortMsg(ComposeActivity.this, "地图库为空");
                                    }
                                }
                            });
                        }
                    }).start();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<MyMap> myMaps = mapPointDao.composeAllMap();
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (null != myMaps) {
                                        selectMaps = myMaps;
                                        ListCompose listCompose = new ListCompose(ComposeActivity.this, selectMaps);
                                        listView.setAdapter(listCompose);
                                    } else {
                                        Tips.showShortMsg(ComposeActivity.this, "地图库为空");
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
                if (listView.getCheckedItemCount() != 2) {
                    Tips.showShortMsg(ComposeActivity.this, "需要选择两张地图");
                } else {
                    SparseBooleanArray checkedItemPositions = listView.getCheckedItemPositions();
                    System.out.println(checkedItemPositions.toString());
                    ArrayList<MyMap> composerMap = new ArrayList<>();
                    for (int i = 0; i < selectMaps.size(); i++) {
                        if (checkedItemPositions.get(i)) {
                            composerMap.add(selectMaps.get(i));
                        }
                    }
                    String Msg = "地图:" + composerMap.get(0).getMapName() + "与" + "地图:" + composerMap.get(1).getMapName() + "起点的相对位置";
                    Tips.composeDlg(ComposeActivity.this, composerMap, user, Msg);
                }
            }
        });
    }

}
