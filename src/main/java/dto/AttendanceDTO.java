package dto;

import model.Attendance;

public class AttendanceDTO {

    private Attendance attendance;
    private String fullName;
    private int userId;

    public AttendanceDTO() {
    }

    public AttendanceDTO(Attendance attendance, String fullName, int userId) {
        this.attendance = attendance;
        this.fullName = fullName;
        this.userId = userId;
    }

    public Attendance getAttendance() {
        return attendance;
    }

    public void setAttendance(Attendance attendance) {
        this.attendance = attendance;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}