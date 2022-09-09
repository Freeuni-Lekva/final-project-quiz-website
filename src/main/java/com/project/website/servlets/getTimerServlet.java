package com.project.website.servlets;

import com.google.gson.Gson;
import com.project.website.Objects.UserSession;
import com.project.website.utils.QuizTimer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

@WebServlet(name = "GetTimerServlet", value = "/getTimer")
public class getTimerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        QuizWebsiteController controller = new QuizWebsiteController(req, resp);
        if (controller.isActiveQuiz()) {
            UserSession session = controller.getUserSession();
            long timeLeft = 0;
            if (session.getTime() != 0) {
                timeLeft = session.getTime() - (Calendar.getInstance().getTimeInMillis() - session.getStartDate().getTime()) / 1000;
            }

            Gson gson = new Gson();
            String json = gson.toJson(new QuizTimer(timeLeft));

            if (json != null) {
                PrintWriter out = resp.getWriter();
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                out.print(json);
                out.flush();
            }
        }
    }
}
