package com.example.shoppingweb;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;
import java.sql.*;
import java.util.*;

@WebServlet(
        name = "mainServlet",
        urlPatterns = {"/products", "/category/*", "/home"},
        loadOnStartup = 1  // 确保Servlet在启动时就初始化
)
public class Main extends HttpServlet {
    private List<Product> products;
    private boolean usingSimulatedData = false;

    @Override
    public void init() throws ServletException {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("🚀 Main Servlet 初始化开始");
        System.out.println("   时间: " + new java.util.Date());
        System.out.println("=".repeat(50));

        // 1. 检查DBUtil状态
        System.out.println("\n📊 数据库状态检查:");
        System.out.println("   DBUtil.isDatabaseAvailable(): " + DBUtil.isDatabaseAvailable());
        System.out.println("   DBUtil.getErrorMessage(): '" + DBUtil.getErrorMessage() + "'");

        // 2. 测试直接连接
        System.out.println("\n🔗 测试直接数据库连接:");
        testDirectConnection();

        // 3. 加载商品数据
        System.out.println("\n📦 开始加载商品数据...");

        if (DBUtil.isDatabaseAvailable()) {
            System.out.println("🛢️ 数据库可用，尝试从数据库加载商品");
            products = loadProductsFromDB();

            if (products != null) {
                System.out.println("✅ 成功从数据库加载 " + products.size() + " 个商品");
                usingSimulatedData = false;

                // 显示前5个商品信息
                if (products.size() > 0) {
                    System.out.println("\n📋 前5个商品信息:");
                    for (int i = 0; i < Math.min(5, products.size()); i++) {
                        Product p = products.get(i);
                        System.out.println(String.format("   %2d. %-30s ¥%,9.2f 库存:%3d",
                                i+1, p.getName(), p.getPrice(), p.getStock()));
                    }
                }
            } else {
                System.out.println("❌ 数据库加载失败，切换到模拟数据");
                products = getSimulatedProducts();
                usingSimulatedData = true;
            }
        } else {
            System.out.println("⚠ 数据库不可用，使用模拟数据");
            System.out.println("   错误信息: " + DBUtil.getErrorMessage());
            products = getSimulatedProducts();
            usingSimulatedData = true;
        }

        System.out.println("\n🎯 Main Servlet 初始化完成");
        System.out.println("   总商品数: " + products.size());
        System.out.println("   数据源: " + (usingSimulatedData ? "模拟数据" : "数据库"));
        System.out.println("=".repeat(50) + "\n");
    }

