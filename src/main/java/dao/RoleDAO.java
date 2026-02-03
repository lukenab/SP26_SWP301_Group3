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

    public List<Role> getAllList() {
        List<Role> list = new ArrayList<>();
        String sql = "SELECT * FROM Role";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int roleId = rs.getInt("RoleID");
                String roleName = rs.getString("RoleName");
                Role role = new Role(roleId, roleName);
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
                Role role = new Role(roleId, roleName);
                return role;
            }
        } catch (Exception e) {
            System.out.println("Fail to get role by ID: " + e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
        RoleDAO dao = new RoleDAO();
        List<Role> list = dao.getAllList();
        System.out.println(list);

        int id = 1;
        System.out.println(dao.getRoleByID(id)
        );
    }

}
