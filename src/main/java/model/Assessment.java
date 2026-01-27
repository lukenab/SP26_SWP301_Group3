/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author phuct
 */
public class Assessment {
    private int assessmentId;
    private Course course;
    private String assessmentName;
    private double weight;

    public Assessment() {
    }

    public Assessment(int assessmentId, Course course, String assessmentName, double weight) {
        this.assessmentId = assessmentId;
        this.course = course;
        this.assessmentName = assessmentName;
        this.weight = weight;
    }

    public int getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(int assessmentId) {
        this.assessmentId = assessmentId;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getAssessmentName() {
        return assessmentName;
    }

    public void setAssessmentName(String assessmentName) {
        this.assessmentName = assessmentName;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Assessment{" + "assessmentId=" + assessmentId + ", course=" + course + ", assessmentName=" + assessmentName + ", weight=" + weight + '}';
    }

   
  
  
}
