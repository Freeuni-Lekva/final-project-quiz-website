package com.project.website.servlets;

import com.project.website.DAOs.QuestionToQuizDAO;
import com.project.website.DAOs.QuizAnswersDAO;
import com.project.website.DAOs.QuizDAO;
import com.project.website.DAOs.UserSessionsDAO;
import com.project.website.Objects.Quiz;
import com.project.website.Objects.UserSession;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class QuizWebsiteController {
    private HttpServletRequest req;
    private HttpServletResponse resp;

    public QuizWebsiteController(HttpServletRequest req, HttpServletResponse resp) {
        this.req = req;
        this.resp = resp;
    }

    public boolean isLoggedIn() {
        return req.getSession().getAttribute("userID") != null;
    }
    public boolean assertLoggedIn() throws IOException {
        if (!isLoggedIn()) {
            resp.sendRedirect("login");
            return false;
        }
        return true;
    }

    public int getUserID() {
        return Math.toIntExact((Long) req.getSession().getAttribute("userID"));
    }

    public boolean isActiveQuiz() {
        if (!isLoggedIn()) {
            return false;
        }
        UserSessionsDAO userSessionsDAO = (UserSessionsDAO) req.getServletContext().getAttribute(UserSessionsDAO.ATTR_NAME);
        UserSession session = userSessionsDAO.getUserSession(getUserID());

        return session != null;
    }
    public boolean assertActiveQuiz() throws IOException, ServletException {
        if (!assertLoggedIn()) {
            return false;
        }

        UserSessionsDAO userSessionsDAO = (UserSessionsDAO) req.getServletContext().getAttribute(UserSessionsDAO.ATTR_NAME);
        UserSession session = userSessionsDAO.getUserSession(getUserID());
        if (session == null) {
            req.setAttribute("errorMessage", "No currently active quiz");
            req.getRequestDispatcher("WEB-INF/error-message.jsp").forward(req, resp);
            return false;
        }

        return true;
    }

    public UserSession getUserSession() {
        UserSessionsDAO userSessionsDAO = (UserSessionsDAO) req.getServletContext().getAttribute(UserSessionsDAO.ATTR_NAME);
        return userSessionsDAO.getUserSession(getUserID());
    }

    public Quiz getActiveQuiz() {
        QuizDAO quizDAO = (QuizDAO) req.getServletContext().getAttribute(QuizDAO.ATTR_NAME);
        UserSession activeSession = getUserSession();
        return quizDAO.getQuizById(activeSession.getQuizID());
    }

    public String getJsonBody() {
        StringBuilder jb = new StringBuilder();
        String line = null;
        try {
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception ignored) {}
        return jb.toString();
    }
    public List<Double> getQuizScores() {
        List<Double> scores = new ArrayList<>();
        QuestionToQuizDAO questionToQuizDAO = (QuestionToQuizDAO) req.getServletContext().getAttribute(QuestionToQuizDAO.ATTR_NAME);
        QuizAnswersDAO quizAnswersDAO = (QuizAnswersDAO) req.getServletContext().getAttribute(QuizAnswersDAO.ATTR_NAME);
        Quiz activeQuiz = getActiveQuiz();
        int userID = getUserID();
        for (int i = 0; i < activeQuiz.getLastQuestionID(); i++) {
            int questionID = questionToQuizDAO.getQuestionIDByQuizAndLocalID(activeQuiz.getID(), i);
            if (questionID != QuestionToQuizDAO.GET_FAILED) {
                scores.add(quizAnswersDAO.getAnswer(activeQuiz.getID(), userID, i));
            }
        }
        return scores;
    }

    public void sendJson(String json) throws IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print(json);
        out.flush();
    }
}
