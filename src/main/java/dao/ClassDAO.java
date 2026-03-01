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
import utils.DBContext;

/**
 *
 * @author Legion
 */
public class ClassDAO extends DBContext {

    public List<Object[]> getClassManagementList() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT c.ClassID, c.ClassName, co.CourseName, u.FullName AS TeacherName, "
                + "c.StartDate, c.EndDate, c.Status, COUNT(e.EnrollmentID) AS StudentCount "
                + "FROM Class c "
                + "LEFT JOIN Course co ON c.CourseID = co.CourseID "
                + "LEFT JOIN [User] u ON c.TeacherID = u.UserID "
                + "LEFT JOIN Enrollment e ON c.ClassID = e.ClassID "
                + "GROUP BY c.ClassID, c.ClassName, co.CourseName, u.FullName, c.StartDate, c.EndDate, c.Status "
                + "ORDER BY c.StartDate DESC, c.ClassID DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Object[] row = new Object[8];
                row[0] = rs.getInt("ClassID");
                row[1] = rs.getString("ClassName");
                row[2] = rs.getString("CourseName");
                row[3] = rs.getString("TeacherName");
                row[4] = rs.getDate("StartDate");
                row[5] = rs.getDate("EndDate");
                row[6] = rs.getString("Status");
                row[7] = rs.getInt("StudentCount");
                list.add(row);
            }
        } catch (Exception e) {
            System.out.println("Fail to get class management list: " + e.getMessage());
        }
        return list;
    }

    public Object[] getClassById(int classId) {
        String sql = "SELECT c.ClassID, c.ClassName, co.CourseName, u.FullName AS TeacherName, "
                + "c.StartDate, c.EndDate, c.Status, COUNT(e.EnrollmentID) AS StudentCount "
                + "FROM Class c "
                + "LEFT JOIN Course co ON c.CourseID = co.CourseID "
                + "LEFT JOIN [User] u ON c.TeacherID = u.UserID "
                + "LEFT JOIN Enrollment e ON c.ClassID = e.ClassID "
                + "WHERE c.ClassID = ? "
                + "GROUP BY c.ClassID, c.ClassName, co.CourseName, u.FullName, c.StartDate, c.EndDate, c.Status";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Object[] row = new Object[8];
                    row[0] = rs.getInt("ClassID");
                    row[1] = rs.getString("ClassName");
                    row[2] = rs.getString("CourseName");
                    row[3] = rs.getString("TeacherName");
                    row[4] = rs.getDate("StartDate");
                    row[5] = rs.getDate("EndDate");
                    row[6] = rs.getString("Status");
                    row[7] = rs.getInt("StudentCount");
                    return row;
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to get class by id: " + e.getMessage());
        }
        return null;
    }

    public List<Object[]> getActiveCoursesForClassForm() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT CourseID, CourseName FROM Course WHERE Status = 1 ORDER BY CourseName ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Object[] row = new Object[2];
                row[0] = rs.getInt("CourseID");
                row[1] = rs.getString("CourseName");
                list.add(row);
            }
        } catch (Exception e) {
            System.out.println("Fail to get active courses for class form: " + e.getMessage());
        }
        return list;
    }

    public List<Object[]> getTeacherOptions() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT u.UserID, u.FullName, u.Email "
                + "FROM [User] u "
                + "WHERE u.RoleID = 4 "
                + "ORDER BY u.FullName ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Object[] row = new Object[3];
                row[0] = rs.getInt("UserID");
                row[1] = rs.getString("FullName");
                row[2] = rs.getString("Email");
                list.add(row);
            }
        } catch (Exception e) {
            System.out.println("Fail to get teacher options: " + e.getMessage());
        }
        return list;
    }

    public boolean createClass(String className, int courseId, int teacherId, Date startDate, Date endDate, String status) {
        String sql = "INSERT INTO Class (ClassName, CourseID, TeacherID, StartDate, EndDate, Status) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, className);
            ps.setInt(2, courseId);
            ps.setInt(3, teacherId);
            ps.setDate(4, startDate);
            ps.setDate(5, endDate);
            ps.setString(6, status);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Fail to create class: " + e.getMessage());
        }
        return false;
    }

    public String getClassNameById(int classId) {

        String sql = "SELECT ClassName FROM Class WHERE ClassID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, classId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("ClassName");
            }

        } catch (Exception e) {
            System.out.println("Fail to get class name: " + e.getMessage());
        }

        return null;
    }

    public int getTeacherIdByClassId(int classId) {
        String sql = "SELECT TeacherID FROM Class WHERE ClassID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("TeacherID");
            }
        } catch (Exception e) {
            System.out.println("Fail to get teacher id by class id: " + e.getMessage());
        }
        return 0;
    }
}
