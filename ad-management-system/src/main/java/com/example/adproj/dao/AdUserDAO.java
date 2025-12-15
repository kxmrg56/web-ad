package com.example.adproj.dao;

public interface AdUserDAO {

    /**
     * 根据 visitorId 查询是否存在
     */
    boolean existsByVisitorId(String visitorId);

    /**
     * 插入新用户
     */
    int insert(String visitorId);

    /**
     * 更新最后访问时间
     */
    int updateLastVisit(String visitorId);
}
