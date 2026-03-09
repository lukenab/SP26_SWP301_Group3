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
import utils.DBContext;

/**
 *
 * @author Legion
 */
public class SalesDAO extends DBContext {

    public Object[] getSalesSummary(LocalDateTime fromDate, LocalDateTime toDate) {
        Object[] summary = new Object[]{0, 0, 0, 0.0};

        String sql = "SELECT "
                + "(SELECT COUNT(*) FROM Lead l WHERE l.CreateDate >= ? AND l.CreateDate <= ?) AS TotalLeads, "
                + "(SELECT COUNT(*) FROM Lead l WHERE l.CreateDate >= ? AND l.CreateDate <= ? AND l.Status = 'Converted') AS ConvertedLeads, "
                + "(SELECT COUNT(*) FROM [User] u WHERE u.CreatedAt >= ? AND u.CreatedAt <= ? AND u.RoleID = 5) AS RegisteredStudents";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(fromDate));
            ps.setTimestamp(2, Timestamp.valueOf(toDate));
            ps.setTimestamp(3, Timestamp.valueOf(fromDate));
            ps.setTimestamp(4, Timestamp.valueOf(toDate));
            ps.setTimestamp(5, Timestamp.valueOf(fromDate));
            ps.setTimestamp(6, Timestamp.valueOf(toDate));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int total = rs.getInt("TotalLeads");
                    int converted = rs.getInt("ConvertedLeads");
                    int students = rs.getInt("RegisteredStudents");

