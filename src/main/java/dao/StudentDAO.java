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

                list.add(u);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Schedule> getScheduleByStudentWeek(int userId,
            String startDate,
            String endDate) {

        List<Schedule> list = new ArrayList<>();

        String sql = "SELECT DISTINCT "
                + "s.ScheduleID, s.LearningDate, s.Slot, "
                + "c.ClassID, c.ClassName, "
                + "co.CourseID, co.CourseName, "
                + "r.RoomID, r.RoomName "
                + "FROM [User] u "
                + "JOIN Student st ON u.UserID = st.StudentID "
                + "JOIN Enrollment e ON st.StudentID = e.StudentID "
                + "JOIN Class c ON e.ClassID = c.ClassID "
                + "JOIN Course co ON c.CourseID = co.CourseID "
                + "JOIN Schedule s ON c.ClassID = s.ClassID "
                + "LEFT JOIN Room r ON s.RoomID = r.RoomID "
                + "WHERE u.UserID = ? "
                + "AND e.Status = 'Enrolled' "
                + "AND s.LearningDate BETWEEN ? AND ? "
                + "ORDER BY s.LearningDate, s.Slot";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setString(2, startDate);
            ps.setString(3, endDate);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Course course = new Course();
                course.setCourseId(rs.getInt("CourseID"));
                course.setCourseName(rs.getString("CourseName"));

                Classes classes = new Classes();
                classes.setClassid(rs.getInt("ClassID"));
                classes.setClassName(rs.getString("ClassName"));
                classes.setCourse(course);

                Room room = new Room();
                room.setRoomId(rs.getInt("RoomID"));
                room.setRoomName(rs.getString("RoomName"));

                Schedule s = new Schedule();
                s.setScheduleId(rs.getInt("ScheduleID"));
                s.setLearningDate(rs.getDate("LearningDate"));
                s.setSlot(rs.getInt("Slot"));
                s.setClasses(classes);
                s.setRoom(room);

                list.add(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
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
