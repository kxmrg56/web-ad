package com.example.shoppingweb;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import com.google.gson.Gson;

@WebServlet("/api/cart/*")
public class CartApiServlet extends HttpServlet {
    private Gson gson = new Gson();

    // 内部类 - 用于结算
    static class CartItemForCheckout {
        int productId;
        int quantity;
        String productName;

        // 构造函数
        CartItemForCheckout(int productId, int quantity, String productName) {
            this.productId = productId;
            this.quantity = quantity;
            this.productName = productName;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");

        System.out.println("\n=== 购物车GET请求开始 ===");
        System.out.println("时间: " + new java.util.Date());

        try {
            String sessionId = getOrCreateSessionId(request, response);
            System.out.println("使用的Session ID: " + sessionId);

            List<Map<String, Object>> cartItems = getCartItems(sessionId);
            System.out.println("获取到购物车商品数量: " + cartItems.size());

            Map<String, Object> result = new HashMap<>();
            result.put("items", cartItems);
            result.put("totalItems", calculateTotalItems(cartItems));
            result.put("totalPrice", calculateTotalPrice(cartItems));

            System.out.println("返回结果: " + gson.toJson(result));
            response.getWriter().write(gson.toJson(result));

        } catch (SQLException e) {
            System.err.println("SQL异常: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(Map.of("error", "数据库错误: " + e.getMessage())));
        } catch (Exception e) {
            System.err.println("其他异常: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(Map.of("error", "服务器错误: " + e.getMessage())));
        }

        System.out.println("=== 购物车GET请求结束 ===\n");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");

        System.out.println("\n=== 添加到购物车POST请求开始 ===");

        try {
            String sessionId = getOrCreateSessionId(request, response);
            System.out.println("购物车会话ID: " + sessionId);

            BufferedReader reader = request.getReader();
            String body = reader.lines().collect(Collectors.joining());
            System.out.println("请求体: " + body);

            Map<String, Object> data = gson.fromJson(body, Map.class);
            System.out.println("解析后的数据: " + data);

            // 安全地获取整数
            int productId = 0;
            int quantity = 1;

            Object productIdObj = data.get("productId");
            Object quantityObj = data.get("quantity");

            if (productIdObj instanceof Double) {
                productId = ((Double) productIdObj).intValue();
            } else if (productIdObj instanceof Integer) {
                productId = (Integer) productIdObj;
            } else if (productIdObj instanceof String) {
                productId = Integer.parseInt((String) productIdObj);
            } else if (productIdObj != null) {
                productId = Integer.parseInt(productIdObj.toString());
            }

            if (quantityObj instanceof Double) {
                quantity = ((Double) quantityObj).intValue();
            } else if (quantityObj instanceof Integer) {
                quantity = (Integer) quantityObj;
            } else if (quantityObj instanceof String) {
                quantity = Integer.parseInt((String) quantityObj);
            } else if (quantityObj != null) {
                quantity = Integer.parseInt(quantityObj.toString());
            }

            System.out.println("添加商品到购物车 - 商品ID: " + productId + ", 数量: " + quantity);

            addToCart(sessionId, productId, quantity);

            response.getWriter().write(gson.toJson(Map.of("success", true, "message", "已添加到购物车")));

        } catch (Exception e) {
            System.err.println("添加到购物车异常: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(Map.of("error", e.getMessage())));
        }

        System.out.println("=== 添加到购物车POST请求结束 ===\n");
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");

        try {
            String sessionId = getOrCreateSessionId(request, response);
            String[] pathParts = request.getPathInfo().split("/");

            if (pathParts.length > 1) {
                int productId = Integer.parseInt(pathParts[1]);
                removeFromCart(sessionId, productId);
                response.getWriter().write(gson.toJson(Map.of("success", true, "message", "已从购物车移除")));
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(Map.of("error", e.getMessage())));
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");

        try {
            String sessionId = getOrCreateSessionId(request, response);
            String[] pathParts = request.getPathInfo().split("/");

            if (pathParts.length > 1) {
                int productId = Integer.parseInt(pathParts[1]);
                BufferedReader reader = request.getReader();
                Map<String, Object> data = gson.fromJson(reader, Map.class);

                int quantity = 1;
                Object quantityObj = data.get("quantity");
                if (quantityObj instanceof Double) {
                    quantity = ((Double) quantityObj).intValue();
                } else if (quantityObj instanceof Integer) {
                    quantity = (Integer) quantityObj;
                }

                updateCartItem(sessionId, productId, quantity);
                response.getWriter().write(gson.toJson(Map.of("success", true, "message", "购物车已更新")));
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(Map.of("error", e.getMessage())));
        }
    }

    // 处理结算请求
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String method = request.getMethod();
        String pathInfo = request.getPathInfo();

        // 检查是否是结算请求
        if ("POST".equals(method) && pathInfo != null && pathInfo.equals("/checkout")) {
            doCheckout(request, response);
        } else {
            super.service(request, response);
        }
    }

    // 结算方法
    protected void doCheckout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");

        System.out.println("\n=== 开始结算 ===");

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String sessionId = getOrCreateSessionId(request, response);
            System.out.println("结算会话ID: " + sessionId);

            conn = DBUtil.getConnection();
            conn.setAutoCommit(false); // 开始事务

            // 1. 获取购物车中所有商品
            List<CartItemForCheckout> cartItems = getCartItemsForCheckout(conn, sessionId);

            if (cartItems.isEmpty()) {
                throw new Exception("购物车为空，无法结算");
            }

            System.out.println("购物车商品数量: " + cartItems.size());

            // 2. 验证库存并减少库存
            for (CartItemForCheckout item : cartItems) {
                // 检查库存
                String checkStockSql = "SELECT stock, name FROM products WHERE id = ?";
                stmt = conn.prepareStatement(checkStockSql);
                stmt.setInt(1, item.productId);
                rs = stmt.executeQuery();

                if (rs.next()) {
                    int stock = rs.getInt("stock");
                    String productName = rs.getString("name");
                    item.productName = productName;

                    if (stock < item.quantity) {
                        throw new Exception("商品 \"" + productName + "\" 库存不足。库存: " + stock + ", 需要: " + item.quantity);
                    }
                    System.out.println("商品 " + productName + " 库存充足: " + stock);
                } else {
                    throw new Exception("商品不存在: " + item.productId);
                }
                rs.close();
                stmt.close();

                // 减少库存
                String updateStockSql = "UPDATE products SET stock = stock - ? WHERE id = ?";
                stmt = conn.prepareStatement(updateStockSql);
                stmt.setInt(1, item.quantity);
                stmt.setInt(2, item.productId);
                int updated = stmt.executeUpdate();

                if (updated == 1) {
                    System.out.println("成功减少商品 " + item.productId + " 库存 " + item.quantity + " 个");
                } else {
                    throw new Exception("更新商品库存失败: " + item.productId);
                }
                stmt.close();
            }

            // 3. 清空购物车（只清空商品）
            clearCartItems(conn, sessionId);

            // 4. 提交事务
            conn.commit();
            System.out.println("结算成功，事务已提交");

            // 返回成功响应
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "结算成功！库存已更新，购物车已清空");
            result.put("itemsCount", cartItems.size());

            response.getWriter().write(gson.toJson(result));

        } catch (Exception e) {
            System.err.println("结算失败: " + e.getMessage());

            // 回滚事务
            try {
                if (conn != null) {
                    conn.rollback();
                    System.out.println("事务已回滚");
                }
            } catch (SQLException rollbackEx) {
                System.err.println("回滚失败: " + rollbackEx.getMessage());
            }

            // 返回错误响应
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(Map.of(
                    "success", false,
                    "error", e.getMessage()
            )));
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("关闭资源失败: " + e.getMessage());
            }
            System.out.println("=== 结算结束 ===\n");
        }
    }

    // 获取或创建会话ID - 简化版本
    private String getOrCreateSessionId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("=== getOrCreateSessionId 开始 ===");

        // 1. 首先尝试从cookie获取
        String sessionId = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println("Cookie: " + cookie.getName() + " = " + cookie.getValue());
                if ("CART_SESSION_ID".equals(cookie.getName())) {
                    sessionId = cookie.getValue();
                    System.out.println("从Cookie找到购物车会话ID: " + sessionId);
                    break;
                }
            }
        }

        // 2. 如果没有cookie，创建新的会话ID
        if (sessionId == null || sessionId.trim().isEmpty()) {
            // 生成唯一ID
            sessionId = UUID.randomUUID().toString();
            System.out.println("生成新的购物车会话ID: " + sessionId);

            // 设置cookie
            Cookie cartCookie = new Cookie("CART_SESSION_ID", sessionId);
            cartCookie.setMaxAge(30 * 24 * 60 * 60); // 30天
            cartCookie.setPath("/");
            cartCookie.setHttpOnly(false); // 允许JavaScript访问
            response.addCookie(cartCookie);
            System.out.println("已设置购物车Cookie");
        }

        System.out.println("最终使用的会话ID: " + sessionId);
        System.out.println("=== getOrCreateSessionId 结束 ===");
        return sessionId;
    }

    // 获取购物车商品
    private List<Map<String, Object>> getCartItems(String sessionId) throws SQLException {
        System.out.println("\n=== getCartItems 开始 ===");
        System.out.println("查询购物车，sessionId: " + sessionId);

        List<Map<String, Object>> items = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            System.out.println("数据库连接成功");

            // 方法1：直接查询，不依赖carts表
            String directSql = "SELECT ci.id, ci.product_id, ci.quantity, " +
                    "p.name, p.price, p.image_url " +
                    "FROM cart_items ci " +
                    "INNER JOIN products p ON ci.product_id = p.id " +
                    "INNER JOIN carts c ON ci.cart_id = c.id " +
                    "WHERE c.session_id = ?";

            System.out.println("执行SQL: " + directSql);
            stmt = conn.prepareStatement(directSql);
            stmt.setString(1, sessionId);
            rs = stmt.executeQuery();

            int count = 0;
            while (rs.next()) {
                count++;
                Map<String, Object> item = new HashMap<>();
                item.put("id", rs.getInt("id"));
                item.put("productId", rs.getInt("product_id"));
                item.put("quantity", rs.getInt("quantity"));
                item.put("name", rs.getString("name"));
                item.put("price", rs.getDouble("price"));
                item.put("imageUrl", rs.getString("image_url"));
                item.put("itemTotal", rs.getDouble("price") * rs.getInt("quantity"));
                items.add(item);

                System.out.println("找到商品 " + count + ": " + item);
            }

            System.out.println("总共找到 " + count + " 个商品");

            return items;

        } catch (SQLException e) {
            System.err.println("查询购物车失败: " + e.getMessage());
            throw e;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("关闭资源失败: " + e.getMessage());
            }
            System.out.println("=== getCartItems 结束 ===");
        }
    }

    // 添加到购物车
    private void addToCart(String sessionId, int productId, int quantity) throws SQLException {
        System.out.println("\n=== addToCart 开始 ===");
        System.out.println("参数: sessionId=" + sessionId + ", productId=" + productId + ", quantity=" + quantity);

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBUtil.getConnection();
            System.out.println("数据库连接成功");

            // 1. 检查商品是否存在
            String checkProductSql = "SELECT name, price, stock FROM products WHERE id = ?";
            stmt = conn.prepareStatement(checkProductSql);
            stmt.setInt(1, productId);
            ResultSet productRs = stmt.executeQuery();

            if (!productRs.next()) {
                System.out.println("错误: 商品ID " + productId + " 不存在!");
                throw new SQLException("商品不存在: " + productId);
            }

            String productName = productRs.getString("name");
            double productPrice = productRs.getDouble("price");
            int stock = productRs.getInt("stock");
            System.out.println("商品信息 - 名称: " + productName + ", 价格: " + productPrice + ", 库存: " + stock);

            // 检查库存
            if (stock < quantity) {
                throw new SQLException("商品库存不足。库存: " + stock + ", 需要: " + quantity);
            }

            productRs.close();
            stmt.close();

            // 2. 获取或创建购物车
            System.out.println("开始获取或创建购物车...");
            int cartId = getOrCreateCart(conn, sessionId);
            System.out.println("购物车ID: " + cartId);

            // 3. 检查是否已存在
            String checkSql = "SELECT id, quantity FROM cart_items WHERE cart_id = ? AND product_id = ?";
            stmt = conn.prepareStatement(checkSql);
            stmt.setInt(1, cartId);
            stmt.setInt(2, productId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int itemId = rs.getInt("id");
                int existingQty = rs.getInt("quantity");
                System.out.println("商品已在购物车，itemId: " + itemId + ", 原数量: " + existingQty);

                // 检查总库存
                if (stock < existingQty + quantity) {
                    throw new SQLException("商品库存不足。当前数量: " + existingQty + ", 新增: " + quantity + ", 总需: " + (existingQty + quantity) + ", 库存: " + stock);
                }

                String updateSql = "UPDATE cart_items SET quantity = ? WHERE id = ?";
                stmt = conn.prepareStatement(updateSql);
                stmt.setInt(1, existingQty + quantity);
                stmt.setInt(2, itemId);
                int rows = stmt.executeUpdate();
                System.out.println("更新成功，影响行数: " + rows);
            } else {
                System.out.println("商品不在购物车，准备新增...");
                String insertSql = "INSERT INTO cart_items (cart_id, product_id, quantity) VALUES (?, ?, ?)";
                stmt = conn.prepareStatement(insertSql);
                stmt.setInt(1, cartId);
                stmt.setInt(2, productId);
                stmt.setInt(3, quantity);
                int rows = stmt.executeUpdate();
                System.out.println("插入成功，影响行数: " + rows);
            }

            System.out.println("=== addToCart 成功结束 ===\n");

        } catch (SQLException e) {
            System.err.println("addToCart 失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("关闭连接失败: " + e.getMessage());
            }
        }
    }

    // 获取或创建购物车
    private int getOrCreateCart(Connection conn, String sessionId) throws SQLException {
        System.out.println("getOrCreateCart: sessionId=" + sessionId);

        String sql = "SELECT id FROM carts WHERE session_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, sessionId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            int cartId = rs.getInt("id");
            System.out.println("找到现有购物车，ID: " + cartId);
            rs.close();
            stmt.close();
            return cartId;
        } else {
            System.out.println("没有找到购物车，创建新的...");
            rs.close();
            stmt.close();

            String insertSql = "INSERT INTO carts (session_id) VALUES (?)";
            stmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, sessionId);
            int affectedRows = stmt.executeUpdate();
            System.out.println("插入购物车，影响行数: " + affectedRows);

            if (affectedRows == 0) {
                throw new SQLException("创建购物车失败，没有行被影响");
            }

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int newCartId = generatedKeys.getInt(1);
                System.out.println("新购物车创建成功，ID: " + newCartId);
                return newCartId;
            } else {
                throw new SQLException("创建购物车失败，无法获取ID");
            }
        }
    }

    // 计算总商品数
    private int calculateTotalItems(List<Map<String, Object>> cartItems) {
        return cartItems.stream().mapToInt(item -> (int) item.get("quantity")).sum();
    }

    // 计算总价格
    private double calculateTotalPrice(List<Map<String, Object>> cartItems) {
        return cartItems.stream().mapToDouble(item ->
                (double) item.get("price") * (int) item.get("quantity")).sum();
    }

    // 从购物车移除商品
    private void removeFromCart(String sessionId, int productId) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE ci FROM cart_items ci " +
                    "INNER JOIN carts c ON ci.cart_id = c.id " +
                    "WHERE c.session_id = ? AND ci.product_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, sessionId);
            stmt.setInt(2, productId);
            int rows = stmt.executeUpdate();
            System.out.println("删除商品，影响行数: " + rows);
        } finally {
            DBUtil.close(conn, stmt, null);
        }
    }

    // 更新购物车商品数量
    private void updateCartItem(String sessionId, int productId, int quantity) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBUtil.getConnection();

            // 先检查库存
            String checkStockSql = "SELECT stock FROM products WHERE id = ?";
            stmt = conn.prepareStatement(checkStockSql);
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int stock = rs.getInt("stock");
                if (stock < quantity) {
                    throw new SQLException("库存不足。库存: " + stock + ", 需要: " + quantity);
                }
            }
            rs.close();
            stmt.close();

            String sql = "UPDATE cart_items ci " +
                    "INNER JOIN carts c ON ci.cart_id = c.id " +
                    "SET ci.quantity = ? " +
                    "WHERE c.session_id = ? AND ci.product_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, quantity);
            stmt.setString(2, sessionId);
            stmt.setInt(3, productId);
            int rows = stmt.executeUpdate();
            System.out.println("更新商品数量，影响行数: " + rows);
        } finally {
            DBUtil.close(conn, stmt, null);
        }
    }

    // 获取购物车商品用于结算
    private List<CartItemForCheckout> getCartItemsForCheckout(Connection conn, String sessionId) throws SQLException {
        List<CartItemForCheckout> items = new ArrayList<>();

        String sql = "SELECT ci.product_id, ci.quantity, p.name as product_name " +
                "FROM cart_items ci " +
                "INNER JOIN products p ON ci.product_id = p.id " +
                "INNER JOIN carts c ON ci.cart_id = c.id " +
                "WHERE c.session_id = ?";

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, sessionId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int productId = rs.getInt("product_id");
                int quantity = rs.getInt("quantity");
                String productName = rs.getString("product_name");

                CartItemForCheckout item = new CartItemForCheckout(productId, quantity, productName);
                items.add(item);
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }

        return items;
    }

    // 清空购物车商品
    private void clearCartItems(Connection conn, String sessionId) throws SQLException {
        String sql = "DELETE ci FROM cart_items ci " +
                "INNER JOIN carts c ON ci.cart_id = c.id " +
                "WHERE c.session_id = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, sessionId);
        int itemsDeleted = stmt.executeUpdate();
        System.out.println("清空购物车: 删除 " + itemsDeleted + " 件商品");
        stmt.close();
    }
}