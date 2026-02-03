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
import model.Consultation;
import model.Employee;
import utils.DBContext;

/**
 *
 * @author LienNTK
 */
public class ConsultationDAO extends DBContext {
       
    public void insertConsultation(Consultation c) {
        String sql = "INSERT INTO consultation_log (lead_id, staff_id, note, created_date) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, c.getLead().getLeadId());
            ps.setInt(2, c.getEmployee().getEmployeeId());
            ps.setString(3, c.getNote());
            ps.setTimestamp(4, Timestamp.valueOf(c.getConsultation()));
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

   
    public List<Consultation> getConsultationsByLeadID(int leadID) {
        List<Consultation> list = new ArrayList<>();
        String sql = "SELECT * FROM consultation_log WHERE lead_id = ? ORDER BY created_date DESC";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, leadID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Consultation c = new Consultation();
                c.setConsultationId(rs.getInt("log_id"));
                c.setNote(rs.getString("note"));
                c.setConsultation(rs.getTimestamp("created_date").toLocalDateTime());
                list.add(c);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}
