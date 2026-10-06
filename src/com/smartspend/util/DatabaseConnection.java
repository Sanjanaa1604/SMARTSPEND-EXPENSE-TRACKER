package com.smartspend.util;

import java.sql.*;

public final class DatabaseConnection {
    private DatabaseConnection() {}
    public static Connection getConnection() throws SQLException {
        String host=env("DB_HOST","localhost"), port=env("DB_PORT","3306"), db=env("DB_NAME","smartspend"), user=env("DB_USER","root"), pass=env("DB_PASSWORD","");
        String url="jdbc:mysql://"+host+":"+port+"/"+db+"?useSSL=false&serverTimezone=Asia/Kolkata&allowPublicKeyRetrieval=true";
        return DriverManager.getConnection(url,user,pass);
    }
    private static String env(String key,String fallback){String v=System.getenv(key); return v==null||v.isBlank()?fallback:v;}
}
