/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        String sql = "SELECT * FROM Lead";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int leadID = rs.getInt("LeadID");
                String fullName = rs.getString("FullName");
                String Email = rs.getString("Email");
                String Phone = rs.getString("Phone");
                Course course = courseDAO.getCourseById(rs.getInt("InterestedCourseID"));
                String status = rs.getString("Status");
                LocalDateTime ts = rs.getTimestamp("CreateDate").toLocalDateTime();
                Lead lead = new Lead(leadID, fullName, Email, Phone, leadID, course, status, ts);
                list.add(lead);
            }
        } catch (SQLException e) {
            System.out.println("Fail to get all Leads: " + e.getMessage());
        }
        return list;
    }

//    public Lead getLeadByID(int id) {
//        String sql = "SELECT l.*, c.course_name FROM Lead l "
//                + "LEFT JOIN Course c ON l.course_id = c.course_id WHERE l.lead_id = ?";
//        try {
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ps.setInt(1, id);
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                return mapLead(rs);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

//    public void insertLead(Lead lead) {
//        String sql = "INSERT INTO Lead (full_name, email, phone, course_id, status) VALUES (?, ?, ?, ?, ?)";
//        try {
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ps.setString(1, lead.getFullName());
//            ps.setString(2, lead.getEmail());
//            ps.setString(3, lead.getPhone());
//            ps.setInt(4, lead.getInterestedCourseID());
//            ps.setString(5, lead.getStatus());
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

//    public void updateLeadStatus(int id, String status) {
//        String sql = "UPDATE Lead SET status = ? WHERE lead_id = ?";
//        try {
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ps.setString(1, status);
//            ps.setInt(2, id);
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
    public static void main(String[] args) {
        LeadDAO dao = new LeadDAO();
        List<Lead> list = dao.getAllLeads();
        for (Lead lead : list) {
            System.out.println(lead);
        }
    }
}