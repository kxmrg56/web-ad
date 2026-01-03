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

@WebServlet("/upload")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 500,
        maxRequestSize = 1024 * 1024 * 510
)
public class VideoUploadServlet extends HttpServlet {
    // ★★★ 自动判断系统，解决 Linux 下找不到 D 盘报错的问题 ★★★
    private static final String UPLOAD_DIR = System.getProperty("os.name").toLowerCase().contains("win")
            ? "D:/video_server_storage"      // 本地测试用
            : "/var/www/video_storage";      // Linux 服务器用

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        // 1. 确保目录存在
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        // 2. 接收文件
        Part filePart = req.getPart("file");
        String fileName = UUID.randomUUID().toString() + ".mp4";
        String fullPath = UPLOAD_DIR + File.separator + fileName;

        // 写入硬盘
        filePart.write(fullPath);

        // 3. 写入数据库
        VideoDao dao = new VideoDao();
        Video v = new Video();
        v.setTitle(req.getParameter("title"));
        v.setDescription(req.getParameter("description"));
        v.setCategory(req.getParameter("category"));
        v.setFilePath(fullPath); // 存入绝对路径
        dao.addVideo(v);

        resp.sendRedirect("index");
    }
}