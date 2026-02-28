/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

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
import model.Classes;
import model.Grade;
import model.User;

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
        switch (action) {
            case "enter":

                int studentIdEnter
                        = Integer.parseInt(request.getParameter("studentId"));

                int classIdEnter
                        = Integer.parseInt(request.getParameter("classId"));

                User student = studentDAO.getUserById(studentIdEnter);
                String className = classDAO.getClassNameById(classIdEnter);

                request.setAttribute("studentName", student.getFullName());
                request.setAttribute("className", className);
                request.setAttribute("studentId", studentIdEnter);
                request.setAttribute("classId", classIdEnter);
                request.setAttribute("home_view",
                        "teacher/enter_grade.jsp");

                request.getRequestDispatcher("dashboard.jsp")
                        .forward(request, response);
                break;

            // ==========================
            // EDIT - LOAD ĐIỂM CŨ
            // ==========================
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
        HttpSession session = request.getSession();
        switch (action) {

            case "save":

                int studentId
                        = Integer.parseInt(request.getParameter("studentId"));

                int classId
                        = Integer.parseInt(request.getParameter("classId"));

                Integer enrollmentId
                        = dao.getEnrollmentId(studentId, classId);

                if (enrollmentId == null) {
                    session.setAttribute("message",
                            "Enrollment not found!");
                    session.setAttribute("messageType", "error");

                    response.sendRedirect(
                            "student?action=viewByClass&classId=" + classId);
                    return;
                }

                Integer courseId
                        = dao.getCourseIdByClassId(classId);

                Integer readingId
                        = dao.getAssessmentIdByName(courseId, "Reading");
                Integer writingId
                        = dao.getAssessmentIdByName(courseId, "Writing");
                Integer speakingId
                        = dao.getAssessmentIdByName(courseId, "Speaking");
                Integer listeningId
                        = dao.getAssessmentIdByName(courseId, "Listening");

                double reading
                        = Double.parseDouble(request.getParameter("reading"));
                double writing
                        = Double.parseDouble(request.getParameter("writing"));
                double speaking
                        = Double.parseDouble(request.getParameter("speaking"));
                double listening
                        = Double.parseDouble(request.getParameter("listening"));

                dao.saveOrUpdate(enrollmentId, readingId, reading);
                dao.saveOrUpdate(enrollmentId, writingId, writing);
                dao.saveOrUpdate(enrollmentId, speakingId, speaking);
                dao.saveOrUpdate(enrollmentId, listeningId, listening);

                session.setAttribute("message",
                        "Grade saved successfully!");
                session.setAttribute("messageType", "success");

                response.sendRedirect(
                        "student?action=viewByClass&classId=" + classId);
                break;

            // ================= DELETE ALL =================
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
