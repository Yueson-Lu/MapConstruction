package com.example.ant.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternTest {

    public static String PatternUser(String username) {
//        帐号是否合法(字母开头，允许5-16字节，允许字母数字下划线)
        Pattern pattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]{4,15}$");
        Matcher matcher = pattern.matcher(username);
        if (!matcher.matches()) {
            return "帐号:字母开头,由5-16位字母数字字符组成 \n";
        } else {
            return "";
        }
    }
//密码(以字母开头，长度在6~18之间，只能包含字母、数字和下划线)
    public static String PatternPassword(String password) {
        Pattern pattern = Pattern.compile("^[a-zA-Z]\\w{5,17}$");
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            return "密码：以字母开头,由6-18位字母数字组成 \n";
        } else {
            return "";
        }
    }

    public static String PatternPhone(String phone) {
        Pattern pattern = Pattern.compile("^(13[0-9]|14[5|7]|15[0|1|2|3|4|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$");
        Matcher matcher = pattern.matcher(phone);
        if (!matcher.matches()) {
            return "电话格式不正确 \n";
        } else {
            return "";
        }
    }

    public static String PatternEmail(String email) {
        Pattern pattern = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            return "邮箱格式不正确";
        } else {
            return "";
        }
    }
}
