<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<style>
    .ad-card { background: #1f1f1f; border: 1px solid #333; margin-bottom: 20px; border-radius: 4px; display: none; overflow: hidden; }
    .ad-card:hover { border-color: #555; }
    /* 媒体容器：既能放video也能放img */
    .ad-media { width: 100%; height: 160px; object-fit: cover; background: #000; display: block; }
    .ad-title { color: #eee; padding: 10px; font-weight: bold; font-size: 14px; margin: 0; }
    .ad-badge { color: #e50914; font-size: 12px; font-weight: bold; padding: 8px; border-bottom: 1px solid #333; display: block; }
</style>

<div id="SIDE_AD" class="ad-card">
    <span class="ad-badge">🔥 精彩推荐 (Sponsored)</span>
    <a id="SIDE_LINK" href="#" target="_blank" style="text-decoration: none;">
        <!-- 动态容器 -->
        <div id="MEDIA_CONTAINER"></div>
        <h4 id="SIDE_TITLE" class="ad-title">Loading...</h4>
    </a>
</div>

<script>
    (function(){
        const ROOT = "http://10.100.164.33:8080/adproj-1.0-SNAPSHOT";
        const CAT_MAP = {'综艺':'时尚', '科技':'数码', '电影':'电影', '游戏':'游戏', '体育':'体育'};

        let params = new URLSearchParams(window.location.search);
        let cat = params.get('category') || "电影";
        let channel = CAT_MAP[cat] || "电影";
        let vid = (document.cookie.match(/(^| )visitor_id=([^;]+)/) || [])[2] || "";

        let url = ROOT + "/ads/api/getAd?siteType=video&channel=" + encodeURIComponent(channel) + "&visitorId=" + vid;

        fetch(url, {credentials: 'include'})
            .then(r => r.json())
            .then(d => {
                if(d && d.image) {
                    let fullPath = d.image.startsWith('http') ? d.image : (ROOT + "/" + d.image);

                    let container = document.getElementById('MEDIA_CONTAINER');
                    let mediaEl;

                    // ★★★ 智能判断：是视频就动起来，是图片就静止 ★★★
                    if (fullPath.endsWith('.mp4')) {
                        mediaEl = document.createElement('video');
                        mediaEl.className = 'ad-media';
                        mediaEl.autoplay = true;
                        mediaEl.muted = true; // 文档强调：必须静音才能自动播放
                        mediaEl.loop = true;
                        mediaEl.src = fullPath;
                    } else {
                        mediaEl = document.createElement('img');
                        mediaEl.className = 'ad-media';
                        mediaEl.src = fullPath;
                    }

                    container.appendChild(mediaEl);
                    document.getElementById('SIDE_TITLE').innerText = d.title;
                    document.getElementById('SIDE_LINK').href = d.linkUrl || "#";
                    document.getElementById('SIDE_AD').style.display = "block";
                }
            }).catch(e => console.log("Sidebar Ad failed:", e));
    })();
</script>