/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Assessment;
import utils.DBContext;

/**
 *
 * @author Legion
 */
public class AssessmentDAO extends DBContext{
    public List<Assessment> getAssessmentsByClass(int classId) {
        List<Assessment> list = new ArrayList<>();

        String sql = "SELECT a.AssessmentID, a.AssessmentName, a.Weight, a.CourseID "
                + "FROM Assessment a "
                + "JOIN Class c ON a.CourseID = c.CourseID "
                + "WHERE c.ClassID = ? "
                + "ORDER BY a.AssessmentID";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, classId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                Assessment a = new Assessment();

                a.setAssessmentId(rs.getInt("AssessmentID"));
                a.setAssessmentName(rs.getString("AssessmentName"));
                a.setWeight(rs.getDouble("Weight"));

                list.add(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Assessment> getAssessmentsByCourse(int courseId) {
        List<Assessment> list = new ArrayList<>();
        String sql = "SELECT AssessmentID, AssessmentName, Weight, CourseID "
                + "FROM Assessment "
                + "WHERE CourseID = ? "
                + "ORDER BY AssessmentID";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, courseId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                Assessment a = new Assessment();
                a.setAssessmentId(rs.getInt("AssessmentID"));
                a.setAssessmentName(rs.getString("AssessmentName"));
                a.setWeight(rs.getDouble("Weight"));
                list.add(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public double getTotalWeightByCourse(int courseId) {
        String sql = "SELECT SUM(Weight) as TotalWeight FROM Assessment WHERE CourseID = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, courseId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                double total = rs.getDouble("TotalWeight");
                return Double.isNaN(total) ? 0.0 : total;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public Assessment getAssessmentById(int assessmentId) {
        String sql = "SELECT AssessmentID, AssessmentName, Weight, CourseID FROM Assessment WHERE AssessmentID = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, assessmentId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                Assessment a = new Assessment();
                a.setAssessmentId(rs.getInt("AssessmentID"));
                a.setAssessmentName(rs.getString("AssessmentName"));
                a.setWeight(rs.getDouble("Weight"));
                return a;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addAssessment(int courseId, String assessmentName, double weight) {
        String sql = "INSERT INTO Assessment (CourseID, AssessmentName, Weight) VALUES (?, ?, ?)";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, courseId);
            st.setString(2, assessmentName);
            st.setDouble(3, weight);

            return st.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateAssessment(int assessmentId, String assessmentName, double weight) {
        String sql = "UPDATE Assessment SET AssessmentName = ?, Weight = ? WHERE AssessmentID = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, assessmentName);
            st.setDouble(2, weight);
            st.setInt(3, assessmentId);

            return st.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteAssessment(int assessmentId) {
        String sql = "DELETE FROM Assessment WHERE AssessmentID = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, assessmentId);
            return st.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkAssessmentNameExists(int courseId, String assessmentName) {
        String sql = "SELECT COUNT(*) FROM Assessment WHERE CourseID = ? AND AssessmentName = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, courseId);
            st.setString(2, assessmentName);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
