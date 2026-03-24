/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.ClassDAO;
import dao.EnrollmentDAO;
import dao.ScheduleDAO;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import model.Classes;
import model.Schedule;
import model.User;

/**
 *
 * @author Legion
 */
@WebServlet(name = "ClassController", urlPatterns = {"/class"})
public class ClassController extends HttpServlet {

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
            out.println("<title>Servlet ClassController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ClassController at " + request.getContextPath() + "</h1>");
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
        TeacherDAO dao = new TeacherDAO();
        ClassDAO classDAO = new ClassDAO();
        ScheduleDAO scheduleDAO = new ScheduleDAO();
        String action = request.getParameter("action");
        String keyword = request.getParameter("keyword");
        String status = request.getParameter("status");

        int page = 1;
        int pageSize = 6;

        if (action == null) {
            action = "all";
        }

        User currentUser = (User) request.getSession().getAttribute("user");

        if (currentUser == null) {
            response.sendRedirect("login");
            return;
        }

        String roleName = currentUser.getRole() != null ? currentUser.getRole().getRoleName() : "";

        if (action.equals("all")) {
            if (!roleName.equalsIgnoreCase("Teacher")) {
                request.getSession().setAttribute("message", "Access Denied: Only Teachers can view this page!");
                request.getSession().setAttribute("messageType", "error");
                response.sendRedirect("dashboard");
                return;
            }
        } else if (action.equals("myClasses") || action.equals("availableClass")) {
            if (!roleName.equalsIgnoreCase("Student")) {
                request.getSession().setAttribute("message", "Access Denied: This feature is for Students only!");
                request.getSession().setAttribute("messageType", "error");
                response.sendRedirect("dashboard");
                return;
            }
        }

