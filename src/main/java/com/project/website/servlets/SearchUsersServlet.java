package com.project.website.servlets;

import com.project.website.DAOs.UserDAO;
import com.project.website.Objects.User;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@WebServlet(name = "SearchUsersServlet", value = "/search_users")
public class SearchUsersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserDAO userDAO = (UserDAO) request.getServletContext().getAttribute(UserDAO.ATTR_NAME);
        String query = request.getParameter("q");
        List<User> searchResults = query == null ? Collections.emptyList() : userDAO.searchUsers("%" + query + "%");
        request.setAttribute("searchResults", searchResults);
        request.getRequestDispatcher("WEB-INF/search-users.jsp").forward(request, response);
    }
}
