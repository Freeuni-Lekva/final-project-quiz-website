package com.project.website.servlets;

import com.project.website.DAOs.QuizCommentDAO;
import com.project.website.DAOs.QuizDAO;
import com.project.website.DAOs.UserSessionsDAO;
import com.project.website.Objects.Quiz;
import com.project.website.Objects.QuizComment;
import com.project.website.Objects.User;
import com.project.website.Objects.UserSession;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
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

        String timedParam = req.getParameter("timeLimit");
        int time = timedParam != null ? Integer.parseInt(timedParam) : quiz.getTimer();

        if (time != 0) {
            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
            df.setTimeZone(TimeZone.getTimeZone("GMT"));
            String timeString = df.format(new Date(time * 1000L));
            req.setAttribute("timeLimit", timeString);
        }
        req.setAttribute("comments", comments);
        req.getRequestDispatcher("WEB-INF/quiz.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long userID = (Long) req.getSession().getAttribute("userID");

        if (userID == null) {
            resp.sendRedirect("login");
            return;
        }

        String quizID = req.getParameter("quizID");

        try {
            if (quizID == null || ((QuizDAO)req.getServletContext().getAttribute("QuizDAO")).getQuizById(Integer.parseInt(quizID)) == null) {
                resp.sendRedirect("home");
                return;
            }

            QuizDAO quizDAO = (QuizDAO) req.getServletContext().getAttribute(QuizDAO.ATTR_NAME);
            UserSessionsDAO userSessionsDAO = (UserSessionsDAO) req.getServletContext().getAttribute(UserSessionsDAO.ATTR_NAME);

            String timedParam = req.getParameter("timeLimit");
            if (userSessionsDAO.getUserSession(Math.toIntExact(userID)) == null) {
                int time = timedParam != null ? Integer.parseInt(timedParam) : quizDAO.getQuizById(Integer.parseInt(quizID)).getTimer();
                userSessionsDAO.insertSession(new UserSession(Math.toIntExact(userID), Integer.parseInt(quizID), time));
                req.getRequestDispatcher("activeQuiz").forward(req, resp);
            } else {
                req.setAttribute("errorMessage", "Already in a quiz!");
                req.getRequestDispatcher("WEB-INF/error-message.jsp").forward(req, resp);
            }
        } catch(NumberFormatException ignored) {
            req.setAttribute("errorMessage", "Already in a quiz!");
            req.getRequestDispatcher("WEB-INF/error-message.jsp").forward(req, resp);
        }
    }
}
