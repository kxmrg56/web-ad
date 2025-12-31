<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>业主注册</title></head>
<body>
<h2>新广告业主注册</h2>
<form action="${pageContext.request.contextPath}/ads/owner/register" method="post">
    用户名: <input name="username" type="text" required><br><br>
    密  码: <input name="password" type="password" required><br><br>
    公司名: <input name="company" type="text" required><br><br>
    <button type="submit">提交注册</button>
</form>
<p><a href="login.jsp">已有账号？去登录</a></p>
</body>
</html>