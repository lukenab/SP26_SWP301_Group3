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
        String action = request.getParameter("action");
        
        if(action == null){
            action = "enterEmail";
        }
        
        HttpSession session = request.getSession();
        
        switch(action){
            case "enterEmail": 
                request.getRequestDispatcher("forgotPassword.jsp").forward(request, response);
                break;
            
            case "verifyOtp":
                if(session.getAttribute("resetEmail") == null){
                    response.sendRedirect("forgotPassword");
                } else{
                   request.getRequestDispatcher("verifyOtp.jsp").forward(request, response);
                }
                break;
              
            case "resetPassword": 
                Boolean canReset = (Boolean) session.getAttribute("canResetPassword");
                if(canReset != null && canReset){
                    request.getRequestDispatcher("resetPassword.jsp").forward(request, response);
                }
                else{
                    response.sendRedirect("forgotPassword");
                }
                break;
        }
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
        
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        UserDAO userDAO = new UserDAO();

        if (action == null) {
            action = "sendOTP"; 
        }

        switch (action) {
            case "sendOTP":
                String email = request.getParameter("email");
                User user = userDAO.getUserByEmail(email);

                if (user != null) {
                    String otp = EmailController.generateOTP();
                    boolean isEmailSent = EmailController.sendOTPEmail(email, user.getFullName(), otp);

                    if (isEmailSent) {
                        session.setAttribute("resetEmail", email);
                        session.setAttribute("otpCode", otp);
                        session.setMaxInactiveInterval(300); 

                        session.setAttribute("message", "An OTP has been sent to your email!");
                        session.setAttribute("messageType", "success");
                        // Đẩy sang giao diện nhập OTP
                        response.sendRedirect("forgotPassword?action=verifyOtp"); 
                    } else {
                        session.setAttribute("message", "Failed to send OTP email. Please try again.");
                        session.setAttribute("messageType", "error");
                        response.sendRedirect("forgotPassword");
                    }
                } else {
                    session.setAttribute("message", "This email is not registered in our system!");
                    session.setAttribute("messageType", "error");
                    response.sendRedirect("forgotPassword");
                }
                break;

            case "verifyOTP":
                String inputOtp = request.getParameter("otp");
                String sessionOtp = (String) session.getAttribute("otpCode");

                if (sessionOtp == null) {
                    session.setAttribute("message", "OTP has expired. Please request a new one.");
                    session.setAttribute("messageType", "error");
                    response.sendRedirect("forgotPassword");
                    break;
                }

                if (inputOtp != null && inputOtp.trim().equals(sessionOtp)) {
                    session.setAttribute("canResetPassword", true);
                    session.removeAttribute("otpCode"); 
                    response.sendRedirect("forgotPassword?action=resetPassword"); 
                } else {
                    session.setAttribute("message", "Invalid OTP code. Please try again.");
                    session.setAttribute("messageType", "error");
                    response.sendRedirect("forgotPassword?action=verifyOtp");
                }
                break;

            case "updatePassword":
                Boolean canReset = (Boolean) session.getAttribute("canResetPassword");
                String resetEmail = (String) session.getAttribute("resetEmail");

                if (canReset == null || !canReset || resetEmail == null) {
                    response.sendRedirect("forgotPassword");
                    break;
                }

                String newPass = request.getParameter("newPassword");
                String confirmPass = request.getParameter("confirmPassword");

                if (!newPass.equals(confirmPass)) {
                    session.setAttribute("message", "Passwords do not match!");
                    session.setAttribute("messageType", "error");
                    response.sendRedirect("forgotPassword?action=resetPassword");
                    break;
                }

                User targetUser = userDAO.getUserByEmail(resetEmail);
                String hashedNewPassword = userDAO.hashMD5(newPass);
                boolean isUpdated = userDAO.updatePassword(hashedNewPassword, targetUser.getUserId());

                if (isUpdated) {
                    session.removeAttribute("canResetPassword");
                    session.removeAttribute("resetEmail");

                    session.setAttribute("message", "Password reset successfully! Please login.");
                    session.setAttribute("messageType", "success");
                    response.sendRedirect("login");
                } else {
                    session.setAttribute("message", "System error. Cannot update password.");
                    session.setAttribute("messageType", "error");
                    response.sendRedirect("forgotPassword?action=resetPassword");
                }
                break;
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
