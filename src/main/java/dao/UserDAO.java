/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.security.MessageDigest;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import model.Role;
import model.User;
import utils.DBContext;

/**
 *
 * @author Legion
 */
public class UserDAO extends DBContext {

    RoleDAO roleDAO = new RoleDAO();

    public String hashMD5(String str) {
        try {
            MessageDigest mes = MessageDigest.getInstance("MD5");
            byte[] messMD5 = mes.digest(str.getBytes());
            //[0x0a, 0x7a, 0x12, 0x09]
            StringBuilder result = new StringBuilder();
            for (byte b : messMD5) {
                //0x0a 0x7a; 0x12 0x09 0x3
                String c = String.format("%02x", b);
                //0a; 7a 12 09 03
                result.append(c);
            }
            return result.toString();
        } catch (Exception e) {
        }

        return "";
    }

    private User mapUser(ResultSet rs) throws Exception {
        User user = new User();
        user.setUserId(rs.getInt("UserID"));
        user.setFullName(rs.getString("FullName"));
        user.setEmail(rs.getString("Email"));
        user.setPassword(rs.getString("Password"));
        user.setPhone(rs.getString("Phone"));
        user.setAddress(rs.getString("Address"));
        user.setGender(rs.getBoolean("Gender"));
        user.setDob(rs.getDate("Dob"));
        user.setAvatar(rs.getString("Avatar"));
        user.setStatus(rs.getBoolean("Status"));
        user.setIsLocked(rs.getBoolean("IsLocked"));
        user.setFailedLoginAttempts(rs.getInt("FailedLoginAttempts"));
        user.setCreatedAt(rs.getTimestamp("CreatedAt"));

        user.setRole(roleDAO.getRoleByID(rs.getInt("RoleID")));
        return user;
    }

