package dao;

import model.Attendance;
import utils.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.User;

public class AttendanceDAO extends DBContext {

    private Attendance getOrCreateAttendance(int scheduleId, int enrollmentId) {
        String checkSql = "SELECT * FROM Attendance WHERE ScheduleID = ? AND EnrollmentID = ?";
        try {

            PreparedStatement check = conn.prepareStatement(checkSql);
            check.setInt(1, scheduleId);
            check.setInt(2, enrollmentId);
            ResultSet rs = check.executeQuery();

            if (rs.next()) {

                Attendance a = new Attendance();
                a.setAttendanceId(rs.getInt("AttendanceID"));
                a.setStatus(rs.getString("Status"));
                a.setNote(rs.getString("Note"));
                return a;
            }

            String insertSql = "INSERT INTO Attendance (ScheduleID, EnrollmentID, Status, Note) VALUES (?, ?, 'Absent', NULL)";

            PreparedStatement insert = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
            insert.setInt(1, scheduleId);
            insert.setInt(2, enrollmentId);
            insert.executeUpdate();

            ResultSet key = insert.getGeneratedKeys();
            if (key.next()) {
                Attendance a = new Attendance();
                a.setAttendanceId(key.getInt(1));
                a.setStatus("Absent");
                a.setNote(null);
                return a;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateAttendance(int attendanceId, String status, String note) {

        String sql = "UPDATE Attendance SET Status = ?, Note = ? "
                + "WHERE AttendanceID = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {

            st.setString(1, status);
            st.setString(2, note);
            st.setInt(3, attendanceId);

            st.executeUpdate();

        } catch (Exception e) {
            System.out.println("updateAttendance: " + e.getMessage());
        }
    }

    public void updateScheduleStatus(int scheduleId) {

        String sql = "UPDATE Schedule SET AttendanceStatus = 1 WHERE ScheduleID = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, scheduleId);
            st.executeUpdate();
        } catch (Exception e) {
            System.out.println("updateScheduleStatus: " + e.getMessage());
        }
    }

    public Map<String, Object> getAttendanceData(int scheduleId, int classId) {

        Map<String, Object> result = new HashMap<>();

        List<User> studentList = new ArrayList<>();
        Map<Integer, Attendance> attendanceMap = new HashMap<>();

        String sql = "SELECT e.EnrollmentID, u.UserID, u.FullName, u.Avatar "
                + "FROM Enrollment e "
                + "JOIN [User] u ON e.StudentID = u.UserID "
                + "WHERE e.ClassID = ? "
                + "AND e.Status = 'Active' "
                + "AND u.RoleID = 5";

        try (PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, classId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {

                int enrollmentId = rs.getInt("EnrollmentID");
                int userId = rs.getInt("UserID");

                User user = new User();
                user.setUserId(userId);
                user.setFullName(rs.getString("FullName"));
                user.setAvatar(rs.getString("Avatar"));
                studentList.add(user);

                Attendance attendance
                        = getOrCreateAttendance(scheduleId, enrollmentId);

                attendanceMap.put(userId, attendance);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        result.put("studentList", studentList);
        result.put("attendanceMap", attendanceMap);

        return result;
    }

    public List<model.Schedule> getSchedulesByClass(int classId) {
        List<model.Schedule> list = new ArrayList<>();
        String sql = "SELECT * FROM Schedule WHERE ClassID = ? ORDER BY LearningDate, SlotID";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, classId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                model.Schedule s = new model.Schedule();
                s.setScheduleId(rs.getInt("ScheduleID"));
                s.setLearningDate(rs.getDate("LearningDate"));

                model.Slot slot = new model.Slot();
                slot.setSlotID(rs.getInt("SlotID"));
                s.setSlot(slot);

                list.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Map<String, String> getAttendanceReportMap(int classId) {
        Map<String, String> map = new HashMap<>();
        String sql = "SELECT e.StudentID, a.ScheduleID, a.Status "
                + "FROM Attendance a "
                + "JOIN Enrollment e ON a.EnrollmentID = e.EnrollmentID "
                + "WHERE e.ClassID = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, classId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String key = rs.getInt("StudentID") + "_" + rs.getInt("ScheduleID");
                map.put(key, rs.getString("Status"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public List<Object[]> getAttendanceReportByStudent(int studentId) {
        List<Object[]> list = new ArrayList<>();

        String sql = "SELECT "
                + "c.ClassID, "
                + "co.CourseName, "
                + "c.ClassName, "
                + "c.StartDate, "
                + "c.EndDate, "
                + "COUNT(s.ScheduleID) AS TotalSlots, "
                + "SUM(CASE WHEN a.Status IN ('Present','Late') THEN 1 ELSE 0 END) AS AttendedSlots, "
                + "ROUND(100.0 * "
                + "SUM(CASE WHEN a.Status IN ('Present','Late') THEN 1 ELSE 0 END) "
                + "/ COUNT(s.ScheduleID),0) AS AttendanceRate "
                + "FROM Enrollment e "
                + "JOIN Class c ON e.ClassID = c.ClassID "
                + "JOIN Course co ON c.CourseID = co.CourseID "
                + "JOIN Schedule s ON c.ClassID = s.ClassID "
                + "LEFT JOIN Attendance a ON s.ScheduleID = a.ScheduleID "
                + "AND a.EnrollmentID = e.EnrollmentID "
                + "WHERE e.StudentID = ? "
                + "GROUP BY c.ClassID, co.CourseName, c.ClassName, c.StartDate, c.EndDate";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, studentId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Object[] row = new Object[]{
                    rs.getInt("ClassID"),
                    rs.getString("CourseName"),
                    rs.getString("ClassName"),
                    rs.getDate("StartDate"),
                    rs.getDate("EndDate"),
                    rs.getInt("TotalSlots"),
                    rs.getInt("AttendedSlots"),
                    rs.getInt("AttendanceRate")
                };

                list.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public Map<String, Integer> getAttendanceSummaryByStudent(int studentId) {

        Map<String, Integer> map = new HashMap<>();

        String sql = "SELECT "
                + "COUNT(s.ScheduleID) AS TotalClasses, "
                + "ISNULL(SUM(CASE WHEN a.Status='Present' THEN 1 ELSE 0 END),0) AS PresentCount, "
                + "ISNULL(SUM(CASE WHEN a.Status='Absent' THEN 1 ELSE 0 END),0) AS AbsentCount, "
                + "ISNULL(SUM(CASE WHEN a.Status='Late' THEN 1 ELSE 0 END),0) AS LateCount "
                + "FROM Enrollment e "
                + "JOIN Schedule s ON e.ClassID = s.ClassID "
                + "LEFT JOIN Attendance a "
                + "ON a.ScheduleID = s.ScheduleID "
                + "AND a.EnrollmentID = e.EnrollmentID "
                + "WHERE e.StudentID = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, studentId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                map.put("total", rs.getInt("TotalClasses"));
                map.put("present", rs.getInt("PresentCount"));
                map.put("absent", rs.getInt("AbsentCount"));
                map.put("late", rs.getInt("LateCount"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }
}
