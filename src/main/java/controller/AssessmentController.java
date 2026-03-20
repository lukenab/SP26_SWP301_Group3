/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AssessmentDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author Legion
 */
@WebServlet(name = "AssessmentController", urlPatterns = {"/assessment"})
public class AssessmentController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        AssessmentDAO assessmentDAO = new AssessmentDAO();
        HttpSession session = request.getSession();
        
        if (action == null) {
            action = "list";
        }
        
        switch (action) {
            case "delete":
                try {
                    int assessmentId = Integer.parseInt(request.getParameter("assessmentId"));
                    int courseId = Integer.parseInt(request.getParameter("courseId"));
                    
                    if (assessmentDAO.deleteAssessment(assessmentId)) {
                        session.setAttribute("message", "Assessment deleted successfully");
                        session.setAttribute("messageType", "success");
                    } else {
                        session.setAttribute("message", "Failed to delete assessment");
                        session.setAttribute("messageType", "error");
                    }
                    
                    response.sendRedirect(request.getContextPath() + "/course?action=assessment&courseId=" + courseId);
                } catch (NumberFormatException e) {
                    session.setAttribute("message", "Invalid ID");
                    session.setAttribute("messageType", "error");
                    response.sendRedirect(request.getContextPath() + "/course?action=all");
                }
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        AssessmentDAO assessmentDAO = new AssessmentDAO();
        HttpSession session = request.getSession();
        
        if (action == null) {
            action = "list";
        }
        
        switch (action) {
            case "add":
                try {
                    int courseId = Integer.parseInt(request.getParameter("courseId"));
                    String assessmentName = request.getParameter("assessmentName").trim();
                    double weight = Double.parseDouble(request.getParameter("weight"));
                    
                    // Validation
                    if (assessmentName.isEmpty()) {
                        session.setAttribute("message", "Assessment name cannot be empty");
                        session.setAttribute("messageType", "error");
                    } else if (assessmentDAO.checkAssessmentNameExists(courseId, assessmentName)) {
                        session.setAttribute("message", "Assessment name '" + assessmentName + "' already exists in this course");
                        session.setAttribute("messageType", "error");
                    } else if (weight < 0 || weight > 100) {
                        session.setAttribute("message", "Weight must be between 0 and 100");
                        session.setAttribute("messageType", "error");
                    } else if (assessmentDAO.getTotalWeightByCourse(courseId) + weight > 100) {
                        double total = assessmentDAO.getTotalWeightByCourse(courseId) + weight;
                        session.setAttribute("message", "Total weight cannot exceed 100%. Current: " + assessmentDAO.getTotalWeightByCourse(courseId) + "% + New: " + weight + "% = " + total + "%");
                        session.setAttribute("messageType", "error");
                    } else if (assessmentDAO.addAssessment(courseId, assessmentName, weight)) {
                        session.setAttribute("message", "Assessment added successfully");
                        session.setAttribute("messageType", "success");
                    } else {
                        session.setAttribute("message", "Failed to add assessment");
                        session.setAttribute("messageType", "error");
                    }
                    
                    response.sendRedirect(request.getContextPath() + "/course?action=assessment&courseId=" + courseId);
                } catch (NumberFormatException e) {
                    session.setAttribute("message", "Invalid input");
                    session.setAttribute("messageType", "error");
                    response.sendRedirect(request.getContextPath() + "/course?action=all");
                }
                break;
                
            case "update":
                try {
                    int assessmentId = Integer.parseInt(request.getParameter("assessmentId"));
                    int courseId = Integer.parseInt(request.getParameter("courseId"));
                    String assessmentName = request.getParameter("assessmentName").trim();
                    double weight = Double.parseDouble(request.getParameter("weight"));
                    
                    // Validation
                    if (assessmentName.isEmpty()) {
                        session.setAttribute("message", "Assessment name cannot be empty");
                        session.setAttribute("messageType", "error");
                    } else if (weight < 0 || weight > 100) {
                        session.setAttribute("message", "Weight must be between 0 and 100");
                        session.setAttribute("messageType", "error");
                    } else {
                        // Get old assessment for weight recalculation
                        AssessmentDAO dao = new AssessmentDAO();
                        model.Assessment oldAssessment = dao.getAssessmentById(assessmentId);
                        if (oldAssessment == null) {
                            session.setAttribute("message", "Assessment not found");
                            session.setAttribute("messageType", "error");
                        } else {
                            double currentTotal = dao.getTotalWeightByCourse(courseId);
                            double newTotal = currentTotal - oldAssessment.getWeight() + weight;
                            
                            if (newTotal > 100) {
                                session.setAttribute("message", "Total weight cannot exceed 100%. New total would be: " + newTotal + "%");
                                session.setAttribute("messageType", "error");
                            } else if (dao.updateAssessment(assessmentId, assessmentName, weight)) {
                                session.setAttribute("message", "Assessment updated successfully");
                                session.setAttribute("messageType", "success");
                            } else {
                                session.setAttribute("message", "Failed to update assessment");
                                session.setAttribute("messageType", "error");
                            }
                        }
                    }
                    
                    response.sendRedirect(request.getContextPath() + "/course?action=assessment&courseId=" + courseId);
                } catch (NumberFormatException e) {
                    session.setAttribute("message", "Invalid input");
                    session.setAttribute("messageType", "error");
                    response.sendRedirect(request.getContextPath() + "/course?action=all");
                }
                break;
                
            case "delete":
                try {
                    int assessmentId = Integer.parseInt(request.getParameter("assessmentId"));
                    int courseId = Integer.parseInt(request.getParameter("courseId"));
                    
                    if (assessmentDAO.deleteAssessment(assessmentId)) {
                        session.setAttribute("message", "Assessment deleted successfully");
                        session.setAttribute("messageType", "success");
                    } else {
                        session.setAttribute("message", "Failed to delete assessment");
                        session.setAttribute("messageType", "error");
                    }
                    
                    response.sendRedirect(request.getContextPath() + "/course?action=assessment&courseId=" + courseId);
                } catch (NumberFormatException e) {
                    session.setAttribute("message", "Invalid input");
                    session.setAttribute("messageType", "error");
                    response.sendRedirect(request.getContextPath() + "/course?action=all");
                }
                break;
                
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    @Override
    public String getServletInfo() {
        return "Assessment Controller";
    }
}
