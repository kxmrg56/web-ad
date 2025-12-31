package com.example.adproj.entity;

import com.google.gson.annotations.SerializedName;

public class AdContent {
    private Integer id;
    private String title;

    // 关键：加注解，让返回给前端的 JSON 字段名为 "image"
    @SerializedName("image")
    private String imageUrl;

    // 关键：加注解，让返回给前端的 JSON 字段名为 "link"
    @SerializedName("link")
    private String linkUrl;

    private String category;
    private Integer ownerId;

    // Getter & Setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getLinkUrl() { return linkUrl; }
    public void setLinkUrl(String linkUrl) { this.linkUrl = linkUrl; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Integer getOwnerId() { return ownerId; }
    public void setOwnerId(Integer ownerId) { this.ownerId = ownerId; }
}