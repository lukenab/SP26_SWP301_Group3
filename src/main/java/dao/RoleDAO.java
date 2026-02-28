/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Role;
import utils.DBContext;

/**
 *
 * @author Legion
 */
public class RoleDAO extends DBContext {

    public List<Role> getAllRole() {
        List<Role> list = new ArrayList<>();
        String sql = "SELECT * FROM Role";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int roleId = rs.getInt("RoleID");
                String roleName = rs.getString("RoleName");
                Boolean manageUser = rs.getBoolean("ManageUser");
                Boolean manageCourse = rs.getBoolean("ManageCourse");
                Boolean Finance = rs.getBoolean("ManageFinance");

                Role role = new Role(roleId, roleName, manageUser, Finance, manageCourse);
                list.add(role);
            }
        } catch (Exception e) {
            System.out.println("Fail to get all role: " + e.getMessage());
        }
        return list;
    }

    public Role getRoleByID(int id) {
        String sql = "SELECT * FROM Role WHERE RoleID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int roleId = rs.getInt("RoleID");
                String roleName = rs.getString("RoleName");
                Boolean manageUser = rs.getBoolean("ManageUser");
                Boolean manageCourse = rs.getBoolean("ManageCourse");
                Boolean Finance = rs.getBoolean("ManageFinance");

                Role role = new Role(roleId, roleName, manageUser, Finance, manageCourse);
                return role;
            }
        } catch (Exception e) {
            System.out.println("Fail to get role by ID: " + e.getMessage());
        }
        return null;
    }

    public Boolean updateRolePermissions(int roleId, boolean manageUser, boolean manageCourse, boolean manageFinance) {
        String sql = "UPDATE [Role] SET ManageUser = ?, ManageCourse = ?, ManageFinance = ? WHERE RoleID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setBoolean(1, manageUser);
            ps.setBoolean(2, manageCourse);
            ps.setBoolean(3, manageFinance);
            ps.setInt(4, roleId);
            int row = ps.executeUpdate();
            if (row > 0) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("Fail to update role: " + e.getMessage());
        }
        return false;
    }

    public static void main(String[] args) {
        RoleDAO dao = new RoleDAO();
        List<Role> list = dao.getAllRole();
        System.out.println(list);

        int id = 1;
        System.out.println(dao.getRoleByID(id)
        );
    }

}
