package dao;

import utils.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Assessment;
import model.Classes;
import model.Course;
import model.Enrollment;
import model.Grade;

public class GradeDAO extends DBContext {

    public Integer getEnrollmentId(int studentId, int classId) {

        String sql = "SELECT EnrollmentID "
                + "FROM Enrollment "
                + "WHERE StudentID = ? AND ClassID = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, studentId);
            st.setInt(2, classId);

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt("EnrollmentID");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer getCourseIdByClassId(int classId) {

        String sql = "SELECT CourseID FROM Class WHERE ClassID = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, classId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return rs.getInt("CourseID");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer getAssessmentIdByName(int courseId, String name) {

        String sql = "SELECT AssessmentID "
                + "FROM Assessment "
                + "WHERE CourseID = ? AND AssessmentName = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, courseId);
            st.setString(2, name);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return rs.getInt("AssessmentID");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public Map<String, Double> getAllScores(int enrollmentId) {

        Map<String, Double> scoreMap = new HashMap<>();

        String sql = "SELECT a.AssessmentName, g.Score "
                + "FROM Grade g "
                + "JOIN Assessment a "
                + "ON g.AssessmentID = a.AssessmentID "
                + "WHERE g.EnrollmentID = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, enrollmentId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                scoreMap.put(
                        rs.getString("AssessmentName"),
                        rs.getDouble("Score")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return scoreMap;
    }

    public Double getScore(int enrollmentId, int assessmentId) {

        String sql = "SELECT Score FROM Grade "
                + "WHERE EnrollmentID = ? AND AssessmentID = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, enrollmentId);
            st.setInt(2, assessmentId);

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getDouble("Score");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void insertScore(int enrollmentId,
            int assessmentId,
            double score) {

        String sql = "INSERT INTO Grade "
                + "(EnrollmentID, AssessmentID, Score) "
                + "VALUES (?, ?, ?)";

        try (PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, enrollmentId);
            st.setInt(2, assessmentId);
            st.setDouble(3, score);

            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateScore(int enrollmentId,
            int assessmentId,
            double score) {

        String sql = "UPDATE Grade SET Score = ? "
                + "WHERE EnrollmentID = ? AND AssessmentID = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {

            st.setDouble(1, score);
            st.setInt(2, enrollmentId);
            st.setInt(3, assessmentId);

            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveOrUpdate(int enrollmentId,
            int assessmentId,
            double score) {

        if (getScore(enrollmentId, assessmentId) != null) {
            updateScore(enrollmentId, assessmentId, score);
        } else {
            insertScore(enrollmentId, assessmentId, score);
        }
    }

    public void deleteScore(int enrollmentId, int assessmentId) {

        String sql = "DELETE FROM Grade "
                + "WHERE EnrollmentID = ? AND AssessmentID = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, enrollmentId);
            st.setInt(2, assessmentId);

            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Double calculateAverage(int enrollmentId) {

        String sql = "SELECT SUM(g.Score * a.Weight) AS FinalScore "
                + "FROM Grade g "
                + "JOIN Assessment a "
                + "ON g.AssessmentID = a.AssessmentID "
                + "WHERE g.EnrollmentID = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, enrollmentId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return rs.getDouble("FinalScore");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public Map<Integer, Double> getAverageByClassId(int classId) {

        Map<Integer, Double> map = new HashMap<>();

        String sql
                = "SELECT e.StudentID, SUM(g.Score * a.Weight) AS FinalScore "
                + "FROM Enrollment e "
                + "LEFT JOIN Grade g ON e.EnrollmentID = g.EnrollmentID "
                + "LEFT JOIN Assessment a ON g.AssessmentID = a.AssessmentID "
                + "WHERE e.ClassID = ? "
                + "GROUP BY e.StudentID";

        try (PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, classId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                map.put(
                        rs.getInt("StudentID"),
                        rs.getDouble("FinalScore")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    public void deleteAllByEnrollment(int enrollmentId) {

        String sql = "DELETE FROM Grade WHERE EnrollmentID = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, enrollmentId);
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
                + "INNER JOIN Student s ON u.UserID = s.StudentID "
                + "INNER JOIN Enrollment e ON s.StudentID = e.StudentID "
                + "INNER JOIN Class c ON e.ClassID = c.ClassID "
                + "INNER JOIN Course co ON c.CourseID = co.CourseID "
                + "INNER JOIN Grade g ON e.EnrollmentID = g.EnrollmentID "
                + "INNER JOIN Assessment a ON g.AssessmentID = a.AssessmentID "
                + "WHERE u.UserID = ? "
                + "ORDER BY c.ClassName, a.AssessmentName";

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

    public static void main(String[] args) {

        GradeDAO dao = new GradeDAO();

        int studentId = 14;
        int classId = 2;

        System.out.println("=== TEST GRADE DAO ===");

        Integer enrollmentId = dao.getEnrollmentId(studentId, classId);
        if (enrollmentId == null) {
            System.out.println("Enrollment not found!");
            return;
        }

        Integer courseId = dao.getCourseIdByClassId(classId);
        if (courseId == null) {
            System.out.println("Course not found!");
            return;
        }

        System.out.println("EnrollmentID: " + enrollmentId);

        Integer readingId = dao.getAssessmentIdByName(courseId, "Reading");
        Integer writingId = dao.getAssessmentIdByName(courseId, "Writing");
        Integer speakingId = dao.getAssessmentIdByName(courseId, "Speaking");
        Integer listeningId = dao.getAssessmentIdByName(courseId, "Listening");

        if (readingId == null) {
            System.out.println("Assessment not found!");
            return;
        }

        dao.saveOrUpdate(enrollmentId, readingId, 7);
        dao.saveOrUpdate(enrollmentId, writingId, 8);
        dao.saveOrUpdate(enrollmentId, speakingId, 6);
        dao.saveOrUpdate(enrollmentId, listeningId, 9);

        Map<String, Double> scores = dao.getAllScores(enrollmentId);

        System.out.println("=== Scores ===");
        for (Map.Entry<String, Double> entry : scores.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        Double avg = dao.calculateAverage(enrollmentId);
        System.out.println("Average: " + avg);
    }
}
