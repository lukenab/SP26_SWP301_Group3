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
public class Feedback {
    private int feedbackId;
    private Enrollment enrollment;
    private int rating;
    private String comment;
    private LocalDateTime sentDate;

    public Feedback() {
    }

    public Feedback(int feedbackId, Enrollment enrollment, int rating, String comment, LocalDateTime sentDate) {
        this.feedbackId = feedbackId;
        this.enrollment = enrollment;
        this.rating = rating;
        this.comment = comment;
        this.sentDate = sentDate;
    }

    public LocalDateTime getSentDate() {
        return sentDate;
    }

    public void setSentDate(LocalDateTime sentDate) {
        this.sentDate = sentDate;
    }

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public Enrollment getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Feedback{" + "feedbackId=" + feedbackId + ", enrollment=" + enrollment + ", rating=" + rating + ", comment=" + comment + ", sentDate=" + sentDate + '}';
    }
    
}
