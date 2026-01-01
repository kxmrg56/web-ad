<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>播放 - ${video.title}</title>
    <link rel="stylesheet" href="assets/css/style.css">
    <style>
        .player-box { max-width: 1000px; margin: 0 auto; position: relative; background: #000; }
        video { width: 100%; display: block; }
        .ad-marker { position: absolute; top: 20px; right: 20px; background: #e50914; color: #fff; padding: 5px 10px; display: none; z-index: 10; font-weight: bold;}
    </style>
</head>
<body>
<nav class="navbar"><a href="index" class="logo">VideoSpace</a></nav>
<div class="container" style="display: flex; gap: 30px;">
    <div style="flex: 3;">
        <div class="player-box">
            <div id="adBadge" class="ad-marker">广告时间</div>
            <video id="mainPlayer" controls autoplay>
                <source src="stream?path=${video.encodedPath}" type="video/mp4">
            </video>
        </div>
        <h1>${video.title}</h1>
        <p>${video.description}</p>
    </div>
    <aside style="flex: 1;">
        <jsp:include page="/WEB-INF/views/inc/ad_component.jsp"/>
    </aside>
</div>
<script>
    const config = {
        hasAd: ${video.hasAd ? 'true' : 'false'},
        adPath: "${video.adEncodedPath}",
        insertTime: ${video.adInsertTime},
        mainPath: "${video.encodedPath}"
    };
    const player = document.getElementById("mainPlayer");
    let adPlayed = false, savedTime = 0;

    player.addEventListener("timeupdate", function() {
        if(config.hasAd && !adPlayed && Math.abs(player.currentTime - config.insertTime) < 1) {
            playAd();
        }
    });
    function playAd() {
        player.pause(); savedTime = player.currentTime; adPlayed = true;
        player.src = "stream?path=" + config.adPath;
        player.controls = false;
        document.getElementById("adBadge").style.display = "block";
        player.play();
        player.onended = function() {
            player.src = "stream?path=" + config.mainPath;
            player.onloadedmetadata = function() {
                player.currentTime = savedTime + 1;
                player.controls = true;
                document.getElementById("adBadge").style.display = "none";
                player.play();
                player.onended = null;
            }
        }
    }
</script>
</body>
</html>