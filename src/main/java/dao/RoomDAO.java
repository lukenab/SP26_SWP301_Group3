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
                boolean status = rs.getBoolean("Status");
                allRoom.add(new Room(roomId, roomName, capacity, type, status));
            }
            return allRoom;
        } catch (Exception e) {
            e.printStackTrace();
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
                boolean status = rs.getBoolean("Status");
                return new Room(roomId, roomName, capacity, type, status);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //CHECK if Room Name already exists
    public boolean checkRoomNameExists(String roomName) {
        try {
            String query = "SELECT COUNT(*) as count FROM Room WHERE RoomName = ?";
            PreparedStatement p = conn.prepareStatement(query);
            p.setString(1, roomName);
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("count");
                return count > 0; // If count > 0, room name exists
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //CHECK if Room is being used in Schedule
    public boolean isRoomInUse(int roomId) {
        try {
            String query = "SELECT COUNT(*) as count FROM Schedule WHERE RoomID = ?";
            PreparedStatement p = conn.prepareStatement(query);
            p.setInt(1, roomId);
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("count");
                return count > 0; // If count > 0, room is in use
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //GET Classes using this room
    public List<String[]> getClassesUsingRoom(int roomId) {
        List<String[]> classList = new ArrayList<>();
        try {
            String query = "SELECT DISTINCT c.ClassID, c.ClassName, c.Status, " +
                          "co.CourseName, u.FullName as TeacherName, " +
                          "COUNT(s.ScheduleID) as TotalSchedules " +
                          "FROM Schedule s " +
                          "INNER JOIN Class c ON s.ClassID = c.ClassID " +
                          "INNER JOIN Course co ON c.CourseID = co.CourseID " +
                          "LEFT JOIN Employee e ON c.TeacherID = e.EmployeeID " +
                          "LEFT JOIN [User] u ON e.EmployeeID = u.UserID " +
                          "WHERE s.RoomID = ? " +
                          "GROUP BY c.ClassID, c.ClassName, c.Status, co.CourseName, u.FullName " +
                          "ORDER BY c.ClassID DESC";
            PreparedStatement p = conn.prepareStatement(query);
            p.setInt(1, roomId);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                String[] classInfo = new String[6];
                classInfo[0] = String.valueOf(rs.getInt("ClassID"));
                classInfo[1] = rs.getString("ClassName");
                classInfo[2] = rs.getString("Status");
                classInfo[3] = rs.getString("CourseName");
                classInfo[4] = rs.getString("TeacherName");
                classInfo[5] = String.valueOf(rs.getInt("TotalSchedules"));
                classList.add(classInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classList;
    }

    //DISABLE Room (set status to 0)
    public int disableRoom(int id) {
        try {
            String query = "UPDATE Room SET Status = 0 WHERE RoomID = ?";
            PreparedStatement p = conn.prepareStatement(query);
            p.setInt(1, id);
            int changes = p.executeUpdate();
            return changes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    //ENABLE Room (set status to 1)
    public int enableRoom(int id) {
        try {
            String query = "UPDATE Room SET Status = 1 WHERE RoomID = ?";
            PreparedStatement p = conn.prepareStatement(query);
            p.setInt(1, id);
            int changes = p.executeUpdate();
            return changes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    //DELETE Room permanently
    public int deleteRoombyID(int id) {
        try {
            String query = "DELETE FROM Room WHERE RoomID = ?";
            PreparedStatement p = conn.prepareStatement(query);
            p.setInt(1, id);
            int changes = p.executeUpdate();
            return changes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    //Create
    public int createRoom(String name, int capacity, String type, int status) {
        try {
            String query = "INSERT INTO Room (RoomName, Capacity, [Type], Status) VALUES (?, ?, ?, ?)";
            PreparedStatement p = conn.prepareStatement(query);
            p.setString(1, name);
            p.setInt(2, capacity);
            p.setString(3, type);
            p.setInt(4, status);
            int changes = p.executeUpdate();
            return changes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    //Update
    public int updateRoom(int id, String name, int capacity, String type, int status) {
        try {
            String query = "UPDATE Room SET RoomName = ?, Capacity = ?, [Type] = ?, Status = ? WHERE RoomID = ?";
            PreparedStatement p = conn.prepareStatement(query);
            p.setString(1, name);
            p.setInt(2, capacity);
            p.setString(3, type);
            p.setInt(4, status);
            p.setInt(5, id);
            int changes = p.executeUpdate();
            return changes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
//    public static void main(String[] args) {
//        RoomDAO dao = new RoomDAO();
////        List<Room> allRoom = dao.getAllRoom();
//////        for (Room room : allRoom) {
//////            System.out.println(room);
//////        }
////        if(allRoom.isEmpty()){
////            System.out.println("Empty");
////        } else {
////            System.out.println("OK");
////        }
//        int changes = dao.updateRoom(25, "Hehe", 1, "hehhehe");
//        if(changes!=-1){
//            System.out.println("OK");
//        } else {
//            System.out.println("Bugggg");
//        }
//    }
}
