package com.project.website.servlets;

import com.project.website.DAOs.QuizCommentDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "CommentServlet", value = "/addComment")
public class AddCommentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("userID") == null) {
            resp.sendRedirect("login");
            return;
        }

        if (req.getParameter("quizID") == null) {
            resp.sendRedirect("home");
            return;
        }

        QuizCommentDAO commentDAO = (QuizCommentDAO) req.getServletContext().getAttribute(QuizCommentDAO.ATTR_NAME);
        commentDAO.postCommentOnQuiz(Long.parseLong(req.getParameter("quizID")), (Long) req.getSession().getAttribute("userID"), (String) req.getParameter("comment_text"));
        resp.sendRedirect("quiz?quizID=" + req.getParameter("quizID"));
    }
}
