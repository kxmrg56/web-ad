package com.example.news.dao;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DatabaseUtil {
    private static String url;
    private static String username;
    private static String password;
    private static String driver;

    static {
        try {
            // 加载数据库配置
            InputStream input = DatabaseUtil.class.getClassLoader()
                    .getResourceAsStream("database.properties");
            Properties prop = new Properties();
            prop.load(input);

            url = prop.getProperty("db.url");
            username = prop.getProperty("db.username");
            password = prop.getProperty("db.password");
            driver = prop.getProperty("db.driver");

            // 注册驱动
            Class.forName(driver);

        } catch (Exception e) {
            throw new ExceptionInInitializerError("数据库配置初始化失败: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(Connection conn, Statement stmt) {
        close(conn, stmt, null);
    }
}