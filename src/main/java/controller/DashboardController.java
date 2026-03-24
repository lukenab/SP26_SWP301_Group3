/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AttendanceDAO;
import dao.ClassDAO;
import dao.EnrollmentDAO;
import dao.LeadDAO;
import dao.PaymentDAO;
import dao.SystemLogDAO;
import dao.UserDAO;
import dao.VoucherDAO;
import java.io.IOException;
import java.time.Year;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import model.SystemLog;
import model.User;
import model.Voucher;

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
        VoucherDAO voucherDAO = new VoucherDAO();
        ClassDAO classDAO = new ClassDAO();
        EnrollmentDAO enrollDAO = new EnrollmentDAO();

        User currentUser = (User) request.getSession().getAttribute("user");
        if (currentUser == null) {
            response.sendRedirect("login");
            return;
        }

        if (action == null) {
            action = "all";
        }

        switch (action) {
            case "all":
                int roleId = currentUser.getRole().getRoleId();
                switch (roleId) {
                    case 1:
                        response.sendRedirect("dashboard?action=admin");
                        break;
                    case 2:
                        response.sendRedirect("dashboard?action=academic");
                        break;
                    case 3:
                        response.sendRedirect("dashboard?action=sale");
                        break;
                    case 4:
                        response.sendRedirect("dashboard?action=teacher");
                        break;
                    case 5:
                        response.sendRedirect("dashboard?action=student");
                        break;
                    default:
                        response.sendRedirect("login");
                        break;
                }
                return;
            case "sale":
                if (!currentUser.getRole().getManageFinance()) {
                    response.sendRedirect("dashboard?action=all");
                    return;
                }

                int totalLeads = leadDAO.countLeadsByFilters(null, "all", null, null, null);
                int convertedLeads = leadDAO.countLeadsByFilters(null, "Converted", null, null, null);
                int pendingPayments = paymentDAO.getPaymentCountByStatus("Pending");
                int approvedPayments = paymentDAO.getPaymentCountByStatus("Approved");
                List<model.Lead> latestNewLeads = leadDAO.searchAndFilterLeadsPaged(null, "New", null, null, null, 1, 6);
                List<Voucher> activeVouchers = voucherDAO.getActiveVoucher();
                List<Voucher> voucherPreview = activeVouchers.size() > 6 ? activeVouchers.subList(0, 6) : activeVouchers;
                List<Object[]> openClassList = classDAO.getOpenClassListForSales();
                List<Object[]> openClassPreview = openClassList.size() > 6 ? openClassList.subList(0, 6) : openClassList;

                request.setAttribute("totalLeads", totalLeads);
                request.setAttribute("convertedLeads", convertedLeads);
                request.setAttribute("pendingPayments", pendingPayments);
                request.setAttribute("approvedPayments", approvedPayments);
                request.setAttribute("latestNewLeads", latestNewLeads);
                request.setAttribute("activeVouchers", voucherPreview);
                request.setAttribute("openClassPreview", openClassPreview);
                request.setAttribute("home_view", "/sale/saleDashboard.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;

            case "admin":
                if (currentUser.getRole().getRoleId() != 1) {
                    response.sendRedirect("dashboard?action=all");
                    return;
                }
                List<User> list = userDAO.getAllUser();
                int totalUsers = list.size();
                double totalRevenue = paymentDAO.getTotalRevenue();
                int totalEnrollments = enrollDAO.getTotalEnrollments();
                double conversionRate = leadDAO.getConversionRate();

                int currentYear = Year.now().getValue();
                List<Double> monthlyRevenue = paymentDAO.getMonthlyRevenue(currentYear);
                String revenueDataString = monthlyRevenue.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(","));

                int currentMonthIndex = java.time.LocalDate.now().getMonthValue() - 1;
                double currentMonthRevenue = 0;
                double lastMonthRevenue = 0;
                double revenueGrowth = 0;

                if (currentMonthIndex >= 0 && currentMonthIndex < 12) {
                    currentMonthRevenue = monthlyRevenue.get(currentMonthIndex);

                    if (currentMonthIndex > 0) {
                        lastMonthRevenue = monthlyRevenue.get(currentMonthIndex - 1);
                    }
                }

                if (lastMonthRevenue > 0) {
                    revenueGrowth = ((currentMonthRevenue - lastMonthRevenue) / lastMonthRevenue) * 100;
                } else if (currentMonthRevenue > 0) {
                    revenueGrowth = 100;
                }

                List<Integer> adminMonthlyEnrollments = enrollDAO.getMonthlyNewEnrollments(currentYear);
                double currentMonthEnroll = 0;
                double lastMonthEnroll = 0;
                double enrollmentGrowth = 0;

                if (adminMonthlyEnrollments != null && currentMonthIndex >= 0 && currentMonthIndex < 12) {
                    currentMonthEnroll = adminMonthlyEnrollments.get(currentMonthIndex);
                    if (currentMonthIndex > 0) {
                        lastMonthEnroll = adminMonthlyEnrollments.get(currentMonthIndex - 1);
                    }
                }

                if (lastMonthEnroll > 0) {
                    enrollmentGrowth = ((currentMonthEnroll - lastMonthEnroll) / lastMonthEnroll) * 100;
                } else if (currentMonthEnroll > 0) {
                    enrollmentGrowth = 100;
                }

                double userGrowth = 8.5;
                double conversionGrowth = -1.2;

                List<Map<String, Object>> pendingPayment = paymentDAO.getPendingPayments();

                SystemLogDAO activityLogDAO = new SystemLogDAO();
                List<SystemLog> recentActivities = activityLogDAO.getRecentLogs("ALL");

                if (recentActivities.size() > 5) {
                    recentActivities = recentActivities.subList(0, 5);
                }

                request.setAttribute("recentActivities", recentActivities);
                request.setAttribute("pendingPayments", pendingPayment);
                request.setAttribute("enrollmentGrowth", enrollmentGrowth);
                request.setAttribute("userGrowth", userGrowth);
                request.setAttribute("conversionGrowth", conversionGrowth);
                request.setAttribute("currentMonthRevenue", currentMonthRevenue);
                request.setAttribute("revenueGrowth", revenueGrowth);
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

                session.setAttribute(
                        "user", freshUser);
                request.setAttribute(
                        "user", freshUser);

                roleId = freshUser.getRole().getRoleId();

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

            case "academic":
                if (!currentUser.getRole().getManageCourse()) {
                    response.sendRedirect("dashboard?action=all");
                    return;
                }
                ClassDAO classDAOAcademic = new ClassDAO();
                List<Object[]> classFillRateList = classDAOAcademic.getClassFillRateReport();
                List<Object[]> gradeEnrollmentSummaryList = classDAOAcademic.getGradeEnrollmentSummary();

                request.setAttribute("classFillRateList", classFillRateList);
                request.setAttribute("gradeEnrollmentSummaryList", gradeEnrollmentSummaryList);
                request.setAttribute("home_view", "/academic/academicDashboard.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;

            case "academicFillRateReport":
                response.sendRedirect("enrollment?action=classes");
                break;

            case "academicGradeEnrollmentReport":
                response.sendRedirect("enrollment?action=classes");
                break;

            case "teacher":
                if (currentUser.getRole().getRoleId() != 4) {
                    response.sendRedirect("dashboard?action=all");
                    return;
                }
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
                request.setAttribute(
                        "todaySlots", todaySlots);

                List<model.Classes> tClasses = tDAO.getAllClassOfTeacherID(tId);
                java.util.Map<Integer, Integer> progressMap = new java.util.HashMap<>();

                for (model.Classes c : tClasses) {
                    progressMap.put(c.getClassid(), tDAO.getClassProgress(c.getClassid()));
                }

                request.setAttribute(
                        "totalSlotsTaught", tDAO.getTotalSlotsTaught(tId));
                request.setAttribute(
                        "teacherClasses", tClasses);
                request.setAttribute(
                        "progressMap", progressMap);
                request.setAttribute(
                        "totalStudents", tDAO.getTotalStudentsByTeacher(tId));

                double avgRating = tDAO.getAverageRating(tId);
                java.util.Map<String, Object> fData = tDAO.getTeacherFeedbackData(tId);
                List<model.Feedback> allF = (List<model.Feedback>) fData.get("feedbackList");

                request.setAttribute(
                        "avgRating", String.format("%.1f", avgRating));
                if (allF != null) {
                    request.setAttribute("latestFeedbacks", allF.size() > 5 ? allF.subList(0, 5) : allF);
                }
                request.setAttribute(
                        "studentNameMap", fData.get("studentNameMap"));

                request.setAttribute(
                        "home_view", "teacher/teacherDashboard.jsp");
                request.getRequestDispatcher(
                        "dashboard.jsp").forward(request, response);
                break;

            case "student":

                User sUser = (User) request.getSession().getAttribute("user");
                if (sUser == null) {
                    response.sendRedirect("login");
                    return;
                }

                int studentId = sUser.getUserId();

                dao.ScheduleDAO scheduleDAO = new dao.ScheduleDAO();
                AttendanceDAO attendanceDAO = new AttendanceDAO();

//                List<Object[]> studentClasses = classDAO.getStudentClasses(studentId);
                java.time.LocalDate todayDate = java.time.LocalDate.now();
                java.time.LocalDate startOfWeek = todayDate.with(java.time.DayOfWeek.MONDAY);
                java.time.LocalDate endOfWeek = todayDate.with(java.time.DayOfWeek.SUNDAY);

                // ===== Classes của student =====
                List<Object[]> studentClasses = classDAO.getStudentClasses(
                        studentId,
                        startOfWeek,
                        endOfWeek
                );

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

                request.setAttribute(
                        "totalClasses", totalClasses);
                request.setAttribute(
                        "studentClasses", studentClasses);
//                request.setAttribute("weeklySchedule", weeklySchedule);
                request.setAttribute(
                        "todaySchedule", todaySchedule);
                request.setAttribute(
                        "summary", summary);

                request.setAttribute(
                        "home_view", "student/studentDashboard.jsp");
                request.getRequestDispatcher(
                        "dashboard.jsp").forward(request, response);

                break;

            case "report":
                SystemLogDAO logDAO = new SystemLogDAO();
                String filterAction = request.getParameter("filterAction");
                if (filterAction == null) {
                    filterAction = "ALL";
                }
                List<SystemLog> logs = logDAO.getRecentLogs(filterAction);

                request.setAttribute("systemLogs", logs);
                request.setAttribute("currentFilter", filterAction);

                // --- SYSTEM USAGE ---
                Map<String, Integer> usageMap = userDAO.getUserDemographics();

                String chartLabels = usageMap.keySet().stream().map(k -> "'" + k + "'").collect(Collectors.joining(","));
                String chartData = usageMap.values().stream().map(String::valueOf).collect(Collectors.joining(","));

                request.setAttribute("chartLabels", chartLabels);
                request.setAttribute("chartData", chartData);
                request.setAttribute("usageStats", usageMap);
                // --- GROWTH REPORT  ---
                int currentYearReport = java.time.Year.now().getValue();
                List<Integer> monthlyEnrollments = enrollDAO.getMonthlyNewEnrollments(currentYearReport);

                String enrollDataStr = monthlyEnrollments.stream().map(String::valueOf).collect(Collectors.joining(","));

                request.setAttribute("growthLabels", "'Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'");
                request.setAttribute("growthData", enrollDataStr);

                request.setAttribute("home_view", "/admin/systemReport.jsp");
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
