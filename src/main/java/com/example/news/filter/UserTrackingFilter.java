package com.example.news.filter;

import com.example.news.model.UserContext;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.UUID;

@WebFilter("/*")
public class UserTrackingFilter implements Filter {

    // 与广告系统一致的Cookie名称
    private static final String VISITOR_ID_COOKIE = "visitor_id";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = httpRequest.getRequestURI();

        // 排除静态资源
        if (path.contains("/css/") || path.contains("/js/") || path.contains("/images/")) {
            chain.doFilter(request, response);
            return;
        }

        // 确保访客ID存在（与广告系统一致）
        String visitorId = ensureVisitorId(httpRequest, httpResponse);

        // 创建用户上下文
        UserContext userContext = createUserContext(httpRequest, visitorId);

        // 将用户上下文设置到请求属性中
        request.setAttribute("userContext", userContext);
        request.setAttribute("visitorId", visitorId); // 单独设置visitorId，便于JSP访问

        chain.doFilter(request, response);
    }

    private String ensureVisitorId(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String visitorId = null;

        // 查找visitor_id（广告系统使用的Cookie名）
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("visitor_id".equals(cookie.getName())) {
                    visitorId = cookie.getValue();
                    break;
                }
            }
        }

        // 如果不存在，生成新的visitor_id
        if (visitorId == null || visitorId.isEmpty()) {
            visitorId = java.util.UUID.randomUUID().toString();
            Cookie cookie = new Cookie("visitor_id", visitorId);
            cookie.setMaxAge(365 * 24 * 60 * 60); // 1年
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        return visitorId;
    }

    private String generateVisitorId() {
        // 生成UUID，与广告系统格式一致
        return UUID.randomUUID().toString();
    }

    private UserContext createUserContext(HttpServletRequest request, String visitorId) {
        UserContext userContext = new UserContext();
        userContext.setAnonymousId(visitorId);

        // 设置当前分类
        String category = request.getParameter("category");
        if (category != null && !category.isEmpty()) {
            userContext.setCurrentCategory(category);
        }

        // 设置用户区域（简化版）
        userContext.setUserRegion(detectUserRegion(request));

        // 设置用户代理
        userContext.setUserAgent(request.getHeader("User-Agent"));

        // 设置IP地址
        userContext.setIpAddress(getClientIpAddress(request));

        return userContext;
    }

    private String detectUserRegion(HttpServletRequest request) {
        // 简化版区域检测
        return "unknown";
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("UserTrackingFilter 初始化完成 - 使用visitor_id Cookie");
    }

    @Override
    public void destroy() {
        System.out.println("UserTrackingFilter 销毁");
    }
}