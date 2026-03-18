/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AttendanceDAO;
import dao.EnrollmentDAO;
import dao.LeadDAO;
import dao.PaymentDAO;
import dao.UserDAO;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import model.User;

/**
 *
 * @author Legion
 */
@WebServlet(name = "DashboardController", urlPatterns = {"/dashboard"})
public class DashboardController extends HttpServlet {

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
        UserDAO userDAO = new UserDAO();
        LeadDAO leadDAO = new LeadDAO();
        PaymentDAO paymentDAO = new PaymentDAO();
        EnrollmentDAO enrollDAO = new EnrollmentDAO();
        if (action == null) {
            action = "all";
        }

        switch (action) {
            case "all":
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;
            case "admin":
                List<User> list = userDAO.getAllUser();
                int totalUsers = list.size();
                double totalRevenue = paymentDAO.getTotalRevenue();
                int totalEnrollments = enrollDAO.getTotalEnrollments();
                double conversionRate = leadDAO.getConversionRate();

                List<Double> monthlyRevenue = paymentDAO.getMonthlyRevenue(2026);
                String revenueDataString = monthlyRevenue.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(","));

                request.setAttribute("conversionRate", conversionRate);
                request.setAttribute("totalEnrollments", totalEnrollments);
                request.setAttribute("totalRevenue", totalRevenue);
                request.setAttribute("revenueData", revenueDataString);
                request.setAttribute("totalUsers", totalUsers);
                request.setAttribute("userList", list);
                request.setAttribute("home_view", "/admin/adminDashboard.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;
            case "profile":
                HttpSession session = request.getSession();
                User loggedInUser = (User) session.getAttribute("user");

                if (loggedInUser == null) {
                    response.sendRedirect("login");
                    return;
                }

                User freshUser = userDAO.getUserById(loggedInUser.getUserId());
                session.setAttribute("user", freshUser);
                request.setAttribute("user", freshUser);

                int roleId = freshUser.getRole().getRoleId();
                if (roleId == 5) {
                    dao.StudentDAO stuDAO = new dao.StudentDAO();
                    request.setAttribute("student", stuDAO.getStudentById(freshUser.getUserId()));
                } else {
                    dao.EmployeeDAO empDAO = new dao.EmployeeDAO();
                    request.setAttribute("employee", empDAO.getEmployeeById(freshUser.getUserId()));
                }

                request.setAttribute("home_view", "profile.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;

            case "teacher":
                User t = (User) request.getSession().getAttribute("user");
                if (t == null) {
                    response.sendRedirect("login");
                    return;
                }

                dao.TeacherDAO tDAO = new dao.TeacherDAO();
                int tId = t.getUserId();
                String today = java.time.LocalDate.now().toString();

                List<model.Schedule> weekly = tDAO.getTeachingSchedule(tId, today);
                List<model.Schedule> todaySlots = new java.util.ArrayList<>();
                for (model.Schedule s : weekly) {
                    if (s.getLearningDate().toString().equals(today)) {
                        todaySlots.add(s);
                    }
                }
                request.setAttribute("todaySlots", todaySlots);

                List<model.Classes> tClasses = tDAO.getAllClassOfTeacherID(tId);
                java.util.Map<Integer, Integer> progressMap = new java.util.HashMap<>();
                for (model.Classes c : tClasses) {
                    progressMap.put(c.getClassid(), tDAO.getClassProgress(c.getClassid()));
                }

                request.setAttribute("totalSlotsTaught", tDAO.getTotalSlotsTaught(tId));
                request.setAttribute("teacherClasses", tClasses);
                request.setAttribute("progressMap", progressMap);
                request.setAttribute("totalStudents", tDAO.getTotalStudentsByTeacher(tId));

                double avgRating = tDAO.getAverageRating(tId);
                java.util.Map<String, Object> fData = tDAO.getTeacherFeedbackData(tId);
                List<model.Feedback> allF = (List<model.Feedback>) fData.get("feedbackList");

                request.setAttribute("avgRating", String.format("%.1f", avgRating));
                if (allF != null) {
                    request.setAttribute("latestFeedbacks", allF.size() > 5 ? allF.subList(0, 5) : allF);
                }
                request.setAttribute("studentNameMap", fData.get("studentNameMap"));

                request.setAttribute("home_view", "teacher/teacherDashboard.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;

            case "student":

                User sUser = (User) request.getSession().getAttribute("user");
                if (sUser == null) {
                    response.sendRedirect("login");
                    return;
                }

                int studentId = sUser.getUserId();

                dao.ClassDAO classDAO = new dao.ClassDAO();
                dao.ScheduleDAO scheduleDAO = new dao.ScheduleDAO();
                AttendanceDAO attendanceDAO = new AttendanceDAO();

                // ===== Classes của student =====
                List<Object[]> studentClasses = classDAO.getStudentClasses(studentId);

                // ===== Schedule tuần này =====
                java.time.LocalDate todayDate = java.time.LocalDate.now();
                java.time.LocalDate startOfWeek = todayDate.with(java.time.DayOfWeek.MONDAY);
                java.time.LocalDate endOfWeek = todayDate.with(java.time.DayOfWeek.SUNDAY);

//                List<model.Schedule> weeklySchedule = scheduleDAO.getScheduleByStudentWeek(
//                        studentId,
//                        startOfWeek.toString(),
//                        endOfWeek.toString()
//                );
                // ===== Schedule hôm nay =====
                List<model.Schedule> todaySchedule = scheduleDAO.getTodayScheduleByStudent(
                        studentId,
                        todayDate.toString()
                );
                Map<String, Integer> summary = attendanceDAO.getAttendanceSummaryByStudent(studentId);
                // ===== Tổng số lớp =====
                int totalClasses = studentClasses.size();

                request.setAttribute("totalClasses", totalClasses);
                request.setAttribute("studentClasses", studentClasses);
//                request.setAttribute("weeklySchedule", weeklySchedule);
                request.setAttribute("todaySchedule", todaySchedule);
                request.setAttribute("summary", summary);

                request.setAttribute("home_view", "student/studentDashboard.jsp");
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
