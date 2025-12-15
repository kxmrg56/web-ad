package com.example.adproj.servlet;

import com.example.adproj.util.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

@WebServlet("/ads/api/getAd")
public class AdApiServlet extends HttpServlet {

    private static final String VISITOR_COOKIE_NAME = "visitor_id";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1️⃣ 获取或生成 visitorId（来自 Cookie）
        String visitorId = getOrCreateVisitorId(request, response);

        // 2️⃣ 核心逻辑：每次访问都同步数据库
        handleVisitor(visitorId);

        // 3️⃣ 返回广告数据（当前写死）
        response.setContentType("application/json;charset=UTF-8");

        String json = "{"
                + "\"visitorId\":\"" + visitorId + "\","
                + "\"adId\":1,"
                + "\"title\":\"Digital Product Sale\","
                + "\"image\":\"http://example.com/ad.jpg\","
                + "\"link\":\"http://example.com/product\""
                + "}";

        response.getWriter().write(json);
    }

    /**
     * 从 Cookie 中获取 visitorId
     * 如果没有则生成新的并写入 Cookie
     */
    private String getOrCreateVisitorId(HttpServletRequest request,
                                        HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (VISITOR_COOKIE_NAME.equals(c.getName())) {
                    return c.getValue();
                }
            }
        }

        // 没有 cookie，生成新的 visitorId
        String visitorId = UUID.randomUUID().toString();

        Cookie cookie = new Cookie(VISITOR_COOKIE_NAME, visitorId);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 30); // 30 天
        response.addCookie(cookie);

        return visitorId;
    }

    /**
     * ✅ 正确且完整的访客处理逻辑
     * - 第一次访问：insert（create_time + last_visit_time）
     * - 再次访问：update last_visit_time
     */
    private void handleVisitor(String visitorId) {

        String checkSql = "SELECT id FROM ad_user WHERE user_uuid = ?";
        String insertSql =
                "INSERT INTO ad_user (user_uuid, create_time, last_visit_time) " +
                        "VALUES (?, NOW(), NOW())";
        String updateSql =
                "UPDATE ad_user SET last_visit_time = NOW() WHERE user_uuid = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement checkPs = conn.prepareStatement(checkSql)) {

            checkPs.setString(1, visitorId);
            ResultSet rs = checkPs.executeQuery();

            if (rs.next()) {
                // 已存在 → 更新最后访问时间
                try (PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
                    updatePs.setString(1, visitorId);
                    updatePs.executeUpdate();
                }
            } else {
                // 不存在 → 插入新用户
                try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
                    insertPs.setString(1, visitorId);
                    insertPs.executeUpdate();
                }
            }

        } catch (Exception e) {
            e.printStackTrace(); // 出错直接看 catalina.out
        }
    }
}
