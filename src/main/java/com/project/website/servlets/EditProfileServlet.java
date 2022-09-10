package com.project.website.servlets;

import com.project.website.DAOs.UserDAO;
import com.project.website.Objects.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "EditProfileServlet", value = "/edit_profile")
public class EditProfileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getSession().getAttribute("userID") == null) {
            resp.sendRedirect("login");
            return;
        }

        UserDAO userDAO = (UserDAO)req.getServletContext().getAttribute(UserDAO.ATTR_NAME);
        User user = userDAO.getUserByID((long)req.getSession().getAttribute("userID"));
        req.setAttribute("user", user);
        req.getRequestDispatcher("WEB-INF/edit-profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String newFirstName = req.getParameter("f_name");
        String newLastName = req.getParameter("l_name");
        String newProfilePicURL = req.getParameter("imgURL");
        String bio = req.getParameter("bio");

        UserDAO userDAO = (UserDAO)req.getServletContext().getAttribute(UserDAO.ATTR_NAME);
        long userID = (long)req.getSession().getAttribute("userID");
        userDAO.changeName(userID, newFirstName, newLastName);
        userDAO.changeProfilePicture(userID, newProfilePicURL);
        userDAO.changeBio(userID, bio);
        resp.sendRedirect("profile");
    }
}
