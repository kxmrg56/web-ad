package com.example.news.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class JdbcHelper {
    // 使用你提供的数据库连接信息
    private static final String URL = "jdbc:mysql://10.100.164.38:3306/news_db?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "MySQL@2025";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("数据库驱动加载成功");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("加载数据库驱动失败", e);
        }
    }

    // 获取数据库连接
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // 资源关闭
    public static void close(AutoCloseable... closables) {
        for (AutoCloseable closable : closables) {
            if (closable != null) {
                try {
                    closable.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 查询单个对象
    public static <T> T queryForObject(String sql, Function<ResultSet, T> mapper, Object... params) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapper.apply(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("数据库查询失败: " + e.getMessage(), e);
        } finally {
            close(rs, pstmt, conn);
        }
    }

    // 查询列表
    public static <T> List<T> queryForList(String sql, Function<ResultSet, T> mapper, Object... params) {
        List<T> result = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            rs = pstmt.executeQuery();
            while (rs.next()) {
                result.add(mapper.apply(rs));
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException("数据库查询失败: " + e.getMessage(), e);
        } finally {
            close(rs, pstmt, conn);
        }
    }

    // 执行更新
    public static int executeUpdate(String sql, Object... params) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            return pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("数据库更新失败: " + e.getMessage(), e);
        } finally {
            close(pstmt, conn);
        }
    }

    // 执行插入并返回主键
    public static int executeInsert(String sql, Object... params) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException("数据库插入失败: " + e.getMessage(), e);
        } finally {
            close(rs, pstmt, conn);
        }
    }
}