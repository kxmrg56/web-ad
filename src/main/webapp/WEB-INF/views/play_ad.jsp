<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>推荐内容 - <%= request.getParameter("title") %></title>
    <style>
        body, html { margin: 0; padding: 0; height: 100%; background: #050505; font-family: sans-serif; color: white; overflow: hidden; }
        .bg-blur { position: absolute; top: 0; left: 0; width: 100%; height: 100%; background: #111; z-index: -1; }

        .container { display: flex; flex-direction: column; align-items: center; justify-content: center; height: 100vh; padding: 20px; box-sizing: border-box; }

        .video-wrapper { width: 100%; max-width: 1000px; box-shadow: 0 20px 50px rgba(0,0,0,0.8); border-radius: 8px; overflow: hidden; background: #000; border: 1px solid #333; }
        video { width: 100%; display: block; outline: none; }

        .info-panel { width: 100%; max-width: 1000px; margin-top: 25px; display: flex; justify-content: space-between; align-items: center; }
        h1 { margin: 0; font-size: 24px; color: #eee; }
        .badge { background: #e50914; color: white; padding: 2px 8px; border-radius: 3px; font-size: 12px; margin-bottom: 5px; display: inline-block; }

        .back-nav { position: absolute; top: 30px; left: 40px; }
        .back-nav a { color: #aaa; text-decoration: none; font-size: 16px; font-weight: bold; transition: 0.3s; }
        .back-nav a:hover { color: #fff; }

        .btn-link { background: #fff; color: #000; padding: 10px 20px; text-decoration: none; border-radius: 4px; font-weight: bold; font-size: 14px; }
        .btn-link:hover { background: #ccc; }
    </style>
</head>
<body>
<div class="bg-blur"></div>
<div class="back-nav"><a href="javascript:history.back()">← 返回播放</a></div>

<div class="container">
    <div class="video-wrapper">
        <video src="<%= request.getParameter("url") %>" controls autoplay></video>
    </div>
    <div class="info-panel">
        <div>
            <span class="badge">Sponsored</span>
            <h1><%= request.getParameter("title") %></h1>
        </div>
        <% if(request.getParameter("link") != null && !request.getParameter("link").equals("#")) { %>
        <a href="<%= request.getParameter("link") %>" target="_blank" class="btn-link">了解详情 ↗</a>
        <% } %>
    </div>
</div>
</body>
</html>