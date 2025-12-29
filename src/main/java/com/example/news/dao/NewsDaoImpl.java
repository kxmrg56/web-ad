package com.example.news.dao;

import com.example.news.model.News;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class NewsDaoImpl implements NewsDao {

    // 从ResultSet映射到News对象
    private News mapToNews(ResultSet rs) throws SQLException {
        News news = new News();
        news.setId(rs.getLong("id"));
        news.setTitle(rs.getString("title"));
        news.setSummary(rs.getString("summary"));
        news.setContent(rs.getString("content"));
        news.setCategory(rs.getString("category_code"));
        news.setImageUrl(rs.getString("image_url"));
        news.setAuthor(rs.getString("author"));
        news.setViewCount(rs.getInt("view_count"));
        news.setPublishDate(new Date(rs.getTimestamp("publish_date").getTime()));
        return news;
    }

    @Override
    public News findById(Long id) {
        String sql = "SELECT * FROM news WHERE id = ?";
        return JdbcHelper.queryForObject(sql, rs -> {
            try {
                return mapToNews(rs);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, id);
    }

    @Override
    public List<News> findByCategory(String categoryCode) {
        String sql = "SELECT * FROM news WHERE category_code = ? ORDER BY publish_date DESC";
        return JdbcHelper.queryForList(sql, rs -> {
            try {
                return mapToNews(rs);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, categoryCode);
    }

    @Override
    public List<News> findLatestNews(int limit) {
        String sql = "SELECT * FROM news ORDER BY publish_date DESC LIMIT ?";
        return JdbcHelper.queryForList(sql, rs -> {
            try {
                return mapToNews(rs);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, limit);
    }

    @Override
    public List<News> findAll() {
        String sql = "SELECT * FROM news ORDER BY publish_date DESC";
        return JdbcHelper.queryForList(sql, rs -> {
            try {
                return mapToNews(rs);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public List<News> searchByKeyword(String keyword) {
        String searchKeyword = "%" + keyword + "%";
        String sql = "SELECT * FROM news WHERE title LIKE ? OR content LIKE ? OR summary LIKE ? OR author LIKE ? ORDER BY publish_date DESC";
        return JdbcHelper.queryForList(sql, rs -> {
            try {
                return mapToNews(rs);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, searchKeyword, searchKeyword, searchKeyword, searchKeyword);
    }

    @Override
    public void incrementViewCount(Long id) {
        String sql = "UPDATE news SET view_count = view_count + 1 WHERE id = ?";
        JdbcHelper.executeUpdate(sql, id);
    }
}