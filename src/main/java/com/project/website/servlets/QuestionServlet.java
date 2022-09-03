package com.project.website.servlets;

import com.project.website.DAOs.QuestionDAO;
import com.project.website.DAOs.QuestionToQuizDAO;
import com.project.website.DAOs.QuizAnswersDAO;
import com.project.website.DAOs.UserSessionsDAO;
import com.project.website.Objects.UserSession;
import com.project.website.Objects.questions.QuestionEntry;
import com.project.website.utils.JSPAttributePair;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "QuestionServlet", value = "/question")
public class QuestionServlet extends HttpServlet {

    private int getUserID(HttpServletRequest req) {
        return Math.toIntExact((Long) req.getSession().getAttribute("userID"));
    }

    private boolean verifyAccess(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        UserSessionsDAO userSessionsDAO = (UserSessionsDAO) req.getServletContext().getAttribute(UserSessionsDAO.ATTR_NAME);
        QuestionDAO questionDAO = (QuestionDAO) req.getServletContext().getAttribute(QuestionDAO.ATTR_NAME);
        QuestionToQuizDAO questionToQuizDAO = (QuestionToQuizDAO) req.getServletContext().getAttribute(QuestionToQuizDAO.ATTR_NAME);
        if (req.getParameter("questionID") == null) {
            resp.sendRedirect("home");
            return false;
        }
        int questionID = Integer.parseInt(req.getParameter("questionID"));
        QuestionEntry questionEntry = questionDAO.getQuestionById(questionID);

        if (questionEntry == null) {
            req.setAttribute("errorMessage", "Invalid question");
            req.getRequestDispatcher("WEB-INF/error-message.jsp").forward(req, resp);
            return false;
        }

        if (req.getSession().getAttribute("userID") == null) {
            resp.sendRedirect("login");
            return false;
        }

        int userID = getUserID(req);
        UserSession session = userSessionsDAO.getUserSession(userID);
        if (session == null) {
            req.setAttribute("errorMessage", "No currently active quiz");
            req.getRequestDispatcher("WEB-INF/error-message.jsp").forward(req, resp);
            return false;
        }

        if (questionID != questionToQuizDAO.getQuestionIDByQuizAndLocalID(session.getQuizID(), session.getCurrentLocalID())) {
            req.setAttribute("errorMessage", "Invalid request");
            req.getRequestDispatcher("WEB-INF/error-message.jsp").forward(req, resp);
            return false;
        }

        return true;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (verifyAccess(req, resp)) {
            int questionID = Integer.parseInt(req.getParameter("questionID"));
            QuestionDAO questionDAO = (QuestionDAO) req.getServletContext().getAttribute(QuestionDAO.ATTR_NAME);
            QuestionEntry questionEntry = questionDAO.getQuestionById(questionID);
            for (JSPAttributePair attributePair : questionEntry.getQuestion().getJSPParams()) {
                req.setAttribute(attributePair.getAttributeName(), attributePair.getAttributeValue());
            }
            req.setAttribute("title", questionEntry.getTitle());
            req.getRequestDispatcher(questionEntry.getQuestion().getJSP()).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (verifyAccess(req, resp)) {
            int questionID = Integer.parseInt(req.getParameter("questionID"));
            UserSessionsDAO userSessionsDAO = (UserSessionsDAO) req.getServletContext().getAttribute(UserSessionsDAO.ATTR_NAME);
            QuestionDAO questionDAO = (QuestionDAO) req.getServletContext().getAttribute(QuestionDAO.ATTR_NAME);
            QuestionEntry questionEntry = questionDAO.getQuestionById(questionID);
            QuizAnswersDAO quizAnswersDAO = (QuizAnswersDAO) req.getServletContext().getAttribute(QuizAnswersDAO.ATTR_NAME);

            int userID = getUserID(req);
            UserSession session = userSessionsDAO.getUserSession(userID);

            List<JSPAttributePair> answers = new ArrayList<>();
            for (String attribute : questionEntry.getQuestion().getAnswerParamNames()) {
                answers.add(new JSPAttributePair(attribute, (String) req.getParameter(attribute)));
            }

            double score = questionEntry.getQuestion().checkAnswer(answers);
            quizAnswersDAO.insertAnswer(session.getQuizID(), userID, session.getCurrentLocalID(), score);

            userSessionsDAO.updateSessionLocalId(userID, session.getCurrentLocalID() + 1);

            req.getRequestDispatcher("activeQuiz").forward(req, resp);
        }
    }
}
