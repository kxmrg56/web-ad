package com.example.news.dao;

import com.example.news.model.News;
import java.util.List;

public interface NewsDao {
    // 根据ID获取新闻
    News findById(Long id);

    // 根据分类获取新闻列表
    List<News> findByCategory(String categoryCode);

    // 获取最新新闻
    List<News> findLatestNews(int limit);

    // 获取所有新闻
    List<News> findAll();

    // 搜索新闻
    List<News> searchByKeyword(String keyword);

    // 增加阅读量
    void incrementViewCount(Long id);
}