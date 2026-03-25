/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.Room;
import utils.DBContext;

/**
 *
 * @author Administrator
 */
public class RoomDAO extends DBContext {

    private static Boolean cachedHasClassRoomIdColumn;

    private boolean hasClassRoomIdColumn() {
        if (cachedHasClassRoomIdColumn != null) {
            return cachedHasClassRoomIdColumn;
        }
        try {
            String query = "SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS "
                    + "WHERE TABLE_NAME = 'Class' AND COLUMN_NAME = 'RoomID'";
            PreparedStatement p = conn.prepareStatement(query);
            ResultSet rs = p.executeQuery();
            cachedHasClassRoomIdColumn = rs.next();
            return cachedHasClassRoomIdColumn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

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

    public List<Room> getActiveRooms() {
        try {
            List<Room> activeRooms = new ArrayList<>();
            String query = "SELECT * FROM Room WHERE Status = 1 ORDER BY RoomName";
            PreparedStatement p = conn.prepareStatement(query);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                int roomId = rs.getInt("RoomID");
                String roomName = rs.getString("RoomName");
                int capacity = rs.getInt("Capacity");
                String type = rs.getString("Type");
                boolean status = rs.getBoolean("Status");
                activeRooms.add(new Room(roomId, roomName, capacity, type, status));
            }
            return activeRooms;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get all distinct room types from database
    public List<String> getDistinctRoomTypes() {
        try {
            List<String> types = new ArrayList<>();
            String query = "SELECT DISTINCT [Type] FROM Room WHERE [Type] IS NOT NULL ORDER BY [Type]";
            PreparedStatement p = conn.prepareStatement(query);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                String type = rs.getString("Type");
                types.add(type);
            }
            return types;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    // Get all distinct room capacities from database
    public List<Integer> getDistinctRoomCapacities() {
        try {
            List<Integer> capacities = new ArrayList<>();
            String query = "SELECT DISTINCT Capacity FROM Room ORDER BY Capacity";
            PreparedStatement p = conn.prepareStatement(query);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                int capacity = rs.getInt("Capacity");
                capacities.add(capacity);
            }
            return capacities;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    // Filter rooms by capacity, type, and status
    public List<Room> getRoomsByFilter(String capacityStr, String typeStr, String statusStr) {
        try {
            List<Room> filteredRooms = new ArrayList<>();
            StringBuilder query = new StringBuilder("SELECT * FROM Room WHERE 1=1");

            if (capacityStr != null && !capacityStr.isEmpty()) {
                int capacity = Integer.parseInt(capacityStr);
                if (capacity < 100) {
                    query.append(" AND Capacity <= ?");
                } else {
                    query.append(" AND Capacity >= ?");
                }
            }

            if (typeStr != null && !typeStr.isEmpty()) {
                query.append(" AND [Type] = ?");
            }

            if (statusStr != null && !statusStr.isEmpty()) {
                int status = statusStr.equalsIgnoreCase("active") ? 1 : 0;
                query.append(" AND Status = ?");
            }

            query.append(" ORDER BY RoomName");

            PreparedStatement p = conn.prepareStatement(query.toString());
            int paramIndex = 1;

            if (capacityStr != null && !capacityStr.isEmpty()) {
                int capacity = Integer.parseInt(capacityStr);
                p.setInt(paramIndex++, capacity);
            }

            if (typeStr != null && !typeStr.isEmpty()) {
                p.setString(paramIndex++, typeStr);
            }

            if (statusStr != null && !statusStr.isEmpty()) {
                int status = statusStr.equalsIgnoreCase("active") ? 1 : 0;
                p.setInt(paramIndex++, status);
            }

            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                int roomId = rs.getInt("RoomID");
                String roomName = rs.getString("RoomName");
                int capacity = rs.getInt("Capacity");
                String type = rs.getString("Type");
                boolean roomStatus = rs.getBoolean("Status");
                filteredRooms.add(new Room(roomId, roomName, capacity, type, roomStatus));
            }
            return filteredRooms;
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
            System.out.println(e.getMessage());;
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
            boolean hasClassRoomId = hasClassRoomIdColumn();
            String query;
            if (hasClassRoomId) {
                query = "SELECT "
                        + "(SELECT COUNT(*) FROM Schedule WHERE RoomID = ?) + "
                        + "(SELECT COUNT(*) FROM Class WHERE RoomID = ?) as count";
            } else {
                query = "SELECT COUNT(*) as count FROM Schedule WHERE RoomID = ?";
            }
            PreparedStatement p = conn.prepareStatement(query);
            p.setInt(1, roomId);
            if (hasClassRoomId) {
                p.setInt(2, roomId);
            }
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
            boolean hasClassRoomId = hasClassRoomIdColumn();
            String query;
            if (hasClassRoomId) {
                query = "SELECT c.ClassID, c.ClassName, c.Status, "
                        + "co.CourseName, u.FullName as TeacherName, "
                        + "COUNT(s.ScheduleID) as TotalSchedules "
                        + "FROM Class c "
                        + "INNER JOIN Course co ON c.CourseID = co.CourseID "
                        + "LEFT JOIN Employee e ON c.TeacherID = e.EmployeeID "
                        + "LEFT JOIN [User] u ON e.EmployeeID = u.UserID "
                        + "LEFT JOIN Schedule s ON s.ClassID = c.ClassID AND s.RoomID = ? "
                        + "WHERE c.RoomID = ? OR s.RoomID = ? "
                        + "GROUP BY c.ClassID, c.ClassName, c.Status, co.CourseName, u.FullName "
                        + "ORDER BY c.ClassID DESC";
            } else {
                query = "SELECT DISTINCT c.ClassID, c.ClassName, c.Status, "
                        + "co.CourseName, u.FullName as TeacherName, "
                        + "COUNT(s.ScheduleID) as TotalSchedules "
                        + "FROM Schedule s "
                        + "INNER JOIN Class c ON s.ClassID = c.ClassID "
                        + "INNER JOIN Course co ON c.CourseID = co.CourseID "
                        + "LEFT JOIN Employee e ON c.TeacherID = e.EmployeeID "
                        + "LEFT JOIN [User] u ON e.EmployeeID = u.UserID "
                        + "WHERE s.RoomID = ? "
                        + "GROUP BY c.ClassID, c.ClassName, c.Status, co.CourseName, u.FullName "
                        + "ORDER BY c.ClassID DESC";
            }
            PreparedStatement p = conn.prepareStatement(query);
            p.setInt(1, roomId);
            if (hasClassRoomId) {
                p.setInt(2, roomId);
                p.setInt(3, roomId);
            }
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
            System.out.println(e.getMessage());
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
    
    public int getRoomCapacity(int roomId) {
        String query = "SELECT Capacity FROM Room WHERE RoomID = ?";
        try (PreparedStatement p = conn.prepareStatement(query)) {
            p.setInt(1, roomId);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Capacity");
                }
            }
        } catch (Exception e) {
            System.out.println("Error getRoomCapacity: " + e.getMessage());
        }
        return 0; 
    }
    public static void main(String[] args) {
        RoomDAO dao = new RoomDAO();
        Scanner sc = new Scanner(System.in);
        String name = null;
        int id  = sc.nextInt();
        sc.nextLine();
//        int capacity = 30;
//        String type = "null name";
//        int status = 1;
//        System.out.println(dao.createRoom(name, capacity, type, 1));
//        List<Room> all = dao.getAllRoom();
//        System.out.println(all.get(all.size()-1).getType());
//        System.out.println(dao.checkRoomNameExists(name));
        System.out.println(dao.getRoomByID(id));

    }
}
