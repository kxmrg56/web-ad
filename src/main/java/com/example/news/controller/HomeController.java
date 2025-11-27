package com.example.news.controller;

import com.example.news.service.NewsService;
import com.example.news.service.CategoryService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/home")
public class HomeController extends HttpServlet {
    private NewsService newsService = new NewsService();
    private CategoryService categoryService = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 设置最新新闻列表
        request.setAttribute("latestNews", newsService.getLatestNews(6));

        // 设置分类列表
        request.setAttribute("categories", categoryService.getAllCategories());

        // 转发到首页
        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
        dispatcher.forward(request, response);
    }
}