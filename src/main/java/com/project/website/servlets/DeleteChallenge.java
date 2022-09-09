package com.project.website.servlets;

import com.project.website.DAOs.ChallengeDAO;
import com.project.website.Objects.Challenge;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "DeleteChallenge", value = "/deleteChallenge")
public class DeleteChallenge extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        QuizWebsiteController controller = new QuizWebsiteController(req, resp);
        if (!controller.assertLoggedIn()) {
            return;
        }

        ChallengeDAO challengeDAO = (ChallengeDAO) req.getServletContext().getAttribute(ChallengeDAO.ATTR_NAME);
        Challenge challenge = challengeDAO.getChallenge(Integer.parseInt(req.getParameter("challengeID")));

        if (challenge.getToUserID() == controller.getUserID()) {
            challengeDAO.deleteChallenge(challenge.getId());
            resp.sendRedirect("profile");
        } else {
            req.setAttribute("errorMessage", "Not your challenge to delete!");
            req.getRequestDispatcher("WEB-INF/error-message.jsp").forward(req, resp);
        }
    }
}
