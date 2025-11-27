package com.example.news.controller;

import com.example.news.service.NewsService;
import com.example.news.service.CategoryService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/news/*")
public class NewsController extends HttpServlet {
    private NewsService newsService = new NewsService();
    private CategoryService categoryService = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            // 新闻列表页
            String category = request.getParameter("category");
            if (category != null && !category.isEmpty()) {
                request.setAttribute("newsList", newsService.getNewsByCategory(category));
                request.setAttribute("currentCategory", categoryService.getCategoryByCode(category));
            } else {
                request.setAttribute("newsList", newsService.getAllNews());
            }

            request.setAttribute("categories", categoryService.getAllCategories());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/news/list.jsp");
            dispatcher.forward(request, response);

        } else if (pathInfo.equals("/detail")) {
            // 新闻详情页
            String idParam = request.getParameter("id");
            if (idParam != null && !idParam.isEmpty()) {
                try {
                    Long id = Long.parseLong(idParam);
                    com.example.news.model.News news = newsService.getNewsById(id);
                    if (news != null) {
                        request.setAttribute("news", news);
                        request.setAttribute("categories", categoryService.getAllCategories());

                        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/news/detail.jsp");
                        dispatcher.forward(request, response);
                        return;
                    }
                } catch (NumberFormatException e) {
                    // ID格式错误
                }
            }

            // 如果新闻不存在或ID无效，重定向到首页
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        }
    }
}