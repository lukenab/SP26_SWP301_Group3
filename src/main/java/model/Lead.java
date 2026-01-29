/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDateTime;

/**
 *
 * @author phuct
 */
public class Lead {
    private int leadId;
    private String fullName;
    private String email;
    private String phone;
    private Course course;
    private String status;
    private LocalDateTime createDate;

    public Lead() {
    }

    public Lead(int leadId, String fullName, String email, String phone, Course course, String status, LocalDateTime createDate) {
        this.leadId = leadId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.course = course;
        this.status = status;
        this.createDate = createDate;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public int getLeadId() {
        return leadId;
    }

    public void setLeadId(int leadId) {
        this.leadId = leadId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Lead{" + "leadId=" + leadId + ", fullName=" + fullName + ", email=" + email + ", phone=" + phone + ", course=" + course + ", status=" + status + ", createDate=" + createDate + '}';
    }
    
}