        switch (action) {
            case "all":
                User user = (User) request.getSession().getAttribute("user");
                if (user != null) {
                    int teacherID = user.getUserId();
                    List<Classes> list = dao.getAllClassOfTeacherID(teacherID);
                    request.setAttribute("ClassList", list);
                    request.setAttribute("home_view", "teacher/teacher_classlist.jsp");
                    request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                } else {
                    response.sendRedirect("login.jsp");
                }
                break;

            case "availableClass":

                Integer teacherId = null;
                if (request.getParameter("teacherId") != null
                        && !request.getParameter("teacherId").isEmpty()) {
                    teacherId = Integer.parseInt(request.getParameter("teacherId"));
                }

                java.sql.Date fromDate = null;
                java.sql.Date toDate = null;

                if (request.getParameter("fromDate") != null
                        && !request.getParameter("fromDate").isEmpty()) {
                    fromDate = java.sql.Date.valueOf(request.getParameter("fromDate"));
                }

                if (request.getParameter("toDate") != null
                        && !request.getParameter("toDate").isEmpty()) {
                    toDate = java.sql.Date.valueOf(request.getParameter("toDate"));
                }

                if (request.getParameter("page") != null) {
                    page = Integer.parseInt(request.getParameter("page"));
                }

                // ===== 3. CALL DAO (NEW) =====
                List<Object[]> classList = classDAO.getClassesAdvanced(
                        keyword,
                        teacherId,
                        status,
                        fromDate,
                        toDate,
                        page,
                        pageSize
                );

                // ===== 4. ADD SCHEDULE INFO =====
                for (Object[] row : classList) {

                    int classId = (int) row[0];

                    List<Schedule> schedules = scheduleDAO.getSchedulesByClass(classId);

                    Set<Integer> dayNumbers = new HashSet<>();
                    String timeRange = "";

                    for (Schedule s : schedules) {

                        LocalDate learningDate
                                = ((java.sql.Date) s.getLearningDate()).toLocalDate();

                        int dayValue = learningDate.getDayOfWeek().getValue();
                        dayNumbers.add(dayValue);

                        if (timeRange.isEmpty()) {
                            timeRange = s.getSlot().getStartTime()
                                    + " - "
                                    + s.getSlot().getEndTime();
                        }
                    }

                    String[] dayMap = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

                    List<String> orderedDays = new ArrayList<>();

                    for (int i = 1; i <= 7; i++) {
                        if (dayNumbers.contains(i)) {
                            orderedDays.add(dayMap[i - 1]);
                        }
                    }

                    String dayString = String.join("-", orderedDays);

                    Object[] newRow = new Object[10];
                    System.arraycopy(row, 0, newRow, 0, row.length);

                    newRow[8] = dayString;
                    newRow[9] = timeRange;

                    int index = classList.indexOf(row);
                    classList.set(index, newRow);
                }

                // ===== 5. SET ATTRIBUTE =====
                request.setAttribute("classList", classList);
                request.setAttribute("currentPage", page);

                // giữ lại filter trên UI
                request.setAttribute("keyword", keyword);
                request.setAttribute("status", status);
                request.setAttribute("teacherId", teacherId);
                request.setAttribute("fromDate", fromDate);
                request.setAttribute("toDate", toDate);

                request.setAttribute("home_view", "student/studentClassList.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);

                break;

            case "detail":

                String classIdRaw = request.getParameter("classId");
                int classId = Integer.parseInt(classIdRaw);

                // lấy nguồn trang
                String source = request.getParameter("source");
                if (source == null) {
                    source = "availableClass"; // mặc định
                }

                Object[] data = classDAO.getClassDetail(classId);

                Classes classDetail = (Classes) data[0];
                String teacherName = (String) data[1];
                String teacherAvatar = (String) data[2];
                String roomName = (String) data[3];

                List<Schedule> schedules = scheduleDAO.getSchedulesByClass(classId);

                // ===== VOUCHER LOGIC =====
                User userV = (User) request.getSession().getAttribute("user");

                dao.VoucherDAO voucherDAO = new dao.VoucherDAO();
                model.Voucher appliedVoucher = null;
                double discountAmount = 0;
                double finalPrice = classDetail.getCourse().getTuitionFee().doubleValue();

                if (userV != null) {
                    appliedVoucher = voucherDAO.getVoucherByStudentAndClass(
                            userV.getUserId(),
                            classId
                    );

                    if (appliedVoucher != null) {
                        discountAmount = voucherDAO.calculateDiscountAmount(
                                appliedVoucher,
                                finalPrice
                        );

                        finalPrice = finalPrice - discountAmount;

                        if (finalPrice < 0) {
                            finalPrice = 0;
                        }
                    }
                }

                // ===== SET ATTRIBUTE =====
                request.setAttribute("classDetail", classDetail);
                request.setAttribute("teacherName", teacherName);
                request.setAttribute("teacherAvatar", teacherAvatar);
                request.setAttribute("roomName", roomName);
                request.setAttribute("scheduleList", schedules);

                // voucher
                request.setAttribute("appliedVoucher", appliedVoucher);
                request.setAttribute("discountAmount", discountAmount);
                request.setAttribute("finalPrice", finalPrice);

                // breadcrumb
                request.setAttribute("sourcePage", source);

                request.setAttribute("home_view", "student/classDetail.jsp");

                request.getRequestDispatcher("dashboard.jsp").forward(request, response);

                break;

            case "myClasses":

                User student = (User) request.getSession().getAttribute("user");

                if (student == null) {
                    response.sendRedirect("login.jsp");
                    return;
                }

                int studentId = student.getUserId();

                String keyword1 = request.getParameter("keyword");
                String status1 = request.getParameter("status");

                String pageRaw = request.getParameter("page");
                if (pageRaw != null && !pageRaw.isBlank()) {
                    try {
                        page = Integer.parseInt(pageRaw);
                    } catch (Exception e) {
                        page = 1;
                    }
                }

                LocalDate todayDate = LocalDate.now();
                LocalDate startOfWeek = todayDate.with(DayOfWeek.MONDAY);
                LocalDate endOfWeek = todayDate.with(DayOfWeek.SUNDAY);

                int totalRecords = classDAO.countStudentClassesAdvanced(
                        studentId,
                        startOfWeek,
                        endOfWeek,
                        keyword1,
                        status1
                );

                int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

                System.out.println("totalRecords = " + totalRecords);
                System.out.println("totalPages = " + totalPages);
                
                if (page < 1) {
                    page = 1;
                }
                if (page > totalPages) {
                    page = totalPages;
                }

                List<Object[]> pageList = classDAO.getStudentClassesAdvanced(
                        studentId,
                        startOfWeek,
                        endOfWeek,
                        keyword1,
                        status1,
                        page,
                        pageSize
                );

                request.setAttribute("classList", pageList);
                request.setAttribute("currentPage", page);
                request.setAttribute("totalPages", totalPages);
                request.setAttribute("keyword", keyword1);
                request.setAttribute("status", status1);

                request.setAttribute("home_view", "student/studentMyClassList.jsp");

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
