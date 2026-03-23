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
        // STEP 0: Check if learning date is within class start and end dates
        // If not, silently skip (don't create the schedule)
        if (!isLearningDateWithinClassRange(classId, learningDate)) {
            System.out.println("Info: Learning date " + learningDate + " is outside class date range, skipping...");
            return false;
        }
        
        // STEP 1: Check if exact duplicate exists (all fields except ScheduleID)
        if (isDuplicateSchedule(classId, roomId, slotId, learningDate, teacherId, -1)) {
            System.out.println("Error: Duplicate schedule already exists!");
            return false;
        }
        
        // STEP 2: Check if class already has schedule in same slot on same date
        // This prevents 1 class from having multiple schedules in same slot on same day
        if (hasClassConflictInSlot(classId, slotId, learningDate, -1)) {
            System.out.println("Error: Class already has a schedule for this slot on this date!");
            return false;
        }
        
        // STEP 3: Check if room already has schedule in same slot on same date
        // This prevents 1 room from having multiple schedules in same slot on same day
        if (hasRoomConflictInSlot(roomId, slotId, learningDate, -1)) {
            System.out.println("Error: Room already has a schedule for this slot on this date!");
            return false;
        }
        
        // STEP 4: Check if teacher already has schedule in same slot on same date
        // This prevents 1 teacher from teaching multiple classes in same slot on same day
        if (hasTeacherConflictInSlot(teacherId, slotId, learningDate, -1)) {
            System.out.println("Error: Teacher already has a schedule for this slot on this date!");
            return false;
        }
        
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
        // STEP 1: Check if exact duplicate exists (exclude current schedule)
        if (isDuplicateSchedule(classId, roomId, slotId, learningDate, teacherId, scheduleId)) {
            System.out.println("Error: Duplicate schedule already exists!");
            return false;
        }
        
        // STEP 2: Check if class already has schedule in same slot on same date (exclude current)
        if (hasClassConflictInSlot(classId, slotId, learningDate, scheduleId)) {
            System.out.println("Error: Class already has a schedule for this slot on this date!");
            return false;
        }
        
        // STEP 3: Check if room already has schedule in same slot on same date (exclude current)
        if (hasRoomConflictInSlot(roomId, slotId, learningDate, scheduleId)) {
            System.out.println("Error: Room already has a schedule for this slot on this date!");
            return false;
        }
        
        // STEP 4: Check if teacher already has schedule in same slot on same date (exclude current)
        if (hasTeacherConflictInSlot(teacherId, slotId, learningDate, scheduleId)) {
            System.out.println("Error: Teacher already has a schedule for this slot on this date!");
            return false;
        }
        
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

    // Create multiple schedules at once (batch operation) - NO DATABASE CHANGES NEEDED!
    public int createMultipleSchedules(int classId, int roomId, int slotId, Date startDate, int teacherId,
            String recurringType, String recurringDays, String endCondition,
            Date endDate, Integer occurrences) {
        try {
            List<Date> scheduleDates = generateScheduleDates(startDate, recurringType, recurringDays,
                    endCondition, endDate, occurrences);

            if (scheduleDates.isEmpty()) {
                System.out.println("No dates generated for recurring schedule");
                return 0;
            }

            String sql = "INSERT INTO Schedule (ClassID, RoomID, SlotID, LearningDate, TeacherID, AttendanceStatus) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";

            conn.setAutoCommit(false);
            int createdCount = 0;

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (Date date : scheduleDates) {
                    // STEP 0: Check if learning date is within class start and end dates
                    if (!isLearningDateWithinClassRange(classId, date)) {
                        System.out.println("Info: Learning date " + date + " is outside class date range, skipping...");
                        continue; // Skip this date
                    }
                    
                    // STEP 1: Check if exact duplicate exists
                    if (isDuplicateSchedule(classId, roomId, slotId, date, teacherId, -1)) {
                        System.out.println("Warning: Duplicate schedule exists for slot " + slotId + " on " + date + ", skipping...");
                        continue; // Skip this date
                    }
                    
                    // STEP 2: Check if class already has schedule in same slot on same date
                    if (hasClassConflictInSlot(classId, slotId, date, -1)) {
                        System.out.println("Warning: Class already has schedule for slot " + slotId + " on " + date + ", skipping...");
                        continue; // Skip this date
                    }
                    
                    // STEP 3: Check if room already has schedule in same slot on same date
                    if (hasRoomConflictInSlot(roomId, slotId, date, -1)) {
                        System.out.println("Warning: Room already has schedule for slot " + slotId + " on " + date + ", skipping...");
                        continue; // Skip this date
                    }
                    
                    // STEP 4: Check if teacher already has schedule in same slot on same date
                    if (hasTeacherConflictInSlot(teacherId, slotId, date, -1)) {
                        System.out.println("Warning: Teacher already has schedule for slot " + slotId + " on " + date + ", skipping...");
                        continue; // Skip this date
                    }
                    
                    ps.setInt(1, classId);
                    ps.setInt(2, roomId);
                    ps.setInt(3, slotId);
                    ps.setDate(4, date);
                    ps.setInt(5, teacherId);
                    ps.setBoolean(6, false);
                    ps.addBatch();
                }

                int[] results = ps.executeBatch();
                conn.commit();

                // Count successful inserts
                for (int result : results) {
                    if (result > 0) {
                        createdCount++;
                    }
                }

                return createdCount;
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            System.out.println("Fail to create multiple schedules: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    // Generate list of dates based on recurring pattern
    private List<Date> generateScheduleDates(Date startDate, String recurringType, String recurringDays,
            String endCondition, Date endDate, Integer occurrences) {
        List<Date> dates = new ArrayList<>();
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(startDate);

        // Save the day of week from the start date for "weekly" pattern
        int startDayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);

        int iteration = 0;
        int maxIterations = 730; // Safety limit (2 years of daily)

        while (iteration < maxIterations) {
            Date currentDate = new Date(cal.getTimeInMillis());

            // Check end condition BEFORE checking pattern match
            if ("on".equals(endCondition) && endDate != null && currentDate.after(endDate)) {
                break;
            }

            // Check if current date matches the recurring pattern
            boolean shouldAdd = false;
            switch (recurringType) {
                case "daily":
                    shouldAdd = true;
                    break;
                case "weekly":
                    // Use the day of week from the start date, not today
                    shouldAdd = (cal.get(java.util.Calendar.DAY_OF_WEEK) == startDayOfWeek);
                    break;
                case "weekdays":
                    int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);
                    shouldAdd = (dayOfWeek >= java.util.Calendar.MONDAY
                            && dayOfWeek <= java.util.Calendar.FRIDAY);
                    break;
                case "custom":
                    if (recurringDays != null && !recurringDays.isEmpty()) {
                        int currentDay = cal.get(java.util.Calendar.DAY_OF_WEEK);
                        // Convert to Monday=1 format
                        int day = currentDay == 1 ? 7 : currentDay - 1;
                        shouldAdd = recurringDays.contains(String.valueOf(day));
                    }
                    break;
                default:
                    shouldAdd = (dates.isEmpty()); // Single schedule
                    break;
            }

            if (shouldAdd) {
                dates.add(currentDate);
                // Check "after" condition AFTER adding
                if ("after".equals(endCondition) && occurrences != null && dates.size() >= occurrences) {
                    break;
                }
            }

            // Move to next day
            cal.add(java.util.Calendar.DAY_OF_MONTH, 1);
            iteration++;

            // Safety check for "never" condition
            if ("never".equals(endCondition) && dates.size() >= 100) {
                break;
            }
        }

        return dates;
    }

    // Find related schedules (same class, slot, room) - for series identification
    public List<Schedule> findRelatedSchedules(int scheduleId) {
        List<Schedule> schedules = new ArrayList<>();
        String sql = "SELECT s2.ScheduleID, s2.ClassID, s2.RoomID, s2.SlotID, s2.LearningDate, "
                + "s2.TeacherID, s2.AttendanceStatus "
                + "FROM Schedule s1 "
                + "INNER JOIN Schedule s2 ON s1.ClassID = s2.ClassID "
                + "    AND s1.RoomID = s2.RoomID "
                + "    AND s1.SlotID = s2.SlotID "
                + "    AND s1.TeacherID = s2.TeacherID "
                + "WHERE s1.ScheduleID = ? "
                + "ORDER BY s2.LearningDate";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, scheduleId);

            try (ResultSet rs = ps.executeQuery()) {
                RoomDAO roomDAO = new RoomDAO();
                SlotDAO slotDAO = new SlotDAO();

                while (rs.next()) {
                    Schedule schedule = new Schedule();
                    schedule.setScheduleId(rs.getInt("ScheduleID"));
                    schedule.setLearningDate(rs.getDate("LearningDate"));
                    schedule.setAttendanceStatus(rs.getBoolean("AttendanceStatus"));

                    int scheduleRoomId = rs.getInt("RoomID");
                    Room room = roomDAO.getRoomByID(scheduleRoomId);
                    schedule.setRoom(room);

                    int slotId = rs.getInt("SlotID");
                    Slot slot = slotDAO.getSlotByID(slotId);
                    schedule.setSlot(slot);

                    schedules.add(schedule);
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to find related schedules: " + e.getMessage());
        }
        return schedules;
    }

    // Delete multiple schedules by same pattern (class, slot, room, teacher)
    public int deleteSchedulesByPattern(int classId, int roomId, int slotId, int teacherId) {
        String sql = "DELETE FROM Schedule WHERE ClassID = ? AND RoomID = ? AND SlotID = ? AND TeacherID = ? AND AttendanceStatus = 0";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            ps.setInt(2, roomId);
            ps.setInt(3, slotId);
            ps.setInt(4, teacherId);
            return ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Fail to delete schedules by pattern: " + e.getMessage());
        }
        return 0;
    }

    // Update multiple schedules by pattern
    public int updateSchedulesByPattern(int oldClassId, int oldRoomId, int oldSlotId, int oldTeacherId,
            int newClassId, int newRoomId, int newSlotId, int newTeacherId) {
        String sql = "UPDATE Schedule SET ClassID = ?, RoomID = ?, SlotID = ?, TeacherID = ? "
                + "WHERE ClassID = ? AND RoomID = ? AND SlotID = ? AND TeacherID = ? AND AttendanceStatus = 0";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newClassId);
            ps.setInt(2, newRoomId);
            ps.setInt(3, newSlotId);
            ps.setInt(4, newTeacherId);
            ps.setInt(5, oldClassId);
            ps.setInt(6, oldRoomId);
            ps.setInt(7, oldSlotId);
            ps.setInt(8, oldTeacherId);
            return ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Fail to update schedules by pattern: " + e.getMessage());
        }
        return 0;
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

    // Check if class already has a schedule for the same slot on the same date (regardless of room)
    // This prevents a class from being in multiple rooms at the same time
    public boolean hasClassSlotConflict(int classId, int slotId, Date learningDate, int excludeScheduleId) {
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
            System.out.println("Fail to check class-slot conflict: " + e.getMessage());
        }
        return false;
    }

    // STEP 1: Check if schedule with exact same info already exists (duplicate)
    // Check: ClassID, RoomID, SlotID, LearningDate, TeacherID all match (except ScheduleID)
    public boolean isDuplicateSchedule(int classId, int roomId, int slotId, Date learningDate, int teacherId, int excludeScheduleId) {
        String sql = "SELECT COUNT(*) as count FROM Schedule "
                + "WHERE ClassID = ? AND RoomID = ? AND SlotID = ? AND LearningDate = ? AND TeacherID = ? AND ScheduleID != ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            ps.setInt(2, roomId);
            ps.setInt(3, slotId);
            ps.setDate(4, learningDate);
            ps.setInt(5, teacherId);
            ps.setInt(6, excludeScheduleId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0; // Duplicate found if count > 0
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to check duplicate schedule: " + e.getMessage());
        }
        return false;
    }

    // STEP 2: Check if class already has schedule in same slot on same date (conflict)
    // This prevents 1 class from having 2 schedules in same slot on same day
    // Check only: ClassID, SlotID, LearningDate (ignore RoomID, TeacherID)
    public boolean hasClassConflictInSlot(int classId, int slotId, Date learningDate, int excludeScheduleId) {
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
            System.out.println("Fail to check class conflict in slot: " + e.getMessage());
        }
        return false;
    }

    // STEP 3: Check if room already has schedule in same slot on same date (conflict)
    // This prevents 1 room from having 2 schedules in same slot on same day
    // Check only: RoomID, SlotID, LearningDate (ignore ClassID, TeacherID)
    public boolean hasRoomConflictInSlot(int roomId, int slotId, Date learningDate, int excludeScheduleId) {
        String sql = "SELECT COUNT(*) as count FROM Schedule "
                + "WHERE RoomID = ? AND SlotID = ? AND LearningDate = ? AND ScheduleID != ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            ps.setInt(2, slotId);
            ps.setDate(3, learningDate);
            ps.setInt(4, excludeScheduleId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0; // Conflict exists if count > 0
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to check room conflict in slot: " + e.getMessage());
        }
        return false;
    }

    // STEP 4: Check if teacher already has schedule in same slot on same date (conflict)
    // This prevents 1 teacher from teaching 2 classes in same slot on same day
    // Check only: TeacherID, SlotID, LearningDate (ignore ClassID, RoomID)
    public boolean hasTeacherConflictInSlot(int teacherId, int slotId, Date learningDate, int excludeScheduleId) {
        String sql = "SELECT COUNT(*) as count FROM Schedule "
                + "WHERE TeacherID = ? AND SlotID = ? AND LearningDate = ? AND ScheduleID != ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, teacherId);
            ps.setInt(2, slotId);
            ps.setDate(3, learningDate);
            ps.setInt(4, excludeScheduleId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0; // Conflict exists if count > 0
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to check teacher conflict in slot: " + e.getMessage());
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

                    int scheduleRoomId = rs.getInt("RoomID");
                    Room room = roomDAO.getRoomByID(scheduleRoomId);
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
    public List<Schedule> getSchedulesForManagement(String selectedDate, Integer classId, Integer roomId) {
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

        // Add room filter
        if (roomId != null && roomId > 0) {
            sql.append("AND s.RoomID = ? ");
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

            if (roomId != null && roomId > 0) {
                ps.setInt(paramIndex++, roomId);
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
                    int scheduleRoomId = rs.getInt("RoomID");
                    Room room = roomDAO.getRoomByID(scheduleRoomId);
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
                + "AND e.Status = 'Active' "
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

    /**
     * Find similar schedules based on Class, Slot, and Room Used for bulk
     * delete/edit operations
     */
    public List<Schedule> getSimilarSchedules(int scheduleId) {
        List<Schedule> similarSchedules = new ArrayList<>();

        // First get the reference schedule
        Schedule refSchedule = getScheduleById(scheduleId);
        if (refSchedule == null) {
            return similarSchedules;
        }

        String sql = "SELECT s.ScheduleID, s.ClassID, s.RoomID, s.SlotID, s.LearningDate, s.TeacherID, s.AttendanceStatus, "
                + "c.ClassName "
                + "FROM Schedule s "
                + "INNER JOIN Class c ON s.ClassID = c.ClassID "
                + "WHERE s.ClassID = ? AND s.SlotID = ? AND s.RoomID = ? "
                + "ORDER BY s.LearningDate ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, refSchedule.getClasses().getClassid());
            ps.setInt(2, refSchedule.getSlot().getSlotID());
            ps.setInt(3, refSchedule.getRoom().getRoomId());

            try (ResultSet rs = ps.executeQuery()) {
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

                    // Set class info
                    Classes classes = new Classes();
                    classes.setClassid(rs.getInt("ClassID"));
                    classes.setClassName(rs.getString("ClassName"));
                    schedule.setClasses(classes);

                    // Get employee info
                    int teacherId = rs.getInt("TeacherID");
                    if (teacherId > 0) {
                        EmployeeDAO empDAO = new EmployeeDAO();
                        Employee teacher = empDAO.getEmployeeById(teacherId);
                        schedule.setEmployee(teacher);
                    }

                    similarSchedules.add(schedule);
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to get similar schedules: " + e.getMessage());
            e.printStackTrace();
        }

        return similarSchedules;
    }

    /**
     * Delete similar schedules (only non-attended ones) Returns number of
     * deleted schedules
     */
    public int deleteSimilarSchedules(int scheduleId) {
        List<Schedule> similarSchedules = getSimilarSchedules(scheduleId);
        int deletedCount = 0;

        String sql = "DELETE FROM Schedule WHERE ScheduleID = ? AND AttendanceStatus = 0";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Schedule schedule : similarSchedules) {
                // Only delete non-attended schedules
                if (!schedule.isAttendanceStatus()) {
                    ps.setInt(1, schedule.getScheduleId());
                    if (ps.executeUpdate() > 0) {
                        deletedCount++;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to delete similar schedules: " + e.getMessage());
            e.printStackTrace();
        }

        return deletedCount;
    }

    public List<Schedule> getTodayScheduleByStudent(int studentId, String today) {

        List<model.Schedule> list = new ArrayList<>();

        String sql = "SELECT s.*, c.ClassName, r.RoomName, sl.StartTime, sl.EndTime "
                + "FROM Schedule s "
                + "JOIN [Class] c ON s.ClassID = c.ClassID "
                + "JOIN Enrollment e ON e.ClassID = c.ClassID "
                + "LEFT JOIN Room r ON s.RoomID = r.RoomID "
                + "LEFT JOIN Slot sl ON s.SlotID = sl.SlotID "
                + "WHERE e.StudentID = ? "
                + "AND s.LearningDate = ? "
                + "AND e.Status = 'Active'";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setString(2, today);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                model.Schedule sc = new model.Schedule();

                model.Classes cl = new model.Classes();
                cl.setClassid(rs.getInt("ClassID"));
                cl.setClassName(rs.getString("ClassName"));

                model.Room room = new model.Room();
                room.setRoomName(rs.getString("RoomName"));

                model.Slot slot = new model.Slot();

                slot.setStartTime(rs.getTime("StartTime").toLocalTime());
                slot.setEndTime(rs.getTime("EndTime").toLocalTime());

                sc.setClasses(cl);
                sc.setRoom(room);
                sc.setSlot(slot);
                sc.setLearningDate(rs.getDate("LearningDate"));

                list.add(sc);
            }

        } catch (Exception e) {
            System.out.println("Fail get today schedule: " + e.getMessage());
        }

        return list;
    }

    // Helper method: Check if learning date is within class start and end dates
    private boolean isLearningDateWithinClassRange(int classId, Date learningDate) {
        String sql = "SELECT StartDate, EndDate FROM Class WHERE ClassID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Date startDate = rs.getDate("StartDate");
                    Date endDate = rs.getDate("EndDate");

                    // Nếu class không có start/end date thì allow (không có restriction)
                    if (startDate == null || endDate == null) {
                        return true;
                    }

                    // Check: learningDate >= startDate AND learningDate <= endDate
                    // compareTo() trả về: -1 (sớm), 0 (bằng), 1 (muộn)
                    boolean isOnOrAfterStart = learningDate.compareTo(startDate) >= 0;
                    boolean isOnOrBeforeEnd = learningDate.compareTo(endDate) <= 0;
                    
                    return isOnOrAfterStart && isOnOrBeforeEnd;
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to check class date range: " + e.getMessage());
        }
        // Nếu class không tồn tại hoặc lỗi thì allow (tránh chặn)
        return true;
    }
}
