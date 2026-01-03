<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>播放 - ${video.title}</title>
    <link rel="stylesheet" href="assets/css/style.css">
    <style>
        .player-box { max-width: 1000px; margin: 0 auto; position: relative; background: #000; }
        video { width: 100%; display: block; }
        .ad-overlay {
            position: absolute; top: 20px; right: 20px;
            background: rgba(229, 9, 20, 0.9); color: #fff;
            padding: 8px 15px; border-radius: 4px;
            display: none; z-index: 10; font-weight: bold;
        }
    </style>
</head>
<body>
<nav class="navbar"><a href="index" class="logo">VideoSpace</a></nav>
<div class="container" style="display: flex; gap: 30px;">
    <div style="flex: 3;">
        <div class="player-box">
            <div id="adOverlay" class="ad-overlay">广告剩余 <span id="adTimer">--</span> 秒</div>
            <video id="mainPlayer" controls autoplay>
                <source src="stream?path=${video.encodedPath}" type="video/mp4">
            </video>
        </div>
        <h1>${video.title}</h1>
    </div>
    <aside style="flex: 1;">
        <jsp:include page="/WEB-INF/views/inc/ad_component.jsp"/>
    </aside>
</div>

<script>
    (function() {
        // ★★★ 队友的文档配置 ★★★
        const SERVER_ROOT = "http://10.100.164.33:8080/adproj-1.0-SNAPSHOT";
        const LOCAL_SRC = "stream?path=${video.encodedPath}";

        // 映射逻辑
        const CAT_MAP = {'综艺':'时尚', '科技':'数码', '电影':'电影', '游戏':'游戏', '体育':'体育'};
        let currentCat = "${video.category}";
        let channel = CAT_MAP[currentCat] || "电影";

        // 获取 Visitor ID
        let vid = (document.cookie.match(/(^| )visitor_id=([^;]+)/) || [])[2] || "";

        const player = document.getElementById("mainPlayer");
        const overlay = document.getElementById("adOverlay");
        const timerSpan = document.getElementById("adTimer");

        let adInfo = { hasAd: false, url: "", time: 10 };
        let status = { played: false, playing: false, savedTime: 0 };

        // ★★★ 严格按照文档发起请求 ★★★
        let api = SERVER_ROOT + "/ads/api/getAd?siteType=video&channel=" + encodeURIComponent(channel) + "&visitorId=" + vid;

        console.log("[Player] 请求广告:", api);

        fetch(api, {credentials: 'include'})
            .then(r => r.json())
            .then(d => {
                // ★★★ 文档要求：必须检查是否为 mp4 ★★★
                if(d && d.image) {
                    let fullPath = d.image.startsWith('http') ? d.image : (SERVER_ROOT + "/" + d.image);

                    // 逻辑判断：如果是视频，才进行插播
                    if (fullPath.endsWith(".mp4")) {
                        console.log("[Player] 获取到视频广告:", fullPath);
                        adInfo.hasAd = true;
                        adInfo.url = fullPath;
                    } else {
                        console.warn("[Player] 队友返回的是图片/其他格式，跳过插播:", fullPath);
                        // 你可以在这里开启本地演示模式（如果需要的话）
                    }
                }
            }).catch(e => console.warn("广告请求失败:", e));

        // 监听播放
        player.addEventListener("timeupdate", () => {
            if(!adInfo.hasAd || status.played || status.playing) return;
            // 误差 0.5 秒内触发
            if(Math.abs(player.currentTime - adInfo.time) < 0.5) startAd();
        });

        function startAd() {
            player.pause();
            status.savedTime = player.currentTime;
            status.playing = true;
            status.played = true;

            player.src = adInfo.url;
            player.controls = false;
            player.muted = false; // 插播时取消静音
            overlay.style.display = "block";
            player.play();

            let t = setInterval(() => {
                if(!status.playing) clearInterval(t);
                timerSpan.innerText = Math.round(player.duration - player.currentTime) || "--";
            }, 500);

            const endAd = () => {
                clearInterval(t);
                status.playing = false;
                overlay.style.display = "none";
                player.src = LOCAL_SRC;
                player.onloadedmetadata = () => {
                    player.currentTime = status.savedTime + 1;
                    player.controls = true;
                    player.play();
                    player.onloadedmetadata = null;
                };
            };

            player.onended = endAd;
            player.onerror = () => { console.error("广告播放出错"); endAd(); };
        }
    })();
</script>
</body>
</html>