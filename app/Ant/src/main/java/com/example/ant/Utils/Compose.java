package com.example.ant.Utils;

import android.util.Log;

import com.example.ant.dto.MyMap;
import com.example.ant.dto.User;

import java.util.ArrayList;

public class Compose {

    public static MyMap composeMap(ArrayList<MyMap> myMaps, MyMap myMapNew,Float dir,Float dis) {
        myMapNew.setPoints(BlobUtil.setObject(myMaps));
        float[] floats = PointSet.caclulateDis(10, dis, dir);
        Log.i("floats",floats.toString());
        myMapNew.setDisx(floats[0]);
        myMapNew.setDisy(floats[1]);
        return myMapNew;
    }
}
