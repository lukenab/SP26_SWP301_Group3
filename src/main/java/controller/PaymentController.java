/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.PaymentDAO;
import dao.PaymentDAO.PaymentDisplay;
import dao.CourseDAO;
import dao.ClassDAO;
import dao.VoucherDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;
import model.Payment;
import model.User;
import model.Voucher;

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

        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        switch (action) {
            case "list":
                handleListPayments(request, response, paymentDAO, courseDAO, classDAO);
                break;
            case "view":
                handleViewPayment(request, response, paymentDAO);
                break;

            case "review":
                try {
                    String classId = request.getParameter("classId");
                    String className = request.getParameter("className");
                    String amountStr = request.getParameter("amount");
                    String voucherCode = request.getParameter("voucherCode");

                    double originalAmount = Double.parseDouble(amountStr);
                    double discountAmount = 0;
                    String voucherMessage = "";
                    int voucherId = -1;

                    if (voucherCode != null && !voucherCode.trim().isEmpty()) {
                        VoucherDAO voucherDAO = new VoucherDAO();
                        Voucher voucher = voucherDAO.getVoucherByCode(voucherCode);

                        if (voucher != null && voucher.isStatus()) {

                            if (voucher.getDiscountAmount() != null && voucher.getDiscountAmount().doubleValue() > 0) {
                                discountAmount = voucher.getDiscountAmount().doubleValue();
                            } else if (voucher.getDiscountPercent() > 0) {
                                discountAmount = originalAmount * (voucher.getDiscountPercent() / 100.0);
                            }
                            voucherMessage = "Discount code applied successfully!";
                            voucherId = voucher.getVoucherId();
                            request.setAttribute("voucherType", "success");
                        } else {
                            voucherMessage = "The discount code is invalid or has expired!";
                            request.setAttribute("voucherType", "error");
                        }
                    }

                    double finalAmount = originalAmount - discountAmount;
                    if (finalAmount < 0) {
                        finalAmount = 0;
                    }

                    request.setAttribute("classId", classId);
                    request.setAttribute("className", className);
                    request.setAttribute("originalAmount", originalAmount);
                    request.setAttribute("discountAmount", discountAmount);
                    request.setAttribute("finalAmount", finalAmount);
                    request.setAttribute("voucherCode", voucherCode);
                    request.setAttribute("voucherMessage", voucherMessage);
                    request.setAttribute("voucherId", voucherId);

                    request.setAttribute("home_view", "/student/reviewPayment.jsp");
                    request.getRequestDispatcher("dashboard.jsp").forward(request, response);

                } catch (Exception e) {
                    System.out.println("Fail at action review: " + e.getMessage());
                }
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
        if (action == null) {
            response.sendRedirect("payment?action=list");
            return;
        }

        PaymentDAO paymentDAO = new PaymentDAO();

        switch (action) {
            case "approve":
                handleApprovePayment(request, response, paymentDAO);
                break;
            case "reject":
                handleRejectPayment(request, response, paymentDAO);
                break;
            case "confirmPayment":
                HttpSession session = request.getSession();
                try {
                    int enrollmentId = Integer.parseInt(request.getParameter("enrollmentId"));
                    double amount = Double.parseDouble(request.getParameter("amount"));

                    boolean isSuccess = paymentDAO.confirmQRPayment(enrollmentId, amount);

                    if (isSuccess) {
                        session.setAttribute("message", "Confirmation successful! Please wait while the center verifies the transaction.");
                        session.setAttribute("messageType", "success");
                    } else {
                        session.setAttribute("message", "An error occurred while confirming the payment. Please try again.");
                        session.setAttribute("messageType", "error");
                    }

                    response.sendRedirect("class?action=availableClass");

                } catch (Exception e) {
                    System.out.println("PaymentController doPost Error: " + e.getMessage());
                    session.setAttribute("message", "Error processing payment confirmation!");
                    session.setAttribute("messageType", "error");
                    response.sendRedirect("dashboard");
                }
                break;

            case "checkout":
                try {
                    int classId = Integer.parseInt(request.getParameter("classId"));
                    double finalAmount = Double.parseDouble(request.getParameter("finalAmount"));
                    String className = request.getParameter("className");

                    HttpSession sessionCheckout = request.getSession();
                    User currentU = (User) sessionCheckout.getAttribute("user");

                    dao.EnrollmentDAO enrollmentDAO = new dao.EnrollmentDAO();
                    int enrollmentId = enrollmentDAO.getOrCreateEnrollment(currentU.getUserId(), classId);

                    if (enrollmentId == -1) {
                        response.sendRedirect("dashboard");
                        return;
                    }

                    String bankId = "MB";
                    String accountNo = "0907625043";
                    String accountName = "LMCS Center";
                    String rawAddInfo = "LMCS " + enrollmentId + " " + className;
                    String addInfo = rawAddInfo.replaceAll(" ", "%20");
                    String urlAccountName = accountName.replaceAll(" ", "%20");

                    long amountToPay = (long) finalAmount;

                    String qrUrl = "https://img.vietqr.io/image/" + bankId + "-" + accountNo + "-compact2.png"
                            + "?amount=" + amountToPay
                            + "&addInfo=" + addInfo
                            + "&accountName=" + urlAccountName;

                    request.setAttribute("qrUrl", qrUrl);
                    request.setAttribute("amount", amountToPay);
                    request.setAttribute("addInfo", rawAddInfo);
                    request.setAttribute("enrollmentId", enrollmentId);
                    request.setAttribute("qrUrl", qrUrl);
                    request.setAttribute("amount", amountToPay);
                    request.setAttribute("addInfo", rawAddInfo);
                    request.setAttribute("enrollmentId", enrollmentId);

                    request.setAttribute("className", className);

                    request.getRequestDispatcher("payment.jsp").forward(request, response);

                } catch (Exception e) {
                    System.out.println("Lỗi checkout: " + e.getMessage());
                    response.sendRedirect("dashboard");
                }
                break;
            default:
                response.sendRedirect("payment?action=list");
                break;
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
