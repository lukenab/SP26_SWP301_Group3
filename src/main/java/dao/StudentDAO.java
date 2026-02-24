/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.Student;
import utils.DBContext;

/**
 *
 * @author Legion
 */
public class StudentDAO extends DBContext {

    public List<Student> getAllStudent() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM Student";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("StudentID");
                Date enrollDate = rs.getDate("EnrollmentDate");
                Student student = new Student(id, enrollDate);
                list.add(student);
            }
        } catch (Exception e) {
            System.out.println("Fail to get all student!: " + e.getMessage());
        }
        return list;
    }

    public Student getStudentById(int id) {
        String sql = "SELECT * FROM Student WHERE StudentID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Date enrollDate = rs.getDate("EnrollmentDate");
                return new Student(id, enrollDate);
            }
        } catch (Exception e) {
            System.out.println("Fail to get all student!: " + e.getMessage());
        }
        return null;
    }
    
    public static void main(String[] args) {
        StudentDAO dao = new StudentDAO();
//        List<Student> list = dao.getAllStudent();
//        for (Student student : list) {
//            System.out.println(student);
//        }
        
        Student student = dao.getStudentById(14);
        System.out.println(student);
    }
}
