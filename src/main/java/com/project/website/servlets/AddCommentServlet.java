package com.project.website.servlets;

import com.project.website.DAOs.QuizCommentDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "QuestionServlet", value = "/addComment")
public class AddCommentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getAttribute("userID") == null) {
            resp.sendRedirect("login");
            return;
        }

        if (req.getParameter("quizID") == null) {
            resp.sendRedirect("home");
            return;
        }

        QuizCommentDAO commentDAO = (QuizCommentDAO) req.getServletContext().getAttribute(QuizCommentDAO.ATTR_NAME);
        commentDAO.postCommentOnQuiz(Long.parseLong(req.getParameter("quizID")), (Long) req.getAttribute("userID"), (String) req.getAttribute("comment_text"));
        resp.sendRedirect("quiz?quizID=" + req.getParameter("quizID"));
    }
}