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
public class Classes {
    private int classid;
    private String className;
    private Course course;
    private Employee employee;
    private Date starDate;
    private Date endDate;
    private String status;

    public Classes() {
    }

    public Classes(int classid, String className, Course course, Employee employee, Date starDate, Date endDate, String status) {
        this.classid = classid;
        this.className = className;
        this.course = course;
        this.employee = employee;
        this.starDate = starDate;
        this.endDate = endDate;
        this.status = status;
    }

    public int getClassid() {
        return classid;
    }

    public void setClassid(int classid) {
        this.classid = classid;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Date getStarDate() {
        return starDate;
    }

    public void setStarDate(Date starDate) {
        this.starDate = starDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Classes{" + "classid=" + classid + ", className=" + className + ", course=" + course + ", employee=" + employee + ", starDate=" + starDate + ", endDate=" + endDate + ", status=" + status + '}';
    }

  
    
}
