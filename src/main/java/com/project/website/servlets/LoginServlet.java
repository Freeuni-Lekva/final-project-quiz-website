package com.project.website.servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/LoginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getParameter("using").equals("username")) {
            request.getRequestDispatcher("WEB-INF/username-login.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("WEB-INF/email-login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO: process the given parameters and use DAO to check their validity
    }
}
