/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.Enrollment;
import model.Payment;
import model.Voucher;
import utils.DBContext;

/**
 *
 * @author Legion
 */
public class PaymentDAO extends DBContext {

    public List<Payment> getAllPayment() {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT * FROM Payment";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int PaymentId = rs.getInt("PaymentID");

                EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
                Enrollment enrollment = enrollmentDAO.getEnrollmentById(rs.getInt("EnrollmentID"));

                BigDecimal amount = rs.getBigDecimal("Amount");
                LocalDateTime paymentDate = rs.getTimestamp("PaymentDate").toLocalDateTime();
                String paymentMethod = rs.getString("PaymentMethod");
                String evidenceImg = rs.getString("EvidenceImage");
                String status = rs.getString("Status");

                VoucherDAO voucherDAO = new VoucherDAO();
                Voucher voucher = voucherDAO.getVoucherByID(rs.getInt("VoucherID"));

                Payment payment = new Payment(PaymentId, enrollment, amount, paymentDate, paymentMethod, evidenceImg, status, voucher);
                list.add(payment);
            }

        } catch (Exception e) {
            System.out.println("Fail to get all payment: " + e.getMessage());
        }
        return list;
    }

    public Payment getPaymentById(int id) {
        String sql = "SELECT * FROM Payment WHERE PaymentID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int PaymentId = rs.getInt("PaymentID");

                EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
                Enrollment enrollment = enrollmentDAO.getEnrollmentById(rs.getInt("EnrollmentID"));

                BigDecimal amount = rs.getBigDecimal("Amount");
                LocalDateTime paymentDate = rs.getTimestamp("PaymentDate").toLocalDateTime();
                String paymentMethod = rs.getString("PaymentMethod");
                String evidenceImg = rs.getString("EvidenceImage");
                String status = rs.getString("Status");

                VoucherDAO voucherDAO = new VoucherDAO();
                Voucher voucher = voucherDAO.getVoucherByID(rs.getInt("VoucherID"));

                Payment payment = new Payment(PaymentId, enrollment, amount, paymentDate, paymentMethod, evidenceImg, status, voucher);
                return payment;
            }

        } catch (Exception e) {
            System.out.println("Fail to get payment by ID: " + e.getMessage());
        }
        return null;
    }

    public boolean processQrPayment(int enrollmentId, double amount) {
        String sql = "INSERT INTO Payment (EnrollmentID, Amount, PaymentDate, PaymentMethod, EvidenceImage, Status) "
                + "VALUES (?, ?, GETDATE(), 'QR Transfer', NULL, 'Pending')";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, enrollmentId);
            ps.setDouble(2, amount);
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Fail to process payment: " +e.getMessage());
        }
        return false;
    }
}
