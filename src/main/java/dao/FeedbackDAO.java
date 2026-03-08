/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import model.Feedback;
import utils.DBContext;

/**
 *
 * @author Legion
 */
public class FeedbackDAO extends DBContext {

    public boolean studentFeedback(Feedback feedback) {

        String sql = "INSERT INTO Feedback "
                + "(EnrollmentID, Rating, Comment, SentDate) "
                + "VALUES (?, ?, ?, GETDATE())";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, feedback.getEnrollment().getEnrollmentId());
            ps.setInt(2, feedback.getRating());
            ps.setString(3, feedback.getComment());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean isFeedbackExist(int enrollmentId) {

        String sql = "SELECT 1 FROM Feedback WHERE EnrollmentID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, enrollmentId);

            return ps.executeQuery().next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
