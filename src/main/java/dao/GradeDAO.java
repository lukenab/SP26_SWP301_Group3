package dao;

import utils.DBContext;
import java.sql.*;

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
}
