/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;

/**
 *
 * @author phuct
 */
class Employee {
    private int employeeId;
    private Date hireDate;
    private String education;
    private String experience;

    public Employee() {
    }

    public Employee(int employeeId, Date hireDate, String education, String experience) {
        this.employeeId = employeeId;
        this.hireDate = hireDate;
        this.education = education;
        this.experience = experience;
    }

   

    @Override
    public String toString() {
        return "Employee{" + "employeeId=" + employeeId + ", hireDate=" + hireDate + ", education=" + education + ", experience=" + experience + '}';
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }
    
}
