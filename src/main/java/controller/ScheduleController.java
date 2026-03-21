/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.ClassDAO;
import dao.ScheduleDAO;
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
import dao.ScheduleDAO;
import dao.SlotDAO;
import dao.UserDAO;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import model.Slot;
import java.time.ZoneId;
import java.util.ArrayList;

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

        // Allow both teacher (roleId 4) and academic staff (roleId 2)
        int roleId = user.getRole().getRoleId();
        if (roleId != 4 && roleId != 2 && roleId != 5 ) {
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

            case "manage":
                // For academic staff to manage all schedules
                ScheduleDAO scheduleDAO = new ScheduleDAO();
                ClassDAO classDAO = new ClassDAO();

                String filterClassId = request.getParameter("classId");
                String filterRoomId = request.getParameter("roomId");
                Integer classFilterId = null;
                Integer roomFilterId = null;
                List<Schedule> managementScheduleList = new ArrayList<>();

                if (filterClassId != null && !filterClassId.isEmpty() && !filterClassId.equals("0")) {
                    classFilterId = Integer.parseInt(filterClassId);
                    // Save selected classId to session for later use
                    session.setAttribute("selectedClassId", classFilterId);
                } else {
                    session.setAttribute("selectedClassId", 0);
                }

                if (filterRoomId != null && !filterRoomId.isEmpty() && !filterRoomId.equals("0")) {
                    roomFilterId = Integer.parseInt(filterRoomId);
                    session.setAttribute("selectedRoomId", roomFilterId);
                } else {
                    session.setAttribute("selectedRoomId", 0);
                }

                // Load schedules using selected filters (class/room/week)
                managementScheduleList = scheduleDAO.getSchedulesForManagement(selectedDate, classFilterId, roomFilterId);

                // Save selected date to session
                session.setAttribute("selectedDate", selectedDate);

                List<Object[]> allClasses = classDAO.getClassManagementList();
                List<Object[]> allRooms = scheduleDAO.getAllRooms();
                List<Object[]> allTeachers = scheduleDAO.getAllTeachers();

                request.setAttribute("selectedDate", selectedDate);
                request.setAttribute("classId", classFilterId);
                request.setAttribute("roomId", roomFilterId);
                request.setAttribute("weekdays", weekdays);
                request.setAttribute("slots", allSlots);
                request.setAttribute("scheduleList", managementScheduleList);
                request.setAttribute("allClasses", allClasses);
                request.setAttribute("allRooms", allRooms);
                request.setAttribute("allTeachers", allTeachers);
                request.setAttribute("home_view", "academic/manageSchedule.jsp");

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

            case "get":
                // Get schedule details as JSON for view/edit modals
                try {
                    int scheduleId = Integer.parseInt(request.getParameter("scheduleId"));
                    ScheduleDAO scheduleDAO2 = new ScheduleDAO();
                    Schedule schedule = scheduleDAO2.getScheduleById(scheduleId);

                    if (schedule != null) {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        PrintWriter out = response.getWriter();

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String json = String.format(
                                "{\"scheduleId\":%d,\"classId\":%d,\"className\":\"%s\",\"roomId\":%d,\"roomName\":\"%s\","
                                + "\"slotId\":%d,\"slotTime\":\"%s - %s\",\"learningDate\":\"%s\",\"attendanceStatus\":%b}",
                                schedule.getScheduleId(),
                                schedule.getClasses().getClassid(),
                                schedule.getClasses().getClassName(),
                                schedule.getRoom().getRoomId(),
                                schedule.getRoom().getRoomName(),
                                schedule.getSlot().getSlotID(),
                                schedule.getSlot().getStartTime(),
                                schedule.getSlot().getEndTime(),
                                sdf.format(schedule.getLearningDate()),
                                schedule.isAttendanceStatus()
                        );
                        out.print(json);
                        out.flush();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
                break;

            case "createForm":
                // Show create schedule form
                ScheduleDAO scheduleDAO3 = new ScheduleDAO();
                ClassDAO classDAO2 = new ClassDAO();
                SlotDAO slotDAO2 = new SlotDAO();

                List<Object[]> allClasses2 = classDAO2.getClassManagementList();
                List<Object[]> allRooms2 = scheduleDAO3.getAllRooms();
                List<Slot> allSlots2 = slotDAO2.getAllSlots();

                request.setAttribute("allClasses", allClasses2);
                request.setAttribute("allRooms", allRooms2);
                request.setAttribute("slots", allSlots2);
                request.setAttribute("home_view", "academic/createSchedule.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;

            case "editForm":
                // Show edit schedule form
                System.out.println("=== EDIT FORM ACTION CALLED ===");
                try {
                    int editScheduleId = Integer.parseInt(request.getParameter("scheduleId"));
                    System.out.println("Schedule ID to edit: " + editScheduleId);

                    ScheduleDAO scheduleDAO4 = new ScheduleDAO();
                    ClassDAO classDAO3 = new ClassDAO();
                    SlotDAO slotDAO3 = new SlotDAO();

                    Schedule editSchedule = scheduleDAO4.getScheduleById(editScheduleId);
                    System.out.println("Schedule found: " + (editSchedule != null));

                    if (editSchedule == null) {
                        System.out.println("ERROR: Schedule is NULL!");
                        session.setAttribute("message", "Schedule not found!");
                        session.setAttribute("messageType", "error");
                        response.sendRedirect("schedule?action=manage");
                        return;
                    }

                    List<Object[]> allClasses3 = classDAO3.getClassManagementList();
                    List<Object[]> allRooms3 = scheduleDAO4.getAllRooms();
                    List<Slot> allSlots3 = slotDAO3.getAllSlots();

                    System.out.println("Data loaded - Classes: " + allClasses3.size() + ", Rooms: " + allRooms3.size() + ", Slots: " + allSlots3.size());

                    request.setAttribute("schedule", editSchedule);
                    request.setAttribute("allClasses", allClasses3);
                    request.setAttribute("allRooms", allRooms3);
                    request.setAttribute("slots", allSlots3);
                    request.setAttribute("home_view", "academic/editSchedule.jsp");

                    System.out.println("Forwarding to editSchedule.jsp...");
                    request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                    System.out.println("Forward completed!");
                } catch (Exception e) {
                    System.out.println("EXCEPTION in editForm: " + e.getMessage());
                    e.printStackTrace();
                    session.setAttribute("message", "Error loading edit form: " + e.getMessage());
                    session.setAttribute("messageType", "error");
                    response.sendRedirect("schedule?action=manage");
                }
                break;

            case "viewDetail":
                // Show schedule details
                try {
                    int viewScheduleId = Integer.parseInt(request.getParameter("scheduleId"));
                    ScheduleDAO scheduleDAO5 = new ScheduleDAO();
                    Schedule viewSchedule = scheduleDAO5.getScheduleById(viewScheduleId);

                    // Save classId to session for Back to List button to use
                    if (viewSchedule != null && viewSchedule.getClasses() != null) {
                        session.setAttribute("selectedClassId", viewSchedule.getClasses().getClassid());
                    }

                    request.setAttribute("schedule", viewSchedule);
                    request.setAttribute("home_view", "academic/viewSchedule.jsp");
                    request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendRedirect("schedule?action=manage");
                }
                break;

            case "delete":
                // Show delete schedule confirmation form
                try {
                    int deleteScheduleId = Integer.parseInt(request.getParameter("scheduleId"));
                    ScheduleDAO scheduleDAO6 = new ScheduleDAO();
                    Schedule deleteSchedule = scheduleDAO6.getScheduleById(deleteScheduleId);

                    if (deleteSchedule == null) {
                        session.setAttribute("message", "Schedule not found.");
                        session.setAttribute("messageType", "error");
                        response.sendRedirect("schedule?action=manage");
                        return;
                    }

                    // Save classId to session for Cancel button to use
                    if (deleteSchedule.getClasses() != null) {
                        session.setAttribute("selectedClassId", deleteSchedule.getClasses().getClassid());
                    }

                    // Save learning date to session
                    if (deleteSchedule.getLearningDate() != null) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        session.setAttribute("selectedDate", sdf.format(deleteSchedule.getLearningDate()));
                    }

                    // Get similar schedules (same class, slot, room) for series delete option
                    List<Schedule> similarSchedules = scheduleDAO6.getSimilarSchedules(deleteScheduleId);
                    int relatedCount = similarSchedules != null ? similarSchedules.size() : 0;

                    request.setAttribute("schedule", deleteSchedule);
                    request.setAttribute("relatedCount", relatedCount);
                    request.setAttribute("similarSchedules", similarSchedules);
                    request.setAttribute("home_view", "academic/deleteSchedule.jsp");
                    request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendRedirect("schedule?action=manage");
                }
                break;

            case "studentView":

                int studentId = user.getUserId();

                LocalDate weekStart;
                String weekStartParam = request.getParameter("weekStart");

                if (weekStartParam != null && !weekStartParam.isBlank()) {
                    weekStart = LocalDate.parse(weekStartParam);
                } else {
                    weekStart = LocalDate.now()
                            .with(java.time.DayOfWeek.MONDAY);
                }

                LocalDate weekEnd = weekStart.plusDays(6);

                ScheduleDAO scheduleDAO1 = new ScheduleDAO();
                SlotDAO slotDAO1 = new SlotDAO();
                UserDAO userDAO = new UserDAO();

                List<Slot> studentSlots = slotDAO1.getAllSlots();

                List<Schedule> studentScheduleList
                        = scheduleDAO1.getScheduleByStudentWeek(
                                studentId,
                                weekStart.toString(),
                                weekEnd.toString()
                        );

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

                Map<java.sql.Date, Map<Integer, Schedule>> weeklySchedule
                        = new LinkedHashMap<>();

                for (Map.Entry<LocalDate, Map<Integer, Schedule>> entry : tempSchedule.entrySet()) {
                    weeklySchedule.put(
                            java.sql.Date.valueOf(entry.getKey()),
                            entry.getValue()
                    );
                }

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
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || user.getRole() == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");
        ScheduleDAO scheduleDAO = new ScheduleDAO();

        try {
            switch (action) {
                case "create":
                    // Create schedule (single or batch)
                    try {
                        int classId = Integer.parseInt(request.getParameter("classId"));
                        int roomId = Integer.parseInt(request.getParameter("roomId"));
                        int slotId = Integer.parseInt(request.getParameter("slotId"));
                        String learningDateStr = request.getParameter("learningDate");
                        Date learningDate = Date.valueOf(learningDateStr);

                        // Get recurring parameters
                        String recurringType = request.getParameter("recurringType");
                        if (recurringType == null || recurringType.isEmpty()) {
                            recurringType = "none";
                        }

                        // Get teacher ID from class
                        ClassDAO classDAO = new ClassDAO();
                        int teacherId = classDAO.getTeacherIdByClassId(classId);

                        // Check if this is a batch creation
                        if (!"none".equals(recurringType)) {
                            // Batch creation - get recurring parameters
                            String endCondition = request.getParameter("endCondition");
                            String endDateStr = request.getParameter("endDate");
                            String occurrencesStr = request.getParameter("occurrences");

                            Date endDate = null;
                            Integer occurrences = null;

                            if ("on".equals(endCondition) && endDateStr != null && !endDateStr.isEmpty()) {
                                endDate = Date.valueOf(endDateStr);
                            }

                            if ("after".equals(endCondition) && occurrencesStr != null && !occurrencesStr.isEmpty()) {
                                occurrences = Integer.parseInt(occurrencesStr);
                            }

                            // Get custom days for custom recurring pattern
                            String[] recurringDaysArray = request.getParameterValues("recurringDays");
                            String recurringDays = "";
                            if (recurringDaysArray != null && recurringDaysArray.length > 0) {
                                recurringDays = String.join(",", recurringDaysArray);
                            }

                            // Create multiple schedules
                            int createdCount = scheduleDAO.createMultipleSchedules(classId, roomId, slotId, learningDate, teacherId,
                                    recurringType, recurringDays, endCondition, endDate, occurrences);

                            if (createdCount > 0) {
                                session.setAttribute("message", "Successfully created " + createdCount + " schedule(s)!");
                                session.setAttribute("messageType", "success");
                            } else {
                                session.setAttribute("message", "Failed to create schedules or no dates were generated!");
                                session.setAttribute("messageType", "error");
                            }
                        } else {
                            // Single schedule creation - validate like before
                            // 1. Check if there's a schedule conflict
                            if (scheduleDAO.hasScheduleConflict(classId, slotId, learningDate, 0)) {
                                session.setAttribute("message", "Schedule conflict: This class already has a schedule at this time slot on this date!");
                                session.setAttribute("messageType", "error");
                                response.sendRedirect("schedule?action=manage");
                                return;
                            }

                            // 2. Check room availability
                            if (!scheduleDAO.isRoomAvailable(roomId, slotId, learningDate, 0)) {
                                session.setAttribute("message", "Room is not available for this time slot!");
                                session.setAttribute("messageType", "error");
                                response.sendRedirect("schedule?action=manage");
                                return;
                            }

                            // 3. Check teacher availability
                            if (!scheduleDAO.isTeacherAvailable(teacherId, slotId, learningDate, 0)) {
                                session.setAttribute("message", "Teacher is not available at this time slot on this date!");
                                session.setAttribute("messageType", "error");
                                response.sendRedirect("schedule?action=manage");
                                return;
                            }

                            // 4. Check if teacher exceeds 5 slots per week limit
                            if (scheduleDAO.teacherExceedsWeeklyLimit(teacherId, learningDate, 0)) {
                                int currentSlots = scheduleDAO.getTeacherWeeklySlotCount(teacherId, learningDate, 0);
                                session.setAttribute("message", "Teacher has reached the weekly limit! Current slots: " + currentSlots + "/5");
                                session.setAttribute("messageType", "error");
                                response.sendRedirect("schedule?action=manage");
                                return;
                            }

                            // Create single schedule
                            boolean success = scheduleDAO.createSchedule(classId, roomId, slotId, learningDate, teacherId, false);

                            if (success) {
                                session.setAttribute("message", "Schedule created successfully!");
                                session.setAttribute("messageType", "success");
                            } else {
                                session.setAttribute("message", "Failed to create schedule!");
                                session.setAttribute("messageType", "error");
                            }
                        }

                    } catch (Exception e) {
                        session.setAttribute("message", "Error creating schedule: " + e.getMessage());
                        session.setAttribute("messageType", "error");
                        e.printStackTrace();
                    }

                    // Save selected values to session for next manage view
                    try {
                        int classId = Integer.parseInt(request.getParameter("classId"));
                        int roomId = Integer.parseInt(request.getParameter("roomId"));
                        String learningDateStr = request.getParameter("learningDate");
                        session.setAttribute("selectedClassId", classId);
                        session.setAttribute("selectedRoomId", roomId);
                        session.setAttribute("selectedDate", learningDateStr);
                    } catch (Exception e) {
                        // Silently fail if parameters are missing
                    }

                    // Redirect back to manage with previously selected class and date
                    Integer savedClassId = (Integer) session.getAttribute("selectedClassId");
                    Integer savedRoomId = (Integer) session.getAttribute("selectedRoomId");
                    String savedDate = (String) session.getAttribute("selectedDate");

                    StringBuilder redirectUrl = new StringBuilder("schedule?action=manage");

                    if (savedClassId != null && savedClassId > 0) {
                        redirectUrl.append("&classId=").append(savedClassId);
                    } else {
                        redirectUrl.append("&classId=0");
                    }

                    if (savedRoomId != null && savedRoomId > 0) {
                        redirectUrl.append("&roomId=").append(savedRoomId);
                    } else {
                        redirectUrl.append("&roomId=0");
                    }

                    if (savedDate != null && !savedDate.isEmpty()) {
                        redirectUrl.append("&date=").append(savedDate);
                    }

                    response.sendRedirect(redirectUrl.toString());
                    break;

                case "update":
                    // Update schedule
                    try {
                        int scheduleId = Integer.parseInt(request.getParameter("scheduleId"));
                        int classId = Integer.parseInt(request.getParameter("classId"));
                        int roomId = Integer.parseInt(request.getParameter("roomId"));
                        int slotId = Integer.parseInt(request.getParameter("slotId"));
                        String learningDateStr = request.getParameter("learningDate");
                        Date learningDate = Date.valueOf(learningDateStr);

                        // Get existing schedule
                        Schedule existingSchedule = scheduleDAO.getScheduleById(scheduleId);
                        int teacherId = existingSchedule.getEmployee() != null
                                ? existingSchedule.getEmployee().getEmployeeId() : 0;
                        boolean attendanceStatus = existingSchedule.isAttendanceStatus();

                        // If teacherId is 0, get from class
                        if (teacherId == 0) {
                            ClassDAO classDAO = new ClassDAO();
                            teacherId = classDAO.getTeacherIdByClassId(classId);
                        }

                        // 1. Check if there's a schedule conflict (exclude current schedule)
                        if (scheduleDAO.hasScheduleConflict(classId, slotId, learningDate, scheduleId)) {
                            session.setAttribute("message", "Schedule conflict: This class already has a schedule at this time slot on this date!");
                            session.setAttribute("messageType", "error");
                            response.sendRedirect("schedule?action=manage");
                            return;
                        }

                        // 2. Check room availability (exclude current schedule)
                        if (!scheduleDAO.isRoomAvailable(roomId, slotId, learningDate, scheduleId)) {
                            session.setAttribute("message", "Room is not available for this time slot!");
                            session.setAttribute("messageType", "error");
                            response.sendRedirect("schedule?action=manage");
                            return;
                        }

                        // 3. Check teacher availability (exclude current schedule)
                        if (!scheduleDAO.isTeacherAvailable(teacherId, slotId, learningDate, scheduleId)) {
                            session.setAttribute("message", "Teacher is not available at this time slot on this date!");
                            session.setAttribute("messageType", "error");
                            response.sendRedirect("schedule?action=manage");
                            return;
                        }

                        // 4. Check if teacher exceeds 5 slots per week limit (exclude current schedule)
                        if (scheduleDAO.teacherExceedsWeeklyLimit(teacherId, learningDate, scheduleId)) {
                            int currentSlots = scheduleDAO.getTeacherWeeklySlotCount(teacherId, learningDate, scheduleId);
                            session.setAttribute("message", "Teacher has reached the weekly limit! Current slots: " + currentSlots + "/5");
                            session.setAttribute("messageType", "error");
                            response.sendRedirect("schedule?action=manage");
                            return;
                        }

                        // Update schedule
                        boolean success = scheduleDAO.editSchedule(scheduleId, classId, roomId, slotId,
                                learningDate, teacherId, attendanceStatus);

                        if (success) {
                            session.setAttribute("message", "Schedule updated successfully!");
                            session.setAttribute("messageType", "success");
                        } else {
                            session.setAttribute("message", "Failed to update schedule!");
                            session.setAttribute("messageType", "error");
                        }

                    } catch (Exception e) {
                        session.setAttribute("message", "Error updating schedule: " + e.getMessage());
                        session.setAttribute("messageType", "error");
                    }

                    Integer savedClassId3 = (Integer) session.getAttribute("selectedClassId");
                    Integer savedRoomId3 = (Integer) session.getAttribute("selectedRoomId");
                    String savedDate3 = (String) session.getAttribute("selectedDate");

                    StringBuilder redirectUrl3 = new StringBuilder("schedule?action=manage");
                    redirectUrl3.append("&classId=").append(savedClassId3 != null ? savedClassId3 : 0);
                    redirectUrl3.append("&roomId=").append(savedRoomId3 != null ? savedRoomId3 : 0);
                    if (savedDate3 != null && !savedDate3.isEmpty()) {
                        redirectUrl3.append("&date=").append(savedDate3);
                    }

                    response.sendRedirect(redirectUrl3.toString());
                    break;

                case "delete":
                    // Delete schedule
                    try {
                        int scheduleId = Integer.parseInt(request.getParameter("scheduleId"));
                        String deleteScope = request.getParameter("deleteScope"); // "single" or "series"

                        // Check if attendance has been taken
                        Schedule schedule = scheduleDAO.getScheduleById(scheduleId);
                        if (schedule != null && schedule.isAttendanceStatus()) {
                            session.setAttribute("message", "Cannot delete schedule with attendance already taken!");
                            session.setAttribute("messageType", "error");
                            response.sendRedirect("schedule?action=manage");
                            return;
                        }

                        boolean success = false;
                        String message = "";

                        if ("series".equals(deleteScope)) {
                            // Delete all similar schedules (same class, slot, room)
                            int deletedCount = scheduleDAO.deleteSimilarSchedules(scheduleId);

                            if (deletedCount > 0) {
                                message = "Successfully deleted " + deletedCount + " schedule(s) in the series!";
                                session.setAttribute("messageType", "success");
                            } else {
                                message = "No schedules were deleted. They may have attendance already taken.";
                                session.setAttribute("messageType", "error");
                            }
                            session.setAttribute("message", message);
                        } else {
                            // Delete only single schedule (default)
                            success = scheduleDAO.deleteSchedule(scheduleId);

                            if (success) {
                                session.setAttribute("message", "Schedule deleted successfully!");
                                session.setAttribute("messageType", "success");
                            } else {
                                session.setAttribute("message", "Failed to delete schedule!");
                                session.setAttribute("messageType", "error");
                            }
                        }

                    } catch (Exception e) {
                        session.setAttribute("message", "Error deleting schedule: " + e.getMessage());
                        session.setAttribute("messageType", "error");
                    }

                    // Redirect back to manage with previously selected class and date
                    Integer savedClassId2 = (Integer) session.getAttribute("selectedClassId");
                    Integer savedRoomId2 = (Integer) session.getAttribute("selectedRoomId");
                    String savedDate2 = (String) session.getAttribute("selectedDate");

                    StringBuilder redirectUrl2 = new StringBuilder("schedule?action=manage");

                    if (savedClassId2 != null && savedClassId2 > 0) {
                        redirectUrl2.append("&classId=").append(savedClassId2);
                    } else {
                        redirectUrl2.append("&classId=0");
                    }

                    if (savedRoomId2 != null && savedRoomId2 > 0) {
                        redirectUrl2.append("&roomId=").append(savedRoomId2);
                    } else {
                        redirectUrl2.append("&roomId=0");
                    }

                    if (savedDate2 != null && !savedDate2.isEmpty()) {
                        redirectUrl2.append("&date=").append(savedDate2);
                    }

                    response.sendRedirect(redirectUrl2.toString());
                    break;

                default:
                    response.sendRedirect("schedule?action=manage");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", "An error occurred: " + e.getMessage());
            session.setAttribute("messageType", "error");
            response.sendRedirect("schedule?action=manage");
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