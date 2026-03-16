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

    public Object[] getSalesSummary(LocalDateTime fromDate, LocalDateTime toDate, Integer courseId) {
        Object[] summary = new Object[]{0, 0, 0, 0.0};
        boolean hasCourse = courseId != null && courseId > 0;
        String leadCourseFilter = hasCourse ? " AND l.InterestedCourseID = ? " : "";
        String enrollCourseFilter = hasCourse ? " AND c.CourseID = ? " : "";

        String sql = "SELECT "
                + "(SELECT COUNT(*) FROM Lead l WHERE l.CreateDate >= ? AND l.CreateDate <= ?"
                + leadCourseFilter
                + ") AS TotalLeads, "
                + "(SELECT COUNT(*) FROM Lead l WHERE l.CreateDate >= ? AND l.CreateDate <= ? AND l.Status = 'Converted'"
                + leadCourseFilter
                + ") AS ConvertedLeads, "
                + "(SELECT COUNT(DISTINCT e.StudentID) "
                + " FROM Enrollment e "
                + " JOIN Class c ON e.ClassID = c.ClassID "
                + " WHERE e.EnrollDate >= ? AND e.EnrollDate <= ?"
                + enrollCourseFilter
                + ") AS RegisteredStudents";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(fromDate));
            ps.setTimestamp(2, Timestamp.valueOf(toDate));
            int idx = 3;
            if (hasCourse) {
                ps.setInt(idx++, courseId);
            }

            ps.setTimestamp(idx++, Timestamp.valueOf(fromDate));
            ps.setTimestamp(idx++, Timestamp.valueOf(toDate));
            if (hasCourse) {
                ps.setInt(idx++, courseId);
            }

            ps.setTimestamp(idx++, Timestamp.valueOf(fromDate));
            ps.setTimestamp(idx++, Timestamp.valueOf(toDate));
            if (hasCourse) {
                ps.setInt(idx++, courseId);
            }

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

    public List<Object[]> getMonthlySalesReport(LocalDateTime fromDate, LocalDateTime toDate, Integer courseId) {
        List<Object[]> rows = new ArrayList<>();
        boolean hasCourse = courseId != null && courseId > 0;
        String leadCourseFilter = hasCourse ? " AND l.InterestedCourseID = ? " : "";
        String enrollCourseFilter = hasCourse ? " AND c.CourseID = ? " : "";

        String sql = "WITH lead_agg AS ( "
                + "    SELECT CAST(l.CreateDate AS DATE) AS DayValue, "
                + "           l.InterestedCourseID AS CourseID, "
                + "           COUNT(*) AS TotalLeads, "
                + "           SUM(CASE WHEN l.Status = 'Converted' THEN 1 ELSE 0 END) AS ConvertedLeads "
                + "    FROM Lead l "
                + "    WHERE l.CreateDate >= ? AND l.CreateDate <= ? "
                + leadCourseFilter
                + "    GROUP BY CAST(l.CreateDate AS DATE), l.InterestedCourseID "
                + "), enroll_agg AS ( "
                + "    SELECT CAST(e.EnrollDate AS DATE) AS DayValue, "
                + "           c.CourseID AS CourseID, "
                + "           COUNT(DISTINCT e.StudentID) AS RegisteredStudents "
                + "    FROM Enrollment e "
                + "    JOIN Class c ON e.ClassID = c.ClassID "
                + "    WHERE e.EnrollDate >= ? AND e.EnrollDate <= ? "
                + enrollCourseFilter
                + "    GROUP BY CAST(e.EnrollDate AS DATE), c.CourseID "
                + ") "
                + "SELECT FORMAT(x.DayValue, 'dd/MM/yyyy') AS DayLabel, "
                + "       COALESCE(co.CourseName, 'Unknown') AS CourseName, "
                + "       SUM(x.TotalLeads) AS TotalLeads, "
                + "       SUM(x.ConvertedLeads) AS ConvertedLeads, "
                + "       SUM(x.RegisteredStudents) AS RegisteredStudents "
                + "FROM ( "
                + "    SELECT DayValue, CourseID, TotalLeads, ConvertedLeads, 0 AS RegisteredStudents "
                + "    FROM lead_agg "
                + "    UNION ALL "
                + "    SELECT DayValue, CourseID, 0, 0, RegisteredStudents "
                + "    FROM enroll_agg "
                + ") x "
                + "LEFT JOIN Course co ON co.CourseID = x.CourseID "
                + "GROUP BY x.DayValue, co.CourseName "
                + "ORDER BY x.DayValue, co.CourseName";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            Timestamp fromTs = Timestamp.valueOf(fromDate);
            Timestamp toTs = Timestamp.valueOf(toDate);
            int idx = 1;
            ps.setTimestamp(idx++, fromTs);
            ps.setTimestamp(idx++, toTs);
            if (hasCourse) {
                ps.setInt(idx++, courseId);
            }
            ps.setTimestamp(idx++, fromTs);
            ps.setTimestamp(idx++, toTs);
            if (hasCourse) {
                ps.setInt(idx++, courseId);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int total = rs.getInt("TotalLeads");
                    int converted = rs.getInt("ConvertedLeads");
                    int students = rs.getInt("RegisteredStudents");
                    double rate = total == 0 ? 0.0 : (converted * 100.0) / total;
                    rows.add(new Object[]{rs.getString("DayLabel"), rs.getString("CourseName"), total, converted, students, rate});
                }
            }
        } catch (SQLException e) {
            System.out.println("Fail to get monthly sales report: " + e.getMessage());
        }

        return rows;
    }

    public List<Object[]> getCourseEnrollmentReport(LocalDateTime fromDate, LocalDateTime toDate) {
        List<Object[]> rows = new ArrayList<>();
        String sql = "SELECT co.CourseID, co.CourseName, "
                + "ISNULL((SELECT COUNT(*) "
                + "        FROM Enrollment e "
                + "        JOIN Class c2 ON e.ClassID = c2.ClassID "
                + "        WHERE c2.CourseID = co.CourseID "
                + "          AND e.EnrollDate >= ? AND e.EnrollDate <= ?), 0) AS NewEnrollments, "
                + "ISNULL((SELECT COUNT(DISTINCT e2.StudentID) "
                + "        FROM Enrollment e2 "
                + "        JOIN Class c3 ON e2.ClassID = c3.ClassID "
                + "        WHERE c3.CourseID = co.CourseID), 0) AS TotalStudents "
                + "FROM Course co "
                + "ORDER BY co.CourseName";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(fromDate));
            ps.setTimestamp(2, Timestamp.valueOf(toDate));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Object[]{
                        rs.getString("CourseName"),
                        rs.getInt("NewEnrollments"),
                        rs.getInt("TotalStudents")
                    });
                }
            }
        } catch (SQLException e) {
            System.out.println("Fail to get course enrollment report: " + e.getMessage());
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
                + "AND p.Status IN ('Approved', 'Paid', 'Complete', 'Completed')";

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
                + "          AND p.Status IN ('Approved', 'Paid', 'Complete', 'Completed')), 0) AS Revenue, "
                + "ISNULL((SELECT COUNT(*) "
                + "        FROM Payment p "
                + "        WHERE p.PaymentDate >= m.MonthStart "
                + "          AND p.PaymentDate < DATEADD(MONTH, 1, m.MonthStart) "
                + "          AND p.Status IN ('Approved', 'Paid', 'Complete', 'Completed')), 0) AS Payments, "
                + "ISNULL((SELECT COUNT(DISTINCT e.StudentID) "
                + "        FROM Payment p "
                + "        JOIN Enrollment e ON p.EnrollmentID = e.EnrollmentID "
                + "        WHERE p.PaymentDate >= m.MonthStart "
                + "          AND p.PaymentDate < DATEADD(MONTH, 1, m.MonthStart) "
                + "          AND p.Status IN ('Approved', 'Paid', 'Complete', 'Completed')), 0) AS Students "
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
