/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.DBContext;

/**
 *
 * @author Legion
 */
public class AttendanceDAO extends DBContext {

    public List<Object[]> getAttendanceList(int scheduleId) {
        List<Object[]> list = new ArrayList<>();
        // Sửa enrolmentId -> EnrollmentID
        String sql = "SELECT a.attendanceId, u.FullName, u.UserID, a.status, a.note "
                + "FROM Attendance a "
                + "JOIN Enrollment e ON a.EnrollmentID = e.EnrollmentID "
                + "JOIN [User] u ON e.StudentID = u.UserID "
                + "WHERE a.ScheduleID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, scheduleId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] row = new Object[5];
                row[0] = rs.getInt("attendanceId");
                row[1] = rs.getString("FullName");
                row[2] = rs.getInt("UserID");
                row[3] = rs.getString("status");
                row[4] = rs.getString("note");
                list.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm hàm này vào AttendanceDAO.java
    public void updateAttendance(int attendanceId, String status, String note) {
        String sql = "UPDATE Attendance SET status = ?, note = ? WHERE attendanceId = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, status); // "Present" hoặc "Absent"
            ps.setString(2, note);
            ps.setInt(3, attendanceId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        AttendanceDAO dao = new AttendanceDAO();

        // Luân thay số 1 bằng một ScheduleID có thật trong bảng Attendance của bạn nhé
        int testScheduleId = 4;
        List<Object[]> list = dao.getAttendanceList(testScheduleId);

        System.out.println("--- ATTENDANCE LIST FOR SCHEDULE ID: " + testScheduleId + " ---");

        if (list.isEmpty()) {
            System.out.println("No attendance records found for this schedule.");
            System.out.println("Check if you have inserted attendance records into the Database!");
        } else {
            // Duyệt mảng Object[] theo index bạn đã định nghĩa
            for (Object[] row : list) {
                System.out.println(
                        "AttID: " + row[0]
                        + " | Student: " + row[1]
                        + " (ID: " + row[2] + ")"
                        + " | Status: " + row[3]
                        + " | Note: " + (row[4] != null ? row[4] : "None")
                );
            }
            System.out.println("--- TOTAL: " + list.size() + " students ---");
        }
    }
}
