/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AssessmentDAO;
import dao.ClassDAO;
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

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet GradeController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet GradeController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

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
        if (action == null) {
            action = "enter";
        }

        GradeDAO dao = new GradeDAO();
        StudentDAO studentDAO = new StudentDAO();
        ClassDAO classDAO = new ClassDAO();
        AssessmentDAO assessmentDAO = new AssessmentDAO();
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        switch (action) {
            case "enter":
                try {

                    int studentIdEnter = Integer.parseInt(request.getParameter("studentId"));
                    int classIdEnter = Integer.parseInt(request.getParameter("classId"));

                    User student = studentDAO.getUserById(studentIdEnter);
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
                    response.sendRedirect("class");
                }
                break;

            case "edit":

                int studentIdEdit
                        = Integer.parseInt(request.getParameter("studentId"));

                int classIdEdit
                        = Integer.parseInt(request.getParameter("classId"));

                Integer enrollmentId
                        = dao.getEnrollmentId(studentIdEdit, classIdEdit);

                if (enrollmentId == null) {
                    response.sendRedirect(
                            "student?action=viewByClass&classId=" + classIdEdit);
                    return;
                }

                List<Assessment> assessmentListEdit = assessmentDAO.getAssessmentsByClass(classIdEdit);
                request.setAttribute("assessmentList", assessmentListEdit);

                User estudent = studentDAO.getUserById(studentIdEdit);
                String eclassName = classDAO.getClassNameById(classIdEdit);

                Map<String, Double> scoreMap
                        = dao.getAllScores(enrollmentId);

                Double average
                        = dao.calculateAverage(enrollmentId);

                request.setAttribute("studentName", estudent.getFullName());
                request.setAttribute("className", eclassName);
                request.setAttribute("studentId", studentIdEdit);
                request.setAttribute("classId", classIdEdit);
                request.setAttribute("scoreMap", scoreMap);
                request.setAttribute("average", average);
                request.setAttribute("home_view",
                        "teacher/enter_grade.jsp");

                request.getRequestDispatcher("dashboard.jsp")
                        .forward(request, response);
                break;

            case "student-courses":

                // ===== 1. AUTH CHECK =====
                if (currentUser == null) {
                    response.sendRedirect("login.jsp");
                    return;
                }

                if (currentUser.getRole() == null
                        || currentUser.getRole().getRoleId() != 5) {

                    session.setAttribute("message", "Bạn không có quyền truy cập!");
                    session.setAttribute("messageType", "error");
                    response.sendRedirect("dashboard.jsp");
                    return;
                }

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

                if (currentUser == null) {
                    response.sendRedirect("login.jsp");
                    return;
                }

                int courseId = Integer.parseInt(request.getParameter("courseId"));
                int studentIdDetail = currentUser.getUserId();

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
                    int classId = Integer.parseInt(request.getParameter("classId"));

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
