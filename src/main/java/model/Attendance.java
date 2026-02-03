/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author phuct
 */
public class Attendance {
    private int attendanceId;
    private Schedule schedule;
    private Enrollment enrolmentId;
    private String status;
    private String note;

    public Attendance() {
    }

    public Attendance(int attendanceId, Schedule schedule, Enrollment enrolmentId, String status, String note) {
        this.attendanceId = attendanceId;
        this.schedule = schedule;
        this.enrolmentId = enrolmentId;
        this.status = status;
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(int attendanceId) {
        this.attendanceId = attendanceId;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Enrollment getEnrolmentId() {
        return enrolmentId;
    }

    public void setEnrolmentId(Enrollment enrolmentId) {
        this.enrolmentId = enrolmentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Attendance{" + "attendanceId=" + attendanceId + ", schedule=" + schedule + ", enrolmentId=" + enrolmentId + ", status=" + status + ", note=" + note + '}';
    }


}
