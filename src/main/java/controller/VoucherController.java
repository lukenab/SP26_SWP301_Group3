package controller;

import dao.SystemLogDAO;
import dao.VoucherDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import model.User;
import model.Voucher;

@WebServlet(name = "VoucherController", urlPatterns = {"/voucher"})
public class VoucherController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        VoucherDAO voucherDAO = new VoucherDAO();

        if (action == null) {
            action = "all";
        }

        switch (action) {
            case "all":
                String searchQuery = request.getParameter("searchQuery");
                String status = request.getParameter("status");
                if (status == null || status.trim().isEmpty()) {
                    status = "all";
                }

                List<Voucher> voucherList = voucherDAO.searchAndFilterVouchers(searchQuery, status);
                request.setAttribute("voucherList", voucherList);
                request.setAttribute("searchQuery", searchQuery == null ? "" : searchQuery);
                request.setAttribute("statusFilter", status);
                request.setAttribute("today", LocalDate.now().toString());
                request.setAttribute("home_view", "/sale/viewVoucherList.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;
            case "report":
                String fromDateRaw = request.getParameter("fromDate");
                String toDateRaw = request.getParameter("toDate");

                LocalDate defaultFromDate = LocalDate.now().withDayOfYear(1);
                LocalDate defaultToDate = LocalDate.now().plusMonths(12);

                LocalDate fromDateValue = parseDateOrNull(fromDateRaw);
                LocalDate toDateValue = parseDateOrNull(toDateRaw);
                if (fromDateValue == null) {
                    fromDateValue = defaultFromDate;
                }
                if (toDateValue == null) {
                    toDateValue = defaultToDate;
                }

                if (fromDateValue.isAfter(toDateValue)) {
                    LocalDate temp = fromDateValue;
                    fromDateValue = toDateValue;
                    toDateValue = temp;
                }

                Date fromDate = Date.valueOf(fromDateValue);
                Date toDate = Date.valueOf(toDateValue);
                Object[] summary = voucherDAO.getVoucherInventoryReportSummary(fromDate, toDate);
                List<Object[]> voucherRows = voucherDAO.getVoucherInventoryReport(fromDate, toDate);
                List<Object[]> rawMonthlyRows = voucherDAO.getVoucherInventoryMonthlyReport(fromDate, toDate);
                List<Object[]> monthlyRows = new java.util.ArrayList<>();
                for (Object[] row : rawMonthlyRows) {
                    int issued = row[1] == null ? 0 : ((Number) row[1]).intValue();
                    int used = row[2] == null ? 0 : ((Number) row[2]).intValue();
                    int remaining = row[3] == null ? 0 : ((Number) row[3]).intValue();
                    if (issued > 0 || used > 0 || remaining > 0) {
                        monthlyRows.add(row);
                    }
                }

                request.setAttribute("totalVouchers", summary[0]);
                request.setAttribute("activeVouchers", summary[1]);
                request.setAttribute("totalIssued", summary[2]);
                request.setAttribute("totalRemaining", summary[3]);
                request.setAttribute("voucherRows", voucherRows);
                request.setAttribute("monthlyRows", monthlyRows);
                request.setAttribute("fromDate", fromDateValue.toString());
                request.setAttribute("toDate", toDateValue.toString());
                request.setAttribute("home_view", "/sale/viewVoucherReport.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;
            case "add":
                request.setAttribute("home_view", "/sale/AddVoucher.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;
            case "detail":
                int detailId = parseInt(request.getParameter("id"));
                Voucher detailVoucher = voucherDAO.getVoucherByID(detailId);
                if (detailVoucher == null) {
                    setSessionMessage(request.getSession(), "Voucher not found.", "error");
                    response.sendRedirect("voucher?action=all");
                    return;
                }
                request.setAttribute("voucher", detailVoucher);
                request.setAttribute("home_view", "/sale/voucherDetail.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;
            case "edit":
                int editId = parseInt(request.getParameter("id"));
                Voucher editVoucher = voucherDAO.getVoucherByID(editId);
                if (editVoucher == null) {
                    setSessionMessage(request.getSession(), "Voucher not found.", "error");
                    response.sendRedirect("voucher?action=all");
                    return;
                }
                request.setAttribute("voucher", editVoucher);
                request.setAttribute("home_view", "/sale/editVoucher.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;
            case "delete":
                int deleteId = parseInt(request.getParameter("id"));
                Voucher deleteVoucher = voucherDAO.getVoucherByID(deleteId);
                if (deleteVoucher == null) {
                    setSessionMessage(request.getSession(), "Voucher not found.", "error");
                    response.sendRedirect("voucher?action=all");
                    return;
                }
                request.setAttribute("voucher", deleteVoucher);
                request.setAttribute("home_view", "/sale/deleteVoucher.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;
            default:
                response.sendRedirect("voucher?action=all");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        VoucherDAO voucherDAO = new VoucherDAO();

        if ("create".equals(action)) {
            handleCreateVoucher(request, response);
            return;
        }

        if ("update".equals(action)) {
            int id = parseInt(request.getParameter("voucherId"));
            String code = normalizeCode(request.getParameter("code"));
            String discountType = request.getParameter("discountType");
            String discountValue = request.getParameter("discountValue");
            String discountAmountRaw = request.getParameter("discountAmount");
            String discountPercentRaw = request.getParameter("discountPercent");
            Integer maxUsage = parsePositiveIntOrNull(request.getParameter("maxUsage"));
            BigDecimal discountAmount;
            double discountPercent;
            Date validUntil = parseSqlDate(request.getParameter("validUntil"));
            boolean status = "1".equals(request.getParameter("status"));

            if ("amount".equalsIgnoreCase(discountType)) {
                discountAmount = parseAmount(isBlank(discountValue) ? discountAmountRaw : discountValue);
                discountPercent = 0;
            } else if ("percent".equalsIgnoreCase(discountType)) {
                discountAmount = BigDecimal.ZERO;
                discountPercent = parsePercent(isBlank(discountValue) ? discountPercentRaw : discountValue);
            } else {
                setSessionMessage(session, "Please choose discount type.", "error");
                response.sendRedirect("voucher?action=all");
                return;
            }

            if (id <= 0 || !isVoucherInputValid(code, discountAmount, discountPercent, maxUsage, session)) {
                response.sendRedirect("voucher?action=all");
                return;
            }

            if (voucherDAO.isCodeExists(code, id)) {
                setSessionMessage(session, "Voucher code already exists.", "error");
                response.sendRedirect("voucher?action=all");
                return;
            }

            boolean updated = voucherDAO.updateVoucher(id, code, discountAmount, discountPercent, validUntil, status, maxUsage);

            if (updated) {
                SystemLogDAO logDAO = new SystemLogDAO();
                User logUser = (User) request.getSession().getAttribute("user");

                String actorName = (logUser != null) ? logUser.getFullName() : "System";
                String actorRole = (logUser != null && logUser.getRole() != null) ? logUser.getRole().getRoleName() : "Sale Staff";

                logDAO.insertLog(actorName, actorRole, "UPDATE_VOUCHER", "Updated Voucher ID: " + id + " (Code: " + code + ")");

                setSessionMessage(session, "Update voucher successfully!", "success");
            } else {
                setSessionMessage(session, "Update voucher failed.", "error");
            }
            response.sendRedirect("voucher?action=all");
            return;
        }

        if ("delete".equals(action)) {
            int id = parseInt(request.getParameter("voucherId"));
            if (id > 0) {
                voucherDAO.deleteVoucher(id);

                SystemLogDAO logDAO = new SystemLogDAO();
                User logUser = (User) request.getSession().getAttribute("user");

                String actorName = (logUser != null) ? logUser.getFullName() : "System";
                String actorRole = (logUser != null && logUser.getRole() != null) ? logUser.getRole().getRoleName() : "Sale Staff";
                logDAO.insertLog(actorName, actorRole, "DEACTIVATE_VOUCHER", "Deactivated/Deleted Voucher ID: " + id);

                setSessionMessage(session, "Voucher has been moved to inactive.", "success");
            } else {
                setSessionMessage(session, "Delete voucher failed.", "error");
            }
            response.sendRedirect("voucher?action=all");
            return;
        }

        if ("restore".equals(action)) {
            int id = parseInt(request.getParameter("voucherId"));
            if (id > 0) {
                voucherDAO.restoreVoucher(id);

                SystemLogDAO logDAO = new SystemLogDAO();
                User logUser = (User) request.getSession().getAttribute("user");

                String actorName = (logUser != null) ? logUser.getFullName() : "System";
                String actorRole = (logUser != null && logUser.getRole() != null) ? logUser.getRole().getRoleName() : "Sale Staff";

                logDAO.insertLog(actorName, actorRole, "ACTIVATE_VOUCHER", "Restored Voucher ID: " + id);

                setSessionMessage(session, "Voucher has been restored.", "success");
            } else {
                setSessionMessage(session, "Restore voucher failed.", "error");
            }
            response.sendRedirect("voucher?action=all");
            return;
        }

        response.sendRedirect("voucher?action=all");
    }

    private void handleCreateVoucher(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        VoucherDAO voucherDAO = new VoucherDAO();
        HttpSession session = request.getSession();

        String code = normalizeCode(request.getParameter("code"));
        String discountType = request.getParameter("discountType");
        String discountValue = request.getParameter("discountValue");
        String discountAmountRaw = request.getParameter("discountAmount");
        String discountPercentRaw = request.getParameter("discountPercent");
        Integer maxUsage = parsePositiveIntOrNull(request.getParameter("maxUsage"));
        BigDecimal discountAmount;
        double discountPercent;
        Date validUntil = parseSqlDate(request.getParameter("validUntil"));
        boolean status = "1".equals(request.getParameter("status"));

        if ("amount".equalsIgnoreCase(discountType)) {
            discountAmount = parseAmount(isBlank(discountValue) ? discountAmountRaw : discountValue);
            discountPercent = 0;
        } else if ("percent".equalsIgnoreCase(discountType)) {
            discountAmount = BigDecimal.ZERO;
            discountPercent = parsePercent(isBlank(discountValue) ? discountPercentRaw : discountValue);
        } else {
            setSessionMessage(session, "Please choose discount type.", "error");
            response.sendRedirect("voucher?action=all");
            return;
        }

        if (!isVoucherInputValid(code, discountAmount, discountPercent, maxUsage, session)) {
            response.sendRedirect("voucher?action=all");
            return;
        }

        if (voucherDAO.isCodeExists(code, null)) {
            setSessionMessage(session, "Voucher code already exists.", "error");
            response.sendRedirect("voucher?action=all");
            return;
        }

        Voucher voucher = new Voucher();
        voucher.setCode(code);
        voucher.setDiscountAmount(discountAmount);
        voucher.setDiscountPercent(discountPercent);
        voucher.setValidUntil(validUntil);
        voucher.setStatus(status);
        voucher.setMaxUsage(maxUsage);
        voucherDAO.insertVoucher(voucher);

        SystemLogDAO logDAO = new SystemLogDAO();
        User logUser = (User) request.getSession().getAttribute("user");

        String actorName = (logUser != null) ? logUser.getFullName() : "System";
        String actorRole = (logUser != null && logUser.getRole() != null) ? logUser.getRole().getRoleName() : "Sale Staff";
        String logDetail = "Created new voucher: " + code;
        if (discountAmount.compareTo(BigDecimal.ZERO) > 0) {
            logDetail += " (Discount: " + discountAmount + " VND)";
        } else {
            logDetail += " (Discount: " + discountPercent + "%)";
        }
        logDAO.insertLog(actorName, actorRole, "CREATE_VOUCHER", logDetail);

        setSessionMessage(session, "Create voucher successfully!", "success");
        response.sendRedirect("voucher?action=all");
    }

    private void setSessionMessage(HttpSession session, String message, String type) {
        session.setAttribute("message", message);
        session.setAttribute("messageType", type);
    }

    private String normalizeCode(String code) {
        return code == null ? null : code.trim().toUpperCase();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return -1;
        }
    }

    private BigDecimal parseAmount(String value) {
        try {
            if (value == null || value.trim().isEmpty()) {
                return BigDecimal.ZERO;
            }
            return new BigDecimal(value.trim());
        } catch (Exception e) {
            return null;
        }
    }

    private double parsePercent(String value) {
        try {
            if (value == null || value.trim().isEmpty()) {
                return 0;
            }
            return Double.parseDouble(value.trim());
        } catch (Exception e) {
            return -1;
        }
    }

    private Date parseSqlDate(String value) {
        try {
            if (value == null || value.trim().isEmpty()) {
                return null;
            }
            return Date.valueOf(value.trim());
        } catch (Exception e) {
            return null;
        }
    }

    private LocalDateTime parseFilterDateTimeStart(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Date.valueOf(value.trim()).toLocalDate().atStartOfDay();
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private LocalDateTime parseFilterDateTimeEnd(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Date.valueOf(value.trim()).toLocalDate().atTime(23, 59, 59);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private LocalDate parseDateOrNull(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Date.valueOf(value.trim()).toLocalDate();
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private boolean isVoucherInputValid(String code, BigDecimal discountAmount, double discountPercent, Integer maxUsage, HttpSession session) {
        if (code == null || code.isEmpty()) {
            setSessionMessage(session, "Voucher code is required.", "error");
            return false;
        }
        if (maxUsage == null || maxUsage <= 0) {
            setSessionMessage(session, "Max usage must be greater than 0.", "error");
            return false;
        }
        if (discountAmount == null || discountAmount.compareTo(BigDecimal.ZERO) < 0) {
            setSessionMessage(session, "Discount amount is invalid.", "error");
            return false;
        }
        if (discountPercent < 0 || discountPercent > 100) {
            setSessionMessage(session, "Discount percent must be from 0 to 100.", "error");
            return false;
        }
        boolean hasAmount = discountAmount.compareTo(BigDecimal.ZERO) > 0;
        boolean hasPercent = discountPercent > 0;
        if (!hasAmount && !hasPercent) {
            setSessionMessage(session, "Voucher must have discount amount or discount percent greater than 0.", "error");
            return false;
        }
        if (hasAmount && hasPercent) {
            setSessionMessage(session, "Please choose only one discount type: amount or percent.", "error");
            return false;
        }
        return true;
    }

    private Integer parsePositiveIntOrNull(String value) {
        try {
            if (value == null || value.trim().isEmpty()) {
                return null;
            }
            int parsed = Integer.parseInt(value.trim());
            return parsed > 0 ? parsed : null;
        } catch (Exception e) {
            return null;
        }
    }
}
