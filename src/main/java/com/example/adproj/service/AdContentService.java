package com.example.adproj.service;

import com.example.adproj.dao.AdContentDAO;
import com.example.adproj.dao.impl.AdContentDAOImpl;
import com.example.adproj.entity.AdContent;
import com.example.adproj.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdContentService {
    private final AdContentDAO adContentDAO = new AdContentDAOImpl();

    /**
     * 精准推荐逻辑
     */
    /**
     * 核心推荐算法：个性化画像优先 + 页面内容匹配
     * @param siteType 网站类型 (news/shop)
     * @param visitorId 访客ID (从Cookie获取)
     * @param finalCategory 当前页面识别出的分类 (如军事、金融)
     */
    public AdContent getRecommendedAd(String siteType, String visitorId, String finalCategory) {
        // 调试日志：确认是谁在请求，当前在看什么
        System.out.println("[推荐引擎] 正在为访客 [" + visitorId + "] 计算推荐，当前频道: " + finalCategory);

        // --- 策略 A: 历史画像优先 ---
        String topCategory = getTopInterestCategory(visitorId);

        // 增加逻辑：如果历史兴趣正好就是当前频道，就不用算概率了，直接出就行
        if (topCategory != null) {
            // 测试阶段建议设为 0.9，增强感知
            if (topCategory.equals(finalCategory) || Math.random() < 0.8) {
                AdContent ad = adContentDAO.getAdByCategory(topCategory);
                if (ad != null) {
                    System.out.println("[推荐引擎] 优先推送历史偏好: " + topCategory);
                    return ad;
                }
            }
        }

        // --- 策略 B: 页面内容匹配 ---
        if (finalCategory != null && !finalCategory.isEmpty()) {
            AdContent ad = adContentDAO.getAdByCategory(finalCategory);
            if (ad != null) {
                System.out.println("[推荐引擎] 匹配当前页面内容: " + finalCategory);
                return ad;
            }
        }

        // --- 策略 C: 全局兜底 ---
        String fallback = ("shop".equals(siteType)) ? "家用电器" : "军事";
        System.out.println("[推荐引擎] 触发兜底策略 -> " + fallback);
        return adContentDAO.getAdByCategory(fallback);
    }

    /**
     * 获取用户画像分类
     */
    /**
     * 获取该访客在数据库中分数最高的分类
     */
    public String getTopInterestCategory(String visitorId) {
        // 这里的表名建议核对，如果是 ad_user_interest 则保持不变
        // 逻辑：按分数从高到低排序，只取第一条
        String sql = "SELECT category FROM ad_user_interest WHERE visitor_id = ? ORDER BY score DESC LIMIT 1";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, visitorId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String topCat = rs.getString("category");
                    // 排除一些无意义的空值
                    if (topCat != null && !topCat.isEmpty()) {
                        return topCat;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[推荐引擎] 获取画像失败: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // ━━━━━━━━━━━━━━━━━━━━ 修复管理后台所需的方法 ━━━━━━━━━━━━━━━━━━━━

    /**
     * 获取指定业主的广告列表 (修复 AdManageServlet 报错)
     */
    public List<AdContent> getAdsByOwner(Integer ownerId) {
        List<AdContent> list = new ArrayList<>();
        // 注意：统一使用 ad_material 表，若无 owner_id 字段此处逻辑需根据实际业务调整
        // 这里暂时通过全表查询来保证编译通过，后续若需按业主管理，需在 ad_material 增加 owner_id 字段
        String sql = "SELECT id, title, image_url, target_url AS link_url, category FROM ad_material";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AdContent ad = new AdContent();
                    ad.setId(rs.getInt("id"));
                    ad.setTitle(rs.getString("title"));
                    ad.setImageUrl(rs.getString("image_url"));
                    ad.setLinkUrl(rs.getString("link_url"));
                    ad.setCategory(rs.getString("category"));
                    list.add(ad);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    /**
     * 发布新广告 (修复 AdManageServlet 报错)
     */
    public boolean addAd(String title, String imageUrl, String linkUrl, String category, Integer ownerId) {
        String sql = "INSERT INTO ad_material (title, image_url, target_url, category, status) VALUES (?, ?, ?, ?, 1)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setString(2, imageUrl);
            ps.setString(3, linkUrl);
            ps.setString(4, category);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新广告 (修复 AdManageServlet 报错)
     */
    public boolean updateAd(int adId, String title, String imageUrl, String linkUrl, String category, Integer ownerId) {
        String sql = "UPDATE ad_material SET title = ?, image_url = ?, target_url = ?, category = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setString(2, imageUrl);
            ps.setString(3, linkUrl);
            ps.setString(4, category);
            ps.setInt(5, adId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void incrementViewCount(int adId) {
        // 此处可增加 ad_material 的展现量更新逻辑
        System.out.println("AD ID: " + adId + " displayed.");
    }
}