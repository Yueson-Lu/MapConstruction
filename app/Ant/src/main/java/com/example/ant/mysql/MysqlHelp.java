package com.example.ant.mysql;

import android.util.Log;

import com.example.ant.Login;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class MysqlHelp {
    public  boolean insertSql()
    {
        String url_1=" jdbc:mysql://127.0.0.1:3306/mymap?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true&allowPublicKeyRetrieval=true";
        String UserName_1="root";
        String pass_1="EAson441224";
        boolean result=false;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            java.sql.Connection cn= DriverManager.getConnection(url_1,UserName_1,pass_1);
            Log.i("cn",cn.toString());
//            String sql = " " ;
//            Statement st=cn.createStatement();
//            result=st.execute(sql);
            result=true;
            cn.close();
//            st.close();
        } catch (ClassNotFoundException e) {
            result=false;
//              e.printStackTrace();
        } catch (SQLException e) {
            //e.printStackTrace();
            result=false;
        }
        Log.i("result",result+"");
        return  result;
    }
}