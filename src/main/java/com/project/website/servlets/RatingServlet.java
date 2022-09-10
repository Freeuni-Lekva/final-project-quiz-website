package com.project.website.servlets;

import com.project.website.DAOs.QuizRatingsDAO;
import com.project.website.Objects.Quiz;
import com.project.website.Objects.QuizRating;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "RatingServlet", value = "/rate")
public class RatingServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        QuizRatingsDAO quizRatingsDAO = (QuizRatingsDAO) request.getServletContext().getAttribute(QuizRatingsDAO.ATTR_NAME);

        Long userID = (Long) request.getSession().getAttribute("userID");
        if(userID == null) {
            response.sendRedirect("login");
            return;
        }

        Long quizID = Long.valueOf(request.getParameter("quizID"));
        Integer rating = Integer.valueOf(request.getParameter("starCount"));
        QuizRating quizRating = quizRatingsDAO.getRatingByUser(quizID, userID);
        if(quizRating == null) {
            quizRatingsDAO.insertRating(new QuizRating(quizID, userID, rating));
        } else {
            quizRatingsDAO.setRating(quizRating.getId(), rating);
        }

        response.sendRedirect("quiz?quizID=" + quizID);
    }
}
