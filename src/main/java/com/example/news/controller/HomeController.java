package com.example.news.controller;

import com.example.news.service.NewsService;
import com.example.news.service.NewsServiceImpl;
import com.example.news.service.CategoryService;
import com.example.news.service.CategoryServiceImpl;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/home")
public class HomeController extends HttpServlet {
    private NewsService newsService = new NewsServiceImpl();
    private CategoryService categoryService = new CategoryServiceImpl();

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