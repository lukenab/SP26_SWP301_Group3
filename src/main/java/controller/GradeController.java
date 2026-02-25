/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.GradeDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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

        switch (action) {

            // ==========================
            // MỞ FORM NHẬP ĐIỂM
            // ==========================
            case "enter":

                int studentId
                        = Integer.parseInt(request.getParameter("studentId"));

                int classId
                        = Integer.parseInt(request.getParameter("classId"));

                request.setAttribute("studentId", studentId);
                request.setAttribute("classId", classId);
                request.setAttribute("home_view",
                        "teacher/enter_grade.jsp");

                request.getRequestDispatcher("dashboard.jsp")
                        .forward(request, response);
                break;

            // ==========================
            // EDIT ĐIỂM
            // ==========================
            case "edit":

                int studentIdEdit
                        = Integer.parseInt(request.getParameter("studentId"));

                int classIdEdit
                        = Integer.parseInt(request.getParameter("classId"));

                Float score
                        = dao.getScore(studentIdEdit, classIdEdit);

                request.setAttribute("studentId", studentIdEdit);
                request.setAttribute("classId", classIdEdit);
                request.setAttribute("score", score);
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

        switch (action) {

            // ==========================
            // SAVE / UPDATE
            // ==========================
            case "save":

                int studentId
                        = Integer.parseInt(request.getParameter("studentId"));

                int classId
                        = Integer.parseInt(request.getParameter("classId"));

                float score
                        = Float.parseFloat(request.getParameter("score"));

                dao.saveOrUpdateScore(studentId, classId, score);

                response.sendRedirect(
                        "student?action=viewByClass&classId=" + classId);
                break;

            // ==========================
            // DELETE ĐIỂM
            // ==========================
            case "delete":

                int studentIdDel
                        = Integer.parseInt(request.getParameter("studentId"));

                int classIdDel
                        = Integer.parseInt(request.getParameter("classId"));

                dao.deleteScore(studentIdDel, classIdDel);

                response.sendRedirect(
                        "student?action=viewByClass&classId=" + classIdDel);
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
