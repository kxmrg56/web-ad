<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>业主登录</title></head>
<body>
<h2>广告业主登录后台</h2>
<form action="${pageContext.request.contextPath}/ads/owner/login" method="post">
    用户名: <input name="username" type="text" required><br>
    密 码: <input name="password" type="password" required><br>
    <button type="submit">登录系统</button>
</form>
<c:if test="${param.error == '1'}">
    <p style="color:red">用户名或密码错误！</p>
</c:if>
</body>
</html>