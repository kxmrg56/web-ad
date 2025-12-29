<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<div class="ad-sidebar right-ad" id="right-ad-container">
    <h3>🔥 热门推荐</h3>
    <div class="ad-content" id="right-ad-content">
        <div class="ad-loading">
            <p>加载中...</p>
        </div>
    </div>
</div>

<script>
    // 右侧广告初始化
    document.addEventListener('DOMContentLoaded', function() {
        loadAd('right');
    });

    // 使用相同的loadAd函数
    // 这里不需要重复定义，因为left-ad.jsp已经定义了
</script>

<style>
    .right-ad {
        background: #f9f9f9;
    }
</style>