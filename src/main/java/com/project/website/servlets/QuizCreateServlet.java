package com.project.website.servlets;

import com.project.website.DAOs.QuestionDAO;
import com.project.website.DAOs.QuestionToQuizDAO;
import com.project.website.DAOs.QuizDAO;
import com.project.website.Objects.Quiz;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "QuizCreateServlet", value = "/create/quiz")
public class QuizCreateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getSession().getAttribute("userID") == null)
            response.sendRedirect("../login");
        else
            request.getRequestDispatcher("/WEB-INF/quiz-create.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        QuestionDAO questionDAO = (QuestionDAO) request.getServletContext().getAttribute(QuestionDAO.ATTR_NAME);
        QuestionToQuizDAO questionToQuizDAO = (QuestionToQuizDAO) request.getServletContext().getAttribute(QuestionToQuizDAO.ATTR_NAME);
        QuizDAO quizDAO = (QuizDAO) request.getServletContext().getAttribute(QuizDAO.ATTR_NAME);

        Long userID = (Long)request.getSession().getAttribute("userID");
        if(userID == null) {
            request.setAttribute("errorMessage", "ERROR! QUIZ CANNOT BE CREATED WITHOUT LOGGING IN");
        }
        List<Integer> questionIDs = Arrays.stream(request.getParameterValues("question")).map(Integer::parseInt).collect(Collectors.toList());
        String quizTitle = request.getParameter("title");
        String quizDescription = request.getParameter("description");
        Integer quizCategory = Integer.parseInt(request.getParameter("category"));
        String quizTimerParam = request.getParameter("timeLimit");
        int quizTimer = quizTimerParam == null ? 0 : Integer.parseInt(quizTimerParam);
        Quiz quiz = new Quiz(Math.toIntExact(userID), quizCategory, quizTitle, quizDescription, quizTimer);
        int newQuizID = quizDAO.insertQuiz(quiz);
        int success = 0;
        for(int i = 0; i < questionIDs.size(); i++) {
            if(questionDAO.getQuestionById(questionIDs.get(i)) != null) {
                questionToQuizDAO.insert(questionIDs.get(i), newQuizID, i + 1);
                success++;
            }
        }
        quizDAO.updateQuizLocalId(newQuizID, success + 1);

        response.sendRedirect("../create");
    }
}
