/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.SettingDAO;
import dao.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

/**
 *
 * @author Legion
 */
@WebServlet(name = "LoginController", urlPatterns = {"/login"})
public class LoginController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UserDAO userDAO = new UserDAO();
        SettingDAO settingDAO = new SettingDAO(); 
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        HttpSession loginSession = request.getSession();

        int maxAttempts = 5;
        int timeoutMinutes = 30;
        
        String maxAttemptsStr = settingDAO.getSettingValue("MAX_LOGIN_ATTEMPTS");
        if (maxAttemptsStr != null) {
            try { maxAttempts = Integer.parseInt(maxAttemptsStr); } 
            catch (NumberFormatException e) { System.out.println("Fail parse MAX_LOGIN_ATTEMPTS"); }
        }
        
        String timeoutStr = settingDAO.getSettingValue("SESSION_TIMEOUT_MINUTES");
        if (timeoutStr != null) {
            try { timeoutMinutes = Integer.parseInt(timeoutStr); } 
            catch (NumberFormatException e) { System.out.println("Fail parse SESSION_TIMEOUT_MINUTES"); }
        }

        User userByEmail = userDAO.getUserByEmail(email);
        
        if (userByEmail == null) {
            loginSession.setAttribute("loginMessage", "Invalid email or password!");
            response.sendRedirect("login");
            return;
        }

        if (!userByEmail.getStatus()) {
            loginSession.setAttribute("loginMessage", "Your account has been deactivated. Please contact Admin.");
            response.sendRedirect("login");
            return;
        }

        if (userByEmail.getIsLocked()) {
            loginSession.setAttribute("loginMessage", "Your account is LOCKED due to multiple attempts. Please contact admin.");
            response.sendRedirect("login");
            return;
        }

        String hashPassword = userDAO.hashMD5(password);
        
        if (userByEmail.getPassword().equals(hashPassword)) {
            userDAO.updateFailLoginAttempts(userByEmail.getUserId(), 0);
            loginSession.setAttribute("user", userByEmail);
            
            loginSession.setMaxInactiveInterval(timeoutMinutes * 60);

            int roleId = userByEmail.getRole().getRoleId();
            if (roleId == 1) {
                response.sendRedirect("dashboard?action=admin");
            } else if (roleId == 2 || roleId == 3 || roleId == 4) {
                response.sendRedirect("dashboard?action=all");
            } else {
                response.sendRedirect("dashboard");
            }
        }
        else {
            int attempts = userByEmail.getFailedLoginAttempts() + 1;
            userDAO.updateFailLoginAttempts(userByEmail.getUserId(), attempts);
            
            if(attempts >= maxAttempts){
                userDAO.toggleLockUser(userByEmail.getUserId(), true);
                loginSession.setAttribute("loginMessage", "You have entered the wrong password " + maxAttempts + " times. Your account is LOCKED.");
            }
            else{
                int remainingAttempts = maxAttempts - attempts;
                loginSession.setAttribute("loginMessage", "Invalid password! You have " + remainingAttempts + " attempts left.");
            }
            response.sendRedirect("login");
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}