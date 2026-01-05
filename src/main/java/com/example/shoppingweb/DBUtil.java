package com.example.shoppingweb;

import java.sql.*;
import java.util.Properties;
import java.io.InputStream;

public class DBUtil {
    private static String url;
    private static String username;
    private static String password;
    private static boolean initialized = false;
    private static String errorMessage = "";

    static {
        try {
            System.out.println("========================================");
            System.out.println("🔧 DBUtil静态初始化开始");

            // 1. 加载配置
            Properties props = new Properties();
            InputStream input = DBUtil.class.getClassLoader()
                    .getResourceAsStream("database.properties");

            if (input != null) {
                System.out.println("✅ 找到database.properties文件");
                props.load(input);
                url = props.getProperty("db.url");
                username = props.getProperty("db.username");
                password = props.getProperty("db.password");

                System.out.println("📋 配置信息:");
                System.out.println("   URL: " + url);
                System.out.println("   用户: " + username);
                System.out.println("   密码: " + (password != null ? "已设置" : "未设置"));
            } else {
                System.out.println("❌ 未找到database.properties，使用默认配置");
                // 使用我们知道的正确配置
                url = "jdbc:mysql://10.100.164.39:3306/shopping_web?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";
                username = "root";
                password = "Kxmrg5.5";
            }

            // 2. 加载驱动
            System.out.println("🔄 加载MySQL驱动...");
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL驱动加载成功");

            // 3. 测试连接
            System.out.println("🔗 测试数据库连接...");
            Connection conn = null;
            try {
                DriverManager.setLoginTimeout(5);
                System.out.println("   连接URL: " + url);
                System.out.println("   用户名: " + username);

                long startTime = System.currentTimeMillis();
                conn = DriverManager.getConnection(url, username, password);
                long endTime = System.currentTimeMillis();

                System.out.println("🎉 数据库连接成功！耗时: " + (endTime - startTime) + "ms");
                initialized = true;

                // 显示数据库信息
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("📊 数据库信息:");
                System.out.println("   产品: " + meta.getDatabaseProductName());
                System.out.println("   版本: " + meta.getDatabaseProductVersion());
                System.out.println("   URL: " + meta.getURL());
                System.out.println("   用户: " + meta.getUserName());

                // 测试查询
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT DATABASE() as db, VERSION() as version");
                if (rs.next()) {
                    System.out.println("   当前数据库: " + rs.getString("db"));
                    System.out.println("   MySQL版本: " + rs.getString("version"));
                }
                rs.close();

                // 检查表
                rs = stmt.executeQuery("SHOW TABLES");
                System.out.println("📋 数据库中的表:");
                int tableCount = 0;
                while (rs.next()) {
                    System.out.println("   - " + rs.getString(1));
                    tableCount++;
                }
                System.out.println("   共 " + tableCount + " 个表");

                rs.close();
                stmt.close();

            } catch (SQLException e) {
                errorMessage = e.getMessage();
                System.err.println("❌ 数据库连接失败！");
                System.err.println("   错误信息: " + e.getMessage());
                System.err.println("   错误码: " + e.getErrorCode());
                System.err.println("   SQL状态: " + e.getSQLState());
                initialized = false;

                // 详细诊断
                diagnoseError(e);

            } finally {
                if (conn != null) {
                    try { conn.close(); } catch (SQLException e) {}
                }
            }

            System.out.println("========================================");
            System.out.println("🔧 DBUtil初始化完成");
            System.out.println("   数据库状态: " + (initialized ? "✅ 已连接" : "❌ 未连接"));
            if (!initialized && !errorMessage.isEmpty()) {
                System.err.println("   错误原因: " + errorMessage);
            }
            System.out.println("========================================");

        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL驱动未找到！");
            System.err.println("   请检查是否添加了MySQL依赖");
            errorMessage = "MySQL驱动未找到: " + e.getMessage();
            initialized = false;

        } catch (Exception e) {
            System.err.println("❌ DBUtil初始化异常: " + e.getMessage());
            e.printStackTrace();
            errorMessage = e.getMessage();
            initialized = false;
        }
    }

    private static void diagnoseError(SQLException e) {
        String message = e.getMessage().toLowerCase();

        if (message.contains("access denied")) {
            System.err.println("\n🔍 诊断: 权限问题");
            System.err.println("   可能原因:");
            System.err.println("   1. 用户名或密码错误");
            System.err.println("   2. 用户没有远程访问权限");
            System.err.println("   3. MySQL用户认证插件问题");
            System.err.println("\n💡 解决方案:");
            System.err.println("   在MySQL服务器上执行:");
            System.err.println("   CREATE USER 'root'@'%' IDENTIFIED BY 'Kxmrg5.5';");
            System.err.println("   GRANT ALL PRIVILEGES ON *.* TO 'root'@'%';");
            System.err.println("   FLUSH PRIVILEGES;");

        } else if (message.contains("communications link failure")) {
            System.err.println("\n🔍 诊断: 网络连接问题");
            System.err.println("   可能原因:");
            System.err.println("   1. MySQL服务未运行");
            System.err.println("   2. 防火墙阻止了3306端口");
            System.err.println("   3. 网络不可达");
            System.err.println("\n💡 解决方案:");
            System.err.println("   1. 检查MySQL服务: sudo systemctl status mysql");
            System.err.println("   2. 检查防火墙: sudo ufw status");
            System.err.println("   3. 测试网络: ping 10.100.164.39");
            System.err.println("   4. 测试端口: telnet 10.100.164.39 3306");

        } else if (message.contains("unknown database")) {
            System.err.println("\n🔍 诊断: 数据库不存在");
            System.err.println("   可能原因: shopping_web数据库不存在");
            System.err.println("\n💡 解决方案:");
            System.err.println("   在MySQL中创建数据库:");
            System.err.println("   CREATE DATABASE shopping_web;");

        } else if (message.contains("public key retrieval")) {
            System.err.println("\n🔍 诊断: MySQL 8.0公钥检索问题");
            System.err.println("\n💡 解决方案:");
            System.err.println("   在连接URL中添加: allowPublicKeyRetrieval=true");
            System.err.println("   jdbc:mysql://10.100.164.39:3306/shopping_web?allowPublicKeyRetrieval=true");
        }
    }

    public static Connection getConnection() throws SQLException {
        if (!initialized) {
            throw new SQLException("数据库未初始化: " + errorMessage);
        }
        return DriverManager.getConnection(url, username, password);
    }

    public static Connection getConnectionSafe() {
        if (!initialized) {
            return null;
        }
        try {
            return getConnection();
        } catch (SQLException e) {
            return null;
        }
    }

    public static boolean isDatabaseAvailable() {
        return initialized;
    }

    public static String getErrorMessage() {
        return errorMessage;
    }

    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            // 静默关闭
        }
    }

    public static void close(Connection conn, Statement stmt) {
        close(conn, stmt, null);
    }
}