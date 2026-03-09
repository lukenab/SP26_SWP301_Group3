/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.Classes;
import model.Course;
import model.Room;
import model.Schedule;
import model.Student;
import model.User;
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

    public List<User> getStudentListByClassId(int classId) {

        List<User> list = new ArrayList<>();

        String sql = "SELECT u.* "
                + "FROM Enrollment e "
                + "JOIN [User] u ON e.StudentID = u.UserID "
                + "WHERE e.ClassID = ? "
                + "AND e.Status = 'Active' "
                + "AND u.RoleID = 5";

        try (PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, classId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {

                User u = new User();
                u.setUserId(rs.getInt("UserID"));
                u.setFullName(rs.getString("FullName"));
                u.setEmail(rs.getString("Email"));
                u.setPhone(rs.getString("Phone"));
                u.setAddress(rs.getString("Address"));
                u.setAvatar(rs.getString("Avatar"));
                list.add(u);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public User getUserById(int userId) {

        String sql = "SELECT * FROM [User] WHERE UserID = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {

                User u = new User();
                u.setUserId(rs.getInt("UserID"));
                u.setFullName(rs.getString("FullName"));
                u.setEmail(rs.getString("Email"));
                u.setPhone(rs.getString("Phone"));
                u.setAddress(rs.getString("Address"));
                u.setAvatar(rs.getString("Avatar"));
                return u;
            }

        } catch (Exception e) {
            e.printStackTrace();
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
