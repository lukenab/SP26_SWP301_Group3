/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Schedule;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Classes;
import model.Course;
import model.Enrollment;
import model.Feedback;
import model.Room;
import model.Slot;
import model.Student;
import model.User;
import utils.DBContext;

/**
 *
 * @author ADMIN
 */
public class TeacherDAO extends DBContext {

    public List<Classes> getAllClassOfTeacherID(int teacherID) {
        List<Classes> list = new ArrayList<>();
        String sql = "SELECT c.*, co.CourseName FROM Class c "
                + "JOIN Course co ON c.CourseID = co.CourseID "
                + "WHERE c.TeacherID = ?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, teacherID);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Classes c = new Classes();
                c.setClassid(rs.getInt("ClassID"));
                c.setClassName(rs.getString("ClassName"));
                c.setStartDate(rs.getDate("StartDate"));
                c.setEndDate(rs.getDate("EndDate"));
                c.setStatus(rs.getString("Status"));
                Course course = new Course();
                course.setCourseId(rs.getInt("CourseID"));
                course.setCourseName(rs.getString("CourseName"));
                c.setCourse(course);
                list.add(c);

            }
        } catch (Exception e) {
        }
        return list;
    }

    // Get all classes with course info for academic staff
    public List<Classes> getAllClasses() {
        List<Classes> list = new ArrayList<>();
        String sql = "SELECT c.*, co.CourseName FROM Class c "
                + "JOIN Course co ON c.CourseID = co.CourseID "
                + "WHERE c.Status = 'Active' "
                + "ORDER BY c.ClassName ASC";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Classes c = new Classes();
                c.setClassid(rs.getInt("ClassID"));
                c.setClassName(rs.getString("ClassName"));
                c.setStartDate(rs.getDate("StartDate"));
                c.setEndDate(rs.getDate("EndDate"));
                c.setStatus(rs.getString("Status"));
                Course course = new Course();
                course.setCourseId(rs.getInt("CourseID"));
                course.setCourseName(rs.getString("CourseName"));
                c.setCourse(course);
                list.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Map<String, Object> getTeacherFeedbackData(int teacherId) {
        Map<String, Object> result = new HashMap<>();
        List<Feedback> feedbackList = new ArrayList<>();
        Map<Integer, String> studentNameMap = new HashMap<>();

        String sql = "SELECT f.FeedbackID, u.FullName AS StudentName, c.ClassName, f.Rating, f.Comment, f.SentDate "
                + "FROM Feedback f "
                + "JOIN Enrollment e ON f.EnrollmentID = e.EnrollmentID "
                + "JOIN [User] u ON e.StudentID = u.UserID "
                + "JOIN Class c ON e.ClassID = c.ClassID "
                + "WHERE c.TeacherID = ? "
                + "ORDER BY f.SentDate DESC";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, teacherId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int fId = rs.getInt("FeedbackID");

                Feedback f = new Feedback();
                f.setFeedbackId(fId);
                f.setRating(rs.getInt("Rating"));
                f.setComment(rs.getString("Comment"));
                f.setSentDate(rs.getTimestamp("SentDate").toLocalDateTime());

                Enrollment e = new Enrollment();
                Classes clazz = new Classes();
                clazz.setClassName(rs.getString("ClassName"));
                e.setClasses(clazz);
                f.setEnrollment(e);

                feedbackList.add(f);

                studentNameMap.put(fId, rs.getString("StudentName"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        result.put("feedbackList", feedbackList);
        result.put("studentNameMap", studentNameMap);
        return result;
    }

//    public int getTotalStudentsByTeacher(int teacherId) {
//        String sql = "SELECT COUNT(DISTINCT e.StudentID) FROM Enrollment e "
//                + "JOIN Class c ON e.ClassID = c.ClassID "
//                + "WHERE c.TeacherID = ?";
//        try (PreparedStatement st = conn.prepareStatement(sql)) {
//            st.setInt(1, teacherId);
//            ResultSet rs = st.executeQuery();
//            if (rs.next()) {
//                return rs.getInt(1);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }

    public int getTotalStudentsByTeacher(int teacherId) {
        String sql = "SELECT COUNT(DISTINCT e.StudentID) "
                + "FROM Enrollment e "
                + "JOIN Class c ON e.ClassID = c.ClassID "
                + "WHERE c.TeacherID = ? "
                + "AND c.Status = 'Active' "
                + "AND CAST(GETDATE() AS date) BETWEEN c.StartDate AND c.EndDate";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, teacherId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getClassProgress(int classId) {
        String sql = "SELECT "
                + "(SELECT COUNT(*) FROM Schedule WHERE ClassID = ? AND AttendanceStatus = 1) * 100 / "
                + "NULLIF((SELECT COUNT(*) FROM Schedule WHERE ClassID = ?), 0)";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, classId);
            st.setInt(2, classId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double getAverageRating(int teacherId) {
        String sql = "SELECT AVG(CAST(f.Rating AS DECIMAL(10,2))) "
                + "FROM Feedback f "
                + "JOIN Enrollment e ON f.EnrollmentID = e.EnrollmentID "
                + "JOIN Class c ON e.ClassID = c.ClassID "
                + "WHERE c.TeacherID = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, teacherId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public int getTotalSlotsTaught(int teacherId) {
        String sql = "SELECT COUNT(*) FROM Schedule s "
                + "JOIN Class c ON s.ClassID = c.ClassID "
                + "WHERE c.TeacherID = ? AND s.AttendanceStatus = 1";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, teacherId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}