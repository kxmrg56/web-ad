package com.example.adproj.servlet;

import com.example.adproj.entity.AdContent;
import com.example.adproj.service.AdContentService;
import com.example.adproj.service.AdUserService;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/ads/api/getAd")
public class AdApiServlet extends HttpServlet {
    private final AdUserService adUserService = new AdUserService();
    private final AdContentService adContentService = new AdContentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. 设置编码与跨域 Header (保留你原来的逻辑)
        request.setCharacterEncoding("UTF-8");

        String origin = request.getHeader("Origin");
        if (origin != null) {
            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Access-Control-Allow-Credentials", "true");
        }

        // 2. 访客追踪 (核心修改：优先从参数拿 visitorId，确保前端传过来的“定海神针” ID 生效)
        String visitorId = request.getParameter("visitorId");
        if (visitorId == null || visitorId.isEmpty() || "null".equals(visitorId)) {
            visitorId = getOrCreateVisitorId(request, response);
        }

        // 记录追踪日志 (保留)
        adUserService.handleVisitorTracking(visitorId);

        // 获取请求参数 (保留)
        String siteType = request.getParameter("siteType");
        String channel = request.getParameter("channel");

        System.out.println("[AdSystem] 访客ID: " + visitorId + " | 频道: " + channel);

        // 3. 分类映射逻辑 (严格保留你所有的映射关系)
        String targetCategory = "军事";
        if (channel != null && !channel.isEmpty()) {
            switch (channel.trim()) {
                case "经济": case "财经":
                    targetCategory = "金融"; break;
                case "科技":
                    targetCategory = "数码"; break;
                case "政治":
                    targetCategory = "教育"; break;
                case "娱乐":
                    targetCategory = "游戏"; break;
                case "体育":
                    targetCategory = "体育"; break;
                case "手机数码": case "电脑办公":
                    targetCategory = "电脑办公"; break;
                case "家用电器":
                    targetCategory = "家用电器"; break;
                case "生活家具": case "居家厨具":
                    targetCategory = "家居"; break;
                default:
                    targetCategory = channel;
            }
        }

        // 4. 【核心执行】将本次访问记入数据库分数 (确保推荐算法能读到最新的偏好)
        // 刷10次政治，这里就会被执行10次，数据库中“教育”分类就会涨到10分
        adUserService.recordUserInterest(visitorId, targetCategory);

        // 5. 执行推荐算法 (此时 visitorId 是稳定的，算法去库里查分数最高的分类)
        AdContent ad = adContentService.getRecommendedAd(siteType, visitorId, targetCategory);

        // 6. 返回结果 (保留你原有的 Gson 处理和展示逻辑)
        response.setContentType("application/json;charset=UTF-8");
        if (ad != null) {
            adContentService.incrementViewCount(ad.getId());
            response.getWriter().write(new Gson().toJson(ad));
            System.out.println("[AdSystem] 推荐结果: " + ad.getTitle() + " [" + targetCategory + "]");
        } else {
            response.getWriter().write("{\"error\":\"No ads for category: " + targetCategory + "\"}");
        }
    }

    private String getOrCreateVisitorId(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("visitor_id".equals(c.getName())) return c.getValue();
            }
        }

        // 生成新 ID
        String id = UUID.randomUUID().toString();

        // 使用特殊的 Header 设置 Cookie，确保跨域兼容性
        // Path=/ 确保在所有路径下都有效
        String cookieHeader = String.format("visitor_id=%s; Path=/; Max-Age=%d; HttpOnly",
                id, 60 * 60 * 24 * 30);
        response.addHeader("Set-Cookie", cookieHeader);

        return id;
    }
}