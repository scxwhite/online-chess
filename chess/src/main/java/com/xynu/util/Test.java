package com.xynu.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void main(String args []) {
        Pattern r_db = Pattern.compile("(.*?\\w)/([\\w]*)");
        String databases = "jdbc:mysql://orderdb1118.my.2dfire-inc.com:3306/order_history100?tinyInt1isBit=false";
        Matcher matcher = r_db.matcher(databases);
        matcher.find();
        System.out.println(matcher.group(2));
    }
}
