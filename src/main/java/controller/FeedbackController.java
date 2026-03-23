/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.ClassDAO;
import dao.FeedbackDAO;
import dao.TeacherDAO;
import dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import model.Classes;
import model.Enrollment;
import model.Feedback;
import model.User;

/**
 *
 * @author Legion
 */
@WebServlet(name = "FeedbackController", urlPatterns = {"/feedback"})
public class FeedbackController extends HttpServlet {

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
            out.println("<title>Servlet FeedbackController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet FeedbackController at " + request.getContextPath() + "</h1>");
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
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || user.getRole() == null || (user.getRole().getRoleId() != 4 && user.getRole().getRoleId() != 5)) {
            response.sendRedirect("login.jsp");
            return;
        }

        TeacherDAO teacherDAO = new TeacherDAO();
        ClassDAO classDAO = new ClassDAO();
        String action = request.getParameter("action");
        if (action == null) {
            action = "viewAll";
        }

        switch (action) {
            case "viewAll":
                String classId = request.getParameter("classId");
                String from = request.getParameter("from");
                List<Classes> classList = teacherDAO.getAllClassOfTeacherID(user.getUserId());
                request.setAttribute("classList", classList);
                request.setAttribute("from", from);
                request.setAttribute("classId", classId);
                Map<String, Object> data = teacherDAO.getTeacherFeedbackData(user.getUserId());
                request.setAttribute("feedbackList", data.get("feedbackList"));
                request.setAttribute("studentNameMap", data.get("studentNameMap"));

                request.setAttribute("home_view", "teacher/feedback_list.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;

            case "viewStudentCoursesFeedback":

                if (user.getRole().getRoleId() != 5) {
                    response.sendRedirect("login.jsp");
                    return;
                }

                int studentId = user.getUserId();

                // ================= SEARCH =================
                String keyword = request.getParameter("keyword");
                if (keyword != null && keyword.trim().isEmpty()) {
                    keyword = null;
                }

                // ================= PAGINATION =================
                int pageSize = 6;
                int page = 1;

                String pageParam = request.getParameter("page");
                if (pageParam != null) {
                    page = Integer.parseInt(pageParam);
                }

                if (page < 1) {
                    page = 1;
                }

                // ================= COUNT =================
                int total = classDAO.countClassesByStudentId(studentId, keyword);

                int totalPage = (int) Math.ceil((double) total / pageSize);
                if (totalPage == 0) {
                    totalPage = 1;
                }

                if (page > totalPage) {
                    page = totalPage;
                }

                // ================= DATA =================
                List<Object[]> classListFeedback = classDAO.getClassesByStudentIdAdvanced(
                        studentId, keyword, page, pageSize);

                request.setAttribute("classList", classListFeedback);

                request.setAttribute("keyword", keyword);
                request.setAttribute("currentPage", page);
                request.setAttribute("totalPage", totalPage);

                request.setAttribute("home_view",
                        "student/studentFeedbackCourses.jsp");

                request.getRequestDispatcher("dashboard.jsp")
                        .forward(request, response);

                break;

            case "writeFeedback":

                if (user.getRole().getRoleId() != 5) {
                    response.sendRedirect("login.jsp");
                    return;
                }

                int enrollmentId = Integer.parseInt(request.getParameter("enrollmentId"));

                List<Object[]> classes = classDAO.getClassesByStudentId(user.getUserId());

                Object[] selectedClass = null;

                for (Object[] c : classes) {
                    if ((int) c[0] == enrollmentId) {
                        selectedClass = c;
                        break;
                    }
                }

                request.setAttribute("classInfo", selectedClass);
                request.setAttribute("enrollmentId", enrollmentId);

                request.setAttribute("home_view", "student/studentFeedback.jsp");

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
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || user.getRole() == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");

        try {

            switch (action) {

                case "studentFeedback":

                    if (user.getRole().getRoleId() != 5) {
                        response.sendRedirect("login.jsp");
                        return;
                    }

                    int enrollmentId = Integer.parseInt(request.getParameter("enrollmentId"));
                    int rating = Integer.parseInt(request.getParameter("rating"));
                    String comment = request.getParameter("comment");

                    FeedbackDAO feedbackDAO = new FeedbackDAO();

                    if (feedbackDAO.isFeedbackExist(enrollmentId)) {
                        session.setAttribute("error", "You already sent feedback for this course!");
                        response.sendRedirect("feedback?action=writeFeedback&enrollmentId=" + enrollmentId);
                        return;
                    }

                    Enrollment enrollment = new Enrollment();
                    enrollment.setEnrollmentId(enrollmentId);

                    Feedback feedback = new Feedback();
                    feedback.setEnrollment(enrollment);
                    feedback.setRating(rating);
                    feedback.setComment(comment);

                    boolean result = feedbackDAO.studentFeedback(feedback);

                    if (result) {
                        session.setAttribute("success", "Feedback sent successfully!");
                    } else {
                        session.setAttribute("message", "Send feedback failed!");
                    }

                    response.sendRedirect("feedback?action=writeFeedback&enrollmentId=" + enrollmentId);
                    break;

                default:
                    response.sendRedirect("dashboard");
                    break;
            }

        } catch (Exception e) {

            e.printStackTrace();
            session.setAttribute("message", "Error: " + e.getMessage());
            response.sendRedirect("dashboard");

        }
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
