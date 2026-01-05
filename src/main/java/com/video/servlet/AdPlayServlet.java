package com.video.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/playAd")
public class AdPlayServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getParameter("url");
        // 简单校验
        if (url == null || url.isEmpty()) {
            resp.sendRedirect("index");
            return;
        }
        // 转发到受保护的 JSP 页面
        req.getRequestDispatcher("/WEB-INF/views/play_ad.jsp").forward(req, resp);
    }
}