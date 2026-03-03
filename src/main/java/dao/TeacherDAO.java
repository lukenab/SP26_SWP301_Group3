/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Schedule;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Classes;
import model.Course;
import model.Enrollment;
import model.Feedback;
import model.Room;
import model.Slot;
import model.Student;
import model.User;
import utils.DBContext;

/**
 *
 * @author ADMIN
 */
public class TeacherDAO extends DBContext {

    public List<Schedule> getTeachingSchedule(int teacherId, String selectedDate) {
        List<Schedule> list = new ArrayList<>();
        // Sửa: Cột thực tế trong DB là SlotID
        String sql = "SET DATEFIRST 1; "
                + "SELECT s.*, c.ClassName, r.RoomName "
                + "FROM Schedule s "
                + "JOIN Class c ON s.ClassID = c.ClassID "
                + "JOIN Room r ON s.RoomID = r.RoomID "
                + "WHERE s.TeacherID = ? "
                + "AND s.LearningDate BETWEEN "
                + "DATEADD(DAY, 1 - DATEPART(WEEKDAY, ?), ?) AND "
                + "DATEADD(DAY, 7 - DATEPART(WEEKDAY, ?), ?)";

        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, teacherId);
            st.setString(2, selectedDate);
            st.setString(3, selectedDate);
            st.setString(4, selectedDate);
            st.setString(5, selectedDate);
            ResultSet rs = st.executeQuery();

            SlotDAO slotDAO = new SlotDAO();

            while (rs.next()) {
                Schedule s = new Schedule();
                s.setScheduleId(rs.getInt("ScheduleID"));

                // Get Slot object from SlotDAO
                int slotId = rs.getInt("SlotID");
                Slot slot = slotDAO.getSlotByID(slotId);
                s.setSlot(slot);

                s.setLearningDate(rs.getDate("LearningDate"));
                s.setAttendanceStatus(rs.getBoolean("AttendanceStatus"));

                Classes c = new Classes();
                c.setClassid(rs.getInt("ClassID"));
                c.setClassName(rs.getString("ClassName"));
                s.setClasses(c);

                Room r = new Room();
                r.setRoomId(rs.getInt("RoomID"));
                r.setRoomName(rs.getString("RoomName"));
                s.setRoom(r);

                list.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Schedule> getScheduleByClassId(int classId, int teacherId, String selectedDate) {
        List<Schedule> list = new ArrayList<>();
        String sql = "SET DATEFIRST 1; "
                + "SELECT s.*, c.ClassName, r.RoomName "
                + "FROM Schedule s "
                + "JOIN Class c ON s.ClassID = c.ClassID "
                + "JOIN Room r ON s.RoomID = r.RoomID "
                + "WHERE s.ClassID = ? AND s.TeacherID = ? "
                + "AND s.LearningDate BETWEEN "
                + "DATEADD(DAY, 1 - DATEPART(WEEKDAY, ?), ?) AND "
                + "DATEADD(DAY, 7 - DATEPART(WEEKDAY, ?), ?)";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, classId);
            st.setInt(2, teacherId);
            st.setString(3, selectedDate);
            st.setString(4, selectedDate);
            st.setString(5, selectedDate);
            st.setString(6, selectedDate);

            ResultSet rs = st.executeQuery();

            SlotDAO slotDAO = new SlotDAO();

            while (rs.next()) {
                Schedule s = new Schedule();
                s.setScheduleId(rs.getInt("ScheduleID"));
                s.setLearningDate(rs.getDate("LearningDate"));

                // Get Slot object from SlotDAO
                int slotId = rs.getInt("SlotID");
                Slot slot = slotDAO.getSlotByID(slotId);
                s.setSlot(slot);

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

    // Get all classes with course info for academic staff
    public List<Classes> getAllClasses() {
        List<Classes> list = new ArrayList<>();
        String sql = "SELECT c.*, co.CourseName FROM Class c "
                + "JOIN Course co ON c.CourseID = co.CourseID "
                + "WHERE c.Status = 'Active' "
                + "ORDER BY c.ClassName ASC";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
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
            e.printStackTrace();
        }
        return list;
    }

    public Map<String, Object> getTeacherFeedbackData(int teacherId) {
        Map<String, Object> result = new HashMap<>();
        List<Feedback> feedbackList = new ArrayList<>();
        Map<Integer, String> studentNameMap = new HashMap<>();

        String sql = "SELECT f.FeedbackID, u.FullName AS StudentName, c.ClassName, f.Rating, f.Comment, f.SentDate "
                + "FROM Feedback f "
                + "JOIN Enrollment e ON f.EnrollmentID = e.EnrollmentID "
                + "JOIN [User] u ON e.StudentID = u.UserID "
                + "JOIN Class c ON e.ClassID = c.ClassID "
                + "WHERE c.TeacherID = ? "
                + "ORDER BY f.SentDate DESC";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, teacherId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int fId = rs.getInt("FeedbackID");

                Feedback f = new Feedback();
                f.setFeedbackId(fId);
                f.setRating(rs.getInt("Rating"));
                f.setComment(rs.getString("Comment"));
                f.setSentDate(rs.getTimestamp("SentDate").toLocalDateTime());

                Enrollment e = new Enrollment();
                Classes clazz = new Classes();
                clazz.setClassName(rs.getString("ClassName"));
                e.setClasses(clazz);
                f.setEnrollment(e);

                feedbackList.add(f);
         
                studentNameMap.put(fId, rs.getString("StudentName"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        result.put("feedbackList", feedbackList);
        result.put("studentNameMap", studentNameMap);
        return result;
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

        Map<String, Object> data = dao.getTeacherFeedbackData(3);

        List<Feedback> flist = (List<Feedback>) data.get("feedbackList");
        Map<Integer, String> names = (Map<Integer, String>) data.get("studentNameMap");

        System.out.println("--- KET QUA TEST FEEDBACK ---");
        if (list != null && !list.isEmpty()) {
            for (Feedback f : flist) {
               
                System.out.println("ID: " + f.getFeedbackId()
                        + " | Student: " + names.get(f.getFeedbackId())
                        + " | Comment: " + f.getComment());
            }
        } else {
            System.out.println("KHONG LAY DUOC DU LIEU! Kiem tra lai ket noi hoac SQL trong DAO.");
        }
    }

   
}
