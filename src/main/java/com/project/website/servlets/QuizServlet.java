package com.project.website.servlets;

import com.project.website.DAOs.QuizCommentDAO;
import com.project.website.DAOs.QuizDAO;
import com.project.website.Objects.Quiz;
import com.project.website.Objects.QuizComment;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@WebServlet(name = "QuizServlet", value = "/quiz")
public class QuizServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String quizID = req.getParameter("quizID");
        if (quizID == null || ((QuizDAO)req.getServletContext().getAttribute("QuizDAO")).getQuizById(Integer.parseInt(quizID)) == null) {
            resp.sendRedirect("home");
            return;
        }

        Quiz quiz = ((QuizDAO)req.getServletContext().getAttribute("QuizDAO")).getQuizById(Integer.parseInt(quizID));
        req.setAttribute("quiz", quiz);

        QuizCommentDAO commentDAO = (QuizCommentDAO) req.getServletContext().getAttribute(QuizCommentDAO.ATTR_NAME);

        List<Long> commentIDs = commentDAO.getQuizComments(quiz.getID(), 0, Long.MAX_VALUE);
        List<QuizComment> comments = commentIDs.stream().map(commentDAO::getCommentByID).collect(Collectors.toList());

        req.setAttribute("comments", comments);
        req.getRequestDispatcher("WEB-INF/quiz.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("quizID") != null) {
            return;
        }

        //TODO start quiz
    }
}
