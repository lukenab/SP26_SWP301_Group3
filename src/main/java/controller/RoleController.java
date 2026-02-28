/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.RoleDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Role;

/**
 *
 * @author Legion
 */
@WebServlet(name = "RoleController", urlPatterns = {"/role"})
public class RoleController extends HttpServlet {

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
        RoleDAO roleDAO = new RoleDAO();
        List<Role> list = roleDAO.getAllRole();
        String action = request.getParameter("action");
        if (action == null) {
            action = "all";
        }

        switch (action) {
            case "all":
                request.setAttribute("roleList", list);
                request.setAttribute("home_view", "/admin/manageRole.jsp");
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
        String action = request.getParameter("action");
        RoleDAO roleDAO = new RoleDAO();
        if (action == null) {
            action = "all";
        }

        switch (action) {
            case "permission":
                int roleId = Integer.parseInt(request.getParameter("roleId"));

                boolean manageUser = request.getParameter("manageUser") != null;
                boolean manageCourse = request.getParameter("manageCourse") != null;
                boolean manageFinance = request.getParameter("manageFinance") != null;

                boolean isUpdated = roleDAO.updateRolePermissions(roleId, manageUser, manageCourse, manageFinance);

                HttpSession permissionSession = request.getSession();
                if (isUpdated) {
                    permissionSession.setAttribute("message", "Permissions updated successfully!");
                    permissionSession.setAttribute("messageType", "success");
                } else {
                    permissionSession.setAttribute("message", "Failed to update permissions!");
                    permissionSession.setAttribute("messageType", "error");
                }

                response.sendRedirect("role");

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
