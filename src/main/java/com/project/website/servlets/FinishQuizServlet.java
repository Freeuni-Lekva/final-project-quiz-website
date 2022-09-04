package com.project.website.servlets;

import com.project.website.DAOs.*;
import com.project.website.Objects.Quiz;
import com.project.website.Objects.QuizFinalScore;
import com.project.website.Objects.UserSession;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "FinishQuiz", value = "/finishQuiz")
public class FinishQuizServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        QuizWebsiteController controller = new QuizWebsiteController(req, resp);

        if (!controller.assertActiveQuiz()) {
            return;
        }

        UserSession session = controller.getUserSession();
        List<Double> scores = controller.getQuizScores();
        QuizFinalScoresDAO quizFinalScoresDAO = (QuizFinalScoresDAO) req.getServletContext().getAttribute(QuizFinalScoresDAO.ATTR_NAME);
        UserSessionsDAO userSessionsDAO = (UserSessionsDAO) req.getServletContext().getAttribute(UserSessionsDAO.ATTR_NAME);
        QuizAnswersDAO quizAnswersDAO = (QuizAnswersDAO) req.getServletContext().getAttribute(QuizAnswersDAO.ATTR_NAME);
        int userID = controller.getUserID();

        quizFinalScoresDAO.insertQuizFinalScore(new QuizFinalScore(session, scores));
        quizAnswersDAO.deleteAllAnswers(session.getQuizID(), session.getUserID());
        userSessionsDAO.deleteSession(userID);

        //TODO redirect to results
    }
}
