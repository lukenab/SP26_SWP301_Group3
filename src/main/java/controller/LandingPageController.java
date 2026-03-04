/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.CourseDAO;
import dao.LeadDAO;
import dao.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Course;
import model.Lead;

/**
 *
 * @author Legion
 */
@WebServlet(name = "LandingPageController", urlPatterns = {"/landingPage"})
public class LandingPageController extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
        CourseDAO courseDAO = new CourseDAO();
        if (action == null) {
            action = "all";
        }

        switch (action) {
            case "all":
                String keyword = request.getParameter("keyword");
                List<Course> list;
                if (isBlank(keyword)) {
                    list = courseDAO.getActiveCourses();
                } else {
                    list = courseDAO.searchActiveCourses(keyword.trim());
                }
                request.setAttribute("courseList", list);
                request.setAttribute("searchKeyword", isBlank(keyword) ? "" : keyword.trim());
                request.getRequestDispatcher("landingPage.jsp").forward(request, response);
                break;
            default:
                response.sendRedirect("landingPage");
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

        if (!"createLead".equals(action)) {
            response.sendRedirect("landingPage");
            return;
        }

        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String note = request.getParameter("note");
        String interestedCourseID = request.getParameter("interestedCourseID");

        if (isBlank(fullName) || isBlank(phone) || isBlank(interestedCourseID)) {
            session.setAttribute("message", "Please provide full name, phone and course.");
            session.setAttribute("messageType", "error");
            response.sendRedirect("landingPage");
            return;
        }

        String normalizedEmail = isBlank(email) ? null : email.trim();
        LeadDAO leadDAO = new LeadDAO();
        UserDAO userDAO = new UserDAO();

        if (normalizedEmail != null
                && (userDAO.isEmailExists(normalizedEmail) || leadDAO.isEmailExists(normalizedEmail))) {
            session.setAttribute("message", "Email already exists. Please use another email.");
            session.setAttribute("messageType", "error");
            response.sendRedirect("landingPage");
            return;
        }

        try {
            Lead lead = new Lead();
            lead.setFullName(fullName.trim());
            lead.setPhone(phone.trim());
            lead.setEmail(normalizedEmail);
            lead.setInterestedCourseID(Integer.parseInt(interestedCourseID));
            lead.setStatus("New");
            lead.setNote(isBlank(note) ? "Submitted from landing page." : note.trim());

            leadDAO.insertLead(lead);

            session.setAttribute("message", "Information submitted successfully. Our consultant will contact you soon.");
            session.setAttribute("messageType", "success");
        } catch (NumberFormatException ex) {
            session.setAttribute("message", "Invalid course selection.");
            session.setAttribute("messageType", "error");
        }

        response.sendRedirect("landingPage");
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
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
