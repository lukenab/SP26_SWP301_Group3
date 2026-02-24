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
}
