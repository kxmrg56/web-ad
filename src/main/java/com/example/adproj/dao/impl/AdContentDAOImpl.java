package com.example.adproj.dao.impl;

import com.example.adproj.dao.AdContentDAO;
import com.example.adproj.entity.AdContent;
import com.example.adproj.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdContentDAOImpl implements AdContentDAO {

    @Override
    public AdContent getRandomAd() {
        // 使用 ad_material 表，映射 target_url
        String sql = "SELECT id, title, image_url, target_url AS link_url, category FROM ad_material WHERE status = 1 ORDER BY RAND() LIMIT 1";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return mapResultSetToEntity(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public AdContent getAdByCategory(String category) {
        // 使用 ad_material 表，根据中文分类查询
        String sql = "SELECT id, title, image_url, target_url AS link_url, category FROM ad_material WHERE category = ? AND status = 1 ORDER BY RAND() LIMIT 1";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapResultSetToEntity(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public List<AdContent> getAdsByOwner(int ownerId) {
        List<AdContent> ads = new ArrayList<>();
        // 注意：如果 ad_material 没有 owner_id 字段，此方法仅作兼容，暂不建议调用
        String sql = "SELECT id, title, image_url, target_url AS link_url, category FROM ad_material";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ads.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return ads;
    }

    private AdContent mapResultSetToEntity(ResultSet rs) throws SQLException {
        AdContent ad = new AdContent();
        ad.setId(rs.getInt("id"));
        ad.setTitle(rs.getString("title"));

        String rawPath = rs.getString("image_url");

        if (rawPath != null) {
            String cleanPath = rawPath;

            // 处理上传的文件路径
            if (cleanPath.startsWith("images/") || cleanPath.startsWith("videos/")) {
                // 上传的文件路径，保持原样
            }
            // 如果是旧的路径（包含 /adproj-1.0-SNAPSHOT/）
            else if (cleanPath.contains("/adproj-1.0-SNAPSHOT/")) {
                cleanPath = cleanPath.replace("/adproj-1.0-SNAPSHOT/", "");
            }

            // 去掉开头的斜杠
            if (cleanPath.startsWith("/")) {
                cleanPath = cleanPath.substring(1);
            }

            ad.setImageUrl(cleanPath);
        }

        ad.setLinkUrl(rs.getString("link_url"));
        ad.setCategory(rs.getString("category"));

        try {
            ad.setViewCount(rs.getLong("view_count"));
        } catch (SQLException e) {
            // 兼容旧表结构
            ad.setViewCount(0L);
        }


        return ad;
    }
}