/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.Classes;
import model.Course;
import model.Enrollment;
import model.Payment;
import model.Student;
import model.User;
import model.Voucher;
import utils.DBContext;

/**
 *
 * @author Legion
 */
public class PaymentDAO extends DBContext {

    /**
     * Get all payment records with enrollment, student, class, course info
     *
     * @return List of PaymentDisplay objects
     */
    public List<PaymentDisplay> getAllPayments() {
        List<PaymentDisplay> list = new ArrayList<>();
        String sql = "SELECT p.PaymentID, p.EnrollmentID, p.Amount, p.PaymentDate, p.PaymentMethod, "
                + "p.EvidenceImage, p.Status, p.VoucherID, "
                + "s.StudentID, u.FullName AS StudentName, u.Email, "
                + "c.ClassID, c.ClassName, "
                + "co.CourseID, co.CourseName, "
                + "v.VoucherID, v.Code AS VoucherCode, v.DiscountAmount, v.DiscountPercent "
                + "FROM Payment p "
                + "INNER JOIN Enrollment e ON p.EnrollmentID = e.EnrollmentID "
                + "INNER JOIN Student s ON e.StudentID = s.StudentID "
                + "INNER JOIN [User] u ON s.StudentID = u.UserID "
                + "INNER JOIN Class c ON e.ClassID = c.ClassID "
                + "INNER JOIN Course co ON c.CourseID = co.CourseID "
                + "LEFT JOIN Voucher v ON p.VoucherID = v.VoucherID "
                + "ORDER BY p.PaymentDate DESC, p.PaymentID DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Payment payment = mapPayment(rs);
                String studentName = rs.getString("StudentName");
                String studentEmail = rs.getString("Email");
                list.add(new PaymentDisplay(payment, studentName, studentEmail));
            }
        } catch (Exception e) {
            System.out.println("Fail to get all payments: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Get filtered payment list by course, class, and status
     *
     * @param courseId Course ID filter (null for all)
     * @param classId Class ID filter (null for all)
     * @param status Status filter (null for all)
     * @return Filtered list of PaymentDisplay objects
     */
    public List<PaymentDisplay> getFilteredPayments(Integer courseId, Integer classId, String status) {
        List<PaymentDisplay> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.PaymentID, p.EnrollmentID, p.Amount, p.PaymentDate, p.PaymentMethod, ")
           .append("p.EvidenceImage, p.Status, p.VoucherID, ")
           .append("s.StudentID, u.FullName AS StudentName, u.Email, ")
           .append("c.ClassID, c.ClassName, ")
           .append("co.CourseID, co.CourseName, ")
           .append("v.VoucherID, v.Code AS VoucherCode, v.DiscountAmount, v.DiscountPercent ")
           .append("FROM Payment p ")
           .append("INNER JOIN Enrollment e ON p.EnrollmentID = e.EnrollmentID ")
           .append("INNER JOIN Student s ON e.StudentID = s.StudentID ")
           .append("INNER JOIN [User] u ON s.StudentID = u.UserID ")
           .append("INNER JOIN Class c ON e.ClassID = c.ClassID ")
           .append("INNER JOIN Course co ON c.CourseID = co.CourseID ")
           .append("LEFT JOIN Voucher v ON p.VoucherID = v.VoucherID ")
           .append("WHERE 1=1 ");

        if (courseId != null && courseId > 0) {
            sql.append("AND co.CourseID = ? ");
        }
        if (classId != null && classId > 0) {
            sql.append("AND c.ClassID = ? ");
        }
        if (status != null && !status.trim().isEmpty() && !status.equals("All")) {
            sql.append("AND p.Status = ? ");
        }

        sql.append("ORDER BY p.PaymentDate DESC, p.PaymentID DESC");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;

            if (courseId != null && courseId > 0) {
                ps.setInt(paramIndex++, courseId);
            }
            if (classId != null && classId > 0) {
                ps.setInt(paramIndex++, classId);
            }
            if (status != null && !status.trim().isEmpty() && !status.equals("All")) {
                ps.setString(paramIndex++, status);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Payment payment = mapPayment(rs);
                    String studentName = rs.getString("StudentName");
                    String studentEmail = rs.getString("Email");
                    list.add(new PaymentDisplay(payment, studentName, studentEmail));
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to get filtered payments: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Get payment by ID
     *
     * @param paymentId Payment ID
     * @return Payment object or null
     */
    public Payment getPaymentById(int paymentId) {
        String sql = "SELECT p.PaymentID, p.EnrollmentID, p.Amount, p.PaymentDate, p.PaymentMethod, "
                + "p.EvidenceImage, p.Status, p.VoucherID, "
                + "s.StudentID, u.FullName AS StudentName, u.Email, "
                + "c.ClassID, c.ClassName, "
                + "co.CourseID, co.CourseName, "
                + "v.VoucherID, v.Code AS VoucherCode, v.DiscountAmount, v.DiscountPercent "
                + "FROM Payment p "
                + "INNER JOIN Enrollment e ON p.EnrollmentID = e.EnrollmentID "
                + "INNER JOIN Student s ON e.StudentID = s.StudentID "
                + "INNER JOIN [User] u ON s.StudentID = u.UserID "
                + "INNER JOIN Class c ON e.ClassID = c.ClassID "
                + "INNER JOIN Course co ON c.CourseID = co.CourseID "
                + "LEFT JOIN Voucher v ON p.VoucherID = v.VoucherID "
                + "WHERE p.PaymentID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, paymentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapPayment(rs);
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to get payment by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Update payment status
     *
     * @param paymentId Payment ID
     * @param status New status (Pending, Approved, Rejected)
     * @return true if success, false otherwise
     */
    public boolean updatePaymentStatus(int paymentId, String status) {
        String sql = "UPDATE Payment SET Status = ? WHERE PaymentID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, paymentId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Fail to update payment status: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get total count of payments by status
     *
     * @param status Status filter (null for all)
     * @return Count of payments
     */
    public int getPaymentCountByStatus(String status) {
        String sql;
        if (status == null || status.trim().isEmpty()) {
            sql = "SELECT COUNT(*) FROM Payment";
        } else {
            sql = "SELECT COUNT(*) FROM Payment WHERE Status = ?";
        }

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            if (status != null && !status.trim().isEmpty()) {
                ps.setString(1, status);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to get payment count: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Get total payment amount by status
     *
     * @param status Status filter (null for all)
     * @return Total amount
     */
    public BigDecimal getTotalAmountByStatus(String status) {
        String sql;
        if (status == null || status.trim().isEmpty()) {
            sql = "SELECT ISNULL(SUM(Amount), 0) FROM Payment";
        } else {
            sql = "SELECT ISNULL(SUM(Amount), 0) FROM Payment WHERE Status = ?";
        }

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            if (status != null && !status.trim().isEmpty()) {
                ps.setString(1, status);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal(1);
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to get total amount: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }

    /**
     * Map ResultSet to Payment object with custom fields for display
     * Note: We store student name and email as separate fields since Student model doesn't have User reference
     */
    private Payment mapPayment(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setPaymentId(rs.getInt("PaymentID"));
        payment.setAmount(rs.getBigDecimal("Amount"));

        Timestamp ts = rs.getTimestamp("PaymentDate");
        if (ts != null) {
            payment.setPaymentDate(ts.toLocalDateTime());
        }

        payment.setPaymentMethod(rs.getString("PaymentMethod"));
        payment.setEvidenceImage(rs.getString("EvidenceImage"));
        payment.setStatus(rs.getString("Status"));

        // Map Enrollment with simplified structure
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentId(rs.getInt("EnrollmentID"));

        // Map Student (only ID, no nested User object)
        Student student = new Student();
        student.setStudentId(rs.getInt("StudentID"));
        enrollment.setStudent(student);

        // Map Class
        Classes classes = new Classes();
        classes.setClassid(rs.getInt("ClassID"));
        classes.setClassName(rs.getString("ClassName"));

        // Map Course
        Course course = new Course();
        course.setCourseId(rs.getInt("CourseID"));
        course.setCourseName(rs.getString("CourseName"));
        classes.setCourse(course);

        enrollment.setClasses(classes);
        payment.setEnrollment(enrollment);

        // Map Voucher (optional)
        int voucherId = rs.getInt("VoucherID");
        if (!rs.wasNull()) {
            Voucher voucher = new Voucher();
            voucher.setVoucherId(voucherId);
            voucher.setCode(rs.getString("VoucherCode"));
            voucher.setDiscountAmount(rs.getBigDecimal("DiscountAmount"));
            voucher.setDiscountPercent(rs.getDouble("DiscountPercent"));
            payment.setVoucher(voucher);
        }

        return payment;
    }

    // Helper class to hold payment display data
    public static class PaymentDisplay {
        private Payment payment;
        private String studentName;
        private String studentEmail;

        public PaymentDisplay(Payment payment, String studentName, String studentEmail) {
            this.payment = payment;
            this.studentName = studentName;
            this.studentEmail = studentEmail;
        }

        public Payment getPayment() { return payment; }
        public String getStudentName() { return studentName; }
        public String getStudentEmail() { return studentEmail; }
    }
    
    public boolean confirmQRPayment(int enrollmentId, double amount) {
        // Thay chữ NULL thành '' để lách luật SQL Server
        String sql = "INSERT INTO Payment (EnrollmentID, Amount, PaymentDate, PaymentMethod, EvidenceImage, Status) "
                     + "VALUES (?, ?, GETDATE(), 'QR Transfer', '', 'Pending')";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, enrollmentId);
            ps.setDouble(2, amount);
            
            int rowAffected = ps.executeUpdate();
            return rowAffected > 0;
        } catch (Exception e) {
            System.out.println("Error at confirmQRPayment: " + e.getMessage());
        }
        return false;
    }
    
    
}

