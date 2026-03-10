package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Syllabus;
import utils.DBContext;

public class SyllabusDAO extends DBContext {

    public List<Syllabus> getAllSyllabus() {
        List<Syllabus> list = new ArrayList<>();
        String sql = "SELECT s.SyllabusID, s.CourseID, s.[OrderIndex], s.TopicName, s.Description, c.CourseName "
                + "FROM Syllabus s "
                + "JOIN Course c ON s.CourseID = c.CourseID "
                + "ORDER BY c.CourseName ASC, s.[OrderIndex] ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Syllabus s = new Syllabus();
                s.setSyllabusId(rs.getInt("SyllabusID"));
                s.setCourseId(rs.getInt("CourseID"));
                s.setOrderIndex(rs.getInt("OrderIndex"));
                s.setTopicName(rs.getString("TopicName"));
                s.setDescription(rs.getString("Description"));
                s.setCourseName(rs.getString("CourseName"));
                list.add(s);
            }
        } catch (Exception e) {
            System.out.println("Fail to get all syllabus: " + e.getMessage());
        }
        return list;
    }

    public List<Syllabus> getSyllabusByCourseId(int courseId) {
        List<Syllabus> list = new ArrayList<>();
        String sql = "SELECT s.SyllabusID, s.CourseID, s.[OrderIndex], s.TopicName, s.Description, c.CourseName "
                + "FROM Syllabus s "
                + "JOIN Course c ON s.CourseID = c.CourseID "
                + "WHERE s.CourseID = ? "
                + "ORDER BY s.[OrderIndex], s.SyllabusID";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Syllabus s = new Syllabus();
                    s.setSyllabusId(rs.getInt("SyllabusID"));
                    s.setCourseId(rs.getInt("CourseID"));
                    s.setOrderIndex(rs.getInt("OrderIndex"));
                    s.setTopicName(rs.getString("TopicName"));
                    s.setDescription(rs.getString("Description"));
                    s.setCourseName(rs.getString("CourseName"));
                    list.add(s);
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to get syllabus by course id: " + e.getMessage());
        }

        return list;
    }

    public Syllabus getSyllabusById(int syllabusId) {
        String sql = "SELECT s.SyllabusID, s.CourseID, s.[OrderIndex], s.TopicName, s.Description, c.CourseName "
                + "FROM Syllabus s "
                + "JOIN Course c ON s.CourseID = c.CourseID "
                + "WHERE s.SyllabusID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, syllabusId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Syllabus s = new Syllabus();
                    s.setSyllabusId(rs.getInt("SyllabusID"));
                    s.setCourseId(rs.getInt("CourseID"));
                    s.setOrderIndex(rs.getInt("OrderIndex"));
                    s.setTopicName(rs.getString("TopicName"));
                    s.setDescription(rs.getString("Description"));
                    s.setCourseName(rs.getString("CourseName"));
                    return s;
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to get syllabus by id: " + e.getMessage());
        }
        return null;
    }

    public boolean updateSyllabus(Syllabus syllabus) {
        String sql = "UPDATE Syllabus "
                + "SET [OrderIndex] = ?, TopicName = ?, Description = ? "
                + "WHERE SyllabusID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, syllabus.getOrderIndex());
            ps.setString(2, syllabus.getTopicName());
            ps.setString(3, syllabus.getDescription());
            ps.setInt(4, syllabus.getSyllabusId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Fail to update syllabus: " + e.getMessage());
        }
        return false;
    }
}
