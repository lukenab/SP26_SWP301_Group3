package dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.Voucher;
import utils.DBContext;

public class VoucherDAO extends DBContext {

    public List<Voucher> getAllVoucher() {
        List<Voucher> list = new ArrayList<>();
        String sql = "SELECT VoucherID, Code, DiscountAmount, DiscountPercent, ValidUntil, Status, MaxUsage "
                + "FROM Voucher ORDER BY VoucherID DESC";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapVoucher(rs));
            }
        } catch (Exception e) {
            System.out.println("Fail to get all voucher: " + e.getMessage());
        }
        return list;
    }

    public List<Voucher> searchAndFilterVouchers(String searchQuery, String status) {
        List<Voucher> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT v.VoucherID, v.Code, v.DiscountAmount, v.DiscountPercent, v.ValidUntil, v.Status, v.MaxUsage, "
                + "ISNULL((SELECT COUNT(*) FROM Payment p WHERE p.VoucherID = v.VoucherID AND p.Status <> 'Rejected'), 0) AS UsedCount "
                + "FROM Voucher v WHERE 1=1 ");

        boolean hasSearch = searchQuery != null && !searchQuery.trim().isEmpty();
        boolean hasStatus = status != null && !status.trim().isEmpty() && !"all".equalsIgnoreCase(status.trim());

        if (hasSearch) {
            sql.append("AND Code LIKE ? ");
        }
        if (hasStatus) {
            sql.append("AND Status = ? ");
        }
        sql.append("ORDER BY v.VoucherID DESC");

        try {
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            int index = 1;
            if (hasSearch) {
                ps.setString(index++, "%" + searchQuery.trim().toUpperCase() + "%");
            }
            if (hasStatus) {
                ps.setBoolean(index++, "1".equals(status) || "active".equalsIgnoreCase(status.trim()));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapVoucher(rs));
            }
        } catch (Exception e) {
            System.out.println("Fail to search and filter voucher: " + e.getMessage());
        }
        return list;
    }

    public List<Voucher> getActiveVoucher() {
        List<Voucher> list = new ArrayList<>();
        String sql = "SELECT VoucherID, Code, DiscountAmount, DiscountPercent, ValidUntil, Status, MaxUsage "
                + "FROM Voucher WHERE Status = 1 ORDER BY ValidUntil ASC";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapVoucher(rs));
            }
        } catch (Exception e) {
            System.out.println("Fail to get active voucher: " + e.getMessage());
        }
        return list;
    }

    public Voucher getVoucherByID(int id) {
        String sql = "SELECT VoucherID, Code, DiscountAmount, DiscountPercent, ValidUntil, Status, MaxUsage "
                + "FROM Voucher WHERE VoucherID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapVoucher(rs);
            }
        } catch (Exception e) {
            System.out.println("Fail to get voucher by id: " + e.getMessage());
        }
        return null;
    }

    public Voucher getVoucherByCode(String code) {
        String sql = "SELECT VoucherID, Code, DiscountAmount, DiscountPercent, ValidUntil, Status, MaxUsage "
                + "FROM Voucher WHERE Code = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapVoucher(rs);
            }
        } catch (Exception e) {
            System.out.println("Fail to get voucher by code: " + e.getMessage());
        }
        return null;
    }

    public void insertVoucher(Voucher v) {
        String sql = "INSERT INTO Voucher (Code, DiscountAmount, DiscountPercent, ValidUntil, Status, MaxUsage) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, v.getCode());
            ps.setBigDecimal(2, v.getDiscountAmount());
            ps.setDouble(3, v.getDiscountPercent());
            if (v.getValidUntil() == null) {
                ps.setNull(4, java.sql.Types.DATE);
            } else {
                ps.setDate(4, new Date(v.getValidUntil().getTime()));
            }
            ps.setBoolean(5, v.isStatus());
            if (v.getMaxUsage() == null) {
                ps.setNull(6, java.sql.Types.INTEGER);
            } else {
                ps.setInt(6, v.getMaxUsage());
            }
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Fail to insert voucher: " + e.getMessage());
        }
    }

    public boolean updateVoucher(int id, String code, java.math.BigDecimal discountAmount, double discountPercent, Date validUntil, boolean status, Integer maxUsage) {
        String sql = "UPDATE Voucher SET Code = ?, DiscountAmount = ?, DiscountPercent = ?, ValidUntil = ?, Status = ?, MaxUsage = ? "
                + "WHERE VoucherID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, code);
            ps.setBigDecimal(2, discountAmount);
            ps.setDouble(3, discountPercent);
            ps.setDate(4, validUntil);
            ps.setBoolean(5, status);
            if (maxUsage == null) {
                ps.setNull(6, java.sql.Types.INTEGER);
            } else {
                ps.setInt(6, maxUsage);
            }
            ps.setInt(7, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Fail to update voucher: " + e.getMessage());
        }
        return false;
    }

    public void deleteVoucher(int id) {
        String sql = "UPDATE Voucher SET Status = 0 WHERE VoucherID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Fail to delete voucher: " + e.getMessage());
        }
    }

    public void restoreVoucher(int id) {
        String sql = "UPDATE Voucher SET Status = 1 WHERE VoucherID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Fail to restore voucher: " + e.getMessage());
        }
    }

    public boolean checkVoucherAvailability(String code) {
        String sql = "SELECT 1 FROM Voucher WHERE Code = ? AND Status = 1 "
                + "AND (ValidUntil IS NULL OR ValidUntil >= CAST(GETDATE() AS DATE))";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.out.println("Fail to check voucher availability: " + e.getMessage());
        }
        return false;
    }

    public boolean isCodeExists(String code, Integer ignoreId) {
        String sql = "SELECT 1 FROM Voucher WHERE Code = ?"
                + (ignoreId != null ? " AND VoucherID <> ?" : "");
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, code);
            if (ignoreId != null) {
                ps.setInt(2, ignoreId);
            }
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.out.println("Fail to check code exists: " + e.getMessage());
        }
        return false;
    }

    private Voucher mapVoucher(ResultSet rs) throws Exception {
        Voucher voucher = new Voucher();
        voucher.setVoucherId(rs.getInt("VoucherID"));
        voucher.setCode(rs.getString("Code"));
        voucher.setDiscountAmount(rs.getBigDecimal("DiscountAmount"));
        voucher.setDiscountPercent(rs.getDouble("DiscountPercent"));
        voucher.setValidUntil(rs.getDate("ValidUntil"));
        voucher.setStatus(rs.getBoolean("Status"));
        int maxUsage = rs.getInt("MaxUsage");
        voucher.setMaxUsage(rs.wasNull() ? 0 : maxUsage);
        int usedCount = 0;
        try {
            usedCount = rs.getInt("UsedCount");
            if (rs.wasNull()) {
                usedCount = 0;
            }
        } catch (Exception ignored) {
            usedCount = 0;
        }
        voucher.setUsedCount(usedCount);
        int remaining = Math.max((voucher.getMaxUsage() != null ? voucher.getMaxUsage() : 0) - usedCount, 0);
        voucher.setRemainingCount(remaining);
        return voucher;
    }

    public boolean hasUserUsedVoucher(int studentId, int voucherId) {
        boolean hasUsed = false;
        String sql = "SELECT 1 FROM Enrollment WHERE StudentID = ? AND VoucherID = ? "
                + "UNION "
                + "SELECT 1 FROM Payment p JOIN Enrollment e ON p.EnrollmentID = e.EnrollmentID "
                + "WHERE e.StudentID = ? AND p.VoucherID = ? AND p.Status != 'Rejected'";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, studentId);
            ps.setInt(2, voucherId);
            ps.setInt(3, studentId);
            ps.setInt(4, voucherId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                hasUsed = true;
            }
        } catch (Exception e) {
            System.out.println("Lỗi hasUserUsedVoucher: " + e.getMessage());
        }
        return hasUsed;
    }

    public double calculateDiscountAmount(Voucher voucher, double originalPrice) {
        if (voucher == null || !voucher.isStatus()) {
            return 0;
        }

        double discountAmount = 0;
        if (voucher.getDiscountAmount() != null && voucher.getDiscountAmount().doubleValue() > 0) {
            discountAmount = voucher.getDiscountAmount().doubleValue();
        } else if (voucher.getDiscountPercent() > 0) {
            discountAmount = originalPrice * (voucher.getDiscountPercent() / 100.0);
        }

        return discountAmount;
    }

    public int getVoucherRemainingUsage(int voucherId) {
        String sql = "SELECT COALESCE(v.MaxUsage, 1) AS MaxUsage, "
                + "ISNULL((SELECT COUNT(*) FROM Payment p "
                + "        WHERE p.VoucherID = v.VoucherID "
                + "          AND p.Status <> 'Rejected'), 0) AS UsedCount "
                + "FROM Voucher v WHERE v.VoucherID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, voucherId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int maxUsage = rs.getInt("MaxUsage");
                int used = rs.getInt("UsedCount");
                return Math.max(maxUsage - used, 0);
            }
        } catch (Exception e) {
            System.out.println("Fail to get voucher remaining usage: " + e.getMessage());
        }
        return 0;
    }

    public boolean isVoucherUsageAvailable(int voucherId) {
        return getVoucherRemainingUsage(voucherId) > 0;
    }

    public Object[] getVoucherInventoryReportSummary(Date fromDate, Date toDate) {
        Object[] summary = new Object[]{0, 0, 0, 0};

        String sql = "SELECT "
                + "COUNT(*) AS TotalVouchers, "
                + "SUM(CASE WHEN v.Status = 1 THEN 1 ELSE 0 END) AS ActiveVouchers, "
                + "SUM(ISNULL(v.MaxUsage, 0)) AS TotalIssued, "
                + "SUM(CASE "
                + "        WHEN ISNULL(v.MaxUsage, 0) - ISNULL(u.UsedCount, 0) > 0 "
                + "        THEN ISNULL(v.MaxUsage, 0) - ISNULL(u.UsedCount, 0) "
                + "        ELSE 0 "
                + "    END) AS TotalRemaining "
                + "FROM Voucher v "
                + "LEFT JOIN ( "
                + "    SELECT VoucherID, COUNT(*) AS UsedCount "
                + "    FROM Payment "
                + "    WHERE VoucherID IS NOT NULL AND Status <> 'Rejected' "
                + "    GROUP BY VoucherID "
                + ") u ON u.VoucherID = v.VoucherID "
                + "WHERE (v.ValidUntil IS NULL OR (v.ValidUntil >= ? AND v.ValidUntil <= ?))";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, fromDate);
            ps.setDate(2, toDate);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    summary[0] = rs.getInt("TotalVouchers");
                    summary[1] = rs.getInt("ActiveVouchers");
                    summary[2] = rs.getInt("TotalIssued");
                    summary[3] = rs.getInt("TotalRemaining");
                }
            }
        } catch (SQLException e) {
            System.out.println("Fail to get voucher inventory report summary: " + e.getMessage());
        }

        return summary;
    }

    public List<Object[]> getVoucherInventoryReport(Date fromDate, Date toDate) {
        List<Object[]> rows = new ArrayList<>();

        String sql = "SELECT v.Code, v.DiscountAmount, v.DiscountPercent, "
                + "ISNULL(v.MaxUsage, 0) AS IssuedCount, "
                + "ISNULL(u.UsedCount, 0) AS UsedCount, "
                + "CASE "
                + "    WHEN ISNULL(v.MaxUsage, 0) - ISNULL(u.UsedCount, 0) > 0 "
                + "    THEN ISNULL(v.MaxUsage, 0) - ISNULL(u.UsedCount, 0) "
                + "    ELSE 0 "
                + "END AS RemainingCount, "
                + "v.ValidUntil, v.Status "
                + "FROM Voucher v "
                + "LEFT JOIN ( "
                + "    SELECT VoucherID, COUNT(*) AS UsedCount "
                + "    FROM Payment "
                + "    WHERE VoucherID IS NOT NULL AND Status <> 'Rejected' "
                + "    GROUP BY VoucherID "
                + ") u ON u.VoucherID = v.VoucherID "
                + "WHERE (v.ValidUntil IS NULL OR (v.ValidUntil >= ? AND v.ValidUntil <= ?)) "
                + "ORDER BY v.ValidUntil, v.Code";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, fromDate);
            ps.setDate(2, toDate);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Object[]{
                        rs.getString("Code"),
                        rs.getBigDecimal("DiscountAmount"),
                        rs.getDouble("DiscountPercent"),
                        rs.getInt("IssuedCount"),
                        rs.getInt("UsedCount"),
                        rs.getInt("RemainingCount"),
                        rs.getDate("ValidUntil"),
                        rs.getBoolean("Status")
                    });
                }
            }
        } catch (SQLException e) {
            System.out.println("Fail to get voucher inventory report: " + e.getMessage());
        }

        return rows;
    }

    public List<Object[]> getVoucherInventoryMonthlyReport(Date fromDate, Date toDate) {
        List<Object[]> rows = new ArrayList<>();

        String sql = "WITH month_cte AS ( "
                + "    SELECT CAST(DATEFROMPARTS(YEAR(?), MONTH(?), 1) AS DATETIME) AS MonthStart "
                + "    UNION ALL "
                + "    SELECT DATEADD(MONTH, 1, MonthStart) "
                + "    FROM month_cte "
                + "    WHERE DATEADD(MONTH, 1, MonthStart) <= DATEFROMPARTS(YEAR(?), MONTH(?), 1) "
                + "), voucher_agg AS ( "
                + "    SELECT DATEFROMPARTS(YEAR(v.ValidUntil), MONTH(v.ValidUntil), 1) AS MonthStart, "
                + "           SUM(ISNULL(v.MaxUsage, 0)) AS IssuedCount, "
                + "           SUM(ISNULL(u.UsedCount, 0)) AS UsedCount, "
                + "           SUM(CASE "
                + "                   WHEN ISNULL(v.MaxUsage, 0) - ISNULL(u.UsedCount, 0) > 0 "
                + "                   THEN ISNULL(v.MaxUsage, 0) - ISNULL(u.UsedCount, 0) "
                + "                   ELSE 0 "
                + "               END) AS RemainingCount "
                + "    FROM Voucher v "
                + "    LEFT JOIN ( "
                + "        SELECT VoucherID, COUNT(*) AS UsedCount "
                + "        FROM Payment "
                + "        WHERE VoucherID IS NOT NULL AND Status <> 'Rejected' "
                + "        GROUP BY VoucherID "
                + "    ) u ON u.VoucherID = v.VoucherID "
                + "    WHERE v.ValidUntil IS NOT NULL AND v.ValidUntil >= ? AND v.ValidUntil <= ? "
                + "    GROUP BY DATEFROMPARTS(YEAR(v.ValidUntil), MONTH(v.ValidUntil), 1) "
                + ") "
                + "SELECT FORMAT(m.MonthStart, 'MM/yyyy') AS MonthLabel, "
                + "       ISNULL(v.IssuedCount, 0) AS IssuedCount, "
                + "       ISNULL(v.UsedCount, 0) AS UsedCount, "
                + "       ISNULL(v.RemainingCount, 0) AS RemainingCount "
                + "FROM month_cte m "
                + "LEFT JOIN voucher_agg v ON v.MonthStart = m.MonthStart "
                + "ORDER BY m.MonthStart "
                + "OPTION (MAXRECURSION 120)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, fromDate);
            ps.setDate(2, fromDate);
            ps.setDate(3, toDate);
            ps.setDate(4, toDate);
            ps.setDate(5, fromDate);
            ps.setDate(6, toDate);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Object[]{
                        rs.getString("MonthLabel"),
                        rs.getInt("IssuedCount"),
                        rs.getInt("UsedCount"),
                        rs.getInt("RemainingCount")
                    });
                }
            }
        } catch (SQLException e) {
            System.out.println("Fail to get voucher inventory monthly report: " + e.getMessage());
        }

        return rows;
    }

    public Object[] getVoucherReportSummary(LocalDateTime fromDate, LocalDateTime toDate) {
        Object[] summary = new Object[]{0, 0, 0, java.math.BigDecimal.ZERO};

        String sql = "SELECT "
                + "COUNT(*) AS TotalUsages, "
                + "COUNT(DISTINCT COALESCE(p.VoucherID, e.VoucherID)) AS UniqueVouchersUsed, "
                + "COUNT(DISTINCT e.StudentID) AS StudentsUsingVoucher, "
                + "ISNULL(SUM(CAST(p.Amount AS DECIMAL(18,2))), 0) AS VoucherRevenue "
                + "FROM Payment p "
                + "JOIN Enrollment e ON p.EnrollmentID = e.EnrollmentID "
                + "WHERE COALESCE(p.VoucherID, e.VoucherID) IS NOT NULL "
                + "AND p.PaymentDate >= ? AND p.PaymentDate <= ? "
                + "AND ISNULL(p.Status, '') <> 'Rejected'";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(fromDate));
            ps.setTimestamp(2, Timestamp.valueOf(toDate));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    summary[0] = rs.getInt("TotalUsages");
                    summary[1] = rs.getInt("UniqueVouchersUsed");
                    summary[2] = rs.getInt("StudentsUsingVoucher");
                    summary[3] = rs.getBigDecimal("VoucherRevenue");
                }
            }
        } catch (SQLException e) {
            System.out.println("Fail to get voucher report summary: " + e.getMessage());
        }

        return summary;
    }

    public List<Object[]> getVoucherUsageReport(LocalDateTime fromDate, LocalDateTime toDate) {
        List<Object[]> rows = new ArrayList<>();

        String sql = "SELECT FORMAT(CAST(p.PaymentDate AS DATE), 'dd/MM/yyyy') AS DayLabel, "
                + "v.Code AS VoucherCode, "
                + "COUNT(*) AS UsageCount, "
                + "COUNT(DISTINCT e.StudentID) AS StudentCount, "
                + "SUM(CAST(p.Amount AS DECIMAL(18,2))) AS Revenue "
                + "FROM Payment p "
                + "JOIN Enrollment e ON p.EnrollmentID = e.EnrollmentID "
                + "JOIN Voucher v ON COALESCE(p.VoucherID, e.VoucherID) = v.VoucherID "
                + "WHERE COALESCE(p.VoucherID, e.VoucherID) IS NOT NULL "
                + "AND p.PaymentDate >= ? AND p.PaymentDate <= ? "
                + "AND ISNULL(p.Status, '') <> 'Rejected' "
                + "GROUP BY CAST(p.PaymentDate AS DATE), v.Code "
                + "ORDER BY CAST(p.PaymentDate AS DATE), v.Code";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(fromDate));
            ps.setTimestamp(2, Timestamp.valueOf(toDate));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Object[]{
                        rs.getString("DayLabel"),
                        rs.getString("VoucherCode"),
                        rs.getInt("UsageCount"),
                        rs.getInt("StudentCount"),
                        rs.getBigDecimal("Revenue")
                    });
                }
            }
        } catch (SQLException e) {
            System.out.println("Fail to get voucher usage report: " + e.getMessage());
        }

        return rows;
    }

    public List<Object[]> getVoucherDailyTotalsReport(LocalDateTime fromDate, LocalDateTime toDate) {
        List<Object[]> rows = new ArrayList<>();

        String sql = "SELECT FORMAT(CAST(p.PaymentDate AS DATE), 'dd/MM/yyyy') AS DayLabel, "
                + "COUNT(*) AS UsageCount, "
                + "COUNT(DISTINCT e.StudentID) AS StudentCount "
                + "FROM Payment p "
                + "JOIN Enrollment e ON p.EnrollmentID = e.EnrollmentID "
                + "WHERE COALESCE(p.VoucherID, e.VoucherID) IS NOT NULL "
                + "AND p.PaymentDate >= ? AND p.PaymentDate <= ? "
                + "AND ISNULL(p.Status, '') <> 'Rejected' "
                + "GROUP BY CAST(p.PaymentDate AS DATE) "
                + "ORDER BY CAST(p.PaymentDate AS DATE)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(fromDate));
            ps.setTimestamp(2, Timestamp.valueOf(toDate));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Object[]{
                        rs.getString("DayLabel"),
                        rs.getInt("UsageCount"),
                        rs.getInt("StudentCount")
                    });
                }
            }
        } catch (SQLException e) {
            System.out.println("Fail to get voucher daily totals report: " + e.getMessage());
        }

        return rows;
    }

    public Object[] getVoucherReportDiagnostics(LocalDateTime fromDate, LocalDateTime toDate) {
        Object[] diagnostics = new Object[]{0, 0, 0, 0};

        String sql = "SELECT "
                + "COUNT(*) AS TotalPaymentsInRange, "
                + "SUM(CASE WHEN p.VoucherID IS NOT NULL THEN 1 ELSE 0 END) AS PaymentVoucherCount, "
                + "SUM(CASE WHEN e.VoucherID IS NOT NULL THEN 1 ELSE 0 END) AS EnrollmentVoucherCount, "
                + "SUM(CASE WHEN COALESCE(p.VoucherID, e.VoucherID) IS NOT NULL THEN 1 ELSE 0 END) AS EffectiveVoucherCount "
                + "FROM Payment p "
                + "JOIN Enrollment e ON p.EnrollmentID = e.EnrollmentID "
                + "WHERE p.PaymentDate >= ? AND p.PaymentDate <= ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(fromDate));
            ps.setTimestamp(2, Timestamp.valueOf(toDate));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    diagnostics[0] = rs.getInt("TotalPaymentsInRange");
                    diagnostics[1] = rs.getInt("PaymentVoucherCount");
                    diagnostics[2] = rs.getInt("EnrollmentVoucherCount");
                    diagnostics[3] = rs.getInt("EffectiveVoucherCount");
                }
            }
        } catch (SQLException e) {
            System.out.println("Fail to get voucher report diagnostics: " + e.getMessage());
        }

        return diagnostics;
    }

    public List<Object[]> getVoucherReportStatusBreakdown(LocalDateTime fromDate, LocalDateTime toDate) {
        List<Object[]> rows = new ArrayList<>();

        String sql = "SELECT ISNULL(NULLIF(LTRIM(RTRIM(p.Status)), ''), 'NULL') AS PaymentStatus, "
                + "COUNT(*) AS PaymentCount "
                + "FROM Payment p "
                + "WHERE p.PaymentDate >= ? AND p.PaymentDate <= ? "
                + "GROUP BY ISNULL(NULLIF(LTRIM(RTRIM(p.Status)), ''), 'NULL') "
                + "ORDER BY PaymentCount DESC, PaymentStatus";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(fromDate));
            ps.setTimestamp(2, Timestamp.valueOf(toDate));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Object[]{rs.getString("PaymentStatus"), rs.getInt("PaymentCount")});
                }
            }
        } catch (SQLException e) {
            System.out.println("Fail to get voucher report status breakdown: " + e.getMessage());
        }

        return rows;
    }

    public Voucher getVoucherByStudentAndClass(int studentId, int classId) {

        String sql = "SELECT TOP 1 v.* "
                + "FROM Enrollment e "
                + "JOIN Payment p ON e.EnrollmentID = p.EnrollmentID "
                + "JOIN Voucher v ON p.VoucherID = v.VoucherID "
                + "WHERE e.StudentID = ? "
                + "AND e.ClassID = ? "
                + "AND p.Status = 'Approved' "
                + "ORDER BY p.PaymentDate DESC";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, studentId);
            ps.setInt(2, classId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapVoucher(rs);
            }

        } catch (Exception e) {
            System.out.println("Fail get voucher by student & class: " + e.getMessage());
        }

        return null;
    }
}
