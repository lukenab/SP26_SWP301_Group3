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
}
