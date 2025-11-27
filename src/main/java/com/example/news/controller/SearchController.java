package com.example.news.controller;

import com.example.news.model.News;
import com.example.news.service.NewsService;
import com.example.news.service.CategoryService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.List;

@WebServlet("/search")
public class SearchController extends HttpServlet {
    private NewsService newsService = new NewsService();
    private CategoryService categoryService = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");

        System.out.println("搜索关键词: " + keyword); // 调试信息

        if (keyword != null && !keyword.trim().isEmpty()) {
            // 调用搜索服务
            List<News> searchResults = newsService.searchNews(keyword.trim());

            // 设置请求属性
            request.setAttribute("searchResults", searchResults);
            request.setAttribute("searchKeyword", keyword);
            request.setAttribute("categories", categoryService.getAllCategories());

            // 转发到搜索结果页面
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/news/search.jsp");
            dispatcher.forward(request, response);
        } else {
            // 如果关键词为空，重定向到首页
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }
}