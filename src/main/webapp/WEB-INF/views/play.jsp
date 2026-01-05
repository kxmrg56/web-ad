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
        /* 新增点击层 */
        #adClickLayer {
            position: absolute; top: 0; left: 0; width: 100%; height: 100%;
            z-index: 5; cursor: pointer; display: none; background: rgba(0,0,0,0);
        }
    </style>
</head>
<body>
<nav class="navbar"><a href="index" class="logo">VideoSpace</a></nav>
<div class="container" style="display: flex; gap: 30px;">
    <div style="flex: 3;">
        <div class="player-box">
            <div id="adOverlay" class="ad-overlay">广告剩余 <span id="adTimer">--</span> 秒</div>
            <div id="adClickLayer"></div>
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
        const SERVER_ROOT = "http://10.100.164.33:8080/adproj-1.0-SNAPSHOT";
        const LOCAL_SRC = "stream?path=${video.encodedPath}";
        const CAT_MAP = {'综艺':'时尚', '科技':'数码', '电影':'电影', '游戏':'游戏', '体育':'体育'};

        let channel = CAT_MAP["${video.category}"] || "电影";
        let vid = (document.cookie.match(/(^| )visitor_id=([^;]+)/) || [])[2] || "";

        const player = document.getElementById("mainPlayer");
        const overlay = document.getElementById("adOverlay");
        const timerSpan = document.getElementById("adTimer");
        const clickLayer = document.getElementById("adClickLayer");

        let adInfo = { hasAd: false, url: "", time: 10, jumpUrl: "#" };
        let status = { played: false, playing: false, savedTime: 0 };

        let api = SERVER_ROOT + "/ads/api/getAd?siteType=video&channel=" + encodeURIComponent(channel) + "&visitorId=" + vid;

        fetch(api, {credentials: 'include'})
            .then(r => r.json())
            .then(d => {
                if(d && d.image && d.image.toLowerCase().endsWith(".mp4")) {
                    let fullPath = d.image.startsWith('http') ? d.image : encodeURI(SERVER_ROOT + (d.image.startsWith('/') ? d.image : "/" + d.image));

                    adInfo.hasAd = true;
                    adInfo.url = fullPath;
                    // ★★★ 跳转到 AdPlayServlet ★★★
                    adInfo.jumpUrl = "playAd?url=" + encodeURIComponent(fullPath) + "&title=" + encodeURIComponent(d.title) + "&link=" + encodeURIComponent(d.link || "#");
                }
            });

        player.addEventListener("timeupdate", () => {
            if(!adInfo.hasAd || status.played || status.playing) return;
            if(player.currentTime >= adInfo.time) startAd();
        });

        function startAd() {
            player.pause();
            status.savedTime = player.currentTime;
            status.playing = true;
            status.played = true;

            player.src = adInfo.url;
            player.controls = false;
            player.muted = false;
            overlay.style.display = "block";
            clickLayer.style.display = "block";

            clickLayer.onclick = () => { window.open(adInfo.jumpUrl, "_blank"); };

            player.play();

            let t = setInterval(() => {
                if(!status.playing) clearInterval(t);
                timerSpan.innerText = Math.round(player.duration - player.currentTime) || "--";
            }, 500);

            const endAd = () => {
                clearInterval(t);
                status.playing = false;
                overlay.style.display = "none";
                clickLayer.style.display = "none";
                player.src = LOCAL_SRC;
                player.onloadedmetadata = () => {
                    player.currentTime = status.savedTime;
                    player.controls = true;
                    player.play();
                    player.onloadedmetadata = null;
                };
            };
            player.onended = endAd;
            player.onerror = endAd;
        }
    })();
</script>
</body>
</html>