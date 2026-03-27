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
import model.Voucher;

@WebServlet(name = "LeadController", urlPatterns = {"/lead"})
public class LeadController extends HttpServlet {

    private static final String DEFAULT_AVATAR = "https://cdn-icons-png.flaticon.com/512/149/149071.png";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        LeadDAO leadDAO = new LeadDAO();
        CourseDAO courseDAO = new CourseDAO();

        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            response.sendRedirect("login");
            return;
        }

        if (currentUser.getRole() == null || !currentUser.getRole().getManageFinance()) {
            redirectWithMessage(session, response, request.getContextPath() + "/dashboard",
                    "Access Denied: You don't have permission to access Leads management!", "error");
            return;
        }

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

                int newLeadCount = 0;
                int convertedLeadCount = 0;
                if ("all".equalsIgnoreCase(status)) {
                    newLeadCount = leadDAO.countLeadsByFilters(searchQuery, "New", interestCourseId, fromDate, toDate);
                    convertedLeadCount = leadDAO.countLeadsByFilters(searchQuery, "Converted", interestCourseId, fromDate, toDate);
                } else if ("New".equalsIgnoreCase(status)) {
                    newLeadCount = totalLeads;
                } else if ("Converted".equalsIgnoreCase(status)) {
                    convertedLeadCount = totalLeads;
                }

                request.setAttribute("leadList", leadList);
                request.setAttribute("interestCourseList", interestCourseList);
                request.setAttribute("totalLeads", totalLeads);
                request.setAttribute("newLead", newLeadCount);
                request.setAttribute("convertedLead", convertedLeadCount);
                request.setAttribute("currentPage", page);
                request.setAttribute("totalPages", totalPages);
                request.setAttribute("pageSize", pageSize);
                request.setAttribute("searchQuery", searchQuery == null ? "" : searchQuery);
                request.setAttribute("statusFilter", status);
                request.setAttribute("interestCourseId", interestCourseId);
                request.setAttribute("fromDate", fromDateRaw == null ? "" : fromDateRaw);
                request.setAttribute("toDate", toDateRaw == null ? "" : toDateRaw);
                forwardDashboard(request, response, "/sale/viewLeadList.jsp");
                break;

            case "add":
                dao.ClassDAO classDAOForLead = new dao.ClassDAO();
                List<Object[]> openCourseList = classDAOForLead.getOpenCoursesForSales();
                request.setAttribute("openCourseList", openCourseList);
                List<Course> courseList = courseDAO.getActiveCourses();
                request.setAttribute("courseList", courseList);
                forwardDashboard(request, response, "/sale/AddLead.jsp");
                break;
            case "openClasses":
                dao.ClassDAO salesClassDAO = new dao.ClassDAO();
                List<Object[]> openClassList = salesClassDAO.getOpenClassListForSales();
                request.setAttribute("openClassList", openClassList);
                forwardDashboard(request, response, "/sale/openClassList.jsp");
                break;
            case "addStudentAtCenter":
                dao.ClassDAO classDAO = new dao.ClassDAO();
                List<Object[]> classList = classDAO.getClassOptionsForWalkIn();
                List<Object[]> openCourseListForWalkin = classDAO.getOpenCoursesForSales();
                request.setAttribute("classList", classList);
                request.setAttribute("openCourseList", openCourseListForWalkin);
                forwardDashboard(request, response, "/sale/addStudentAtCenter.jsp");
                break;

            case "detail":
                int detailId = Integer.parseInt(request.getParameter("id"));
                Lead detailLead = getLeadForAction(leadDAO, detailId, session, response,
                        "Inactive lead cannot be viewed.", null);
                if (detailLead == null) {
                    return;
                }
                List<Consultation> consultationHistory = leadDAO.getConsultationHistoryByLeadId(detailId);
                request.setAttribute("lead", detailLead);
                request.setAttribute("consultationHistory", consultationHistory);
                forwardDashboard(request, response, "/sale/leadDetail.jsp");
                break;

            case "edit":
                int editId = Integer.parseInt(request.getParameter("id"));
                Lead lead = getLeadForAction(leadDAO, editId, session, response,
                        "Inactive lead cannot be edited.", "Converted lead cannot be edited.");
                if (lead == null) {
                    return;
                }
                request.setAttribute("lead", lead);
                forwardDashboard(request, response, "/sale/editLead.jsp");
                break;

            case "logForm":
                int logId = Integer.parseInt(request.getParameter("id"));
                Lead logLead = getLeadForAction(leadDAO, logId, session, response,
                        "Lead does not exist or is inactive.", "Converted lead cannot be logged.");
                if (logLead == null) {
                    return;
                }

                String nowDateTime = LocalDateTime.now().withSecond(0).withNano(0).toString();
                if (nowDateTime.length() > 16) {
                    nowDateTime = nowDateTime.substring(0, 16);
                }

                request.setAttribute("lead", logLead);
                request.setAttribute("nowDateTime", nowDateTime);
                forwardDashboard(request, response, "/sale/logConsultation.jsp");
                break;

            case "convertForm":
                int convertId = Integer.parseInt(request.getParameter("id"));
                Lead convertLead = getLeadForAction(leadDAO, convertId, session, response,
                        "Lead does not exist or is inactive.", "Lead has already been converted.");
                if (convertLead == null) {
                    return;
                }

                request.setAttribute("lead", convertLead);
                request.setAttribute("today", LocalDate.now().toString());
                forwardDashboard(request, response, "/sale/convertLead.jsp");
                break;

            case "delete":
                int deleteId = Integer.parseInt(request.getParameter("id"));
                Lead dLead = leadDAO.getLeadByID(deleteId);
                request.setAttribute("dLead", dLead);
                forwardDashboard(request, response, "/sale/deleteLead.jsp");
                break;

            case "salesReport":
                SalesDAO salesDAO = new SalesDAO();
                String fromDateRawReport = request.getParameter("fromDate");
                String toDateRawReport = request.getParameter("toDate");
                LocalDateTime[] salesRange = resolveReportRange(fromDateRawReport, toDateRawReport);
                LocalDateTime fromDateReport = salesRange[0];
                LocalDateTime toDateReport = salesRange[1];

                Object[] summary = salesDAO.getSalesSummary(fromDateReport, toDateReport, null);
                List<Object[]> monthlyRows = salesDAO.getMonthlySalesReport(fromDateReport, toDateReport, null);
                List<Object[]> filteredRows = filterNonEmptySalesRows(monthlyRows);

                request.setAttribute("totalLeads", summary[0]);
                request.setAttribute("convertedLeads", summary[1]);
                request.setAttribute("registeredStudents", summary[2]);
                request.setAttribute("conversionRate", summary[3]);
                request.setAttribute("monthlyRows", filteredRows);
                request.setAttribute("fromDate", fromDateReport.toLocalDate().toString());
                request.setAttribute("toDate", toDateReport.toLocalDate().toString());
                forwardDashboard(request, response, "/sale/viewSalesReport.jsp");
                break;

            case "revenueReport":
                SalesDAO revenueDAO = new SalesDAO();
                String fromDateRawRevenue = request.getParameter("fromDate");
                String toDateRawRevenue = request.getParameter("toDate");
                LocalDateTime[] revenueRange = resolveReportRange(fromDateRawRevenue, toDateRawRevenue);
                LocalDateTime fromDateRevenue = revenueRange[0];
                LocalDateTime toDateRevenue = revenueRange[1];

                Object[] revenueSummary = revenueDAO.getRevenueSummary(fromDateRevenue, toDateRevenue);
                List<Object[]> monthlyRevenueRows = revenueDAO.getMonthlyRevenueReport(fromDateRevenue, toDateRevenue);

                request.setAttribute("totalRevenue", revenueSummary[0]);
                request.setAttribute("verifiedPayments", revenueSummary[1]);
                request.setAttribute("payingStudents", revenueSummary[2]);
                request.setAttribute("monthlyRevenueRows", monthlyRevenueRows);
                request.setAttribute("fromDate", fromDateRevenue.toLocalDate().toString());
                request.setAttribute("toDate", toDateRevenue.toLocalDate().toString());
                forwardDashboard(request, response, "/sale/viewRevenueReport.jsp");
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

        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            response.sendRedirect("login");
            return;
        }

        if (currentUser.getRole() == null || !currentUser.getRole().getManageFinance()) {
            redirectWithMessage(session, response, request.getContextPath() + "/dashboard",
                    "Security Alert: Unauthorized action on Lead data!", "error");
            return;
        }

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
                redirectWithMessage(session, response, "lead?action=add", "Email is required.", "error");
                return;
            }
            if (normalizedPhone == null) {
                redirectWithMessage(session, response, "lead?action=add", "Phone is required.", "error");
                return;
            }

            if (userDAO.isFieldExists("email", normalizedEmail) || leadDAO.isEmailExists(normalizedEmail)) {
                redirectWithMessage(session, response, "lead?action=add",
                        "Email already exists. Please use another email.", "error");
                return;
            }
            if (userDAO.isFieldExists("phone", normalizedPhone) || leadDAO.isPhoneExists(normalizedPhone)) {
                redirectWithMessage(session, response, "lead?action=add",
                        "Phone already exists. Please use another phone number.", "error");
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

            redirectWithMessage(session, response, "lead?action=all", "Add new lead successfully!", "success");
            return;
        }

        if ("createStudentAtCenter".equals(action)) {
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String genderRaw = request.getParameter("gender");
            String dobRaw = request.getParameter("dob");
            String classIdRaw = request.getParameter("classId");
            String voucherCode = request.getParameter("voucherCode");

            if (isBlank(fullName) || isBlank(email) || isBlank(phone) || isBlank(genderRaw) || isBlank(dobRaw) || isBlank(classIdRaw)) {
                redirectWithMessage(session, response, "lead?action=addStudentAtCenter",
                        "Please fill in all required fields.", "error");
                return;
            }

            String normalizedEmail = email.trim();
            String normalizedPhone = phone.trim();
            if (userDAO.isFieldExists("email", normalizedEmail) || leadDAO.isEmailExists(normalizedEmail)) {
                redirectWithMessage(session, response, "lead?action=addStudentAtCenter",
                        "Email already exists. Please use another email.", "error");
                return;
            }
            if (userDAO.isFieldExists("phone", normalizedPhone) || leadDAO.isPhoneExists(normalizedPhone)) {
                redirectWithMessage(session, response, "lead?action=addStudentAtCenter",
                        "Phone already exists. Please use another phone number.", "error");
                return;
            }

            Boolean gender = null;
            if ("female".equalsIgnoreCase(genderRaw)) {
                gender = true;
            } else if ("male".equalsIgnoreCase(genderRaw)) {
                gender = false;
            }

            Date dob = parseSqlDate(dobRaw);
            if (dob == null) {
                redirectWithMessage(session, response, "lead?action=addStudentAtCenter",
                        "Invalid date of birth.", "error");
                return;
            }

            int classId = parsePositiveIntOrDefault(classIdRaw, 0);
            if (classId <= 0) {
                redirectWithMessage(session, response, "lead?action=addStudentAtCenter",
                        "Please select a class.", "error");
                return;
            }

            String password = EmailController.generateRandomPassword();
            Date enrollmentDate = new Date(System.currentTimeMillis());

            boolean created = userDAO.addNewUserFull(
                    fullName.trim(),
                    normalizedEmail,
                    password,
                    normalizedPhone,
                    null,
                    gender,
                    dob,
                    DEFAULT_AVATAR,
                    true,
                    5,
                    null,
                    null,
                    null,
                    enrollmentDate
            );

            if (!created) {
                redirectWithMessage(session, response, "lead?action=addStudentAtCenter",
                        "Failed to create student account.", "error");
                return;
            }

            User newUser = userDAO.getUserByEmail(normalizedEmail);
            if (newUser == null) {
                redirectWithMessage(session, response, "lead?action=addStudentAtCenter",
                        "Failed to fetch new student account.", "error");
                return;
            }

            dao.EnrollmentDAO enrollmentDAO = new dao.EnrollmentDAO();
            int enrollmentId = enrollmentDAO.getOrCreateEnrollment(newUser.getUserId(), classId);
            if (enrollmentId <= 0) {
                redirectWithMessage(session, response, "lead?action=addStudentAtCenter",
                        "Failed to enroll student into class.", "error");
                return;
            }

            dao.ClassDAO classDAO = new dao.ClassDAO();
            double amount = classDAO.getClassPrice(classId);
            double discountAmount = 0;
            if (!isBlank(voucherCode)) {
                dao.VoucherDAO voucherDAO = new dao.VoucherDAO();
                Voucher voucher = voucherDAO.getVoucherByCode(voucherCode.trim());
                if (voucher == null || !voucher.isStatus()) {
                    redirectWithMessage(session, response, "lead?action=addStudentAtCenter",
                            "Invalid or expired voucher code.", "error");
                    return;
                }
                if (!voucherDAO.isVoucherUsageAvailable(voucher.getVoucherId())) {
                    redirectWithMessage(session, response, "lead?action=addStudentAtCenter",
                            "This voucher has reached its usage limit.", "error");
                    return;
                }
                discountAmount = voucherDAO.calculateDiscountAmount(voucher, amount);
                if (discountAmount < 0) {
                    discountAmount = 0;
                }
            }
            double finalAmount = amount - discountAmount;
            if (finalAmount < 0) {
                finalAmount = 0;
            }

            boolean emailSent = EmailController.sendLeadConversionEmail(normalizedEmail, fullName.trim(), password);
            if (emailSent) {
                setSessionMessage(session,
                        "Student created, enrolled, and payment approved. Temporary password has been sent to the email.",
                        "success");
            } else {
                setSessionMessage(session,
                        "Student created and enrolled, but failed to send password email. Please inform the student manually.",
                        "error");
            }
            response.sendRedirect("lead?action=addStudentAtCenter");
            return;
        }

        if ("update".equals(action)) {
            int id = Integer.parseInt(request.getParameter("leadId"));
            Lead currentLead = leadDAO.getLeadByID(id);
            if (currentLead == null) {
                redirectWithMessage(session, response, "lead?action=all", "Lead not found.", "error");
                return;
            }
            if ("Converted".equalsIgnoreCase(currentLead.getStatus())) {
                redirectWithMessage(session, response, "lead?action=all", "Converted lead cannot be edited.", "error");
                return;
            }

            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            int interestedCourseID = currentLead.getInterestedCourseID();
            String status = normalizeStatus(request.getParameter("status"));
            String note = request.getParameter("note");

            if (!"New".equalsIgnoreCase(status) && !"Contacted".equalsIgnoreCase(status)) {
                redirectWithMessage(session, response, "lead?action=all",
                        "Only New or Contacted status can be edited.", "error");
                return;
            }

            String interestedCourseIDParam = request.getParameter("interestedCourseID");
            if (interestedCourseIDParam != null && !interestedCourseIDParam.trim().isEmpty()) {
                interestedCourseID = Integer.parseInt(interestedCourseIDParam);
            }

            boolean updated = leadDAO.updateLead(id, fullName, email, phone, interestedCourseID, status, note);
            if (updated) {
                setSessionMessage(session, "Update lead successfully!", "success");
            } else {
                setSessionMessage(session, "Update lead failed.", "error");
            }
            response.sendRedirect("lead?action=all");
            return;
        }

        if ("logConsultation".equals(action)) {
            String leadIdRaw = request.getParameter("leadId");
            String consultationNote = request.getParameter("consultationNote");
            String consultDateRaw = request.getParameter("consultDate");

            if (isBlank(leadIdRaw)) {
                redirectWithMessage(session, response, "lead?action=all", "Invalid lead id.", "error");
                return;
            }

            int leadId = Integer.parseInt(leadIdRaw);
            Lead lead = getLeadForAction(leadDAO, leadId, session, response,
                    "Lead does not exist or is inactive.", "Converted lead cannot be logged.");
            if (lead == null) {
                return;
            }

            if (isBlank(consultationNote)) {
                redirectWithMessage(session, response, "lead?action=logForm&id=" + leadId,
                        "Consultation note is required.", "error");
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
                setSessionMessage(session, "Consultation record saved successfully.", "success");
            } else {
                setSessionMessage(session, "Failed to save consultation record.", "error");
            }

            response.sendRedirect("lead?action=detail&id=" + leadId);
            return;
        }

        if ("convert".equals(action)) {
            int leadId = Integer.parseInt(request.getParameter("leadId"));
            Lead lead = getLeadForAction(leadDAO, leadId, session, response,
                    "Lead not found or inactive.", "Lead has already been converted.");
            if (lead == null) {
                return;
            }

            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String dobRaw = request.getParameter("dob");
            String genderRaw = request.getParameter("gender");
            String defaultPassword = EmailController.generateRandomPassword();
            String enrollmentDateRaw = request.getParameter("enrollmentDate");
            String convertNote = request.getParameter("convertNote");

            if (isBlank(fullName) || isBlank(email)) {
                redirectWithMessage(session, response, "lead?action=convertForm&id=" + leadId,
                        "Full name and email are required for conversion.", "error");
                return;
            }

            if (userDAO.isFieldExists("email", email.trim())) {
                redirectWithMessage(session, response, "lead?action=convertForm&id=" + leadId,
                        "Email already exists in user system.", "error");
                return;
            }

            Date dob = parseSqlDate(dobRaw);
            if (!isBlank(dobRaw) && dob == null) {
                redirectWithMessage(session, response, "lead?action=convertForm&id=" + leadId,
                        "Invalid DOB format.", "error");
                return;
            }
            Date enrollmentDate = parseSqlDate(enrollmentDateRaw);
            if (enrollmentDate == null) {
                enrollmentDate = new Date(System.currentTimeMillis());
            }

            Boolean gender = null;
            if ("female".equalsIgnoreCase(genderRaw)) {
                gender = true;
            } else if ("male".equalsIgnoreCase(genderRaw)) {
                gender = false;
            }
            boolean created = userDAO.addNewUserFull(
                    fullName.trim(),
                    email.trim(),
                    defaultPassword,
                    isBlank(phone) ? null : phone.trim(),
                    isBlank(address) ? null : address.trim(),
                    gender,
                    dob,
                    DEFAULT_AVATAR,
                    true,
                    5,
                    null,
                    null,
                    null,
                    enrollmentDate
            );

            if (!created) {
                redirectWithMessage(session, response, "lead?action=convertForm&id=" + leadId,
                        "Convert lead failed.", "error");
                return;
            }

            String mergedNote = appendNote(lead.getNote(), convertNote);
            leadDAO.updateLead(leadId, fullName.trim(), email.trim(),
                    isBlank(phone) ? lead.getPhone() : phone.trim(),
                    lead.getInterestedCourseID(), "Converted", mergedNote);

            boolean emailSent = EmailController.sendLeadConversionEmail(email.trim(), fullName.trim(), defaultPassword);
            if (emailSent) {
                setSessionMessage(session,
                        "Convert successful. A temporary password has been sent to the student email.", "success");
            } else {
                setSessionMessage(session,
                        "Convert successful, but failed to send password email. Please send credentials manually.",
                        "error");
            }
            response.sendRedirect("lead?action=all");
            return;
        }

        if ("delete".equals(action)) {
            Integer id = parseLeadIdParam(request);
            if (id != null) {
                leadDAO.deleteLead(id);
                setSessionMessage(session, "Lead has been moved to inactive.", "success");
            } else {
                setSessionMessage(session, "Delete lead failed.", "error");
            }
            response.sendRedirect("lead?action=all");
            return;
        }

        if ("restore".equals(action)) {
            Integer id = parseLeadIdParam(request);
            if (id != null) {
                leadDAO.restoreLead(id);
                setSessionMessage(session, "Lead has been restored to new.", "success");
            } else {
                setSessionMessage(session, "Restore lead failed.", "error");
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

    private void setSessionMessage(HttpSession session, String message, String type) {
        session.setAttribute("message", message);
        session.setAttribute("messageType", type);
    }

    private void redirectWithMessage(HttpSession session, HttpServletResponse response,
            String location, String message, String type) throws IOException {
        setSessionMessage(session, message, type);
        response.sendRedirect(location);
    }

    private void forwardDashboard(HttpServletRequest request, HttpServletResponse response, String homeView)
            throws ServletException, IOException {
        request.setAttribute("home_view", homeView);
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }

    private Lead getLeadForAction(LeadDAO leadDAO, int leadId, HttpSession session,
            HttpServletResponse response, String inactiveMessage, String convertedMessage) throws IOException {
        Lead lead = leadDAO.getLeadByID(leadId);
        if (lead == null || "Inactive".equalsIgnoreCase(lead.getStatus())) {
            redirectWithMessage(session, response, "lead?action=all", inactiveMessage, "error");
            return null;
        }
        if (convertedMessage != null && "Converted".equalsIgnoreCase(lead.getStatus())) {
            redirectWithMessage(session, response, "lead?action=all", convertedMessage, "error");
            return null;
        }
        return lead;
    }

    private Integer parseLeadIdParam(HttpServletRequest request) {
        Integer leadId = parseIntegerOrNull(request.getParameter("leadID"));
        return leadId != null ? leadId : parseIntegerOrNull(request.getParameter("leadId"));
    }

    private LocalDateTime[] resolveReportRange(String fromDateRaw, String toDateRaw) {
        LocalDate defaultTo = LocalDate.now();
        LocalDate defaultFrom = defaultTo.minusMonths(5).withDayOfMonth(1);

        LocalDateTime fromDate = parseFilterDateTimeStart(fromDateRaw);
        LocalDateTime toDate = parseFilterDateTimeEnd(toDateRaw);
        if (fromDate == null) {
            fromDate = defaultFrom.atStartOfDay();
        }
        if (toDate == null) {
            toDate = defaultTo.atTime(23, 59, 59);
        }
        if (fromDate.isAfter(toDate)) {
            LocalDateTime temp = fromDate;
            fromDate = toDate.minusMonths(1);
            toDate = temp.plusMonths(1);
        }

        return new LocalDateTime[]{fromDate, toDate};
    }

    private List<Object[]> filterNonEmptySalesRows(List<Object[]> monthlyRows) {
        List<Object[]> filteredRows = new java.util.ArrayList<>();
        for (Object[] row : monthlyRows) {
            int total = row[1] == null ? 0 : ((Number) row[1]).intValue();
            int converted = row[2] == null ? 0 : ((Number) row[2]).intValue();
            int students = row[3] == null ? 0 : ((Number) row[3]).intValue();
            if (total > 0 || converted > 0 || students > 0) {
                filteredRows.add(row);
            }
        }
        return filteredRows;
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
