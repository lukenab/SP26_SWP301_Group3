/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.Course;
import model.Lead;
import utils.DBContext;

/**
 *
 * @author Legion
 */
public class LeadDAO extends DBContext {

    CourseDAO courseDAO = new CourseDAO();

    public List<Lead> getAllLeads() {

        List<Lead> list = new ArrayList<>();

        String sql = "SELECT l.LeadID, l.FullName, l.Email, l.Phone, "
        + "l.InterestedCourseID, co.CourseName, "
        + "l.Status, l.CreateDate, c.Note "
        + "FROM Lead l "
        + "LEFT JOIN Course co ON l.InterestedCourseID = co.CourseID "
        + "LEFT JOIN Consultation c ON l.LeadID = c.LeadID";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int leadID = rs.getInt("LeadID");
                String fullName = rs.getString("FullName");
                String email = rs.getString("Email");
                String phone = rs.getString("Phone");

                int interestedCourseID = rs.getInt("InterestedCourseID");
                String courseName = rs.getString("CourseName");

                String status = rs.getString("Status");
                
                Timestamp ts = rs.getTimestamp("CreateDate");
                LocalDateTime createDate = null;
                if (ts != null) {
                    createDate = ts.toLocalDateTime();
                }

                String note = rs.getString("Note");

                Course course = null;
                if (courseName != null) {
                    course = new Course();
                    course.setCourseId(interestedCourseID);
                    course.setCourseName(courseName);
                }

                Lead lead = new Lead(leadID, fullName, email, phone, interestedCourseID, course, status, createDate);

                lead.setNote(note);

                list.add(lead);
            }

        } catch (SQLException e) {
            System.out.println("Fail to get all Leads: " + e.getMessage());
        }

        return list;
    }

    public Lead getLeadByID(int id) {

        String sql = "SELECT l.*, c.Note, co.CourseName "
                + "FROM Lead l "
                + "LEFT JOIN Consultation c ON l.LeadID = c.LeadID "
                + "LEFT JOIN Course co ON l.InterestedCourseID = co.CourseID "
                + "WHERE l.LeadID = ?";

        Lead lead = null;

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                int leadID = rs.getInt("LeadID");
                String fullName = rs.getString("FullName");
                String email = rs.getString("Email");
                String phone = rs.getString("Phone");
                int interestedCourseID = rs.getInt("InterestedCourseID");
                String status = rs.getString("Status");
                LocalDateTime createDate = rs.getTimestamp("CreateDate").toLocalDateTime();
                String note = rs.getString("Note");
                String courseName = rs.getString("CourseName");

                Course course = null;
                if (courseName != null) {
                    course = new Course();
                    course.setCourseId(interestedCourseID);
                    course.setCourseName(courseName);
                }

                lead = new Lead(leadID, fullName, email, phone,
                        interestedCourseID, course, status, createDate);

                lead.setNote(note);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lead;
    }

    public void insertLead(Lead lead) {

        String sqlLead = "INSERT INTO Lead (FullName, Email, Phone, InterestedCourseID, Status, CreateDate) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        String sqlConsult = "INSERT INTO Consultation (LeadID, Note) VALUES (?, ?)";

        try {
            conn.setAutoCommit(false);

            PreparedStatement ps = conn.prepareStatement(sqlLead, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setString(1, lead.getFullName());
            ps.setString(2, lead.getEmail());
            ps.setString(3, lead.getPhone());
            ps.setInt(4, lead.getInterestedCourseID());
            ps.setString(5, lead.getStatus());
            ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            int newLeadID = 0;

            if (rs.next()) {
                newLeadID = rs.getInt(1);
            }

            PreparedStatement psConsult = conn.prepareStatement(sqlConsult);
            psConsult.setInt(1, newLeadID);
            psConsult.setString(2, lead.getNote());
            psConsult.executeUpdate();

            conn.commit();

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    public boolean updateLead(int id, String fullName, String email, String phone,
            int interestedCourseID, String status, String note) {

        String sqlLead = "UPDATE Lead SET FullName = ?, Email = ?, Phone = ?, "
                + "InterestedCourseID = ?, Status = ? WHERE LeadID = ?";
        String sqlConsultUpdate = "UPDATE Consultation SET Note = ? WHERE LeadID = ?";
        String sqlConsultInsert = "INSERT INTO Consultation (LeadID, Note) VALUES (?, ?)";

        try {
            conn.setAutoCommit(false);

            PreparedStatement ps1 = conn.prepareStatement(sqlLead);
            ps1.setString(1, fullName);
            ps1.setString(2, email);
            ps1.setString(3, phone);
            ps1.setInt(4, interestedCourseID);
            ps1.setString(5, status);
            ps1.setInt(6, id);
            ps1.executeUpdate();

            PreparedStatement ps2 = conn.prepareStatement(sqlConsultUpdate);
            ps2.setString(1, note);
            ps2.setInt(2, id);

            int affected = ps2.executeUpdate();

            if (affected == 0) {
                PreparedStatement ps3 = conn.prepareStatement(sqlConsultInsert);
                ps3.setInt(1, id);
                ps3.setString(2, note);
                ps3.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
    }

   public void deleteLead(int id) {
        String sql = "UPDATE Lead SET Status = 'Inactive' WHERE LeadID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Fail to soft delete lead: " + e.getMessage());
        }
   }

   public void restoreLead(int id) {
        String sql = "UPDATE Lead SET Status = 'New' WHERE LeadID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Fail to restore lead: " + e.getMessage());
        }
   }
    public static void main(String[] args) {
        LeadDAO dao = new LeadDAO();
        List<Lead> list = dao.getAllLeads();
        for (Lead lead : list) {
            System.out.println(lead);
        }
    }
}
