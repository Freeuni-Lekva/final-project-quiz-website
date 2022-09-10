package com.project.website.servlets;

import com.project.website.DAOs.ChallengeDAO;
import com.project.website.DAOs.QuizCommentDAO;
import com.project.website.DAOs.QuizDAO;
import com.project.website.DAOs.UserSessionsDAO;
import com.project.website.Objects.*;
import com.project.website.DAOs.QuizRatingsDAO;
import com.project.website.DAOs.QuizRatingsDAOSQL;
import com.project.website.Objects.Quiz;
import com.project.website.Objects.QuizComment;
import com.project.website.Objects.User;
import com.project.website.Objects.UserSession;
import com.project.website.Objects.QuizRating;

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

    private class QuizHelper {
        private final ChallengeDAO challengeDAO;
        Challenge challenge = null;

        Quiz quiz;

        public QuizHelper(HttpServletRequest req) {
            QuizDAO quizDAO = (QuizDAO) req.getServletContext().getAttribute(QuizDAO.ATTR_NAME);
            String challengeID = req.getParameter("challengeID");
            String quizID = req.getParameter("quizID");
            challengeDAO = (ChallengeDAO) req.getServletContext().getAttribute(ChallengeDAO.ATTR_NAME);
            if (challengeID != null) {
                challenge = challengeDAO.getChallenge(Integer.parseInt(challengeID));
                if (challenge != null) {
                    quiz = quizDAO.getQuizById(challenge.getQuizID());
                    quiz.setTimer(challenge.getTime());
                }
            } else {
                quiz = quizDAO.getQuizById(Integer.parseInt(quizID));
            }
        }

        private Quiz getQuiz() {
            return quiz;
        }

        private Challenge getChallenge() {
            return challenge;
        }

        private void endChallenge() {
            challengeDAO.deleteChallenge(challenge.getId());
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            QuizCommentDAO commentDAO = (QuizCommentDAO) req.getServletContext().getAttribute(QuizCommentDAO.ATTR_NAME);

            QuizHelper quizHelper = new QuizHelper(req);
            QuizWebsiteController controller = new QuizWebsiteController(req, resp);
            Challenge challenge = quizHelper.getChallenge();
            Quiz quiz = quizHelper.getQuiz();

            if (quiz == null || challenge != null && controller.isLoggedIn() && controller.getUserID() != challenge.getToUserID()) {
                req.setAttribute("errorMessage", "Invalid URL");
                req.getRequestDispatcher("WEB-INF/error-message.jsp").forward(req, resp);
                return;
            }

            req.setAttribute("quiz", quiz);

            List<Long> commentIDs = commentDAO.getQuizComments(quiz.getID(), 0, Long.MAX_VALUE);
            List<QuizComment> comments = commentIDs.stream().map(commentDAO::getCommentByID).collect(Collectors.toList());

            if (quiz.getTimer() != 0) {
                SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                String timeString = df.format(new Date(quiz.getTimer() * 1000L));
                req.setAttribute("timeLimit", timeString);
            }

            QuizRatingsDAO quizRatingsDAO = (QuizRatingsDAO) req.getServletContext().getAttribute(QuizRatingsDAO.ATTR_NAME);
            long quizRatingCount = quizRatingsDAO.getQuizRatingCount(quiz.getID());
            Double quizRatingAvg = quizRatingsDAO.getQuizRatingSum(quiz.getID()) / (double) (quizRatingCount == 0 ? 1 : quizRatingCount);
            int myQuizRating = 0;
            Long userID = (Long) req.getSession().getAttribute("userID");
            if(userID != null) {
                QuizRating userRating = quizRatingsDAO.getRatingByUser(quiz.getID(), userID);
                myQuizRating = userRating != null ? userRating.getRating() : 0;
            }
            req.setAttribute("myQuizRating", myQuizRating);
            req.setAttribute("ratingCount", quizRatingCount);
            req.setAttribute("ratingAvg", quizRatingAvg);

            req.setAttribute("comments", comments);
            req.getRequestDispatcher("WEB-INF/quiz.jsp").forward(req, resp);
        } catch (NumberFormatException ignored) {
            req.setAttribute("errorMessage", "Invalid URL");
            req.getRequestDispatcher("WEB-INF/error-message.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            ChallengeDAO challengeDAO = (ChallengeDAO) req.getServletContext().getAttribute(ChallengeDAO.ATTR_NAME);
            QuizDAO quizDAO = (QuizDAO) req.getServletContext().getAttribute(QuizDAO.ATTR_NAME);
            QuizWebsiteController controller = new QuizWebsiteController(req, resp);
            QuizHelper quizHelper = new QuizHelper(req);
            Challenge challenge = quizHelper.getChallenge();
            Quiz quiz = quizHelper.getQuiz();

            if (!controller.assertLoggedIn()) {
                return;
            }

            if (quiz == null || challenge != null && controller.isLoggedIn() && controller.getUserID() != challenge.getToUserID()) {
                req.setAttribute("errorMessage", "Invalid URL");
                req.getRequestDispatcher("WEB-INF/error-message.jsp").forward(req, resp);
                return;
            }

            UserSessionsDAO userSessionsDAO = (UserSessionsDAO) req.getServletContext().getAttribute(UserSessionsDAO.ATTR_NAME);

            if (controller.isActiveQuiz()) {
                req.setAttribute("errorMessage", "Already in a quiz!");
                req.getRequestDispatcher("WEB-INF/error-message.jsp").forward(req, resp);
            } else {
                userSessionsDAO.insertSession(new UserSession(controller.getUserID(), quiz.getID(), quiz.getTimer()));
                if (challenge != null) {
                    quizHelper.endChallenge();
                }
                req.getRequestDispatcher("activeQuiz").forward(req, resp);
            }

        } catch (NumberFormatException ignored) {
            req.setAttribute("errorMessage", "Invalid URL!");
            req.getRequestDispatcher("WEB-INF/error-message.jsp").forward(req, resp);
        }
    }
}
