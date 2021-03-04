package com.example.ant.dao.impl;

import com.example.ant.dao.UserDao;
import com.example.ant.dto.User;
import com.example.ant.mysql.MysqlHelp;
import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao {

    String url = "jdbc:mysql://39.106.119.22:3306/map?SSL=false";
    String root = "map";
    String psw = "123456";
    String driver = "com.mysql.jdbc.Driver";
    Connection cn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    boolean result;
    public static MysqlHelp mysqlHelp;


    @Override
    public boolean addUser(User user) {
        //MySQL 语句
        String sql = "insert into user(id,username,password,gender,phone,email) values(?,?,?,?,?,?)";
        //获取链接数据库对象
        cn = MysqlHelp.getConnection();
//        预编译
        if (null != cn) {
            try {
                ps = (PreparedStatement) cn.prepareStatement(sql);
                ps.setString(1,null);
                ps.setString(2, user.getUsername());
                ps.setString(3, user.getPassword());
                ps.setString(4, user.getGender());
                ps.setString(5, user.getPhone());
                ps.setString(6, user.getEmail());
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
    public boolean deleteUser(User user) {
        return false;
    }

    @Override
    public boolean updateUser(User user) {
        return false;
    }

    @Override
    public User selectUser(User user) {
        User u = new User();
        //MySQL 语句
        String sql = "select * from user where username=?&&password=?";
        //获取链接数据库对象
        cn = MysqlHelp.getConnection();
//        预编译
        if (null != cn) {
            try {
                ps = (PreparedStatement) cn.prepareStatement(sql);
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getPassword());
                rs = ps.executeQuery();
                if (null != rs) {
                    while (rs.next()) {
                        u.setId(rs.getInt("id"));
                        u.setUsername(rs.getString("username"));
                        u.setPassword(rs.getString("password"));
                        u.setPhone(rs.getString("phone"));
                        u.setEmail(rs.getString("email"));
                        u.setGender(rs.getString("gender"));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        MysqlHelp.closeAll();
        return u;
    }
}
