/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AttendanceDAO;
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
import java.util.Map;
import model.Classes;
import model.Schedule;
import model.User;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "AttendanceController", urlPatterns = {"/attendance"})
public class AttendanceController extends HttpServlet {

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
            out.println("<title>Servlet AttendanceController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AttendanceController at " + request.getContextPath() + "</h1>");
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
            action = "take";
        } else {
            action = action.trim();
        }

        AttendanceDAO dao = new AttendanceDAO();

        switch (action) {

            case "take":
                try {
                    int scheduleId = Integer.parseInt(
                            request.getParameter("scheduleId").trim());

                    int classId = Integer.parseInt(
                            request.getParameter("classId").trim());

                    Map<String, Object> data
                            = dao.getAttendanceData(scheduleId, classId);

                    request.setAttribute("studentList",
                            data.get("studentList"));

                    request.setAttribute("attendanceMap",
                            data.get("attendanceMap"));

                    request.setAttribute("scheduleId", scheduleId);
                    request.setAttribute("classId", classId);
                    request.setAttribute("home_view",
                            "teacher/take_attendance.jsp");

                    request.getRequestDispatcher("dashboard.jsp")
                            .forward(request, response);

                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendRedirect("schedule?action=view");
                }
                break;

            case "report":
                try {
                    int classId = Integer.parseInt(request.getParameter("classId").trim());

                    HttpSession session = request.getSession();
                    User user = (User) session.getAttribute("user");

                    Map<String, Object> data = dao.getAttendanceData(0, classId);
                    List<User> studentList = (List<User>) data.get("studentList");
                    List<Schedule> scheduleList = dao.getSchedulesByClass(classId);
                    Map<String, String> reportMap = dao.getAttendanceReportMap(classId);

                    TeacherDAO teacherDAO = new dao.TeacherDAO();
                    List<Classes> classesOfTeacher = teacherDAO.getAllClassOfTeacherID(user.getUserId());
                    String currentClassName = "";
                    for (Classes c : classesOfTeacher) {

                        if (c.getClassid() == classId) {
                            currentClassName = c.getClassName();
                            break;
                        }
                    }

                    request.setAttribute("classId", classId);
                    request.setAttribute("className", currentClassName);
                    request.setAttribute("studentList", studentList);
                    request.setAttribute("scheduleList", scheduleList);
                    request.setAttribute("reportMap", reportMap);

                    request.setAttribute("home_view", "teacher/attendanceReport.jsp");
                    request.getRequestDispatcher("dashboard.jsp").forward(request, response);

                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendRedirect("dashboard?action=teacher");
                }
                break;

            case "studentReport":
                try {

                    HttpSession session = request.getSession();
                    User user = (User) session.getAttribute("user");

                    if (user == null) {
                        response.sendRedirect("login.jsp");
                        return;
                    }

                    int studentId = user.getUserId();

                    AttendanceDAO daoAttendance = new AttendanceDAO();

                    List<Object[]> reportList = daoAttendance.getAttendanceReportByStudent(studentId);

                    Map<String, Integer> summary = daoAttendance.getAttendanceSummaryByStudent(studentId);

                    request.setAttribute("attendanceReport", reportList);

                    request.setAttribute("summary", summary);

                    request.setAttribute("home_view", "student/attendanceReport.jsp");

                    request.getRequestDispatcher("dashboard.jsp").forward(request, response);

                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendRedirect("dashboard?action=student");
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
        AttendanceDAO dao = new AttendanceDAO();

        HttpSession session = request.getSession();

        switch (action) {
            case "save":
                try {
                    String[] attIds = request.getParameterValues("attId");
                    int scheduleId = Integer.parseInt(request.getParameter("scheduleId"));
                    int classId = Integer.parseInt(request.getParameter("classId"));

                    if (attIds != null) {
                        for (String id : attIds) {
                            int attendanceId = Integer.parseInt(id);
                            String status = request.getParameter("status_" + id);
                            String note = request.getParameter("note_" + id);
                            dao.updateAttendance(attendanceId, status, note);
                        }
                    }

                    dao.updateScheduleStatus(scheduleId);

                    session.setAttribute("message", "Attendance records have been saved successfully!");
                    session.setAttribute("messageType", "success");

                    response.sendRedirect("schedule?action=view&classId=" + classId);
                } catch (Exception e) {
                    e.printStackTrace();
                    session.setAttribute("message", "An error occurred while saving attendance.");
                    session.setAttribute("messageType", "error");
                    response.sendRedirect("schedule?action=view");
                }
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
