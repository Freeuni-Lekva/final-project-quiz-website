package com.project.website.servlets;

import com.project.website.DAOs.UserDAO;
import com.project.website.Objects.User;
import com.project.website.utils.Hasher;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "RegisterServlet", value = "/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        if(email.equals(""))
            email = null;
        String username = request.getParameter("username");
        String passwordHash = Hasher.getHash(request.getParameter("password"));

        UserDAO DAO = (UserDAO) request.getServletContext().getAttribute(UserDAO.ATTR_NAME);
        int regResult = DAO.register(username, passwordHash, email);

        if(regResult == UserDAO.SUCCESS) {
            // save the user info in the session and redirect them to the home page
            User user = DAO.getUserByUsername(username);
            request.getSession().setAttribute("userID", user.getId());
            request.getSession().setAttribute("username", username);
            response.sendRedirect("home");
            return;
        } else if(regResult == UserDAO.USERNAME_TAKEN) {
            request.setAttribute("error", Integer.toString(UserDAO.USERNAME_TAKEN));
        } else if(regResult == UserDAO.EMAIL_TAKEN) {
            request.setAttribute("error", Integer.toString(UserDAO.EMAIL_TAKEN));
        }
        request.getRequestDispatcher("WEB-INF/register.jsp").forward(request, response);
    }
}
