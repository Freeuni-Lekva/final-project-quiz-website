package com.project.website.servlets;

import com.project.website.DAOs.AchievementDAO;
import com.project.website.DAOs.UserDAO;
import com.project.website.Objects.Achievement;
import com.project.website.Objects.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProfileServlet", value = "/profile")
public class ProfileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserDAO userDAO = (UserDAO) req.getServletContext().getAttribute(UserDAO.ATTR_NAME);
        String id = req.getParameter("id");

        if(id == null) {
            if(req.getSession().getAttribute("userID") != null) {
                User user = userDAO.getUserByID((long)req.getSession().getAttribute("userID"));
                addData(req, resp, user);
            }
            else {
                resp.sendRedirect("login");
            }
        }
        else {
            User user = userDAO.getUserByID(Long.parseLong(id));
            if(user == null) {
                req.setAttribute("errorMessage", "Profile not found");
                req.getRequestDispatcher("WEB-INF/error-message.jsp").forward(req, resp);
            }
            else {
                addData(req, resp, user);
            }
        }
    }

    private void addData(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        AchievementDAO achievementDAO = (AchievementDAO) req.getServletContext().getAttribute(AchievementDAO.ATTR_NAME);
        List<Achievement> achievements = achievementDAO.getUserAchievements(Math.toIntExact(user.getId()));
        req.setAttribute("achievements", achievements);
        req.setAttribute("user", user);
        req.getRequestDispatcher("WEB-INF/profile.jsp").forward(req, resp);
    }
}
