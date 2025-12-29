package com.example.news.service;

import com.example.news.dao.CategoryDao;
import com.example.news.dao.CategoryDaoImpl;
import com.example.news.model.Category;
import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    private CategoryDao categoryDao = new CategoryDaoImpl();

    @Override
    public List<Category> getAllCategories() {
        return categoryDao.findAll();
    }

    @Override
    public Category getCategoryByCode(String code) {
        return categoryDao.findByCode(code);
    }
}