package com.example.news.filter;

import com.example.news.model.UserContext;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.UUID;

@WebFilter("/*")
public class UserTrackingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = httpRequest.getRequestURI();

        // 排除静态资源 - 考虑上下文路径
        if (path.contains("/css/") || path.contains("/js/") || path.contains("/images/")) {
            chain.doFilter(request, response);
            return;
        }

        // 确保匿名用户ID存在并创建用户上下文
        UserContext userContext = createUserContext(httpRequest, httpResponse);

        // 将用户上下文设置到请求属性中
        request.setAttribute("userContext", userContext);

        chain.doFilter(request, response);
    }

    private UserContext createUserContext(HttpServletRequest request, HttpServletResponse response) {
        String anonymousId = ensureAnonymousId(request, response);
        UserContext userContext = new UserContext();
        userContext.setAnonymousId(anonymousId);

        // 设置当前分类（从请求参数或URL路径获取）
        String category = request.getParameter("category");
        if (category != null && !category.isEmpty()) {
            userContext.setCurrentCategory(category);
        }

        // 设置用户区域（简化版，实际可以从IP地址获取）
        userContext.setUserRegion(detectUserRegion(request));

        // 设置用户代理
        userContext.setUserAgent(request.getHeader("User-Agent"));

        // 设置IP地址
        userContext.setIpAddress(getClientIpAddress(request));

        // 从Cookie中读取用户兴趣（简化版）
        loadUserInterestsFromCookie(request, userContext);

        return userContext;
    }

    private String ensureAnonymousId(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String anonymousId = null;

        // 查找现有的匿名ID
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("anonymousId".equals(cookie.getName())) {
                    anonymousId = cookie.getValue();
                    break;
                }
            }
        }

        // 如果不存在，生成新的匿名ID
        if (anonymousId == null || anonymousId.isEmpty()) {
            anonymousId = generateAnonymousId();
            Cookie cookie = new Cookie("anonymousId", anonymousId);
            cookie.setMaxAge(365 * 24 * 60 * 60); // 1年有效期
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        return anonymousId;
    }

    private String generateAnonymousId() {
        return "user_" + UUID.randomUUID().toString().substring(0, 8);
    }

    private String detectUserRegion(HttpServletRequest request) {
        // 简化版区域检测，实际可以从IP地址库获取
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

    private void loadUserInterestsFromCookie(HttpServletRequest request, UserContext userContext) {
        // 简化版兴趣加载，实际可以从Cookie或数据库中读取用户历史行为
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().startsWith("interest_")) {
                    String category = cookie.getName().substring(9); // 去掉"interest_"前缀
                    userContext.addInterest(category, cookie.getValue());
                }
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("UserTrackingFilter 初始化完成");
    }

    @Override
    public void destroy() {
        System.out.println("UserTrackingFilter 销毁");
    }
}