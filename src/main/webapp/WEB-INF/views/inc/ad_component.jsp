<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<style>
    .ad-card { background: #1f1f1f; border: 1px solid #333; margin-bottom: 20px; border-radius: 4px;
        display: none; overflow: hidden; }
    .ad-card:hover { border-color: #555;
    }
    /* 媒体容器：既能放video也能放img */
    .ad-media { width: 100%; height: 160px; object-fit: cover; background: #000;
        display: block; }
    .ad-title { color: #eee; padding: 10px; font-weight: bold; font-size: 14px; margin: 0;
    }
    .ad-badge { color: #e50914; font-size: 12px; font-weight: bold; padding: 8px; border-bottom: 1px solid #333; display: block;
    }
</style>

<div id="SIDE_AD" class="ad-card">
    <span class="ad-badge">🔥 精彩推荐 (Sponsored)</span>
    <a id="SIDE_LINK" href="javascript:;"
       target="_blank" style="text-decoration: none;">
        <div id="MEDIA_CONTAINER"></div>
        <h4 id="SIDE_TITLE" class="ad-title">Loading...</h4>
    </a>
</div>

<script>
    (function(){
        // ★★★ 核心配置：指向广告服务器 C同学的 IP ★★★
        const ROOT = "http://10.100.164.33:8080/adproj-1.0-SNAPSHOT";
        const CAT_MAP = {'综艺':'时尚', '科技':'数码', '电影':'电影', '游戏':'游戏', '体育':'体育'};

        let params = new URLSearchParams(window.location.search);
        let cat = params.get('category') || "电影";
        let channel = CAT_MAP[cat] || "电影";

        // 1. 自动清除死锁 ID (user_1974)
        let localVid = localStorage.getItem('global_visitor_id');
        if (localVid === 'user_1974') {
            console.warn("[AdComponent] 检测到死锁 ID (user_1974)，已自动清除。");
            localStorage.removeItem('global_visitor_id');
            localVid = "";
        }

        // 尝试从 Cookie 获取 (作为备份)
        let cookieVid = (document.cookie.match(/(^| )visitor_id=([^;]+)/) || [])[2];
        let vid = localVid || cookieVid || "";

        // 构建请求 URL
        let url = ROOT + "/ads/api/getAd?siteType=video&channel=" + encodeURIComponent(channel) + "&visitorId=" + vid;

        // ★★★ 核心修改：开启 credentials: 'include' 允许跨域 Cookie ★★★
        fetch(url, {
            method: 'GET',
            credentials: 'include'
        })
            .then(r => r.json())
            .then(d => {
                if(d && d.image) {
                    console.log("[AdComponent] 广告数据:", d);

                    // 2. 更新 ID 逻辑
                    if (d.visitorId) {
                        if (d.visitorId !== 'user_1974') {
                            localStorage.setItem('global_visitor_id', d.visitorId);
                            console.log("[AdComponent] 更新本地 ID:", d.visitorId);
                        } else {
                            console.warn("[AdComponent] 后端返回了 user_1974，已忽略");
                        }
                    }

                    // 3. 处理图片或视频路径
                    let fullPath = d.image.startsWith('http') ?
                        d.image : encodeURI(ROOT + (d.image.startsWith('/') ? d.image : "/" + d.image));

                    let container = document.getElementById('MEDIA_CONTAINER');
                    container.innerHTML = "";

                    let mediaEl;
                    // 判断文件扩展名来决定创建 img 还是 video 标签
                    if (fullPath.toLowerCase().endsWith('.mp4')) {
                        mediaEl = document.createElement('video');
                        mediaEl.className = 'ad-media';
                        mediaEl.autoplay = true;
                        mediaEl.muted = true;
                        mediaEl.loop = true;
                        mediaEl.src = fullPath;
                    } else {
                        mediaEl = document.createElement('img');
                        mediaEl.className = 'ad-media';
                        mediaEl.src = fullPath;
                    }

                    container.appendChild(mediaEl);
                    document.getElementById('SIDE_TITLE').innerText = d.title;

                    // 链接兜底逻辑
                    let finalLink = d.linkUrl || fullPath;

                    if (finalLink && finalLink !== "#") {
                        document.getElementById('SIDE_LINK').href = finalLink;
                    } else {
                        document.getElementById('SIDE_LINK').removeAttribute('href');
                    }

                    document.getElementById('SIDE_AD').style.display = "block";
                }
            }).catch(e => console.log("Sidebar Ad failed:", e));
    })();
</script>