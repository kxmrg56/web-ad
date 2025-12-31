package com.example.adproj.dao.impl;

import com.example.adproj.dao.AdUserDAO;
import com.example.adproj.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdUserDAOImpl implements AdUserDAO {

    @Override
    public boolean existsByVisitorId(String visitorId) {
        String sql = "SELECT id FROM ad_user WHERE user_uuid = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, visitorId);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int insert(String visitorId) {
        String sql = "INSERT INTO ad_user (user_uuid, create_time, last_visit_time) " +
                "VALUES (?, NOW(), NOW())";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, visitorId);
            return ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int updateLastVisit(String visitorId) {
        String sql = "UPDATE ad_user SET last_visit_time = NOW() WHERE user_uuid = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, visitorId);
            return ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
