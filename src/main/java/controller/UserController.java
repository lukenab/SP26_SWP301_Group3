/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.EmployeeDAO;
import dao.RoleDAO;
import dao.StudentDAO;
import dao.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import model.Employee;
import model.Role;
import model.Student;
import model.User;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;
import java.io.File;

/**
 *
 * @author Legion
 */
@WebServlet(name = "UserController", urlPatterns = {"/user"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50
)
public class UserController extends HttpServlet {

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
        UserDAO userDAO = new UserDAO();
        RoleDAO roleDAO = new RoleDAO();
        EmployeeDAO employeeDAO = new EmployeeDAO();
        StudentDAO studentDAO = new StudentDAO();
        String action = request.getParameter("action");
        if (action == null) {
            action = "all";
        }

        switch (action) {
            case "all":
                String seachQuery = request.getParameter("searchQuery");
                String searchRoleId = request.getParameter("roleId");
                String searchStatus = request.getParameter("status");

                List<User> list = userDAO.searchAndFilterUsers(seachQuery, searchRoleId, searchStatus);

                int totalUsers = list.size();
                request.setAttribute("totalUsers", totalUsers);
                request.setAttribute("userList", list);
                request.setAttribute("home_view", "/admin/manageUser.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;
            case "add":
                List<Role> roleList = roleDAO.getAllRole();
                request.setAttribute("roleList", roleList);
                request.setAttribute("home_view", "/admin/createUser.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;
            case "inActivate":
                int dId = Integer.parseInt(request.getParameter("id"));
                User uDelete = userDAO.getUserById(dId);
                request.setAttribute("uDelete", uDelete);
                request.setAttribute("home_view", "/admin/deleteUser.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;
            case "view":
                int id = Integer.parseInt(request.getParameter("id"));
                User user = userDAO.getUserById(id);
                Student student = studentDAO.getStudentById(id);
                Employee employee = employeeDAO.getEmployeeById(id);
                request.setAttribute("employee", employee);
                request.setAttribute("student", student);
                request.setAttribute("user", user);
                request.setAttribute("home_view", "/admin/viewUser.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;
            case "update":
                int uId = Integer.parseInt(request.getParameter("id"));
                User updateUser = userDAO.getUserById(uId);
                Student uStudent = studentDAO.getStudentById(uId);
                Employee uEmployee = employeeDAO.getEmployeeById(uId);
                request.setAttribute("employee", uEmployee);
                request.setAttribute("student", uStudent);
                request.setAttribute("user", updateUser);
                request.setAttribute("home_view", "/admin/editUser.jsp");
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
        UserDAO userDAO = new UserDAO();
        RoleDAO roleDAO = new RoleDAO();
        String action = request.getParameter("action");
        if (action == null) {
            action = "all";
        }

        switch (action) {
            case "add":
                String fullName = request.getParameter("fullName");
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                String phone = request.getParameter("phone");
                String address = request.getParameter("address");
                if (address == null || address.trim().isEmpty()) {
                    address = "";
                }
                Boolean gender = Boolean.valueOf(request.getParameter("gender"));
                String dobStr = request.getParameter("dob");
                java.sql.Date dob = null;
                if (dobStr != null && !dobStr.isEmpty()) {
                    dob = java.sql.Date.valueOf(dobStr);
                }

                String avatar = request.getParameter("avatar");
                if (avatar == null || avatar.trim().isEmpty()) {
                    avatar = "https://cdn-icons-png.flaticon.com/512/149/149071.png";
                }

                Boolean status = Boolean.valueOf(request.getParameter("status"));

                int roleId = Integer.parseInt(request.getParameter("roleId"));
                Role role = roleDAO.getRoleByID(roleId);

                java.sql.Date hireDate = null;
                String education = null;
                String experience = null;
                java.sql.Date enrollmentDate = null;

                if (roleId == 2 || roleId == 3 || roleId == 4) {
                    String hDate = request.getParameter("hireDate");
                    if (hDate != null && !hDate.isEmpty()) {
                        hireDate = java.sql.Date.valueOf(hDate);
                    }
                    education = request.getParameter("education");
                    experience = request.getParameter("experience");
                } else if (roleId == 5) {
                    String enrollStr = request.getParameter("enrollmentDate");
                    if (enrollStr != null && !enrollStr.isEmpty()) {
                        enrollmentDate = java.sql.Date.valueOf(enrollStr);
                    }
                }

                Boolean isAdded = userDAO.addNewUserFull(fullName, email, password, phone, address, gender, dob, avatar, status, roleId, hireDate, education, experience, enrollmentDate);

                HttpSession aSession = request.getSession();
                if (isAdded) {
                    aSession.setAttribute("message", "Add New User Successfully!");
                    aSession.setAttribute("messageType", "success");
                } else {
                    aSession.setAttribute("message", "Fail To Add New User");
                    aSession.setAttribute("messageType", "error");
                }
                response.sendRedirect("user");
                break;
            case "inActivate":
                int id = Integer.parseInt(request.getParameter("id"));
                Boolean inactivateSuccess = userDAO.inactivateUser(id);
                HttpSession uSession = request.getSession();
                if (inactivateSuccess) {
                    uSession.setAttribute("message", "Inactivate User Success!");
                    uSession.setAttribute("messageType", "success");
                } else {
                    uSession.setAttribute("message", "Fail To Inactivate User");
                    uSession.setAttribute("messageType", "error");
                }
                response.sendRedirect("user");
                break;
            case "activate":
                int aId = Integer.parseInt(request.getParameter("id"));
                Boolean activateSuccess = userDAO.activateUser(aId);
                HttpSession activateSession = request.getSession();
                if (activateSuccess) {
                    activateSession.setAttribute("message", "Activate User Success!");
                    activateSession.setAttribute("messageType", "success");
                } else {
                    activateSession.setAttribute("message", "Fail To Activate User");
                    activateSession.setAttribute("messageType", "error");
                }
                response.sendRedirect("user");
                break;
            case "update":
                String uFullName = request.getParameter("fullName");
                String uPhone = request.getParameter("phone");
                String uAddress = request.getParameter("address");
                if (uAddress == null || uAddress.trim().isEmpty()) {
                    uAddress = "";
                }
                Boolean uGender = Boolean.valueOf(request.getParameter("gender"));
                java.sql.Date uDob = java.sql.Date.valueOf(request.getParameter("dob"));
                String uAvatar = request.getParameter("avatar");

                try {
                    Part filePart = request.getPart("avatarFile");

                    if (filePart != null && filePart.getSize() > 0) {

                        String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
                        File uploadDir = new File(uploadPath);
                        if (!uploadDir.exists()) {
                            uploadDir.mkdir();
                        }

                        String fileName = java.nio.file.Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

                        String uniqueFileName = System.currentTimeMillis() + "_" + fileName;

                        filePart.write(uploadPath + File.separator + uniqueFileName);

                        uAvatar = "uploads/" + uniqueFileName;
                    }
                } catch (Exception e) {
                    System.out.println("Error uploading file: " + e.getMessage());
                }

                int uUserId = Integer.parseInt(request.getParameter("userId"));

                int uRoleId = Integer.parseInt(request.getParameter("roleId"));

                java.sql.Date uHDate = null;
                String uEducation = null;
                String uExperience = null;
                java.sql.Date uEnrollmentDate = null;

                if (uRoleId == 2 || uRoleId == 3 || uRoleId == 4) {
                    String uhireDate = request.getParameter("hireDate");
                    if (uhireDate != null && !uhireDate.isEmpty()) {
                        uHDate = java.sql.Date.valueOf(uhireDate);
                    }
                    uEducation = request.getParameter("education");
                    uExperience = request.getParameter("experience");
                } else if (uRoleId == 5) {
                    String enrollStr = request.getParameter("enrollmentDate");
                    if (enrollStr != null && !enrollStr.isEmpty()) {
                        uEnrollmentDate = java.sql.Date.valueOf(enrollStr);
                    }
                }
                boolean isUpdated = userDAO.updateUserById(uFullName, uPhone, uAddress, uGender, uDob, uAvatar, uRoleId, uUserId, uEnrollmentDate, uHDate, uEducation, uExperience);
                HttpSession session = request.getSession();
                if (isUpdated) {
                    session.setAttribute("message", "Update User Info Successfully!");
                    session.setAttribute("messageType", "success");
                } else {
                    session.setAttribute("message", "Update Failed!");
                    session.setAttribute("messageType", "error");
                }
                response.sendRedirect("user");
                break;
            case "changePassword":
                int cpUserId = Integer.parseInt(request.getParameter("userId"));
                String currentPassword = request.getParameter("currentPassword");
                String newPassword = request.getParameter("newPassword");
                String confirmPassword = request.getParameter("confirmPassword");

                HttpSession passSession = request.getSession();
                if (!newPassword.equals(confirmPassword)) {
                    passSession.setAttribute("message", "New password and confirm password do not match!");
                    passSession.setAttribute("messageType", "error");
                    response.sendRedirect("dashboard?action=profile");
                    break;
                }

                User currentUser = userDAO.getUserById(cpUserId);
                String hashCurrentPass = userDAO.hashMD5(currentPassword);

                if (currentPassword == null || !currentUser.getPassword().equals(hashCurrentPass)) {
                    passSession.setAttribute("message", "Incorrect current pasword");
                    passSession.setAttribute("messageType", "error");
                    response.sendRedirect("dashboard?action=profile");
                    break;
                }

                String newPasswordHashed = userDAO.hashMD5(newPassword);
                Boolean isPasswordChanged = userDAO.updatePassword(newPasswordHashed, cpUserId);
                if (isPasswordChanged) {
                    passSession.invalidate();

                    HttpSession newSession = request.getSession();
                    newSession.setAttribute("message", "Password changed successfully!");
                    newSession.setAttribute("messageType", "success");
                    response.sendRedirect("login");
                } else {
                    passSession.setAttribute("message", "Failed to change password!");
                    passSession.setAttribute("messageType", "error");
                }
                break;

            case "updateProfile":
                String pFullName = request.getParameter("fullName");
                String pPhone = request.getParameter("phone");
                String pAddress = request.getParameter("address");
                if (pAddress == null) {
                    pAddress = "";
                }

                Boolean pGender = Boolean.valueOf(request.getParameter("gender"));

                String DobStr = request.getParameter("dob");
                java.sql.Date pDob = null;
                if (DobStr != null && !DobStr.isEmpty()) {
                    pDob = java.sql.Date.valueOf(DobStr);
                }

                String pAvatar = request.getParameter("avatar");

                try {
                    Part filePart = request.getPart("avatarFile");

                    if (filePart != null && filePart.getSize() > 0) {

                        String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
                        File uploadDir = new File(uploadPath);
                        if (!uploadDir.exists()) {
                            uploadDir.mkdir();
                        }

                        String fileName = java.nio.file.Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

                        String uniqueFileName = System.currentTimeMillis() + "_" + fileName;

                        filePart.write(uploadPath + File.separator + uniqueFileName);

                        pAvatar = "uploads/" + uniqueFileName;
                    }
                } catch (Exception e) {
                    System.out.println("Error uploading file: " + e.getMessage());
                }

                int pUserId = Integer.parseInt(request.getParameter("userId"));
                int pRoleId = Integer.parseInt(request.getParameter("roleId"));

                java.sql.Date pHireDate = null;
                String pEducation = null;
                String pExperience = null;
                java.sql.Date pEnrollmentDate = null;

                if (pRoleId == 2 || pRoleId == 3 || pRoleId == 4) {
                    String hDate = request.getParameter("hireDate");
                    if (hDate != null && !hDate.isEmpty()) {
                        pHireDate = java.sql.Date.valueOf(hDate);
                    }
                    pEducation = request.getParameter("education");
                    pExperience = request.getParameter("experience");
                } else if (pRoleId == 5) {
                    String enrollStr = request.getParameter("enrollmentDate");
                    if (enrollStr != null && !enrollStr.isEmpty()) {
                        pEnrollmentDate = java.sql.Date.valueOf(enrollStr);
                    }
                }

                boolean isProfUpdated = userDAO.updateUserById(pFullName, pPhone, pAddress, pGender, pDob, pAvatar, pRoleId, pUserId, pEnrollmentDate, pHireDate, pEducation, pExperience);
                HttpSession pSession = request.getSession();

                if (isProfUpdated) {
                    pSession.setAttribute("message", "Profile Updated Successfully!");
                    pSession.setAttribute("messageType", "success");

                    User updatedUser = userDAO.getUserById(pUserId);
                    pSession.setAttribute("user", updatedUser);
                } else {
                    pSession.setAttribute("message", "Profile Update Failed!");
                    pSession.setAttribute("messageType", "error");
                }

                response.sendRedirect("dashboard?action=profile");
                break;

            case "resetPassword":
                int targetUserId = Integer.parseInt(request.getParameter("userId"));
                String targetEmail = request.getParameter("email");

                User targetUser = userDAO.getUserById(targetUserId);
                String targetFullName = (targetUser != null) ? targetUser.getFullName() : "User";

                String randomPass = controller.EmailController.generateRandomPassword();
                String hashedRandomPass = userDAO.hashMD5(randomPass);

                Boolean isReset = userDAO.updatePassword(hashedRandomPass, targetUserId);

                HttpSession resetSession = request.getSession();
                if (isReset) {
                    Boolean isEmailSent = controller.EmailController.sendEmail(targetEmail, targetFullName, randomPass);
                    if (isEmailSent) {
                        resetSession.setAttribute("message", "Reset password success. Email has been sent!");
                        resetSession.setAttribute("messageType", "success");
                    } else {
                        resetSession.setAttribute("message", "Reset successful, but failed to send email. Temp pass: " + randomPass);
                        resetSession.setAttribute("messageType", "error");
                    }
                } else {
                    resetSession.setAttribute("message", "Fail to reset password!");
                    resetSession.setAttribute("messageType", "error");
                }

                response.sendRedirect("user");
                break;
            case "toggleLock":
                int lockUserId = Integer.parseInt(request.getParameter("id"));
                boolean lockVal = Boolean.parseBoolean(request.getParameter("val"));

                boolean isToggled = userDAO.toggleLockUser(lockUserId, lockVal);
                HttpSession lockSession = request.getSession();
                if (isToggled) {
                    lockSession.setAttribute("message", "User account has been " + (lockVal ? "Locked" : "Unlocked") + " successfully!");
                    lockSession.setAttribute("messageType", "success");
                } else {
                    lockSession.setAttribute("message", "Failed to update lock status!");
                    lockSession.setAttribute("messageType", "error");
                }
                response.sendRedirect("user");
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
