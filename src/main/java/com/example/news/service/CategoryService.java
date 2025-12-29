package com.example.news.service;

import com.example.news.model.Category;
import java.util.List;

public interface CategoryService {
    // 获取所有分类
    List<Category> getAllCategories();

    // 根据code获取分类
    Category getCategoryByCode(String code);
}