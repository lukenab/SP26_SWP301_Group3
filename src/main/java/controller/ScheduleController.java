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
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

        if (user == null || user.getRole() == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Allow both teacher (roleId 4) and academic staff (roleId 2)
        int roleId = user.getRole().getRoleId();
        if (roleId != 4 && roleId != 2) {
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
            selectedDate = java.time.LocalDate.now().toString();
        }

        switch (action) {
            case "view":
                String classIdParam = request.getParameter("classId");
                List<Schedule> scheduleList;

                if (classIdParam != null && !classIdParam.isEmpty()) {
                
                    int classId = Integer.parseInt(classIdParam);
                    scheduleList = teacherDAO.getScheduleByClassId(classId, user.getUserId(), selectedDate);

                    List<Classes> allClass = teacherDAO.getAllClassOfTeacherID(user.getUserId());
                    String className = "";
                    for (Classes c : allClass) {
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
                request.setAttribute("slots", allSlots);
                request.setAttribute("scheduleList", scheduleList);

                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;

            case "manage":
                // For academic staff to manage all schedules
                ScheduleDAO scheduleDAO = new ScheduleDAO();
                ClassDAO classDAO = new ClassDAO();

                String filterClassId = request.getParameter("classId");
                Integer classFilterId = null;
                List<Schedule> managementScheduleList = new ArrayList<>();

                if (filterClassId != null && !filterClassId.isEmpty() && !filterClassId.equals("0")) {
                    classFilterId = Integer.parseInt(filterClassId);
                    // Save selected classId to session for later use
                    session.setAttribute("selectedClassId", classFilterId);
                    // Only load schedules if a class is selected
                    managementScheduleList = scheduleDAO.getSchedulesForManagement(selectedDate, classFilterId);
                }

                // Save selected date to session
                session.setAttribute("selectedDate", selectedDate);

                List<Object[]> allClasses = classDAO.getClassManagementList();
                List<Object[]> allRooms = scheduleDAO.getAllRooms();
                List<Object[]> allTeachers = scheduleDAO.getAllTeachers();

                request.setAttribute("selectedDate", selectedDate);
                request.setAttribute("classId", classFilterId);
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
                            "{\"scheduleId\":%d,\"classId\":%d,\"className\":\"%s\",\"roomId\":%d,\"roomName\":\"%s\"," +
                            "\"slotId\":%d,\"slotTime\":\"%s - %s\",\"learningDate\":\"%s\",\"attendanceStatus\":%b}",
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

                    request.setAttribute("schedule", deleteSchedule);
                    request.setAttribute("home_view", "academic/deleteSchedule.jsp");
                    request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendRedirect("schedule?action=manage");
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
                    handleCreateSchedule(request, response, scheduleDAO, session);
                    break;

                case "update":
                    handleUpdateSchedule(request, response, scheduleDAO, session);
                    break;

                case "delete":
                    handleDeleteSchedule(request, response, scheduleDAO, session);
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

    private void handleCreateSchedule(HttpServletRequest request, HttpServletResponse response,
            ScheduleDAO scheduleDAO, HttpSession session) throws IOException {
        try {
            int classId = Integer.parseInt(request.getParameter("classId"));
            int roomId = Integer.parseInt(request.getParameter("roomId"));
            int slotId = Integer.parseInt(request.getParameter("slotId"));
            String learningDateStr = request.getParameter("learningDate");
            Date learningDate = Date.valueOf(learningDateStr);

            // Get teacher ID from class
            ClassDAO classDAO = new ClassDAO();
            int teacherId = classDAO.getTeacherIdByClassId(classId);

            // 1. Check if there's a schedule conflict (same class, same slot, same date)
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

            // 3. Check teacher availability (same slot, same date)
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

            // Create schedule
            boolean success = scheduleDAO.createSchedule(classId, roomId, slotId, learningDate, teacherId, false);

            if (success) {
                session.setAttribute("message", "Schedule created successfully!");
                session.setAttribute("messageType", "success");
            } else {
                session.setAttribute("message", "Failed to create schedule!");
                session.setAttribute("messageType", "error");
            }

        } catch (Exception e) {
            session.setAttribute("message", "Error creating schedule: " + e.getMessage());
            session.setAttribute("messageType", "error");
        }

        // Redirect back to manage with previously selected class and date
        Integer savedClassId = (Integer) session.getAttribute("selectedClassId");
        String savedDate = (String) session.getAttribute("selectedDate");

        StringBuilder redirectUrl = new StringBuilder("schedule?action=manage");

        if (savedClassId != null && savedClassId > 0) {
            redirectUrl.append("&classId=").append(savedClassId);
        } else {
            redirectUrl.append("&classId=0");
        }

        if (savedDate != null && !savedDate.isEmpty()) {
            redirectUrl.append("&date=").append(savedDate);
        }

        response.sendRedirect(redirectUrl.toString());
    }

    private void handleUpdateSchedule(HttpServletRequest request, HttpServletResponse response,
            ScheduleDAO scheduleDAO, HttpSession session) throws IOException {
        try {
            int scheduleId = Integer.parseInt(request.getParameter("scheduleId"));
            int classId = Integer.parseInt(request.getParameter("classId"));
            int roomId = Integer.parseInt(request.getParameter("roomId"));
            int slotId = Integer.parseInt(request.getParameter("slotId"));
            String learningDateStr = request.getParameter("learningDate");
            Date learningDate = Date.valueOf(learningDateStr);

            // Get existing schedule
            Schedule existingSchedule = scheduleDAO.getScheduleById(scheduleId);
            int teacherId = existingSchedule.getEmployee() != null ?
                          existingSchedule.getEmployee().getEmployeeId() : 0;
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

        response.sendRedirect("schedule?action=manage");
    }

    private void handleDeleteSchedule(HttpServletRequest request, HttpServletResponse response,
            ScheduleDAO scheduleDAO, HttpSession session) throws IOException {
        try {
            int scheduleId = Integer.parseInt(request.getParameter("scheduleId"));

            // Check if attendance has been taken
            Schedule schedule = scheduleDAO.getScheduleById(scheduleId);
            if (schedule != null && schedule.isAttendanceStatus()) {
                session.setAttribute("message", "Cannot delete schedule with attendance already taken!");
                session.setAttribute("messageType", "error");
                response.sendRedirect("schedule?action=manage");
                return;
            }

            // Delete schedule
            boolean success = scheduleDAO.deleteSchedule(scheduleId);

            if (success) {
                session.setAttribute("message", "Schedule deleted successfully!");
                session.setAttribute("messageType", "success");
            } else {
                session.setAttribute("message", "Failed to delete schedule!");
                session.setAttribute("messageType", "error");
            }

        } catch (Exception e) {
            session.setAttribute("message", "Error deleting schedule: " + e.getMessage());
            session.setAttribute("messageType", "error");
        }

        // Redirect back to manage with previously selected class and date
        Integer savedClassId = (Integer) session.getAttribute("selectedClassId");
        String savedDate = (String) session.getAttribute("selectedDate");

        StringBuilder redirectUrl = new StringBuilder("schedule?action=manage");

        if (savedClassId != null && savedClassId > 0) {
            redirectUrl.append("&classId=").append(savedClassId);
        } else {
            redirectUrl.append("&classId=0");
        }

        if (savedDate != null && !savedDate.isEmpty()) {
            redirectUrl.append("&date=").append(savedDate);
        }

        response.sendRedirect(redirectUrl.toString());
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
