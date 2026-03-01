/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.TeacherDAO;
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
            out.println("<title>Servlet ScheduleController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ScheduleController at " + request.getContextPath() + "</h1>");
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

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || user.getRole() == null || user.getRole().getRoleId() != 4) {
            response.sendRedirect("login.jsp");
            return;
        }

        TeacherDAO teacherDAO = new TeacherDAO();
        String action = request.getParameter("action");
        if (action == null) {
            action = "view";
        }
        String[] weekdays = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        int[] slots = {1, 2, 3, 4, 5, 6};
        String[] slotTimes = {"", "07:30 - 09:30", "09:45 - 11:45", "12:30 - 14:30", "14:45 - 16:45", "17:00 - 19:00", "19:15 - 21:15"};

        switch (action) {
            case "view":
                String selectedDate = request.getParameter("date");
                if (selectedDate == null || selectedDate.trim().isEmpty()) {
                    selectedDate = java.time.LocalDate.now().toString();
                }

                String classIdParam = request.getParameter("classId");
                List<Schedule> scheduleList;

                if (classIdParam != null && !classIdParam.isEmpty()) {
                    int classId = Integer.parseInt(classIdParam);
                    scheduleList = teacherDAO.getTeachingSchedule(user.getUserId(), selectedDate);
                    List<model.Classes> allClass = teacherDAO.getAllClassOfTeacherID(user.getUserId());
                    String className = "";
                    for (model.Classes c : allClass) {
                        if (c.getClassid() == classId) {
                            className = c.getClassName();
                            break;
                        }
                    }

                    request.setAttribute("classId", classId);
                    request.setAttribute("className", className);
                    request.setAttribute("home_view", "teacher/view_class_schedule.jsp");
                } else {
                    scheduleList = teacherDAO.getTeachingSchedule(user.getUserId(), selectedDate);
                    request.setAttribute("home_view", "teacher/teacher_schedule.jsp");
                }

                request.setAttribute("selectedDate", selectedDate);
                request.setAttribute("weekdays", weekdays);
                request.setAttribute("slots", slots);
                request.setAttribute("slotTimes", slotTimes);
                request.setAttribute("scheduleList", scheduleList);

                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;
            case "viewByClass":
                try {
                    int classId = Integer.parseInt(request.getParameter("classId"));
                    selectedDate = request.getParameter("date");
                    if (selectedDate == null || selectedDate.trim().isEmpty()) {
                        selectedDate = java.time.LocalDate.now().toString();
                    }
            
                    List<Schedule> scheduleListByClass = teacherDAO.getTeachingSchedule(user.getUserId(), selectedDate);

                    List<model.Classes> allClass = teacherDAO.getAllClassOfTeacherID(user.getUserId());
                    String className = "";
                    for (model.Classes c : allClass) {
                        if (c.getClassid() == classId) {
                            className = c.getClassName();
                            break;
                        }
                    }

                    request.setAttribute("selectedDate", selectedDate);
                    request.setAttribute("classId", classId);
                    request.setAttribute("className", className);
                    request.setAttribute("scheduleList", scheduleListByClass);

                    request.setAttribute("weekdays", weekdays);
                    request.setAttribute("slots", slots);
                    request.setAttribute("slotTimes", slotTimes);

                    request.setAttribute("home_view", "teacher/view_class_schedule.jsp");
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
