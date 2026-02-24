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
import java.util.List;
import model.User;

/**
 *
 * @author Legion
 */
@WebServlet(name = "DashboardController", urlPatterns = {"/dashboard"})
public class DashboardController extends HttpServlet {

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
        UserDAO userDAO = new UserDAO();
        if (action == null) {
            action = "all";
        }

        switch (action) {
            case "all":
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;
            case "admin":
                List<User> list = userDAO.getAllUser();
                int totalUsers = list.size();
                request.setAttribute("totalUsers", totalUsers);
                request.setAttribute("userList", list);
                request.setAttribute("home_view", "/admin/adminDashboard.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;
            case "profile": 
                HttpSession session = request.getSession();
                User user = (User)session.getAttribute("user");
                
                if(user == null){
                    response.sendRedirect("login");
                }
                
                request.setAttribute("user", user);
                request.setAttribute("home_view", "profile.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                
                
                
                
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
