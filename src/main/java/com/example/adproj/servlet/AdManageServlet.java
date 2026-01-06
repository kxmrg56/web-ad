package com.example.adproj.servlet;

import com.example.adproj.entity.AdContent;
import com.example.adproj.service.AdContentService;
import com.example.adproj.util.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/ads/manage/*")
public class AdManageServlet extends HttpServlet {

    private final AdContentService adService = new AdContentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo(); // 获取类似 "/list" 或 "/delete" 的路径

        // 处理删除请求
        if ("/delete".equals(pathInfo)) {
            handleDelete(request, response);
            return;
        }

        // 如果路径是空的或者是根路径，我们也默认展示列表
        if (pathInfo == null || "/list".equals(pathInfo) || "/".equals(pathInfo)) {

            // 1. 从 Session 获取当前登录的业主 ID
            HttpSession session = request.getSession();
            Integer ownerId = (Integer) session.getAttribute("ownerId");

            if (ownerId != null) {
                // 2. 【核心修复】：不要赋值为 null，要 new 一个具体的对象
                AdContentService adContentService = new AdContentService();

                // 调用 Service 查询该业主的所有广告（那 75 条数据）
                List<AdContent> ads = adContentService.getAdsByOwner(ownerId);

                // 3. 将广告列表存入 Request 作用域，JSP 里的 ${ads} 才能拿到数据
                request.setAttribute("ads", ads);

                // 4. 转发到 dashboard.jsp 进行渲染
                request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
            } else {
                // 没登录则跳回登录页
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            }
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        Integer ownerId = (Integer) session.getAttribute("ownerId");

        if (ownerId == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            int adId = Integer.parseInt(request.getParameter("id"));

            // 使用 Service 删除
            boolean deleted = adService.deleteAd(adId, ownerId);

            if (deleted) {
                response.sendRedirect(request.getContextPath() + "/ads/manage/list?msg=deleted");
            } else {
                response.sendRedirect(request.getContextPath() + "/ads/manage/list?error=delete_failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/ads/manage/list?error=invalid_id");
        }
    }

    // 临时数据库删除方法
    private boolean deleteAdFromDatabase(int adId) {
        String sql = "DELETE FROM ad_material WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, adId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        HttpSession session = req.getSession();
        Integer ownerId = (Integer) session.getAttribute("ownerId");

        // 鉴权
        if (ownerId == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        if ("/add".equals(pathInfo)) {
            // 1. 获取表单数据
            String title = req.getParameter("title");
            String image = req.getParameter("image");
            String link = req.getParameter("link");
            String category = req.getParameter("category");

            // 2. 调用 Service 写入数据库
            adService.addAd(title, image, link, category, ownerId);

            // 3. 新增成功后，重定向到列表页刷新显示
            resp.sendRedirect(req.getContextPath() + "/ads/manage/list");

        } else if ("/update".equals(pathInfo)) {
            // 修改逻辑
            try {
                int adId = Integer.parseInt(req.getParameter("id"));
                String title = req.getParameter("title");
                String image = req.getParameter("image");
                String link = req.getParameter("link");
                String category = req.getParameter("category");

                adService.updateAd(adId, title, image, link, category, ownerId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            resp.sendRedirect(req.getContextPath() + "/ads/manage/list");
        }
    }


}