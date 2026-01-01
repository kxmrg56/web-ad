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
@MultipartConfig(fileSizeThreshold = 1024*1024*2, maxFileSize = 1024*1024*500, maxRequestSize = 1024*1024*510)
public class VideoUploadServlet extends HttpServlet {
    private static final String UPLOAD_DIR = "D:/video_server_storage";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String type = req.getParameter("type");
        Part filePart = req.getPart("file");
        String fileName = UUID.randomUUID().toString() + ".mp4";

        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) dir.mkdirs();
        String fullPath = UPLOAD_DIR + "/" + fileName;
        filePart.write(fullPath);

        VideoDao dao = new VideoDao();
        if ("ad".equals(type)) {
            dao.addAdConfig(Integer.parseInt(req.getParameter("videoId")), fullPath, Integer.parseInt(req.getParameter("insertTime")));
        } else {
            Video v = new Video();
            v.setTitle(req.getParameter("title"));
            v.setDescription(req.getParameter("description"));
            v.setCategory(req.getParameter("category"));
            v.setFilePath(fullPath);
            dao.addVideo(v);
        }
        resp.sendRedirect("index");
    }
}