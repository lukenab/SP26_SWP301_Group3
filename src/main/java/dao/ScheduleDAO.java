/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Classes;
import model.Course;
import model.Employee;
import model.Room;
import model.Schedule;
import model.Slot;
import model.User;
import utils.DBContext;

/**
 *
 * @author Legion
 */
public class ScheduleDAO extends DBContext {

    // Get all schedules
    public List<Schedule> getAll() {
        List<Schedule> scheduleList = new ArrayList<>();
        String sql = "SELECT s.ScheduleID, s.ClassID, s.RoomID, s.SlotID, s.LearningDate, s.TeacherID, s.AttendanceStatus "
                + "FROM Schedule s "
                + "ORDER BY s.LearningDate DESC, s.SlotID ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            RoomDAO roomDAO = new RoomDAO();
            SlotDAO slotDAO = new SlotDAO();

            while (rs.next()) {
                Schedule schedule = new Schedule();
                schedule.setScheduleId(rs.getInt("ScheduleID"));
                schedule.setLearningDate(rs.getDate("LearningDate"));
                schedule.setAttendanceStatus(rs.getBoolean("AttendanceStatus"));

                // Get related room object
                int roomId = rs.getInt("RoomID");
                Room room = roomDAO.getRoomByID(roomId);
                schedule.setRoom(room);

                // Get related slot object
                int slotId = rs.getInt("SlotID");
                Slot slot = slotDAO.getSlotByID(slotId);
                schedule.setSlot(slot);

                scheduleList.add(schedule);
            }
        } catch (Exception e) {
            System.out.println("Fail to get all schedules: " + e.getMessage());
        }
        return scheduleList;
    }

    // Get schedule by ID
    public Schedule getScheduleById(int scheduleId) {
        String sql = "SELECT s.ScheduleID, s.ClassID, s.RoomID, s.SlotID, s.LearningDate, s.TeacherID, s.AttendanceStatus "
                + "FROM Schedule s "
                + "WHERE s.ScheduleID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, scheduleId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Schedule schedule = new Schedule();
                    schedule.setScheduleId(rs.getInt("ScheduleID"));
                    schedule.setLearningDate(rs.getDate("LearningDate"));
                    schedule.setAttendanceStatus(rs.getBoolean("AttendanceStatus"));

                    // Get related room object
                    int roomId = rs.getInt("RoomID");
                    RoomDAO roomDAO = new RoomDAO();
                    Room room = roomDAO.getRoomByID(roomId);
                    schedule.setRoom(room);

                    // Get related slot object
                    int slotId = rs.getInt("SlotID");
                    SlotDAO slotDAO = new SlotDAO();
                    Slot slot = slotDAO.getSlotByID(slotId);
                    schedule.setSlot(slot);

                    return schedule;
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to get schedule by ID: " + e.getMessage());
        }
        return null;
    }

    // Create new schedule
    public boolean createSchedule(int classId, int roomId, int slotId, Date learningDate, int teacherId, boolean attendanceStatus) {
        String sql = "INSERT INTO Schedule (ClassID, RoomID, SlotID, LearningDate, TeacherID, AttendanceStatus) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            ps.setInt(2, roomId);
            ps.setInt(3, slotId);
            ps.setDate(4, learningDate);
            ps.setInt(5, teacherId);
            ps.setBoolean(6, attendanceStatus);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Fail to create schedule: " + e.getMessage());
        }
        return false;
    }

    // Edit/Update schedule
    public boolean editSchedule(int scheduleId, int classId, int roomId, int slotId, Date learningDate, int teacherId, boolean attendanceStatus) {
        String sql = "UPDATE Schedule SET ClassID = ?, RoomID = ?, SlotID = ?, LearningDate = ?, TeacherID = ?, AttendanceStatus = ? "
                + "WHERE ScheduleID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            ps.setInt(2, roomId);
            ps.setInt(3, slotId);
            ps.setDate(4, learningDate);
            ps.setInt(5, teacherId);
            ps.setBoolean(6, attendanceStatus);
            ps.setInt(7, scheduleId);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Fail to edit schedule: " + e.getMessage());
        }
        return false;
    }

    // Delete schedule
    public boolean deleteSchedule(int scheduleId) {
        String sql = "DELETE FROM Schedule WHERE ScheduleID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, scheduleId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Fail to delete schedule: " + e.getMessage());
        }
        return false;
    }

    // Check if room is available for a specific slot and date
    public boolean isRoomAvailable(int roomId, int slotId, Date learningDate, int excludeScheduleId) {
        String sql = "SELECT COUNT(*) as count FROM Schedule "
                + "WHERE RoomID = ? AND SlotID = ? AND LearningDate = ? AND ScheduleID != ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            ps.setInt(2, slotId);
            ps.setDate(3, learningDate);
            ps.setInt(4, excludeScheduleId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") == 0; // Room is available if count is 0
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to check room availability: " + e.getMessage());
        }
        return false;
    }

    // Check if teacher is available for a specific slot and date
    public boolean isTeacherAvailable(int teacherId, int slotId, Date learningDate, int excludeScheduleId) {
        String sql = "SELECT COUNT(*) as count FROM Schedule "
                + "WHERE TeacherID = ? AND SlotID = ? AND LearningDate = ? AND ScheduleID != ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, teacherId);
            ps.setInt(2, slotId);
            ps.setDate(3, learningDate);
            ps.setInt(4, excludeScheduleId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") == 0; // Teacher is available if count is 0
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to check teacher availability: " + e.getMessage());
        }
        return false;
    }

    // Get schedules by date
    public List<Schedule> getSchedulesByDate(Date learningDate) {
        List<Schedule> scheduleList = new ArrayList<>();
        String sql = "SELECT s.ScheduleID, s.ClassID, s.RoomID, s.SlotID, s.LearningDate, s.TeacherID, s.AttendanceStatus "
                + "FROM Schedule s "
                + "WHERE s.LearningDate = ? "
                + "ORDER BY s.SlotID ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, learningDate);

            try (ResultSet rs = ps.executeQuery()) {
                RoomDAO roomDAO = new RoomDAO();
                SlotDAO slotDAO = new SlotDAO();

                while (rs.next()) {
                    Schedule schedule = new Schedule();
                    schedule.setScheduleId(rs.getInt("ScheduleID"));
                    schedule.setLearningDate(rs.getDate("LearningDate"));
                    schedule.setAttendanceStatus(rs.getBoolean("AttendanceStatus"));

                    int roomId = rs.getInt("RoomID");
                    Room room = roomDAO.getRoomByID(roomId);
                    schedule.setRoom(room);

                    int slotId = rs.getInt("SlotID");
                    Slot slot = slotDAO.getSlotByID(slotId);
                    schedule.setSlot(slot);

                    scheduleList.add(schedule);
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to get schedules by date: " + e.getMessage());
        }
        return scheduleList;
    }

    // Get schedules by class
    public List<Schedule> getSchedulesByClass(int classId) {
        List<Schedule> scheduleList = new ArrayList<>();
        String sql = "SELECT s.ScheduleID, s.ClassID, s.RoomID, s.SlotID, s.LearningDate, s.TeacherID, s.AttendanceStatus "
                + "FROM Schedule s "
                + "WHERE s.ClassID = ? "
                + "ORDER BY s.LearningDate ASC, s.SlotID ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);

            try (ResultSet rs = ps.executeQuery()) {
                RoomDAO roomDAO = new RoomDAO();
                SlotDAO slotDAO = new SlotDAO();

                while (rs.next()) {
                    Schedule schedule = new Schedule();
                    schedule.setScheduleId(rs.getInt("ScheduleID"));
                    schedule.setLearningDate(rs.getDate("LearningDate"));
                    schedule.setAttendanceStatus(rs.getBoolean("AttendanceStatus"));

                    int roomId = rs.getInt("RoomID");
                    Room room = roomDAO.getRoomByID(roomId);
                    schedule.setRoom(room);

                    int slotId = rs.getInt("SlotID");
                    Slot slot = slotDAO.getSlotByID(slotId);
                    schedule.setSlot(slot);

                    scheduleList.add(schedule);
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to get schedules by class: " + e.getMessage());
        }
        return scheduleList;
    }

    public List<Schedule> getScheduleByStudentWeek(int userId,
            String startDate,
            String endDate) {

        List<Schedule> list = new ArrayList<>();

        String sql = "SELECT "
                + "s.ScheduleID, s.LearningDate, "
                + "sl.SlotID, sl.StartTime, sl.EndTime, "
                + "c.ClassID, c.ClassName, "
                + "cr.CourseID, cr.CourseName, "
                + "r.RoomID, r.RoomName, "
                + "u.UserID AS TeacherID, "
                + "emp.Education, emp.Experience, "
                + "a.Status AS AttendanceStatus "
                + "FROM Schedule s "
                + "JOIN Enrollment e ON e.ClassID = s.ClassID "
                + "AND e.StudentID = ? "
                + "AND e.Status = 1 "
                + "JOIN Class c ON s.ClassID = c.ClassID "
                + "JOIN Course cr ON c.CourseID = cr.CourseID "
                + "JOIN Room r ON s.RoomID = r.RoomID "
                + "JOIN Slot sl ON s.SlotID = sl.SlotID "
                + "JOIN [User] u ON s.TeacherID = u.UserID AND u.RoleID = 4 "
                + "LEFT JOIN Employee emp ON u.UserID = emp.EmployeeID "
                + "LEFT JOIN Attendance a ON a.ScheduleID = s.ScheduleID "
                + "AND a.EnrollmentID = e.EnrollmentID "
                + "WHERE s.LearningDate BETWEEN ? AND ? "
                + "ORDER BY s.LearningDate, sl.SlotID";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setString(2, startDate);
            ps.setString(3, endDate);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                // ===== Course =====
                Course course = new Course();
                course.setCourseId(rs.getInt("CourseID"));
                course.setCourseName(rs.getString("CourseName"));

                // ===== Class =====
                Classes classes = new Classes();
                classes.setClassid(rs.getInt("ClassID"));
                classes.setClassName(rs.getString("ClassName"));
                classes.setCourse(course);

                // ===== Room =====
                Room room = new Room();
                room.setRoomId(rs.getInt("RoomID"));
                room.setRoomName(rs.getString("RoomName"));

                // ===== Employee (Teacher info theo model hiện tại) =====
                Employee emp = new Employee();
                emp.setEmployeeId(rs.getInt("TeacherID"));
                emp.setEducation(rs.getString("Education"));
                emp.setExperience(rs.getString("Experience"));

                // ===== Slot (LocalTime đúng kiểu) =====
                Slot slot = new Slot();
                slot.setSlotID(rs.getInt("SlotID"));
                slot.setStartTime(rs.getTime("StartTime").toLocalTime());
                slot.setEndTime(rs.getTime("EndTime").toLocalTime());

                // ===== Schedule =====
                Schedule s = new Schedule();
                s.setScheduleId(rs.getInt("ScheduleID"));
                s.setLearningDate(rs.getDate("LearningDate"));
                s.setSlot(slot);
                s.setClasses(classes);
                s.setRoom(room);
                s.setEmployee(emp);

                // ===== Attendance (boolean theo model Schedule) =====
                String status = rs.getString("AttendanceStatus");

                boolean attendance = false;
                if (status != null && status.equalsIgnoreCase("Present")) {
                    attendance = true;
                }

                s.setAttendanceStatus(attendance);

                list.add(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Main method for testing
    public static void main(String[] args) {
        ScheduleDAO scheduleDAO = new ScheduleDAO();

        System.out.println("=== Testing getAll() Method ===");
        List<Schedule> allSchedules = scheduleDAO.getAll();

        if (allSchedules != null && !allSchedules.isEmpty()) {
            System.out.println("Total schedules found: " + allSchedules.size());
            System.out.println("\nSchedule Details:");
            System.out.println("---------------------------------------------------");

            for (Schedule schedule : allSchedules) {
                System.out.println("Schedule ID: " + schedule.getScheduleId());

                if (schedule.getSlot() != null) {
                    System.out.println("  Slot: " + schedule.getSlot().getSlotID()
                            + " (" + schedule.getSlot().getStartTime()
                            + " - " + schedule.getSlot().getEndTime() + ")");
                } else {
                    System.out.println("  Slot: N/A");
                }

                System.out.println("  Learning Date: " + schedule.getLearningDate());
                System.out.println("  Attendance Status: " + (schedule.isAttendanceStatus() ? "Taken" : "Not Taken"));

                if (schedule.getRoom() != null) {
                    System.out.println("  Room: " + schedule.getRoom().getRoomName()
                            + " (Capacity: " + schedule.getRoom().getCapacity() + ")");
                } else {
                    System.out.println("  Room: N/A");
                }

                System.out.println("---------------------------------------------------");
            }
        } else {
            System.out.println("No schedules found or error occurred.");
        }

        System.out.println("\n=== Test Completed ===");
    }
}
