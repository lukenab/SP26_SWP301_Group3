/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
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
}