                    summary[0] = total;
                    summary[1] = converted;
                    summary[2] = students;
                    summary[3] = total == 0 ? 0.0 : (converted * 100.0) / total;
                }
            }
        } catch (SQLException e) {
            System.out.println("Fail to get sales summary: " + e.getMessage());
        }

        return summary;
    }

    public List<Object[]> getMonthlySalesReport(LocalDateTime fromDate, LocalDateTime toDate) {
        List<Object[]> rows = new ArrayList<>();

        String sql = "WITH month_cte AS ( "
                + "    SELECT CAST(DATEFROMPARTS(YEAR(?), MONTH(?), 1) AS DATETIME) AS MonthStart "
                + "    UNION ALL "
                + "    SELECT DATEADD(MONTH, 1, MonthStart) "
                + "    FROM month_cte "
                + "    WHERE DATEADD(MONTH, 1, MonthStart) <= DATEFROMPARTS(YEAR(?), MONTH(?), 1) "
                + ") "
                + "SELECT FORMAT(m.MonthStart, 'MM/yyyy') AS MonthLabel, "
                + "ISNULL((SELECT COUNT(*) FROM Lead l "
                + "       WHERE l.CreateDate >= m.MonthStart "
                + "         AND l.CreateDate < DATEADD(MONTH, 1, m.MonthStart)), 0) AS TotalLeads, "
                + "ISNULL((SELECT COUNT(*) FROM Lead l "
                + "       WHERE l.CreateDate >= m.MonthStart "
                + "         AND l.CreateDate < DATEADD(MONTH, 1, m.MonthStart) "
                + "         AND l.Status = 'Converted'), 0) AS ConvertedLeads, "
                + "ISNULL((SELECT COUNT(*) FROM [User] u "
                + "       WHERE u.CreatedAt >= m.MonthStart "
                + "         AND u.CreatedAt < DATEADD(MONTH, 1, m.MonthStart) "
                + "         AND u.RoleID = 5), 0) AS RegisteredStudents "
                + "FROM month_cte m "
                + "OPTION (MAXRECURSION 120)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            Timestamp fromTs = Timestamp.valueOf(fromDate);
            Timestamp toTs = Timestamp.valueOf(toDate);
            ps.setTimestamp(1, fromTs);
            ps.setTimestamp(2, fromTs);
            ps.setTimestamp(3, toTs);
            ps.setTimestamp(4, toTs);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int total = rs.getInt("TotalLeads");
                    int converted = rs.getInt("ConvertedLeads");
                    int students = rs.getInt("RegisteredStudents");
                    double rate = total == 0 ? 0.0 : (converted * 100.0) / total;
                    rows.add(new Object[]{rs.getString("MonthLabel"), total, converted, students, rate});
                }
            }
        } catch (SQLException e) {
            System.out.println("Fail to get monthly sales report: " + e.getMessage());
        }

        return rows;
    }

    public Object[] getRevenueSummary(LocalDateTime fromDate, LocalDateTime toDate) {
        Object[] summary = new Object[]{0.0, 0, 0};

        String sql = "SELECT "
                + "ISNULL(SUM(CAST(p.Amount AS DECIMAL(18,2))), 0) AS TotalRevenue, "
                + "COUNT(*) AS VerifiedPayments, "
                + "COUNT(DISTINCT e.StudentID) AS PayingStudents "
                + "FROM Payment p "
                + "JOIN Enrollment e ON p.EnrollmentID = e.EnrollmentID "
                + "WHERE p.PaymentDate >= ? AND p.PaymentDate <= ? "
                + "AND p.Status IN ('Paid', 'Complete', 'Completed')";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(fromDate));
            ps.setTimestamp(2, Timestamp.valueOf(toDate));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    summary[0] = rs.getBigDecimal("TotalRevenue");
                    summary[1] = rs.getInt("VerifiedPayments");
                    summary[2] = rs.getInt("PayingStudents");
                }
            }
        } catch (SQLException e) {
            System.out.println("Fail to get revenue summary: " + e.getMessage());
        }

        return summary;
    }

    public List<Object[]> getMonthlyRevenueReport(LocalDateTime fromDate, LocalDateTime toDate) {
        List<Object[]> rows = new ArrayList<>();

        String sql = "WITH month_cte AS ( "
                + "    SELECT CAST(DATEFROMPARTS(YEAR(?), MONTH(?), 1) AS DATETIME) AS MonthStart "
                + "    UNION ALL "
                + "    SELECT DATEADD(MONTH, 1, MonthStart) "
                + "    FROM month_cte "
                + "    WHERE DATEADD(MONTH, 1, MonthStart) <= DATEFROMPARTS(YEAR(?), MONTH(?), 1) "
                + ") "
                + "SELECT FORMAT(m.MonthStart, 'MM/yyyy') AS MonthLabel, "
                + "ISNULL((SELECT SUM(CAST(p.Amount AS DECIMAL(18,2))) "
                + "        FROM Payment p "
                + "        WHERE p.PaymentDate >= m.MonthStart "
                + "          AND p.PaymentDate < DATEADD(MONTH, 1, m.MonthStart) "
                + "          AND p.Status IN ('Paid', 'Complete', 'Completed')), 0) AS Revenue, "
                + "ISNULL((SELECT COUNT(*) "
                + "        FROM Payment p "
                + "        WHERE p.PaymentDate >= m.MonthStart "
                + "          AND p.PaymentDate < DATEADD(MONTH, 1, m.MonthStart) "
                + "          AND p.Status IN ('Paid', 'Complete', 'Completed')), 0) AS Payments, "
                + "ISNULL((SELECT COUNT(DISTINCT e.StudentID) "
                + "        FROM Payment p "
                + "        JOIN Enrollment e ON p.EnrollmentID = e.EnrollmentID "
                + "        WHERE p.PaymentDate >= m.MonthStart "
                + "          AND p.PaymentDate < DATEADD(MONTH, 1, m.MonthStart) "
                + "          AND p.Status IN ('Paid', 'Complete', 'Completed')), 0) AS Students "
                + "FROM month_cte m "
                + "ORDER BY m.MonthStart "
                + "OPTION (MAXRECURSION 120)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            Timestamp fromTs = Timestamp.valueOf(fromDate);
            Timestamp toTs = Timestamp.valueOf(toDate);
            ps.setTimestamp(1, fromTs);
            ps.setTimestamp(2, fromTs);
            ps.setTimestamp(3, toTs);
            ps.setTimestamp(4, toTs);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Object[]{
                        rs.getString("MonthLabel"),
                        rs.getBigDecimal("Revenue"),
                        rs.getInt("Payments"),
                        rs.getInt("Students")
                    });
                }
            }
        } catch (SQLException e) {
            System.out.println("Fail to get monthly revenue report: " + e.getMessage());
        }

        return rows;
    }
}
