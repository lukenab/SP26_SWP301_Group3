/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
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

    public List<Object[]> getClassManagementList() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT c.ClassID, c.ClassName, co.CourseName, u.FullName AS TeacherName, "
                + "c.StartDate, c.EndDate, c.Status, COUNT(e.EnrollmentID) AS StudentCount, "
                + "co.TotalSlots, DATEADD(DAY, -5, c.StartDate) AS RegistrationDeadline "
                + "FROM Class c "
                + "LEFT JOIN Course co ON c.CourseID = co.CourseID "
                + "LEFT JOIN [User] u ON c.TeacherID = u.UserID "
                + "LEFT JOIN Enrollment e ON c.ClassID = e.ClassID "
                + "GROUP BY c.ClassID, c.ClassName, co.CourseName, u.FullName, c.StartDate, c.EndDate, c.Status, co.TotalSlots "
                + "ORDER BY c.StartDate DESC, c.ClassID DESC";
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
            System.out.println("Fail to get class management list: " + e.getMessage());
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
        String sql = "SELECT c.ClassID, c.ClassName, co.CourseName, u.FullName AS TeacherName, "
                + "c.StartDate, c.EndDate, c.Status, COUNT(e.EnrollmentID) AS StudentCount, "
                + "co.TotalSlots, DATEADD(DAY, -5, c.StartDate) AS RegistrationDeadline "
                + "FROM Class c "
                + "LEFT JOIN Course co ON c.CourseID = co.CourseID "
                + "LEFT JOIN [User] u ON c.TeacherID = u.UserID "
                + "LEFT JOIN Enrollment e ON c.ClassID = e.ClassID "
                + "WHERE c.ClassID = ? "
                + "GROUP BY c.ClassID, c.ClassName, co.CourseName, u.FullName, c.StartDate, c.EndDate, c.Status, co.TotalSlots";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
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
        String sql = "SELECT CourseID, CourseName FROM Course WHERE Status = 1 ORDER BY CourseName ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Object[] row = new Object[2];
                row[0] = rs.getInt("CourseID");
                row[1] = rs.getString("CourseName");
                list.add(row);
            }
        } catch (Exception e) {
            System.out.println("Fail to get active courses for class form: " + e.getMessage());
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

    public boolean createClass(String className, int courseId, int teacherId, Date startDate, Date endDate, String status) {
        String sql = "INSERT INTO Class (ClassName, CourseID, TeacherID, StartDate, EndDate, Status) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, className);
            ps.setInt(2, courseId);
            ps.setInt(3, teacherId);
            ps.setDate(4, startDate);
            ps.setDate(5, endDate);
            ps.setString(6, status);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Fail to create class: " + e.getMessage());
        }
        return false;
    }

    public Object[] getClassForEdit(int classId) {
        String sql = "SELECT ClassID, ClassName, CourseID, TeacherID, StartDate, EndDate, Status "
                + "FROM Class WHERE ClassID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Object[] row = new Object[7];
                    row[0] = rs.getInt("ClassID");
                    row[1] = rs.getString("ClassName");
                    row[2] = rs.getInt("CourseID");
                    row[3] = rs.getInt("TeacherID");
                    row[4] = rs.getDate("StartDate");
                    row[5] = rs.getDate("EndDate");
                    row[6] = rs.getString("Status");
                    return row;
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to get class for edit: " + e.getMessage());
        }
        return null;
    }

    public boolean updateClass(int classId, String className, int courseId, int teacherId, Date startDate, Date endDate) {
        String sql = "UPDATE Class "
                + "SET ClassName = ?, CourseID = ?, TeacherID = ?, StartDate = ?, EndDate = ? "
                + "WHERE ClassID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, className);
            ps.setInt(2, courseId);
            ps.setInt(3, teacherId);
            ps.setDate(4, startDate);
            ps.setDate(5, endDate);
            ps.setInt(6, classId);
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
                String roomName = rs.getString("RoomName");

                return new Object[]{c, teacherName, roomName};
            }

        } catch (Exception e) {
            System.out.println("Fail to get class detail: " + e.getMessage());
        }

        return null;
    }

    public List<Object[]> getStudentClasses(int studentId) {

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

            ps.setInt(1, studentId);
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
}
