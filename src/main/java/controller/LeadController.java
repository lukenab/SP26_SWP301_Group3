package controller;

import dao.CourseDAO;
import dao.LeadDAO;
import dao.SalesDAO;
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
import java.time.LocalDateTime;
import java.util.List;
import model.Consultation;
import model.Course;
import model.Lead;
import model.User;

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
                String searchQuery = request.getParameter("searchQuery");
                String status = request.getParameter("status");
                String interestRaw = request.getParameter("interestCourseId");
                String fromDateRaw = request.getParameter("fromDate");
                String toDateRaw = request.getParameter("toDate");
                String pageRaw = request.getParameter("page");
                if (status == null || status.trim().isEmpty()) {
                    status = "all";
                }

                Integer interestCourseId = parseIntegerOrNull(interestRaw);
                LocalDateTime fromDate = parseFilterDateTimeStart(fromDateRaw);
                LocalDateTime toDate = parseFilterDateTimeEnd(toDateRaw);
                int page = parsePositiveIntOrDefault(pageRaw, 1);
                int pageSize = 10;

                int totalLeads = leadDAO.countLeadsByFilters(searchQuery, status, interestCourseId, fromDate, toDate);
                int totalPages = totalLeads == 0 ? 1 : (int) Math.ceil((double) totalLeads / pageSize);
                if (page > totalPages) {
                    page = totalPages;
                }

                List<Lead> leadList = leadDAO.searchAndFilterLeadsPaged(searchQuery, status, interestCourseId, fromDate, toDate, page, pageSize);
                List<Course> interestCourseList = courseDAO.getAllCourse();
                request.setAttribute("leadList", leadList);
                request.setAttribute("interestCourseList", interestCourseList);
                request.setAttribute("totalLeads", totalLeads);
                request.setAttribute("currentPage", page);
                request.setAttribute("totalPages", totalPages);
                request.setAttribute("pageSize", pageSize);
                request.setAttribute("searchQuery", searchQuery == null ? "" : searchQuery);
                request.setAttribute("statusFilter", status);
                request.setAttribute("interestCourseId", interestCourseId);
                request.setAttribute("fromDate", fromDateRaw == null ? "" : fromDateRaw);
                request.setAttribute("toDate", toDateRaw == null ? "" : toDateRaw);
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
                List<Consultation> consultationHistory = leadDAO.getConsultationHistoryByLeadId(detailId);
                request.setAttribute("lead", detailLead);
                request.setAttribute("consultationHistory", consultationHistory);
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

            case "logForm":
                int logId = Integer.parseInt(request.getParameter("id"));
                Lead logLead = leadDAO.getLeadByID(logId);
                if (logLead == null || "Inactive".equalsIgnoreCase(logLead.getStatus())) {
                    request.getSession().setAttribute("message", "Lead does not exist or is inactive.");
                    request.getSession().setAttribute("messageType", "error");
                    response.sendRedirect("lead?action=all");
                    return;
                }
                if ("Converted".equalsIgnoreCase(logLead.getStatus())) {
                    request.getSession().setAttribute("message", "Converted lead cannot be logged.");
                    request.getSession().setAttribute("messageType", "error");
                    response.sendRedirect("lead?action=all");
                    return;
                }

                String nowDateTime = LocalDateTime.now().withSecond(0).withNano(0).toString();
                if (nowDateTime.length() > 16) {
                    nowDateTime = nowDateTime.substring(0, 16);
                }

                request.setAttribute("lead", logLead);
                request.setAttribute("nowDateTime", nowDateTime);
                request.setAttribute("home_view", "/sale/logConsultation.jsp");
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

            case "salesReport":
                SalesDAO salesDAO = new SalesDAO();
                String fromDateRawReport = request.getParameter("fromDate");
                String toDateRawReport = request.getParameter("toDate");

                LocalDate defaultTo = LocalDate.now();
                LocalDate defaultFrom = defaultTo.minusMonths(5).withDayOfMonth(1);

                LocalDateTime fromDateReport = parseFilterDateTimeStart(fromDateRawReport);
                LocalDateTime toDateReport = parseFilterDateTimeEnd(toDateRawReport);
                if (fromDateReport == null) {
                    fromDateReport = defaultFrom.atStartOfDay();
                }
                if (toDateReport == null) {
                    toDateReport = defaultTo.atTime(23, 59, 59);
                }

                if (fromDateReport.isAfter(toDateReport)) {
                    LocalDateTime temp = fromDateReport;
                    fromDateReport = toDateReport.minusMonths(1);
                    toDateReport = temp.plusMonths(1);
                }

                Object[] summary = salesDAO.getSalesSummary(fromDateReport, toDateReport);
                List<Object[]> monthlyRows = salesDAO.getMonthlySalesReport(fromDateReport, toDateReport);

                request.setAttribute("totalLeads", summary[0]);
                request.setAttribute("convertedLeads", summary[1]);
                request.setAttribute("registeredStudents", summary[2]);
                request.setAttribute("conversionRate", summary[3]);
                request.setAttribute("monthlyRows", monthlyRows);
                request.setAttribute("fromDate", fromDateReport.toLocalDate().toString());
                request.setAttribute("toDate", toDateReport.toLocalDate().toString());
                request.setAttribute("home_view", "/sale/viewSalesReport.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;

            case "revenueReport":
                SalesDAO revenueDAO = new SalesDAO();
                String fromDateRawRevenue = request.getParameter("fromDate");
                String toDateRawRevenue = request.getParameter("toDate");

                LocalDate revenueDefaultTo = LocalDate.now();
                LocalDate revenueDefaultFrom = revenueDefaultTo.minusMonths(5).withDayOfMonth(1);

                LocalDateTime fromDateRevenue = parseFilterDateTimeStart(fromDateRawRevenue);
                LocalDateTime toDateRevenue = parseFilterDateTimeEnd(toDateRawRevenue);
                if (fromDateRevenue == null) {
                    fromDateRevenue = revenueDefaultFrom.atStartOfDay();
                }
                if (toDateRevenue == null) {
                    toDateRevenue = revenueDefaultTo.atTime(23, 59, 59);
                }

                if (fromDateRevenue.isAfter(toDateRevenue)) {
                    LocalDateTime temp = fromDateRevenue;
                    fromDateRevenue = toDateRevenue.minusMonths(1);
                    toDateRevenue = temp.plusMonths(1);
                }

                Object[] revenueSummary = revenueDAO.getRevenueSummary(fromDateRevenue, toDateRevenue);
                List<Object[]> monthlyRevenueRows = revenueDAO.getMonthlyRevenueReport(fromDateRevenue, toDateRevenue);

                request.setAttribute("totalRevenue", revenueSummary[0]);
                request.setAttribute("verifiedPayments", revenueSummary[1]);
                request.setAttribute("payingStudents", revenueSummary[2]);
                request.setAttribute("monthlyRevenueRows", monthlyRevenueRows);
                request.setAttribute("fromDate", fromDateRevenue.toLocalDate().toString());
                request.setAttribute("toDate", toDateRevenue.toLocalDate().toString());
                request.setAttribute("home_view", "/sale/viewRevenueReport.jsp");
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
            String normalizedEmail = isBlank(email) ? null : email.trim();
            String normalizedPhone = isBlank(phone) ? null : phone.trim();

            if (normalizedEmail == null) {
                session.setAttribute("message", "Email is required.");
                session.setAttribute("messageType", "error");
                response.sendRedirect("lead?action=add");
                return;
            }
            if (normalizedPhone == null) {
                session.setAttribute("message", "Phone is required.");
                session.setAttribute("messageType", "error");
                response.sendRedirect("lead?action=add");
                return;
            }

            if (userDAO.isEmailExists(normalizedEmail) || leadDAO.isEmailExists(normalizedEmail)) {
                session.setAttribute("message", "Email already exists. Please use another email.");
                session.setAttribute("messageType", "error");
                response.sendRedirect("lead?action=add");
                return;
            }
            if (userDAO.isPhoneExists(normalizedPhone) || leadDAO.isPhoneExists(normalizedPhone)) {
                session.setAttribute("message", "Phone already exists. Please use another phone number.");
                session.setAttribute("messageType", "error");
                response.sendRedirect("lead?action=add");
                return;
            }

            Lead lead = new Lead();
            lead.setFullName(fullName);
            lead.setEmail(normalizedEmail);
            lead.setPhone(normalizedPhone);
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

        if ("logConsultation".equals(action)) {
            String leadIdRaw = request.getParameter("leadId");
            String consultationNote = request.getParameter("consultationNote");
            String consultDateRaw = request.getParameter("consultDate");

            if (isBlank(leadIdRaw)) {
                session.setAttribute("message", "Invalid lead id.");
                session.setAttribute("messageType", "error");
                response.sendRedirect("lead?action=all");
                return;
            }

            int leadId = Integer.parseInt(leadIdRaw);
            Lead lead = leadDAO.getLeadByID(leadId);
            if (lead == null || "Inactive".equalsIgnoreCase(lead.getStatus())) {
                session.setAttribute("message", "Lead does not exist or is inactive.");
                session.setAttribute("messageType", "error");
                response.sendRedirect("lead?action=all");
                return;
            }
            if ("Converted".equalsIgnoreCase(lead.getStatus())) {
                session.setAttribute("message", "Converted lead cannot be logged.");
                session.setAttribute("messageType", "error");
                response.sendRedirect("lead?action=all");
                return;
            }

            if (isBlank(consultationNote)) {
                session.setAttribute("message", "Consultation note is required.");
                session.setAttribute("messageType", "error");
                response.sendRedirect("lead?action=logForm&id=" + leadId);
                return;
            }

            LocalDateTime consultDate = parseLocalDateTime(consultDateRaw);
            if (consultDate == null) {
                consultDate = LocalDateTime.now();
            }

            Integer saleId = null;
            User loginUser = (User) session.getAttribute("user");
            if (loginUser != null) {
                saleId = loginUser.getUserId();
            }

            boolean saved = leadDAO.insertConsultationLog(leadId, saleId, consultationNote.trim(), consultDate);
            if (saved) {
                session.setAttribute("message", "Consultation record saved successfully.");
                session.setAttribute("messageType", "success");
            } else {
                session.setAttribute("message", "Failed to save consultation record.");
                session.setAttribute("messageType", "error");
            }

            response.sendRedirect("lead?action=detail&id=" + leadId);
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
            String defaultPassword = "123456";
            String enrollmentDateRaw = request.getParameter("enrollmentDate");
            String avatar = request.getParameter("avatar");
            String convertNote = request.getParameter("convertNote");

            if (isBlank(fullName) || isBlank(email) || isBlank(dobRaw) || isBlank(genderRaw)) {
                session.setAttribute("message", "Please fill all required information for conversion.");
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
                    defaultPassword,
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

            boolean emailSent = EmailController.sendLeadConversionEmail(email.trim(), fullName.trim(), defaultPassword);
            if (emailSent) {
                session.setAttribute("message", "Convert successful. Default password 123456 has been sent to student email.");
                session.setAttribute("messageType", "success");
            } else {
                session.setAttribute("message", "Convert successful, but failed to send default password email. Please send credentials manually.");
                session.setAttribute("messageType", "error");
            }
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

    private LocalDateTime parseLocalDateTime(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(value.trim());
        } catch (Exception ex) {
            return null;
        }
    }

    private Integer parseIntegerOrNull(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private int parsePositiveIntOrDefault(String value, int defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            int parsed = Integer.parseInt(value.trim());
            return parsed > 0 ? parsed : defaultValue;
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    private LocalDateTime parseFilterDateTimeStart(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Date.valueOf(value.trim()).toLocalDate().atStartOfDay();
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    private LocalDateTime parseFilterDateTimeEnd(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Date.valueOf(value.trim()).toLocalDate().atTime(23, 59, 59);
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
