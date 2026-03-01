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

        String sql = "SELECT e.EnrollmentID, u.UserID, u.FullName "
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

                // tạo User object
                User user = new User();
                user.setUserId(userId);
                user.setFullName(rs.getString("FullName"));

                studentList.add(user);

                // lấy attendance
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

}
