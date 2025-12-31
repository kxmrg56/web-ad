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

        String rawPath = rs.getString("image_url"); // 数据库里存的是 /adproj-1.0-SNAPSHOT/images/xxx.jpg

        if (rawPath != null) {
            // 【核心修改】去掉数据库路径中重复的 contextPath 部分
            // 把 "/adproj-1.0-SNAPSHOT/images/..." 变成 "images/..."
            String cleanPath = rawPath.replace("/adproj-1.0-SNAPSHOT/", "");
            // 还要去掉开头的斜杠，因为新闻站 JS 已经拼了一个 "/"
            if (cleanPath.startsWith("/")) {
                cleanPath = cleanPath.substring(1);
            }
            ad.setImageUrl(cleanPath);
        }

        ad.setLinkUrl(rs.getString("link_url"));
        ad.setCategory(rs.getString("category"));
        return ad;
    }
}