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
     * @param siteType 网站类型 (news/shop/video)
     * @param visitorId 访客ID
     * @param finalCategory 当前页面识别出的分类
     */
    public AdContent getRecommendedAd(String siteType, String visitorId, String finalCategory) {
        // 调试日志：确认是谁在请求，站点类型是什么
        System.out.println("[推荐引擎] 正在为访客 [" + visitorId + "] 计算推荐，站点类型: " + siteType + "，当前频道: " + finalCategory);

        // --- 策略 A: 历史画像优先 ---
        String topCategory = getTopInterestCategory(visitorId);

        if (topCategory != null) {
            // 原逻辑：如果历史兴趣正好就是当前频道，或者满足随机概率
            if (topCategory.equals(finalCategory) || Math.random() < 0.8) {
                // 修改点：加入 siteType 过滤素材类型
                AdContent ad = getAdByCategoryAndType(topCategory, siteType);
                if (ad != null) {
                    System.out.println("[推荐引擎] 优先推送历史偏好: " + topCategory + " (素材匹配: " + siteType + ")");
                    return ad;
                }
            }
        }

        // --- 策略 B: 页面内容匹配 ---
        if (finalCategory != null && !finalCategory.isEmpty()) {
            // 修改点：加入 siteType 过滤素材类型
            AdContent ad = getAdByCategoryAndType(finalCategory, siteType);
            if (ad != null) {
                System.out.println("[推荐引擎] 匹配当前页面内容: " + finalCategory + " (素材匹配: " + siteType + ")");
                return ad;
            }
        }

        // --- 策略 C: 全局兜底 ---
        String fallback = ("shop".equals(siteType)) ? "家用电器" : "军事";
        System.out.println("[推荐引擎] 触发兜底策略 -> " + fallback);
        return getAdByCategoryAndType(fallback, siteType);
    }

    /**
     * 核心扩展方法：根据分类和站点类型精准获取素材 (图片或视频)
     * 逻辑：video 站点只拿 .mp4，其他站点只拿 .jpg
     */
    private AdContent getAdByCategoryAndType(String category, String siteType) {
        // 1. 预处理参数：防止 null 导致的匹配失败，并强制转为小写对比
        String type = (siteType == null) ? "news" : siteType.trim().toLowerCase();
        String suffixCondition = type.equals("video") ? "%.mp4" : "%.jpg";

        // 调试日志：在后台打印出来，看看程序到底在搜什么
        System.out.println("[查询日志] 准备搜索 - 分类: " + category + " | 匹配后缀: " + suffixCondition);

        // 2. 编写 SQL（直接使用你查询成功的字段名）
        String sql = "SELECT id, title, image_url, target_url AS link_url, category FROM ad_material " +
                "WHERE category = ? AND image_url LIKE ? ORDER BY RAND() LIMIT 1";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category);
            ps.setString(2, suffixCondition);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    AdContent ad = new AdContent();
                    ad.setId(rs.getInt("id"));
                    ad.setTitle(rs.getString("title"));
                    ad.setImageUrl(rs.getString("image_url"));
                    ad.setLinkUrl(rs.getString("link_url"));
                    ad.setCategory(rs.getString("category"));
                    return ad;
                } else {
                    // 如果没搜到，打印警告
                    System.out.println("[查询警告] 数据库中未找到符合条件的素材: " + category + " [" + suffixCondition + "]");
                }
            }
        } catch (SQLException e) {
            System.err.println("[查询错误] SQL执行异常: " + e.getMessage());
        }

        // 3. 只有上面彻底没招了，才返回 DAO 默认值（原来的兜底）
        return adContentDAO.getAdByCategory(category);
    }

    /**
     * 获取该访客在数据库中分数最高的分类
     */
    public String getTopInterestCategory(String visitorId) {
        String sql = "SELECT category FROM ad_user_interest WHERE visitor_id = ? ORDER BY score DESC LIMIT 1";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, visitorId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String topCat = rs.getString("category");
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

    // ━━━━━━━━━━━━━━━━━━━━ 修复管理后台所需的方法 (保留原样) ━━━━━━━━━━━━━━━━━━━━

    public List<AdContent> getAdsByOwner(Integer ownerId) {
        List<AdContent> list = new ArrayList<>();
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
        System.out.println("AD ID: " + adId + " displayed.");
    }
}