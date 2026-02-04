/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.CourseDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
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
                request.setAttribute("courseList", list);
                request.setAttribute("home_view", "course_list.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;
            case "active":
                List<Course> activeList = courseDAO.getActiveCourses();
                request.setAttribute("courseList", activeList);
                request.setAttribute("home_view", "course_list.jsp");
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
                            request.getRequestDispatcher("course_details.jsp").forward(request, response);
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
            default:
                List<Course> defaultList = courseDAO.getAllCourse();
                request.setAttribute("courseList", defaultList);
                request.getRequestDispatcher("course_list.jsp").forward(request, response);
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
