/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public List<Object[]> getCourseReviews(int courseId) {
        List<Object[]> reviews = new ArrayList<>();
        String sql = "SELECT f.FeedbackID, u.FullName AS StudentName, c.ClassName, t.FullName AS TeacherName, "
                + "f.Rating, f.Comment, f.SentDate "
                + "FROM Feedback f "
                + "JOIN Enrollment e ON f.EnrollmentID = e.EnrollmentID "
                + "JOIN Class c ON e.ClassID = c.ClassID "
                + "JOIN [User] u ON e.StudentID = u.UserID "
                + "LEFT JOIN [User] t ON c.TeacherID = t.UserID "
                + "WHERE c.CourseID = ? "
                + "ORDER BY f.SentDate DESC, f.FeedbackID DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reviews.add(new Object[]{
                        rs.getInt("FeedbackID"),
                        rs.getString("StudentName"),
                        rs.getString("ClassName"),
                        rs.getString("TeacherName"),
                        rs.getInt("Rating"),
                        rs.getString("Comment"),
                        rs.getTimestamp("SentDate")
                    });
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to get course reviews: " + e.getMessage());
        }

        return reviews;
    }

    public double getAverageRatingByCourseId(int courseId) {
        String sql = "SELECT AVG(CAST(f.Rating AS FLOAT)) AS AverageRating "
                + "FROM Feedback f "
                + "JOIN Enrollment e ON f.EnrollmentID = e.EnrollmentID "
                + "JOIN Class c ON e.ClassID = c.ClassID "
                + "WHERE c.CourseID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("AverageRating");
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to get average rating by course id: " + e.getMessage());
        }

        return 0;
    }
}
