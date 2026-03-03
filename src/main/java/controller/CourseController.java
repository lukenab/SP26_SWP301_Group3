/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.CourseDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.math.BigDecimal;
import model.Course;

/**
 *
 * @author Legion
 */
@WebServlet(name = "CourseController", urlPatterns = {"/course"})
public class CourseController extends HttpServlet {

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
        if(action == null){
            action = "all";
        }
        
        switch(action){
            case "all":
                List<Course> list = courseDAO.getAllCourse();
                int totalCourse = list.size();
                request.setAttribute("totalCourse", totalCourse);
                request.setAttribute("courseList", list);
                request.setAttribute("home_view", "/academic/course_list.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;
            case "active":
                List<Course> activeList = courseDAO.getActiveCourses();
                request.setAttribute("courseList", activeList);
                request.setAttribute("home_view", "/academic/course_list.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;
            case "details":
                String courseIdParam = request.getParameter("courseId");
                if(courseIdParam != null && !courseIdParam.isEmpty()){
                    try {
                        int courseId = Integer.parseInt(courseIdParam);
                        Course course = courseDAO.getCourseById(courseId);
                        if(course != null){
                            request.setAttribute("course", course);
                            request.setAttribute("home_view", "/academic/course_details.jsp");
                            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                        } else {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        }
                    } catch(NumberFormatException e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
                break;
            case "publicDetails":
                String publicCourseIdParam = request.getParameter("courseId");
                if (publicCourseIdParam == null || publicCourseIdParam.isEmpty()) {
                    publicCourseIdParam = request.getParameter("id");
                }

                if (publicCourseIdParam != null && !publicCourseIdParam.isEmpty()) {
                    try {
                        int courseId = Integer.parseInt(publicCourseIdParam);
                        Course course = courseDAO.getCourseById(courseId);
                        if (course != null && course.isStatus()) {
                            request.setAttribute("course", course);
                            request.getRequestDispatcher("courseDetailPublic.jsp").forward(request, response);
                        } else {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        }
                    } catch (NumberFormatException e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
                break;
            case "add":
                // Show add form
                request.setAttribute("course", new Course());
                request.setAttribute("formAction", "add");
                request.setAttribute("pageTitle", "Add New Course");
                request.setAttribute("home_view", "/academic/course_form.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;
            case "edit":
                String editCourseIdParam = request.getParameter("courseId");
                if(editCourseIdParam != null && !editCourseIdParam.isEmpty()){
                    try {
                        int courseId = Integer.parseInt(editCourseIdParam);
                        Course course = courseDAO.getCourseById(courseId);
                        if(course != null){
                            request.setAttribute("course", course);
                            request.setAttribute("formAction", "update");
                            request.setAttribute("pageTitle", "Edit Course");
                            request.setAttribute("home_view", "/academic/editCourse.jsp");
                            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                        } else {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        }
                    } catch(NumberFormatException e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
                break;
            case "delete":
                String deleteCourseIdParam = request.getParameter("courseId");
                if(deleteCourseIdParam != null && !deleteCourseIdParam.isEmpty()){
                    try {
                        int courseId = Integer.parseInt(deleteCourseIdParam);
                        Course course = courseDAO.getCourseById(courseId);
                        if(course != null){
                            request.setAttribute("course", course);
                            request.setAttribute("home_view", "/academic/course_delete_confirm.jsp");
                            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                        } else {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        }
                    } catch(NumberFormatException e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
                break;
            case "search":
                String keyword = request.getParameter("keyword");
                List<Course> searchResults;
                if(keyword != null && !keyword.trim().isEmpty()) {
                    searchResults = courseDAO.searchCourses(keyword.trim());
                } else {
                    searchResults = courseDAO.getAllCourse();
                }
                request.setAttribute("courseList", searchResults);
                request.setAttribute("searchKeyword", keyword);
                request.setAttribute("home_view", "/academic/course_list.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;
            default:
                List<Course> defaultList = courseDAO.getAllCourse();
                request.setAttribute("courseList", defaultList);
                request.setAttribute("home_view", "/academic/course_list.jsp");
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
        CourseDAO courseDAO = new CourseDAO();
        
        if (action == null) {
            action = "list";
        }
        
        switch (action) {
            case "add":
                // Get form data
                String courseName = request.getParameter("courseName");
                String description = request.getParameter("description");
                String totalSlotsStr = request.getParameter("totalSlots");
                String tuitionFeeStr = request.getParameter("tuitionFee");
                String statusStr = request.getParameter("status");
                String images = request.getParameter("images");
                
                // Validate input
                if (courseName == null || courseName.trim().isEmpty()) {
                    request.setAttribute("errorMessage", "Course name is required");
                    request.setAttribute("course", new Course()); // Empty course for form
                    request.getRequestDispatcher("/academic/course_form.jsp").forward(request, response);
                    return;
                }
                
                try {
                    int totalSlots = Integer.parseInt(totalSlotsStr);
                    BigDecimal tuitionFee = new BigDecimal(tuitionFeeStr);
                    boolean status = Boolean.parseBoolean(statusStr);
                    
                    Course newCourse = new Course();
                    newCourse.setCourseName(courseName.trim());
                    newCourse.setDescription(description != null ? description.trim() : "");
                    newCourse.setTotalSlots(totalSlots);
                    newCourse.setTuitionFee(tuitionFee);
                    newCourse.setStatus(status);
                    newCourse.setImages(images != null ? images.trim() : "");
                    
                    boolean success = courseDAO.addCourse(newCourse);
                    if (success) {
                        response.sendRedirect(request.getContextPath() + "/course?action=all&message=Course added successfully");
                    } else {
                        request.setAttribute("errorMessage", "Failed to add course");
                        request.setAttribute("course", newCourse);
                        request.getRequestDispatcher("/academic/course_form.jsp").forward(request, response);
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("errorMessage", "Invalid number format for slots or fee");
                    request.setAttribute("course", new Course());
                    request.getRequestDispatcher("/academic/course_form.jsp").forward(request, response);
                }
                break;
                
            case "update":
                String courseIdStr = request.getParameter("courseId");
                if (courseIdStr == null || courseIdStr.isEmpty()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                
                try {
                    int courseId = Integer.parseInt(courseIdStr);
                    Course existingCourse = courseDAO.getCourseById(courseId);
                    if (existingCourse == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                    
                    // Get form data
                    String updCourseName = request.getParameter("courseName");
                    String updDescription = request.getParameter("description");
                    String updTotalSlotsStr = request.getParameter("totalSlots");
                    String updTuitionFeeStr = request.getParameter("tuitionFee");
                    String updStatusStr = request.getParameter("status");
                    String updImages = request.getParameter("images");
                    
                    // Validate input
                    if (updCourseName == null || updCourseName.trim().isEmpty()) {
                        request.setAttribute("errorMessage", "Course name is required");
                        request.setAttribute("course", existingCourse);
                        request.getRequestDispatcher("/academic/course_form.jsp").forward(request, response);
                        return;
                    }
                    
                    int updTotalSlots = Integer.parseInt(updTotalSlotsStr);
                    BigDecimal updTuitionFee = new BigDecimal(updTuitionFeeStr);
                    boolean updStatus = Boolean.parseBoolean(updStatusStr);
                    
                    existingCourse.setCourseName(updCourseName.trim());
                    existingCourse.setDescription(updDescription != null ? updDescription.trim() : "");
                    existingCourse.setTotalSlots(updTotalSlots);
                    existingCourse.setTuitionFee(updTuitionFee);
                    existingCourse.setStatus(updStatus);
                    existingCourse.setImages(updImages != null ? updImages.trim() : "");
                    
                    boolean success = courseDAO.updateCourse(existingCourse);
                    if (success) {
                        response.sendRedirect(request.getContextPath() + "/course?action=all&message=Course updated successfully");
                    } else {
                        request.setAttribute("errorMessage", "Failed to update course");
                        request.setAttribute("course", existingCourse);
                        request.getRequestDispatcher("/academic/course_form.jsp").forward(request, response);
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("errorMessage", "Invalid number format for slots or fee");
                    String id = request.getParameter("courseId");
                    if (id != null) {
                        try {
                            Course course = courseDAO.getCourseById(Integer.parseInt(id));
                            request.setAttribute("course", course);
                        } catch (Exception ex) {
                            request.setAttribute("course", new Course());
                        }
                    }
                    request.getRequestDispatcher("/academic/course_form.jsp").forward(request, response);
                }
                break;
                
            case "delete":
                String deleteCourseIdStr = request.getParameter("courseId");
                if (deleteCourseIdStr == null || deleteCourseIdStr.isEmpty()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                
                try {
                    int deleteCourseId = Integer.parseInt(deleteCourseIdStr);
                    boolean success = courseDAO.deleteCourse(deleteCourseId);
                    HttpSession session = request.getSession();
                    if (success) {
                        session.setAttribute("message", "Inactivate Course Success!");
                        session.setAttribute("messageType", "success");
                    } else {
                        session.setAttribute("message", "Fail To Inactivate Course");
                        session.setAttribute("messageType", "error");
                    }
                    response.sendRedirect(request.getContextPath() + "/course?action=all");
                } catch (NumberFormatException e) {
                    HttpSession session = request.getSession();
                    session.setAttribute("message", "Invalid course ID");
                    session.setAttribute("messageType", "error");
                    response.sendRedirect(request.getContextPath() + "/course?action=all");
                }
                break;

            case "activate":
                String activateCourseIdStr = request.getParameter("courseId");
                if (activateCourseIdStr == null || activateCourseIdStr.isEmpty()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                try {
                    int activateCourseId = Integer.parseInt(activateCourseIdStr);
                    boolean success = courseDAO.activateCourse(activateCourseId);
                    HttpSession session = request.getSession();
                    if (success) {
                        session.setAttribute("message", "Activate Course Success!");
                        session.setAttribute("messageType", "success");
                    } else {
                        session.setAttribute("message", "Fail To Activate Course");
                        session.setAttribute("messageType", "error");
                    }
                    response.sendRedirect(request.getContextPath() + "/course?action=all");
                } catch (NumberFormatException e) {
                    HttpSession session = request.getSession();
                    session.setAttribute("message", "Invalid course ID");
                    session.setAttribute("messageType", "error");
                    response.sendRedirect(request.getContextPath() + "/course?action=all");
                }
                break;
                
            default:
                response.sendRedirect(request.getContextPath() + "/course?action=all");
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
