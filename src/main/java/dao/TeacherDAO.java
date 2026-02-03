/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Schedule;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Classes;
import model.Room;
import utils.DBContext;

/**
 *
 * @author ADMIN
 */
public class TeacherDAO extends DBContext {

    public List<Schedule> getTeachingSchedule(int teacherId) {
        List<Schedule> list = new ArrayList<>();
        String sql = "SELECT s.*, c.ClassName, r.RoomName, r.Capacity, r.Type "
                + "FROM Schedule s "
                + "JOIN Class c ON s.ClassID = c.ClassID "
                + "JOIN Room r ON s.RoomID = r.RoomID "
                + "WHERE s.TeacherID = ? "
                + "ORDER BY s.LearningDate ASC, s.Slot ASC";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, teacherId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Schedule s = new Schedule();
                s.setScheduleId(rs.getInt("ScheduleId"));
                s.setSlot(rs.getInt("Slot"));
                s.setLearningDate(rs.getDate("LearningDate"));
                s.setAttendanceStatus(rs.getBoolean("AttendanceStatus"));

                Classes c = new Classes();
                c.setClassid(rs.getInt("ClassID"));
                c.setClassName(rs.getString("ClassName"));
                s.setClasses(c);

                Room room = new Room();
                room.setRoomId(rs.getInt("RoomID"));
                room.setRoomName(rs.getString("RoomName"));
                room.setCapacity(rs.getInt("Capacity"));
                room.setType(rs.getString("Type"));
                s.setRoom(room);

                list.add(s);
            }
        } catch (SQLException e) {
            System.out.println("getScheduleByTeacher: " + e.getMessage());
        }
        return list;
    }

    public static void main(String[] args) {

        TeacherDAO dao = new TeacherDAO();
        int testTeacherID = 2; 

        System.out.println("--- TESTING TEACHING SCHEDULE FOR TEACHER ID: " + testTeacherID + " ---");

        
        List<Schedule> list = dao.getTeachingSchedule(testTeacherID);

        if (list == null) {
            System.out.println("ERROR: Method returned NULL. Please check DBContext connection!");
        } else if (list.isEmpty()) {
            System.out.println("NOTIFICATION: Connection successful, but NO schedule found for this teacher.");
            System.out.println("Please ensure data is inserted into the 'Schedule' table for TeacherID = " + testTeacherID);
        } else {
            System.out.println("SUCCESS: Found " + list.size() + " teaching session(s).");
            System.out.println("--------------------------------------------------");
            for (Schedule s : list) {
         
                System.out.println("Schedule ID: " + s.getScheduleId());
                System.out.println("Date: " + s.getLearningDate());
                System.out.println("Slot: " + s.getSlot());
                System.out.println("Class Info: " + s.getClasses());
                System.out.println("Attendance Status: " + (s.isAttendanceStatus() ? "Attended" : "Not yet"));
                System.out.println("--------------------------------------------------");
            }
        }
    }
}
