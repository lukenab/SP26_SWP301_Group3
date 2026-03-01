/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.SettingDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Setting;
import model.User;

/**
 *
 * @author Legion
 */
@WebServlet(name = "SettingController", urlPatterns = {"/setting"})
public class SettingController extends HttpServlet {

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

        SettingDAO settingDAO = new SettingDAO();

        User currentUser = (User) request.getSession().getAttribute("user");
        if (currentUser == null || currentUser.getRole().getRoleId() != 1) {
            HttpSession errSession = request.getSession();
            errSession.setAttribute("message", "Access Denied! You don't have permission.");
            errSession.setAttribute("messageType", "error");
            response.sendRedirect("dashboard");
            return;
        }

        List<Setting> list = settingDAO.getAllSetting();
        Map<String, String> settingMap = new HashMap<>();
        for (Setting setting : list) {
            settingMap.put(setting.getSettingKey(), setting.getSettingValue());
        }

        request.setAttribute("settings", settingMap);
        request.setAttribute("home_view", "/admin/manageSetting.jsp");
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);

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
        SettingDAO settingDAO = new SettingDAO();

        String[] keys = {
            "DEFAULT_PASSING_GRADE", "MAX_STUDENTS_PER_CLASS", "MAX_ABSENCE_PERCENTAGE",
            "PAYMENT_GRACE_PERIOD_DAYS",
            "MAX_LOGIN_ATTEMPTS", "SESSION_TIMEOUT_MINUTES"
        };

        boolean allSuccess = true;
        for (String key : keys) {
            String val = request.getParameter(key);
            if (val != null && !val.trim().isEmpty()) {
                boolean updated = settingDAO.updateSetting(key, val.trim());
                if (!updated) {
                    allSuccess = false;
                }
            }
        }
        HttpSession session = request.getSession();
        if (allSuccess) {
            session.setAttribute("message", "System settings updated successfully!");
            session.setAttribute("messageType", "success");
        } else {
            session.setAttribute("message", "There was an error updating some settings.");
            session.setAttribute("messageType", "error");
        }
        response.sendRedirect("setting");
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
