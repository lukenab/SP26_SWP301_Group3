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
import model.Room;
import model.Schedule;
import model.Slot;
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

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

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
        String sql = "SELECT s.ScheduleID, s.ClassID, s.RoomID, s.SlotID, s.LearningDate, s.TeacherID, s.AttendanceStatus, "
                + "c.ClassName "
                + "FROM Schedule s "
                + "INNER JOIN Class c ON s.ClassID = c.ClassID "
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

                    // Set class info
                    model.Classes classes = new model.Classes();
                    classes.setClassid(rs.getInt("ClassID"));
                    classes.setClassName(rs.getString("ClassName"));
                    schedule.setClasses(classes);

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

    // Check if there's a schedule conflict (same class, same slot, same date)
    public boolean hasScheduleConflict(int classId, int slotId, Date learningDate, int excludeScheduleId) {
        String sql = "SELECT COUNT(*) as count FROM Schedule "
                + "WHERE ClassID = ? AND SlotID = ? AND LearningDate = ? AND ScheduleID != ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            ps.setInt(2, slotId);
            ps.setDate(3, learningDate);
            ps.setInt(4, excludeScheduleId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0; // Conflict exists if count > 0
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to check schedule conflict: " + e.getMessage());
        }
        return false;
    }

    // Check if teacher exceeds 5 slots per week
    public boolean teacherExceedsWeeklyLimit(int teacherId, Date learningDate, int excludeScheduleId) {
        // Get the start and end of the week containing learningDate
        String sql = "SELECT COUNT(*) as count FROM Schedule "
                + "WHERE TeacherID = ? "
                + "AND LearningDate >= DATEADD(day, -(DATEPART(weekday, ?) - 2), CAST(? AS DATE)) "
                + "AND LearningDate < DATEADD(day, 7 - (DATEPART(weekday, ?) - 2), CAST(? AS DATE)) "
                + "AND ScheduleID != ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, teacherId);
            ps.setDate(2, learningDate);
            ps.setDate(3, learningDate);
            ps.setDate(4, learningDate);
            ps.setDate(5, learningDate);
            ps.setInt(6, excludeScheduleId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int currentSlots = rs.getInt("count");
                    return currentSlots >= 5; // Returns true if teacher already has 5 or more slots
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to check teacher weekly limit: " + e.getMessage());
        }
        return false;
    }

    // Get teacher's slot count for a specific week
    public int getTeacherWeeklySlotCount(int teacherId, Date learningDate, int excludeScheduleId) {
        String sql = "SELECT COUNT(*) as count FROM Schedule "
                + "WHERE TeacherID = ? "
                + "AND LearningDate >= DATEADD(day, -(DATEPART(weekday, ?) - 2), CAST(? AS DATE)) "
                + "AND LearningDate < DATEADD(day, 7 - (DATEPART(weekday, ?) - 2), CAST(? AS DATE)) "
                + "AND ScheduleID != ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, teacherId);
            ps.setDate(2, learningDate);
            ps.setDate(3, learningDate);
            ps.setDate(4, learningDate);
            ps.setDate(5, learningDate);
            ps.setInt(6, excludeScheduleId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count");
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to get teacher weekly slot count: " + e.getMessage());
        }
        return 0;
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

    // Get all available rooms
    public List<Object[]> getAllRooms() {
        List<Object[]> rooms = new ArrayList<>();
        String sql = "SELECT RoomID, RoomName, Capacity, Type, Status FROM Room WHERE Status = 1 ORDER BY RoomName";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Object[] room = new Object[5];
                room[0] = rs.getInt("RoomID");
                room[1] = rs.getString("RoomName");
                room[2] = rs.getInt("Capacity");
                room[3] = rs.getString("Type");
                room[4] = rs.getBoolean("Status");
                rooms.add(room);
            }
        } catch (Exception e) {
            System.out.println("Fail to get all rooms: " + e.getMessage());
        }
        return rooms;
    }

    // Get all teachers
    public List<Object[]> getAllTeachers() {
        List<Object[]> teachers = new ArrayList<>();
        String sql = "SELECT u.UserID, u.FullName, u.Email FROM [User] u WHERE u.RoleID = 4 ORDER BY u.FullName";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Object[] teacher = new Object[3];
                teacher[0] = rs.getInt("UserID");
                teacher[1] = rs.getString("FullName");
                teacher[2] = rs.getString("Email");
                teachers.add(teacher);
            }
        } catch (Exception e) {
            System.out.println("Fail to get all teachers: " + e.getMessage());
        }
        return teachers;
    }

    // Get schedules for management with filters (for academic staff)
    public List<Schedule> getSchedulesForManagement(String selectedDate, Integer classId) {
        List<Schedule> scheduleList = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT s.ScheduleID, s.ClassID, s.RoomID, s.SlotID, s.LearningDate, s.TeacherID, s.AttendanceStatus, ");
        sql.append("c.ClassName, co.CourseName, u.FullName as TeacherName ");
        sql.append("FROM Schedule s ");
        sql.append("INNER JOIN Class c ON s.ClassID = c.ClassID ");
        sql.append("INNER JOIN Course co ON c.CourseID = co.CourseID ");
        sql.append("LEFT JOIN [User] u ON s.TeacherID = u.UserID ");
        sql.append("WHERE 1=1 ");

        // Add date filter for the week
        if (selectedDate != null && !selectedDate.isEmpty()) {
            sql.append("AND s.LearningDate >= DATEADD(day, -(DATEPART(weekday, ?) - 2), CAST(? AS DATE)) ");
            sql.append("AND s.LearningDate < DATEADD(day, 7 - (DATEPART(weekday, ?) - 2), CAST(? AS DATE)) ");
        }

        // Add class filter
        if (classId != null && classId > 0) {
            sql.append("AND s.ClassID = ? ");
        }

        sql.append("ORDER BY s.LearningDate ASC, s.SlotID ASC");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;

            if (selectedDate != null && !selectedDate.isEmpty()) {
                ps.setString(paramIndex++, selectedDate);
                ps.setString(paramIndex++, selectedDate);
                ps.setString(paramIndex++, selectedDate);
                ps.setString(paramIndex++, selectedDate);
            }

            if (classId != null && classId > 0) {
                ps.setInt(paramIndex++, classId);
            }

            try (ResultSet rs = ps.executeQuery()) {
                RoomDAO roomDAO = new RoomDAO();
                SlotDAO slotDAO = new SlotDAO();

                while (rs.next()) {
                    Schedule schedule = new Schedule();
                    schedule.setScheduleId(rs.getInt("ScheduleID"));
                    schedule.setLearningDate(rs.getDate("LearningDate"));
                    schedule.setAttendanceStatus(rs.getBoolean("AttendanceStatus"));

                    // Set room
                    int roomId = rs.getInt("RoomID");
                    Room room = roomDAO.getRoomByID(roomId);
                    schedule.setRoom(room);

                    // Set slot
                    int slotId = rs.getInt("SlotID");
                    Slot slot = slotDAO.getSlotByID(slotId);
                    schedule.setSlot(slot);

                    // Set class with name
                    model.Classes classes = new model.Classes();
                    classes.setClassid(rs.getInt("ClassID"));
                    classes.setClassName(rs.getString("ClassName"));
                    schedule.setClasses(classes);

                    scheduleList.add(schedule);
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to get schedules for management: " + e.getMessage());
            e.printStackTrace();
        }
        return scheduleList;
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
