package com.example.news.filter;

import com.example.news.model.UserContext;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * 用户追踪过滤器 (修正版)
 * 1. 解决了 setVisitorId 报错问题
 * 2. 实现了 Cookie 路径为 "/"，确保与视频网站 ID 互通
 */
@WebFilter("/*")
public class UserTrackingFilter implements Filter {

    private static final String COOKIE_NAME = "visitor_id";
    // 严格校验 UUID 格式，防止非法 ID
    private static final String UUID_REGEX = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String path = request.getRequestURI();

        // 排除静态资源，提高性能
        if (path.contains("/css/") || path.contains("/js/") || path.contains("/images/")) {
            chain.doFilter(req, res);
            return;
        }

        // 1. 获取并确保全局 Visitor ID 存在 (核心跨站逻辑)
        String visitorId = ensureGlobalVisitorId(request, response);

        // 2. 构建 UserContext (保留你原有的业务逻辑)
        UserContext userContext = createUserContext(request, visitorId);

        // 3. 放入 Request 域，供 JSP/Controller 使用
        request.setAttribute("userContext", userContext);
        request.setAttribute("visitorId", visitorId);

        // 4. 将 ID 存入 ThreadLocal (如果有的话)，防止多线程冲突
        if (UserContext.class != null) {
            // 注意：这里如果 UserContext 没有 setContext/setVisitorId 静态方法，请根据你实际情况调整
            // 既然你原来的代码没用 ThreadLocal，这里保持简单，只用 setAttribute 即可
        }

        try {
            chain.doFilter(req, res);
        } finally {
            // 清理工作
        }
    }

    /**
     * 核心方法：获取 ID 并强制设置 Cookie Path = /
     */
    private String ensureGlobalVisitorId(HttpServletRequest request, HttpServletResponse response) {
        String visitorId = null;
        Cookie[] cookies = request.getCookies();

        // A. 尝试读取现有的 ID
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (COOKIE_NAME.equals(c.getName())) {
                    String val = c.getValue();
                    // 校验格式，且屏蔽黑名单 ID
                    if (val != null && val.matches(UUID_REGEX) && !"user_1974".equals(val)) {
                        visitorId = val;
                    }
                    break;
                }
            }
        }

        // B. 如果没有有效 ID，生成一个新的
        if (visitorId == null) {
            visitorId = UUID.randomUUID().toString();
            System.out.println("[NewsSite] 生成新访客ID: " + visitorId);
        }

        // C. ★★★ 无论 ID 是新是旧，都重写 Cookie 确保 Path=/ ★★★
        Cookie cookie = new Cookie(COOKIE_NAME, visitorId);
        cookie.setPath("/"); // 关键：打通视频网和新闻网
        cookie.setMaxAge(60 * 60 * 24 * 365); // 1年有效
        cookie.setHttpOnly(false); // 允许前端 JS 读取
        response.addCookie(cookie);

        return visitorId;
    }

    /**
     * 构建 UserContext 对象 (修正了报错的方法调用)
     */
    private UserContext createUserContext(HttpServletRequest request, String visitorId) {
        UserContext userContext = new UserContext();

        // ★★★ 修正点：这里改回 setAnonymousId，解决报错 ★★★
        userContext.setAnonymousId(visitorId);

        // 设置当前分类
        String category = request.getParameter("category");
        if (category != null && !category.isEmpty()) {
            userContext.setCurrentCategory(category);
        }

        // 设置IP和UA
        userContext.setIpAddress(getClientIpAddress(request));
        userContext.setUserAgent(request.getHeader("User-Agent"));

        // 简化的区域检测
        userContext.setUserRegion("unknown");

        return userContext;
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void destroy() {}
}