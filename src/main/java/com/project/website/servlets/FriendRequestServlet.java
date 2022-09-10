package com.project.website.servlets;

import com.project.website.DAOs.FriendRequestDAO;
import com.project.website.DAOs.FriendshipDAO;
import com.project.website.Objects.listeners.QuizWebsiteListener;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "FriendRequestServlet", value = "/add_friend")
public class FriendRequestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("home");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FriendRequestDAO friendRequestDAO = (FriendRequestDAO) req.getServletContext().getAttribute(FriendRequestDAO.ATTR_NAME);
        FriendshipDAO friendshipDAO = (FriendshipDAO) req.getServletContext().getAttribute(FriendshipDAO.ATTR_NAME);
        Long userID = (Long) req.getSession().getAttribute("userID");
        Long receiverID = Long.parseLong(req.getParameter("receiver_id"));
        String action = req.getParameter("action");
        if(userID == null) {
            resp.sendRedirect("login");
        }
        else if(action.equals("send")){
            if(friendRequestDAO.checkIfFriendRequestSent(receiverID, userID)) {
                friendRequestDAO.removeFriendRequest(receiverID, userID);
                friendshipDAO.addFriendship(userID, receiverID);

                QuizWebsiteListener listener = (QuizWebsiteListener) req.getServletContext().getAttribute("listener");
                listener.onFriendAdded(Math.toIntExact(userID), Math.toIntExact(receiverID));
            }
            else {
                friendRequestDAO.addFriendRequest(userID, receiverID);
            }
            resp.sendRedirect("profile?id=" + receiverID); //maybe do something else?
        }
        else if(action.equals("remove")){
            friendshipDAO.removeFriendship(userID, receiverID);
            friendRequestDAO.removeFriendRequest(userID, receiverID);
            friendRequestDAO.removeFriendRequest(receiverID, userID);
            resp.sendRedirect("profile?id=" + receiverID);
        }
        else {
            resp.sendRedirect("home");//Error?
        }
    }
}
