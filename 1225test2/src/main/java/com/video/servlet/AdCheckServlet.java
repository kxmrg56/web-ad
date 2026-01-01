package com.video.servlet;

import com.video.dao.VideoDao;
import com.video.model.Video;
// ↓↓↓↓↓ 注意这里全变成了 jakarta ↓↓↓↓↓
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
// ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/check-ad")
public class AdCheckServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置响应类型为 JSON
        resp.setContentType("application/json;charset=UTF-8");

        String idStr = req.getParameter("videoId");
        PrintWriter out = resp.getWriter();

        if(idStr == null) {
            out.print("{}");
            return;
        }

        try {
            VideoDao dao = new VideoDao();
            Video v = dao.getVideoById(Integer.parseInt(idStr));

            if(v != null && v.isHasAd()){
                // 手动构建JSON字符串
                String json = String.format(
                        "{\"hasAd\": true, \"insertTime\": %d, \"adPath\": \"%s\"}",
                        v.getAdInsertTime(),
                        v.getAdEncodedPath()
                );
                out.print(json);
            } else {
                out.print("{\"hasAd\": false}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"error\": true}");
        }

        out.flush();
    }
}