package com.google.codeu.servlets;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.Message;

import java.io.IOException;

@WebServlet("/delete")
public class DeleteMessageServlet extends HttpServlet{

  private Datastore datastore;

  @Override
  public void init() {
   datastore = new Datastore();
  }

 @Override
 public void doGet(HttpServletRequest request, HttpServletResponse response)
   throws IOException {

     UserService userService = UserServiceFactory.getUserService();
     String user = userService.getCurrentUser().getEmail();

     String messageID = request.getParameter("message-id");
     datastore.deleteMessage(messageID);

     System.out.print("before redirect");
     response.sendRedirect("/user-page.html?user=" + user);
     System.out.print("after redirect");

 }
}
