package com.video.servlet;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * 全局访客过滤器 (视频网站版)
 * 作用：确保 visitor_id Cookie 在整个服务器(Path=/)通用，实现跨网站追踪
 */
@WebFilter("/*")
public class GlobalVisitorFilter implements Filter {

    private static final String COOKIE_NAME = "visitor_id";
    // 正则：只允许标准 UUID 格式，严防 user_1974
    private static final String UUID_REGEX = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // 1. 检查现有的 ID
        String currentId = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (COOKIE_NAME.equals(c.getName())) {
                    String val = c.getValue();
                    // 如果 ID 是合法的 UUID 且不是 user_1974，我们才认它
                    if (val != null && val.matches(UUID_REGEX) && !"user_1974".equals(val)) {
                        currentId = val;
                    }
                    break;
                }
            }
        }

        // 2. 如果没有有效 ID，生成一个新的
        if (currentId == null) {
            currentId = UUID.randomUUID().toString();
            System.out.println("[VideoSite] 未检测到有效身份，生成全局 ID: " + currentId);
        }

        // 3. ★★★ 关键步骤：强制重写 Cookie，将 Path 设置为 "/" ★★★
        // 无论这个 ID 是刚生成的，还是原本就有的，我们都重新发给浏览器，
        // 并强调：这个 Cookie 属于根目录 "/"，谁都能读！
        Cookie globalCookie = new Cookie(COOKIE_NAME, currentId);
        globalCookie.setPath("/"); // 核心：打通视频网和新闻网的关键
        globalCookie.setMaxAge(60 * 60 * 24 * 365); // 1年有效
        globalCookie.setHttpOnly(false); // 允许前端 JS 读取，方便调试
        response.addCookie(globalCookie);

        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) {}
    @Override
    public void destroy() {}
}