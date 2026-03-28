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

    private static final int DEFAULT_PAGE_SIZE = 10;

    private int parsePage(HttpServletRequest request) {
        String pageParam = request.getParameter("page");
        if (pageParam == null || pageParam.trim().isEmpty()) {
            return 1;
        }
        try {
            return Math.max(1, Integer.parseInt(pageParam));
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    private List<User> paginateUserList(List<User> source, int page, int pageSize, HttpServletRequest request) {
        if (source == null || source.isEmpty()) {
            request.setAttribute("currentPage", 1);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("totalItems", 0);
            request.setAttribute("totalPages", 1);
            request.setAttribute("startItem", 0);
            request.setAttribute("endItem", 0);
            return java.util.Collections.emptyList();
        }

        int totalItems = source.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        int currentPage = Math.min(page, totalPages);
        int fromIndex = (currentPage - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalItems);

        request.setAttribute("currentPage", currentPage);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("totalItems", totalItems);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("startItem", fromIndex + 1);
        request.setAttribute("endItem", toIndex);

        return source.subList(fromIndex, toIndex);
    }

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

                List<User> fullList = userDAO.searchAndFilterUsers(seachQuery, searchRoleId, searchStatus);

                long activeCount = fullList.stream().filter(u -> u.getStatus()).count();
                long inactiveCount = fullList.size() - activeCount;
                long adminCount = fullList.stream().filter(u -> u.getRole().getRoleId() == 1).count();

                StringBuilder paginationQuery = new StringBuilder();
                if (seachQuery != null && !seachQuery.isEmpty()) {
                    paginationQuery.append("&searchQuery=").append(seachQuery);
                }
                if (searchRoleId != null && !searchRoleId.isEmpty()) {
                    paginationQuery.append("&roleId=").append(searchRoleId);
                }
                if (searchStatus != null && !searchStatus.isEmpty()) {
                    paginationQuery.append("&status=").append(searchStatus);
                }
                request.setAttribute("paginationQuery", paginationQuery.toString());

                List<User> pagedList = paginateUserList(fullList, parsePage(request), DEFAULT_PAGE_SIZE, request);

                request.setAttribute("totalUsers", fullList.size());
                request.setAttribute("activeUsersCount", activeCount);
                request.setAttribute("inactiveUsersCount", inactiveCount);
                request.setAttribute("adminsCount", adminCount);
                request.setAttribute("userList", pagedList);
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
                String phone = getStringParam(request, "phone", "").trim();
                String address = getStringParam(request, "address", "");
                Boolean gender = getBoolParam(request, "gender");
                Date dob = getDateParam(request, "dob");
                Boolean status = getBoolParam(request, "status");
                int roleId = getIntParam(request, "roleId", 0);

                boolean hasError = false;

                if (roleId == 0) {
                    request.setAttribute("roleError", "Please select a valid role.");
                    hasError = true;
                }

                if (userDAO.isFieldExists("email", email)) {
                    request.setAttribute("emailError", "Email '" + email + "' is already registered!");
                    hasError = true;
                }

                String phoneRegex = "^0\\d{9}$";
                if (!phone.matches(phoneRegex)) {
                    request.setAttribute("phoneError", "Phone must start with 0 and have exactly 10 digits!");
                    hasError = true;
                } else if (userDAO.isFieldExists("phone", phone)) {
                    request.setAttribute("phoneError", "This phone number is already in use!");
                    hasError = true;
                }

                if (dob != null) {
                    Date today = new Date(System.currentTimeMillis());
                    if (dob.after(today)) {
                        request.setAttribute("dobError", "Date of Birth cannot be in the future!");
                        hasError = true;
                    } else {
                        java.util.Calendar calDob = java.util.Calendar.getInstance();
                        calDob.setTime(dob);
                        java.util.Calendar calToday = java.util.Calendar.getInstance();
                        int age = calToday.get(java.util.Calendar.YEAR) - calDob.get(java.util.Calendar.YEAR);
                        if (calToday.get(java.util.Calendar.DAY_OF_YEAR) < calDob.get(java.util.Calendar.DAY_OF_YEAR)) {
                            age--;
                        }

                        if (roleId >= 1 && roleId <= 4 && age < 18) {
                            request.setAttribute("dobError", "Admins and Staff must be at least 18 years old!");
                            hasError = true;
                        } else if (roleId == 5 && age < 5) {
                            request.setAttribute("dobError", "Students must be at least 5 years old!");
                            hasError = true;
                        }
                    }
                } else {
                    request.setAttribute("dobError", "Please enter Date of Birth.");
                    hasError = true;
                }

                if (hasError) {
                    RoleDAO roleDAO = new RoleDAO();
                    request.setAttribute("roleList", roleDAO.getAllRole());

                    request.setAttribute("message", "Please fix the highlighted errors.");
                    request.setAttribute("messageType", "error");

                    request.setAttribute("home_view", "/admin/createUser.jsp");
                    request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                    return;
                }

                String defaultAvatar = "https://cdn-icons-png.flaticon.com/512/149/149071.png";
                String avatarPath = uploadAvatar(request, defaultAvatar);

                Date hireDate = getDateParam(request, "hireDate");
                Date enrollmentDate = getDateParam(request, "enrollmentDate");
                String education = getStringParam(request, "education", "");
                String experience = getStringParam(request, "experience", "");

                if (roleId >= 2 && roleId <= 4) {
                    hireDate = getDateParam(request, "hireDate");
                    education = getStringParam(request, "education", "");
                    experience = getStringParam(request, "experience", "");
                } else if (roleId == 5) {
                    enrollmentDate = getDateParam(request, "enrollmentDate");
                }

                Boolean isAdded = userDAO.addNewUserFull(
                        fullName, email, password, phone, address, gender,
                        dob, avatarPath, status, roleId,
                        hireDate, education, experience, enrollmentDate
                );

                if (isAdded) {
                    SystemLogDAO logDAO = new SystemLogDAO();
                    User logUser = (User) session.getAttribute("user");
                    String actorName = (logUser != null) ? logUser.getFullName() : "System";
                    String actorRole = (logUser != null && logUser.getRole() != null) ? logUser.getRole().getRoleName() : "Admin";
                    logDAO.insertLog(actorName, actorRole, "CREATE_USER", "Admin created user: " + fullName + " (" + email + ")");

                    session.setAttribute("message", "User created successfully!");
                    session.setAttribute("messageType", "success");
                    response.sendRedirect("user");
                } else {
                    request.setAttribute("message", "Failed to add user due to database error.");
                    request.setAttribute("messageType", "error");

                    RoleDAO roleDAO = new RoleDAO();
                    request.setAttribute("roleList", roleDAO.getAllRole());
                    request.setAttribute("home_view", "/admin/createUser.jsp");
                    request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                }
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
                int uUserId = Integer.parseInt(request.getParameter("userId"));
                int uRoleId = Integer.parseInt(request.getParameter("roleId"));
                String uFullName = getStringParam(request, "fullName", "");
                String uPhone = getStringParam(request, "phone", "");
                String uAddress = getStringParam(request, "address", "");
                Boolean uGender = getBoolParam(request, "gender");
                Date uDob = getDateParam(request, "dob");

                HttpSession updateSession = request.getSession();

                phoneRegex = "^0\\d{9}$";
                if (!uPhone.matches(phoneRegex)) {
                    updateSession.setAttribute("message", "Invalid phone format! Must start with 0 and have exactly 10 digits.");
                    updateSession.setAttribute("messageType", "error");
                    response.sendRedirect("user?action=update&id=" + uUserId);
                    return;
                }

                User existingUser = userDAO.getUserById(uUserId);

                if (!uPhone.equals(existingUser.getPhone())) {
                    if (userDAO.isFieldExists("phone", uPhone)) {
                        updateSession.setAttribute("message", "This phone number is already registered to another account!");
                        updateSession.setAttribute("messageType", "error");
                        response.sendRedirect("user?action=update&id=" + uUserId);
                        return;
                    }
                }

                if (uDob != null) {
                    long currentTime = System.currentTimeMillis();
                    Date today = new Date(currentTime);
                    if (uDob.after(today)) {
                        updateSession.setAttribute("message", "Date of Birth cannot be in the future!");
                        updateSession.setAttribute("messageType", "error");
                        response.sendRedirect("user?action=update&id=" + uUserId);
                        return;
                    }

                    java.util.Calendar calDob = java.util.Calendar.getInstance();
                    calDob.setTime(uDob);
                    java.util.Calendar calToday = java.util.Calendar.getInstance();
                    int age = calToday.get(java.util.Calendar.YEAR) - calDob.get(java.util.Calendar.YEAR);
                    if (calToday.get(java.util.Calendar.DAY_OF_YEAR) < calDob.get(java.util.Calendar.DAY_OF_YEAR)) {
                        age--;
                    }

                    if (uRoleId >= 1 && uRoleId <= 4) {
                        if (age < 18) {
                            updateSession.setAttribute("message", "This role requires at least 18 years old!");
                            updateSession.setAttribute("messageType", "error");
                            response.sendRedirect("user?action=update&id=" + uUserId);
                            return;
                        }
                    } else if (uRoleId == 5 && age < 5) {
                        updateSession.setAttribute("message", "Student must be at least 5 years old!");
                        updateSession.setAttribute("messageType", "error");
                        response.sendRedirect("user?action=update&id=" + uUserId);
                        return;
                    }
                }

                String uAvatar = uploadAvatar(request, request.getParameter("avatar"));

                java.sql.Date uHDate = null;
                String uEducation = null;
                String uExperience = null;
                java.sql.Date uEnrollmentDate = null;

                if (uRoleId >= 2 && uRoleId <= 4) {
                    uHDate = getDateParam(request, "hireDate");
                    uEducation = getStringParam(request, "education", "");
                    uExperience = getStringParam(request, "experience", "");
                } else if (uRoleId == 5) {
                    uEnrollmentDate = getDateParam(request, "enrollmentDate");
                }

                boolean isUpdated = userDAO.updateUserById(uFullName, uPhone, uAddress, uGender, uDob, uAvatar, uRoleId, uUserId, uEnrollmentDate, uHDate, uEducation, uExperience);

                if (isUpdated) {
                    SystemLogDAO logDAO = new SystemLogDAO();
                    User logUser = (User) session.getAttribute("user");
                    String actorName = (logUser != null) ? logUser.getFullName() : "System";
                    String actorRole = (logUser != null && logUser.getRole() != null) ? logUser.getRole().getRoleName() : "Admin";
                    logDAO.insertLog(actorName, actorRole, "UPDATE_USER", "Updated info for User ID: " + uUserId);

                    updateSession.setAttribute("message", "Update User Successfully!");
                    updateSession.setAttribute("messageType", "success");
                    response.sendRedirect("user");
                } else {
                    updateSession.setAttribute("message", "Update Failed!");
                    updateSession.setAttribute("messageType", "error");
                    response.sendRedirect("user?action=update&id=" + uUserId);
                }
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
