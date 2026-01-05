<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>VideoSpace Home</title>
    <link rel="stylesheet" href="assets/css/style.css">
</head>
<body>
<nav class="navbar"><a href="index" class="logo">VideoSpace</a><div><a href="admin" class="btn">管理后台</a></div></nav>
<div class="category-bar">
    <a href="index" class="${currentCategory == 'all' ? 'active' : ''}">全部</a>
    <c:forEach items="${['电影','综艺','游戏','科技','体育']}" var="cat">
        <a href="index?category=${cat}" class="${currentCategory == cat ? 'active' : ''}">${cat}</a>
    </c:forEach>
</div>
<div class="container" style="display: flex; gap: 20px;">
    <div style="flex: 3;" class="video-grid">
        <c:forEach items="${videoList}" var="v">
            <div class="video-card" onclick="location.href='play?id=${v.id}'">
                <div class="thumb">▶</div>
                <div class="card-content"><h3>${v.title}</h3><span>${v.category}</span></div>
            </div>
        </c:forEach>
    </div>
    <aside style="flex: 1;"><jsp:include page="/WEB-INF/views/inc/ad_component.jsp"/></aside>
</div>

</body>
</html>