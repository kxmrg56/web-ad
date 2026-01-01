<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<style>
    .video-ad-box {
        background-color: #222; border-radius: 8px; overflow: hidden; margin-bottom: 25px;
        box-shadow: 0 4px 15px rgba(0,0,0,0.5); border: 1px solid #333; display: none;
    }
    .video-ad-box:hover { transform: translateY(-3px); border-color: #555; }
    .ad-header { padding: 10px 15px; background: rgba(255,255,255,0.05); display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid #333; }
    .ad-badge { background: #333; color: #888; font-size: 11px; padding: 2px 6px; border-radius: 3px; }
    .ad-img-wrapper { width: 100%; height: 180px; background: #000; }
    .ad-img-wrapper img { width: 100%; height: 100%; object-fit: cover; opacity: 0.9; }
    .ad-info { padding: 15px; }
    .ad-title { color: #fff; font-size: 16px; font-weight: 600; margin: 0 0 8px 0; }
    .ad-link-btn { color: #e50914; font-size: 13px; text-decoration: none; font-weight: bold; }
</style>

<div id="VIDEO_AD_COMPONENT" class="video-ad-box">
    <a id="AD_LINK" href="#" target="_blank" style="text-decoration: none; display: block;">
        <div class="ad-header">
            <span style="color: #ddd; font-size: 13px;">🎁 猜你喜欢 (Sponsored)</span><span class="ad-badge">广告</span>
        </div>
        <div class="ad-img-wrapper"><img id="AD_IMG" src="" alt="Advertisement"></div>
        <div class="ad-info">
            <h4 id="AD_TITLE" class="ad-title">正在加载推荐...</h4><span class="ad-link-btn">立即查看 &rarr;</span>
        </div>
    </a>
</div>

<script>
    (function() {
        // TODO: 请确保这里是你队友的真实IP地址
        const SERVER_URL = "http://10.100.164.33:8080/adproj-1.0-SNAPSHOT";

        function getStableId() {
            let match = document.cookie.match(new RegExp('(^| )visitor_id=([^;]*)'));
            return match ? match[2] : "";
        }
        function getMappedChannel() {
            const params = new URLSearchParams(window.location.search);
            const currentCat = params.get('category');
            if (!currentCat) return "电影";
            const map = {
                '电影': '电影', '游戏': '游戏', '体育': '体育', '教育': '教育',
                '美食': '美食', '旅游': '旅游',
                '综艺': '时尚', '科技': '数码', '动漫': '时尚', '剧集': '电影'
            };
            return map[currentCat] || "电影";
        }

        const visitorId = getStableId();
        const mappedChannel = getMappedChannel();
        // 修改了 console.log 的写法，避免 JSP 解析报错
        console.log("[AdSystem] Site: video | Channel: " + mappedChannel + " | ID: " + visitorId);

        // ★★★ 修复点：使用 + 号拼接 URL，修复 JSP 500 错误 ★★★
        const apiPath = SERVER_URL + "/ads/api/getAd?siteType=video&channel=" + encodeURIComponent(mappedChannel) + "&visitorId=" + visitorId;

        fetch(apiPath, {
            method: 'GET', credentials: 'include'
        })
            .then(res => { if (!res.ok) throw new Error("API Error"); return res.json(); })
            .then(data => {
                if (data && data.image) {
                    const finalImgUrl = data.image.startsWith('http') ? data.image : SERVER_URL + (data.image.startsWith('/') ? '' : '/') + data.image;
                    document.getElementById('AD_IMG').src = finalImgUrl;
                    document.getElementById('AD_TITLE').innerText = data.title;
                    document.getElementById('AD_LINK').href = data.link;
                    document.getElementById('VIDEO_AD_COMPONENT').style.display = 'block';
                }
            }).catch(err => console.warn("[AdSystem] Ignored:", err));
    })();
</script>