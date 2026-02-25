/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.ClassDAO;
import dao.EnrollmentDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author Legion
 */
@WebServlet(name = "EnrollmentController", urlPatterns = {"/enrollment"})
public class EnrollmentController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "classes";
        }

        EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
        ClassDAO classDAO = new ClassDAO();

        switch (action) {
            case "classes":
                List<Object[]> classList = classDAO.getClassManagementList();
                request.setAttribute("classList", classList);
                request.setAttribute("home_view", "/academic/class_list.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;
            case "createClassForm":
                request.setAttribute("courseOptions", classDAO.getActiveCoursesForClassForm());
                request.setAttribute("teacherOptions", classDAO.getTeacherOptions());
                request.setAttribute("home_view", "/academic/create_class.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;
            case "addStudentForm":
                String classIdParam = request.getParameter("classId");
                if (classIdParam == null || classIdParam.isEmpty()) {
                    response.sendRedirect("enrollment?action=classes");
                    return;
                }
                try {
                    int classId = Integer.parseInt(classIdParam);
                    Object[] classInfo = classDAO.getClassById(classId);
                    if (classInfo == null) {
                        response.sendRedirect("enrollment?action=classes");
                        return;
                    }
                    List<Object[]> studentsInClass = enrollmentDAO.getStudentsInClass(classId);
                    List<Object[]> availableStudents = enrollmentDAO.getStudentsNotInClass(classId);
                    request.setAttribute("classInfo", classInfo);
                    request.setAttribute("studentsInClass", studentsInClass);
                    request.setAttribute("availableStudents", availableStudents);
                    request.setAttribute("home_view", "/academic/add_student_to_class.jsp");
                    request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                } catch (NumberFormatException e) {
                    response.sendRedirect("enrollment?action=classes");
                }
                break;
            default:
                response.sendRedirect("enrollment?action=classes");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
        ClassDAO classDAO = new ClassDAO();

        if ("createClass".equals(action)) {
            String className = request.getParameter("className");
            String courseIdParam = request.getParameter("courseId");
            String teacherIdParam = request.getParameter("teacherId");
            String startDateParam = request.getParameter("startDate");
            String endDateParam = request.getParameter("endDate");
            String status = request.getParameter("status");

            if (className == null || className.trim().isEmpty()
                    || courseIdParam == null || teacherIdParam == null
                    || startDateParam == null || endDateParam == null
                    || status == null || status.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Please fill all required fields.");
                request.setAttribute("courseOptions", classDAO.getActiveCoursesForClassForm());
                request.setAttribute("teacherOptions", classDAO.getTeacherOptions());
                request.setAttribute("home_view", "/academic/create_class.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                return;
            }

            try {
                int courseId = Integer.parseInt(courseIdParam);
                int teacherId = Integer.parseInt(teacherIdParam);
                Date startDate = Date.valueOf(startDateParam);
                Date endDate = Date.valueOf(endDateParam);

                if (endDate.before(startDate)) {
                    request.setAttribute("errorMessage", "End date must be after or equal to start date.");
                    request.setAttribute("courseOptions", classDAO.getActiveCoursesForClassForm());
                    request.setAttribute("teacherOptions", classDAO.getTeacherOptions());
                    request.setAttribute("home_view", "/academic/create_class.jsp");
                    request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                    return;
                }

                boolean created = classDAO.createClass(className.trim(), courseId, teacherId, startDate, endDate, status.trim());
                if (created) {
                    request.getSession().setAttribute("message", "Class created successfully.");
                    request.getSession().setAttribute("messageType", "success");
                } else {
                    request.getSession().setAttribute("message", "Failed to create class.");
                    request.getSession().setAttribute("messageType", "error");
                }
                response.sendRedirect("enrollment?action=classes");
            } catch (Exception e) {
                request.setAttribute("errorMessage", "Invalid input format.");
                request.setAttribute("courseOptions", classDAO.getActiveCoursesForClassForm());
                request.setAttribute("teacherOptions", classDAO.getTeacherOptions());
                request.setAttribute("home_view", "/academic/create_class.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
            }
        } else if ("addStudents".equals(action)) {
            String classIdParam = request.getParameter("classId");
            if (classIdParam == null || classIdParam.isEmpty()) {
                response.sendRedirect("enrollment?action=classes");
                return;
            }
            try {
                int classId = Integer.parseInt(classIdParam);
                String[] selectedStudentIds = request.getParameterValues("studentIds");
                if (selectedStudentIds == null || selectedStudentIds.length == 0) {
                    request.getSession().setAttribute("message", "Please choose at least one student.");
                    request.getSession().setAttribute("messageType", "error");
                    response.sendRedirect("enrollment?action=addStudentForm&classId=" + classId);
                    return;
                }

                int[] studentIds = new int[selectedStudentIds.length];
                for (int i = 0; i < selectedStudentIds.length; i++) {
                    studentIds[i] = Integer.parseInt(selectedStudentIds[i]);
                }

                int inserted = enrollmentDAO.addStudentsToClass(classId, studentIds);
                if (inserted > 0) {
                    request.getSession().setAttribute("message", "Added " + inserted + " student(s) to class successfully.");
                    request.getSession().setAttribute("messageType", "success");
                } else {
                    request.getSession().setAttribute("message", "Cannot add students to class.");
                    request.getSession().setAttribute("messageType", "error");
                }
                response.sendRedirect("enrollment?action=addStudentForm&classId=" + classId);
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("message", "Invalid request data.");
                request.getSession().setAttribute("messageType", "error");
                response.sendRedirect("enrollment?action=classes");
            }
        } else {
            response.sendRedirect("enrollment?action=classes");
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
