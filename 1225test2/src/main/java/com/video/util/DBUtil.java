package com.video.util;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBUtil {
    private static Properties props = new Properties();
    static {
        try (InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            props.load(in);
            Class.forName(props.getProperty("driver"));
        } catch (Exception e) { e.printStackTrace(); }
    }
    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(props.getProperty("url"), props.getProperty("user"), props.getProperty("password"));
    }
}