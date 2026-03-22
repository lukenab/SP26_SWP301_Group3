/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.Classes;
import model.Course;
import model.Employee;
import model.Enrollment;
import utils.DBContext;

/**
 *
 * @author Legion
 */
public class ClassDAO extends DBContext {

    private static Boolean cachedHasClassMaxCapacityColumn;
    private static Boolean cachedHasClassRoomIdColumn;

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

    private boolean hasClassRoomIdColumn() {
        if (cachedHasClassRoomIdColumn != null) {
            return cachedHasClassRoomIdColumn;
        }
        String sql = "SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS "
                + "WHERE TABLE_NAME = 'Class' AND COLUMN_NAME = 'RoomID'";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            cachedHasClassRoomIdColumn = rs.next();
            return cachedHasClassRoomIdColumn;
        } catch (Exception e) {
            System.out.println("Fail to detect Class.RoomID column: " + e.getMessage());
        }
        return false;
    }

    public List<Object[]> getClassManagementList() {
        return getClassManagementList(null, null, null);
    }

    public List<Object[]> getClassManagementList(String searchQuery, String statusFilter, Integer monthFilter) {
        List<Object[]> list = new ArrayList<>();
        boolean hasMaxCapacity = hasClassMaxCapacityColumn();
        boolean hasRoomId = hasClassRoomIdColumn();
        String sql = "SELECT c.ClassID, c.ClassName, co.CourseName, u.FullName AS TeacherName, "
                + "c.StartDate, c.EndDate, c.Status, COUNT(e.EnrollmentID) AS StudentCount, "
                + (hasMaxCapacity ? "c.MaxCapacity" : "co.TotalSlots") + " AS MaxCapacity, "
                + "DATEADD(DAY, -5, c.StartDate) AS RegistrationDeadline, "
                + (hasRoomId ? "COALESCE(cr.RoomName, rm.RoomName)" : "rm.RoomName") + " AS RoomName "
                + "FROM Class c "
                + "LEFT JOIN Course co ON c.CourseID = co.CourseID "
                + "LEFT JOIN [User] u ON c.TeacherID = u.UserID "
                + "LEFT JOIN Enrollment e ON c.ClassID = e.ClassID "
                + (hasRoomId ? "LEFT JOIN Room cr ON c.RoomID = cr.RoomID " : "")
                + "LEFT JOIN ( "
                + "   SELECT ClassID, STRING_AGG(RoomName, ', ') AS RoomName "
                + "   FROM ( "
                + "       SELECT DISTINCT sc.ClassID, r.RoomName "
                + "       FROM Schedule sc "
                + "       JOIN Room r ON sc.RoomID = r.RoomID "
                + "   ) x "
                + "   GROUP BY ClassID "
                + ") rm ON rm.ClassID = c.ClassID "
                + "WHERE 1 = 1 ";

        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            sql += "AND LOWER(c.ClassName) LIKE ? ";
        }
        if (statusFilter != null && !statusFilter.trim().isEmpty() && !"all".equalsIgnoreCase(statusFilter.trim())) {
            sql += "AND c.Status = ? ";
        }
        if (monthFilter != null) {
            sql += "AND MONTH(c.StartDate) = ? ";
        }

        sql += "GROUP BY c.ClassID, c.ClassName, co.CourseName, u.FullName, c.StartDate, c.EndDate, c.Status, "
                + (hasMaxCapacity ? "c.MaxCapacity" : "co.TotalSlots") + ", "
                + (hasRoomId ? "cr.RoomName, " : "")
                + "rm.RoomName "
                + "ORDER BY c.StartDate DESC, c.ClassID DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            int paramIndex = 1;
            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                ps.setString(paramIndex++, searchQuery.trim().toLowerCase() + "%");
            }
            if (statusFilter != null && !statusFilter.trim().isEmpty() && !"all".equalsIgnoreCase(statusFilter.trim())) {
                ps.setString(paramIndex++, statusFilter.trim());
            }
            if (monthFilter != null) {
                ps.setInt(paramIndex++, monthFilter);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] row = new Object[11];
                    row[0] = rs.getInt("ClassID");
                    row[1] = rs.getString("ClassName");
                    row[2] = rs.getString("CourseName");
                    row[3] = rs.getString("TeacherName");
                    row[4] = rs.getDate("StartDate");
                    row[5] = rs.getDate("EndDate");
                    row[6] = rs.getString("Status");
                    row[7] = rs.getInt("StudentCount");
                    row[8] = rs.getInt("MaxCapacity");
                    row[9] = rs.getDate("RegistrationDeadline");
                    row[10] = rs.getString("RoomName");
                    list.add(row);
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to get class management list: " + e.getMessage());
        }
        return list;
    }

    public List<Object[]> getOpenClassListForSales() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT c.ClassID, c.ClassName, co.CourseName, u.FullName AS TeacherName, "
                + "c.StartDate, c.EndDate, c.Status, COUNT(e.EnrollmentID) AS StudentCount, "
                + "co.TotalSlots, DATEADD(DAY, -5, c.StartDate) AS RegistrationDeadline "
                + "FROM Class c "
                + "LEFT JOIN Course co ON c.CourseID = co.CourseID "
                + "LEFT JOIN [User] u ON c.TeacherID = u.UserID "
                + "LEFT JOIN Enrollment e ON c.ClassID = e.ClassID "
                + "WHERE (c.Status = 'Active' OR c.Status = 'Pending' OR c.Status = 1) "
                + "GROUP BY c.ClassID, c.ClassName, co.CourseName, u.FullName, c.StartDate, c.EndDate, c.Status, co.TotalSlots "
                + "HAVING COUNT(e.EnrollmentID) < co.TotalSlots "
                + "ORDER BY c.StartDate ASC, c.ClassID ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Object[] row = new Object[10];
                row[0] = rs.getInt("ClassID");
                row[1] = rs.getString("ClassName");
                row[2] = rs.getString("CourseName");
                row[3] = rs.getString("TeacherName");
                row[4] = rs.getDate("StartDate");
                row[5] = rs.getDate("EndDate");
                row[6] = rs.getString("Status");
                row[7] = rs.getInt("StudentCount");
                row[8] = rs.getInt("TotalSlots");
                row[9] = rs.getDate("RegistrationDeadline");
                list.add(row);
            }
        } catch (Exception e) {
            System.out.println("Fail to get open class list for sales: " + e.getMessage());
        }
        return list;
    }

    public List<Object[]> getOpenCoursesForSales() {
        List<Object[]> list = new ArrayList<>();
        String sql = "WITH open_class AS ( "
                + "    SELECT c.ClassID, c.CourseID "
                + "    FROM Class c "
                + "    JOIN Course co ON c.CourseID = co.CourseID "
                + "    LEFT JOIN Enrollment e ON c.ClassID = e.ClassID "
                + "    WHERE (c.Status = 'Active' OR c.Status = 'Pending' OR c.Status = 1) "
                + "    GROUP BY c.ClassID, c.CourseID, co.TotalSlots "
                + "    HAVING COUNT(e.EnrollmentID) < co.TotalSlots "
                + ") "
                + "SELECT DISTINCT co.CourseID, co.CourseName, co.TuitionFee "
                + "FROM open_class oc "
                + "JOIN Course co ON oc.CourseID = co.CourseID "
                + "ORDER BY co.CourseName";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Object[] row = new Object[3];
                row[0] = rs.getInt("CourseID");
                row[1] = rs.getString("CourseName");
                row[2] = rs.getBigDecimal("TuitionFee");
                list.add(row);
            }
        } catch (Exception e) {
            System.out.println("Fail to get open courses for sales: " + e.getMessage());
        }
        return list;
    }

    public List<Object[]> getClassOptionsForWalkIn() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT c.ClassID, c.ClassName, co.CourseID, co.CourseName, co.TuitionFee "
                + "FROM Class c "
                + "LEFT JOIN Course co ON c.CourseID = co.CourseID "
                + "ORDER BY co.CourseName, c.ClassName";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Object[] row = new Object[5];
                row[0] = rs.getInt("ClassID");
                row[1] = rs.getString("ClassName");
                row[2] = rs.getInt("CourseID");
                row[3] = rs.getString("CourseName");
                row[4] = rs.getBigDecimal("TuitionFee");
                list.add(row);
            }
        } catch (Exception e) {
            System.out.println("Fail to get class options for walk-in: " + e.getMessage());
        }
        return list;
    }

    public Classes getClassByID(int id) {
        String sql = "SELECT * FROM Class WHERE ClassID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                CourseDAO courseDAO = new CourseDAO();
                Course course = courseDAO.getCourseById(rs.getInt("CourseID"));

                EmployeeDAO employeeDAO = new EmployeeDAO();
                Employee employee = employeeDAO.getEmployeeById(rs.getInt("TeacherID"));

                return new Classes(
                        rs.getInt("ClassID"),
                        rs.getString("ClassName"),
                        course,
                        employee,
                        rs.getDate("StartDate"),
                        rs.getDate("EndDate"),
                        rs.getString("Status")
                );
            }

        } catch (Exception e) {
            System.out.println("Fail to get class by ID: " + e.getMessage());
        }
        return null;
    }

    public Object[] getClassById(int classId) {
        boolean hasMaxCapacity = hasClassMaxCapacityColumn();
        boolean hasRoomId = hasClassRoomIdColumn();
        String sql = "SELECT c.ClassID, c.ClassName, co.CourseName, u.FullName AS TeacherName, "
                + "c.StartDate, c.EndDate, c.Status, COUNT(e.EnrollmentID) AS StudentCount, "
                + (hasMaxCapacity ? "c.MaxCapacity" : "co.TotalSlots") + " AS MaxCapacity, "
                + "DATEADD(DAY, -5, c.StartDate) AS RegistrationDeadline, "
                + (hasRoomId ? "COALESCE(cr.RoomName, rm.RoomName)" : "rm.RoomName") + " AS RoomName "
                + "FROM Class c "
                + "LEFT JOIN Course co ON c.CourseID = co.CourseID "
                + "LEFT JOIN [User] u ON c.TeacherID = u.UserID "
                + "LEFT JOIN Enrollment e ON c.ClassID = e.ClassID "
                + (hasRoomId ? "LEFT JOIN Room cr ON c.RoomID = cr.RoomID " : "")
                + "LEFT JOIN ( "
                + "   SELECT ClassID, STRING_AGG(RoomName, ', ') AS RoomName "
                + "   FROM ( "
                + "       SELECT DISTINCT sc.ClassID, r.RoomName "
                + "       FROM Schedule sc "
                + "       JOIN Room r ON sc.RoomID = r.RoomID "
                + "   ) x "
                + "   GROUP BY ClassID "
                + ") rm ON rm.ClassID = c.ClassID "
                + "WHERE c.ClassID = ? "
                + "GROUP BY c.ClassID, c.ClassName, co.CourseName, u.FullName, c.StartDate, c.EndDate, c.Status, "
                + (hasMaxCapacity ? "c.MaxCapacity" : "co.TotalSlots") + ", "
                + (hasRoomId ? "cr.RoomName, " : "")
                + "rm.RoomName";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Object[] row = new Object[11];
                    row[0] = rs.getInt("ClassID");
                    row[1] = rs.getString("ClassName");
                    row[2] = rs.getString("CourseName");
                    row[3] = rs.getString("TeacherName");
                    row[4] = rs.getDate("StartDate");
                    row[5] = rs.getDate("EndDate");
                    row[6] = rs.getString("Status");
                    row[7] = rs.getInt("StudentCount");
                    row[8] = rs.getInt("MaxCapacity");
                    row[9] = rs.getDate("RegistrationDeadline");
                    row[10] = rs.getString("RoomName");
                    return row;
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to get class by id: " + e.getMessage());
        }
        return null;
    }

    public List<Object[]> getActiveCoursesForClassForm() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT CourseID, CourseName, TotalSlots FROM Course WHERE Status = 1 ORDER BY CourseName ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Object[] row = new Object[3];
                row[0] = rs.getInt("CourseID");
                row[1] = rs.getString("CourseName");
                row[2] = rs.getInt("TotalSlots");
                list.add(row);
            }
        } catch (Exception e) {
            System.out.println("Fail to get active courses for class form: " + e.getMessage());
        }
        return list;
    }

    public List<Object[]> getClassFillRateReport() {
        List<Object[]> list = new ArrayList<>();
        boolean hasMaxCapacity = hasClassMaxCapacityColumn();
        String maxCapacityField = hasMaxCapacity ? "c.MaxCapacity" : "co.TotalSlots";
        String sql = "SELECT c.ClassID, c.ClassName, co.CourseName, u.FullName AS TeacherName, c.Status, "
                + "COUNT(e.EnrollmentID) AS Enrolled, "
                + maxCapacityField + " AS MaxCapacity, "
                + "CASE WHEN " + maxCapacityField + " > 0 THEN (COUNT(e.EnrollmentID) * 100.0) / " + maxCapacityField + " ELSE 0 END AS FillRate "
                + "FROM Class c "
                + "LEFT JOIN Course co ON c.CourseID = co.CourseID "
                + "LEFT JOIN [User] u ON c.TeacherID = u.UserID "
                + "LEFT JOIN Enrollment e ON c.ClassID = e.ClassID "
                + "GROUP BY c.ClassID, c.ClassName, co.CourseName, u.FullName, c.Status, "
                + maxCapacityField + " "
                + "ORDER BY c.ClassName";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Object[] row = new Object[8];
                row[0] = rs.getInt("ClassID");
                row[1] = rs.getString("ClassName");
                row[2] = rs.getString("CourseName");
                row[3] = rs.getString("TeacherName");
                row[4] = rs.getString("Status");
                row[5] = rs.getInt("Enrolled");
                row[6] = rs.getInt("MaxCapacity");
                row[7] = rs.getDouble("FillRate");
                list.add(row);
            }
        } catch (Exception e) {
            System.out.println("Fail to get class fill rate report: " + e.getMessage());
        }
        return list;
    }

    public List<Object[]> getGradeEnrollmentSummary() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT c.ClassID, c.ClassName, co.CourseName, "
                + "COUNT(e.EnrollmentID) AS TotalEnrollments, "
                + "SUM(CASE WHEN e.Status IN ('Paid','Active','Completed') THEN 1 ELSE 0 END) AS PaidCount, "
                + "SUM(CASE WHEN e.Status NOT IN ('Paid','Active','Completed') THEN 1 ELSE 0 END) AS UnpaidCount, "
                + "SUM(CASE WHEN e.FinalGrade IS NOT NULL AND e.FinalGrade > 0 THEN 1 ELSE 0 END) AS GradedCount, "
                + "SUM(CASE WHEN e.FinalGrade >= 5 THEN 1 ELSE 0 END) AS PassCount "
                + "FROM Class c "
                + "LEFT JOIN Course co ON c.CourseID = co.CourseID "
                + "LEFT JOIN Enrollment e ON c.ClassID = e.ClassID "
                + "GROUP BY c.ClassID, c.ClassName, co.CourseName "
                + "ORDER BY c.ClassName";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Object[] row = new Object[9];
                row[0] = rs.getInt("ClassID");
                row[1] = rs.getString("ClassName");
                row[2] = rs.getString("CourseName");
                row[3] = rs.getInt("TotalEnrollments");
                row[4] = rs.getInt("PaidCount");
                row[5] = rs.getInt("UnpaidCount");
                row[6] = rs.getInt("GradedCount");
                row[7] = rs.getInt("GradedCount");
                row[8] = rs.getInt("PassCount");
                list.add(row);
            }
        } catch (Exception e) {
            System.out.println("Fail to get grade enrollment summary: " + e.getMessage());
        }
        return list;
    }

    public List<Object[]> getGradeEnrollmentDetails(int classId) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT e.EnrollmentID, c.ClassName, co.CourseName, u.FullName AS StudentName, e.EnrollDate, e.Status, e.FinalGrade "
                + "FROM Enrollment e "
                + "JOIN Class c ON e.ClassID = c.ClassID "
                + "LEFT JOIN Course co ON c.CourseID = co.CourseID "
                + "JOIN Student s ON e.StudentID = s.StudentID "
                + "JOIN [User] u ON s.StudentID = u.UserID "
                + "WHERE c.ClassID = ? "
                + "ORDER BY u.FullName";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] row = new Object[7];
                    row[0] = rs.getInt("EnrollmentID");
                    row[1] = rs.getString("ClassName");
                    row[2] = rs.getString("CourseName");
                    row[3] = rs.getString("StudentName");
                    row[4] = rs.getDate("EnrollDate");
                    row[5] = rs.getString("Status");
                    row[6] = rs.getDouble("FinalGrade");
                    list.add(row);
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to get grade enrollment details: " + e.getMessage());
        }
        return list;
    }

    public List<Object[]> getTeacherOptions() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT u.UserID, u.FullName, u.Email "
                + "FROM [User] u "
                + "WHERE u.RoleID = 4 "
                + "ORDER BY u.FullName ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Object[] row = new Object[3];
                row[0] = rs.getInt("UserID");
                row[1] = rs.getString("FullName");
                row[2] = rs.getString("Email");
                list.add(row);
            }
        } catch (Exception e) {
            System.out.println("Fail to get teacher options: " + e.getMessage());
        }
        return list;
    }

    public boolean createClass(String className, int courseId, int teacherId, Date startDate, Date endDate, String status, int maxCapacity, Integer roomId) {
        boolean hasMaxCapacity = hasClassMaxCapacityColumn();
        boolean hasRoomId = hasClassRoomIdColumn();
        String sql;
        if (hasMaxCapacity && hasRoomId) {
            sql = "INSERT INTO Class (ClassName, CourseID, TeacherID, StartDate, EndDate, Status, MaxCapacity, RoomID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        } else if (hasMaxCapacity) {
            sql = "INSERT INTO Class (ClassName, CourseID, TeacherID, StartDate, EndDate, Status, MaxCapacity) VALUES (?, ?, ?, ?, ?, ?, ?)";
        } else if (hasRoomId) {
            sql = "INSERT INTO Class (ClassName, CourseID, TeacherID, StartDate, EndDate, Status, RoomID) VALUES (?, ?, ?, ?, ?, ?, ?)";
        } else {
            sql = "INSERT INTO Class (ClassName, CourseID, TeacherID, StartDate, EndDate, Status) VALUES (?, ?, ?, ?, ?, ?)";
        }
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, className);
            ps.setInt(2, courseId);
            ps.setInt(3, teacherId);
            ps.setDate(4, startDate);
            ps.setDate(5, endDate);
            ps.setString(6, status);
            int parameterIndex = 7;
            if (hasMaxCapacity) {
                ps.setInt(parameterIndex++, maxCapacity);
            }
            if (hasRoomId) {
                if (roomId != null && roomId > 0) {
                    ps.setInt(parameterIndex, roomId);
                } else {
                    ps.setNull(parameterIndex, java.sql.Types.INTEGER);
                }
            }
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Fail to create class: " + e.getMessage());
        }
        return false;
    }

    public Object[] getClassForEdit(int classId) {
        boolean hasMaxCapacity = hasClassMaxCapacityColumn();
        boolean hasRoomId = hasClassRoomIdColumn();
        String sql = "SELECT c.ClassID, c.ClassName, c.CourseID, c.TeacherID, c.StartDate, c.EndDate, c.Status, "
                + (hasMaxCapacity ? "c.MaxCapacity" : "co.TotalSlots") + " AS MaxCapacity, "
                + (hasRoomId ? "c.RoomID" : "NULL") + " AS RoomID, "
                + (hasRoomId ? "COALESCE(cr.RoomName, rm.RoomName)" : "rm.RoomName") + " AS RoomName "
                + "FROM Class c "
                + "LEFT JOIN Course co ON c.CourseID = co.CourseID "
                + (hasRoomId ? "LEFT JOIN Room cr ON c.RoomID = cr.RoomID " : "")
                + "LEFT JOIN ( "
                + "   SELECT ClassID, STRING_AGG(RoomName, ', ') AS RoomName "
                + "   FROM ( "
                + "       SELECT DISTINCT sc.ClassID, r.RoomName "
                + "       FROM Schedule sc "
                + "       JOIN Room r ON sc.RoomID = r.RoomID "
                + "   ) x "
                + "   GROUP BY ClassID "
                + ") rm ON rm.ClassID = c.ClassID "
                + "WHERE c.ClassID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Object[] row = new Object[10];
                    row[0] = rs.getInt("ClassID");
                    row[1] = rs.getString("ClassName");
                    row[2] = rs.getInt("CourseID");
                    row[3] = rs.getInt("TeacherID");
                    row[4] = rs.getDate("StartDate");
                    row[5] = rs.getDate("EndDate");
                    row[6] = rs.getString("Status");
                    row[7] = rs.getInt("MaxCapacity");
                    row[8] = rs.getObject("RoomID");
                    row[9] = rs.getString("RoomName");
                    return row;
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to get class for edit: " + e.getMessage());
        }
        return null;
    }

    public boolean updateClass(int classId, String className, int courseId, int teacherId, Date startDate, Date endDate, String status, int maxCapacity, Integer roomId) {
        boolean hasMaxCapacity = hasClassMaxCapacityColumn();
        boolean hasRoomId = hasClassRoomIdColumn();
        String sql;
        if (hasMaxCapacity && hasRoomId) {
            sql = "UPDATE Class SET ClassName = ?, CourseID = ?, TeacherID = ?, StartDate = ?, EndDate = ?, Status = ?, MaxCapacity = ?, RoomID = ? WHERE ClassID = ?";
        } else if (hasMaxCapacity) {
            sql = "UPDATE Class SET ClassName = ?, CourseID = ?, TeacherID = ?, StartDate = ?, EndDate = ?, Status = ?, MaxCapacity = ? WHERE ClassID = ?";
        } else if (hasRoomId) {
            sql = "UPDATE Class SET ClassName = ?, CourseID = ?, TeacherID = ?, StartDate = ?, EndDate = ?, Status = ?, RoomID = ? WHERE ClassID = ?";
        } else {
            sql = "UPDATE Class SET ClassName = ?, CourseID = ?, TeacherID = ?, StartDate = ?, EndDate = ?, Status = ? WHERE ClassID = ?";
        }
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, className);
            ps.setInt(2, courseId);
            ps.setInt(3, teacherId);
            ps.setDate(4, startDate);
            ps.setDate(5, endDate);
            ps.setString(6, status);
            int parameterIndex = 7;
            if (hasMaxCapacity) {
                ps.setInt(parameterIndex++, maxCapacity);
            }
            if (hasRoomId) {
                if (roomId != null && roomId > 0) {
                    ps.setInt(parameterIndex++, roomId);
                } else {
                    ps.setNull(parameterIndex++, java.sql.Types.INTEGER);
                }
            }
            ps.setInt(parameterIndex, classId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Fail to update class: " + e.getMessage());
        }
        return false;
    }

    public boolean updateClassStatus(int classId, String status) {
        String sql = "UPDATE Class SET Status = ? WHERE ClassID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, classId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Fail to update class status: " + e.getMessage());
        }
        return false;
    }

    public String getClassNameById(int classId) {

        String sql = "SELECT ClassName FROM Class WHERE ClassID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, classId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("ClassName");
            }

        } catch (Exception e) {
            System.out.println("Fail to get class name: " + e.getMessage());
        }

        return null;
    }

    public int getTeacherIdByClassId(int classId) {
        String sql = "SELECT TeacherID FROM Class WHERE ClassID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("TeacherID");
            }
        } catch (Exception e) {
            System.out.println("Fail to get teacher id by class id: " + e.getMessage());
        }
        return 0;
    }

    public List<Object[]> getClassesByStudentId(int studentId) {

        List<Object[]> list = new ArrayList<>();

        String sql = "SELECT e.EnrollmentID, c.ClassName, co.CourseName, u.FullName AS TeacherName "
                + "FROM Enrollment e "
                + "JOIN Class c ON e.ClassID = c.ClassID "
                + "JOIN Course co ON c.CourseID = co.CourseID "
                + "JOIN [User] u ON c.TeacherID = u.UserID "
                + "WHERE e.StudentID = ? "
                + "AND e.Status = 'Active'";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Object[] row = new Object[4];

                row[0] = rs.getInt("EnrollmentID");
                row[1] = rs.getString("ClassName");
                row[2] = rs.getString("CourseName");
                row[3] = rs.getString("TeacherName");

                list.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Object[]> getClassesByStudentIdAdvanced(
            int studentId,
            String keyword,
            int page,
            int pageSize) {

        List<Object[]> list = new ArrayList<>();

        String sql = "SELECT e.EnrollmentID, c.ClassName, co.CourseName, u.FullName AS TeacherName "
                + "FROM Enrollment e "
                + "JOIN Class c ON e.ClassID = c.ClassID "
                + "JOIN Course co ON c.CourseID = co.CourseID "
                + "JOIN [User] u ON c.TeacherID = u.UserID "
                + "WHERE e.StudentID = ? "
                + "AND e.Status = 'Active' ";

        // 🔥 FILTER SEARCH
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql += "AND (c.ClassName LIKE ? OR co.CourseName LIKE ? OR u.FullName LIKE ?) ";
        }

        sql += "ORDER BY c.ClassID DESC "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            int index = 1;

            ps.setInt(index++, studentId);

            // keyword
            if (keyword != null && !keyword.trim().isEmpty()) {
                ps.setString(index++, "%" + keyword + "%");
                ps.setString(index++, "%" + keyword + "%");
                ps.setString(index++, "%" + keyword + "%");
            }

            // pagination
            int offset = (page - 1) * pageSize;
            ps.setInt(index++, offset);
            ps.setInt(index++, pageSize);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] row = new Object[4];

                row[0] = rs.getInt("EnrollmentID");
                row[1] = rs.getString("ClassName");
                row[2] = rs.getString("CourseName");
                row[3] = rs.getString("TeacherName");

                list.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public int countClassesByStudentId(int studentId, String keyword) {

        String sql = "SELECT COUNT(*) "
                + "FROM Enrollment e "
                + "JOIN Class c ON e.ClassID = c.ClassID "
                + "JOIN Course co ON c.CourseID = co.CourseID "
                + "JOIN [User] u ON c.TeacherID = u.UserID "
                + "WHERE e.StudentID = ? "
                + "AND e.Status = 'Active' ";

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql += "AND (c.ClassName LIKE ? OR co.CourseName LIKE ? OR u.FullName LIKE ?) ";
        }

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            int index = 1;

            ps.setInt(index++, studentId);

            if (keyword != null && !keyword.trim().isEmpty()) {
                ps.setString(index++, "%" + keyword + "%");
                ps.setString(index++, "%" + keyword + "%");
                ps.setString(index++, "%" + keyword + "%");
            }

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public List<Object[]> getOpenClassesForStudent() {

        List<Object[]> list = new ArrayList<>();

        String sql = "SELECT c.ClassID, c.ClassName, co.CourseName, "
                + "u.FullName AS TeacherName, "
                + "c.StartDate, c.EndDate, "
                + "COUNT(e.EnrollmentID) AS StudentCount, "
                + "co.TuitionFee "
                + "FROM Class c "
                + "LEFT JOIN Course co ON c.CourseID = co.CourseID "
                + "LEFT JOIN [User] u ON c.TeacherID = u.UserID "
                + "LEFT JOIN Enrollment e ON c.ClassID = e.ClassID "
                + "WHERE c.Status = 'Active' "
                + "GROUP BY c.ClassID, c.ClassName, co.CourseName, "
                + "u.FullName, c.StartDate, c.EndDate, co.TuitionFee "
                + "ORDER BY c.StartDate ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Object[] row = new Object[8];

                row[0] = rs.getInt("ClassID");
                row[1] = rs.getString("ClassName");
                row[2] = rs.getString("CourseName");
                row[3] = rs.getString("TeacherName");
                row[4] = rs.getDate("StartDate");
                row[5] = rs.getDate("EndDate");
                row[6] = rs.getInt("StudentCount");
                row[7] = rs.getDouble("TuitionFee");

                list.add(row);
            }

        } catch (Exception e) {
            System.out.println("Fail to get open classes: " + e.getMessage());
        }

        return list;
    }

    public double getClassPrice(int classId) {
        double price = 0;
        String sql = "SELECT co.TuitionFee "
                + "FROM Class c "
                + "JOIN Course co ON c.CourseID = co.CourseID "
                + "WHERE c.ClassID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                price = rs.getDouble("TuitionFee");
            }
        } catch (Exception e) {
            System.out.println("Fail to get class price: " + e.getMessage());
        }

        return price;
    }

    public Object[] getClassDetail(int classId) {

        String sql = "SELECT c.ClassID, c.ClassName, c.StartDate, c.EndDate, c.Status, "
                + "co.CourseID, co.CourseName, co.Description, co.TotalSlots, co.TuitionFee, co.image, "
                + "emp.EmployeeID, emp.HireDate, emp.Education, emp.Experience, "
                + "u.FullName AS TeacherName, "
                + "u.Avatar AS TeacherAvatar, "
                + "rm.RoomName "
                + "FROM Class c "
                + "LEFT JOIN Course co ON c.CourseID = co.CourseID "
                + "LEFT JOIN [User] u ON c.TeacherID = u.UserID "
                + "LEFT JOIN Employee emp ON emp.EmployeeID = u.UserID "
                + "LEFT JOIN ( "
                + "   SELECT ClassID, STRING_AGG(RoomName, ', ') AS RoomName "
                + "   FROM ( "
                + "       SELECT DISTINCT sc.ClassID, r.RoomName "
                + "       FROM Schedule sc "
                + "       JOIN Room r ON sc.RoomID = r.RoomID "
                + "   ) x "
                + "   GROUP BY ClassID "
                + ") rm ON rm.ClassID = c.ClassID "
                + "WHERE c.ClassID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, classId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Classes c = new Classes();

                c.setClassid(rs.getInt("ClassID"));
                c.setClassName(rs.getString("ClassName"));
                c.setStartDate(rs.getDate("StartDate"));
                c.setEndDate(rs.getDate("EndDate"));
                c.setStatus(rs.getString("Status"));

                Course course = new Course();
                course.setCourseId(rs.getInt("CourseID"));
                course.setCourseName(rs.getString("CourseName"));
                course.setDescription(rs.getString("Description"));
                course.setTotalSlots(rs.getInt("TotalSlots"));
                course.setTuitionFee(rs.getBigDecimal("TuitionFee"));
                course.setImages(rs.getString("image"));

                Employee emp = new Employee();
                emp.setEmployeeId(rs.getInt("EmployeeID"));
                emp.setHireDate(rs.getDate("HireDate"));
                emp.setEducation(rs.getString("Education"));
                emp.setExperience(rs.getString("Experience"));

                c.setCourse(course);
                c.setEmployee(emp);

                String teacherName = rs.getString("TeacherName");
                String teacherAvatar = rs.getString("TeacherAvatar");
                String roomName = rs.getString("RoomName");

                return new Object[]{c, teacherName, teacherAvatar, roomName};
            }

        } catch (Exception e) {
            System.out.println("Fail to get class detail: " + e.getMessage());
        }

        return null;
    }

    public List<Object[]> getStudentClasses(int studentId, LocalDate startWeek, LocalDate endWeek) {

        List<Object[]> list = new ArrayList<>();

        String sql = "SELECT "
                + "e.EnrollmentID, "
                + "c.ClassID, c.ClassName, "
                + "co.CourseID, co.CourseName, "
                + "u.FullName AS TeacherName, "
                + "d.LearningDays, "
                + "s.SlotTime, "
                + "rm.RoomName, "
                + "e.Status, e.FinalGrade "
                + "FROM Enrollment e "
                + "JOIN Class c ON e.ClassID = c.ClassID "
                + "LEFT JOIN Course co ON c.CourseID = co.CourseID "
                + "LEFT JOIN [User] u ON c.TeacherID = u.UserID "
                // ===== Learning Days (không lặp) =====
                + "LEFT JOIN ( "
                + "   SELECT ClassID, "
                + "   STRING_AGG(DayName, '-') WITHIN GROUP (ORDER BY DayOrder) AS LearningDays "
                + "   FROM ( "
                + "       SELECT DISTINCT ClassID, "
                + "       DATENAME(WEEKDAY, LearningDate) AS DayName, "
                + "       CASE DATENAME(WEEKDAY, LearningDate) "
                + "           WHEN 'Monday' THEN 1 "
                + "           WHEN 'Tuesday' THEN 2 "
                + "           WHEN 'Wednesday' THEN 3 "
                + "           WHEN 'Thursday' THEN 4 "
                + "           WHEN 'Friday' THEN 5 "
                + "           WHEN 'Saturday' THEN 6 "
                + "           WHEN 'Sunday' THEN 7 "
                + "       END AS DayOrder "
                + "       FROM Schedule "
                + "  WHERE LearningDate BETWEEN ? AND ?"
                + "   ) x "
                + "   GROUP BY ClassID "
                + ") d ON d.ClassID = c.ClassID "
                // ===== Slot Time =====
                + "LEFT JOIN ( "
                + "   SELECT t.ClassID, STRING_AGG(t.TimeRange, ', ') AS SlotTime "
                + "   FROM ( "
                + "       SELECT DISTINCT sc.ClassID, "
                + "       CONVERT(varchar(5), sl.StartTime, 108) + ' - ' + "
                + "       CONVERT(varchar(5), sl.EndTime, 108) AS TimeRange "
                + "       FROM Schedule sc "
                + "       JOIN Slot sl ON sc.SlotID = sl.SlotID "
                + "       WHERE sc.SlotID IS NOT NULL "
                + "   ) t "
                + "   GROUP BY t.ClassID "
                + ") s ON s.ClassID = c.ClassID "
                // ===== Room  =====
                + "LEFT JOIN ( "
                + "   SELECT ClassID, STRING_AGG(RoomName, ', ') AS RoomName "
                + "   FROM ( "
                + "       SELECT DISTINCT sc.ClassID, r.RoomName "
                + "       FROM Schedule sc "
                + "       JOIN Room r ON sc.RoomID = r.RoomID "
                + "   ) x "
                + "   GROUP BY ClassID "
                + ") rm ON rm.ClassID = c.ClassID "
                + "WHERE e.StudentID = ? "
                + "AND e.Status IN ('Active','Completed')";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(startWeek));
            ps.setDate(2, java.sql.Date.valueOf(endWeek));
            ps.setInt(3, studentId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Classes c = new Classes();
                c.setClassid(rs.getInt("ClassID"));
                c.setClassName(rs.getString("ClassName"));

                Course course = new Course();
                course.setCourseId(rs.getInt("CourseID"));
                course.setCourseName(rs.getString("CourseName"));
                c.setCourse(course);

                Enrollment e = new Enrollment();
                e.setEnrollmentId(rs.getInt("EnrollmentID"));
                e.setStatus(rs.getString("Status"));
                e.setFinalGrade(rs.getDouble("FinalGrade"));
                e.setClasses(c);

                String teacherName = rs.getString("TeacherName");
                String learningDays = rs.getString("LearningDays");
                String slotTime = rs.getString("SlotTime");
                String roomName = rs.getString("RoomName");

                list.add(new Object[]{
                    e,
                    teacherName,
                    learningDays,
                    slotTime,
                    roomName
                });
            }

        } catch (Exception e) {
            System.out.println("Fail to get student classes: " + e.getMessage());
        }

        return list;
    }

    public List<Object[]> getStudentClassesAdvanced(
            int studentId,
            LocalDate startWeek,
            LocalDate endWeek,
            String keyword,
            String status) {

        List<Object[]> list = new ArrayList<>();

        String sql = "SELECT "
                + "e.EnrollmentID, "
                + "c.ClassID, c.ClassName, "
                + "co.CourseID, co.CourseName, "
                + "u.FullName AS TeacherName, "
                + "d.LearningDays, "
                + "s.SlotTime, "
                + "rm.RoomName, "
                + "e.Status, e.FinalGrade "
                + "FROM Enrollment e "
                + "JOIN Class c ON e.ClassID = c.ClassID "
                + "LEFT JOIN Course co ON c.CourseID = co.CourseID "
                + "LEFT JOIN [User] u ON c.TeacherID = u.UserID "
                + "LEFT JOIN ( "
                + "   SELECT ClassID, "
                + "   STRING_AGG(DayName, '-') WITHIN GROUP (ORDER BY DayOrder) AS LearningDays "
                + "   FROM ( "
                + "       SELECT DISTINCT ClassID, "
                + "       DATENAME(WEEKDAY, LearningDate) AS DayName, "
                + "       CASE DATENAME(WEEKDAY, LearningDate) "
                + "           WHEN 'Monday' THEN 1 "
                + "           WHEN 'Tuesday' THEN 2 "
                + "           WHEN 'Wednesday' THEN 3 "
                + "           WHEN 'Thursday' THEN 4 "
                + "           WHEN 'Friday' THEN 5 "
                + "           WHEN 'Saturday' THEN 6 "
                + "           WHEN 'Sunday' THEN 7 "
                + "       END AS DayOrder "
                + "       FROM Schedule "
                + "       WHERE LearningDate BETWEEN ? AND ? "
                + "   ) x "
                + "   GROUP BY ClassID "
                + ") d ON d.ClassID = c.ClassID "
                + "LEFT JOIN ( "
                + "   SELECT t.ClassID, STRING_AGG(t.TimeRange, ', ') AS SlotTime "
                + "   FROM ( "
                + "       SELECT DISTINCT sc.ClassID, "
                + "       CONVERT(varchar(5), sl.StartTime, 108) + ' - ' + "
                + "       CONVERT(varchar(5), sl.EndTime, 108) AS TimeRange "
                + "       FROM Schedule sc "
                + "       JOIN Slot sl ON sc.SlotID = sl.SlotID "
                + "   ) t "
                + "   GROUP BY t.ClassID "
                + ") s ON s.ClassID = c.ClassID "
                + "LEFT JOIN ( "
                + "   SELECT ClassID, STRING_AGG(RoomName, ', ') AS RoomName "
                + "   FROM ( "
                + "       SELECT DISTINCT sc.ClassID, r.RoomName "
                + "       FROM Schedule sc "
                + "       JOIN Room r ON sc.RoomID = r.RoomID "
                + "   ) x "
                + "   GROUP BY ClassID "
                + ") rm ON rm.ClassID = c.ClassID "
                + "WHERE e.StudentID = ? "
                + "AND e.Status IN ('Active','Completed') ";

        if (keyword != null && !keyword.isBlank()) {
            sql += " AND (c.ClassName LIKE ? OR co.CourseName LIKE ? OR u.FullName LIKE ?) ";
        }

        if (status != null && !status.isBlank()) {
            sql += " AND e.Status = ? ";
        }

        sql += " ORDER BY c.ClassID DESC ";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            int index = 1;

            ps.setDate(index++, java.sql.Date.valueOf(startWeek));
            ps.setDate(index++, java.sql.Date.valueOf(endWeek));
            ps.setInt(index++, studentId);

            if (keyword != null && !keyword.isBlank()) {
                ps.setString(index++, "%" + keyword + "%");
                ps.setString(index++, "%" + keyword + "%");
                ps.setString(index++, "%" + keyword + "%");
            }

            if (status != null && !status.isBlank()) {
                ps.setString(index++, status);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Classes c = new Classes();
                c.setClassid(rs.getInt("ClassID"));
                c.setClassName(rs.getString("ClassName"));

                Course course = new Course();
                course.setCourseId(rs.getInt("CourseID"));
                course.setCourseName(rs.getString("CourseName"));
                c.setCourse(course);

                Enrollment e = new Enrollment();
                e.setEnrollmentId(rs.getInt("EnrollmentID"));
                e.setStatus(rs.getString("Status"));
                e.setFinalGrade(rs.getDouble("FinalGrade"));
                e.setClasses(c);

                list.add(new Object[]{
                    e,
                    rs.getString("TeacherName"),
                    rs.getString("LearningDays"),
                    rs.getString("SlotTime"),
                    rs.getString("RoomName")
                });
            }

        } catch (Exception e) {
            System.out.println("Fail advanced student classes: " + e.getMessage());
        }

        return list;
    }

    public List<Object[]> getClassesAdvanced(
            String keyword,
            Integer teacherId,
            String status,
            Date fromDate,
            Date toDate,
            int page,
            int pageSize) {

        List<Object[]> list = new ArrayList<>();

        String sql = "SELECT c.ClassID, c.ClassName, co.CourseName, "
                + "u.FullName AS TeacherName, "
                + "c.StartDate, c.EndDate, "
                + "COUNT(e.EnrollmentID) AS StudentCount, "
                + "co.TuitionFee "
                + "FROM Class c "
                + "LEFT JOIN Course co ON c.CourseID = co.CourseID "
                + "LEFT JOIN [User] u ON c.TeacherID = u.UserID "
                + "LEFT JOIN Enrollment e ON c.ClassID = e.ClassID AND e.Status='Active' "
                + "WHERE 1=1 ";

        if (keyword != null && !keyword.isBlank()) {
            sql += " AND (c.ClassName LIKE ? OR co.CourseName LIKE ?) ";
        }

        if (teacherId != null) {
            sql += " AND c.TeacherID = ? ";
        }

        if (status != null && !status.isBlank()) {
            sql += " AND c.Status = ? ";
        }

        if (fromDate != null) {
            sql += " AND c.StartDate >= ? ";
        }

        if (toDate != null) {
            sql += " AND c.EndDate <= ? ";
        }

        sql += " GROUP BY c.ClassID, c.ClassName, co.CourseName, "
                + "u.FullName, c.StartDate, c.EndDate, co.TuitionFee ";

        sql += " ORDER BY c.StartDate ASC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            int index = 1;

            if (keyword != null && !keyword.isBlank()) {
                ps.setString(index++, "%" + keyword + "%");
                ps.setString(index++, "%" + keyword + "%");
            }

            if (teacherId != null) {
                ps.setInt(index++, teacherId);
            }

            if (status != null && !status.isBlank()) {
                ps.setString(index++, status);
            }

            if (fromDate != null) {
                ps.setDate(index++, fromDate);
            }

            if (toDate != null) {
                ps.setDate(index++, toDate);
            }

            int offset = (page - 1) * pageSize;
            ps.setInt(index++, offset);
            ps.setInt(index++, pageSize);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] row = new Object[8];
                row[0] = rs.getInt("ClassID");
                row[1] = rs.getString("ClassName");
                row[2] = rs.getString("CourseName");
                row[3] = rs.getString("TeacherName");
                row[4] = rs.getDate("StartDate");
                row[5] = rs.getDate("EndDate");
                row[6] = rs.getInt("StudentCount");
                row[7] = rs.getDouble("TuitionFee");

                list.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
