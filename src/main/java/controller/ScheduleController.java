/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.TeacherDAO;
import dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Schedule;
import model.User;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "ScheduleController", urlPatterns = {"/schedule"})
public class ScheduleController extends HttpServlet {
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

        TeacherDAO teacherDAO = new TeacherDAO();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String action = request.getParameter("action");
        if (action == null) {
            action = "view";
        }

        switch (action) {
            case "view":
                String selectedDate = request.getParameter("date");
                if (selectedDate == null) {
                    selectedDate = "2026-02-03";
                }

                List<Schedule> scheduleList = teacherDAO.getTeachingSchedule(user.getUserId());

                String[] weekdays = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
                int[] slots = {1, 2, 3, 4, 5, 6};

                String[] slotTimes = {"", "07:30 - 09:30", "09:45 - 11:45", "12:30 - 14:30", "14:45 - 16:45", "17:00 - 19:00", "19:15 - 21:15"};

                request.setAttribute("selectedDate", selectedDate);
                request.setAttribute("weekdays", weekdays);
                request.setAttribute("slots", slots);
                request.setAttribute("slotTimes", slotTimes);
                request.setAttribute("scheduleList", scheduleList);

                request.setAttribute("home_view", "/teacher/teacher_schedule.jsp");
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

