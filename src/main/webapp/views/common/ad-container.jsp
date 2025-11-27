<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="ad-container" id="ad-container-${param.position}">
    <div class="ad-loading">
        <p>广告加载中...</p>
        <div class="loading-spinner"></div>
    </div>
    <div class="ad-content" style="display: none;"></div>
</div>

<script>
    // 广告容器初始化
    document.addEventListener('DOMContentLoaded', function() {
        const container = document.getElementById('ad-container-${param.position}');
        if (container) {
            const position = '${param.position}'; // left 或 right
            // 这里可以添加特定位置的广告初始化逻辑
            console.log('初始化广告容器: ' + position);
        }
    });
</script>

<style>
    .loading-spinner {
        border: 3px solid #f3f3f3;
        border-top: 3px solid #3498db;
        border-radius: 50%;
        width: 30px;
        height: 30px;
        animation: spin 1s linear infinite;
        margin: 10px auto;
    }

    @keyframes spin {
        0% { transform: rotate(0deg); }
        100% { transform: rotate(360deg); }
    }

    .ad-loading {
        text-align: center;
        color: #666;
        padding: 20px;
    }
</style>