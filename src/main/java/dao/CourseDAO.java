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
        try {
            String sql = "SELECT * FROM Course";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int courseId = rs.getInt("CourseID");
                String courseName = rs.getString("CourseName");
                String description = rs.getString("Description");
                int totalSlots = rs.getInt("TotalSlots");
                BigDecimal tuitionFee = rs.getBigDecimal("TuitionFee");
                Boolean status = rs.getBoolean("Status");
                String images = rs.getString("image");
                
                Course course = new Course(courseId, courseName, description, totalSlots, tuitionFee, status, images);
                list.add(course);
            }
        }
            catch (Exception e) {
                  System.out.println("Fail to get all course: " +e.getMessage());  
        }
        return list;
    }
    
    public static void main(String[] args) {
        CourseDAO dao = new CourseDAO();
        List<Course> list = dao.getAllCourse();
        for (Course course : list) {
            System.out.println(course);
        }
    }
}

