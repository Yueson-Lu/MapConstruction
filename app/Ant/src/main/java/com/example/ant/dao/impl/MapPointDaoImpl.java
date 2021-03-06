package com.example.ant.dao.impl;

import com.example.ant.dao.MapPointDao;
import com.example.ant.dto.MyMap;
import com.example.ant.mysql.MysqlHelp;
import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class MapPointDaoImpl implements MapPointDao {

    String url = "jdbc:mysql://39.106.119.22:3306/map?SSL=false";
    String root = "map";
    String psw = "123456";
    String driver = "com.mysql.jdbc.Driver";
    Connection cn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    boolean result;

    @Override
    public boolean addMap(MyMap myMap) {
        //MySQL 语句
        String sql = "insert into map(id,mapName,points,navigation,canNavigation,isPub,authorId,author,createTime) values(?,?,?,?,?,?,?,?,?)";
        //获取链接数据库对象
        cn = MysqlHelp.getConnection();
//        预编译
        if (null != cn) {
            try {
                ps = (PreparedStatement) cn.prepareStatement(sql);
                ps.setInt(1,myMap.getId());
                ps.setString(2,myMap.getMapName());
                ps.setBytes(3,myMap.getPoints());
                ps.setBytes(4,myMap.getNavigation());
                ps.setInt(5,myMap.isCanNavigation()?1:0);
                ps.setInt(6,myMap.isPub()?1:0);
                ps.setInt(7,myMap.getAuthorId());
                ps.setString(8,myMap.getAuthor());
                ps.setTimestamp(9,new Timestamp(myMap.getCreateTime().getTime()));
                int i = ps.executeUpdate();
                if (i == 1) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        MysqlHelp.closeAll();
        return result;
    }

    @Override
    public boolean deleteMap(int id) {
        return false;
    }

    @Override
    public boolean updateMap(MyMap myMap) {
        return false;
    }

    @Override
    public MyMap selectmap(int id) {
        return null;
    }

    @Override
    public ArrayList<MyMap> slectAllMap() {
        return null;
    }
}
