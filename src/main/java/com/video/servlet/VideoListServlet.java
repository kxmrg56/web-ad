package com.video.servlet;
import com.video.dao.VideoDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/index")
public class VideoListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        VideoDao dao = new VideoDao();
        String category = req.getParameter("category");
        if (category != null && !category.isEmpty()) {
            req.setAttribute("videoList", dao.getVideosByCategory(category));
            req.setAttribute("currentCategory", category);
        } else {
            req.setAttribute("videoList", dao.getAllVideos());
            req.setAttribute("currentCategory", "all");
        }
        req.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(req, resp);
    }
}