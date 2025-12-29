<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<div class="ad-sidebar left-ad" id="left-ad-container">
    <h3>📢 推荐广告</h3>
    <div class="ad-content" id="left-ad-content">
        <div class="ad-loading">
            <p>加载中...</p>
        </div>
    </div>
</div>

<script>
    // 左侧广告初始化
    document.addEventListener('DOMContentLoaded', function() {
        loadAd('left');
    });

    // 广告加载函数
    async function loadAd(position) {
        const container = document.getElementById(position + '-ad-content');
        const loadingEl = container.querySelector('.ad-loading');

        try {
            // 调用真实的广告API
            const response = await fetch('http://10.100.164.33:8080/adproj-1.0-SNAPSHOT/ads/api/getAd');

            if (response.ok) {
                const adData = await response.json();

                // 构建广告HTML
                const adHtml = `
                <div class="ad-item" data-ad-id="${adData.adId}">
                    <h4>${adData.title}</h4>
                    <a href="${adData.link}" target="_blank" onclick="trackAdClick('${adData.adId}')">
                        <img src="${adData.image}" alt="${adData.title}"
                             style="width:100%; border-radius:5px;"
                             onerror="this.src='/images/ads/default.jpg'">
                    </a>
                    <button onclick="window.open('${adData.link}', '_blank'); trackAdClick('${adData.adId}')">
                        了解更多
                    </button>
                </div>
            `;

                // 移除加载动画，显示广告
                container.innerHTML = adHtml;

            } else {
                showDefaultAd(container);
            }
        } catch (error) {
            console.error('广告加载失败:', error);
            showDefaultAd(container);
        }
    }

    // 显示默认广告
    function showDefaultAd(container) {
        container.innerHTML = `
        <div class="ad-item">
            <h4>精彩推荐</h4>
            <img src="/images/ads/default.jpg" alt="默认广告" style="width:100%;">
            <button onclick="alert('广告系统维护中')">暂不可用</button>
        </div>
    `;
    }

    // 记录广告点击
    function trackAdClick(adId) {
        console.log(`广告点击: ${adId}`);
        // 可以在这里调用广告系统的点击统计API
    }
</script>

<style>
    .ad-sidebar {
        width: 200px;
        background: #f5f5f5;
        border: 1px solid #ddd;
        border-radius: 5px;
        padding: 15px;
        margin-bottom: 20px;
    }

    .ad-sidebar h3 {
        margin: 0 0 15px 0;
        font-size: 16px;
        color: #333;
    }

    .ad-item {
        text-align: center;
    }

    .ad-item h4 {
        margin: 0 0 10px 0;
        font-size: 14px;
        color: #666;
    }

    .ad-item button {
        margin-top: 10px;
        padding: 8px 15px;
        background: #007bff;
        color: white;
        border: none;
        border-radius: 3px;
        cursor: pointer;
    }

    .ad-item button:hover {
        background: #0056b3;
    }

    .ad-loading {
        text-align: center;
        color: #999;
        padding: 20px 0;
    }
</style>