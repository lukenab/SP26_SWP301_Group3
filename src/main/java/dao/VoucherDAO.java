package dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Voucher;
import utils.DBContext;

public class VoucherDAO extends DBContext {

    public List<Voucher> getAllVoucher() {
        List<Voucher> list = new ArrayList<>();
        String sql = "SELECT VoucherID, Code, DiscountAmount, DiscountPercent, ValidUntil, Status "
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
        StringBuilder sql = new StringBuilder("SELECT VoucherID, Code, DiscountAmount, DiscountPercent, ValidUntil, Status "
                + "FROM Voucher WHERE 1=1 ");

        boolean hasSearch = searchQuery != null && !searchQuery.trim().isEmpty();
        boolean hasStatus = status != null && !status.trim().isEmpty() && !"all".equalsIgnoreCase(status.trim());

        if (hasSearch) {
            sql.append("AND Code LIKE ? ");
        }
        if (hasStatus) {
            sql.append("AND Status = ? ");
        }
        sql.append("ORDER BY VoucherID DESC");

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
        String sql = "SELECT VoucherID, Code, DiscountAmount, DiscountPercent, ValidUntil, Status "
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
        String sql = "SELECT VoucherID, Code, DiscountAmount, DiscountPercent, ValidUntil, Status "
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
        String sql = "SELECT VoucherID, Code, DiscountAmount, DiscountPercent, ValidUntil, Status "
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
        String sql = "INSERT INTO Voucher (Code, DiscountAmount, DiscountPercent, ValidUntil, Status) VALUES (?, ?, ?, ?, ?)";
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
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Fail to insert voucher: " + e.getMessage());
        }
    }

    public boolean updateVoucher(int id, String code, java.math.BigDecimal discountAmount, double discountPercent, Date validUntil, boolean status) {
        String sql = "UPDATE Voucher SET Code = ?, DiscountAmount = ?, DiscountPercent = ?, ValidUntil = ?, Status = ? "
                + "WHERE VoucherID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, code);
            ps.setBigDecimal(2, discountAmount);
            ps.setDouble(3, discountPercent);
            ps.setDate(4, validUntil);
            ps.setBoolean(5, status);
            ps.setInt(6, id);
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
}
