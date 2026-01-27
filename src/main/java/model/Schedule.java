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
class Schedule {
    private int scheduleId;
    private Classes classes;
    private Room room;
    private int slot;
    private Date learningDate;
    private Employee employee;
    private boolean attendanceStatus;

    public Schedule() {
    }

    public Schedule(int scheduleId, Classes classes, Room room, int slot, Date learningDate, Employee employee, boolean attendanceStatus) {
        this.scheduleId = scheduleId;
        this.classes = classes;
        this.room = room;
        this.slot = slot;
        this.learningDate = learningDate;
        this.employee = employee;
        this.attendanceStatus = attendanceStatus;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Classes getClasses() {
        return classes;
    }

    public void setClasses(Classes classes) {
        this.classes = classes;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public Date getLearningDate() {
        return learningDate;
    }

    public void setLearningDate(Date learningDate) {
        this.learningDate = learningDate;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public boolean isAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(boolean attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    @Override
    public String toString() {
        return "Schedule{" + "scheduleId=" + scheduleId + ", classes=" + classes + ", room=" + room + ", slot=" + slot + ", learningDate=" + learningDate + ", employee=" + employee + ", attendanceStatus=" + attendanceStatus + '}';
    }

    
    
}
