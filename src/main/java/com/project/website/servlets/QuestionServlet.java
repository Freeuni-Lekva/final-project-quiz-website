package com.project.website.servlets;

import com.project.website.DAOs.QuestionDAO;
import com.project.website.Objects.questions.QuestionEntry;
import com.project.website.utils.JSPAttributePair;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "QuestionServlet", value = "/question")
public class QuestionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int questionID = Integer.parseInt(req.getParameter("questionID"));
        QuestionDAO questionDAO = (QuestionDAO) req.getServletContext().getAttribute("QuestionDAO");
        QuestionEntry questionEntry = questionDAO.getQuestionById(questionID);

        if (questionEntry == null) {
            //TODO redirect to missing page
            return;
        }

        for (JSPAttributePair attributePair : questionEntry.getQuestion().getJSPParams()) {
            req.setAttribute(attributePair.getAttributeName(), attributePair.getAttributeValue());
        }
        req.setAttribute("title", "test"); //TODO implement title logic
        req.getRequestDispatcher(questionEntry.getQuestion().getJSP()).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //TODO handle answer
    }
}
