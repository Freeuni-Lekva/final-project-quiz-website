package com.project.website.servlets;

import com.project.website.DAOs.AnnouncementDAO;
import com.project.website.DAOs.UserDAO;
import com.project.website.Objects.Announcement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "AnnouncementsServlet", value = "/announcements")
public class AnnouncementsServlet extends HttpServlet {
    public static final int ANNOUNCEMENTS_PER_PAGE = 10;
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

        int pageNumber = 1;
        try {
            if (request.getParameter("page") != null)
                pageNumber = Integer.parseInt(request.getParameter("page"));
        } catch(Exception ignored){}

        int pageCount = Math.max((announcementList.size() + ANNOUNCEMENTS_PER_PAGE - 1) / ANNOUNCEMENTS_PER_PAGE, 1);   // page count should be positive
        if(pageNumber > pageCount)  // page number shouldn't be greater than page count
            pageNumber = pageCount;
        // sublist of indices that match the number of the page that the user wants to see
        announcementList = announcementList.subList((pageNumber - 1) * ANNOUNCEMENTS_PER_PAGE,
                (pageNumber == pageCount) ? announcementList.size() : pageNumber * ANNOUNCEMENTS_PER_PAGE);

        request.setAttribute("announcements", announcementList);
        request.setAttribute("userDAO", request.getServletContext().getAttribute(UserDAO.ATTR_NAME));
        request.setAttribute("pageNumber", pageNumber);
        request.setAttribute("pageCount", pageCount);
        request.getRequestDispatcher("WEB-INF/announcements.jsp").forward(request, response);
    }
}
