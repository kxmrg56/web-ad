package com.video.servlet;

import com.video.dao.VideoDao;
import com.video.model.Video;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/admin")
@MultipartConfig(maxFileSize = 1024 * 1024 * 500)
public class AdminServlet extends HttpServlet {
    private static final String UPLOAD_DIR = System.getProperty("os.name").toLowerCase().contains("win")
            ? "D:/video_server_storage" : "/var/www/video_storage";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 加载视频列表供管理表格使用
        req.setAttribute("videoList", new VideoDao().getAllVideos());
        req.getRequestDispatcher("/WEB-INF/views/admin.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        VideoDao dao = new VideoDao();

        if ("delete".equals(action)) {
            // 处理删除逻辑
            int id = Integer.parseInt(req.getParameter("id"));
            dao.deleteVideo(id);
        } else if ("upload".equals(action)) {
            // 处理上传逻辑
            String type = req.getParameter("type");
            Part filePart = req.getPart("file");

            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) dir.mkdirs();

            String fileName = UUID.randomUUID().toString() + ".mp4";
            String fullPath = UPLOAD_DIR + File.separator + fileName;
            filePart.write(fullPath);

            if ("video".equals(type)) {
                Video v = new Video();
                v.setTitle(req.getParameter("title"));
                v.setDescription(req.getParameter("description"));
                v.setCategory(req.getParameter("category"));
                v.setFilePath(fullPath);
                dao.addVideo(v);
            } else {
                int videoId = Integer.parseInt(req.getParameter("videoId"));
                int insertTime = Integer.parseInt(req.getParameter("insertTime"));
                dao.addAdConfig(videoId, fullPath, insertTime);
            }
        }
        resp.sendRedirect("admin");
    }
}