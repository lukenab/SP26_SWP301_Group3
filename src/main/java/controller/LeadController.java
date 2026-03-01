package controller;

import dao.LeadDAO;
import dao.CourseDAO;
import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import model.Lead;
import model.Course;

@WebServlet(name = "LeadController", urlPatterns = {"/lead"})
public class LeadController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        LeadDAO leadDAO = new LeadDAO();
        CourseDAO courseDAO = new CourseDAO();

        if (action == null) {
            action = "all";
        }

        switch (action) {
            case "all":
                List<Lead> leadList = leadDAO.getAllLeads();
                request.setAttribute("leadList", leadList);
                request.setAttribute("totalLeads", leadList.size());
                request.setAttribute("home_view", "/sale/viewLeadList.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;

            case "add":
                List<Course> courseList = courseDAO.getActiveCourses();
                request.setAttribute("courseList", courseList);
                request.setAttribute("home_view", "/sale/AddLead.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;

            case "detail":
                int detailId = Integer.parseInt(request.getParameter("id"));
                Lead detailLead = leadDAO.getLeadByID(detailId);
                if (detailLead == null || "Inactive".equalsIgnoreCase(detailLead.getStatus())) {
                    request.getSession().setAttribute("message", "Inactive lead cannot be viewed.");
                    request.getSession().setAttribute("messageType", "error");
                    response.sendRedirect("lead?action=all");
                    return;
                }
                request.setAttribute("lead", detailLead);
                request.setAttribute("home_view", "/sale/leadDetail.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;

            case "edit":
                int editId = Integer.parseInt(request.getParameter("id"));
                Lead lead = leadDAO.getLeadByID(editId);
                if (lead == null || "Inactive".equalsIgnoreCase(lead.getStatus())) {
                    request.getSession().setAttribute("message", "Inactive lead cannot be edited.");
                    request.getSession().setAttribute("messageType", "error");
                    response.sendRedirect("lead?action=all");
                    return;
                }
                if ("Converted".equalsIgnoreCase(lead.getStatus())) {
                    request.getSession().setAttribute("message", "Converted lead cannot be edited.");
                    request.getSession().setAttribute("messageType", "error");
                    response.sendRedirect("lead?action=all");
                    return;
                }
                request.setAttribute("lead", lead);
                request.setAttribute("home_view", "/sale/editLead.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;

            case "convertForm":
                int convertId = Integer.parseInt(request.getParameter("id"));
                Lead convertLead = leadDAO.getLeadByID(convertId);
                if (convertLead == null || "Inactive".equalsIgnoreCase(convertLead.getStatus())) {
                    request.getSession().setAttribute("message", "Lead does not exist or is inactive.");
                    request.getSession().setAttribute("messageType", "error");
                    response.sendRedirect("lead?action=all");
                    return;
                }
                if ("Converted".equalsIgnoreCase(convertLead.getStatus())) {
                    request.getSession().setAttribute("message", "Lead has already been converted.");
                    request.getSession().setAttribute("messageType", "error");
                    response.sendRedirect("lead?action=all");
                    return;
                }

                request.setAttribute("lead", convertLead);
                request.setAttribute("today", LocalDate.now().toString());
                request.setAttribute("home_view", "/sale/convertLead.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;

            case "delete":
                int deleteId = Integer.parseInt(request.getParameter("id"));
                Lead dLead = leadDAO.getLeadByID(deleteId);
                request.setAttribute("dLead", dLead);
                request.setAttribute("home_view", "/sale/deleteLead.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;

            default:
                response.sendRedirect("lead?action=all");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        LeadDAO leadDAO = new LeadDAO();
        UserDAO userDAO = new UserDAO();
        HttpSession session = request.getSession();

        if ("create".equals(action)) {
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            int interestedCourseID = Integer.parseInt(request.getParameter("interestedCourseID"));
            String status = normalizeStatus(request.getParameter("status"));
            String note = request.getParameter("note");

            Lead lead = new Lead();
            lead.setFullName(fullName);
            lead.setEmail(email);
            lead.setPhone(phone);
            lead.setInterestedCourseID(interestedCourseID);
            lead.setStatus(status);
            lead.setNote(note);
            leadDAO.insertLead(lead);

            session.setAttribute("message", "Add new lead successfully!");
            session.setAttribute("messageType", "success");
            response.sendRedirect("lead?action=all");
            return;
        }

        if ("update".equals(action)) {
            int id = Integer.parseInt(request.getParameter("leadId"));
            Lead currentLead = leadDAO.getLeadByID(id);
            if (currentLead == null) {
                session.setAttribute("message", "Lead not found.");
                session.setAttribute("messageType", "error");
                response.sendRedirect("lead?action=all");
                return;
            }
            if ("Converted".equalsIgnoreCase(currentLead.getStatus())) {
                session.setAttribute("message", "Converted lead cannot be edited.");
                session.setAttribute("messageType", "error");
                response.sendRedirect("lead?action=all");
                return;
            }

            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            int interestedCourseID = currentLead.getInterestedCourseID();
            String status = normalizeStatus(request.getParameter("status"));
            String note = request.getParameter("note");

            if (!"New".equalsIgnoreCase(status) && !"Contacted".equalsIgnoreCase(status)) {
                session.setAttribute("message", "Only New or Contacted status can be edited.");
                session.setAttribute("messageType", "error");
                response.sendRedirect("lead?action=all");
                return;
            }

            String interestedCourseIDParam = request.getParameter("interestedCourseID");
            if (interestedCourseIDParam != null && !interestedCourseIDParam.trim().isEmpty()) {
                interestedCourseID = Integer.parseInt(interestedCourseIDParam);
            }

            boolean updated = leadDAO.updateLead(id, fullName, email, phone, interestedCourseID, status, note);
            if (updated) {
                session.setAttribute("message", "Update lead successfully!");
                session.setAttribute("messageType", "success");
            } else {
                session.setAttribute("message", "Update lead failed.");
                session.setAttribute("messageType", "error");
            }
            response.sendRedirect("lead?action=all");
            return;
        }

        if ("convert".equals(action)) {
            int leadId = Integer.parseInt(request.getParameter("leadId"));
            Lead lead = leadDAO.getLeadByID(leadId);

            if (lead == null || "Inactive".equalsIgnoreCase(lead.getStatus())) {
                session.setAttribute("message", "Lead not found or inactive.");
                session.setAttribute("messageType", "error");
                response.sendRedirect("lead?action=all");
                return;
            }

            if ("Converted".equalsIgnoreCase(lead.getStatus())) {
                session.setAttribute("message", "Lead has already been converted.");
                session.setAttribute("messageType", "error");
                response.sendRedirect("lead?action=all");
                return;
            }

            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String dobRaw = request.getParameter("dob");
            String genderRaw = request.getParameter("gender");
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirmPassword");
            String enrollmentDateRaw = request.getParameter("enrollmentDate");
            String avatar = request.getParameter("avatar");
            String convertNote = request.getParameter("convertNote");

            if (isBlank(fullName) || isBlank(email) || isBlank(password)
                    || isBlank(confirmPassword) || isBlank(dobRaw) || isBlank(genderRaw)) {
                session.setAttribute("message", "Please fill all required information for conversion.");
                session.setAttribute("messageType", "error");
                response.sendRedirect("lead?action=convertForm&id=" + leadId);
                return;
            }

            if (!password.equals(confirmPassword)) {
                session.setAttribute("message", "Password confirmation does not match.");
                session.setAttribute("messageType", "error");
                response.sendRedirect("lead?action=convertForm&id=" + leadId);
                return;
            }

            if (userDAO.isEmailExists(email.trim())) {
                session.setAttribute("message", "Email already exists in user system.");
                session.setAttribute("messageType", "error");
                response.sendRedirect("lead?action=convertForm&id=" + leadId);
                return;
            }

            Date dob = parseSqlDate(dobRaw);
            Date enrollmentDate = parseSqlDate(enrollmentDateRaw);
            if (dob == null) {
                session.setAttribute("message", "Invalid DOB format.");
                session.setAttribute("messageType", "error");
                response.sendRedirect("lead?action=convertForm&id=" + leadId);
                return;
            }
            if (enrollmentDate == null) {
                enrollmentDate = new Date(System.currentTimeMillis());
            }

            boolean isFemale = "female".equalsIgnoreCase(genderRaw);
            String defaultAvatar = "https://cdn-icons-png.flaticon.com/512/149/149071.png";

            boolean created = userDAO.addNewUserFull(
                    fullName.trim(),
                    email.trim(),
                    password,
                    isBlank(phone) ? null : phone.trim(),
                    isBlank(address) ? null : address.trim(),
                    isFemale,
                    dob,
                    isBlank(avatar) ? defaultAvatar : avatar.trim(),
                    true,
                    5,
                    null,
                    null,
                    null,
                    enrollmentDate
            );

            if (!created) {
                session.setAttribute("message", "Convert lead failed.");
                session.setAttribute("messageType", "error");
                response.sendRedirect("lead?action=convertForm&id=" + leadId);
                return;
            }

            String mergedNote = appendNote(lead.getNote(), convertNote);
            leadDAO.updateLead(leadId, fullName.trim(), email.trim(),
                    isBlank(phone) ? lead.getPhone() : phone.trim(),
                    lead.getInterestedCourseID(), "Converted", mergedNote);

            session.setAttribute("message", "Convert successful. Student account: " + email.trim() + " / " + password);
            session.setAttribute("messageType", "success");
            response.sendRedirect("lead?action=all");
            return;
        }

        if ("delete".equals(action)) {
            String idParam = request.getParameter("leadID");
            if (idParam == null || idParam.isEmpty()) {
                idParam = request.getParameter("leadId");
            }
            if (idParam != null && !idParam.isEmpty()) {
                int id = Integer.parseInt(idParam);
                leadDAO.deleteLead(id);
                session.setAttribute("message", "Lead has been moved to inactive.");
                session.setAttribute("messageType", "success");
            } else {
                session.setAttribute("message", "Delete lead failed.");
                session.setAttribute("messageType", "error");
            }
            response.sendRedirect("lead?action=all");
            return;
        }

        if ("restore".equals(action)) {
            String idParam = request.getParameter("leadID");
            if (idParam == null || idParam.isEmpty()) {
                idParam = request.getParameter("leadId");
            }
            if (idParam != null && !idParam.isEmpty()) {
                int id = Integer.parseInt(idParam);
                leadDAO.restoreLead(id);
                session.setAttribute("message", "Lead has been restored to new.");
                session.setAttribute("messageType", "success");
            } else {
                session.setAttribute("message", "Restore lead failed.");
                session.setAttribute("messageType", "error");
            }
            response.sendRedirect("lead?action=all");
            return;
        }

        response.sendRedirect("lead?action=all");
    }

    private String normalizeStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return "New";
        }

        String cleaned = status.trim().toLowerCase();
        switch (cleaned) {
            case "new":
                return "New";
            case "contacted":
                return "Contacted";
            case "converted":
                return "Converted";
            case "inactive":
                return "Inactive";
            default:
                return "New";
        }
    }

    private Date parseSqlDate(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Date.valueOf(value.trim());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String appendNote(String currentNote, String newNote) {
        if (isBlank(newNote)) {
            return currentNote;
        }
        if (isBlank(currentNote)) {
            return newNote.trim();
        }
        return currentNote.trim() + "\n[Convert Note] " + newNote.trim();
    }

}
