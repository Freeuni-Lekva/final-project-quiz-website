package com.project.website.servlets;

import com.project.website.DAOs.UserDAO;
import com.project.website.Objects.User;
import com.project.website.utils.Hasher;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/LoginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String loginMethod = request.getParameter("using");
        if(loginMethod == null || loginMethod.equals("username")) {
            request.getRequestDispatcher("WEB-INF/username-login.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("WEB-INF/email-login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserDAO DAO = (UserDAO) request.getServletContext().getAttribute("UserDAO");

        String passwordHash = Hasher.getHash(request.getParameter("password"));
        if(request.getParameter("email") == null) {
            /* login with username */
            String username = request.getParameter("username");
            int loginResult = DAO.attemptLoginWithUsername(username, passwordHash);

            if(loginResult == UserDAO.SUCCESS) {
                // save the user info in the session and redirect them to the home page
                request.getSession().setAttribute("userID", DAO.getUserByUsername(username));
                request.getSession().setAttribute("username", username);
                response.sendRedirect("home");
            } else if(loginResult == UserDAO.USERNAME_DOES_NOT_EXIST) {
                // TODO: redirect the user to the same login page, but with a note about the username
            } else {    // if password wrong
                // TODO: redirect the user to the same login page, but with a note about the password
            }
        } else {
            /* login with email */
            String email = request.getParameter("email");
            int loginResult = DAO.attemptLoginWithEmail(email, passwordHash);

            if(loginResult == UserDAO.SUCCESS) {
                // save the user info in the session and redirect them to the home page
                User user = DAO.getUserByEmail(email);
                request.getSession().setAttribute("userID", user.getId());
                request.getSession().setAttribute("username", user.getUsername());
                response.sendRedirect("home");
            } else if(loginResult == UserDAO.EMAIL_DOES_NOT_EXIST) {
                // TODO: redirect the user to the same login page, but with a note about the email
            } else {    // if password wrong
                // TODO: redirect the user to the same login page, but with a note about the password
            }
        }
    }
}
