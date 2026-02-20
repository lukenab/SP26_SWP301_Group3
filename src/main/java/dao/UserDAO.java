/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.security.MessageDigest;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public List<User> getAllUser() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM [user]";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int userId = rs.getInt("UserID");
                String fullname = rs.getString("FullName");
                String email = rs.getString("Email");
                String password = rs.getString("Password");
                String phone = rs.getString("Phone");
                String address = rs.getString("Address");
                Boolean gender = rs.getBoolean("Gender");
                Date birthdate = rs.getDate("Dob");
                String avatar = rs.getString("Avatar");
                Boolean status = rs.getBoolean("Status");
                Role role = roleDAO.getRoleByID(rs.getInt("RoleID"));

                User user = new User(userId, fullname, email, password, phone, address, gender, birthdate, avatar, status, role);
                list.add(user);
            }
        } catch (Exception e) {
            System.out.println("Fail to get all user: " + e.getMessage());
        }
        return list;
    }

    public User getUserById(int id) {
        String sql = "SELECT * FROM [user] WHERE UserID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int userId = rs.getInt("UserID");
                String fullname = rs.getString("FullName");
                String email = rs.getString("Email");
                String password = rs.getString("Password");
                String phone = rs.getString("Phone");
                String address = rs.getString("Address");
                Boolean gender = rs.getBoolean("Gender");
                Date birthdate = rs.getDate("Dob");
                String avatar = rs.getString("Avatar");
                Boolean status = rs.getBoolean("Status");
                Role role = roleDAO.getRoleByID(rs.getInt("RoleID"));

                return new User(userId, fullname, email, password, phone, address, gender, birthdate, avatar, status, role);
            }
        } catch (Exception e) {
            System.out.println("Fail to get user by ID: " + e.getMessage());
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

                User user = new User(userId, fullname, email, hashedPassword, phone, address, gender, birthdate, avatar, status, role);
                return user;
            }
        } catch (Exception e) {
            System.out.println("Fail to check login: " + e.getMessage());
        }
        return null;
    }

    public Boolean addNewUser(String fullName, String email, String password, String phone, String address, Boolean gender, Date Dob, String avatar, Boolean status, Role role) {
        String sql = "INSERT INTO [dbo].[User] ([FullName],[Email],[Password],[Phone],[Address],[Gender],[Dob],[Avatar],[Status],[RoleID]) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, fullName);
            ps.setString(2, email);
            ps.setString(3, hashMD5(password));
            ps.setString(4, phone);
            ps.setString(5, address);
            ps.setBoolean(6, gender);
            ps.setDate(7, Dob);
            ps.setString(8, avatar);
            ps.setBoolean(9, status);
            ps.setInt(10, role.getRoleId());
            int row = ps.executeUpdate();
            if (row != 0) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("Fail to add new user: " + e.getMessage());
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

    public static void main(String[] args) {
        UserDAO dao = new UserDAO();
        User u = dao.getUserById(1);
        System.out.println(u);
//        List<User> list = dao.getAllUser();
//        System.out.println(list);
//        System.out.println(dao.hashMD5("123456"));
//
//        String name = "Nguyen An Binh";
//        String email = "binhce200008@gmail.com";
//        String password = dao.hashMD5("123456");
//        String address = "Can Tho";
//        Boolean gender = false;
//        Date dob = Date.valueOf("2006-06-11");
//        String avatar = null;
//        Boolean status = true;
//        String phone = "0812154005";
//        RoleDAO roleDAO = new RoleDAO();
//        Role role = roleDAO.getRoleByID(3);
//        
//        Boolean addSucess = dao.addNewUser(name, email, password, phone, address, gender, dob, avatar, status, role);
//        System.out.println(addSucess);
//        dao.inactivateUser(1);
        dao.activateUser(1);

    }
}
