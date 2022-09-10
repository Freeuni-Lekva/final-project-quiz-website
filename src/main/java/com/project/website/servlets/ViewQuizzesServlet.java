package com.project.website.servlets;

import com.project.website.DAOs.CategoryDAO;
import com.project.website.DAOs.Filters.*;
import com.project.website.DAOs.Order.OrderByQuizRating;
import com.project.website.DAOs.QuizDAO;
import com.project.website.DAOs.UserDAO;
import com.project.website.Objects.Quiz;
import com.project.website.Objects.User;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "ViewQuizzesServlet", value = "/quizzes")
public class ViewQuizzesServlet extends HttpServlet {

    private static final int QUIZ_PER_PAGE = 10;

    private static final String MYCREATED = "mycreated";
    private static final String TAKEN     = "taken";
    private static final String FRIENDS   = "friends";

    private SQLFilter addFilters(String sortBy, SQLFilter filter, QuizWebsiteController controller) {
        if (sortBy == null || !controller.isLoggedIn()) {
            return filter;
        } else if (sortBy.equals(MYCREATED)) {
            return new AndFilter(Arrays.asList(filter, new CreatorFilter(controller.getUserID())));
        } else if (sortBy.equals(TAKEN)) {
            return new AndFilter(Arrays.asList(filter, new TakenFilter(controller.getUserID(), "q")));
        } else if (sortBy.equals(FRIENDS)) {
            return new AndFilter(Arrays.asList(filter, new FriendsFilter(controller.getUserID(), "q")));
        }

        return filter;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        QuizDAO quizDAO = (QuizDAO) request.getServletContext().getAttribute(QuizDAO.ATTR_NAME);
        QuizWebsiteController controller = new QuizWebsiteController(request, response);

        SQLFilter filter = addFilters(request.getParameter("sortby"), new NoFilter(), controller);
        List<Quiz> quizList = quizDAO.searchQuizzes(filter, new OrderByQuizRating(), 0, QUIZ_PER_PAGE);

        request.setAttribute("searchResults", quizList);
        request.getRequestDispatcher("/WEB-INF/view-quizzes.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        QuizDAO quizDAO = (QuizDAO) request.getServletContext().getAttribute(QuizDAO.ATTR_NAME);
        QuizWebsiteController controller = new QuizWebsiteController(request, response);
        String searchQuery = request.getParameter("q");
        String sortby = request.getParameter("sortby");
        searchQuery = searchQuery == null ? "" : searchQuery;
        OrFilter orFilter = new OrFilter(Arrays.asList(new ColumnLikeFilter("quiz_title", "%" + searchQuery + "%"),
                new ColumnLikeFilter("quiz_description", "%" + searchQuery + "%")));
        int category = Integer.parseInt(request.getParameter("category"));
        SQLFilter categoryFilter;
        if(category > 0) {
            categoryFilter = new CategoryFilter(category);
        }
        else {
            categoryFilter = new NoFilter();
        }

        SQLFilter andFilter = new AndFilter(Arrays.asList(orFilter, categoryFilter));
        andFilter = addFilters(sortby, andFilter, controller);
        int page = Integer.parseInt(request.getParameter("p"));
        List<Quiz> quizList = quizDAO.searchQuizzes(andFilter, new OrderByQuizRating(), QUIZ_PER_PAGE * (page - 1), QUIZ_PER_PAGE);

        request.setAttribute("searchResults", quizList);
        request.getRequestDispatcher("/WEB-INF/view-quizzes.jsp").forward(request, response);
    }
}
