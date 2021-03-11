package com.example.ant.ui.dashboard;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ant.R;
import com.example.ant.dto.MyMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListCompose extends BaseAdapter {


    ArrayList<MyMap> arrayList;
    LayoutInflater layoutInflater;

    private TextView mapName;
    private TextView mapId;
    private TextView author;
    private TextView createTime;


    public ListCompose(Context context,ArrayList<MyMap> myMaps) {
        if (null==arrayList){
            arrayList=new ArrayList<>();
            this.arrayList=myMaps;
        }
        this.layoutInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=layoutInflater.inflate(R.layout.list_compose,null);
        mapName =view.findViewById(R.id.mapName);
        mapId = view.findViewById(R.id.mapId);
        author = view.findViewById(R.id.author);
        createTime = view.findViewById(R.id.createTime);
        mapName.setText(mapName.getText()+arrayList.get(position).getMapName());
        mapId.setText(mapId.getText()+""+arrayList.get(position).getId()+"");
        author.setText(author.getText()+arrayList.get(position).getAuthor());
        createTime.setText(createTime.getText()+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(arrayList.get(position).getCreateTime()));
        return view;
    }

}