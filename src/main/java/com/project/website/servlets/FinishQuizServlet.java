package com.project.website.servlets;

import com.project.website.DAOs.*;
import com.project.website.Objects.QuizFinalScore;
import com.project.website.Objects.UserSession;
import com.project.website.Objects.listeners.QuizWebsiteListener;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet(name = "FinishQuiz", value = "/finishQuiz")
public class FinishQuizServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

        QuizFinalScore finalScore = new QuizFinalScore(session, scores);
        quizFinalScoresDAO.insertQuizFinalScore(finalScore);
        quizAnswersDAO.deleteAllAnswers(session.getQuizID(), session.getUserID());
        userSessionsDAO.deleteSession(userID);

        long secondsLeft = Calendar.getInstance().getTimeInMillis() - session.getStartDate().getTime();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        String time = df.format(new Date(
                (Calendar.getInstance().getTimeInMillis() - session.getStartDate().getTime())));

        req.setAttribute("time", time);
        req.setAttribute("finalScore", finalScore.getScore());
        req.setAttribute("maxScore", finalScore.getMaxScore());
        req.setAttribute("questionScores", scores);
        req.setAttribute("quizID", session.getQuizID());


        QuizWebsiteListener listener = (QuizWebsiteListener) req.getServletContext().getAttribute("listener");
        listener.onQuizFinished(userID, session.getQuizID(), finalScore.getScore(), finalScore.getMaxScore());

        req.getRequestDispatcher("WEB-INF/quiz-results.jsp").forward(req, resp);
    }
}
