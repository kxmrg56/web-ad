package com.example.adproj.dao;
import com.example.adproj.entity.AdContent;
import java.util.List;

public interface AdContentDAO {
    AdContent getRandomAd();
    AdContent getAdByCategory(String category);

    // --- 新增：获取指定业主的所有广告列表 ---
    List<AdContent> getAdsByOwner(int ownerId);
}