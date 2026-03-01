/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.SlotDAO;
import dao.TeacherDAO;
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
import model.Classes;
import model.Schedule;
import model.Slot;
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
        SlotDAO slotDAO = new SlotDAO();
        String action = request.getParameter("action");
        if (action == null) {
            action = "view";
        }

    
        List<Slot> allSlots = slotDAO.getAllSlots();
        String[] weekdays = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

        String selectedDate = request.getParameter("date");
        if (selectedDate == null || selectedDate.trim().isEmpty()) {
            selectedDate = LocalDate.now().toString();
        }

        LocalDate current = LocalDate.parse(selectedDate);
        LocalDate mondayDate = current.with(DayOfWeek.MONDAY); 

        String[] dateOfWeek = new String[7];
        for (int i = 0; i < 7; i++) {
            dateOfWeek[i] = mondayDate.plusDays(i).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM"));
        }

        request.setAttribute("selectedDate", selectedDate);
        request.setAttribute("monday", mondayDate.toString());
        request.setAttribute("dateOfWeek", dateOfWeek);
        request.setAttribute("prevWeek", mondayDate.minusWeeks(1).toString());
        request.setAttribute("nextWeek", mondayDate.plusWeeks(1).toString());
        request.setAttribute("weekdays", weekdays);
        request.setAttribute("slots", allSlots);

        switch (action) {
            case "view":
                String classIdParam = request.getParameter("classId");
                List<Schedule> scheduleList;

                if (classIdParam != null && !classIdParam.isEmpty()) {
                    int classId = Integer.parseInt(classIdParam);
                    scheduleList = teacherDAO.getScheduleByClassId(classId, user.getUserId(), selectedDate);

                    List<Classes> allClass = teacherDAO.getAllClassOfTeacherID(user.getUserId());
                    for (Classes c : allClass) {
                        if (c.getClassid() == classId) {
                            request.setAttribute("className", c.getClassName());
                            break;
                        }
                    }
                    request.setAttribute("classId", classId);
                    request.setAttribute("home_view", "teacher/view_class_schedule.jsp");
                } else {
                    scheduleList = teacherDAO.getTeachingSchedule(user.getUserId(), selectedDate);
                    request.setAttribute("home_view", "teacher/teacher_schedule.jsp");
                }

                request.setAttribute("scheduleList", scheduleList);
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;

            case "viewByClass":
                try {
                    int classId = Integer.parseInt(request.getParameter("classId"));
                    List<Schedule> scheduleByClass = teacherDAO.getScheduleByClassId(classId, user.getUserId(), selectedDate);

                    List<Classes> classesOfTeacher = teacherDAO.getAllClassOfTeacherID(user.getUserId());
                    String currentClassName = "";
                    for (Classes c : classesOfTeacher) {
                        if (c.getClassid() == classId) {
                            currentClassName = c.getClassName();
                            break;
                        }
                    }

                    request.setAttribute("selectedDate", selectedDate);
                    request.setAttribute("classId", classId);
                    request.setAttribute("className", currentClassName);
                    request.setAttribute("scheduleList", scheduleByClass);
                    request.setAttribute("weekdays", weekdays);
                    request.setAttribute("slots", allSlots);

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
