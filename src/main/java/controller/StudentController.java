/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.GradeDAO;
import dao.StudentDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import model.User;

/**
 *
 * @author Legion
 */
@WebServlet(name = "StudentController", urlPatterns = {"/student"})
public class StudentController extends HttpServlet {

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

        jakarta.servlet.http.HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            response.sendRedirect("login");
            return;
        }

        String roleName = (currentUser.getRole() != null) ? currentUser.getRole().getRoleName() : "";
        boolean isTeacher = roleName.equalsIgnoreCase("Teacher");

        if (!isTeacher) {
            session.setAttribute("message", "Access Denied: Only teachers can view the class list!");
            session.setAttribute("messageType", "error");
            response.sendRedirect("dashboard");
            return;
        }

        if (action == null) {
            action = "viewByClass";
        } else {
            action = action.trim();
        }

        StudentDAO dao = new StudentDAO();

        switch (action) {

            case "viewByClass":
                try {

                    int classId = Integer.parseInt(
                            request.getParameter("classId"));

                    List<User> studentList
                            = dao.getStudentListByClassId(classId);
                    GradeDAO gradeDAO = new GradeDAO();
                    Map<Integer, Double> averageMap
                            = gradeDAO.getAverageByClassId(classId);

                    request.setAttribute("studentList", studentList);
                    request.setAttribute("averageMap", averageMap);
                    request.setAttribute("classId", classId);
                    request.setAttribute("home_view",
                            "teacher/student_list_of_class.jsp");

                    request.getRequestDispatcher("dashboard.jsp")
                            .forward(request, response);

                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendRedirect("class");
                }
                break;

            default:
                response.sendRedirect("dashboard");
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
