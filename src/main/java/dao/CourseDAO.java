/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.sql.SQLException;
import model.Course;
import utils.DBContext;

/**
 *
 * @author Legion
 */
public class CourseDAO extends DBContext {

    public List<Course> getAllCourse() {
        List<Course> list = new ArrayList<>();

        String sql = "SELECT * FROM Course";

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Course course = new Course(
                        rs.getInt("CourseID"),
                        rs.getString("CourseName"),
                        rs.getString("Description"),
                        rs.getInt("TotalSlots"),
                        rs.getBigDecimal("TuitionFee"),
                        rs.getBoolean("Status"),
                        rs.getString("Image")
                );

                list.add(course);
            }

        } catch (Exception e) {
            System.out.println("Fail to get all course: " + e.getMessage());
        }

        return list;
    }

    public List<Course> getCoursesByStudentId(int studentId) {

        List<Course> list = new ArrayList<>();

        String sql = "SELECT DISTINCT "
                + "co.CourseID, "
                + "co.CourseName, "
                + "co.Description, "
                + "co.TotalSlots, "
                + "co.TuitionFee, "
                + "co.Status, "
                + "co.Image "
                + "FROM Enrollment e "
                + "INNER JOIN Class c ON e.ClassID = c.ClassID "
                + "INNER JOIN Course co ON c.CourseID = co.CourseID "
                + "WHERE e.StudentID = ? "
                + "AND co.Status = 1";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Course course = new Course(
                        rs.getInt("CourseID"),
                        rs.getString("CourseName"),
                        rs.getString("Description"),
                        rs.getInt("TotalSlots"),
                        rs.getBigDecimal("TuitionFee"),
                        rs.getBoolean("Status"),
                        rs.getString("Image")
                );

                list.add(course);
            }

        } catch (Exception e) {
            System.out.println("Fail to get courses by student: " + e.getMessage());
        }

        return list;
    }

    public List<Course> getActiveCourses() {
        List<Course> list = new ArrayList<>();

        String sql = "SELECT * FROM Course WHERE Status = 1";

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Course course = new Course(
                        rs.getInt("CourseID"),
                        rs.getString("CourseName"),
                        rs.getString("Description"),
                        rs.getInt("TotalSlots"),
                        rs.getBigDecimal("TuitionFee"),
                        rs.getBoolean("Status"),
                        rs.getString("Image")
                );

                list.add(course);
            }

        } catch (Exception e) {
            System.out.println("Fail to get active courses: " + e.getMessage());
        }

        return list;
    }

    public Course getCourseById(int id) {
        Course course = null;

        String sql = "SELECT * FROM Course WHERE CourseID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    course = new Course(
                            rs.getInt("CourseID"),
                            rs.getString("CourseName"),
                            rs.getString("Description"),
                            rs.getInt("TotalSlots"),
                            rs.getBigDecimal("TuitionFee"),
                            rs.getBoolean("Status"),
                            rs.getString("Image")
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to get course by ID: " + e.getMessage());
        }

        return course;
    }

    public Double getAverageByStudentAndCourse(int courseId, int studentId) {

        String sql = "SELECT COALESCE(AVG(g.Score), 0) AS AverageScore "
                + "FROM Class cl "
                + "JOIN Enrollment e ON cl.ClassID = e.ClassID "
                + "JOIN Grade g ON e.EnrollmentID = g.EnrollmentID "
                + "WHERE cl.CourseID = ? "
                + "AND e.StudentID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, courseId);
            ps.setInt(2, studentId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Double avg = rs.getDouble("AverageScore");

                    // Nếu không có điểm → AVG trả NULL
                    if (rs.wasNull()) {
                        return 0.0;
                    }

                    return avg;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error calculating average: " + e.getMessage());
        }

        return 0.0;
    }

    public boolean addCourse(Course course) {
        String sql = "INSERT INTO Course (CourseName, Description, TotalSlots, TuitionFee, Status, Image) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, course.getCourseName());
            ps.setString(2, course.getDescription());
            ps.setInt(3, course.getTotalSlots());
            ps.setBigDecimal(4, course.getTuitionFee());
            ps.setBoolean(5, course.isStatus());
            ps.setString(6, course.getImages());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            System.out.println("Fail to add course: " + e.getMessage());
            return false;
        }
    }

    public boolean updateCourse(Course course) {
        String sql = "UPDATE Course SET CourseName = ?, Description = ?, TotalSlots = ?, TuitionFee = ?, Status = ?, Image = ? WHERE CourseID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, course.getCourseName());
            ps.setString(2, course.getDescription());
            ps.setInt(3, course.getTotalSlots());
            ps.setBigDecimal(4, course.getTuitionFee());
            ps.setBoolean(5, course.isStatus());
            ps.setString(6, course.getImages());
            ps.setInt(7, course.getCourseId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            System.out.println("Fail to update course: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteCourse(int courseId) {
        String sql = "UPDATE Course SET Status = 0 WHERE CourseID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            System.out.println("Fail to delete course: " + e.getMessage());
            return false;
        }
    }

    public boolean activateCourse(int courseId) {
        String sql = "UPDATE Course SET Status = 1 WHERE CourseID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            System.out.println("Fail to activate course: " + e.getMessage());
            return false;
        }
    }
    
    public int getTotalCourseCount() {
        String sql = "SELECT COUNT(*) FROM Course";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("Fail to get total course count: " + e.getMessage());
        }
        return 0;
    }

    public List<Course> searchCourses(String keyword) {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM Course WHERE CourseName LIKE ? OR Description LIKE ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            String searchKeyword = "%" + keyword + "%";
            ps.setString(1, searchKeyword);
            ps.setString(2, searchKeyword);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Course course = new Course(
                            rs.getInt("CourseID"),
                            rs.getString("CourseName"),
                            rs.getString("Description"),
                            rs.getInt("TotalSlots"),
                            rs.getBigDecimal("TuitionFee"),
                            rs.getBoolean("Status"),
                            rs.getString("Image")
                    );
                    list.add(course);
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to search courses: " + e.getMessage());
        }

        return list;
    }

    public List<Course> searchActiveCourses(String keyword) {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM Course WHERE Status = 1 AND (CourseName LIKE ? OR Description LIKE ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            String searchKeyword = "%" + keyword + "%";
            ps.setString(1, searchKeyword);
            ps.setString(2, searchKeyword);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Course course = new Course(
                            rs.getInt("CourseID"),
                            rs.getString("CourseName"),
                            rs.getString("Description"),
                            rs.getInt("TotalSlots"),
                            rs.getBigDecimal("TuitionFee"),
                            rs.getBoolean("Status"),
                            rs.getString("Image")
                    );
                    list.add(course);
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to search active courses: " + e.getMessage());
        }

        return list;
    }
}
