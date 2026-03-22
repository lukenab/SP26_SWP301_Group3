/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.EmployeeDAO;
import dao.RoleDAO;
import dao.StudentDAO;
import dao.SystemLogDAO;
import dao.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Employee;
import model.Role;
import model.Student;
import model.User;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;
import java.io.File;
import java.sql.Date;

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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UserDAO userDAO = new UserDAO();
        RoleDAO roleDAO = new RoleDAO();
        EmployeeDAO employeeDAO = new EmployeeDAO();
        StudentDAO studentDAO = new StudentDAO();

        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            response.sendRedirect("login");
        }

        if (!currentUser.getRole().getManageUser()) {
            session.setAttribute("message", "Access Denied: You don't have permission to manage users!");
            session.setAttribute("messageType", "error");
            response.sendRedirect("dashboard");
            return;
        }

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
                int targetId = getIntParam(request, "id", 0);
                if (targetId <= 0) {
                    response.sendRedirect("user");
                    return;
                }
                User targetUser = userDAO.getUserById(targetId);
                request.setAttribute("uDelete", targetUser);
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
                int uId = getIntParam(request, "id", 0);
                if (uId <= 0) {
                    response.sendRedirect("user");
                    return;
                }

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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UserDAO userDAO = new UserDAO();
        String action = request.getParameter("action");
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null) {
            response.sendRedirect("login");
            return;
        }

        if (!currentUser.getRole().getManageUser()) {
            session.setAttribute("message", "Access Denied: You don't have permission to perform this action!");
            session.setAttribute("messageType", "error");
            response.sendRedirect("dashboard");
            return; 
        }

        switch (action) {
            case "add":
                String fullName = getStringParam(request, "fullName", "");
                String email = getStringParam(request, "email", "");
                String password = getStringParam(request, "password", "");
                String phone = getStringParam(request, "phone", "");
                String address = getStringParam(request, "address", "");
                Boolean gender = getBoolParam(request, "gender");
                Date dob = getDateParam(request, "dob");
                String avatar = getStringParam(request, "avatar", "https://cdn-icons-png.flaticon.com/512/149/149071.png");

                Boolean status = getBoolParam(request, "status");

                int roleId = getIntParam(request, "roleId", 5); // roleId = 5 => Student

                Date hireDate = null;
                String education = null;
                String experience = null;
                Date enrollmentDate = null;

                if (roleId == 2 || roleId == 3 || roleId == 4) {
                    hireDate = getDateParam(request, "hireDate");
                    education = getStringParam(request, "education", null);
                    experience = getStringParam(request, "experience", null);
                } else if (roleId == 5) {
                    enrollmentDate = getDateParam(request, "enrollmentDate");
                }

                Boolean isAdded = userDAO.addNewUserFull(fullName, email, password, phone, address, gender, dob, avatar, status, roleId, hireDate, education, experience, enrollmentDate);

                HttpSession aSession = request.getSession();
                if (isAdded) {

                    SystemLogDAO logDAO = new SystemLogDAO();
                    User logUser = (User) request.getSession().getAttribute("user");

                    String actorName = (logUser != null) ? logUser.getFullName() : "System";
                    String actorRole = (logUser != null && logUser.getRole() != null) ? logUser.getRole().getRoleName() : "Admin";

                    String logAction = "CREATE_USER";
                    String detail = "Admin has create new account: " + fullName + " (Email: " + email + ")";

                    logDAO.insertLog(actorName, actorRole, logAction, detail);

                    aSession.setAttribute("message", "Add New User Successfully!");
                    aSession.setAttribute("messageType", "success");
                } else {
                    aSession.setAttribute("message", "Fail To Add New User");
                    aSession.setAttribute("messageType", "error");
                }
                response.sendRedirect("user");
                break;

            case "inActivate":
            case "activate":
                int id = getIntParam(request, "id", 0);
                if (id <= 0) {
                    response.sendRedirect("user");
                    return;
                }

                boolean newStatus = action.equals("activate");

                Boolean statusUpdated = userDAO.updateUserStatus(id, newStatus);

                HttpSession uSession = request.getSession();
                String actionName = newStatus ? "Activate" : "Inactivate";

                if (statusUpdated) {

                    SystemLogDAO logDAO = new SystemLogDAO();
                    User logUser = (User) request.getSession().getAttribute("user");

                    String actorName = (logUser != null) ? logUser.getFullName() : "System";
                    String actorRole = (logUser != null && logUser.getRole() != null) ? logUser.getRole().getRoleName() : "Admin";

                    String logAction = newStatus ? "ACTIVATE_USER" : "DEACTIVATE_USER";
                    String detail = "Admin has " + (newStatus ? "active" : "inactive") + " account: " + id;
                    logDAO.insertLog(actorName, actorRole, logAction, detail);

                    uSession.setAttribute("message", actionName + " User Success!");
                    uSession.setAttribute("messageType", "success");
                } else {
                    uSession.setAttribute("message", "Fail To " + actionName + " User");
                    uSession.setAttribute("messageType", "error");
                }
                response.sendRedirect("user");
                break;

            case "update":
                String uFullName = getStringParam(request, "fullName", "");
                String uPhone = getStringParam(request, "phone", "");
                String uAddress = getStringParam(request, "address", "");
                Boolean uGender = getBoolParam(request, "gender");
                Date uDob = getDateParam(request, "dob");

                String uAvatar = uploadAvatar(request, request.getParameter("avatar"));

                int uUserId = Integer.parseInt(request.getParameter("userId"));
                int uRoleId = Integer.parseInt(request.getParameter("roleId"));

                java.sql.Date uHDate = null;
                String uEducation = null;
                String uExperience = null;
                java.sql.Date uEnrollmentDate = null;

                if (uRoleId == 2 || uRoleId == 3 || uRoleId == 4) {
                    uHDate = getDateParam(request, "hireDate");
                    uEducation = getStringParam(request, "education", "");
                    uExperience = getStringParam(request, "experience", "");
                } else if (uRoleId == 5) {
                    uEnrollmentDate = getDateParam(request, "enrollmentDate");
                }

                boolean isUpdated = userDAO.updateUserById(uFullName, uPhone, uAddress, uGender, uDob, uAvatar, uRoleId, uUserId, uEnrollmentDate, uHDate, uEducation, uExperience);
                HttpSession updateSession = request.getSession();
                if (isUpdated) {

                    SystemLogDAO logDAO = new SystemLogDAO();
                    User logUser = (User) request.getSession().getAttribute("user");

                    String actorName = (logUser != null) ? logUser.getFullName() : "System";
                    String actorRole = (logUser != null && logUser.getRole() != null) ? logUser.getRole().getRoleName() : "Admin";

                    String logAction = "UPDATE_USER";
                    String detail = "Admin has update info for User ID: " + uUserId + " (" + uFullName + ")";
                    logDAO.insertLog(actorName, actorRole, logAction, detail);

                    updateSession.setAttribute("message", "Update User Info Successfully!");
                    updateSession.setAttribute("messageType", "success");
                } else {
                    updateSession.setAttribute("message", "Update Failed!");
                    updateSession.setAttribute("messageType", "error");
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

                currentUser = userDAO.getUserById(cpUserId);
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
                    SystemLogDAO logDAO = new SystemLogDAO();
                    User logUser = (User) request.getSession().getAttribute("user");

                    String actorName = (logUser != null) ? logUser.getFullName() : "System";
                    String actorRole = (logUser != null && logUser.getRole() != null) ? logUser.getRole().getRoleName() : "Admin";
                    logDAO.insertLog(actorName, actorRole, "CHANGE_PASSWORD", "User ID " + cpUserId + " changed their password.");

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
                String pAddress = getStringParam(request, "address", "");
                Boolean pGender = getBoolParam(request, "gender");
                Date pDob = getDateParam(request, "dob");

                String pAvatar = uploadAvatar(request, request.getParameter("avatar"));

                int pUserId = Integer.parseInt(request.getParameter("userId"));
                int pRoleId = Integer.parseInt(request.getParameter("roleId"));

                Date pHireDate = null;
                String pEducation = null;
                String pExperience = null;
                Date pEnrollmentDate = null;

                if (pRoleId == 2 || pRoleId == 3 || pRoleId == 4) {
                    pHireDate = getDateParam(request, "hireDate");
                    pEducation = getStringParam(request, "education", "");
                    pExperience = getStringParam(request, "experience", "");
                } else if (pRoleId == 5) {
                    pEnrollmentDate = getDateParam(request, "enrollmentDate");
                }

                boolean isProfUpdated = userDAO.updateUserById(pFullName, pPhone, pAddress, pGender, pDob, pAvatar, pRoleId, pUserId, pEnrollmentDate, pHireDate, pEducation, pExperience);
                HttpSession pSession = request.getSession();

                if (isProfUpdated) {

                    SystemLogDAO logDAO = new SystemLogDAO();
                    User logUser = (User) request.getSession().getAttribute("user");

                    String actorName = (logUser != null) ? logUser.getFullName() : "System";
                    String actorRole = (logUser != null && logUser.getRole() != null) ? logUser.getRole().getRoleName() : "Admin";

                    String logAction = "UPDATE_PROFILE";
                    String detail = "User updated personal profile information (User ID: " + pUserId + ")";
                    logDAO.insertLog(actorName, actorRole, logAction, detail);

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

                    SystemLogDAO logDAO = new SystemLogDAO();
                    User logUser = (User) request.getSession().getAttribute("user");

                    String actorName = (logUser != null) ? logUser.getFullName() : "System";
                    String actorRole = (logUser != null && logUser.getRole() != null) ? logUser.getRole().getRoleName() : "Admin";
                    logDAO.insertLog(actorName, actorRole, "RESET_PASSWORD", "Admin reset password for User ID " + targetUserId + " (" + targetEmail + ")");

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
                    SystemLogDAO logDAO = new SystemLogDAO();
                    User logUser = (User) request.getSession().getAttribute("user");

                    String actorName = (logUser != null) ? logUser.getFullName() : "System";
                    String actorRole = (logUser != null && logUser.getRole() != null) ? logUser.getRole().getRoleName() : "Admin";

                    String logAction = lockVal ? "LOCK_USER" : "UNLOCK_USER";
                    String detail = "Admin đã " + (lockVal ? "lock" : "unlock") + " account has ID: " + lockUserId;
                    logDAO.insertLog(actorName, actorRole, logAction, detail);

                    lockSession.setAttribute("message", "User account has been...");
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

    private String uploadAvatar(HttpServletRequest request, String currentAvatar) {
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
                return "uploads/" + uniqueFileName;
            }
        } catch (Exception e) {
            System.out.println("Error uploading file: " + e.getMessage());
        }
        return currentAvatar;
    }

    private int getIntParam(HttpServletRequest request, String name, int defaultValue) {
        String value = request.getParameter(name);
        try {
            if (value != null && !value.isEmpty()) {
                return Integer.parseInt(value.trim());
            }
        } catch (NumberFormatException e) {
            System.out.println("Fail to parse number for parameter " + name + ":" + e.getMessage());
        }
        return defaultValue;
    }

    private java.sql.Date getDateParam(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        try {
            if (value != null && !value.isEmpty()) {
                return java.sql.Date.valueOf(value);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Fail to format date for param: " + e.getMessage());
        }
        return null;
    }

    private String getStringParam(HttpServletRequest request, String name, String defaultValue) {
        String value = request.getParameter(name);
        return (value != null && !value.trim().isEmpty() ? value.trim() : defaultValue);
    }

    private boolean getBoolParam(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return Boolean.parseBoolean(value);
    }
}
