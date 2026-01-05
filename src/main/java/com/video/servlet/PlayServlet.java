package com.video.servlet;
import com.video.dao.VideoDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/play")
public class PlayServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr != null) {
            req.setAttribute("video", new VideoDao().getVideoById(Integer.parseInt(idStr)));
            req.getRequestDispatcher("/WEB-INF/views/play.jsp").forward(req, resp);
        } else { resp.sendRedirect("index"); }
    }
}