/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import model.Employee;
import utils.DBContext;

/**
 *
 * @author Legion
 */
public class EmployeeDAO extends DBContext {

    public List<Employee> getAllEmployee() {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM Employee";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int employeeId = rs.getInt("EmployeeID");
                Date hireDate = rs.getDate("HireDate");
                String education = rs.getString("Education");
                String experience = rs.getString("Experience");
                Employee employee = new Employee(employeeId, hireDate, education, experience);
                list.add(employee);
            }

        } catch (Exception e) {
            System.out.println("Fail to get all employee: " + e.getMessage());
        }
        return list;
    }

    public Employee getEmployeeById(int id) {
        String sql = "SELECT * FROM Employee WHERE EmployeeID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int employeeId = rs.getInt("EmployeeID");
                Date hireDate = rs.getDate("HireDate");
                String education = rs.getString("Education");
                String experience = rs.getString("Experience");
                return new Employee(employeeId, hireDate, education, experience);
            }
        } catch (Exception e) {
            System.out.println("Fail to get employee by ID: " + e.getMessage());
        }
        return null;
    }
    
    public static void main(String[] args) {
        EmployeeDAO dao = new EmployeeDAO();
        List<Employee> list = dao.getAllEmployee();
        for (Employee employee : list) {
            System.out.println(employee);
        }
        
        System.out.println(dao.getEmployeeById(16));
    }
}
