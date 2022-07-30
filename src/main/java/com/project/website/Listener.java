package com.project.website;

import com.project.website.DAOs.*;
import com.project.website.Objects.Category;
import com.project.website.Objects.questions.QuestionEntry;
import com.project.website.Objects.questions.TextQuestion;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;

@WebListener
public class Listener implements ServletContextListener, HttpSessionListener, HttpSessionAttributeListener {

    public Listener() {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        /* This method is called when the servlet context is initialized(when the Web application is deployed). */
        // initialize database connection

        DataSource src = new QuizSiteDataSource();
        // initialize user DAO
        UserDAO userDAO = new UserDAOSQL(src);
        CategoryDAO categoryDAO = new CategoryDAOSQL(src);
        QuestionDAO questionDAO = new QuestionDAOSQL(src);
        QuizDAO quizDAO = new QuizDAOSQL(src);
        QuizCommentDAO quizCommentDAO = new QuizCommentDAOSQL(src);

        /* Test
        int success = categoryDAO.insertCategory(new Category("AAA"));

        success = questionDAO.insertQuestion(new QuestionEntry(1, 1, new TextQuestion("FUCK ME?", Collections.singletonList("YES"))));
        */
        // set the DAO as a context attribute
        sce.getServletContext().setAttribute("UserDAO", userDAO);
        sce.getServletContext().setAttribute("QuestionDAO", questionDAO);
        sce.getServletContext().setAttribute("CategoryDAO", categoryDAO);
        sce.getServletContext().setAttribute("QuizDAO", quizDAO);
        sce.getServletContext().setAttribute(QuizCommentDAO.ATTR_NAME, quizCommentDAO);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        /* This method is called when the servlet Context is undeployed or Application Server shuts down. */
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        /* Session is created. */
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        /* Session is destroyed. */
    }

    @Override
    public void attributeAdded(HttpSessionBindingEvent sbe) {
        /* This method is called when an attribute is added to a session. */
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent sbe) {
        /* This method is called when an attribute is removed from a session. */
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent sbe) {
        /* This method is called when an attribute is replaced in a session. */
    }
}
