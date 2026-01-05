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
        filePath = URLDecoder.decode(filePath, StandardCharsets.UTF_8);

        File videoFile = new File(filePath);
        if (!videoFile.exists()) { resp.sendError(404); return; }

        long length = videoFile.length();
        String range = req.getHeader("Range");
        long start = 0, end = length - 1;

        if (range != null && range.startsWith("bytes=")) {
            String[] ranges = range.substring(6).split("-");
            try {
                start = Long.parseLong(ranges[0]);
                if (ranges.length > 1 && !ranges[1].isEmpty()) end = Long.parseLong(ranges[1]);
            } catch (Exception ignored) {}
        }

        long contentLength = end - start + 1;
        resp.reset();
        resp.setHeader("Accept-Ranges", "bytes");
        resp.setContentType("video/mp4");
        if (range != null) {
            resp.setStatus(206);
            resp.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + length);
        }
        resp.setHeader("Content-Length", String.valueOf(contentLength));

        try (RandomAccessFile raf = new RandomAccessFile(videoFile, "r");
             OutputStream out = resp.getOutputStream()) {
            raf.seek(start);
            byte[] buffer = new byte[65536];
            long readCount = 0;
            while (readCount < contentLength) {
                int read = raf.read(buffer);
                if (read == -1) break;
                long remain = contentLength - readCount;
                out.write(buffer, 0, (int) Math.min(read, remain));
                readCount += read;
            }
        }
    }
}