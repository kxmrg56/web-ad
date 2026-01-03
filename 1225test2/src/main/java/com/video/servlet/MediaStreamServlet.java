package com.video.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/stream")
public class MediaStreamServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String filePath = req.getParameter("path");
        if (filePath == null) return;
        // 解码路径
        filePath = URLDecoder.decode(filePath, StandardCharsets.UTF_8);

        File videoFile = new File(filePath);
        if (!videoFile.exists()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 处理 Range 请求 (支持拖拽进度条)
        String range = req.getHeader("Range");
        long length = videoFile.length();
        long start = 0;
        long end = length - 1;

        if (range != null && range.startsWith("bytes=")) {
            String[] ranges = range.substring(6).split("-");
            try {
                start = Long.parseLong(ranges[0]);
                if (ranges.length > 1 && !ranges[1].isEmpty()) {
                    end = Long.parseLong(ranges[1]);
                }
            } catch (NumberFormatException ignored) {}
        }

        long contentLength = end - start + 1;
        resp.reset();
        resp.setBufferSize(1024 * 64);
        resp.setHeader("Content-Disposition", "inline;filename=\"video.mp4\"");
        resp.setHeader("Accept-Ranges", "bytes");
        resp.setContentType("video/mp4");

        if (range != null) {
            resp.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            resp.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + length);
        } else {
            resp.setStatus(HttpServletResponse.SC_OK);
        }
        resp.setHeader("Content-Length", String.valueOf(contentLength));

        // 输出文件流
        try (RandomAccessFile raf = new RandomAccessFile(videoFile, "r");
             OutputStream out = resp.getOutputStream()) {
            raf.seek(start);
            byte[] buffer = new byte[1024 * 64];
            long bytesRead = 0;
            while (bytesRead < contentLength) {
                int read = raf.read(buffer);
                if (read == -1) break;
                long remaining = contentLength - bytesRead;
                out.write(buffer, 0, (int) (read > remaining ? remaining : read));
                bytesRead += read;
            }
        }
    }
}