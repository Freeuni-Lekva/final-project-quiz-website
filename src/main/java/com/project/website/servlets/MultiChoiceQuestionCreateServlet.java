package com.project.website.servlets;

import com.project.website.DAOs.QuestionDAO;
import com.project.website.Objects.questions.MultiChoiceQuestion;
import com.project.website.Objects.questions.QuestionEntry;
import com.project.website.Objects.questions.TextQuestion;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "MultiChoiceQuestionCreateServlet", value = "/create/multi-choice-question")
public class MultiChoiceQuestionCreateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getSession().getAttribute("userID") == null)
            response.sendRedirect("/final_project_quiz_website_war_exploded/login");
        else
            request.getRequestDispatcher("/WEB-INF/multi-choice-question-create.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long creator = (Long) request.getSession().getAttribute("userID");
        if(creator == null) {
            response.sendRedirect("/final_project_quiz_website_war_exploded/login");
            return;
        }

        String questionTitle = request.getParameter("title");
        String questionStatement = request.getParameter("statement");
        int categoryID = Integer.parseInt(request.getParameter("category"));
        List<String> questionAnswers = Arrays.asList(request.getParameterValues("answer"));
        List<String> questionChoices = Arrays.asList(request.getParameterValues("choice"));

        QuestionDAO questionDAO = (QuestionDAO) request.getServletContext().getAttribute(QuestionDAO.ATTR_NAME);
        QuestionEntry newEntry = new QuestionEntry(Math.toIntExact(creator), categoryID, new MultiChoiceQuestion(questionStatement, questionAnswers, questionChoices));
        questionDAO.insertQuestion(newEntry);
    }
}