    /**
     * 测试直接数据库连接
     */
    private void testDirectConnection() {
        Connection conn = null;
        try {
            String url = "jdbc:mysql://10.100.164.39:3306/shopping_web?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8&allowPublicKeyRetrieval=true";
            String user = "root";
            String password = "Kxmrg5.5";

            conn = DriverManager.getConnection(url, user, password);
            System.out.println("   ✅ 直接连接成功");

            // 查询商品数量
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM products");
            if (rs.next()) {
                System.out.println("   数据库中的商品数量: " + rs.getInt("count"));
            }

            // 查询一个样本商品
            rs = stmt.executeQuery("SELECT name, price FROM products LIMIT 1");
            if (rs.next()) {
                System.out.println("   样本商品: " + rs.getString("name") + " - ¥" + rs.getDouble("price"));
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.err.println("   ❌ 直接连接失败: " + e.getMessage());
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) {}
            }
        }
    }

    /**
     * 从数据库加载所有商品
     */
    private List<Product> loadProductsFromDB() {
        List<Product> productList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            System.out.println("\n   🔍 执行数据库查询...");
            conn = DBUtil.getConnection();
            System.out.println("   ✅ 获取数据库连接成功");

            String sql = "SELECT id, name, description, price, image_url, stock FROM products ORDER BY id";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            System.out.println("   ✅ SQL查询执行成功");

            int count = 0;
            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getString("image_url"),
                        rs.getInt("stock")
                );
                productList.add(product);
                count++;

                // 每加载10个商品打印一个点
                if (count % 10 == 0) {
                    System.out.print(".");
                }
            }

            if (count > 0) {
                System.out.println();
            }
            System.out.println("   📥 从数据库读取了 " + count + " 条记录");

            return productList;

        } catch (SQLException e) {
            System.err.println("\n   ❌ 数据库查询失败: " + e.getMessage());
            System.err.println("   SQL状态: " + e.getSQLState());
            System.err.println("   错误码: " + e.getErrorCode());
            return null;

        } finally {
            DBUtil.close(conn, stmt, rs);
        }
    }

    /**
     * 获取模拟商品数据
     */
    private List<Product> getSimulatedProducts() {
        System.out.println("\n   🎭 生成模拟数据...");
        List<Product> simulated = new ArrayList<>();

        // 家用电器
        simulated.add(new Product(1, "索尼85英寸4K电视", "XR认知芯片，全阵列背光", 12999.00, "https://picsum.photos/300/200?random=1", 8));
        simulated.add(new Product(2, "海尔对开门冰箱", "变频风冷无霜，智能温控", 5999.00, "https://picsum.photos/300/200?random=2", 15));
        simulated.add(new Product(3, "美的变频空调", "新一级能效，智能控温", 3299.00, "https://picsum.photos/300/200?random=3", 20));
        simulated.add(new Product(4, "西门子滚筒洗衣机", "10公斤大容量，智能除菌", 4599.00, "https://picsum.photos/300/200?random=4", 12));
        simulated.add(new Product(5, "戴森无叶风扇", "空气净化，智能温控", 3999.00, "https://picsum.photos/300/200?random=5", 18));
        simulated.add(new Product(6, "小米扫地机器人", "LDS激光导航，智能路径规划", 1999.00, "https://picsum.photos/300/200?random=6", 25));

        // 数码产品
        simulated.add(new Product(7, "iPhone 14 Pro Max", "A16芯片，4800万像素", 8999.00, "https://picsum.photos/300/200?random=7", 50));
        simulated.add(new Product(8, "MacBook Pro 16寸", "M2 Pro芯片，32GB内存", 18999.00, "https://picsum.photos/300/200?random=8", 30));
        simulated.add(new Product(9, "AirPods Pro 2", "主动降噪，空间音频", 1899.00, "https://picsum.photos/300/200?random=9", 100));

        return simulated;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("\n📥 [" + new java.util.Date() + "] 收到请求: " +
                request.getServletPath() +
                (request.getPathInfo() != null ? request.getPathInfo() : ""));

        // 添加数据源信息到请求属性
        request.setAttribute("usingSimulatedData", usingSimulatedData);
        request.setAttribute("totalProducts", products.size());

        response.setContentType("text/html;charset=UTF-8");

        String path = request.getServletPath();

        if ("/products".equals(path)) {
            // 显示所有商品
            request.setAttribute("products", products);
            request.getRequestDispatcher("/product.html").forward(request, response);

        } else if (path.startsWith("/category/")) {
            // 分类页面
            String category = path.substring(10);
            List<Product> filtered = filterProductsByCategory(category);

            request.setAttribute("products", filtered);
            request.setAttribute("category", category);
            request.getRequestDispatcher("/category-products.html").forward(request, response);

        } else if ("/home".equals(path)) {
            // 首页
            response.sendRedirect("index.html");

        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private List<Product> filterProductsByCategory(String category) {
        System.out.println("   筛选分类: " + category);

        if (usingSimulatedData) {
            System.out.println("   使用模拟数据筛选");
            return filterSimulatedProducts(category);
        }

        // 如果是数据库数据，查询数据库
        System.out.println("   从数据库查询分类: " + category);
        return queryProductsByCategory(category);
    }

    private List<Product> filterSimulatedProducts(String category) {
        List<Product> filtered = new ArrayList<>();
        Map<String, Integer[]> categoryMap = new HashMap<>();
        categoryMap.put("electronics", new Integer[]{7, 8, 9});
        categoryMap.put("appliances", new Integer[]{1, 2, 3, 4, 5, 6});
        categoryMap.put("computer", new Integer[]{8});

        Integer[] ids = categoryMap.get(category);
        if (ids != null) {
            for (Product p : products) {
                for (Integer id : ids) {
                    if (p.getId() == id) {
                        filtered.add(p);
                        break;
                    }
                }
            }
        }

        System.out.println("   筛选结果: " + filtered.size() + " 个商品");
        return filtered.isEmpty() ? products : filtered;
    }

    private List<Product> queryProductsByCategory(String category) {
        List<Product> result = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT id, name, description, price, image_url, stock FROM products WHERE category = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, category);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getString("image_url"),
                        rs.getInt("stock")
                );
                result.add(product);
            }

            System.out.println("   数据库查询结果: " + result.size() + " 个商品");

        } catch (SQLException e) {
            System.err.println("   ❌ 分类查询失败: " + e.getMessage());
        } finally {
            DBUtil.close(conn, stmt, rs);
        }

        return result;
    }
}