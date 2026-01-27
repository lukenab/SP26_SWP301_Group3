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
class Enrollment {
    private int enrollmentId;
    private Student student;
    private Classes classes;
    private Date enrollDate;
    private String status;
    private double finalGrade;

    public Enrollment() {
    }

    public Enrollment(int enrollmentId, Student student, Classes classes, Date enrollDate, String status, double finalGrade) {
        this.enrollmentId = enrollmentId;
        this.student = student;
        this.classes = classes;
        this.enrollDate = enrollDate;
        this.status = status;
        this.finalGrade = finalGrade;
    }

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Classes getClasses() {
        return classes;
    }

    public void setClasses(Classes classes) {
        this.classes = classes;
    }

    public Date getEnrollDate() {
        return enrollDate;
    }

    public void setEnrollDate(Date enrollDate) {
        this.enrollDate = enrollDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(double finalGrade) {
        this.finalGrade = finalGrade;
    }

    @Override
    public String toString() {
        return "Enrollment{" + "enrollmentId=" + enrollmentId + ", student=" + student + ", classes=" + classes + ", enrollDate=" + enrollDate + ", status=" + status + ", finalGrade=" + finalGrade + '}';
    }

 
    
}
