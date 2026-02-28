/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

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

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UserDAO userDAO = new UserDAO();
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        HttpSession loginSession = request.getSession();

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

        User validUser = userDAO.checkLogin(email, password);
        if (validUser != null) {
            userDAO.resetFailedLogin(email);
            loginSession.setAttribute("user", validUser);

            int roleId = validUser.getRole().getRoleId();
            if (roleId == 1) {
                response.sendRedirect("dashboard?action=admin");
            } else if (roleId == 2 || roleId == 3 || roleId == 4) {
                response.sendRedirect("dashboard?action=all");
            } else {
                response.sendRedirect("dashboard");
            }
        }
        
        else{
            userDAO.incrementFailedLogin(email);
            int attempts = userByEmail.getFailedLoginAttempts() + 1;
            
            if(attempts >= 5){
                userDAO.lockUser(email);
                loginSession.setAttribute("loginMessage", "You have entered the wrong password 5 times. Your account is LOCKED.");
            }
            else{
                int remainingAttempts = 5 - attempts;
                loginSession.setAttribute("loginMessage", "Invalid password! You have " + remainingAttempts + " attempts left.");
            }
            response.sendRedirect("login");
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
