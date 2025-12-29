package com.example.news.dao;

import com.example.news.model.Category;
import java.util.List;

public interface CategoryDao {
    // 获取所有分类
    List<Category> findAll();

    // 根据code获取分类
    Category findByCode(String code);
}