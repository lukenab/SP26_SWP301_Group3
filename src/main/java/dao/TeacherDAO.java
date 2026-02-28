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
import model.Attendance;
import model.Classes;
import model.Course;
import model.Enrollment;
import model.Room;
import model.Student;
import model.User;
import utils.DBContext;

/**
 *
 * @author ADMIN
 */
public class TeacherDAO extends DBContext {

    public List<Schedule> getTeachingSchedule(int teacherId, String startDate) {
        List<Schedule> list = new ArrayList<>();

        String sql = "SELECT s.*, c.ClassName, r.RoomName "
                + "FROM Schedule s "
                + "JOIN Class c ON s.ClassID = c.ClassID "
                + "JOIN Room r ON s.RoomID = r.RoomID "
                + "WHERE s.TeacherID = ? "
                + "AND s.LearningDate BETWEEN ? AND DATEADD(day, 6, ?)";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, teacherId);
            st.setString(2, startDate);
            st.setString(3, startDate);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                Schedule s = new Schedule();
                s.setScheduleId(rs.getInt("ScheduleID"));
                s.setSlot(rs.getInt("Slot"));
                s.setLearningDate(rs.getDate("LearningDate"));
                s.setAttendanceStatus(rs.getBoolean("AttendanceStatus"));

                model.Classes c = new model.Classes();
                c.setClassid(rs.getInt("ClassID"));
                c.setClassName(rs.getString("ClassName"));
                s.setClasses(c);

                model.Room r = new model.Room();
                r.setRoomId(rs.getInt("RoomID"));
                r.setRoomName(rs.getString("RoomName"));
                s.setRoom(r);

                list.add(s);
            }
        } catch (Exception e) {
            System.out.println("getTeachingSchedule error: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public List<Schedule> getScheduleByClassId(int classId, int teacherId) {

        List<Schedule> list = new ArrayList<>();

        String sql = "SELECT s.*, c.ClassName, r.RoomName "
                + "FROM Schedule s "
                + "JOIN Class c ON s.ClassID = c.ClassID "
                + "JOIN Room r ON s.RoomID = r.RoomID "
                + "WHERE s.ClassID = ? "
                + "AND s.TeacherID = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, classId);
            st.setInt(2, teacherId);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {

                Schedule s = new Schedule();
                s.setScheduleId(rs.getInt("ScheduleID"));
                s.setLearningDate(rs.getDate("LearningDate"));
                s.setSlot(rs.getInt("Slot"));
                s.setAttendanceStatus(rs.getBoolean("AttendanceStatus"));

                Room room = new Room();
                room.setRoomName(rs.getString("RoomName"));
                s.setRoom(room);

                Classes c = new Classes();
                c.setClassName(rs.getString("ClassName"));
                s.setClasses(c);

                list.add(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Classes> getAllClassOfTeacherID(int teacherID) {
        List<Classes> list = new ArrayList<>();
        String sql = "SELECT c.*, co.CourseName FROM Class c "
                + "JOIN Course co ON c.CourseID = co.CourseID "
                + "WHERE c.TeacherID = ?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, teacherID);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Classes c = new Classes();
                c.setClassid(rs.getInt("ClassID"));
                c.setClassName(rs.getString("ClassName"));
                c.setStartDate(rs.getDate("StartDate"));
                c.setEndDate(rs.getDate("EndDate"));
                c.setStatus(rs.getString("Status"));
                Course course = new Course();
                course.setCourseId(rs.getInt("CourseID"));
                course.setCourseName(rs.getString("CourseName"));
                c.setCourse(course);
                list.add(c);

            }
        } catch (Exception e) {
        }
        return list;
    }

    public static void main(String[] args) {
        TeacherDAO dao = new TeacherDAO();

        int testTeacherID = 3;

        String startDate = "2026-02-23";

        System.out.println("--- TESTING TEACHING SCHEDULE FOR TEACHER ID: " + testTeacherID + " ---");
        System.out.println("Start Date Filter: " + startDate);

        List<Schedule> list = dao.getTeachingSchedule(testTeacherID, startDate);

        if (list == null) {
            System.out.println("ERROR: Method returned NULL. Please check DBContext connection!");
        } else if (list.isEmpty()) {
            System.out.println("NOTIFICATION: Connection successful, but NO schedule found for this teacher in this week.");
            System.out.println("Check if LearningDate in DB is between " + startDate + " and 2026-03-01");
        } else {
            System.out.println("SUCCESS: Found " + list.size() + " teaching session(s).");
            System.out.println("--------------------------------------------------");
            for (Schedule s : list) {
                System.out.println("Schedule ID: " + s.getScheduleId());
                System.out.println("Date: " + s.getLearningDate());
                System.out.println("Slot: " + s.getSlot());

                if (s.getClasses() != null) {
                    System.out.println("Class: " + s.getClasses().getClassName() + " (ID: " + s.getClasses().getClassid() + ")");
                }
                if (s.getRoom() != null) {
                    System.out.println("Room: " + s.getRoom().getRoomName());
                }
                System.out.println("Attendance Status: " + (s.isAttendanceStatus() ? "Attended (GREEN)" : "Not yet (YELLOW)"));
                System.out.println("--------------------------------------------------");
            }
        }
    }
}
