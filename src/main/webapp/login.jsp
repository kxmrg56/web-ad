<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>业主登录 - 广告投放管理系统</title>
    <style>
        * { box-sizing: border-box; }
        body {
            font-family: 'Segoe UI', Arial, sans-serif;
            margin: 0;
            background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
            height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .login-container {
            background: white;
            padding: 40px;
            border-radius: 12px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.1);
            width: 100%;
            max-width: 400px;
        }
        h2 {
            color: #2c3e50;
            margin-top: 0;
            margin-bottom: 25px;
            text-align: center;
            font-size: 24px;
        }
        .form-group {
            margin-bottom: 20px;
        }
        .form-label {
            display: block;
            margin-bottom: 8px;
            color: #555;
            font-size: 14px;
            font-weight: 600;
        }
        .form-control {
            width: 100%;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 6px;
            font-size: 14px;
            transition: all 0.3s ease;
            outline: none;
        }
        .form-control:focus {
            border-color: #007bff;
            box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.1);
        }
        .btn-login {
            width: 100%;
            padding: 13px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 6px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: background-color 0.3s ease, transform 0.2s ease;
            margin-top: 10px;
        }
        .btn-login:hover {
            background-color: #0056b3;
            transform: translateY(-1px);
        }
        .footer-links {
            margin-top: 25px;
            text-align: center;
            font-size: 14px;
            color: #666;
        }
        .footer-links a {
            color: #007bff;
            text-decoration: none;
            font-weight: 500;
        }
        .footer-links a:hover {
            text-decoration: underline;
        }
        /* 错误消息样式 */
        .alert-error {
            background-color: #f8d7da;
            color: #721c24;
            padding: 10px;
            border-radius: 6px;
            margin-bottom: 20px;
            font-size: 13px;
            border: 1px solid #f5c6cb;
            text-align: center;
        }
    </style>
</head>
<body>

<div class="login-container">
    <h2>广告业主登录</h2>

    <%-- 显示登录失败的错误提示 --%>
    <% if (request.getAttribute("error") != null) { %>
    <div class="alert-error">
        <%= request.getAttribute("error") %>
    </div>
    <% } %>

    <form action="${pageContext.request.contextPath}/ads/owner/login" method="post">
        <div class="form-group">
            <label class="form-label">用户名</label>
            <input name="username" type="text" class="form-control"
                   placeholder="请输入您的用户名" required autofocus>
        </div>

        <div class="form-group">
            <label class="form-label">密码</label>
            <input name="password" type="password" class="form-control"
                   placeholder="请输入密码" required>
        </div>

        <button type="submit" class="btn-login">登录系统</button>
    </form>

    <div class="footer-links">
        还没有账号？<a href="register.jsp">立即入驻</a>
        <br><br>
        <a href="index.jsp" style="color: #999; font-size: 12px;">返回系统首页</a>
    </div>
</div>

</body>
</html>