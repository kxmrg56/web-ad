<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>VideoSpace Home</title>
    <link rel="stylesheet" href="assets/css/style.css">
</head>
<body>
<nav class="navbar">
    <a href="index" class="logo">VideoSpace</a>
    <div><a href="admin" class="btn">上传/管理</a></div>
</nav>
<div class="category-bar">
    <a href="index" class="${currentCategory == 'all' || currentCategory == null ? 'active' : ''}">全部</a>
    <a href="index?category=电影" class="${currentCategory == '电影' ? 'active' : ''}">电影</a>
    <a href="index?category=综艺" class="${currentCategory == '综艺' ? 'active' : ''}">综艺</a>
    <a href="index?category=游戏" class="${currentCategory == '游戏' ? 'active' : ''}">游戏</a>
    <a href="index?category=科技" class="${currentCategory == '科技' ? 'active' : ''}">科技</a>
    <a href="index?category=体育" class="${currentCategory == '体育' ? 'active' : ''}">体育</a>
    <a href="index?category=美食" class="${currentCategory == '美食' ? 'active' : ''}">美食</a>
    <a href="index?category=教育" class="${currentCategory == '教育' ? 'active' : ''}">教育</a>
</div>

<div class="container">
    <h2>${currentCategory == 'all' || currentCategory == null ? '最新推荐' : currentCategory}</h2>
    <div class="layout-wrapper" style="display: flex; gap: 30px; align-items: flex-start;">
        <div class="main-content" style="flex: 3;">
            <div class="video-grid">
                <c:forEach items="${videoList}" var="v">
                    <div class="video-card" onclick="location.href='play?id=${v.id}'">
                        <div style="height:150px; background:#333; display:flex; align-items:center; justify-content:center;">
                            <span style="font-size:30px; color:#555;">▶</span>
                        </div>
                        <div class="card-content">
                            <h3 class="card-title">${v.title}</h3>
                            <span class="card-cat">${v.category}</span>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
        <aside class="sidebar" style="flex: 1; min-width: 260px; position: sticky; top: 80px;">
            <!-- 引入侧边栏广告组件 -->
            <jsp:include page="/WEB-INF/views/inc/ad_component.jsp"/>
        </aside>
    </div>
</div>

<script>
    // 自动生成测试ID，确保广告系统能识别到用户
    if (!document.cookie.match(/visitor_id=/)) {
        let rid = "user_" + Math.floor(Math.random() * 9999);
        document.cookie = "visitor_id=" + rid + "; path=/; max-age=86400";
    }
</script>
</body>
</html>