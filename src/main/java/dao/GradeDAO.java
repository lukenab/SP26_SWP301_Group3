package dao;

import utils.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Assessment;
import model.Classes;
import model.Course;
import model.Enrollment;
import model.Grade;

/**
 * DAO xử lý bảng Grade
 */
public class GradeDAO extends DBContext {

    // ==============================
    // 1. Kiểm tra đã có điểm chưa
    // ==============================
    public boolean hasGrade(int studentId, int classId) {

        String sql = "SELECT 1"
                + "            FROM Grade g"
                + "            JOIN Enrollment e"
                + "                ON g.EnrollmentID = e.EnrollmentID"
                + "            WHERE e.StudentID = ?"
                + "              AND e.ClassID = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, studentId);
            st.setInt(2, classId);

            ResultSet rs = st.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ==============================
    // 2. Lấy điểm
    // ==============================
    public Float getScore(int studentId, int classId) {

        String sql = "SELECT g.Score"
                + "            FROM Grade g"
                + "            JOIN Enrollment e"
                + "                ON g.EnrollmentID = e.EnrollmentID"
                + "            WHERE e.StudentID = ?"
                + "              AND e.ClassID = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, studentId);
            st.setInt(2, classId);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return rs.getFloat("Score");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ==============================
    // 3. Insert điểm mới
    // ==============================
    public void insertScore(int studentId,
            int classId,
            float score) {

        String sql = "INSERT INTO Grade (EnrollmentID, Score)"
                + "            SELECT EnrollmentID, ?"
                + "            FROM Enrollment"
                + "            WHERE StudentID = ?"
                + "              AND ClassID = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {

            st.setFloat(1, score);
            st.setInt(2, studentId);
            st.setInt(3, classId);

            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==============================
    // 4. Update điểm
    // ==============================
    public void updateScore(int studentId,
            int classId,
            float score) {

        String sql = "UPDATE g"
                + "            SET g.Score = ?"
                + "            FROM Grade g"
                + "            JOIN Enrollment e"
                + "                ON g.EnrollmentID = e.EnrollmentID"
                + "            WHERE e.StudentID = ?"
                + "              AND e.ClassID = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {

            st.setFloat(1, score);
            st.setInt(2, studentId);
            st.setInt(3, classId);

            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==============================
    // 5. Save or Update (gọn)
    // ==============================
    public void saveOrUpdateScore(int studentId,
            int classId,
            float score) {

        if (hasGrade(studentId, classId)) {
            updateScore(studentId, classId, score);
        } else {
            insertScore(studentId, classId, score);
        }
    }

    // ==============================
    // 6. Delete điểm
    // ==============================
    public void deleteScore(int studentId, int classId) {

        String sql = "DELETE g"
                + "            FROM Grade g"
                + "            JOIN Enrollment e"
                + "                ON g.EnrollmentID = e.EnrollmentID"
                + "            WHERE e.StudentID = ?"
                + "              AND e.ClassID = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, studentId);
            st.setInt(2, classId);

            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Grade> getGradesByStudentId(int userId) {

        List<Grade> list = new ArrayList<>();

        String sql = "SELECT "
                + "g.GradeID, "
                + "g.Score, "
                + "a.AssessmentID, "
                + "a.AssessmentName, "
                + "a.Weight, "
                + "e.EnrollmentID, "
                + "c.ClassID, "
                + "c.ClassName, "
                + "co.CourseID, "
                + "co.CourseName "
                + "FROM [User] u "
                + "JOIN Student s ON u.UserID = s.StudentID "
                + "JOIN Enrollment e ON s.StudentID = e.StudentID "
                + "JOIN Class c ON e.ClassID = c.ClassID "
                + "JOIN Course co ON c.CourseID = co.CourseID "
                + "JOIN Grade g ON e.EnrollmentID = g.EnrollmentID "
                + "JOIN Assessment a ON g.AssessmentID = a.AssessmentID "
                + "WHERE u.UserID = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                // ===== Course =====
                Course course = new Course();
                course.setCourseId(rs.getInt("CourseID"));
                course.setCourseName(rs.getString("CourseName"));

                // ===== Class =====
                Classes clazz = new Classes();
                clazz.setClassid(rs.getInt("ClassID"));
                clazz.setClassName(rs.getString("ClassName"));
                clazz.setCourse(course);

                // ===== Enrollment =====
                Enrollment enrollment = new Enrollment();
                enrollment.setEnrollmentId(rs.getInt("EnrollmentID"));
                enrollment.setClasses(clazz);

                // ===== Assessment =====
                Assessment assessment = new Assessment();
                assessment.setAssessmentId(rs.getInt("AssessmentID"));
                assessment.setAssessmentName(rs.getString("AssessmentName"));
                assessment.setWeight(rs.getDouble("Weight"));

                // ===== Grade =====
                Grade grade = new Grade();
                grade.setGradeId(rs.getInt("GradeID"));
                grade.setScore(rs.getDouble("Score"));
                grade.setAssessment(assessment);
                grade.setEnrollment(enrollment);

                list.add(grade);
            }

        } catch (Exception e) {
            System.out.println("Error getGradesByStudentId: " + e.getMessage());
        }

        return list;
    }
}
