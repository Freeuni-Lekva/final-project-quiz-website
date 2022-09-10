package com.project.website.servlets;

import com.google.gson.Gson;
import com.project.website.DAOs.NotificationDAO;
import com.project.website.Objects.Notification;
import com.project.website.Objects.NotificationBasket;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name="NotificationServlet", value="/getNotifications")
public class NotificationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        QuizWebsiteController controller = new QuizWebsiteController(req, resp);
        NotificationDAO notificationDAO = (NotificationDAO) req.getServletContext().getAttribute(NotificationDAO.ATTR_NAME);
        if (controller.isLoggedIn()) {
            int userID = controller.getUserID();
            List<Notification> list = notificationDAO.getUserNotifications(userID, 100);
            NotificationBasket basket = new NotificationBasket(list.size(), list.subList(0, Math.min(list.size(), 10)));
            Gson gson = new Gson();
            String result = gson.toJson(basket);
            controller.sendJson(result);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        QuizWebsiteController controller = new QuizWebsiteController(req, resp);
        NotificationDAO notificationDAO = (NotificationDAO) req.getServletContext().getAttribute(NotificationDAO.ATTR_NAME);
        if (controller.isLoggedIn()) {
            int userID = controller.getUserID();
            List<Notification> list = notificationDAO.getUserNotifications(userID, 100);
            list.subList(0, Math.min(list.size(), 10)).forEach(notification -> notificationDAO.deleteNotification(notification.getId()));
        }
    }
}
