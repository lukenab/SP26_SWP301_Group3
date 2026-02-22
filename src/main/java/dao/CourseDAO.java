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
}
