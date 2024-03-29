package com.project.website.servlets;

import com.project.website.DAOs.AnnouncementDAO;
import com.project.website.DAOs.UserDAO;
import com.project.website.Objects.Announcement;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "AdminServlet", value = "/AdminServlet")
public class AdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!(boolean) request.getSession().getAttribute("admin")) {
            response.sendRedirect("home");
            return;
        }
        UserDAO userDAO = (UserDAO) request.getServletContext().getAttribute(UserDAO.ATTR_NAME);
        if (request.getParameter("delete-user") != null) {
            int userId = Integer.parseInt((String) request.getParameter("delete-user"));
            userDAO.deleteUserById(userId);
        }
        request.getRequestDispatcher("WEB-INF/admin.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!(boolean) request.getSession().getAttribute("admin")) {
            response.sendRedirect("home");
            return;
        }

        AnnouncementDAO announcementDAO = (AnnouncementDAO) request.getServletContext().getAttribute(AnnouncementDAO.ATTR_NAME);
        if (request.getParameter("announcement-title") != null) {
            String announcementTitle = request.getParameter("announcement-title");
            String announcementText = request.getParameter("announcement-text");
            announcementDAO.insertAnnouncement(new Announcement(((Long)request.getSession().getAttribute("userID")).intValue(), announcementTitle, announcementText));
        }
        request.getRequestDispatcher("WEB-INF/admin.jsp").forward(request, response);
    }
}
