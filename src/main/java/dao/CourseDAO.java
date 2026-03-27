/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.sql.SQLException;
import model.Employee;
import model.Course;
import model.User;
import utils.DBContext;

/**
 *
 * @author Legion
 */
public class CourseDAO extends DBContext {

    public List<Course> getAllCourse() {
        List<Course> list = new ArrayList<>();

        String sql = "SELECT * FROM Course";

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Course course = new Course(
                        rs.getInt("CourseID"),
                        rs.getString("CourseName"),
                        rs.getString("Description"),
                        rs.getInt("TotalSlots"),
                        rs.getBigDecimal("TuitionFee"),
                        rs.getBoolean("Status"),
                        rs.getString("Image")
                );

                list.add(course);
            }

        } catch (Exception e) {
            System.out.println("Fail to get all course: " + e.getMessage());
        }

        return list;
    }

    public List<Course> getCoursesByStudentId(int studentId) {

        List<Course> list = new ArrayList<>();

        String sql = "SELECT DISTINCT "
                + "co.CourseID, "
                + "co.CourseName, "
                + "co.Description, "
                + "co.TotalSlots, "
                + "co.TuitionFee, "
                + "co.Status, "
                + "co.Image "
                + "FROM Enrollment e "
                + "INNER JOIN Class c ON e.ClassID = c.ClassID "
                + "INNER JOIN Course co ON c.CourseID = co.CourseID "
                + "WHERE e.StudentID = ? "
                + "AND e.Status IN ('Active', 'Completed') "
                + "AND co.Status = 1";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Course course = new Course(
                        rs.getInt("CourseID"),
                        rs.getString("CourseName"),
                        rs.getString("Description"),
                        rs.getInt("TotalSlots"),
                        rs.getBigDecimal("TuitionFee"),
                        rs.getBoolean("Status"),
                        rs.getString("Image")
                );

                list.add(course);
            }

        } catch (Exception e) {
            System.out.println("Fail to get courses by student: " + e.getMessage());
        }

        return list;
    }

    public List<Course> getCoursesByStudentAdvanced(
            int studentId,
            String keyword,
            Boolean status,
            int page,
            int pageSize) {

        List<Course> list = new ArrayList<>();

        String sql = "SELECT DISTINCT "
                + "co.CourseID, "
                + "co.CourseName, "
                + "co.Description, "
                + "co.TotalSlots, "
                + "co.TuitionFee, "
                + "co.Status, "
                + "co.Image "
                + "FROM Enrollment e "
                + "INNER JOIN Class c ON e.ClassID = c.ClassID "
                + "INNER JOIN Course co ON c.CourseID = co.CourseID "
                + "WHERE e.StudentID = ? "
                + "AND e.Status IN ('Active', 'Completed') ";

        if (keyword != null && !keyword.isBlank()) {
            sql += " AND co.CourseName LIKE ? ";
        }

        if (status != null) {
            sql += " AND co.Status = ? ";
        }

        sql += " ORDER BY co.CourseID DESC ";
        sql += " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            int index = 1;

            ps.setInt(index++, studentId);

            if (keyword != null && !keyword.isBlank()) {
                ps.setString(index++, "%" + keyword + "%");
            }

            if (status != null) {
                ps.setBoolean(index++, status);
            }

            int offset = (page - 1) * pageSize;

            ps.setInt(index++, offset);
            ps.setInt(index++, pageSize);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Course course = new Course(
                        rs.getInt("CourseID"),
                        rs.getString("CourseName"),
                        rs.getString("Description"),
                        rs.getInt("TotalSlots"),
                        rs.getBigDecimal("TuitionFee"),
                        rs.getBoolean("Status"),
                        rs.getString("Image")
                );

                list.add(course);
            }

        } catch (Exception e) {
            System.out.println("Fail advanced course search: " + e.getMessage());
        }

        return list;
    }

    public List<Course> getActiveCourses() {
        List<Course> list = new ArrayList<>();

        String sql = "SELECT * FROM Course WHERE Status = 1";

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Course course = new Course(
                        rs.getInt("CourseID"),
                        rs.getString("CourseName"),
                        rs.getString("Description"),
                        rs.getInt("TotalSlots"),
                        rs.getBigDecimal("TuitionFee"),
                        rs.getBoolean("Status"),
                        rs.getString("Image")
                );

                list.add(course);
            }

        } catch (Exception e) {
            System.out.println("Fail to get active courses: " + e.getMessage());
        }

        return list;
    }

    public Course getCourseById(int id) {
        Course course = null;

        String sql = "SELECT * FROM Course WHERE CourseID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    course = new Course(
                            rs.getInt("CourseID"),
                            rs.getString("CourseName"),
                            rs.getString("Description"),
                            rs.getInt("TotalSlots"),
                            rs.getBigDecimal("TuitionFee"),
                            rs.getBoolean("Status"),
                            rs.getString("Image")
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to get course by ID: " + e.getMessage());
        }

        return course;
    }

    public Double getAverageByStudentAndCourse(int courseId, int studentId) {

        String sql = "SELECT COALESCE(AVG(g.Score), 0) AS AverageScore "
                + "FROM Class cl "
                + "JOIN Enrollment e ON cl.ClassID = e.ClassID "
                + "JOIN Grade g ON e.EnrollmentID = g.EnrollmentID "
                + "WHERE cl.CourseID = ? "
                + "AND e.StudentID = ? "
                + "AND e.Status IN ('Active', 'Completed')";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, courseId);
            ps.setInt(2, studentId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Double avg = rs.getDouble("AverageScore");

                    // Nếu không có điểm → AVG trả NULL
                    if (rs.wasNull()) {
                        return 0.0;
                    }

                    return avg;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error calculating average: " + e.getMessage());
        }

        return 0.0;
    }

    public boolean addCourse(Course course) {
        String sql = "INSERT INTO Course (CourseName, Description, TotalSlots, TuitionFee, Status, Image) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, course.getCourseName());
            ps.setString(2, course.getDescription());
            ps.setInt(3, course.getTotalSlots());
            ps.setBigDecimal(4, course.getTuitionFee());
            ps.setBoolean(5, course.isStatus());
            ps.setString(6, course.getImages());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            System.out.println("Fail to add course: " + e.getMessage());
            return false;
        }
    }

    public boolean updateCourse(Course course) {
        String sql = "UPDATE Course SET CourseName = ?, Description = ?, TotalSlots = ?, TuitionFee = ?, Status = ?, Image = ? WHERE CourseID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, course.getCourseName());
            ps.setString(2, course.getDescription());
            ps.setInt(3, course.getTotalSlots());
            ps.setBigDecimal(4, course.getTuitionFee());
            ps.setBoolean(5, course.isStatus());
            ps.setString(6, course.getImages());
            ps.setInt(7, course.getCourseId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            System.out.println("Fail to update course: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteCourse(int courseId) {
        String sql = "UPDATE Course SET Status = 0 WHERE CourseID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            System.out.println("Fail to delete course: " + e.getMessage());
            return false;
        }
    }

    public boolean activateCourse(int courseId) {
        String sql = "UPDATE Course SET Status = 1 WHERE CourseID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            System.out.println("Fail to activate course: " + e.getMessage());
            return false;
        }
    }

    public int getTotalCourseCount() {
        String sql = "SELECT COUNT(*) FROM Course";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("Fail to get total course count: " + e.getMessage());
        }
        return 0;
    }

    public List<Course> searchCourses(String keyword) {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM Course WHERE (? IS NULL OR LTRIM(RTRIM(?)) = '' OR CourseName LIKE ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            String normalizedKeyword = keyword == null ? null : keyword.trim();
            String searchKeyword = normalizedKeyword == null || normalizedKeyword.isEmpty()
                    ? null : normalizedKeyword + "%";
            ps.setString(1, normalizedKeyword);
            ps.setString(2, normalizedKeyword);
            ps.setString(3, searchKeyword);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Course course = new Course(
                            rs.getInt("CourseID"),
                            rs.getString("CourseName"),
                            rs.getString("Description"),
                            rs.getInt("TotalSlots"),
                            rs.getBigDecimal("TuitionFee"),
                            rs.getBoolean("Status"),
                            rs.getString("Image")
                    );
                    list.add(course);
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to search courses: " + e.getMessage());
        }

        return list;
    }

    public List<Course> searchActiveCourses(String keyword) {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM Course WHERE Status = 1 AND (CourseName LIKE ? OR Description LIKE ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            String searchKeyword = "%" + keyword + "%";
            ps.setString(1, searchKeyword);
            ps.setString(2, searchKeyword);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Course course = new Course(
                            rs.getInt("CourseID"),
                            rs.getString("CourseName"),
                            rs.getString("Description"),
                            rs.getInt("TotalSlots"),
                            rs.getBigDecimal("TuitionFee"),
                            rs.getBoolean("Status"),
                            rs.getString("Image")
                    );
                    list.add(course);
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to search active courses: " + e.getMessage());
        }

        return list;
    }

    public Object[] getInstructorProfileByCourseId(int courseId) {
        String sql = "SELECT TOP 1 "
                + "u.UserID, u.FullName, u.Email, u.Phone, u.Address, u.Gender, u.Dob, u.Avatar, u.Status, "
                + "e.EmployeeID, e.HireDate, e.Education, e.Experience, "
                + "(SELECT COUNT(*) FROM Class c2 WHERE c2.CourseID = c.CourseID AND c2.TeacherID = u.UserID) AS ClassCount "
                + "FROM Class c "
                + "JOIN [User] u ON c.TeacherID = u.UserID "
                + "LEFT JOIN Employee e ON e.EmployeeID = u.UserID "
                + "WHERE c.CourseID = ? "
                + "ORDER BY "
                + "CASE WHEN c.Status = 'Active' THEN 0 WHEN c.Status = 'Pending' THEN 1 ELSE 2 END, "
                + "c.StartDate DESC, c.ClassID DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User instructor = new User();
                    instructor.setUserId(rs.getInt("UserID"));
                    instructor.setFullName(rs.getString("FullName"));
                    instructor.setEmail(rs.getString("Email"));
                    instructor.setPhone(rs.getString("Phone"));
                    instructor.setAddress(rs.getString("Address"));
                    instructor.setGender(rs.getBoolean("Gender"));
                    instructor.setDob(rs.getDate("Dob"));
                    instructor.setAvatar(rs.getString("Avatar"));
                    instructor.setStatus(rs.getBoolean("Status"));

                    Employee profile = null;
                    int employeeId = rs.getInt("EmployeeID");
                    if (!rs.wasNull()) {
                        profile = new Employee();
                        profile.setEmployeeId(employeeId);
                        profile.setHireDate(rs.getDate("HireDate"));
                        profile.setEducation(rs.getString("Education"));
                        profile.setExperience(rs.getString("Experience"));
                    }

                    return new Object[]{instructor, profile, rs.getInt("ClassCount")};
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to get instructor profile by course id: " + e.getMessage());
        }

        return null;
    }

    public Double getFinalGradeByStudentAndCourse(int courseId, int studentId) {

        String sql = "SELECT SUM(g.Score * a.Weight) / 100 AS FinalGrade "
                + "FROM Class cl "
                + "JOIN Enrollment e ON cl.ClassID = e.ClassID "
                + "JOIN Grade g ON e.EnrollmentID = g.EnrollmentID "
                + "JOIN Assessment a ON g.AssessmentID = a.AssessmentID "
                + "WHERE cl.CourseID = ? "
                + "AND e.StudentID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, courseId);
            ps.setInt(2, studentId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double result = rs.getDouble("FinalGrade");

                if (rs.wasNull()) {
                    return 0.0;
                }

                return result;
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return 0.0;
    }
}
