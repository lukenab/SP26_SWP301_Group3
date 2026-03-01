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
import dao.ScheduleDAO;
import dao.SlotDAO;
import dao.UserDAO;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import model.Slot;
import java.time.ZoneId;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "ScheduleController", urlPatterns = {"/schedule"})
public class ScheduleController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || user.getRole() == null) {
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
                    selectedDate = java.time.LocalDate.now().toString(); // Sẽ ra "2026-02-24"
                }

                request.setAttribute("selectedDate", selectedDate);

                List<Schedule> scheduleList = teacherDAO.getTeachingSchedule(user.getUserId(), selectedDate);

                request.setAttribute("selectedDate", selectedDate);
                request.setAttribute("weekdays", weekdays);
                request.setAttribute("slots", slots);
                request.setAttribute("slotTimes", slotTimes);
                request.setAttribute("scheduleList", scheduleList);

                request.setAttribute("home_view", "teacher/teacher_schedule.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;

            case "viewByClass":
                try {
                    int classId = Integer.parseInt(request.getParameter("classId"));
                    TeacherDAO dao = new TeacherDAO();
                    List<Schedule> scheduleListByClass
                            = dao.getScheduleByClassId(classId, user.getUserId());
                    String className = null;
                    if (!scheduleListByClass.isEmpty()) {
                        className = scheduleListByClass
                                .get(0)
                                .getClasses()
                                .getClassName();
                    }

                    request.setAttribute("classId", classId);
                    request.setAttribute("className", className);
                    request.setAttribute("scheduleList", scheduleListByClass);
                    request.setAttribute("weekdays", weekdays);
                    request.setAttribute("slots", slots);
                    request.setAttribute("slotTimes", slotTimes);

                    request.setAttribute("home_view", "teacher/view_class_schedule.jsp");

                    request.getRequestDispatcher("dashboard.jsp")
                            .forward(request, response);

                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendRedirect("class");
                }
                break;

            case "studentView":

                // ===== 1. Lấy StudentID =====
                int studentId = user.getUserId();

                // ===== 2. Xác định tuần =====
                LocalDate weekStart;
                String weekStartParam = request.getParameter("weekStart");

                if (weekStartParam != null && !weekStartParam.isBlank()) {
                    weekStart = LocalDate.parse(weekStartParam);
                } else {
                    weekStart = LocalDate.now()
                            .with(java.time.DayOfWeek.MONDAY);
                }

                LocalDate weekEnd = weekStart.plusDays(6);

                // ===== 3. Lấy dữ liệu =====
                ScheduleDAO scheduleDAO = new ScheduleDAO();
                SlotDAO slotDAO = new SlotDAO();
                UserDAO userDAO = new UserDAO();

                List<Slot> studentSlots = slotDAO.getAllSlots();

                List<Schedule> studentScheduleList
                        = scheduleDAO.getScheduleByStudentWeek(
                                studentId,
                                weekStart.toString(),
                                weekEnd.toString()
                        );

                // ===== 4. Map chứa tên giáo viên =====
                Map<Integer, User> employeeUsers = new HashMap<>();

                for (Schedule schedule : studentScheduleList) {
                    if (schedule.getEmployee() != null) {
                        int empId = schedule.getEmployee().getEmployeeId();

                        // tránh query lặp
                        if (!employeeUsers.containsKey(empId)) {
                            User empUser = userDAO.getUserByEmployeeId(empId);
                            employeeUsers.put(empId, empUser);
                        }
                    }
                }

                // ===== 5. Build Map xử lý bằng LocalDate =====
                Map<LocalDate, Map<Integer, Schedule>> tempSchedule
                        = new LinkedHashMap<>();

                for (int i = 0; i < 7; i++) {
                    LocalDate date = weekStart.plusDays(i);
                    tempSchedule.put(date, new HashMap<>());
                }

                for (Schedule schedule : studentScheduleList) {

                    LocalDate learningDate
                            = ((java.sql.Date) schedule.getLearningDate())
                                    .toLocalDate();

                    int slotId = schedule.getSlot().getSlotID();

                    Map<Integer, Schedule> daySchedule
                            = tempSchedule.get(learningDate);

                    if (daySchedule != null) {
                        daySchedule.put(slotId, schedule);
                    }
                }

                // ===== 6. Convert sang java.sql.Date cho JSP =====
                Map<java.sql.Date, Map<Integer, Schedule>> weeklySchedule
                        = new LinkedHashMap<>();

                for (Map.Entry<LocalDate, Map<Integer, Schedule>> entry : tempSchedule.entrySet()) {
                    weeklySchedule.put(
                            java.sql.Date.valueOf(entry.getKey()),
                            entry.getValue()
                    );
                }

                // ===== 7. Gửi sang JSP =====
                request.setAttribute("weeklySchedule", weeklySchedule);
                request.setAttribute("slots", studentSlots);
                request.setAttribute("weekStart", weekStart);
                request.setAttribute("employeeUsers", employeeUsers);

                request.setAttribute("home_view", "student/studentSchedule.jsp");
                request.getRequestDispatcher("dashboard.jsp")
                        .forward(request, response);

                break;
        }
    }

}
