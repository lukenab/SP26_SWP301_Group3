/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigDecimal;

/**
 *
 * @author phuct
 */
class Course {
    private int courseId;
    private String courseName;
    private String description;
    private int totalSlots;
    private BigDecimal tuitionFee;
    private boolean status;

    public Course() {
    }

    public Course(int courseId, String courseName, String description, int totalSlots, BigDecimal tuitionFee, boolean status) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.description = description;
        this.totalSlots = totalSlots;
        this.tuitionFee = tuitionFee;
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTotalSlots() {
        return totalSlots;
    }

    public void setTotalSlots(int totalSlots) {
        this.totalSlots = totalSlots;
    }

    public BigDecimal getTuitionFee() {
        return tuitionFee;
    }

    public void setTuitionFee(BigDecimal tuitionFee) {
        this.tuitionFee = tuitionFee;
    }

    @Override
    public String toString() {
        return "Course{" + "courseId=" + courseId + ", courseName=" + courseName + ", description=" + description + ", totalSlots=" + totalSlots + ", tuitionFee=" + tuitionFee + ", status=" + status + '}';
    }
    
}
