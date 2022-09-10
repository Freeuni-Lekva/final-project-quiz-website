package com.project.website.servlets;

import com.google.gson.Gson;
import com.project.website.DAOs.CategoryDAO;
import com.project.website.DAOs.Filters.*;
import com.project.website.DAOs.QuestionDAO;
import com.project.website.DAOs.UserDAO;
import com.project.website.Objects.questions.QuestionEntry;
import com.project.website.utils.QuestionJson;
import com.project.website.utils.QuestionsQuery;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet( name = "GetQuestions", value = "/getQuestions")
public class GetQuestionsServlet extends HttpServlet {

    private static final int MAX_QUESTIONS = 50;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        QuizWebsiteController controller = new QuizWebsiteController(req, resp);
        QuestionDAO questionDAO = (QuestionDAO) req.getServletContext().getAttribute(QuestionDAO.ATTR_NAME);
        CategoryDAO categoryDAO = (CategoryDAO) req.getServletContext().getAttribute(CategoryDAO.ATTR_NAME);
        QuestionsQuery query = gson.fromJson(controller.getJsonBody(), QuestionsQuery.class);
        UserDAO userDAO = (UserDAO) req.getServletContext().getAttribute(UserDAO.ATTR_NAME);

        List<QuestionEntry> questions;

        List<SQLFilter> filters = new ArrayList<>();
        if(query.getCategory() >= 0)
            filters.add(new CategoryFilter(query.getCategory()));

        Long userID = (Long) req.getSession().getAttribute("userID");
        if(userID != null && query.isShowMine())
            filters.add(new CreatorFilter(Math.toIntExact(userID)));
        filters.add(new ColumnLikeFilter("questionTitle","%" + query.getQuery() + "%"));
        questions = questionDAO.searchQuestions(new AndFilter(filters), query.getPage() * MAX_QUESTIONS, MAX_QUESTIONS);
        if (questions == null) {
            return;
        }
        List<QuestionJson> questionJsons = questions.stream()
                .map(questionEntry -> new QuestionJson(questionEntry.getId(),
                        questionEntry.getTitle(), questionEntry.getCreator_id(),
                        categoryDAO.getCategory(questionEntry.getCategory_id()).getCategoryName(),
                        userDAO.getUserByID(questionEntry.getCreator_id()).getUsername()))
                .collect(Collectors.toList());

        String returnJson = gson.toJson(questionJsons);

        if (returnJson != null) {
            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            out.print(returnJson);
            out.flush();
        }
    }
}
