/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.LeadDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Lead;

/**
 *
 * @author LienNTK
 */
@WebServlet(name = "LeadController", urlPatterns = {"/lead"})

public class LeadController extends HttpServlet {

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
        LeadDAO leadDAO = new LeadDAO();
        if (action == null) {
            action = "all";
        }
        switch (action) {
            case "all":
                List<Lead> leadList = leadDAO.getAllLeads();

                int totalLeads = leadList.size();

                request.setAttribute("leadList", leadList);
                request.setAttribute("totalLeads", totalLeads);
                request.setAttribute("home_view", "Lead/viewLead.jsp");
                request.getRequestDispatcher("dashboard").forward(request, response);
                break;
            case "add":
                request.setAttribute("home_view", "Lead/AddLead.jsp");
                request.getRequestDispatcher("dashboard").forward(request, response);
                break;

            case "update":
                int id = Integer.parseInt(request.getParameter("id"));
                Lead lead = leadDAO.getLeadByID(id);

                request.setAttribute("lead", lead);
                request.setAttribute("home_view", "Lead/UpdateLead.jsp");
                request.getRequestDispatcher("dashboard").forward(request, response);
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
        LeadDAO leadDAO = new LeadDAO();

        if ("create".equals(action)) {

            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            int interestedCourseID = Integer.parseInt(request.getParameter("interestedCourseID"));
            String status = request.getParameter("status");
            String note = request.getParameter("note");

            Lead lead = new Lead();
            lead.setFullName(fullName);
            lead.setEmail(email);
            lead.setPhone(phone);
            lead.setInterestedCourseID(interestedCourseID);
            lead.setStatus(status);
            lead.setNote(note);
            leadDAO.insertLead(lead);

            response.sendRedirect("lead?action=all");
        } else if ("update".equals(action)) {

            int id = Integer.parseInt(request.getParameter("leadID"));
            String status = request.getParameter("status");
            String note = request.getParameter("note");

            leadDAO.updateLeadStatus(id, status, note);

            response.sendRedirect("lead?action=all");
        } else if ("delete".equals(action)) {

            String idParam = request.getParameter("leadID");

            if (idParam != null && !idParam.isEmpty()) {
                int id = Integer.parseInt(idParam);
                leadDAO.deleteLead(id);   
            }

            response.sendRedirect("lead?action=all");
        }
    }
}
