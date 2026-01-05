package com.example.shoppingweb;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;
import java.sql.*;
import java.util.*;

import com.google.gson.Gson;

@WebServlet("/api/products")
public class ProductApiServlet extends HttpServlet {
    private Gson gson = new Gson();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("📡 API调用: " + request.getQueryString());
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");

        String category = request.getParameter("category");

        try {
            List<Map<String, Object>> products;

            if (category != null && !category.trim().isEmpty()) {
                // 按分类查询
                products = getProductsByCategory(category);
            } else {
                // 查询所有商品
                products = getAllProducts();
            }

            System.out.println("✅ 从数据库返回 " + products.size() + " 个商品");
            response.getWriter().write(gson.toJson(products));

        } catch (SQLException e) {
            System.err.println("❌ 数据库查询失败: " + e.getMessage());
            // 返回空数组或错误信息
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, Object> error = new HashMap<>();
            error.put("error", "数据库查询失败");
            error.put("message", e.getMessage());
            response.getWriter().write(gson.toJson(error));
        }
    }

    /**
     * 从数据库获取所有商品
     */
    private List<Map<String, Object>> getAllProducts() throws SQLException {
        List<Map<String, Object>> products = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT id, name, description, price, image_url, stock, category " +
                    "FROM products ORDER BY id";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                products.add(createProductMapFromResultSet(rs));
            }

            return products;

        } finally {
            DBUtil.close(conn, stmt, rs);
        }
    }

    /**
     * 按分类获取商品
     */
    private List<Map<String, Object>> getProductsByCategory(String category) throws SQLException {
        List<Map<String, Object>> products = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT id, name, description, price, image_url, stock, category " +
                    "FROM products WHERE category = ? ORDER BY id";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, category);
            rs = stmt.executeQuery();

            while (rs.next()) {
                products.add(createProductMapFromResultSet(rs));
            }

            return products;

        } finally {
            DBUtil.close(conn, stmt, rs);
        }
    }

    /**
     * 从ResultSet创建商品Map
     */
    private Map<String, Object> createProductMapFromResultSet(ResultSet rs) throws SQLException {
        Map<String, Object> product = new HashMap<>();
        product.put("id", rs.getInt("id"));
        product.put("name", rs.getString("name"));
        product.put("description", rs.getString("description"));
        product.put("price", rs.getDouble("price"));
        product.put("stock", rs.getInt("stock"));
        product.put("category", rs.getString("category"));

        // 处理图片URL
        String imageUrl = rs.getString("image_url");
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            product.put("imageUrl", "https://picsum.photos/300/200?random=" + rs.getInt("id"));
        } else {
            product.put("imageUrl", imageUrl);
        }

        return product;
    }
}