package com.example.news.model;

import java.util.HashMap;
import java.util.Map;

public class UserContext {
    private String anonymousId;
    private String currentCategory;
    private String userRegion;
    private Map<String, Object> interests;
    private String userAgent;
    private String ipAddress;

    public UserContext() {
        this.interests = new HashMap<>();
    }

    public UserContext(String anonymousId, String currentCategory) {
        this();
        this.anonymousId = anonymousId;
        this.currentCategory = currentCategory;
    }

    // Getters and Setters
    public String getAnonymousId() { return anonymousId; }
    public void setAnonymousId(String anonymousId) { this.anonymousId = anonymousId; }

    public String getCurrentCategory() { return currentCategory; }
    public void setCurrentCategory(String currentCategory) { this.currentCategory = currentCategory; }

    public String getUserRegion() { return userRegion; }
    public void setUserRegion(String userRegion) { this.userRegion = userRegion; }

    public Map<String, Object> getInterests() { return interests; }
    public void setInterests(Map<String, Object> interests) { this.interests = interests; }

    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    // 工具方法
    public void addInterest(String category, Object value) {
        this.interests.put(category, value);
    }

    public Object getInterest(String category) {
        return this.interests.get(category);
    }

    public boolean hasInterest(String category) {
        return this.interests.containsKey(category);
    }

    @Override
    public String toString() {
        return "UserContext{" +
                "anonymousId='" + anonymousId + '\'' +
                ", currentCategory='" + currentCategory + '\'' +
                ", userRegion='" + userRegion + '\'' +
                ", interests=" + interests +
                '}';
    }
}