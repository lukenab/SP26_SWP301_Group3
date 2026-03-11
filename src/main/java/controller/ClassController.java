/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.ClassDAO;
import dao.EnrollmentDAO;
import dao.ScheduleDAO;
import dao.TeacherDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import model.Classes;
import model.Schedule;
import model.User;

/**
 *
 * @author Legion
 */
@WebServlet(name = "ClassController", urlPatterns = {"/class"})
public class ClassController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ClassController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ClassController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        TeacherDAO dao = new TeacherDAO();
        ClassDAO classDAO = new ClassDAO();
        ScheduleDAO scheduleDAO = new ScheduleDAO();
        String action = request.getParameter("action");
        if (action == null) {
            action = "all";
        }
        switch (action) {

            case "all":

                User user = (User) request.getSession().getAttribute("user");
                if (user != null) {
                    int teacherID = user.getUserId();
                    List<Classes> list = dao.getAllClassOfTeacherID(teacherID);
                    request.setAttribute("ClassList", list);
                    request.setAttribute("home_view", "teacher/teacher_classlist.jsp");
                    request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                } else {
                    response.sendRedirect("login.jsp");
                }
                break;

            case "availableClass":

                List<Object[]> classList = classDAO.getOpenClassesForStudent();

                for (Object[] row : classList) {

                    int classId = (int) row[0];

                    List<Schedule> schedules = scheduleDAO.getSchedulesByClass(classId);

                    Set<String> days = new LinkedHashSet<>();
                    String timeRange = "";

                    for (Schedule s : schedules) {

                        java.time.LocalDate learningDate
                                = ((java.sql.Date) s.getLearningDate()).toLocalDate();

                        java.time.DayOfWeek d = learningDate.getDayOfWeek();

                        switch (d) {
                            case MONDAY:
                                days.add("Mon");
                                break;
                            case TUESDAY:
                                days.add("Tue");
                                break;
                            case WEDNESDAY:
                                days.add("Wed");
                                break;
                            case THURSDAY:
                                days.add("Thu");
                                break;
                            case FRIDAY:
                                days.add("Fri");
                                break;
                            case SATURDAY:
                                days.add("Sat");
                                break;
                            case SUNDAY:
                                days.add("Sun");
                                break;
                        }

                        if (timeRange.isEmpty()) {
                            timeRange = s.getSlot().getStartTime() + " - " + s.getSlot().getEndTime();
                        }
                    }

                    String dayString = String.join("-", days);

                    Object[] newRow = new Object[10];

                    System.arraycopy(row, 0, newRow, 0, row.length);

                    newRow[8] = dayString;
                    newRow[9] = timeRange;

                    int index = classList.indexOf(row);
                    classList.set(index, newRow);
                }

                request.setAttribute("classList", classList);

                request.setAttribute("home_view", "student/studentClassList.jsp");

                request.getRequestDispatcher("dashboard.jsp").forward(request, response);

                break;

            case "detail":

                String classIdRaw = request.getParameter("classId");
                int classId = Integer.parseInt(classIdRaw);

                // lấy nguồn trang
                String source = request.getParameter("source");
                if (source == null) {
                    source = "availableClass"; // mặc định
                }

                Object[] data = classDAO.getClassDetail(classId);

                Classes classDetail = (Classes) data[0];
                String teacherName = (String) data[1];
                String roomName = (String) data[2];

                List<Schedule> schedules = scheduleDAO.getSchedulesByClass(classId);

                request.setAttribute("classDetail", classDetail);
                request.setAttribute("teacherName", teacherName);
                 request.setAttribute("roomName", roomName); 
                request.setAttribute("scheduleList", schedules);

                // truyền sang JSP để breadcrumb biết nguồn
                request.setAttribute("sourcePage", source);

                request.setAttribute("home_view", "student/classDetail.jsp");

                request.getRequestDispatcher("dashboard.jsp").forward(request, response);

                break;

            case "myClasses":

                User student = (User) request.getSession().getAttribute("user");

                if (student == null) {
                    response.sendRedirect("login.jsp");
                    return;
                }

                int studentId = student.getUserId();

                List<Object[]> myClassList = classDAO.getStudentClasses(studentId);

                request.setAttribute("classList", myClassList);

                request.setAttribute("home_view", "student/studentMyClassList.jsp");

                request.getRequestDispatcher("dashboard.jsp").forward(request, response);

                break;
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
