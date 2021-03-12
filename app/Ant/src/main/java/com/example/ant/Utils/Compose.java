package com.example.ant.Utils;

import android.util.Log;

import com.example.ant.dto.MyMap;
import com.example.ant.dto.User;

import java.util.ArrayList;
import java.util.HashMap;

public class Compose {

    public static MyMap composeMap(ArrayList<MyMap> myMaps, MyMap myMapNew,Float dir,Float dis) {
         HashMap composePoint=new HashMap();
         composePoint.put("map1",   BlobUtil.getObject(myMaps.get(0).getPoints()));
         composePoint.put("map2",   BlobUtil.getObject(myMaps.get(1).getPoints()));
        myMapNew.setPoints(BlobUtil.setObject(composePoint));
        float[] floats = PointSet.caclulateDis(10, dis, dir);
//        Log.i("floats",floats.toString());
        myMapNew.setDisx(floats[0]);
        myMapNew.setDisy(floats[1]);
        return myMapNew;
    }
}
