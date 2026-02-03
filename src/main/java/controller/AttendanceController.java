/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AttendanceDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "AttendanceController", urlPatterns = {"/attendance"})
public class AttendanceController extends HttpServlet {

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
            out.println("<title>Servlet AttendanceController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AttendanceController at " + request.getContextPath() + "</h1>");
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
        String action = request.getParameter("action");
        if (action == null) {
            action = "take";
        }

        AttendanceDAO dao = new AttendanceDAO();

        switch (action) {
            case "take":
                try {
                    int scheduleId = Integer.parseInt(request.getParameter("scheduleId"));
                    // Lấy danh sách Object[] (ID, Name, UserID, Status, Note)
                    List<Object[]> attList = dao.getAttendanceList(scheduleId);

                    request.setAttribute("attList", attList);
                    request.setAttribute("scheduleId", scheduleId);
                    request.setAttribute("home_view", "take_attendance.jsp");
                    request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                } catch (Exception e) {
                    response.sendRedirect("schedule?action=view");
                }
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
        String action = request.getParameter("action");
        AttendanceDAO dao = new AttendanceDAO();

        switch (action) {
            case "save":
                try {
                    // Lấy mảng ID điểm danh để duyệt
                    String[] attIds = request.getParameterValues("attId");
                    if (attIds != null) {
                        for (String id : attIds) {
                            // Lấy status và note tương ứng từng ID
                            String status = request.getParameter("status_" + id);
                            String note = request.getParameter("note_" + id);

                            // Gọi hàm update trong DAO
                            dao.updateAttendance(Integer.parseInt(id), status, note);
                        }
                    }
                    response.sendRedirect("schedule?action=view&msg=Success");
                } catch (Exception e) {
                    response.sendRedirect("schedule?action=view&msg=Error");
                }
                break;
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
