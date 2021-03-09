package com.example.ant.dao;

import com.example.ant.dto.MyMap;
import com.example.ant.dto.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public interface MapPointDao {

    boolean addMap(MyMap myMap);

    boolean deleteMap(int id);

    boolean updateMap(MyMap myMap);

    MyMap selectMap(int id);

    ArrayList<MyMap> selectAllMap();

    ArrayList<MyMap> selectAllMap(int athorId);
}
