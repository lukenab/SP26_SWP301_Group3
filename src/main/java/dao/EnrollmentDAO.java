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
import model.Enrollment;
import model.Student;
import utils.DBContext;

/**
 *
 * @author Legion
 */
public class EnrollmentDAO extends DBContext {
    
    public List<Enrollment> getAllEnrollment(){
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT * FROM Enrollment";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
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
            System.out.println("Fail to get all enrollment: " +e.getMessage());
        }
        return list;
    }
    
    public Enrollment getEnrollmentById(int id){
        String sql = "SELECT * FROM Enrollment WHERE EnrollmentID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
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
            System.out.println("Fail to get enrollment by ID: " +e.getMessage());
        }
        return null;
    }

    public List<Object[]> getStudentsInClass(int classId) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT e.EnrollmentID, s.StudentID, u.FullName, u.Email, e.EnrollDate, e.Status "
                + "FROM Enrollment e "
                + "JOIN Student s ON e.StudentID = s.StudentID "
                + "JOIN [User] u ON s.StudentID = u.UserID "
                + "WHERE e.ClassID = ? "
                + "ORDER BY u.FullName ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] row = new Object[6];
                    row[0] = rs.getInt("EnrollmentID");
                    row[1] = rs.getInt("StudentID");
                    row[2] = rs.getString("FullName");
                    row[3] = rs.getString("Email");
                    row[4] = rs.getDate("EnrollDate");
                    row[5] = rs.getString("Status");
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
        String sql = "SELECT s.StudentID, u.FullName, u.Email, s.EnrollmentDate "
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
                    Object[] row = new Object[4];
                    row[0] = rs.getInt("StudentID");
                    row[1] = rs.getString("FullName");
                    row[2] = rs.getString("Email");
                    row[3] = rs.getDate("EnrollmentDate");
                    list.add(row);
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to get students not in class: " + e.getMessage());
        }
        return list;
    }

    public int addStudentsToClass(int classId, int[] studentIds) {
        if (studentIds == null || studentIds.length == 0) {
            return 0;
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
                    ps.setString(3, "Active");
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
        String sql = "DELETE FROM Enrollment WHERE ClassID = ? AND StudentID = ?";
        int removedCount = 0;
        try {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (int studentId : studentIds) {
                    ps.setInt(1, classId);
                    ps.setInt(2, studentId);
                    removedCount += ps.executeUpdate();
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
    
    public static void main(String[] args) {
        EnrollmentDAO dao = new EnrollmentDAO();
//        System.out.println(dao.getEnrollmentById(0));
    }

}
