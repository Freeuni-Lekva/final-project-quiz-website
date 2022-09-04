package com.project.website.servlets;

import com.project.website.DAOs.QuestionToQuizDAO;
import com.project.website.DAOs.QuizAnswersDAO;
import com.project.website.DAOs.QuizDAO;
import com.project.website.DAOs.UserSessionsDAO;
import com.project.website.Objects.Quiz;
import com.project.website.Objects.User;
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
        QuizWebsiteController controller = new QuizWebsiteController(req, resp);
        if (!controller.assertActiveQuiz()) {
            return;
        }

        req.setAttribute("questionScores", controller.getQuizScores());
        req.setAttribute("quizID", controller.getUserSession().getQuizID());
        req.getRequestDispatcher("WEB-INF/active-quiz.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserSessionsDAO userSessionsDAO = (UserSessionsDAO) req.getServletContext().getAttribute(UserSessionsDAO.ATTR_NAME);
        QuizWebsiteController controller = new QuizWebsiteController(req, resp);
        if (!controller.assertActiveQuiz()) {
            return;
        }
        UserSession session = controller.getUserSession();
        int userID = controller.getUserID();
        QuestionToQuizDAO questionToQuizDAO = (QuestionToQuizDAO) req.getServletContext().getAttribute(QuestionToQuizDAO.ATTR_NAME);
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
