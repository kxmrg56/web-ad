package com.video.servlet;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 处理跨域请求 (CORS) 的过滤器
 * 允许前端 fetch 请求携带 cookie (credentials: include)
 */
@WebFilter("/*")
public class CorsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // 获取请求来源
        String origin = request.getHeader("Origin");

        // 1. 允许的来源
        // 注意：当 allowCredentials 为 true 时，allowOrigin 不能为 "*"
        // 这里为了方便测试，反射回 origin，生产环境建议判断 origin 是否在白名单内
        if (origin != null && !origin.isEmpty()) {
            response.setHeader("Access-Control-Allow-Origin", origin);
        }

        // 2. 允许携带凭证 (Cookie) - 这是实现 credentials: 'include' 的关键
        response.setHeader("Access-Control-Allow-Credentials", "true");

        // 3. 允许的请求方法
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");

        // 4. 允许的请求头
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");

        // 5. 预检请求 (OPTIONS) 的缓存时间
        response.setHeader("Access-Control-Max-Age", "3600");

        // 处理 OPTIONS 预检请求，直接返回成功，不进入后续逻辑
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {
    }
}