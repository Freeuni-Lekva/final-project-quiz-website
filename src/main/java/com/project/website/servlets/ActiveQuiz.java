package com.project.website.servlets;

import com.project.website.DAOs.QuestionToQuizDAO;
import com.project.website.DAOs.QuizAnswersDAO;
import com.project.website.DAOs.QuizDAO;
import com.project.website.DAOs.UserSessionsDAO;
import com.project.website.Objects.Quiz;
import com.project.website.Objects.UserSession;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ActiveQuiz", value = "/activeQuiz")
public class ActiveQuiz extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("userID") == null) {
            resp.sendRedirect("login");
            return;
        }
        int userID = Math.toIntExact((Long) req.getSession().getAttribute("userID"));

        UserSessionsDAO userSessionsDAO = (UserSessionsDAO) req.getServletContext().getAttribute(UserSessionsDAO.ATTR_NAME);
        UserSession session = userSessionsDAO.getUserSession(userID);
        if (session == null) {
            req.setAttribute("errorMessage", "No currently active quiz");
            req.getRequestDispatcher("WEB-INF/error-message.jsp").forward(req, resp);
            return;
        }
        QuizAnswersDAO quizAnswersDAO = (QuizAnswersDAO) req.getServletContext().getAttribute(QuizAnswersDAO.ATTR_NAME);
        QuestionToQuizDAO questionToQuizDAO = (QuestionToQuizDAO) req.getServletContext().getAttribute(QuestionToQuizDAO.ATTR_NAME);
        QuizDAO quizDAO = (QuizDAO) req.getServletContext().getAttribute(QuizDAO.ATTR_NAME);
        Quiz activeQuiz = quizDAO.getQuizById(session.getQuizID());

        if (activeQuiz == null) {
            req.setAttribute("errorMessage", "Invalid active quiz");
            req.getRequestDispatcher("WEB-INF/error-message.jsp").forward(req, resp);
            return;
        }

        List<Double> scores = new ArrayList<>();
        for (int i = 0; i < activeQuiz.getLastQuestionID(); i++) {
            int questionID = questionToQuizDAO.getQuestionIDByQuizAndLocalID(activeQuiz.getID(), i);
            if (questionID != QuestionToQuizDAO.GET_FAILED) {
                scores.add(quizAnswersDAO.getAnswer(activeQuiz.getID(), userID, i));
            }
        }
        req.setAttribute("questionScores", scores);
        req.setAttribute("quizID", session.getQuizID());
        req.getRequestDispatcher("WEB-INF/active-quiz.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("userID") == null) {
            return;
        }
        int userID = Math.toIntExact((Long) req.getSession().getAttribute("userID"));

        UserSessionsDAO userSessionsDAO = (UserSessionsDAO) req.getServletContext().getAttribute(UserSessionsDAO.ATTR_NAME);
        UserSession session = userSessionsDAO.getUserSession(userID);
        QuestionToQuizDAO questionToQuizDAO = (QuestionToQuizDAO) req.getServletContext().getAttribute(QuestionToQuizDAO.ATTR_NAME);
        if (session == null) {
            return;
        }

        int localID = questionToQuizDAO.getNextLocalId(session.getQuizID(), session.getCurrentLocalID());

        if (localID != session.getCurrentLocalID() && localID != QuestionToQuizDAO.GET_FAILED) {
            userSessionsDAO.updateSessionLocalId(userID, localID);
        }

        int questionID = questionToQuizDAO.getQuestionIDByQuizAndLocalID(session.getQuizID(), localID);

        if (questionID != QuestionToQuizDAO.GET_FAILED) {
            resp.sendRedirect("question?questionID=" + questionID);
        } else {
            resp.sendRedirect("activeQuiz");
        }
    }
}
