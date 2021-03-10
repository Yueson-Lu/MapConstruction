package com.example.ant.dao.impl;

import android.util.Log;

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
        String sql1 = "insert into map(id,mapName,points,navigation,canNavigation,isPub,authorId,author,createTime,disx,disy) values(?,?,?,?,?,?,?,?,?,?,?)";
        //获取链接数据库对象
        cn = MysqlHelp.getConnection();
//        预编译
        if (null != cn) {
            try {
                ps = (PreparedStatement) cn.prepareStatement(sql1);
                ps.setInt(1, myMap.getId());
                ps.setString(2, myMap.getMapName());
                ps.setBytes(3, myMap.getPoints());
                ps.setBytes(4, myMap.getNavigation());
                ps.setInt(5, myMap.isCanNavigation() ? 1 : 0);
                ps.setInt(6, myMap.isPub() ? 1 : 0);
                ps.setInt(7, myMap.getAuthorId());
                ps.setString(8, myMap.getAuthor());
                ps.setTimestamp(9, new Timestamp(myMap.getCreateTime().getTime()));
                ps.setFloat(10, myMap.getDisx());
                ps.setFloat(11, myMap.getDisy());
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
    public MyMap selectMap(int id) {
        //MySQL 语句
        String sql = "select * from map where id=?";
        //获取链接数据库对象
        cn = MysqlHelp.getConnection();
//        预编译
        MyMap myMap = new MyMap();
        if (null != cn) {
            try {
                ps = (PreparedStatement) cn.prepareStatement(sql);
                ps.setInt(1, id);
                rs = ps.executeQuery();
                while (rs.next()) {
                    myMap.setId(rs.getInt("id"));
                    myMap.setMapName(rs.getString("mapName"));
                    myMap.setPoints(rs.getBytes("points"));
                    myMap.setNavigation(rs.getBytes("navigation"));
                    myMap.setCanNavigation(rs.getInt("canNavigation") == 1 ? true : false);
                    myMap.setPub(rs.getInt("isPub") == 1 ? true : false);
                    myMap.setAuthor(rs.getString("author"));
                    myMap.setAuthorId(rs.getInt("authorId"));
                    myMap.setCreateTime(rs.getTimestamp("createTime"));
                    myMap.setDisx(rs.getFloat("disx"));
                    myMap.setDisy(rs.getFloat("disy"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
//        Log.i("mymap",mymap1.toString());
        MysqlHelp.closeAll();
        return myMap;   //MySQL 语句
    }

    @Override
    public ArrayList<MyMap> selectAllMap() {
        //MySQL 语句
        String sql = "select * from map where isPub=1 and canNavigation=1";
        //获取链接数据库对象
        cn = MysqlHelp.getConnection();
//        预编译
        ArrayList<MyMap> myMaps = new ArrayList<>();
        if (null != cn) {
            try {
                ps = (PreparedStatement) cn.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    MyMap myMap = new MyMap();
                    myMap.setId(rs.getInt("id"));
                    myMap.setMapName(rs.getString("mapName"));
                    myMap.setPoints(rs.getBytes("points"));
                    myMap.setNavigation(rs.getBytes("navigation"));
                    myMap.setCanNavigation(rs.getInt("canNavigation") == 1 ? true : false);
                    myMap.setPub(rs.getInt("isPub") == 1 ? true : false);
                    myMap.setAuthor(rs.getString("author"));
                    myMap.setAuthorId(rs.getInt("authorId"));
                    myMap.setCreateTime(rs.getTimestamp("createTime"));
                    myMap.setDisx(rs.getFloat("disx"));
                    myMap.setDisy(rs.getFloat("disy"));
                    myMaps.add(myMap);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        Log.i("mymaps", myMaps.size() + "");
        MysqlHelp.closeAll();
        return myMaps;
    }

    @Override
    public ArrayList<MyMap> selectAllMap(int athorId) {
        //MySQL 语句
        String sql = "select * from map where authorId=" + athorId;
        //获取链接数据库对象
        cn = MysqlHelp.getConnection();
//        预编译
        ArrayList<MyMap> myMaps = new ArrayList<>();
        if (null != cn) {
            try {
                ps = (PreparedStatement) cn.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    MyMap myMap = new MyMap();
                    myMap.setId(rs.getInt("id"));
                    myMap.setMapName(rs.getString("mapName"));
                    myMap.setPoints(rs.getBytes("points"));
                    myMap.setNavigation(rs.getBytes("navigation"));
                    myMap.setCanNavigation(rs.getInt("canNavigation") == 1 ? true : false);
                    myMap.setPub(rs.getInt("isPub") == 1 ? true : false);
                    myMap.setAuthor(rs.getString("author"));
                    myMap.setAuthorId(rs.getInt("authorId"));
                    myMap.setCreateTime(rs.getTimestamp("createTime"));
                    myMap.setDisx(rs.getFloat("disx"));
                    myMap.setDisy(rs.getFloat("disy"));
                    myMaps.add(myMap);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
//        Log.i("mymaps", myMaps.size() + "");
        MysqlHelp.closeAll();
        return myMaps;
    }
}
