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
                request.setAttribute("home_view", "/academic/classes/class_list.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;
            case "deleteClass":
                String deleteClassIdParam = request.getParameter("classId");
                if (deleteClassIdParam == null || deleteClassIdParam.isEmpty()) {
                    response.sendRedirect("enrollment?action=classes");
                    return;
                }
                try {
                    int classId = Integer.parseInt(deleteClassIdParam);
                    Object[] classInfo = classDAO.getClassById(classId);
                    if (classInfo == null) {
                        response.sendRedirect("enrollment?action=classes");
                        return;
                    }
                    request.setAttribute("classInfo", classInfo);
                    request.setAttribute("home_view", "/academic/classes/class_delete_confirm.jsp");
                    request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                } catch (NumberFormatException e) {
                    response.sendRedirect("enrollment?action=classes");
                }
                break;
            case "createClassForm":
                request.setAttribute("courseOptions", classDAO.getActiveCoursesForClassForm());
                request.setAttribute("teacherOptions", classDAO.getTeacherOptions());
                request.setAttribute("home_view", "/academic/classes/create_class.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;
            case "editClassForm":
                String editClassIdParam = request.getParameter("classId");
                if (editClassIdParam == null || editClassIdParam.isEmpty()) {
                    response.sendRedirect("enrollment?action=classes");
                    return;
                }
                try {
                    int classId = Integer.parseInt(editClassIdParam);
                    Object[] classEditInfo = classDAO.getClassForEdit(classId);
                    if (classEditInfo == null) {
                        response.sendRedirect("enrollment?action=classes");
                        return;
                    }
                    request.setAttribute("classEditInfo", classEditInfo);
                    request.setAttribute("courseOptions", classDAO.getActiveCoursesForClassForm());
                    request.setAttribute("teacherOptions", classDAO.getTeacherOptions());
                    request.setAttribute("home_view", "/academic/classes/edit_class.jsp");
                    request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                } catch (NumberFormatException e) {
                    response.sendRedirect("enrollment?action=classes");
                }
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
                    request.setAttribute("home_view", "/academic/classes/add_student_to_class.jsp");
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
                request.setAttribute("home_view", "/academic/classes/create_class.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                return;
            }

            String normalizedStatus = status.trim();
            if (!"Active".equalsIgnoreCase(normalizedStatus) && !"Inactive".equalsIgnoreCase(normalizedStatus)) {
                request.setAttribute("errorMessage", "Status must be Active or Inactive.");
                request.setAttribute("courseOptions", classDAO.getActiveCoursesForClassForm());
                request.setAttribute("teacherOptions", classDAO.getTeacherOptions());
                request.setAttribute("home_view", "/academic/classes/create_class.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                return;
            }
            normalizedStatus = Character.toUpperCase(normalizedStatus.charAt(0)) + normalizedStatus.substring(1).toLowerCase();

            try {
                int courseId = Integer.parseInt(courseIdParam);
                int teacherId = Integer.parseInt(teacherIdParam);
                Date startDate = Date.valueOf(startDateParam);
                Date endDate = Date.valueOf(endDateParam);

                if (endDate.before(startDate)) {
                    request.setAttribute("errorMessage", "End date must be after or equal to start date.");
                    request.setAttribute("courseOptions", classDAO.getActiveCoursesForClassForm());
                    request.setAttribute("teacherOptions", classDAO.getTeacherOptions());
                    request.setAttribute("home_view", "/academic/classes/create_class.jsp");
                    request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                    return;
                }

                boolean created = classDAO.createClass(className.trim(), courseId, teacherId, startDate, endDate, normalizedStatus);
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
                request.setAttribute("home_view", "/academic/classes/create_class.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
            }
        } else if ("updateClass".equals(action)) {
            String classIdParam = request.getParameter("classId");
            String className = request.getParameter("className");
            String courseIdParam = request.getParameter("courseId");
            String teacherIdParam = request.getParameter("teacherId");
            String startDateParam = request.getParameter("startDate");
            String endDateParam = request.getParameter("endDate");

            if (classIdParam == null || classIdParam.isEmpty()
                    || className == null || className.trim().isEmpty()
                    || courseIdParam == null || teacherIdParam == null
                    || startDateParam == null || endDateParam == null) {
                request.getSession().setAttribute("message", "Please fill all required fields.");
                request.getSession().setAttribute("messageType", "error");
                response.sendRedirect("enrollment?action=classes");
                return;
            }

            try {
                int classId = Integer.parseInt(classIdParam);
                int courseId = Integer.parseInt(courseIdParam);
                int teacherId = Integer.parseInt(teacherIdParam);
                Date startDate = Date.valueOf(startDateParam);
                Date endDate = Date.valueOf(endDateParam);

                if (endDate.before(startDate)) {
                    request.getSession().setAttribute("message", "End date must be after or equal to start date.");
                    request.getSession().setAttribute("messageType", "error");
                    response.sendRedirect("enrollment?action=editClassForm&classId=" + classId);
                    return;
                }

                boolean updated = classDAO.updateClass(classId, className.trim(), courseId, teacherId, startDate, endDate);
                if (updated) {
                    request.getSession().setAttribute("message", "Class updated successfully.");
                    request.getSession().setAttribute("messageType", "success");
                } else {
                    request.getSession().setAttribute("message", "Failed to update class.");
                    request.getSession().setAttribute("messageType", "error");
                }
                response.sendRedirect("enrollment?action=classes");
            } catch (Exception e) {
                request.getSession().setAttribute("message", "Invalid input format.");
                request.getSession().setAttribute("messageType", "error");
                response.sendRedirect("enrollment?action=classes");
            }
        } else if ("updateClassStatus".equals(action)) {
            String classIdParam = request.getParameter("classId");
            String status = request.getParameter("status");

            if (classIdParam == null || classIdParam.isEmpty()
                    || status == null || status.trim().isEmpty()) {
                request.getSession().setAttribute("message", "Invalid status update request.");
                request.getSession().setAttribute("messageType", "error");
                response.sendRedirect("enrollment?action=classes");
                return;
            }

            String normalizedStatus = status.trim();
            if (!"Active".equalsIgnoreCase(normalizedStatus) && !"Inactive".equalsIgnoreCase(normalizedStatus)) {
                request.getSession().setAttribute("message", "Status must be Active or Inactive.");
                request.getSession().setAttribute("messageType", "error");
                response.sendRedirect("enrollment?action=classes");
                return;
            }
            normalizedStatus = Character.toUpperCase(normalizedStatus.charAt(0)) + normalizedStatus.substring(1).toLowerCase();

            try {
                int classId = Integer.parseInt(classIdParam);
                boolean updated = classDAO.updateClassStatus(classId, normalizedStatus);
                if (updated) {
                    request.getSession().setAttribute("message", "Class status updated successfully.");
                    request.getSession().setAttribute("messageType", "success");
                } else {
                    request.getSession().setAttribute("message", "Failed to update class status.");
                    request.getSession().setAttribute("messageType", "error");
                }
                response.sendRedirect("enrollment?action=classes");
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("message", "Invalid class ID.");
                request.getSession().setAttribute("messageType", "error");
                response.sendRedirect("enrollment?action=classes");
            }
        } else if ("deactivateClass".equals(action) || "activateClass".equals(action)) {
            String classIdParam = request.getParameter("classId");
            if (classIdParam == null || classIdParam.isEmpty()) {
                request.getSession().setAttribute("message", "Invalid class status request.");
                request.getSession().setAttribute("messageType", "error");
                response.sendRedirect("enrollment?action=classes");
                return;
            }

            String targetStatus = "deactivateClass".equals(action) ? "Inactive" : "Active";
            try {
                int classId = Integer.parseInt(classIdParam);
                boolean updated = classDAO.updateClassStatus(classId, targetStatus);
                if (updated) {
                    request.getSession().setAttribute("message",
                            "deactivateClass".equals(action) ? "Inactivate Class Success!" : "Activate Class Success!");
                    request.getSession().setAttribute("messageType", "success");
                } else {
                    request.getSession().setAttribute("message",
                            "deactivateClass".equals(action) ? "Fail To Inactivate Class" : "Fail To Activate Class");
                    request.getSession().setAttribute("messageType", "error");
                }
                response.sendRedirect("enrollment?action=classes");
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("message", "Invalid class ID.");
                request.getSession().setAttribute("messageType", "error");
                response.sendRedirect("enrollment?action=classes");
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
                String enrollmentStatus = request.getParameter("enrollmentStatus");
                if (selectedStudentIds == null || selectedStudentIds.length == 0) {
                    request.getSession().setAttribute("message", "Please choose at least one student.");
                    request.getSession().setAttribute("messageType", "error");
                    response.sendRedirect("enrollment?action=addStudentForm&classId=" + classId);
                    return;
                }
                if (enrollmentStatus == null || enrollmentStatus.trim().isEmpty()) {
                    request.getSession().setAttribute("message", "Please choose status Paid or UnPaid.");
                    request.getSession().setAttribute("messageType", "error");
                    response.sendRedirect("enrollment?action=addStudentForm&classId=" + classId);
                    return;
                }
                String normalizedStatus = enrollmentStatus.trim();
                if (!"Paid".equalsIgnoreCase(normalizedStatus) && !"UnPaid".equalsIgnoreCase(normalizedStatus)) {
                    request.getSession().setAttribute("message", "Status must be Paid or UnPaid.");
                    request.getSession().setAttribute("messageType", "error");
                    response.sendRedirect("enrollment?action=addStudentForm&classId=" + classId);
                    return;
                }
                normalizedStatus = "Paid".equalsIgnoreCase(normalizedStatus) ? "Paid" : "UnPaid";

                int[] studentIds = new int[selectedStudentIds.length];
                for (int i = 0; i < selectedStudentIds.length; i++) {
                    studentIds[i] = Integer.parseInt(selectedStudentIds[i]);
                }

                int inserted = enrollmentDAO.addStudentsToClass(classId, studentIds, normalizedStatus);
                if (inserted > 0) {
                    request.getSession().setAttribute("message", "Added " + inserted + " student(s) to class with status " + normalizedStatus + ".");
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
        } else if ("removeStudents".equals(action)) {
            String classIdParam = request.getParameter("classId");
            if (classIdParam == null || classIdParam.isEmpty()) {
                response.sendRedirect("enrollment?action=classes");
                return;
            }
            try {
                int classId = Integer.parseInt(classIdParam);
                String[] selectedStudentIds = request.getParameterValues("studentIds");
                if (selectedStudentIds == null || selectedStudentIds.length == 0) {
                    request.getSession().setAttribute("message", "Please choose at least one student to remove.");
                    request.getSession().setAttribute("messageType", "error");
                    response.sendRedirect("enrollment?action=addStudentForm&classId=" + classId);
                    return;
                }

                int[] studentIds = new int[selectedStudentIds.length];
                for (int i = 0; i < selectedStudentIds.length; i++) {
                    studentIds[i] = Integer.parseInt(selectedStudentIds[i]);
                }

                int removed = enrollmentDAO.removeStudentsFromClass(classId, studentIds);
                if (removed > 0) {
                    request.getSession().setAttribute("message", "Removed " + removed + " student(s) from class successfully.");
                    request.getSession().setAttribute("messageType", "success");
                } else {
                    request.getSession().setAttribute("message", "Cannot remove students from class.");
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
