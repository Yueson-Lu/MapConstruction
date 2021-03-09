package com.example.ant.Utils;

import android.util.Log;

import com.example.ant.dto.MyMap;
import com.example.ant.dto.User;

import java.util.ArrayList;

public class Compose {

    public static MyMap composeMap(ArrayList<MyMap> myMaps, MyMap myMapNew,Float dir,Float dis) {
        Log.i("map1",myMaps.get(0).toString());
        Log.i("map2",myMaps.get(1).toString());
        Log.i("mapNew",myMapNew.toString());
        Log.i("dir",dir.toString());
        Log.i("dis",dis.toString());
        return null;
    }
}
