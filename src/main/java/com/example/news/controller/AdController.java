package com.example.news.controller;

import com.example.news.service.AdService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/ad/*")
public class AdController extends HttpServlet {
    private AdService adService = new AdService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 广告统计接口 - 暂时预留
        String action = request.getParameter("action");
        String adId = request.getParameter("adId");
        String anonymousId = request.getParameter("anonymousId");

        if ("impression".equals(action)) {
            adService.recordAdImpression(adId, anonymousId);
        } else if ("click".equals(action)) {
            adService.recordAdClick(adId, anonymousId);
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }
}