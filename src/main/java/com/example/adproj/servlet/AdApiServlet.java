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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 广告核心 API Servlet (C同学专用 - 修正版)
 * 作用：接收来自视频网、新闻网的请求，统一发放和识别身份 ID，并返回广告数据。
 */
@WebServlet("/ads/api/getAd")
public class AdApiServlet extends HttpServlet {

    // 业务 Service，保留原有逻辑
    private final AdUserService adUserService = new AdUserService();
    private final AdContentService adContentService = new AdContentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ============================================================
        // 1. 基础设置与跨域 (CORS)
        // ============================================================
        request.setCharacterEncoding("UTF-8");

        // 获取请求来源 (例如 http://10.100.164.44 或 .55)
        String origin = request.getHeader("Origin");
        if (origin != null) {
            // 允许该来源跨域访问
            response.setHeader("Access-Control-Allow-Origin", origin);
            // 允许携带 Cookie (credentials: include 的关键)
            response.setHeader("Access-Control-Allow-Credentials", "true");
        }

        // ============================================================
        // 2. 智能身份识别逻辑
        // 优先级：Cookie (老熟人) > URL参数 (客户端汇报) > 新生成
        // ============================================================

        String finalVisitorId = null;
        // 正则校验：只允许标准 UUID 格式，严防 "user_1974"
        String uuidRegex = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";

        // A. 第一优先级：检查 Cookie
        // 只要浏览器自动带上了 Cookie，说明是之前来过的设备，绝对信任它
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("visitor_id".equals(c.getName())) {
                    String val = c.getValue();
                    // 校验 ID 格式
                    if (val != null && val.matches(uuidRegex) && !"user_1974".equals(val)) {
                        finalVisitorId = val;
                        System.out.println("[AdServer] Cookie 识别成功: " + finalVisitorId);
                    }
                    break;
                }
            }
        }

        // B. 第二优先级：如果 Cookie 没读到，尝试读取前端传来的 visitorId 参数
        if (finalVisitorId == null) {
            String paramId = request.getParameter("visitorId");
            if (paramId != null && paramId.matches(uuidRegex) && !"user_1974".equals(paramId)) {
                finalVisitorId = paramId;
                System.out.println("[AdServer] 参数识别成功: " + finalVisitorId);
            }
        }

        // C. 第三优先级：如果是全新用户，生成新 ID
        if (finalVisitorId == null) {
            finalVisitorId = UUID.randomUUID().toString();
            System.out.println("[AdServer] 全新访客，生成 ID: " + finalVisitorId);
        }

        // ============================================================
        // 3. 种下 Cookie (关键修正！)
        // ============================================================
        // 注意：这里去掉了 "SameSite=Lax"。
        // 原因：在 IP 地址且非 HTTPS 的环境下，设置 SameSite 会导致 fetch 请求无法携带/保存 Cookie。
        // Path=/ 确保整个广告服务器路径通用。
        // HttpOnly=false 允许前端 JS 读取（方便调试和同步）。
        String cookieHeader = String.format(
                "visitor_id=%s; Path=/; Max-Age=%d; HttpOnly=false; SameSite=None; Secure",
                finalVisitorId, 60 * 60 * 24 * 365 * 10
        );
        response.addHeader("Set-Cookie", cookieHeader);


        // ============================================================
        // 4. 业务逻辑 (完全保留)
        // ============================================================

        // 4.1 记录访客追踪
        adUserService.handleVisitorTracking(finalVisitorId);

        // 4.2 获取分类参数
        String siteType = request.getParameter("siteType");
        String channel = request.getParameter("channel");
        // 使用下方的辅助方法进行映射
        String targetCategory = mapChannelToCategory(channel);

        // 4.3 记录用户兴趣
        adUserService.recordUserInterest(finalVisitorId, targetCategory);

        // 4.4 获取推荐广告
        AdContent ad = adContentService.getRecommendedAd(siteType, finalVisitorId, targetCategory);


        // ============================================================
        // 5. 构建并返回 JSON
        // ============================================================

        response.setContentType("application/json;charset=UTF-8");
        Map<String, Object> resultMap = new HashMap<>();

        // ★★★ 必须回传 ID，让前端（视频/新闻）同步更新本地存储 ★★★
        resultMap.put("visitorId", finalVisitorId);

        if (ad != null) {
            adContentService.incrementViewCount(ad.getId());
            resultMap.put("id", ad.getId());
            resultMap.put("title", ad.getTitle());
            resultMap.put("image", ad.getImageUrl());
            // 确保 linkUrl 不为 null，否则前端跳转可能会出错
            resultMap.put("linkUrl", ad.getLinkUrl() != null ? ad.getLinkUrl() : ad.getImageUrl());
        } else {
            resultMap.put("error", "No ads found");
            resultMap.put("image", "");
        }

        response.getWriter().write(new Gson().toJson(resultMap));
    }

    /**
     * 辅助方法：将不同网站的频道名称映射为统一的广告分类
     */
    private String mapChannelToCategory(String channel) {
        if (channel == null) return "军事";
        String c = channel.trim();
        switch (c) {
            case "经济": case "财经": return "金融";
            case "政治": return "教育";
            case "科技": case "手机数码": return "数码";
            case "娱乐": case "综艺": return "游戏";
            case "体育": case "运动专区": return "体育";
            case "电脑办公": return "电脑办公";
            case "家用电器": return "家用电器";
            case "生活家具": case "居家厨具": return "家居";
            case "美妆护理": case "美妆": return "美妆";
            case "服装精选": case "时尚": return "服装精选";
            case "日用文创": return "日用文创";
            case "美食": return "美食";
            case "旅游": return "旅游";
            // 如果没有匹配项，直接返回原值
            default: return c;
        }
    }
}