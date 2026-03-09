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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.Classes;
import model.Course;
import model.Enrollment;
import model.Payment;
import model.Student;
import model.Voucher;
import utils.DBContext;

/**
 *
 * @author Legion
 */
public class PaymentDAO extends DBContext {

    /**
     * Get all payment records with full details for display
     * Returns Object array: [Payment, StudentName, StudentEmail, CourseName, ClassName]
     */
    public List<Object[]> getAllPaymentsDisplay() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT p.PaymentID, p.EnrollmentID, p.Amount, p.PaymentDate, p.PaymentMethod, "
                + "p.EvidenceImage, p.Status, p.VoucherID, "
                + "s.StudentID, u.FullName AS StudentName, u.Email, "
                + "c.ClassID, c.ClassName, "
                + "co.CourseID, co.CourseName, "
                + "v.Code AS VoucherCode, v.DiscountAmount, v.DiscountPercent, "
                + "e.EnrollDate, e.Status AS EnrollStatus, e.FinalGrade "
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
                Payment payment = mapPaymentFromResultSet(rs);
                String studentName = rs.getString("StudentName");
                String studentEmail = rs.getString("Email");
                String courseName = rs.getString("CourseName");
                String className = rs.getString("ClassName");

                Object[] row = {payment, studentName, studentEmail, courseName, className};
                list.add(row);
            }
        } catch (Exception e) {
            System.out.println("Fail to get all payments: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Get filtered payment list by course, class, and status
     */
    public List<Object[]> getFilteredPaymentsDisplay(Integer courseId, Integer classId, String status) {
        List<Object[]> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT p.PaymentID, p.EnrollmentID, p.Amount, p.PaymentDate, p.PaymentMethod, ")
                .append("p.EvidenceImage, p.Status, p.VoucherID, ")
                .append("s.StudentID, u.FullName AS StudentName, u.Email, ")
                .append("c.ClassID, c.ClassName, ")
                .append("co.CourseID, co.CourseName, ")
                .append("v.Code AS VoucherCode, v.DiscountAmount, v.DiscountPercent, ")
                .append("e.EnrollDate, e.Status AS EnrollStatus, e.FinalGrade ")
                .append("FROM Payment p ")
                .append("INNER JOIN Enrollment e ON p.EnrollmentID = e.EnrollmentID ")
                .append("INNER JOIN Student s ON e.StudentID = s.StudentID ")
                .append("INNER JOIN [User] u ON s.StudentID = u.UserID ")
                .append("INNER JOIN Class c ON e.ClassID = c.ClassID ")
                .append("INNER JOIN Course co ON c.CourseID = co.CourseID ")
                .append("LEFT JOIN Voucher v ON p.VoucherID = v.VoucherID ")
                .append("WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        if (courseId != null && courseId > 0) {
            sql.append("AND co.CourseID = ? ");
            params.add(courseId);
        }
        if (classId != null && classId > 0) {
            sql.append("AND c.ClassID = ? ");
            params.add(classId);
        }
        if (status != null && !status.isEmpty() && !"all".equalsIgnoreCase(status)) {
            sql.append("AND p.Status = ? ");
            params.add(status);
        }

        sql.append("ORDER BY p.PaymentDate DESC, p.PaymentID DESC");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Payment payment = mapPaymentFromResultSet(rs);
                    String studentName = rs.getString("StudentName");
                    String studentEmail = rs.getString("Email");
                    String courseName = rs.getString("CourseName");
                    String className = rs.getString("ClassName");

                    Object[] row = {payment, studentName, studentEmail, courseName, className};
                    list.add(row);
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to get filtered payments: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Get payment by ID with full details
     */
    public Payment getPaymentById(int paymentId) {
        String sql = "SELECT p.PaymentID, p.EnrollmentID, p.Amount, p.PaymentDate, p.PaymentMethod, "
                + "p.EvidenceImage, p.Status, p.VoucherID, "
                + "s.StudentID, s.EnrollmentDate, "
                + "c.ClassID, c.ClassName, c.StartDate, c.EndDate, c.Status AS ClassStatus, "
                + "co.CourseID, co.CourseName, co.Description, co.TotalSlots, co.TuitionFee, co.Status AS CourseStatus, co.Image, "
                + "v.Code AS VoucherCode, v.DiscountAmount, v.DiscountPercent, "
                + "e.EnrollDate, e.Status AS EnrollStatus, e.FinalGrade "
                + "FROM Payment p "
                + "INNER JOIN Enrollment e ON p.EnrollmentID = e.EnrollmentID "
                + "INNER JOIN Student s ON e.StudentID = s.StudentID "
                + "INNER JOIN Class c ON e.ClassID = c.ClassID "
                + "INNER JOIN Course co ON c.CourseID = co.CourseID "
                + "LEFT JOIN Voucher v ON p.VoucherID = v.VoucherID "
                + "WHERE p.PaymentID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, paymentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapPaymentFromResultSet(rs);
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
     */
    public boolean updatePaymentStatus(int paymentId, String newStatus) {
        String sql = "UPDATE Payment SET Status = ? WHERE PaymentID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, paymentId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Fail to update payment status: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get pending payments count
     */
    public int getPendingPaymentsCount() {
        String sql = "SELECT COUNT(*) FROM Payment WHERE Status = 'Pending'";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("Fail to get pending payments count: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Get all courses for filter dropdown
     */
    public List<Object[]> getAllCoursesForFilter() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT DISTINCT co.CourseID, co.CourseName "
                + "FROM Course co "
                + "INNER JOIN Class c ON co.CourseID = c.CourseID "
                + "INNER JOIN Enrollment e ON c.ClassID = e.ClassID "
                + "INNER JOIN Payment p ON e.EnrollmentID = p.EnrollmentID "
                + "ORDER BY co.CourseName ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Object[] row = {rs.getInt("CourseID"), rs.getString("CourseName")};
                list.add(row);
            }
        } catch (Exception e) {
            System.out.println("Fail to get courses for filter: " + e.getMessage());
        }
        return list;
    }

    /**
     * Get classes for filter dropdown (optionally filtered by course)
     */
    public List<Object[]> getClassesForFilter(Integer courseId) {
        List<Object[]> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT DISTINCT c.ClassID, c.ClassName ")
                .append("FROM Class c ")
                .append("INNER JOIN Enrollment e ON c.ClassID = e.ClassID ")
                .append("INNER JOIN Payment p ON e.EnrollmentID = p.EnrollmentID ");

        if (courseId != null && courseId > 0) {
            sql.append("WHERE c.CourseID = ? ");
        }

        sql.append("ORDER BY c.ClassName ASC");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            if (courseId != null && courseId > 0) {
                ps.setInt(1, courseId);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {rs.getInt("ClassID"), rs.getString("ClassName")};
                    list.add(row);
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to get classes for filter: " + e.getMessage());
        }
        return list;
    }

    /**
     * Get payment count by status
     */
    public int getPaymentCountByStatus(String status) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Payment WHERE 1=1 ");

        if (status != null && !status.isEmpty()) {
            sql.append("AND Status = ?");
        }

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            if (status != null && !status.isEmpty()) {
                ps.setString(1, status);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to get payment count by status: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Get total amount by status
     */
    public BigDecimal getTotalAmountByStatus(String status) {
        StringBuilder sql = new StringBuilder("SELECT ISNULL(SUM(Amount), 0) FROM Payment WHERE 1=1 ");

        if (status != null && !status.isEmpty()) {
            sql.append("AND Status = ?");
        }

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            if (status != null && !status.isEmpty()) {
                ps.setString(1, status);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal(1);
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to get total amount by status: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }

    /**
     * Approve payment
     */
    public boolean approvePayment(int paymentId) {
        return updatePaymentStatus(paymentId, "Approved");
    }

    /**
     * Reject payment
     */
    public boolean rejectPayment(int paymentId) {
        return updatePaymentStatus(paymentId, "Rejected");
    }

    /**
     * Helper method to map ResultSet to Payment object
     */
    private Payment mapPaymentFromResultSet(ResultSet rs) throws SQLException {
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

        // Map Enrollment
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentId(rs.getInt("EnrollmentID"));

        // Map Student
        Student student = new Student();
        student.setStudentId(rs.getInt("StudentID"));
        try {
            Date enrollmentDate = rs.getDate("EnrollmentDate");
            student.setEnrollmentDate(enrollmentDate);
        } catch (Exception e) {
            // EnrollmentDate might not be in all queries
        }
        enrollment.setStudent(student);

        // Map Class
        Classes classes = new Classes();
        classes.setClassid(rs.getInt("ClassID"));
        classes.setClassName(rs.getString("ClassName"));
        try {
            classes.setStartDate(rs.getDate("StartDate"));
            classes.setEndDate(rs.getDate("EndDate"));
            classes.setStatus(rs.getString("ClassStatus"));
        } catch (Exception e) {
            // Some fields might not be in all queries
        }

        // Map Course
        Course course = new Course();
        course.setCourseId(rs.getInt("CourseID"));
        course.setCourseName(rs.getString("CourseName"));
        try {
            course.setDescription(rs.getString("Description"));
            course.setTotalSlots(rs.getInt("TotalSlots"));
            course.setTuitionFee(rs.getBigDecimal("TuitionFee"));
            course.setStatus(rs.getBoolean("CourseStatus"));
            course.setImages(rs.getString("Image"));
        } catch (Exception e) {
            // Some fields might not be in all queries
        }

        classes.setCourse(course);
        enrollment.setClasses(classes);

        try {
            enrollment.setEnrollDate(rs.getDate("EnrollDate"));
            enrollment.setStatus(rs.getString("EnrollStatus"));
            enrollment.setFinalGrade(rs.getDouble("FinalGrade"));
        } catch (Exception e) {
            // Some fields might not be in all queries
        }

        payment.setEnrollment(enrollment);

        // Map Voucher (if exists)
        int voucherId = rs.getInt("VoucherID");
        if (voucherId > 0) {
            Voucher voucher = new Voucher();
            voucher.setVoucherId(voucherId);
            try {
                voucher.setCode(rs.getString("VoucherCode"));
                voucher.setDiscountAmount(rs.getBigDecimal("DiscountAmount"));
                BigDecimal discountPercent = rs.getBigDecimal("DiscountPercent");
                if (discountPercent != null) {
                    voucher.setDiscountPercent(discountPercent.doubleValue());
                }
            } catch (Exception e) {
                // Voucher fields might not be in all queries
            }
            payment.setVoucher(voucher);
        }

        return payment;
    }
}





