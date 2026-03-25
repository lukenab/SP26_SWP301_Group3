/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AssessmentDAO;
import dao.ClassDAO;
import dao.EnrollmentDAO;
import dao.GradeDAO;
import dao.StudentDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import model.Grade;
import model.Student;
import model.User;
import dao.CourseDAO;
import java.util.HashMap;
import model.Assessment;
import model.Course;

/**
 *
 * @author Legion
 */
@WebServlet(name = "GradeController", urlPatterns = {"/grade"})
public class GradeController extends HttpServlet {
    
     private static final double MIN_SCORE = 0.0;
    private static final double MAX_SCORE = 10.0;

    private Integer parseIntParam(HttpServletRequest request, String paramName) {
        try {
            String raw = request.getParameter(paramName);
            if (raw == null || raw.trim().isEmpty()) {
                return null;
            }
            return Integer.parseInt(raw.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private boolean canManageClassGrades(User currentUser, int classId, ClassDAO classDAO) {
        if (currentUser == null || currentUser.getRole() == null) {
            return false;
        }
        if (currentUser.getRole().getManageCourse()) {
            return true;
        }
        String roleName = currentUser.getRole().getRoleName();
        if (!"Teacher".equalsIgnoreCase(roleName)) {
            return false;
        }
        int teacherId = classDAO.getTeacherIdByClassId(classId);
        return teacherId > 0 && teacherId == currentUser.getUserId();
    }

    private void denyGradeAccess(HttpSession session, HttpServletResponse response, String message) throws IOException {
        session.setAttribute("message", message);
        session.setAttribute("messageType", "error");
        response.sendRedirect("dashboard");
    }

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
        if (action == null) {
            action = "enter";
        }

        GradeDAO dao = new GradeDAO();
        StudentDAO studentDAO = new StudentDAO();
        ClassDAO classDAO = new ClassDAO();
        AssessmentDAO assessmentDAO = new AssessmentDAO();
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String roleName = (currentUser.getRole() != null) ? currentUser.getRole().getRoleName() : "";

        if (action.equals("enter") || action.equals("edit") || action.equals("report")) {
            boolean canAccess = roleName.equalsIgnoreCase("Teacher") || currentUser.getRole().getManageCourse();
            if (!canAccess) {
                session.setAttribute("message", "Access Denied: You don't have permission to manage grades!");
                session.setAttribute("messageType", "error");
                response.sendRedirect("dashboard");
                return;
            }
        } else if (action.equals("student-courses") || action.equals("student-course-grades")) {
            if (!roleName.equalsIgnoreCase("Student")) {
                response.sendRedirect("dashboard.jsp");
                return;
            }
        }

        switch (action) {
            case "enter":
                try {
                    Integer studentIdEnter = parseIntParam(request, "studentId");
                    Integer classIdEnter = parseIntParam(request, "classId");

                    if (studentIdEnter == null || classIdEnter == null) {
                        session.setAttribute("message", "Invalid student or class identifier.");
                        session.setAttribute("messageType", "error");
                        response.sendRedirect("class");
                        return;
                    }

                    if (!canManageClassGrades(currentUser, classIdEnter, classDAO)) {
                        denyGradeAccess(session, response, "Access Denied: You don't have permission to manage this class grade!");
                        return;
                    }

                    User student = studentDAO.getUserById(studentIdEnter);
                    if (student == null) {
                        session.setAttribute("message", "Student not found.");
                        session.setAttribute("messageType", "error");
                        response.sendRedirect("student?action=viewByClass&classId=" + classIdEnter);
                        return;
                    }

                    String className = classDAO.getClassNameById(classIdEnter);
                    List<Assessment> assessmentList = assessmentDAO.getAssessmentsByClass(classIdEnter);

                    Integer enrollmentId = dao.getEnrollmentId(studentIdEnter, classIdEnter);
                    if (enrollmentId != null) {
                        Map<String, Double> scoreMap = dao.getAllScores(enrollmentId);
                        Double average = dao.calculateAverage(enrollmentId);
                        request.setAttribute("scoreMap", scoreMap);
                        request.setAttribute("average", average);
                    }

                    request.setAttribute("studentName", student.getFullName());
                    request.setAttribute("className", className);
                    request.setAttribute("studentId", studentIdEnter);
                    request.setAttribute("classId", classIdEnter);
                    request.setAttribute("assessmentList", assessmentList);

                    request.setAttribute("home_view", "teacher/enter_grade.jsp");
                    request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                } catch (Exception e) {
                    e.printStackTrace();
                    session.setAttribute("message", "Unable to load grade form.");
                    session.setAttribute("messageType", "error");
                    response.sendRedirect("class");
                }
                break;

             case "edit":
                try {
                    Integer studentIdEdit = parseIntParam(request, "studentId");
                    Integer classIdEdit = parseIntParam(request, "classId");

                    if (studentIdEdit == null || classIdEdit == null) {
                        session.setAttribute("message", "Invalid student or class identifier.");
                        session.setAttribute("messageType", "error");
                        response.sendRedirect("class");
                        return;
                    }

                    if (!canManageClassGrades(currentUser, classIdEdit, classDAO)) {
                        denyGradeAccess(session, response, "Access Denied: You don't have permission to manage this class grade!");
                        return;
                    }

                    Integer enrollmentId = dao.getEnrollmentId(studentIdEdit, classIdEdit);
                    if (enrollmentId == null) {
                        session.setAttribute("message", "Student is not enrolled in this class.");
                        session.setAttribute("messageType", "error");
                        response.sendRedirect("student?action=viewByClass&classId=" + classIdEdit);
                        return;
                    }

                    List<Assessment> assessmentListEdit = assessmentDAO.getAssessmentsByClass(classIdEdit);
                    request.setAttribute("assessmentList", assessmentListEdit);

                    User estudent = studentDAO.getUserById(studentIdEdit);
                    String eclassName = classDAO.getClassNameById(classIdEdit);

                    Map<String, Double> scoreMap = dao.getAllScores(enrollmentId);
                    Double average = dao.calculateAverage(enrollmentId);

                    request.setAttribute("studentName", estudent != null ? estudent.getFullName() : "");
                    request.setAttribute("className", eclassName);
                    request.setAttribute("studentId", studentIdEdit);
                    request.setAttribute("classId", classIdEdit);
                    request.setAttribute("scoreMap", scoreMap);
                    request.setAttribute("average", average);
                    request.setAttribute("home_view", "teacher/enter_grade.jsp");

                    request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                } catch (Exception e) {
                    e.printStackTrace();
                    session.setAttribute("message", "Unable to load grade for editing.");
                    session.setAttribute("messageType", "error");
                    response.sendRedirect("class");
                }
                break;

            case "student-courses":

                int studentId = currentUser.getUserId();

                // ===== 2. GET FILTER PARAM =====
                String keyword = request.getParameter("keyword");
                String statusParam = request.getParameter("status");

                Boolean status = null;

                if (statusParam != null && !statusParam.isEmpty()) {
                    status = Boolean.parseBoolean(statusParam);
                }

                // ===== 3. PAGINATION =====
                int page = 1;
                int pageSize = 6;

                if (request.getParameter("page") != null) {
                    page = Integer.parseInt(request.getParameter("page"));
                }

                // ===== 4. STUDENT INFO =====
                Student studentInfo = studentDAO.getStudentById(studentId);

                // ===== 5. GET COURSE LIST (ADVANCED SEARCH) =====
                CourseDAO courseDAO = new CourseDAO();

                List<Course> courseList = courseDAO.getCoursesByStudentAdvanced(
                        studentId,
                        keyword,
                        status,
                        page,
                        pageSize
                );

                // ===== 6. CALCULATE AVERAGE SCORE =====
                Map<Integer, Double> averageMap = new HashMap<>();

                for (Course c : courseList) {

                    Double avg = courseDAO.getAverageByStudentAndCourse(
                            c.getCourseId(), studentId);

                    averageMap.put(c.getCourseId(), avg);
                }

                // ===== 7. SET ATTRIBUTE =====
                request.setAttribute("courseList", courseList);
                request.setAttribute("averageMap", averageMap);

                request.setAttribute("studentName", currentUser.getFullName());
                request.setAttribute("studentInfo", studentInfo);

                // giữ filter khi reload page
                request.setAttribute("keyword", keyword);
                request.setAttribute("status", status);
                request.setAttribute("currentPage", page);

                request.setAttribute("home_view", "student/studentCourseCard.jsp");

                request.getRequestDispatcher("dashboard.jsp")
                        .forward(request, response);

                break;

            case "student-course-grades":
                int courseId = Integer.parseInt(request.getParameter("courseId"));
                int studentIdDetail = currentUser.getUserId();
                EnrollmentDAO enrollmentDAO = new EnrollmentDAO();

                if (!enrollmentDAO.hasAcademicAccessForCourse(studentIdDetail, courseId)) {
                    session.setAttribute("message", "Grades are locked until your enrollment becomes Active.");
                    session.setAttribute("messageType", "error");
                    response.sendRedirect("class?action=myClasses");
                    return;
                }

                List<Grade> gradeList = dao.getGradesByStudentId(studentIdDetail);

                List<Grade> filteredList = new java.util.ArrayList<>();

                for (Grade g : gradeList) {
                    if (g.getEnrollment()
                            .getClasses()
                            .getCourse()
                            .getCourseId() == courseId) {

                        filteredList.add(g);
                    }
                }

                request.setAttribute("gradeList", filteredList);
                request.setAttribute("studentName", currentUser.getFullName());
                request.setAttribute("home_view", "student/studentGrade.jsp");

                request.getRequestDispatcher("dashboard.jsp")
                        .forward(request, response);
                break;

             case "report":
                try {
                    Integer classId = parseIntParam(request, "classId");
                    if (classId == null) {
                        session.setAttribute("message", "Invalid class identifier.");
                        session.setAttribute("messageType", "error");
                        response.sendRedirect("class");
                        return;
                    }

                    if (!canManageClassGrades(currentUser, classId, classDAO)) {
                        denyGradeAccess(session, response, "Access Denied: You don't have permission to view this class grade report!");
                        return;
                    }

                    List<Assessment> assessmentList = assessmentDAO.getAssessmentsByClass(classId);
                    List<Map<String, Object>> gradeAllList = dao.getFullGradeReport(classId);
                    Map<Integer, Double> avgMap = dao.getAverageByClassId(classId);
                    String currentClassName = classDAO.getClassNameById(classId);

                    request.setAttribute("className", currentClassName);
                    request.setAttribute("classId", classId);
                    request.setAttribute("assessmentList", assessmentList);
                    request.setAttribute("gradeList", gradeAllList);
                    request.setAttribute("avgMap", avgMap);

                    request.setAttribute("home_view", "teacher/gradeReport.jsp");
                    request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                } catch (Exception e) {
                    e.printStackTrace();
                    session.setAttribute("message", "Unable to load grade report.");
                    session.setAttribute("messageType", "error");
                    response.sendRedirect("class");
                }
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
        GradeDAO dao = new GradeDAO();
        AssessmentDAO assessmentDAO = new AssessmentDAO();
        HttpSession session = request.getSession();
        
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String roleName = (currentUser.getRole() != null) ? currentUser.getRole().getRoleName() : "";
        boolean canModify = roleName.equalsIgnoreCase("Teacher") || currentUser.getRole().getManageCourse();

        if (!canModify) {
            session.setAttribute("message", "Security Alert: Unauthorized attempts to change grades!");
            session.setAttribute("messageType", "error");
            response.sendRedirect("dashboard.jsp");
            return;
        }
        
        switch (action) {

            case "save":
                try {
                    int studentId = Integer.parseInt(request.getParameter("studentId"));
                    int classId = Integer.parseInt(request.getParameter("classId"));

                    Integer enrollmentId = dao.getEnrollmentId(studentId, classId);

                    if (enrollmentId != null) {

                        List<Assessment> assessmentList = assessmentDAO.getAssessmentsByClass(classId);

                        for (Assessment a : assessmentList) {

                            String scoreVal = request.getParameter("score_" + a.getAssessmentId());

                            if (scoreVal != null && !scoreVal.isEmpty()) {

                                double score = Double.parseDouble(scoreVal);

                                dao.saveOrUpdate(enrollmentId, a.getAssessmentId(), score);
                            }
                        }
                        dao.recalculateAndPersistFinalGrade(enrollmentId);
                        session.setAttribute("message", "Grades updated successfully!");
                        session.setAttribute("messageType", "success");
                    }

                } catch (Exception e) {
                    session.setAttribute("message", "Error saving grades!");
                    session.setAttribute("messageType", "error");
                }

                String cid = request.getParameter("classId");
                response.sendRedirect("student?action=viewByClass&classId=" + cid);
                break;

            case "delete":

                int studentIdDel
                        = Integer.parseInt(request.getParameter("studentId"));

                int classIdDel
                        = Integer.parseInt(request.getParameter("classId"));

                Integer enrollmentIdDel
                        = dao.getEnrollmentId(studentIdDel, classIdDel);

                if (enrollmentIdDel != null) {
                    dao.deleteAllByEnrollment(enrollmentIdDel);
                }
                dao.updateFinalGradeByEnrollmentId(enrollmentIdDel, null);
                session.setAttribute("message", "All grades deleted successfully!");
                session.setAttribute("messageType", "success");

                response.sendRedirect(
                        "student?action=viewByClass&classId=" + classIdDel);
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
