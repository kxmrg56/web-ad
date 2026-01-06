package com.example.adproj.servlet;

import com.example.adproj.util.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.sql.*;
import java.util.UUID;

// 路由保持一致：支持 /ads/upload/form 和 /ads/upload/doUpload
@WebServlet("/ads/upload/*")
@MultipartConfig(
        maxFileSize = 50 * 1024 * 1024,    // 50MB
        maxRequestSize = 100 * 1024 * 1024 // 100MB
)
public class UploadServlet extends HttpServlet {

    private static final String IMAGES_DIR = "images";
    private static final String VIDEOS_DIR = "videos";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        // 兼容 / 或 /form 访问上传页面
        if (pathInfo == null || "/".equals(pathInfo) || "/form".equals(pathInfo)) {
            request.getRequestDispatcher("/upload.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if ("/doUpload".equals(pathInfo)) {
            handleFileUpload(request, response);
        }
    }

    private void handleFileUpload(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. 登录校验
        HttpSession session = request.getSession();
        Integer ownerId = (Integer) session.getAttribute("ownerId");
        if (ownerId == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            request.setCharacterEncoding("UTF-8");
            // 获取表单数据
            String title = request.getParameter("title");
            String category = request.getParameter("category");
            String materialType = request.getParameter("materialType");
            String targetUrl = request.getParameter("targetUrl");
            String materialName = request.getParameter("materialName");

            String fileUrl = null;

            // 2. 根据类型处理 Part (核心修正点：确保名称与前端一致)
            if ("image".equals(materialType)) {
                // 注意：前端必须是 <input type="file" name="imageFile">
                Part imagePart = request.getPart("imageFile");
                if (imagePart != null && imagePart.getSize() > 0) {
                    fileUrl = saveFile(imagePart, "image");
                }
            } else if ("video".equals(materialType)) {
                // 注意：前端必须是 <input type="file" name="videoFile">
                Part videoPart = request.getPart("videoFile");
                if (videoPart != null && videoPart.getSize() > 0) {
                    fileUrl = saveFile(videoPart, "video");
                }

                // 处理视频封面（可选）
                Part thumbnailPart = request.getPart("thumbnail");
                if (thumbnailPart != null && thumbnailPart.getSize() > 0) {
                    saveFile(thumbnailPart, "image");
                }
            }

            // 3. 验证是否保存成功
            if (fileUrl == null) {
                request.setAttribute("error", "文件上传失败，请确保选择了正确的文件并重试");
                request.getRequestDispatcher("/upload.jsp").forward(request, response);
                return;
            }

            // 4. 保存到数据库
            boolean success = saveToDatabase(title, category, materialName, fileUrl, targetUrl);

            if (success) {
                // 重定向到列表页并带上成功标记
                response.sendRedirect(request.getContextPath() + "/ads/manage/list?upload_success=true");
            } else {
                request.setAttribute("error", "数据库保存失败");
                request.getRequestDispatcher("/upload.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "系统异常: " + e.getMessage());
            request.getRequestDispatcher("/upload.jsp").forward(request, response);
        }
    }

    private String saveFile(Part part, String fileType) throws IOException {
        String fileName = part.getSubmittedFileName();
        if (fileName == null || fileName.isEmpty()) return null;

        // 生成唯一文件名
        String fileExtension = getFileExtension(fileName);
        String newFileName = UUID.randomUUID().toString() + "_" + System.currentTimeMillis() + fileExtension;

        // 确定保存目录
        String saveDir = "image".equals(fileType) ? IMAGES_DIR : VIDEOS_DIR;

        // 获取 Web 物理路径
        String realPath = getServletContext().getRealPath("/");
        if (realPath == null) throw new IOException("无法获取 Web 根目录物理路径");

        // 确保路径以分隔符结尾并拼接目录
        File uploadFolder = new File(realPath + File.separator + saveDir);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }

        // 保存文件到磁盘
        String fullPath = uploadFolder.getAbsolutePath() + File.separator + newFileName;
        part.write(fullPath);

        System.out.println("[成功保存] " + fileType + " -> " + fullPath);

        // 返回用于存入数据库的相对路径（使用正斜杠防止前端无法解析）
        return saveDir + "/" + newFileName;
    }

    private boolean saveToDatabase(String title, String category, String materialName,
                                   String fileUrl, String targetUrl) {
        String sql = "INSERT INTO ad_material (category, material_name, title, image_url, target_url, status, create_time) " +
                "VALUES (?, ?, ?, ?, ?, 1, NOW())";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, category);
            ps.setString(2, materialName != null ? materialName : "");
            ps.setString(3, title);
            ps.setString(4, fileUrl);
            ps.setString(5, targetUrl);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        return (dotIndex == -1) ? ".jpg" : fileName.substring(dotIndex).toLowerCase();
    }
}