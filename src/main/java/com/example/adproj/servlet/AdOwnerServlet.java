package com.example.adproj.servlet;

import com.example.adproj.service.AdOwnerService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/ads/owner/*") // 匹配 /ads/owner/login 和 /ads/owner/register
public class AdOwnerServlet extends HttpServlet {

    private final AdOwnerService ownerService = new AdOwnerService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();

        if ("/login".equals(pathInfo)) {
            handleLogin(request, response);
        } else if ("/register".equals(pathInfo)) {
            // 1. 获取注册参数
            String user = request.getParameter("username");
            String pass = request.getParameter("password");
            String company = request.getParameter("company");

            // 2. 调用 Service 写入数据库
            boolean success = ownerService.register(user, pass, company);

            // 3. 根据结果重定向 (不要返回JSON)
            if (success) {
                // 注册成功，跳到登录页面
                response.sendRedirect(request.getContextPath() + "/login.jsp?msg=reg_ok");
            } else {
                // 注册失败，跳回注册页并报错
                response.sendRedirect(request.getContextPath() + "/register.jsp?error=fail");
            }
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String user = request.getParameter("username");
        String pass = request.getParameter("password");

        Integer ownerId = ownerService.login(user, pass);
        if (ownerId != null) {
            HttpSession session = request.getSession();
            session.setAttribute("ownerId", ownerId);
            session.setAttribute("username", user);

            // 重定向到 AdManageServlet 处理列表显示
            response.sendRedirect(request.getContextPath() + "/ads/manage/list");
        } else {
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=1");
        }
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String user = request.getParameter("username");
        String pass = request.getParameter("password");
        String company = request.getParameter("company");

        boolean ok = ownerService.register(user, pass, company);
        if (ok) {
            response.getWriter().write("{\"success\":true, \"message\":\"注册成功，请登录\"}");
        } else {
            response.getWriter().write("{\"success\":false, \"message\":\"注册失败，用户名可能已存在\"}");
        }
    }
}