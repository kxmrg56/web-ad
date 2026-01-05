<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>后台管理</title>
    <link rel="stylesheet" href="assets/css/style.css">
    <style>
        input, select, textarea { width: 100%; padding: 10px; background: #333; border: 1px solid #444; color: #fff; margin-bottom: 15px; }
        .mgr-table { width: 100%; border-collapse: collapse; margin-top: 30px; background: #1f1f1f; }
        .mgr-table th, .mgr-table td { padding: 12px; border: 1px solid #333; text-align: left; }
        .btn-del { background: #e50914; color: white; border: none; padding: 5px 10px; cursor: pointer; border-radius: 3px; }
    </style>
</head>
<body>
<nav class="navbar"><a href="index" class="logo">后台管理</a></nav>
<div class="container" style="max-width: 800px;">
    <h3>上传视频 / 广告</h3>
    <!-- 提交给 AdminServlet -->
    <form action="admin" method="post" enctype="multipart/form-data">
        <input type="hidden" name="action" value="upload">
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
                <option value="体育">体育</option>
            </select>
        </div>
        <div id="adFields" style="display:none;">
            <input type="number" name="videoId" placeholder="关联视频ID (例如: 1)">
            <input type="number" name="insertTime" placeholder="插入时间 (秒)">
        </div>
        <input type="file" name="file" required>
        <button type="submit" class="btn">开始上传</button>
    </form>

    <h3>视频库管理</h3>
    <table class="mgr-table">
        <thead>
        <tr><th>ID</th><th>标题</th><th>分类</th><th>操作</th></tr>
        </thead>
        <tbody>
        <c:forEach items="${videoList}" var="v">
            <tr>
                <td>${v.id}</td>
                <td>${v.title}</td>
                <td>${v.category}</td>
                <td>
                    <form action="admin" method="post" onsubmit="return confirm('确定要删除吗？')" style="display:inline;">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value="${v.id}">
                        <button type="submit" class="btn-del">删除</button>
                    </form>
                    <a href="play?id=${v.id}" target="_blank" style="color:#888; font-size:12px; margin-left:10px;">预览</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<script>
    function toggle(v){
        document.getElementById('videoFields').style.display=v==='video'?'block':'none';
        document.getElementById('adFields').style.display=v==='ad'?'block':'none';
    }
</script>
</body>
</html>