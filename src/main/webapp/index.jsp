<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>广告系统首页</title></head>
<body>
<h1>欢迎来到广告投放管理系统</h1>
<hr>
<a href="register.jsp">业主入驻（注册账号）</a> |
<a href="login.jsp">业主登录后台</a>
<hr>
<h3>模拟终端用户看广告</h3>
<a href="${pageContext.request.contextPath}/ads/api/getAd?siteType=news">访问新闻站</a>
</body>
</html>