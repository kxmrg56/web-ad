package com.video.model;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Video {
    private int id;
    private String title;
    private String description;
    private String filePath;
    private String category;
    private boolean hasAd;
    private int adInsertTime;
    private String adFilePath;

    // Standard Getters/Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public boolean isHasAd() { return hasAd; }
    public void setHasAd(boolean hasAd) { this.hasAd = hasAd; }
    public int getAdInsertTime() { return adInsertTime; }
    public void setAdInsertTime(int adInsertTime) { this.adInsertTime = adInsertTime; }
    public String getAdFilePath() { return adFilePath; }
    public void setAdFilePath(String adFilePath) { this.adFilePath = adFilePath; }

    public String getEncodedPath() {
        try { return URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString()); } catch (Exception e) { return ""; }
    }
    public String getAdEncodedPath() {
        try { return URLEncoder.encode(adFilePath, StandardCharsets.UTF_8.toString()); } catch (Exception e) { return ""; }
    }
}