    public List<User> getAllUser() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM [user]";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapUser(rs));
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to get all user: " + e.getMessage());
        }
        return list;
    }

    public User getUserById(int id) {
        String sql = "SELECT * FROM [User] WHERE UserID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to get user by ID: " + e.getMessage());
        }
        return null;
    }

    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM [User] WHERE Email = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to get user by email: " + e.getMessage());
        }
        return null;
    }

    public User checkLogin(String email, String password) {
        String sql = "SELECT * FROM [User] WHERE Email = ? AND Password = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            String hashedPassword = hashMD5(password);
            ps.setString(2, hashedPassword);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("UserID");
                String fullname = rs.getString("FullName");

                String phone = rs.getString("Phone");
                String address = rs.getString("Address");
                Boolean gender = rs.getBoolean("Gender");
                Date birthdate = rs.getDate("Dob");
                String avatar = rs.getString("Avatar");
                Boolean status = rs.getBoolean("Status");
                Role role = roleDAO.getRoleByID(rs.getInt("RoleID"));
                Timestamp createdAt = rs.getTimestamp("CreatedAt");

                User user = new User(userId, fullname, email, hashedPassword, phone, address, gender, birthdate, avatar, status, role, createdAt);
                user.setIsLocked(rs.getBoolean("IsLocked"));
                return user;
            }
        } catch (Exception e) {
            System.out.println("Fail to check login: " + e.getMessage());
        }
        return null;
    }

    public boolean isEmailExists(String email) {
        String sql = "SELECT 1 FROM [User] WHERE Email = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.out.println("Fail to check existing email: " + e.getMessage());
        }
        return false;
    }

    public boolean isPhoneExists(String phone) {
        String sql = "SELECT 1 FROM [User] WHERE Phone = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.out.println("Fail to check existing phone: " + e.getMessage());
        }
        return false;
    }

    public Boolean addNewUserFull(String fullName, String email, String password, String phone, String address, Boolean gender, Date dob, String avatar, Boolean status, int roleId, Date hireDate, String education, String experience, Date enrollmentDate) {
        String sqlUser = "INSERT INTO [dbo].[User] ([FullName],[Email],[Password],[Phone],[Address],[Gender],[Dob],[Avatar],[Status],[RoleID]) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlEmp = "INSERT INTO Employee (EmployeeID, HireDate, Education, Experience) VALUES (?, ?, ?, ?)";
        String sqlStu = "INSERT INTO Student (StudentID, EnrollmentDate) VALUES (?, ?)";

        try {
            conn.setAutoCommit(false);

            PreparedStatement psUser = conn.prepareStatement(sqlUser, PreparedStatement.RETURN_GENERATED_KEYS);
            psUser.setString(1, fullName);
            psUser.setString(2, email);
            psUser.setString(3, hashMD5(password));
            psUser.setString(4, phone);
            psUser.setString(5, address);
            psUser.setBoolean(6, gender);
            psUser.setDate(7, dob);
            psUser.setString(8, avatar);
            psUser.setBoolean(9, status);
            psUser.setInt(10, roleId);
            psUser.executeUpdate();

            ResultSet rs = psUser.getGeneratedKeys();
            int newUserId = 0;
            if (rs.next()) {
                newUserId = rs.getInt(1);
            }

            if (roleId == 2 || roleId == 3 || roleId == 4) {
                PreparedStatement psEmp = conn.prepareStatement(sqlEmp);
                psEmp.setInt(1, newUserId);
                psEmp.setDate(2, hireDate != null ? hireDate : new java.sql.Date(System.currentTimeMillis()));
                psEmp.setString(3, education);
                psEmp.setString(4, experience);
                psEmp.executeUpdate();

            } else if (roleId == 5) {
                PreparedStatement psStu = conn.prepareStatement(sqlStu);
                psStu.setInt(1, newUserId);
                psStu.setDate(2, enrollmentDate != null ? enrollmentDate : new java.sql.Date(System.currentTimeMillis()));
                psStu.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
            }
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (Exception e) {
            }
        }
        return false;
    }

    public boolean inactivateUser(int id) {
        String sql = "UPDATE [USER] SET Status = 0 WHERE UserID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            int row = ps.executeUpdate();
            if (row > 0) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("Fail to inactivate User: " + e.getMessage());
        }
        return false;
    }

    public boolean activateUser(int id) {
        String sql = "UPDATE [USER] SET Status = 1 WHERE UserID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            int row = ps.executeUpdate();
            if (row > 0) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("Fail to activate User: " + e.getMessage());
        }
        return false;
    }

    public Boolean updateUserById(String fullName, String phone, String address, Boolean gender, Date dob, String avatar, int roleId, int userId, Date enrollmentDate, Date HireDate, String education, String experience) {
        String sql = "UPDATE [dbo].[User] SET [FullName] = ?,[Phone] = ?,[Address] = ?,[Gender] = ?,[Dob] = ?,[Avatar] = ? WHERE UserID = ?";
        String employeeSql = "UPDATE [dbo].[Employee] SET [HireDate] = ?, [Education] = ?, [Experience] = ? WHERE EmployeeID = ?";
        String studentSql = "UPDATE [dbo].[Student] SET [EnrollmentDate] = ? WHERE [StudentID] = ?";
        try {
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, fullName);
            ps.setString(2, phone);
            ps.setString(3, address);
            ps.setBoolean(4, gender);
            ps.setDate(5, dob);
            ps.setString(6, avatar);
            ps.setInt(7, userId);
            ps.executeUpdate();
            if (roleId == 5) {
                PreparedStatement psStu = conn.prepareStatement(studentSql);
                psStu.setDate(1, enrollmentDate);
                psStu.setInt(2, userId);
                psStu.executeUpdate();
            } else if (roleId == 2 || roleId == 3 || roleId == 4) {
                PreparedStatement psEmpl = conn.prepareStatement(employeeSql);
                psEmpl.setDate(1, HireDate);
                psEmpl.setString(2, education);
                psEmpl.setString(3, experience);
                psEmpl.setInt(4, userId);
                psEmpl.executeUpdate();
            }
            conn.commit();
            return true;
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
            }
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (Exception e) {
            }
        }
        return false;
    }

    public Boolean updatePassword(String password, int userId) {
        String sql = "UPDATE [User] SET Password = ? WHERE UserID = ?;";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, password);
            ps.setInt(2, userId);

            int row = ps.executeUpdate();
            if (row > 0) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("Fail to update password: " + e.getMessage());
        }
        return false;
    }

    public List<User> searchAndFilterUsers(String searchQuery, String roleId, String status) {
        List<User> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM [user] WHERE 1=1 ");

        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            sql.append(" AND (FullName LIKE ? OR Email LIKE ?) ");
        }
        if (roleId != null && !roleId.trim().isEmpty()) {
            sql.append(" AND RoleID = ? ");
        }
        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND Status = ? ");
        }

        try {
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            int index = 1;

            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                ps.setString(index++, "%" + searchQuery.trim() + "%");
                ps.setString(index++, "%" + searchQuery.trim() + "%");
            }
            if (roleId != null && !roleId.trim().isEmpty()) {
                ps.setInt(index++, Integer.parseInt(roleId));
            }
            if (status != null && !status.trim().isEmpty()) {
                ps.setBoolean(index++, status.equals("1"));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapUser(rs));
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to search and filter user: " + e.getMessage());
        }
        return list;
    }

    public boolean toggleLockUser(int userId, boolean isLocked) {
        String sql = "UPDATE [User] SET IsLocked = ? WHERE UserID = ?";
        try {
            PreparedStatement ps = conn.prepareCall(sql);
            ps.setBoolean(1, isLocked);
            ps.setInt(2, userId);
            int row = ps.executeUpdate();
            if (row > 0) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("Fail to lock/ unlock user: " + e.getMessage());
        }
        return false;
    }

    public void incrementFailedLogin(String email) {
        String sql = "UPDATE [User] SET FailedLoginAttempts = FailedLoginAttempts + 1 WHERE Email = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.executeUpdate();
        } catch (Exception e) {
        }
    }

    public void resetFailedLogin(String email) {
        String sql = "UPDATE [User] SET FailedLoginAttempts = 0 WHERE Email = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.executeUpdate();
        } catch (Exception e) {
        }
    }

    public void lockUser(String email) {
        String sql = "UPDATE [User] SET IsLocked = 1 WHERE Email = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.executeUpdate();
        } catch (Exception e) {
        }
    }

    public User getUserByEmployeeId(int employeeId) {
        String sql = "SELECT * FROM [User] WHERE UserID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to get user by employeeId: " + e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
        UserDAO dao = new UserDAO();
        System.out.println(dao.getUserById(3));
    }
}
