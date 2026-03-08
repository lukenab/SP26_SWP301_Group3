package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import utils.DBContext;

public class PaymentDAO extends DBContext {

    public List<Object[]> searchAndFilterPayments(String searchQuery, String statusFilter,
            LocalDateTime fromDate, LocalDateTime toDate) {
        List<Object[]> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT p.PaymentID, p.EnrollmentID, p.Amount, p.PaymentDate, "
                + "p.PaymentMethod, p.EvidenceImage, p.Status, v.Code AS VoucherCode, "
                + "u.FullName AS StudentName, u.Email AS StudentEmail "
                + "FROM Payment p "
                + "JOIN Enrollment e ON p.EnrollmentID = e.EnrollmentID "
                + "JOIN Student s ON e.StudentID = s.StudentID "
                + "JOIN [User] u ON s.StudentID = u.UserID "
                + "LEFT JOIN Voucher v ON p.VoucherID = v.VoucherID "
                + "WHERE 1=1 ");

        boolean hasSearch = searchQuery != null && !searchQuery.trim().isEmpty();
        boolean hasStatus = statusFilter != null && !statusFilter.trim().isEmpty()
                && !"all".equalsIgnoreCase(statusFilter.trim());
        boolean hasFromDate = fromDate != null;
        boolean hasToDate = toDate != null;

        if (hasSearch) {
            sql.append("AND (u.FullName LIKE ? OR u.Email LIKE ?) ");
        }
        if (hasStatus) {
            sql.append("AND p.Status = ? ");
        }
        if (hasFromDate) {
            sql.append("AND p.PaymentDate >= ? ");
        }
        if (hasToDate) {
            sql.append("AND p.PaymentDate <= ? ");
        }

        sql.append("ORDER BY p.PaymentDate DESC, p.PaymentID DESC");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int index = 1;

            if (hasSearch) {
                String keyword = "%" + searchQuery.trim() + "%";
                ps.setString(index++, keyword);
                ps.setString(index++, keyword);
            }
            if (hasStatus) {
                ps.setString(index++, statusFilter.trim());
            }
            if (hasFromDate) {
                ps.setTimestamp(index++, Timestamp.valueOf(fromDate));
            }
            if (hasToDate) {
                ps.setTimestamp(index++, Timestamp.valueOf(toDate));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Timestamp paymentTs = rs.getTimestamp("PaymentDate");
                    LocalDateTime paymentDate = paymentTs == null ? null : paymentTs.toLocalDateTime();
                    list.add(new Object[]{
                        rs.getInt("PaymentID"),
                        rs.getInt("EnrollmentID"),
                        rs.getString("StudentName"),
                        rs.getString("StudentEmail"),
                        rs.getBigDecimal("Amount"),
                        paymentDate,
                        rs.getString("PaymentMethod"),
                        rs.getString("Status"),
                        rs.getString("VoucherCode"),
                        rs.getString("EvidenceImage")
                    });
                }
            }
        } catch (SQLException e) {
            System.out.println("Fail to search and filter payments: " + e.getMessage());
        }

        return list;
    }
}
