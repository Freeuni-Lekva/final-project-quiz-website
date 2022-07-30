package com.project.website.servlets;

import com.project.website.DAOs.QuizDAO;
import com.project.website.Objects.Quiz;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "QuestionServlet", value = "/quiz")
public class QuizServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String quizID = req.getParameter("quizID");
        if (quizID == null || ((QuizDAO)req.getServletContext().getAttribute("QuizDao")).getQuizById(Integer.parseInt(quizID)) == null) {
            resp.sendRedirect("home");
            return;
        }

        Quiz quiz = ((QuizDAO)req.getServletContext().getAttribute("QuizDao")).getQuizById(Integer.parseInt(quizID));
        //TODO implement logic
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("quizID") != null) {
            return;
        }
        //TODO implement quiz starting and comments
    }


}
