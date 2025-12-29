package com.example.news.dao;

import com.example.news.model.Category;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CategoryDaoImpl implements CategoryDao {

    private Category mapToCategory(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setId(rs.getLong("id"));
        category.setName(rs.getString("name"));
        category.setCode(rs.getString("code"));
        return category;
    }

    @Override
    public List<Category> findAll() {
        String sql = "SELECT * FROM categories ORDER BY id";
        return JdbcHelper.queryForList(sql, rs -> {
            try {
                return mapToCategory(rs);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public Category findByCode(String code) {
        String sql = "SELECT * FROM categories WHERE code = ?";
        return JdbcHelper.queryForObject(sql, rs -> {
            try {
                return mapToCategory(rs);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, code);
    }
}