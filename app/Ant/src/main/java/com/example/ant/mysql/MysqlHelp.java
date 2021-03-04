package com.example.ant.mysql;

import android.util.Log;

import com.example.ant.dto.User;
import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;


public class MysqlHelp {
    static String url = "jdbc:mysql://39.106.119.22:3306/map";
    static String root = "map";
    static String password = "123456";
    static String driver = "com.mysql.jdbc.Driver";
    static Connection cn = null;
    static PreparedStatement ps = null;
    static ResultSet rs = null;


    private MysqlHelp() {

    }


    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName(driver);//获取MYSQL驱动
            conn = DriverManager.getConnection(url, root, password);//获取连接
            System.out.println("连接数据库成功");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("连接数据库失败");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }


    /**
     * 关闭数据库
     */

    public static void closeAll() {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (cn != null) {
            try {
                cn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

