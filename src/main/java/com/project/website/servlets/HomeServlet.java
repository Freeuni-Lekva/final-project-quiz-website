package com.project.website.servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "HomeServlet", value = "/HomeServlet")
public class HomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO: fetch data from the database and set attributes for home.jsp to display
        request.getRequestDispatcher("WEB-INF/home.jsp").forward(request, response);
    }
}
