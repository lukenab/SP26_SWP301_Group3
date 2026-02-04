/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Room;
import utils.DBContext;

/**
 *
 * @author Administrator
 */
public class RoomDAO extends DBContext {

    //READ
    public List<Room> getAllRoom() {
        try {
            List<Room> allRoom = new ArrayList<>();
            String query = "select * from Room";
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

    //READ
    public Room getRoomByID(int id) {
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

    //DELETE
    public int deleteRoombyID(int id) {
        try {
            String query = "Delete from Room where \n"
                    + "RoomID = ?";
            PreparedStatement p = conn.prepareStatement(query);
            p.setInt(1, id);
            int changes = p.executeUpdate();
            return changes;
        } catch (Exception e) {

        }
        return -1;
    }

    //Create
    public int createRoom(String name, int capacity, String type) {
        try {
            String query = "insert into Room \n"
                    + "(RoomName, Capacity, [Type]) Values\n"
                    + "(?, ?, ?)";
            PreparedStatement p = conn.prepareStatement(query);
            p.setString(1, name);
            p.setInt(2, capacity);
            p.setString(3, type);
            int changes = p.executeUpdate();
            return changes;
        } catch (Exception e) {

        }
        return -1;
    }

    //Update
    public int updateRoom(int id, String name, int capacity, String type) {
        try {
            String query = "Update Room set RoomName = "
                    + "?, Capacity = ?, "
                    + "[Type] = ?\n"
                    + "where RoomID = ?";
            PreparedStatement p = conn.prepareStatement(query);
            p.setString(1, name);
            p.setInt(2, capacity);
            p.setString(3, type);
            p.setInt(4, id);
            int changes = p.executeUpdate();
            return changes;
        } catch (Exception e) {

        }
        return -1;
    }
    public static void main(String[] args) {
        RoomDAO dao = new RoomDAO();
//        List<Room> allRoom = dao.getAllRoom();
////        for (Room room : allRoom) {
////            System.out.println(room);
////        }
//        if(allRoom.isEmpty()){
//            System.out.println("Empty");
//        } else {
//            System.out.println("OK");
//        }
        int changes = dao.updateRoom(25, "Hehe", 1, "hehhehe");
        if(changes!=-1){
            System.out.println("OK");
        } else {
            System.out.println("Bugggg");
        }
    }
}
