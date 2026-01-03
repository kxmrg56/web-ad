<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>后台管理</title>
    <link rel="stylesheet" href="assets/css/style.css">
    <style>input, select, textarea { width: 100%; padding: 10px; background: #333; border: 1px solid #444; color: #fff; margin-bottom: 15px; }</style>
</head>
<body>
<nav class="navbar"><a href="index" class="logo">后台管理</a></nav>
<div class="container" style="max-width: 600px;">
    <h3>上传视频 / 广告</h3>
    <form action="upload" method="post" enctype="multipart/form-data">
        <label>类型:</label>
        <select name="type" onchange="toggle(this.value)">
            <option value="video">普通视频</option><option value="ad">插播广告</option>
        </select>
        <div id="videoFields">
            <input type="text" name="title" placeholder="标题">
            <textarea name="description" placeholder="描述"></textarea>
            <select name="category">
                <option value="电影">电影</option><option value="综艺">综艺</option>
                <option value="游戏">游戏</option><option value="科技">科技</option>
                <option value="体育">体育</option><option value="美食">美食</option>
                <option value="教育">教育</option><option value="旅游">旅游</option>
            </select>
        </div>
        <div id="adFields" style="display:none;">
            <input type="number" name="videoId" placeholder="关联视频ID (例如: 1)">
            <input type="number" name="insertTime" placeholder="插入时间 (秒)">
        </div>
        <input type="file" name="file" required>
        <button type="submit" class="btn">开始上传</button>
    </form>
</div>
<script>function toggle(v){document.getElementById('videoFields').style.display=v==='video'?'block':'none';document.getElementById('adFields').style.display=v==='ad'?'block':'none';}</script>
</body>
</html>