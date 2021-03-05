package com.example.ant.dao;

import com.example.ant.dto.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public interface MapPointDao {

    boolean addMap(int id,ArrayList points, ArrayList navigation);

    boolean deleteMap(int id);

    boolean updateMap(int id, ArrayList points, ArrayList navigation);

    HashMap selectmap(int id);

    HashMap slectAllMap();
}
