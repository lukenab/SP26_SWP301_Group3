/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.StudentDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import model.Schedule;
import model.User;

/**
 *
 * @author WIN11
 */

@WebServlet(name = "StudentScheduleController", urlPatterns = {"/student-schedule"})
public class StudentScheduleController extends HttpServlet {

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
            out.println("<title>Servlet StudentScheduleController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet StudentScheduleController at " + request.getContextPath() + "</h1>");
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
        HttpSession session = request.getSession(false);

        // ===== CHECK LOGIN =====
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // ===== DOUBLE CHECK ROLE =====
        if (user.getRole().getRoleId() != 5) {
            response.sendRedirect(request.getContextPath() + "/access-denied.jsp");
            return;
        }

        StudentDAO studentDAO = new StudentDAO();

        String selectedDate = request.getParameter("date");

        if (selectedDate == null || selectedDate.trim().isEmpty()) {
            selectedDate = LocalDate.now().toString();
        }

        LocalDate date = LocalDate.parse(selectedDate);

        // Tìm Monday & Sunday
        LocalDate monday = date.with(DayOfWeek.MONDAY);
        LocalDate sunday = monday.plusDays(6);

        List<Schedule> scheduleList =
                studentDAO.getScheduleByStudentWeek(
                        user.getUserId(),
                        monday.toString(),
                        sunday.toString());

        String[] weekdays = {
            "Monday", "Tuesday", "Wednesday",
            "Thursday", "Friday", "Saturday", "Sunday"
        };

        int[] slots = {1, 2, 3, 4, 5, 6};

        String[] slotTimes = {
            "",
            "07:30 - 09:30",
            "09:45 - 11:45",
            "12:30 - 14:30",
            "14:45 - 16:45",
            "17:00 - 19:00",
            "19:15 - 21:15"
        };

        request.setAttribute("selectedDate", selectedDate);
        request.setAttribute("weekdays", weekdays);
        request.setAttribute("slots", slots);
        request.setAttribute("slotTimes", slotTimes);
        request.setAttribute("scheduleList", scheduleList);

        request.setAttribute("home_view", "student/studentSchedule.jsp");

        request.getRequestDispatcher("dashboard.jsp")
                .forward(request, response);
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
        processRequest(request, response);
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
