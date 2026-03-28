/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Classes;
import model.Course;
import model.Enrollment;
import model.Student;
import utils.DBContext;

/**
 *
 * @author Legion
 */
public class EnrollmentDAO extends DBContext {

    private static Boolean cachedHasClassMaxCapacityColumn;

    private boolean hasClassMaxCapacityColumn() {
        if (cachedHasClassMaxCapacityColumn != null) {
            return cachedHasClassMaxCapacityColumn;
        }
        String sql = "SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS "
                + "WHERE TABLE_NAME = 'Class' AND COLUMN_NAME = 'MaxCapacity'";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            cachedHasClassMaxCapacityColumn = rs.next();
            return cachedHasClassMaxCapacityColumn;
        } catch (Exception e) {
            System.out.println("Fail to detect Class.MaxCapacity column: " + e.getMessage());
        }
        return false;
    }

    private int getRemainingSlots(int classId, boolean lockRows) {
        String maxCapacityField = hasClassMaxCapacityColumn() ? "c.MaxCapacity" : "co.TotalSlots";
        String classHint = lockRows ? " WITH (UPDLOCK, HOLDLOCK)" : "";
        String enrollmentHint = lockRows ? " WITH (UPDLOCK, HOLDLOCK)" : "";
        String sql = "SELECT " + maxCapacityField + " AS MaxCapacity, COUNT(e.EnrollmentID) AS StudentCount "
                + "FROM Class c" + classHint + " "
                + "JOIN Course co ON c.CourseID = co.CourseID "
                + "LEFT JOIN Enrollment e" + enrollmentHint + " ON c.ClassID = e.ClassID "
                + "WHERE c.ClassID = ? "
                + "GROUP BY " + maxCapacityField;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Math.max(0, rs.getInt("MaxCapacity") - rs.getInt("StudentCount"));
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to get remaining slots: " + e.getMessage());
        }
        return 0;
    }

    public boolean isClassFull(int classId) {
        return getRemainingSlots(classId, false) <= 0;
    }

    public List<Enrollment> getAllEnrollment() {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT * FROM Enrollment";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int enrollmentId = rs.getInt("EnrollmentID");

                StudentDAO studentDAO = new StudentDAO();
                Student student = studentDAO.getStudentById(rs.getInt("StudentID"));

                ClassDAO classDAO = new ClassDAO();
                Classes cl = classDAO.getClassByID(rs.getInt("ClassID"));
                Date enrollDate = rs.getDate("EnrollDate");
                String status = rs.getString("Status");
                Double finalGrade = rs.getDouble("FinalGrade");

                Enrollment enrollment = new Enrollment(enrollmentId, student, cl, enrollDate, status, finalGrade);
                list.add(enrollment);
            }
        } catch (Exception e) {
            System.out.println("Fail to get all enrollment: " + e.getMessage());
        }
        return list;
    }

    public Enrollment getEnrollmentById(int id) {
        String sql = "SELECT * FROM Enrollment WHERE EnrollmentID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int enrollmentId = rs.getInt("EnrollmentID");

                StudentDAO studentDAO = new StudentDAO();
                Student student = studentDAO.getStudentById(rs.getInt("StudentID"));

                ClassDAO classDAO = new ClassDAO();
                Classes cl = classDAO.getClassByID(rs.getInt("ClassID"));
                Date enrollDate = rs.getDate("EnrollDate");
                String status = rs.getString("Status");
                Double finalGrade = rs.getDouble("FinalGrade");

                Enrollment enrollment = new Enrollment(enrollmentId, student, cl, enrollDate, status, finalGrade);
                return enrollment;
            }
        } catch (Exception e) {
            System.out.println("Fail to get enrollment by ID: " + e.getMessage());
        }
        return null;
    }

    public List<Object[]> getStudentsInClass(int classId) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT e.EnrollmentID, s.StudentID, u.FullName, u.Email, e.EnrollDate, e.Status, grade_summary.FinalGrade "
                + "FROM Enrollment e "
                + "JOIN Student s ON e.StudentID = s.StudentID "
                + "JOIN [User] u ON s.StudentID = u.UserID "
                + "LEFT JOIN ( "
                + "    SELECT g.EnrollmentID, "
                + "           SUM(g.Score * a.Weight) / NULLIF(SUM(a.Weight), 0) AS FinalGrade "
                + "    FROM Grade g "
                + "    JOIN Assessment a ON g.AssessmentID = a.AssessmentID "
                + "    GROUP BY g.EnrollmentID "
                + ") grade_summary ON e.EnrollmentID = grade_summary.EnrollmentID "
                + "WHERE e.ClassID = ? "
                + "ORDER BY u.FullName ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] row = new Object[7];
                    row[0] = rs.getInt("EnrollmentID");
                    row[1] = rs.getInt("StudentID");
                    row[2] = rs.getString("FullName");
                    row[3] = rs.getString("Email");
                    row[4] = rs.getDate("EnrollDate");
                    row[5] = rs.getString("Status");
                    row[6] = rs.getObject("FinalGrade");
                    list.add(row);
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to get students in class: " + e.getMessage());
        }
        return list;
    }

    public List<Object[]> getStudentsNotInClass(int classId) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT s.StudentID, u.FullName, u.Email, s.EnrollmentDate, "
                + "CASE "
                + "    WHEN EXISTS ( "
                + "        SELECT 1 "
                + "        FROM Enrollment e2 "
                + "        WHERE e2.StudentID = s.StudentID "
                + "          AND e2.Status IN ('Paid', 'Active', 'Completed') "
                + "    ) "
                + "    OR EXISTS ( "
                + "        SELECT 1 "
                + "        FROM Payment p "
                + "        JOIN Enrollment e3 ON p.EnrollmentID = e3.EnrollmentID "
                + "        WHERE e3.StudentID = s.StudentID "
                + "          AND p.Status IN ('Approved', 'Paid', 'Complete', 'Completed') "
                + "    ) "
                + "    THEN 'Paid' "
                + "    ELSE 'UnPaid' "
                + "END AS SuggestedStatus "
                + "FROM Student s "
                + "JOIN [User] u ON s.StudentID = u.UserID "
                + "WHERE NOT EXISTS ( "
                + "    SELECT 1 FROM Enrollment e "
                + "    WHERE e.ClassID = ? AND e.StudentID = s.StudentID "
                + ") "
                + "ORDER BY u.FullName ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] row = new Object[5];
                    row[0] = rs.getInt("StudentID");
                    row[1] = rs.getString("FullName");
                    row[2] = rs.getString("Email");
                    row[3] = rs.getDate("EnrollmentDate");
                    row[4] = rs.getString("SuggestedStatus");
                    list.add(row);
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to get students not in class: " + e.getMessage());
        }
        return list;
    }

    public boolean hasPaidStudent(int[] studentIds) {
        if (studentIds == null || studentIds.length == 0) {
            return false;
        }

        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < studentIds.length; i++) {
            if (i > 0) {
                placeholders.append(", ");
            }
            placeholders.append("?");
        }

        String sql = "SELECT TOP 1 1 "
                + "FROM Student s "
                + "WHERE s.StudentID IN (" + placeholders + ") "
                + "AND ( "
                + "    EXISTS ( "
                + "        SELECT 1 "
                + "        FROM Enrollment e "
                + "        WHERE e.StudentID = s.StudentID "
                + "          AND e.Status IN ('Paid', 'Active', 'Completed') "
                + "    ) "
                + "    OR EXISTS ( "
                + "        SELECT 1 "
                + "        FROM Payment p "
                + "        JOIN Enrollment e2 ON p.EnrollmentID = e2.EnrollmentID "
                + "        WHERE e2.StudentID = s.StudentID "
                + "          AND p.Status IN ('Approved', 'Paid', 'Complete', 'Completed') "
                + "    ) "
                + ")";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < studentIds.length; i++) {
                ps.setInt(i + 1, studentIds[i]);
            }

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            System.out.println("Fail to check paid students: " + e.getMessage());
        }
        return false;
    }

    public int addStudentsToClass(int classId, int[] studentIds, String enrollmentStatus) {
        if (studentIds == null || studentIds.length == 0) {
            return 0;
        }
        if (enrollmentStatus == null || enrollmentStatus.trim().isEmpty()) {
            enrollmentStatus = "UnPaid";
        }
        String sql = "INSERT INTO Enrollment (StudentID, ClassID, EnrollDate, Status, FinalGrade) "
                + "VALUES (?, ?, GETDATE(), ?, ?)";
        int insertedCount = 0;
        try {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (int studentId : studentIds) {
                    ps.setInt(1, studentId);
                    ps.setInt(2, classId);
                    ps.setString(3, enrollmentStatus);
                    ps.setDouble(4, 0);
                    insertedCount += ps.executeUpdate();
                }
            }
            conn.commit();
            return insertedCount;
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                System.out.println("Rollback failed: " + ex.getMessage());
            }
            System.out.println("Fail to add students to class: " + e.getMessage());
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (Exception e) {
                System.out.println("Fail to reset auto commit: " + e.getMessage());
            }
        }
        return 0;
    }

    public int removeStudentsFromClass(int classId, int[] studentIds) {
        if (studentIds == null || studentIds.length == 0) {
            return 0;
        }
        String findEnrollmentSql = "SELECT EnrollmentID FROM Enrollment WHERE ClassID = ? AND StudentID = ?";
        String deleteAttendanceSql = "DELETE FROM Attendance WHERE EnrollmentID = ?";
        String deleteGradeSql = "DELETE FROM Grade WHERE EnrollmentID = ?";
        String deleteFeedbackSql = "DELETE FROM Feedback WHERE EnrollmentID = ?";
        String deletePaymentSql = "DELETE FROM Payment WHERE EnrollmentID = ?";
        String deleteEnrollmentSql = "DELETE FROM Enrollment WHERE EnrollmentID = ?";
        int removedCount = 0;
        try {
            conn.setAutoCommit(false);
            try (PreparedStatement findEnrollment = conn.prepareStatement(findEnrollmentSql); PreparedStatement deleteAttendance = conn.prepareStatement(deleteAttendanceSql); PreparedStatement deleteGrade = conn.prepareStatement(deleteGradeSql); PreparedStatement deleteFeedback = conn.prepareStatement(deleteFeedbackSql); PreparedStatement deletePayment = conn.prepareStatement(deletePaymentSql); PreparedStatement deleteEnrollment = conn.prepareStatement(deleteEnrollmentSql)) {
                for (int studentId : studentIds) {
                    findEnrollment.setInt(1, classId);
                    findEnrollment.setInt(2, studentId);

                    try (ResultSet rs = findEnrollment.executeQuery()) {
                        while (rs.next()) {
                            int enrollmentId = rs.getInt("EnrollmentID");

                            deleteAttendance.setInt(1, enrollmentId);
                            deleteAttendance.executeUpdate();

                            deleteGrade.setInt(1, enrollmentId);
                            deleteGrade.executeUpdate();

                            deleteFeedback.setInt(1, enrollmentId);
                            deleteFeedback.executeUpdate();

                            deletePayment.setInt(1, enrollmentId);
                            deletePayment.executeUpdate();

                            deleteEnrollment.setInt(1, enrollmentId);
                            removedCount += deleteEnrollment.executeUpdate();
                        }
                    }
                }
            }
            conn.commit();
            return removedCount;
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                System.out.println("Rollback failed: " + ex.getMessage());
            }
            System.out.println("Fail to remove students from class: " + e.getMessage());
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (Exception e) {
                System.out.println("Fail to reset auto commit: " + e.getMessage());
            }
        }
        return 0;
    }

    public int getOrCreateEnrollment(int studentId, int classId) {
        int enrollmentId = -1;
        try {
            // BƯỚC CỨU CÁNH: Tự động thêm User vào bảng Student nếu chưa có (Tránh lỗi Khóa Ngoại)
            String checkStudent = "SELECT StudentID FROM Student WHERE StudentID = ?";
            PreparedStatement psStudent = conn.prepareStatement(checkStudent);
            psStudent.setInt(1, studentId);
            if (!psStudent.executeQuery().next()) {
                PreparedStatement psInsertStudent = conn.prepareStatement("INSERT INTO Student (StudentID, EnrollmentDate) VALUES (?, GETDATE())");
                psInsertStudent.setInt(1, studentId);
                psInsertStudent.executeUpdate();
            }

            // 1. Kiểm tra xem sinh viên đã đăng ký lớp này trước đó chưa
            String checkQuery = "SELECT EnrollmentID FROM Enrollment WHERE StudentID = ? AND ClassID = ?";
            PreparedStatement psCheck = conn.prepareStatement(checkQuery);
            psCheck.setInt(1, studentId);
            psCheck.setInt(2, classId);
            ResultSet rs = psCheck.executeQuery();

            if (rs.next()) {
                return rs.getInt("EnrollmentID"); // Trả về ID cũ nếu đã từng bấm đăng ký
            }

            // 2. Nếu chưa thì Insert tạo mới (Bổ sung FinalGrade = 0 để SQL không báo lỗi)
            if (getRemainingSlots(classId, true) <= 0) {
                return -1;
            }

            String insertQuery = "INSERT INTO Enrollment (StudentID, ClassID, EnrollDate, Status, FinalGrade) VALUES (?, ?, GETDATE(), 'Unpaid', 0)";
            PreparedStatement psInsert = conn.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            psInsert.setInt(1, studentId);
            psInsert.setInt(2, classId);
            psInsert.executeUpdate();

            ResultSet generatedKeys = psInsert.getGeneratedKeys();
            if (generatedKeys.next()) {
                enrollmentId = generatedKeys.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("Error at getOrCreateEnrollment: " + e.getMessage());
        }
        return enrollmentId;
    }

    public String checkEnrollmentStatus(int studentId, int classId) {
        String status = null;
        String sql = "SELECT Status FROM Enrollment WHERE StudentID = ? AND ClassID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, studentId);
            ps.setInt(2, classId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                status = rs.getString("Status");
            }
        } catch (Exception e) {
            System.out.println("Fail to check Enrollment Status: " + e.getMessage());
        }
        return status; // Trả về "Active", "Unpaid" hoặc null
    }

    public void updateEnrollmentVoucher(int enrollmentId, int voucherId) {
        String sql = "UPDATE Enrollment SET VoucherID = ? WHERE EnrollmentID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, voucherId);
            ps.setInt(2, enrollmentId);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Fail to update Enrollment Voucher: " + e.getMessage());
        }
    }

    // Hàm cập nhật trạng thái của Enrollment (ví dụ: từ Unpaid -> Active)
    public boolean updateEnrollmentStatus(int enrollmentId, String status) {
        String sql = "UPDATE Enrollment SET Status = ? WHERE EnrollmentID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, enrollmentId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Fail to update Enrollment Status: " + e.getMessage());
        }
        return false;
    }

    public int countEnrollmentsByClassAndStatus(int classId, String status) {
        String sql = "SELECT COUNT(*) AS Total "
                + "FROM Enrollment "
                + "WHERE ClassID = ? AND UPPER(LTRIM(RTRIM(Status))) = UPPER(LTRIM(RTRIM(?)))";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            ps.setString(2, status);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Total");
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to count enrollments by class and status: " + e.getMessage());
        }
        return 0;
    }

    public int countActivationBlockedEnrollments(int classId) {
        String sql = "SELECT COUNT(*) AS Total "
                + "FROM Enrollment "
                + "WHERE ClassID = ? "
                + "  AND UPPER(LTRIM(RTRIM(Status))) = 'UNPAID'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Total");
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to count activation-blocked enrollments: " + e.getMessage());
        }
        return 0;
    }

    public int promoteEligibleEnrollmentsToActive(int classId) {
        String sql = "UPDATE Enrollment "
                + "SET Status = 'Active' "
                + "WHERE ClassID = ? "
                + "AND UPPER(LTRIM(RTRIM(Status))) IN ('PAID', 'PENDING')";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            return ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Fail to promote eligible enrollments to active: " + e.getMessage());
        }
        return 0;
    }

    public boolean hasAcademicAccessForCourse(int studentId, int courseId) {
        String sql = "SELECT TOP 1 1 "
                + "FROM Enrollment e "
                + "JOIN Class c ON e.ClassID = c.ClassID "
                + "WHERE e.StudentID = ? "
                + "AND c.CourseID = ? "
                + "AND e.Status IN ('Active', 'Completed')";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            System.out.println("Fail to validate academic access by course: " + e.getMessage());
        }
        return false;
    }

    public int getTotalEnrollments() {
        int total = 0;
        String sql = "SELECT COUNT(*) as total FROM Enrollment";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (Exception e) {
            System.out.println("Fail to get total enrollment: " + e.getMessage());
        }
        return total;
    }

    public List<Object[]> getEnrollmentManagementList(Integer courseId, Integer classId, String statusFilter) {
        List<Object[]> list = new ArrayList<>();
        String normalizedStatus = statusFilter == null ? "" : statusFilter.trim();

        String sql = "SELECT e.EnrollmentID, u.UserID AS StudentID, u.FullName, u.Email, "
                + "c.ClassID, c.ClassName, co.CourseID, co.CourseName, "
                + "e.EnrollDate, e.Status, p.Status AS PaymentStatus "
                + "FROM Enrollment e "
                + "JOIN Student s ON e.StudentID = s.StudentID "
                + "JOIN [User] u ON s.StudentID = u.UserID "
                + "JOIN Class c ON e.ClassID = c.ClassID "
                + "JOIN Course co ON c.CourseID = co.CourseID "
                + "OUTER APPLY ( "
                + "    SELECT TOP 1 Status "
                + "    FROM Payment p "
                + "    WHERE p.EnrollmentID = e.EnrollmentID "
                + "    ORDER BY p.PaymentDate DESC, p.PaymentID DESC "
                + ") p "
                + "WHERE (? IS NULL OR co.CourseID = ?) "
                + "AND (? IS NULL OR c.ClassID = ?) "
                + "AND ("
                + "    ? = '' "
                + "    OR (? = 'Pending' AND e.Status IN ('Pending', 'UnPaid', 'Unpaid')) "
                + "    OR (? = 'Active' AND e.Status = 'Active') "
                + "    OR (? = 'Rejected' AND e.Status = 'Rejected') "
                + ") "
                + "ORDER BY e.EnrollDate DESC, e.EnrollmentID DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, courseId);
            ps.setObject(2, courseId);
            ps.setObject(3, classId);
            ps.setObject(4, classId);
            ps.setString(5, normalizedStatus);
            ps.setString(6, normalizedStatus);
            ps.setString(7, normalizedStatus);
            ps.setString(8, normalizedStatus);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] row = new Object[11];
                    row[0] = rs.getInt("EnrollmentID");
                    row[1] = rs.getInt("StudentID");
                    row[2] = rs.getString("FullName");
                    row[3] = rs.getString("Email");
                    row[4] = rs.getInt("ClassID");
                    row[5] = rs.getString("ClassName");
                    row[6] = rs.getInt("CourseID");
                    row[7] = rs.getString("CourseName");
                    row[8] = rs.getDate("EnrollDate");
                    row[9] = rs.getString("Status");
                    row[10] = rs.getString("PaymentStatus");
                    list.add(row);
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to get enrollment management list: " + e.getMessage());
        }

        return list;
    }

    public int countEnrollmentsByStatus(String statusFilter) {
        int total = 0;
        String normalizedStatus = statusFilter == null ? "" : statusFilter.trim();
        String sql = "SELECT COUNT(*) AS Total "
                + "FROM Enrollment e "
                + "WHERE ("
                + "    ? = '' "
                + "    OR (? = 'Pending' AND e.Status IN ('Pending', 'UnPaid', 'Unpaid')) "
                + "    OR (? = 'Active' AND e.Status = 'Active') "
                + "    OR (? = 'Rejected' AND e.Status = 'Rejected') "
                + ")";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, normalizedStatus);
            ps.setString(2, normalizedStatus);
            ps.setString(3, normalizedStatus);
            ps.setString(4, normalizedStatus);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    total = rs.getInt("Total");
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to count enrollments by status: " + e.getMessage());
        }

        return total;
    }

    public List<Integer> getMonthlyNewEnrollments(int year) {
        List<Integer> data = new java.util.ArrayList<>();
        for (int i = 0; i < 12; i++) {
            data.add(0);
        }
        String sql = "SELECT MONTH(EnrollDate) as Month, COUNT(StudentID) as Total "
                + "FROM Enrollment " 
                + "WHERE YEAR(EnrollDate) = ? "
                + "GROUP BY MONTH(EnrollDate)";

        try (java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, year);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int month = rs.getInt("Month");
                    int total = rs.getInt("Total");               
                    if (month >= 1 && month <= 12) {
                        data.set(month - 1, total);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to get monthly new enrollments: " + e.getMessage());
        }
        return data;
    }

    public static void main(String[] args) {
    }
}
