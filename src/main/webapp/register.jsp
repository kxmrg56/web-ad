<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>业主注册 - 广告投放管理系统</title>
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
        .register-container {
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
            margin-bottom: 18px;
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
        .btn-register {
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
        .btn-register:hover {
            background-color: #0056b3;
            transform: translateY(-1px);
        }
        .btn-register:active {
            transform: translateY(0);
        }
        .footer-links {
            margin-top: 20px;
            text-align: center;
            font-size: 14px;
        }
        .footer-links a {
            color: #007bff;
            text-decoration: none;
            font-weight: 500;
        }
        .footer-links a:hover {
            text-decoration: underline;
        }
        /* 错误提示样式 */
        .error-msg {
            color: #dc3545;
            background: #f8d7da;
            padding: 10px;
            border-radius: 4px;
            margin-bottom: 15px;
            font-size: 13px;
            text-align: center;
            display: none; /* 默认隐藏 */
        }
    </style>
</head>
<body>

<div class="register-container">
    <h2>新广告业主入驻</h2>

    <%-- 如果有错误消息，可以通过 request 分发显示 --%>
    <% if (request.getAttribute("error") != null) { %>
    <div class="error-msg" style="display: block;">
        <%= request.getAttribute("error") %>
    </div>
    <% } %>

    <form action="${pageContext.request.contextPath}/ads/owner/register" method="post">
        <div class="form-group">
            <label class="form-label">用户名</label>
            <input name="username" type="text" class="form-control" placeholder="设置登录账号" required>
        </div>

        <div class="form-group">
            <label class="form-label">登录密码</label>
            <input name="password" type="password" class="form-control" placeholder="设置 6 位以上密码" required>
        </div>

        <div class="form-group">
            <label class="form-label">公司名称</label>
            <input name="company" type="text" class="form-control" placeholder="您的企业或组织全称" required>
        </div>

        <button type="submit" class="btn-register">提交注册</button>
    </form>

    <div class="footer-links">
        <a href="login.jsp">已有账号？去登录</a>
        <br><br>
        <a href="index.jsp" style="color: #6c757d; font-size: 12px;">返回首页</a>
    </div>
</div>

</body>
</html>