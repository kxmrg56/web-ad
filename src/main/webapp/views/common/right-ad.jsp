<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>

<style>
    .ad-sidebar-card {
        width: 100%;
        border: 1px solid #e8e8e8;
        background: #ffffff;
        margin-bottom: 20px;
        border-radius: 4px;
        transition: all 0.3s ease;
        overflow: hidden;
        display: none;
    }
    .ad-sidebar-card:hover {
        box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        transform: translateY(-2px);
    }
    .ad-tag {
        font-size: 10px;
        color: #bbb;
        border: 1px solid #eee;
        padding: 0 4px;
        border-radius: 2px;
    }
    .ad-title {
        margin: 0;
        font-size: 15px;
        color: #333;
        line-height: 1.4;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        overflow: hidden;
    }
</style>

<div class="ad-sidebar right-ad" id="right-ad-container">
    <h3>🔥 热门推荐</h3>

    <!-- 新的广告内容 -->
    <div class="ad-content" id="right-ad-content">
        <div id="AD_SIDEBAR_CONTAINER" class="ad-sidebar-card">
            <div style="padding: 8px 12px; border-bottom: 1px solid #f0f0f0; display: flex; justify-content: space-between; align-items: center;">
                <span style="font-size: 13px; color: #ff4400; font-weight: bold;">特别推荐</span>
                <span class="ad-tag">广告</span>
            </div>

            <a id="AD_SIDEBAR_LINK" href="#" target="_blank" style="text-decoration: none;">
                <div style="width: 100%; height: 160px; background: #f7f7f7; overflow: hidden;">
                    <img id="AD_SIDEBAR_IMG" src="" style="width: 100%; height: 100%; object-fit: cover;"
                         onerror="this.src='${pageContext.request.contextPath}/images/news/default.jpg'">
                </div>
                <div style="padding: 12px;">
                    <h4 id="AD_SIDEBAR_TITLE" class="ad-title">正在为您匹配...</h4>
                    <div style="margin-top: 10px; display: flex; justify-content: flex-end;">
                        <span style="color: #007bff; font-size: 13px;">查看详情 &raquo;</span>
                    </div>
                </div>
            </a>
        </div>
    </div>
</div>

<script type="text/javascript">
    (function() {
        // 更安全的方式获取分类
        var getCategory = function() {
            try {
                // 直接从URL获取
                var params = new URLSearchParams(window.location.search);
                var enCat = params.get('category');

                var map = {
                    'politics': '政治',
                    'military': '军事',
                    'economy': '经济',
                    'finance': '金融',
                    'sports': '体育',
                    'tech': '科技',
                    'entertainment': '娱乐'
                };

                // 如果有URL参数，使用它
                if (enCat && map[enCat]) {
                    return map[enCat];
                }

                // 否则尝试从页面元素获取
                var categoryElement = document.getElementById('news-category');
                if (categoryElement) {
                    return categoryElement.innerText || "军事";
                }

                return "军事"; // 默认值
            } catch (e) {
                console.warn("获取分类失败:", e);
                return "军事";
            }
        };

        var AD_CONFIG = {
            server: "http://10.100.164.33:8080/adproj-1.0-SNAPSHOT",
            category: getCategory()
        };

        console.log("[AdSystem] 识别到的频道: " + AD_CONFIG.category);

        function getStableId() {
            let match = document.cookie.match(new RegExp('(^| )visitor_id=([^;]*)'));
            return match ? match[2] : "";
        }

        const myVisitorId = getStableId();

        fetch(AD_CONFIG.server + "/ads/api/getAd?siteType=news&channel=" + encodeURIComponent(AD_CONFIG.category)+ "&visitorId=" + myVisitorId, {
            method: 'GET',
            credentials: 'include'
        })
            .then(function(res) {
                if (!res.ok) {
                    throw new Error('Network response was not ok');
                }
                return res.json();
            })
            .then(function(data) {
                if (data && data.image) {
                    var imgPath = data.image.startsWith('/') ? data.image : "/" + data.image;
                    document.getElementById('AD_SIDEBAR_IMG').src = AD_CONFIG.server + imgPath;
                    document.getElementById('AD_SIDEBAR_TITLE').innerText = data.title;
                    document.getElementById('AD_SIDEBAR_LINK').href = data.link;
                    document.getElementById('AD_SIDEBAR_CONTAINER').style.display = 'block';
                    console.log("[AdSystem] 广告加载成功");
                } else {
                    document.getElementById('AD_SIDEBAR_CONTAINER').style.display = 'none';
                }
            })
            .catch(function(error) {
                console.warn("[AdSystem] 广告加载失败", error);
                document.getElementById('AD_SIDEBAR_CONTAINER').style.display = 'none';
            });
    })();
</script>

<style>
    .right-ad {
        background: #f9f9f9;
    }

    .ad-content {
        width: 100%;
    }
</style>
