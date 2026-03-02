/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "ForgotPasswordController", urlPatterns = {"/forgotPassword"})
public class ForgotPasswordController extends HttpServlet {

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
        request.getRequestDispatcher("forgotPassword.jsp").forward(request, response);
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
        String email = request.getParameter("email");
        UserDAO userDAO = new UserDAO();
        HttpSession session = request.getSession();

        User user = userDAO.getUserByEmail(email);
        if (user != null) {
            String plainPassword = EmailController.generateRandomPassword();
            String hashedNewPassword = userDAO.hashMD5(plainPassword);

            boolean isUpdated = userDAO.updatePassword(hashedNewPassword, user.getUserId());

            if (isUpdated) {
                boolean isEmailSent = EmailController.sendEmail(email, user.getFullName(), plainPassword);
                if (isEmailSent) {
                    session.setAttribute("message", "A new password has been sent to your email!");
                    session.setAttribute("messageType", "success");
                    response.sendRedirect("login.jsp");
                } else {
                    session.setAttribute("message", "Password reset but failed to send email. Please contact admin.");
                    session.setAttribute("messageType", "error");
                }
            } else {
                session.setAttribute("message", "System error! Cannot reset password.");
                session.setAttribute("messageType", "error");
                response.sendRedirect("forgotPassword");
            }
        }
        else{
            session.setAttribute("message", "This email is not registered in our system!");
            session.setAttribute("messageType", "error");
            response.sendRedirect("forgotPassword");
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
