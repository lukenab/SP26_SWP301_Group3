package controller;

import dao.PaymentDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet(name = "PaymentController", urlPatterns = {"/payment"})
public class PaymentController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null || action.trim().isEmpty()) {
            action = "all";
        }

        PaymentDAO paymentDAO = new PaymentDAO();

        if ("all".equals(action)) {
            String searchQuery = request.getParameter("searchQuery");
            String status = request.getParameter("status");
            String fromDateRaw = request.getParameter("fromDate");
            String toDateRaw = request.getParameter("toDate");

            if (status == null || status.trim().isEmpty()) {
                status = "all";
            }

            LocalDateTime fromDate = parseFilterDateTimeStart(fromDateRaw);
            LocalDateTime toDate = parseFilterDateTimeEnd(toDateRaw);
            List<Object[]> paymentList = paymentDAO.searchAndFilterPayments(searchQuery, status, fromDate, toDate);

            request.setAttribute("paymentList", paymentList);
            request.setAttribute("totalPayments", paymentList.size());
            request.setAttribute("searchQuery", searchQuery == null ? "" : searchQuery);
            request.setAttribute("statusFilter", status);
            request.setAttribute("fromDate", fromDateRaw == null ? "" : fromDateRaw);
            request.setAttribute("toDate", toDateRaw == null ? "" : toDateRaw);
            request.setAttribute("home_view", "/sale/viewPaymentStatus.jsp");
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
            return;
        }

        response.sendRedirect("payment?action=all");
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
}
