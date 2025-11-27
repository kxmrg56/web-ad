package com.example.news.dao;

import com.example.news.model.Category;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    public List<Category> findAll() {
        // 数据库版本 - 暂未使用
        return new ArrayList<>();
    }

    public Category findByCode(String code) {
        // 数据库版本 - 暂未使用
        return null;
    }
}