package com.example.adproj.service;

import com.example.adproj.util.DBUtil;
import java.sql.*;

public class AdOwnerService {

    // 登录验证：成功返回 ownerId，失败返回 null
    public Integer login(String username, String password) {
        String sql = "SELECT id FROM ad_owners WHERE username = ? AND password = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // 注册业主
    public boolean register(String username, String password, String company) {
        String sql = "INSERT INTO ad_owners (username, password, company_name) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, company);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}