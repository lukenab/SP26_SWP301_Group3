/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.RoleDAO;
import dao.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import model.Role;
import model.User;

/**
 *
 * @author Legion
 */
@WebServlet(name = "UserController", urlPatterns = {"/user"})
public class UserController extends HttpServlet {

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
        UserDAO userDAO = new UserDAO();
        RoleDAO roleDAO = new RoleDAO();
        String action = request.getParameter("action");
        if (action == null) {
            action = "all";
        }
        
        switch (action) {
            case "all":
                List<User> list = userDAO.getAllUser();
                request.setAttribute("userList", list);
                request.setAttribute("home_view", "/admin/manageUser.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;
            case "add":
                List<Role> roleList = roleDAO.getAllRole();
                request.setAttribute("roleList", roleList);
                request.setAttribute("home_view", "/admin/createUser.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;
            case "inActivate":
                int dId = Integer.parseInt(request.getParameter("id"));
                User uDelete = userDAO.getUserById(dId);
                request.setAttribute("uDelete", uDelete);
                request.setAttribute("home_view", "/admin/deleteUser.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
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
        UserDAO userDAO = new UserDAO();
        RoleDAO roleDAO = new RoleDAO();
        String action = request.getParameter("action");
        if (action == null) {
            action = "all";
        }
        
        switch (action) {
            case "add":
                String fullName = request.getParameter("fullName");
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                String phone = request.getParameter("phone");
                String address = request.getParameter("address");
                if (address == null || address.trim().isEmpty()) {
                    address = "";
                }
                Boolean gender = Boolean.valueOf(request.getParameter("gender"));
                java.sql.Date dob = java.sql.Date.valueOf(request.getParameter("dob"));
                String avatar = request.getParameter("avatar");
                if (avatar == null || avatar.trim().isEmpty()) {
                    avatar = "https://cdn-icons-png.flaticon.com/512/149/149071.png";
                }
                Boolean status = Boolean.valueOf(request.getParameter("status"));
                
                int roleId = Integer.parseInt(request.getParameter("roleId"));
                Role role = roleDAO.getRoleByID(roleId);
                
                Boolean isAdded = userDAO.addNewUser(fullName, email, password, phone, address, gender, dob, avatar, status, role);
                
                HttpSession aSession = request.getSession();
                if (isAdded) {
                    aSession.setAttribute("message", "Add New User Successfully!");
                    aSession.setAttribute("messageType", "success");
                } else {
                    aSession.setAttribute("message", "Fail To Add New User");
                    aSession.setAttribute("messageType", "error");
                }
                response.sendRedirect("user");
                break;
            case "inActivate":
                int id = Integer.parseInt(request.getParameter("id"));
                Boolean inactivateSuccess = userDAO.inactivateUser(id);
                HttpSession uSession = request.getSession();
                if (inactivateSuccess) {
                    uSession.setAttribute("message", "Inactivate User Success!");
                    uSession.setAttribute("messageType", "success");
                } else {
                    uSession.setAttribute("message", "Fail To Inactivate User");
                    uSession.setAttribute("messageType", "error");
                }
                response.sendRedirect("user");
                break;
            case "activate":
                int aId = Integer.parseInt(request.getParameter("id"));
                Boolean activateSuccess = userDAO.activateUser(aId);
                HttpSession activateSession = request.getSession();
                if (activateSuccess) {
                    activateSession.setAttribute("message", "Activate User Success!");
                    activateSession.setAttribute("messageType", "success");
                } else {
                    activateSession.setAttribute("message", "Fail To Activate User");
                    activateSession.setAttribute("messageType", "error");
                }
                response.sendRedirect("user");
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
