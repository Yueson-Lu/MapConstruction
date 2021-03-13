package com.example.ant.Utils;

import android.util.Log;

import com.example.ant.dto.MyMap;
import com.example.ant.dto.User;

import java.util.ArrayList;
import java.util.HashMap;

public class Compose {

    public static MyMap composeMap(ArrayList<MyMap> myMaps, MyMap myMapNew,Float dir,Float dis) {
         HashMap composePoint=new HashMap();
         HashMap composeNavigation=new HashMap();
         composePoint.put("map1",   BlobUtil.getObject(myMaps.get(0).getPoints()));
         composePoint.put("map2",   BlobUtil.getObject(myMaps.get(1).getPoints()));
        composeNavigation.put("map1",BlobUtil.getObject(myMaps.get(0).getNavigation()));
        composeNavigation.put("map2",BlobUtil.getObject(myMaps.get(1).getNavigation()));
        myMapNew.setPoints(BlobUtil.setObject(composePoint));
        myMapNew.setNavigation(BlobUtil.setObject(composeNavigation));
        float[] floats = PointSet.caclulateDis(10, dis, dir);
        myMapNew.setDisx(floats[0]);
        myMapNew.setDisy(floats[1]);
        return myMapNew;
    }
}
