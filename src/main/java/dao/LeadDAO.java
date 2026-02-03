/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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

    public List<Lead> getAllLeads(String status) {

        List<Lead> list = new ArrayList<>();

        String sql = "SELECT l.[LeadID], l.[FullName], l.[Email], l.[Phone], "
                + "l.[Status], l.[CreateDate], l.[InterestedCourseID], "
                + "c.[CourseName] "
                + "FROM [Lead] l "
                + "LEFT JOIN [Course] c ON l.[InterestedCourseID] = c.[CourseID]";

        if (status != null && !status.isEmpty()) {
            sql += " WHERE l.[Status] = ?";
        }

        try {

            PreparedStatement ps = conn.prepareStatement(sql);

            if (status != null && !status.isEmpty()) {
                ps.setString(1, status);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Lead lead = new Lead();

                lead.setLeadId(rs.getInt("LeadID"));
                lead.setFullName(rs.getString("FullName"));
                lead.setEmail(rs.getString("Email"));
                lead.setPhone(rs.getString("Phone"));
                lead.setInterestedCourseID(rs.getInt("InterestedCourseID"));
                lead.setStatus(rs.getString("Status"));
                Timestamp ts = rs.getTimestamp("CreateDate");
                if (ts != null) {
                    lead.setCreateDate(ts.toLocalDateTime());
                }

                Course course = new Course();
                course.setCourseName(rs.getString("CourseName"));
                lead.setCourse(course);

                list.add(lead);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi getAllLeads: " + e.getMessage());
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

}
