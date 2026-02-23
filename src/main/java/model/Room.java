/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author phuct
 */
public class Room {
    private int roomId;
    private String roomName;
    private int capacity;
    private String type;
    private boolean status; // true: Active, false: Disabled

    public Room() {
    }

    public Room(int roomId, String roomName, int capacity, String type) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.capacity = capacity;
        this.type = type;
        this.status = true; // Default is active
    }

    public Room(int roomId, String roomName, int capacity, String type, boolean status) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.capacity = capacity;
        this.type = type;
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "Room{" + "roomId=" + roomId + ", roomName=" + roomName + ", capacity=" + capacity + ", type=" + type + ", status=" + status + '}';
    }
    
}
