package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Assessment;
import model.Classes;
import model.Course;
import model.Enrollment;
import model.Grade;
import utils.DBContext;

public class GradeDAO extends DBContext {

    public List<Grade> getGradesByStudentId(int userId) {

        List<Grade> list = new ArrayList<>();

        String sql = "SELECT "
                + "g.GradeID, "
                + "g.Score, "
                + "a.AssessmentID, "
                + "a.AssessmentName, "
                + "a.Weight, "
                + "e.EnrollmentID, "
                + "c.ClassID, "
                + "c.ClassName, "
                + "co.CourseID, "
                + "co.CourseName "
                + "FROM [User] u "
                + "JOIN Student s ON u.UserID = s.StudentID "
                + "JOIN Enrollment e ON s.StudentID = e.StudentID "
                + "JOIN Class c ON e.ClassID = c.ClassID "
                + "JOIN Course co ON c.CourseID = co.CourseID "
                + "JOIN Grade g ON e.EnrollmentID = g.EnrollmentID "
                + "JOIN Assessment a ON g.AssessmentID = a.AssessmentID "
                + "WHERE u.UserID = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                // ===== Course =====
                Course course = new Course();
                course.setCourseId(rs.getInt("CourseID"));
                course.setCourseName(rs.getString("CourseName"));

                // ===== Class =====
                Classes clazz = new Classes();
                clazz.setClassid(rs.getInt("ClassID"));
                clazz.setClassName(rs.getString("ClassName"));
                clazz.setCourse(course);

                // ===== Enrollment =====
                Enrollment enrollment = new Enrollment();
                enrollment.setEnrollmentId(rs.getInt("EnrollmentID"));
                enrollment.setClasses(clazz);

                // ===== Assessment =====
                Assessment assessment = new Assessment();
                assessment.setAssessmentId(rs.getInt("AssessmentID"));
                assessment.setAssessmentName(rs.getString("AssessmentName"));
                assessment.setWeight(rs.getDouble("Weight"));

                // ===== Grade =====
                Grade grade = new Grade();
                grade.setGradeId(rs.getInt("GradeID"));
                grade.setScore(rs.getDouble("Score"));
                grade.setAssessment(assessment);
                grade.setEnrollment(enrollment);

                list.add(grade);
            }

        } catch (Exception e) {
            System.out.println("Error getGradesByStudentId: " + e.getMessage());
        }

        return list;
    }
}
