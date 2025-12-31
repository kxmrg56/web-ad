package com.example.adproj.service;

import com.example.adproj.dao.AdUserDAO;
import com.example.adproj.dao.impl.AdUserDAOImpl;
import com.example.adproj.util.DBUtil; // 确保导入了DBUtil
import java.sql.Connection;           // 必须导入这个，否则报错
import java.sql.PreparedStatement;    // 必须导入这个
import java.sql.SQLException;         // 必须导入这个

public class AdUserService {

    private final AdUserDAO adUserDAO = new AdUserDAOImpl();

    /**
     * 处理访客追踪（存入 ad_user 表）
     */
    public void handleVisitorTracking(String visitorId) {
        if (visitorId == null || visitorId.isEmpty()) {
            return;
        }
        if (!adUserDAO.existsByVisitorId(visitorId)) {
            adUserDAO.insert(visitorId);
        } else {
            adUserDAO.updateLastVisit(visitorId);
        }
    }

    /**
     * 记录用户兴趣（存入 ad_user_interest 表）
     */
    public void recordUserInterest(String visitorId, String category) {
        // 使用 ON DUPLICATE KEY UPDATE 语法
        String sql = "INSERT INTO ad_user_interest (visitor_id, category, score) " +
                "VALUES (?, ?, 1) " +
                "ON DUPLICATE KEY UPDATE score = score + 1";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, visitorId);
            ps.setString(2, category);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}