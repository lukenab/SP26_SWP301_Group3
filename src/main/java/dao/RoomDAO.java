/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import model.Room;
import utils.DBContext;

/**
 *
 * @author Administrator
 */
public class RoomDAO extends DBContext {

    public List getAllRoom() {
        try {
            List<Room> allRoom = null;
            String query = "SELECT * FROM Room";
            PreparedStatement p = conn.prepareStatement(query);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                int roomId = rs.getInt("RoomID");
                String roomName = rs.getString("RoomName");
                int capacity = rs.getInt("Capacity");
                String type = rs.getString("Type");
                allRoom.add(new Room(roomId, roomName, capacity, type));
            }
            return allRoom;
        } catch (Exception e) {

        }
        return null;
    }
    public Room getRoomByID(int id){
        try {
            String query = "select * from Room where RoomID = ?";
            PreparedStatement p = conn.prepareStatement(query);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                int roomId = rs.getInt("RoomID");
                String roomName = rs.getString("RoomName");
                int capacity = rs.getInt("Capacity");
                String type = rs.getString("Type");
                return new Room(roomId, roomName, capacity, type);
            }
        } catch (Exception e) {

        }
        return null;
    }
    public int deleteRoombyID(int id){
        try {
            String query = "select * from Room where RoomID = ?";
            PreparedStatement p = conn.prepareStatement(query);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                int roomId = rs.getInt("RoomID");
                String roomName = rs.getString("RoomName");
                int capacity = rs.getInt("Capacity");
                String type = rs.getString("Type");
                return new Room(roomId, roomName, capacity, type);
            }
        } catch (Exception e) {

        }
        return null;
    }
}
