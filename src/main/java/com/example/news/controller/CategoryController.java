package com.example.news.controller;

import com.example.news.service.NewsService;
import com.example.news.service.CategoryService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/category/*")
public class CategoryController extends HttpServlet {
    private NewsService newsService = new NewsService();
    private CategoryService categoryService = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        if (pathInfo != null) {
            String categoryCode = pathInfo.substring(1); // 去掉开头的"/"

            // 设置新闻列表
            request.setAttribute("newsList", newsService.getNewsByCategory(categoryCode));

            // 设置当前分类信息
            request.setAttribute("currentCategory", categoryService.getCategoryByCode(categoryCode));

            // 设置所有分类（用于导航）
            request.setAttribute("categories", categoryService.getAllCategories());

            // 转发到分类页面
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/news/list.jsp");
            dispatcher.forward(request, response);
        } else {
            // 如果没有指定分类，重定向到首页
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        }
    }
}