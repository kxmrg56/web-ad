<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>管理大盘 - MVC版</title>
    <style>table{border-collapse:collapse;} th,td{padding:8px;}</style>
</head>
<body>
<h2>欢迎回来: ${sessionScope.username}</h2>
<a href="${pageContext.request.contextPath}/index.jsp">返回首页</a>

<h3>我的广告资产</h3>
<table border="1" style="width: 100%; border-collapse: collapse; text-align: center;">
    <tr style="background-color: #f2f2f2;">
        <th>广告标题</th>
        <th>所属分类</th>
        <th>图片预览</th> <th>跳转链接</th>
        <th>操作</th>
    </tr>
    <c:forEach items="${ads}" var="ad">
        <tr>
            <td>${ad.title}</td>
            <td><span style="color: blue;">[${ad.category}]</span></td>
            <td>
                <img src="${pageContext.request.contextPath}/${ad.imageUrl}"
                     alt="预览图"
                     style="width: 100px; height: 60px; object-fit: cover; border: 1px solid #ddd; padding: 2px;">
            </td>
            <td><a href="${ad.linkUrl}" target="_blank">查看链接</a></td>
            <td>
                <a href="delete?id=${ad.id}" onclick="return confirm('确定删除吗？')">删除</a>
            </td>
        </tr>
    </c:forEach>
</table>

<hr>
<h3>发布新广告</h3>
<form action="${pageContext.request.contextPath}/ads/manage/add" method="post">
    标题: <input name="title" required><br>
    分类: <input name="category" placeholder="gaming/education" required><br>
    图片: <input name="image" placeholder="http://..."><br>
    链接: <input name="link" placeholder="http://..."><br>
    <button type="submit">立即发布</button>
</form>
</body>
</html>