/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author phuct
 */
public class Grade {
    private int gradeId;
    private Enrollment enrollment;
    private Assessment assessment;
    private double score;

    public Grade() {
    }

    public Grade(int gradeId, Enrollment enrollment, Assessment assessment, double score) {
        this.gradeId = gradeId;
        this.enrollment = enrollment;
        this.assessment = assessment;
        this.score = score;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public Enrollment getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    public Assessment getAssessment() {
        return assessment;
    }

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }

    @Override
    public String toString() {
        return "Grade{" + "gradeId=" + gradeId + ", enrollment=" + enrollment + ", assessment=" + assessment + ", score=" + score + '}';
    }
    
}
