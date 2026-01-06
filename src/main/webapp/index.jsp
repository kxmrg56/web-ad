<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>广告投放管理系统 - 首页</title>
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
        .container {
            background: white;
            padding: 40px;
            border-radius: 12px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.1);
            text-align: center;
            max-width: 500px;
            width: 90%;
        }
        h1 {
            color: #2c3e50;
            margin-bottom: 10px;
            font-size: 24px;
        }
        p {
            color: #7f8c8d;
            margin-bottom: 30px;
        }
        .button-group {
            display: flex;
            flex-direction: column;
            gap: 15px;
        }
        .btn {
            display: block;
            padding: 15px;
            text-decoration: none;
            border-radius: 8px;
            font-weight: 600;
            transition: all 0.3s ease;
        }
        .btn-login {
            background-color: #007bff;
            color: white;
        }
        .btn-login:hover {
            background-color: #0056b3;
            transform: translateY(-2px);
        }
        .btn-reg {
            background-color: transparent;
            color: #007bff;
            border: 2px solid #007bff;
        }
        .btn-reg:hover {
            background-color: #f0f7ff;
            transform: translateY(-2px);
        }
        .footer {
            margin-top: 30px;
            font-size: 12px;
            color: #bdc3c7;
        }
    </style>
</head>
<body>

<div class="container">
    <h1>广告投放管理系统</h1>
    <p>提供一站式广告位管理与收益监控服务</p>

    <div class="button-group">
        <a href="login.jsp" class="btn btn-login">业主登录后台</a>
        <a href="register.jsp" class="btn btn-reg">业主入驻（注册账号）</a>
    </div>

    <div class="footer">
        &copy; 2026 广告商管理系统 · 版权所有
    </div>
</div>

</body>
</html>