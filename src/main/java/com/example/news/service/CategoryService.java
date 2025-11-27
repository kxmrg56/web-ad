package com.example.news.service;

import com.example.news.model.Category;
import java.util.*;

public class CategoryService {
    private static List<Category> categories = Arrays.asList(
            new Category(1L, "政治", "politics"),
            new Category(2L, "军事", "military"),
            new Category(3L, "经济", "economy"),
            new Category(4L, "体育", "sports"),
            new Category(5L, "科技", "tech"),
            new Category(6L, "娱乐", "entertainment")
    );

    public List<Category> getAllCategories() {
        return new ArrayList<>(categories);
    }

    public Category getCategoryByCode(String code) {
        return categories.stream()
                .filter(cat -> cat.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}