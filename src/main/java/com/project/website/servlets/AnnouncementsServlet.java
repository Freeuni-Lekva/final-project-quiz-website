package com.project.website.servlets;

import com.project.website.DAOs.AnnouncementDAO;
import com.project.website.DAOs.UserDAO;
import com.project.website.Objects.Announcement;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "AnnouncementsServlet", value = "/announcements")
public class AnnouncementsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AnnouncementDAO dao = (AnnouncementDAO) request.getServletContext().getAttribute(AnnouncementDAO.ATTR_NAME);

        List<Announcement> announcementList = null;

        // if the user input a search query, only select announcements that match it
        if(request.getParameter("q") != null) {
            announcementList = dao.searchAnnouncements(request.getParameter("q"));
        } else {
            announcementList = dao.getAllAnnouncements();
        }

        request.setAttribute("announcements", announcementList);
        request.setAttribute("userDAO", request.getServletContext().getAttribute(UserDAO.ATTR_NAME));
        request.getRequestDispatcher("WEB-INF/announcements.jsp").forward(request, response);
    }
}
