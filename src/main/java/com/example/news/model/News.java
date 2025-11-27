package com.example.news.model;

import java.util.Date;

public class News {
    private Long id;
    private String title;
    private String summary;
    private String content;
    private String category;
    private String imageUrl;
    private String author;
    private Date publishDate;
    private Integer viewCount;

    // 构造函数
    public News() {}

    public News(Long id, String title, String summary, String content, String category,
                String imageUrl, String author, Date publishDate, Integer viewCount) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.content = content;
        this.category = category;
        this.imageUrl = imageUrl;
        this.author = author;
        this.publishDate = publishDate;
        this.viewCount = viewCount;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public Date getPublishDate() { return publishDate; }
    public void setPublishDate(Date publishDate) { this.publishDate = publishDate; }

    public Integer getViewCount() { return viewCount; }
    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }
}