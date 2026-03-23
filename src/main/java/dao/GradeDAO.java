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

         String sql = "SELECT SUM(g.Score * a.Weight) / SUM(a.Weight) AS FinalScore "
                + "FROM Grade g "
                + "JOIN Assessment a "
                + "ON g.AssessmentID = a.AssessmentID "
                + "WHERE g.EnrollmentID = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, enrollmentId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                double score = rs.getDouble("FinalScore");
                return rs.wasNull() ? null : score;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public Map<Integer, Double> getAverageByClassId(int classId) {

        Map<Integer, Double> map = new HashMap<>();

        String sql
                = "SELECT e.StudentID, SUM(g.Score * a.Weight) / NULLIF(SUM(a.Weight), 0) AS FinalScore "
                + "FROM Enrollment e "
                + "LEFT JOIN Grade g ON e.EnrollmentID = g.EnrollmentID "
                + "LEFT JOIN Assessment a ON g.AssessmentID = a.AssessmentID "
                + "WHERE e.ClassID = ? "
                + "GROUP BY e.StudentID";

        try (PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, classId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                 double score = rs.getDouble("FinalScore");
                map.put(
                        rs.getInt("StudentID"),
                        rs.wasNull() ? null : score
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

    public List<Map<String, Object>> getFullGradeReport(int classId) {
        List<Map<String, Object>> list = new ArrayList<>();

        String sql = "SELECT u.UserID, u.FullName, u.Avatar, a.AssessmentName, g.Score "
                + "FROM Enrollment e "
                + "JOIN [User] u ON e.StudentID = u.UserID "
                + "LEFT JOIN Grade g ON e.EnrollmentID = g.EnrollmentID "
                + "LEFT JOIN Assessment a ON g.AssessmentID = a.AssessmentID "
                + "WHERE e.ClassID = ? "
                + "ORDER BY u.UserID, a.AssessmentID";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, classId);
            ResultSet rs = st.executeQuery();

            Map<Integer, Map<String, Object>> studentMap = new HashMap<>();

            while (rs.next()) {
                int userId = rs.getInt("UserID");
                if (!studentMap.containsKey(userId)) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("userId", userId);
                    row.put("fullName", rs.getString("FullName"));
                    row.put("avatar", rs.getString("Avatar"));

                    row.put("scores", new HashMap<String, Double>());
                    studentMap.put(userId, row);
                    list.add(row);
                }

                String assName = rs.getString("AssessmentName");
                if (assName != null) {
                    Map<String, Double> scores = (Map<String, Double>) studentMap.get(userId).get("scores");
                    scores.put(assName, rs.getDouble("Score"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    
     public void updateFinalGradeByEnrollmentId(int enrollmentId, Double finalGrade) {
        String sql = "UPDATE Enrollment SET FinalGrade = ? WHERE EnrollmentID = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            if (finalGrade == null) {
                st.setNull(1, Types.DOUBLE);
            } else {
                st.setDouble(1, finalGrade);
            }
            st.setInt(2, enrollmentId);
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Double recalculateAndPersistFinalGrade(int enrollmentId) {
        Double finalGrade = calculateAverage(enrollmentId);
        updateFinalGradeByEnrollmentId(enrollmentId, finalGrade);
        return finalGrade;
    }

    public void recalculateAndPersistFinalGradeByClassId(int classId) {
        String sql = "UPDATE e "
                + "SET e.FinalGrade = calc.FinalScore "
                + "FROM Enrollment e "
                + "OUTER APPLY ("
                + "    SELECT SUM(g.Score * a.Weight) / NULLIF(SUM(a.Weight), 0) AS FinalScore "
                + "    FROM Grade g "
                + "    JOIN Assessment a ON g.AssessmentID = a.AssessmentID "
                + "    WHERE g.EnrollmentID = e.EnrollmentID"
                + ") calc "
                + "WHERE e.ClassID = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, classId);
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
