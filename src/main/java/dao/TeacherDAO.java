/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import model.Schedule;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.DBContext;
/**
 *
 * @author ADMIN
 */
public class TeacherDAO extends DBContext{
    // Trong TeacherDAO.java hoặc ScheduleDAO.java

public List<Schedule> getScheduleByTeacher(int teacherId) {
    List<Schedule> list = new ArrayList<>();
    // SQL join để lấy thông tin lớp học và phòng học
    String sql = "SELECT s.*, c.ClassName, r.RoomName, r.Capacity, r.Type " +
                 "FROM Schedule s " +
                 "JOIN Class c ON s.ClassID = c.ClassID " +
                 "JOIN Room r ON s.RoomID = r.RoomID " +
                 "WHERE s.TeacherID = ? " +
                 "ORDER BY s.LearningDate ASC, s.Slot ASC";
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

            // Set thông tin Lớp học
            Classes c = new Classes();
            c.setClassid(rs.getInt("ClassID"));
            c.setClassName(rs.getString("ClassName"));
            s.setClasses(c);

            // Set thông tin Phòng học
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
}
