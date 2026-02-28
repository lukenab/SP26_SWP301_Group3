package model;

import java.time.LocalTime;

/**
 *
 * @author Administrator
 */
public class Slot {
    private int slotID;
    private LocalTime startTime;
    private LocalTime endTime;

    public Slot() {
    }

    public Slot(int slotID, LocalTime startTime, LocalTime endTime) {
        this.slotID = slotID;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getSlotID() {
        return slotID;
    }

    public void setSlotID(int slotID) {
        this.slotID = slotID;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Slot{" + "slotID=" + slotID + ", startTime=" + startTime + ", endTime=" + endTime + '}';
    }
}