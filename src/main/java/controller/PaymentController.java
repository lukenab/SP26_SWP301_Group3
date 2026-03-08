/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.PaymentDAO;
import dao.PaymentDAO.PaymentDisplay;
import dao.CourseDAO;
import dao.ClassDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import model.Payment;

/**
 *
 * @author Legion
 */
@WebServlet(name = "PaymentController", urlPatterns = {"/payment"})
public class PaymentController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        PaymentDAO paymentDAO = new PaymentDAO();
        CourseDAO courseDAO = new CourseDAO();
        ClassDAO classDAO = new ClassDAO();

        switch (action) {
            case "list":
                handleListPayments(request, response, paymentDAO, courseDAO, classDAO);
                break;
            case "view":
                handleViewPayment(request, response, paymentDAO);
                break;
            default:
                handleListPayments(request, response, paymentDAO, courseDAO, classDAO);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        PaymentDAO paymentDAO = new PaymentDAO();

        if ("approve".equals(action)) {
            handleApprovePayment(request, response, paymentDAO);
        } else if ("reject".equals(action)) {
            handleRejectPayment(request, response, paymentDAO);
        } else {
            response.sendRedirect("payment?action=list");
        }
    }

    /**
     * Handle listing payments with filters
     */
    private void handleListPayments(HttpServletRequest request, HttpServletResponse response,
            PaymentDAO paymentDAO, CourseDAO courseDAO, ClassDAO classDAO)
            throws ServletException, IOException {

        // Get filter parameters
        String courseIdParam = request.getParameter("courseId");
        String classIdParam = request.getParameter("classId");
        String status = request.getParameter("status");

        Integer courseId = null;
        Integer classId = null;

        try {
            if (courseIdParam != null && !courseIdParam.isEmpty() && !courseIdParam.equals("0")) {
                courseId = Integer.parseInt(courseIdParam);
            }
            if (classIdParam != null && !classIdParam.isEmpty() && !classIdParam.equals("0")) {
                classId = Integer.parseInt(classIdParam);
            }
        } catch (NumberFormatException e) {
            // Invalid format, ignore
        }

        // Get filtered payments
        List<PaymentDisplay> paymentList;
        if (courseId != null || classId != null || (status != null && !status.isEmpty())) {
            paymentList = paymentDAO.getFilteredPayments(courseId, classId, status);
        } else {
            paymentList = paymentDAO.getAllPayments();
        }

        // Get statistics
        int totalPayments = paymentDAO.getPaymentCountByStatus(null);
        int pendingPayments = paymentDAO.getPaymentCountByStatus("Pending");
        int approvedPayments = paymentDAO.getPaymentCountByStatus("Approved");
        int rejectedPayments = paymentDAO.getPaymentCountByStatus("Rejected");

        BigDecimal totalAmount = paymentDAO.getTotalAmountByStatus(null);
        BigDecimal approvedAmount = paymentDAO.getTotalAmountByStatus("Approved");

        // Get filter options
        List<Object[]> courseOptions = classDAO.getActiveCoursesForClassForm();
        List<Object[]> classList = classDAO.getClassManagementList();

        // Set attributes
        request.setAttribute("paymentList", paymentList);
        request.setAttribute("totalPayments", totalPayments);
        request.setAttribute("pendingPayments", pendingPayments);
        request.setAttribute("approvedPayments", approvedPayments);
        request.setAttribute("rejectedPayments", rejectedPayments);
        request.setAttribute("totalAmount", totalAmount);
        request.setAttribute("approvedAmount", approvedAmount);
        request.setAttribute("courseOptions", courseOptions);
        request.setAttribute("classList", classList);

        // Set current filters for display
        request.setAttribute("selectedCourseId", courseIdParam);
        request.setAttribute("selectedClassId", classIdParam);
        request.setAttribute("selectedStatus", status);

        request.setAttribute("home_view", "/academic/payment_list.jsp");
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }

    /**
     * Handle viewing payment details
     */
    private void handleViewPayment(HttpServletRequest request, HttpServletResponse response,
            PaymentDAO paymentDAO)
            throws ServletException, IOException {

        String paymentIdParam = request.getParameter("id");
        if (paymentIdParam == null || paymentIdParam.isEmpty()) {
            response.sendRedirect("payment?action=list");
            return;
        }

        try {
            int paymentId = Integer.parseInt(paymentIdParam);
            Payment payment = paymentDAO.getPaymentById(paymentId);

            if (payment == null) {
                request.getSession().setAttribute("message", "Payment not found.");
                request.getSession().setAttribute("messageType", "error");
                response.sendRedirect("payment?action=list");
                return;
            }

            request.setAttribute("payment", payment);
            request.setAttribute("home_view", "/academic/payment_detail.jsp");
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect("payment?action=list");
        }
    }

    /**
     * Handle approving payment
     */
    private void handleApprovePayment(HttpServletRequest request, HttpServletResponse response,
            PaymentDAO paymentDAO)
            throws ServletException, IOException {

        String paymentIdParam = request.getParameter("paymentId");
        if (paymentIdParam == null || paymentIdParam.isEmpty()) {
            response.sendRedirect("payment?action=list");
            return;
        }

        try {
            int paymentId = Integer.parseInt(paymentIdParam);
            boolean success = paymentDAO.updatePaymentStatus(paymentId, "Approved");

            if (success) {
                request.getSession().setAttribute("message", "Payment approved successfully.");
                request.getSession().setAttribute("messageType", "success");
            } else {
                request.getSession().setAttribute("message", "Failed to approve payment.");
                request.getSession().setAttribute("messageType", "error");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("message", "Invalid payment ID.");
            request.getSession().setAttribute("messageType", "error");
        }

        response.sendRedirect("payment?action=list");
    }

    /**
     * Handle rejecting payment
     */
    private void handleRejectPayment(HttpServletRequest request, HttpServletResponse response,
            PaymentDAO paymentDAO)
            throws ServletException, IOException {

        String paymentIdParam = request.getParameter("paymentId");
        if (paymentIdParam == null || paymentIdParam.isEmpty()) {
            response.sendRedirect("payment?action=list");
            return;
        }

        try {
            int paymentId = Integer.parseInt(paymentIdParam);
            boolean success = paymentDAO.updatePaymentStatus(paymentId, "Rejected");

            if (success) {
                request.getSession().setAttribute("message", "Payment rejected successfully.");
                request.getSession().setAttribute("messageType", "success");
            } else {
                request.getSession().setAttribute("message", "Failed to reject payment.");
                request.getSession().setAttribute("messageType", "error");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("message", "Invalid payment ID.");
            request.getSession().setAttribute("messageType", "error");
        }

        response.sendRedirect("payment?action=list");
    }
}

