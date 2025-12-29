package com.example.news.service;

import com.example.news.dao.NewsDao;
import com.example.news.dao.NewsDaoImpl;
import com.example.news.model.News;
import java.util.List;

public class NewsServiceImpl implements NewsService {
    private NewsDao newsDao = new NewsDaoImpl();

    @Override
    public List<News> getLatestNews(int limit) {
        return newsDao.findLatestNews(limit);
    }

    @Override
    public News getNewsById(Long id) {
        News news = newsDao.findById(id);
        if (news != null) {
            // 增加阅读量
            newsDao.incrementViewCount(id);
            // 重新获取最新的阅读量
            news = newsDao.findById(id);
        }
        return news;
    }

    @Override
    public List<News> getNewsByCategory(String category) {
        return newsDao.findByCategory(category);
    }

    @Override
    public List<News> getAllNews() {
        return newsDao.findAll();
    }

    @Override
    public List<News> searchNews(String keyword) {
        return newsDao.searchByKeyword(keyword);
    }
}