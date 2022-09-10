package com.project.website.servlets;

import com.google.gson.Gson;
import com.project.website.DAOs.ChallengeDAO;
import com.project.website.DAOs.ChallengeDAOSQL;
import com.project.website.DAOs.UserDAO;
import com.project.website.Objects.Challenge;
import com.project.website.Objects.Quiz;
import com.project.website.Objects.User;
import com.project.website.Objects.listeners.QuizWebsiteListener;
import com.project.website.utils.ChallengeQuery;
import com.project.website.utils.ChallengeResponse;
import com.project.website.utils.QuestionsQuery;
import com.project.website.utils.QuizTimer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ChallengeServlet", value = "/challenge")
public class ChallengeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        QuizWebsiteController controller = new QuizWebsiteController(req, resp);
        ChallengeResponse response = new ChallengeResponse();
        ChallengeDAO challengeDAO = (ChallengeDAO) req.getServletContext().getAttribute(ChallengeDAO.ATTR_NAME);

        try {
            ChallengeQuery query = gson.fromJson(controller.getJsonBody(), ChallengeQuery.class);
            UserDAO userDAO = (UserDAO) req.getServletContext().getAttribute(UserDAO.ATTR_NAME);
            User user = userDAO.getUserByUsername(query.getUsername());
            int time = Integer.parseInt(query.getTime());
            int quizID = Integer.parseInt(query.getQuizID());

            if (!controller.isLoggedIn()) {
                response.setSuccess(false);
                response.setMessage("Log in to challenge!");
            } else if (query.getUsername() == null) {
                response.setSuccess(false);
                response.setMessage("Invalid username!");
            }  else if (user == null) {
                response.setSuccess(false);
                response.setMessage("No such user exists!");
            } else {
                QuizWebsiteListener listener = (QuizWebsiteListener) req.getServletContext().getAttribute("listener");
                listener.onChallengeSent(controller.getUserID(), Math.toIntExact(user.getId()));
                challengeDAO.insertChallenge(new Challenge(Math.toIntExact(user.getId()), controller.getUserID(), quizID, time));
                response.setSuccess(true);
                response.setMessage("Challenge sent to " + query.getUsername() + "!");
            }

        } catch(NumberFormatException ignored) {
            response.setSuccess(false);
            response.setMessage("Invalid time!");
        }

        String json = gson.toJson(response);

        if (json != null) {
            controller.sendJson(json);
        }
    }
}
