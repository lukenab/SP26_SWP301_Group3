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
import model.Consultation;
import model.Course;
import model.Lead;
import utils.DBContext;

/**
 *
 * @author Legion
 */
public class LeadDAO extends DBContext {

    CourseDAO courseDAO = new CourseDAO();

    public List<Lead> getAllLeads() {

        List<Lead> list = new ArrayList<>();

        syncConvertedStatusForAllLeads();

        String sql = "SELECT l.LeadID, l.FullName, l.Email, l.Phone, "
                + "l.InterestedCourseID, co.CourseName, "
                + "l.Status, "
                + "l.CreateDate, latest.Note, COALESCE(latest.ConsultDate, l.CreateDate) AS LastUpdatedDate "
                + "FROM Lead l "
                + "LEFT JOIN Course co ON l.InterestedCourseID = co.CourseID "
                + "OUTER APPLY ( "
                + "    SELECT TOP 1 c.Note, c.ConsultDate "
                + "    FROM Consultation c "
                + "    WHERE c.LeadID = l.LeadID "
                + "    ORDER BY c.ConsultDate DESC, c.ConsultationID DESC "
                + ") latest";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int leadID = rs.getInt("LeadID");
                String fullName = rs.getString("FullName");
                String email = rs.getString("Email");
                String phone = rs.getString("Phone");

                int interestedCourseID = rs.getInt("InterestedCourseID");
                String courseName = rs.getString("CourseName");

                String status = rs.getString("Status");

                Timestamp ts = rs.getTimestamp("CreateDate");
                LocalDateTime createDate = null;
                if (ts != null) {
                    createDate = ts.toLocalDateTime();
                }

                String note = rs.getString("Note");
                Timestamp updatedTs = rs.getTimestamp("LastUpdatedDate");
                LocalDateTime lastUpdatedDate = null;
                if (updatedTs != null) {
                    lastUpdatedDate = updatedTs.toLocalDateTime();
                }

                Course course = null;
                if (courseName != null) {
                    course = new Course();
                    course.setCourseId(interestedCourseID);
                    course.setCourseName(courseName);
                }

                Lead lead = new Lead(leadID, fullName, email, phone, interestedCourseID, course, status, createDate);

                lead.setNote(note);
                lead.setLastUpdatedDate(lastUpdatedDate);

                list.add(lead);
            }

        } catch (SQLException e) {
            System.out.println("Fail to get all Leads: " + e.getMessage());
        }

        return list;
    }

    public List<Lead> searchAndFilterLeads(String searchQuery, String statusFilter, Integer interestCourseId,
            LocalDateTime fromDate, LocalDateTime toDate) {

        List<Lead> list = new ArrayList<>();

        syncConvertedStatusForAllLeads();

        StringBuilder sql = new StringBuilder("SELECT l.LeadID, l.FullName, l.Email, l.Phone, "
                + "l.InterestedCourseID, co.CourseName, "
                + "l.Status, "
                + "l.CreateDate, latest.Note, COALESCE(latest.ConsultDate, l.CreateDate) AS LastUpdatedDate "
                + "FROM Lead l "
                + "LEFT JOIN Course co ON l.InterestedCourseID = co.CourseID "
                + "OUTER APPLY ( "
                + "    SELECT TOP 1 c.Note, c.ConsultDate "
                + "    FROM Consultation c "
                + "    WHERE c.LeadID = l.LeadID "
                + "    ORDER BY c.ConsultDate DESC, c.ConsultationID DESC "
                + ") latest "
                + "WHERE 1=1 ");

        boolean hasSearch = searchQuery != null && !searchQuery.trim().isEmpty();
        boolean hasStatus = statusFilter != null && !statusFilter.trim().isEmpty()
                && !"all".equalsIgnoreCase(statusFilter.trim());
        boolean hasInterest = interestCourseId != null && interestCourseId > 0;
        boolean hasFromDate = fromDate != null;
        boolean hasToDate = toDate != null;

        if (hasSearch) {
            sql.append("AND (l.FullName LIKE ? OR l.Email LIKE ?) ");
        }
        if (hasStatus) {
            sql.append("AND l.Status = ? ");
        }
        if (hasInterest) {
            sql.append("AND l.InterestedCourseID = ? ");
        }
        if (hasFromDate) {
            sql.append("AND l.CreateDate >= ? ");
        }
        if (hasToDate) {
            sql.append("AND l.CreateDate <= ? ");
        }

        try {
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            int index = 1;

            if (hasSearch) {
                String keyword = "%" + searchQuery.trim() + "%";
                ps.setString(index++, keyword);
                ps.setString(index++, keyword);
            }
            if (hasStatus) {
                ps.setString(index++, statusFilter.trim());
            }
            if (hasInterest) {
                ps.setInt(index++, interestCourseId);
            }
            if (hasFromDate) {
                ps.setTimestamp(index++, Timestamp.valueOf(fromDate));
            }
            if (hasToDate) {
                ps.setTimestamp(index++, Timestamp.valueOf(toDate));
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int leadID = rs.getInt("LeadID");
                String fullName = rs.getString("FullName");
                String email = rs.getString("Email");
                String phone = rs.getString("Phone");

                int interestedCourseID = rs.getInt("InterestedCourseID");
                String courseName = rs.getString("CourseName");

                String status = rs.getString("Status");

                Timestamp ts = rs.getTimestamp("CreateDate");
                LocalDateTime createDate = null;
                if (ts != null) {
                    createDate = ts.toLocalDateTime();
                }

                String note = rs.getString("Note");
                Timestamp updatedTs = rs.getTimestamp("LastUpdatedDate");
                LocalDateTime lastUpdatedDate = null;
                if (updatedTs != null) {
                    lastUpdatedDate = updatedTs.toLocalDateTime();
                }

                Course course = null;
                if (courseName != null) {
                    course = new Course();
                    course.setCourseId(interestedCourseID);
                    course.setCourseName(courseName);
                }

                Lead lead = new Lead(leadID, fullName, email, phone, interestedCourseID, course, status, createDate);
                lead.setNote(note);
                lead.setLastUpdatedDate(lastUpdatedDate);
                list.add(lead);
            }

        } catch (SQLException e) {
            System.out.println("Fail to search and filter leads: " + e.getMessage());
        }

        return list;
    }

    public List<Lead> searchAndFilterLeadsPaged(String searchQuery, String statusFilter, Integer interestCourseId,
            LocalDateTime fromDate, LocalDateTime toDate, int page, int pageSize) {

        List<Lead> list = new ArrayList<>();

        syncConvertedStatusForAllLeads();

        StringBuilder sql = new StringBuilder("SELECT l.LeadID, l.FullName, l.Email, l.Phone, "
                + "l.InterestedCourseID, co.CourseName, "
                + "l.Status, "
                + "l.CreateDate, latest.Note, COALESCE(latest.ConsultDate, l.CreateDate) AS LastUpdatedDate "
                + "FROM Lead l "
                + "LEFT JOIN Course co ON l.InterestedCourseID = co.CourseID "
                + "OUTER APPLY ( "
                + "    SELECT TOP 1 c.Note, c.ConsultDate "
                + "    FROM Consultation c "
                + "    WHERE c.LeadID = l.LeadID "
                + "    ORDER BY c.ConsultDate DESC, c.ConsultationID DESC "
                + ") latest "
                + "WHERE 1=1 ");

        boolean hasSearch = searchQuery != null && !searchQuery.trim().isEmpty();
        boolean hasStatus = statusFilter != null && !statusFilter.trim().isEmpty()
                && !"all".equalsIgnoreCase(statusFilter.trim());
        boolean hasInterest = interestCourseId != null && interestCourseId > 0;
        boolean hasFromDate = fromDate != null;
        boolean hasToDate = toDate != null;

        if (hasSearch) {
            sql.append("AND (l.FullName LIKE ? OR l.Email LIKE ?) ");
        }
        if (hasStatus) {
            sql.append("AND l.Status = ? ");
        }
        if (hasInterest) {
            sql.append("AND l.InterestedCourseID = ? ");
        }
        if (hasFromDate) {
            sql.append("AND l.CreateDate >= ? ");
        }
        if (hasToDate) {
            sql.append("AND l.CreateDate <= ? ");
        }

        sql.append("ORDER BY COALESCE(latest.ConsultDate, l.CreateDate) DESC, l.LeadID DESC ");
        sql.append("OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try {
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            int index = 1;

            if (hasSearch) {
                String keyword = "%" + searchQuery.trim() + "%";
                ps.setString(index++, keyword);
                ps.setString(index++, keyword);
            }
            if (hasStatus) {
                ps.setString(index++, statusFilter.trim());
            }
            if (hasInterest) {
                ps.setInt(index++, interestCourseId);
            }
            if (hasFromDate) {
                ps.setTimestamp(index++, Timestamp.valueOf(fromDate));
            }
            if (hasToDate) {
                ps.setTimestamp(index++, Timestamp.valueOf(toDate));
            }

            int safePage = Math.max(1, page);
            int safePageSize = Math.max(1, pageSize);
            int offset = (safePage - 1) * safePageSize;
            ps.setInt(index++, offset);
            ps.setInt(index++, safePageSize);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int leadID = rs.getInt("LeadID");
                String fullName = rs.getString("FullName");
                String email = rs.getString("Email");
                String phone = rs.getString("Phone");

                int interestedCourseID = rs.getInt("InterestedCourseID");
                String courseName = rs.getString("CourseName");
                String status = rs.getString("Status");

                Timestamp ts = rs.getTimestamp("CreateDate");
                LocalDateTime createDate = null;
                if (ts != null) {
                    createDate = ts.toLocalDateTime();
                }

                String note = rs.getString("Note");
                Timestamp updatedTs = rs.getTimestamp("LastUpdatedDate");
                LocalDateTime lastUpdatedDate = null;
                if (updatedTs != null) {
                    lastUpdatedDate = updatedTs.toLocalDateTime();
                }

                Course course = null;
                if (courseName != null) {
                    course = new Course();
                    course.setCourseId(interestedCourseID);
                    course.setCourseName(courseName);
                }

                Lead lead = new Lead(leadID, fullName, email, phone, interestedCourseID, course, status, createDate);
                lead.setNote(note);
                lead.setLastUpdatedDate(lastUpdatedDate);
                list.add(lead);
            }
        } catch (SQLException e) {
            System.out.println("Fail to search and filter leads with paging: " + e.getMessage());
        }

        return list;
    }

    public int countLeadsByFilters(String searchQuery, String statusFilter, Integer interestCourseId,
            LocalDateTime fromDate, LocalDateTime toDate) {
        syncConvertedStatusForAllLeads();

        StringBuilder sql = new StringBuilder("SELECT COUNT(*) AS TotalCount FROM Lead l WHERE 1=1 ");

        boolean hasSearch = searchQuery != null && !searchQuery.trim().isEmpty();
        boolean hasStatus = statusFilter != null && !statusFilter.trim().isEmpty()
                && !"all".equalsIgnoreCase(statusFilter.trim());
        boolean hasInterest = interestCourseId != null && interestCourseId > 0;
        boolean hasFromDate = fromDate != null;
        boolean hasToDate = toDate != null;

        if (hasSearch) {
            sql.append("AND (l.FullName LIKE ? OR l.Email LIKE ?) ");
        }
        if (hasStatus) {
            sql.append("AND l.Status = ? ");
        }
        if (hasInterest) {
            sql.append("AND l.InterestedCourseID = ? ");
        }
        if (hasFromDate) {
            sql.append("AND l.CreateDate >= ? ");
        }
        if (hasToDate) {
            sql.append("AND l.CreateDate <= ? ");
        }

        try {
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            int index = 1;

            if (hasSearch) {
                String keyword = "%" + searchQuery.trim() + "%";
                ps.setString(index++, keyword);
                ps.setString(index++, keyword);
            }
            if (hasStatus) {
                ps.setString(index++, statusFilter.trim());
            }
            if (hasInterest) {
                ps.setInt(index++, interestCourseId);
            }
            if (hasFromDate) {
                ps.setTimestamp(index++, Timestamp.valueOf(fromDate));
            }
            if (hasToDate) {
                ps.setTimestamp(index++, Timestamp.valueOf(toDate));
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("TotalCount");
            }
        } catch (SQLException e) {
            System.out.println("Fail to count leads by filters: " + e.getMessage());
        }

        return 0;
    }

    public Lead getLeadByID(int id) {

        syncConvertedStatusForLead(id);

        String sql = "SELECT l.*, latest.Note, co.CourseName, COALESCE(latest.ConsultDate, l.CreateDate) AS LastUpdatedDate "
                + "FROM Lead l "
                + "LEFT JOIN Course co ON l.InterestedCourseID = co.CourseID "
                + "OUTER APPLY ( "
                + "    SELECT TOP 1 c.Note, c.ConsultDate "
                + "    FROM Consultation c "
                + "    WHERE c.LeadID = l.LeadID "
                + "    ORDER BY c.ConsultDate DESC, c.ConsultationID DESC "
                + ") latest "
                + "WHERE l.LeadID = ?";

        Lead lead = null;

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                int leadID = rs.getInt("LeadID");
                String fullName = rs.getString("FullName");
                String email = rs.getString("Email");
                String phone = rs.getString("Phone");
                int interestedCourseID = rs.getInt("InterestedCourseID");
                String status = rs.getString("Status");
                LocalDateTime createDate = rs.getTimestamp("CreateDate").toLocalDateTime();
                String note = rs.getString("Note");
                String courseName = rs.getString("CourseName");
                Timestamp updatedTs = rs.getTimestamp("LastUpdatedDate");
                LocalDateTime lastUpdatedDate = null;
                if (updatedTs != null) {
                    lastUpdatedDate = updatedTs.toLocalDateTime();
                }

                Course course = null;
                if (courseName != null) {
                    course = new Course();
                    course.setCourseId(interestedCourseID);
                    course.setCourseName(courseName);
                }

                lead = new Lead(leadID, fullName, email, phone,
                        interestedCourseID, course, status, createDate);

                lead.setNote(note);
                lead.setLastUpdatedDate(lastUpdatedDate);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lead;
    }

    public List<Consultation> getConsultationHistoryByLeadId(int leadId) {
        List<Consultation> history = new ArrayList<>();
        String sql = "SELECT c.ConsultationID, c.LeadID, c.SaleID, c.Note, c.ConsultDate, u.FullName AS SaleName "
                + "FROM Consultation c "
                + "LEFT JOIN [User] u ON c.SaleID = u.UserID "
                + "WHERE c.LeadID = ? "
                + "ORDER BY c.ConsultDate DESC, c.ConsultationID DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, leadId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Consultation consultation = new Consultation();
                    consultation.setConsultationId(rs.getInt("ConsultationID"));

                    int saleIdValue = rs.getInt("SaleID");
                    if (!rs.wasNull()) {
                        consultation.setSaleId(saleIdValue);
                    }

                    consultation.setSaleName(rs.getString("SaleName"));
                    consultation.setNote(rs.getString("Note"));

                    Timestamp consultTs = rs.getTimestamp("ConsultDate");
                    if (consultTs != null) {
                        consultation.setConsultation(consultTs.toLocalDateTime());
                    }

                    history.add(consultation);
                }
            }
        } catch (SQLException e) {
            System.out.println("Fail to get consultation history: " + e.getMessage());
        }

        return history;
    }

    public boolean insertConsultationLog(int leadId, Integer saleId, String note, LocalDateTime consultDate) {
        String sql = "INSERT INTO Consultation (LeadID, SaleID, Note, ConsultDate) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, leadId);
            if (saleId == null) {
                ps.setNull(2, java.sql.Types.INTEGER);
            } else {
                ps.setInt(2, saleId);
            }
            ps.setString(3, note);
            LocalDateTime safeDate = consultDate == null ? LocalDateTime.now() : consultDate;
            ps.setTimestamp(4, Timestamp.valueOf(safeDate));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Fail to insert consultation log: " + e.getMessage());
        }

        return false;
    }

    public boolean isEmailExists(String email) {
        String sql = "SELECT 1 FROM Lead WHERE Email = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Fail to check existing lead email: " + e.getMessage());
        }
        return false;
    }

    public boolean isPhoneExists(String phone) {
        String sql = "SELECT 1 FROM Lead WHERE Phone = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Fail to check existing lead phone: " + e.getMessage());
        }
        return false;
    }

    private void syncConvertedStatusForAllLeads() {
        String sql = "UPDATE l "
                + "SET l.Status = 'Converted' "
                + "FROM Lead l "
                + "WHERE l.Status <> 'Converted' "
                + "AND EXISTS ("
                + "    SELECT 1 FROM [User] u "
                + "    WHERE u.Email = l.Email AND u.RoleID = 5"
                + ")";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Fail to sync converted status for all leads: " + e.getMessage());
        }
    }

    private void syncConvertedStatusForLead(int leadId) {
        String sql = "UPDATE l "
                + "SET l.Status = 'Converted' "
                + "FROM Lead l "
                + "WHERE l.LeadID = ? "
                + "AND l.Status <> 'Converted' "
                + "AND EXISTS ("
                + "    SELECT 1 FROM [User] u "
                + "    WHERE u.Email = l.Email AND u.RoleID = 5"
                + ")";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, leadId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Fail to sync converted status for lead: " + e.getMessage());
        }
    }

    public void insertLead(Lead lead) {

        String sqlLead = "INSERT INTO Lead (FullName, Email, Phone, InterestedCourseID, Status, CreateDate) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        String sqlConsult = "INSERT INTO Consultation (LeadID, SaleID, Note, ConsultDate) VALUES (?, ?, ?, ?)";

        try {
            conn.setAutoCommit(false);

            PreparedStatement ps = conn.prepareStatement(sqlLead, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setString(1, lead.getFullName());
            ps.setString(2, lead.getEmail());
            ps.setString(3, lead.getPhone());
            ps.setInt(4, lead.getInterestedCourseID());
            ps.setString(5, lead.getStatus());
            ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            int newLeadID = 0;

            if (rs.next()) {
                newLeadID = rs.getInt(1);
            }

            if (!isBlank(lead.getNote())) {
                PreparedStatement psConsult = conn.prepareStatement(sqlConsult);
                psConsult.setInt(1, newLeadID);
                psConsult.setNull(2, java.sql.Types.INTEGER);
                psConsult.setString(3, lead.getNote());
                psConsult.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                psConsult.executeUpdate();
            }

            conn.commit();

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    public boolean updateLead(int id, String fullName, String email, String phone,
            int interestedCourseID, String status, String note) {

        String sqlLead = "UPDATE Lead SET FullName = ?, Email = ?, Phone = ?, "
                + "InterestedCourseID = ?, Status = ? WHERE LeadID = ?";

        String sqlConsultInsert = "INSERT INTO Consultation (LeadID, SaleID, Note, ConsultDate) VALUES (?, ?, ?, ?)";

        try {
            conn.setAutoCommit(false);

            PreparedStatement ps1 = conn.prepareStatement(sqlLead);
            ps1.setString(1, fullName);
            ps1.setString(2, email);
            ps1.setString(3, phone);
            ps1.setInt(4, interestedCourseID);
            ps1.setString(5, status);
            ps1.setInt(6, id);
            ps1.executeUpdate();

            if (!isBlank(note)) {
                PreparedStatement ps2 = conn.prepareStatement(sqlConsultInsert);
                ps2.setInt(1, id);
                ps2.setNull(2, java.sql.Types.INTEGER);
                ps2.setString(3, note.trim());
                ps2.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                ps2.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
    }

    public void deleteLead(int id) {
        String sql = "UPDATE Lead SET Status = 'Inactive' WHERE LeadID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Fail to soft delete lead: " + e.getMessage());
        }
    }

    public void restoreLead(int id) {
        String sql = "UPDATE Lead SET Status = 'New' WHERE LeadID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Fail to restore lead: " + e.getMessage());
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static void main(String[] args) {
        LeadDAO dao = new LeadDAO();
        List<Lead> list = dao.getAllLeads();
        for (Lead lead : list) {
            System.out.println(lead);
        }
    }
}
