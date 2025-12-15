package com.example.adproj.service;

import com.example.adproj.dao.AdUserDAO;
import com.example.adproj.dao.impl.AdUserDAOImpl;

public class AdUserService {

    private final AdUserDAO adUserDAO = new AdUserDAOImpl();

    /**
     * 处理 visitorId：
     * - 不存在 → 插入
     * - 已存在 → 更新最后访问时间
     */
    public void handleVisitor(String visitorId) {
        if (!adUserDAO.existsByVisitorId(visitorId)) {
            adUserDAO.insert(visitorId);
        } else {
            adUserDAO.updateLastVisit(visitorId);
        }
    }
}
