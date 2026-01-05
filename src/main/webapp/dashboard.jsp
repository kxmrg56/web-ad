<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>管理大盘 - 广告资产管理</title>
    <style>
        table { border-collapse: collapse; width: 100%; text-align: center; }
        th, td { padding: 8px; border: 1px solid #ddd; }
        th { background-color: #f2f2f2; }
        .preview-img { width: 100px; height: 60px; object-fit: cover; border: 1px solid #ddd; padding: 2px; }
        .category-tag { color: blue; font-weight: bold; }
    </style>
</head>
<body>
<h2>欢迎回来: ${sessionScope.username}</h2>
<a href="${pageContext.request.contextPath}/index.jsp">返回首页</a>

<h3>我的广告资产</h3>
<table>
    <thead>
    <tr>
        <th>广告标题</th>
        <th>所属分类</th>
        <th>图片预览</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${ads}" var="ad">
        <tr>
            <td>${ad.title}</td>
            <td><span class="category-tag">[${ad.category}]</span></td>
            <td>
                <img src="${pageContext.request.contextPath}/${ad.imageUrl}"
                     alt="预览图" class="preview-img">
            </td>
            <td>
                <a href="delete?id=${ad.id}" onclick="return confirm('确定删除吗？')">删除</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<hr>
<h3>发布新广告</h3>
<form action="${pageContext.request.contextPath}/ads/manage/add" method="post">
    <div>标题: <input name="title" required></div>
    <div>分类: <input name="category" placeholder="例如：日用文创" required></div>
    <div>图片路径: <input name="image" placeholder="例如：videos/电影/movies.mp4" required></div>
    <button type="submit" style="margin-top: 10px;">立即发布</button>
</form>
</body>
</html>