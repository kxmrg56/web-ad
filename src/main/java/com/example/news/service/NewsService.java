package com.example.news.service;

import com.example.news.model.News;
import java.util.List;

public interface NewsService {
    // 获取最新新闻
    List<News> getLatestNews(int limit);

    // 根据ID获取新闻
    News getNewsById(Long id);

    // 根据分类获取新闻
    List<News> getNewsByCategory(String category);

    // 获取所有新闻
    List<News> getAllNews();

    // 搜索新闻
    List<News> searchNews(String keyword);
}