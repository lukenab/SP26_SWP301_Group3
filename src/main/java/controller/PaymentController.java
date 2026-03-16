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
                    String classIdStr = request.getParameter("classId");
                    int classId = Integer.parseInt(classIdStr);

                    dao.EnrollmentDAO enrollmentDAO = new dao.EnrollmentDAO();
                    String currentStatus = enrollmentDAO.checkEnrollmentStatus(currentUser.getUserId(), classId);

                    if ("Active".equals(currentStatus)) {
                        session.setAttribute("message", "You are already enrolled in this class.");
                        session.setAttribute("messageType", "error");
                        response.sendRedirect("class?action=availableClass");
                        return;
                    } else if ("Unpaid".equals(currentStatus)) {
                        session.setAttribute("message", "You already have a pending payment for this class. Please wait for admin approval.");
                        session.setAttribute("messageType", "error");
                        response.sendRedirect("class?action=availableClass");
                        return;
                    }
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

                            boolean hasUsed = voucherDAO.hasUserUsedVoucher(currentUser.getUserId(), voucher.getVoucherId());

                            if (hasUsed) {
                                voucherMessage = "You have already used or are currently applying this voucher for another class!";
                                request.setAttribute("voucherType", "error");
                            } else {
                                if (voucher.getDiscountAmount() != null && voucher.getDiscountAmount().doubleValue() > 0) {
                                    discountAmount = voucher.getDiscountAmount().doubleValue();
                                } else if (voucher.getDiscountPercent() > 0) {
                                    discountAmount = originalAmount * (voucher.getDiscountPercent() / 100.0);
                                }
                                voucherMessage = "Discount code applied successfully!";
                                voucherId = voucher.getVoucherId();
                                request.setAttribute("voucherType", "success");
                            }
                            // ------------------------------
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

                    String voucherIdStr = request.getParameter("voucherId");
                    Integer voucherId = null;
                    if (voucherIdStr != null && !voucherIdStr.isEmpty()) {
                        voucherId = Integer.parseInt(voucherIdStr);
                    }

                    // --- TRUYỀN THÊM voucherId VÀO HÀM DAO ---
                    boolean isSuccess = paymentDAO.confirmQRPayment(enrollmentId, amount, voucherId);

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
                    String className = request.getParameter("className");
                    String voucherCode = request.getParameter("voucherCode");

                    HttpSession sessionCheckout = request.getSession();
                    User currentU = (User) sessionCheckout.getAttribute("user");

                    dao.EnrollmentDAO enrollmentDAO = new dao.EnrollmentDAO();
                    dao.ClassDAO clsDAO = new dao.ClassDAO();
                    dao.VoucherDAO vchDAO = new dao.VoucherDAO();

                    String currentStatus = enrollmentDAO.checkEnrollmentStatus(currentU.getUserId(), classId);
                    if ("Active".equals(currentStatus)) {
                        sessionCheckout.setAttribute("message", "You have already paid and are enrolled in this class.");
                        sessionCheckout.setAttribute("messageType", "error");
                        response.sendRedirect("class?action=myclasses");
                        return;
                    } else if ("Unpaid".equals(currentStatus)) {
                        sessionCheckout.setAttribute("message", "You have a pending invoice for this class. Please complete the payment");
                        sessionCheckout.setAttribute("messageType", "error");
                        response.sendRedirect("class?action=availableClass");
                        return;
                    }

                    int enrollmentId = enrollmentDAO.getOrCreateEnrollment(currentU.getUserId(), classId);

                    double originalPrice = clsDAO.getClassPrice(classId);
                    double discountAmount = 0;

                    if (voucherCode != null && !voucherCode.isEmpty()) {
                        Voucher voucher = vchDAO.getVoucherByCode(voucherCode);
                        if (voucher != null && voucher.isStatus()) {
                            boolean hasUsed = vchDAO.hasUserUsedVoucher(currentU.getUserId(), voucher.getVoucherId());
                            if (!hasUsed) {
                                // DÙNG HÀM MỚI Ở ĐÂY
                                discountAmount = vchDAO.calculateDiscountAmount(voucher, originalPrice);
                                enrollmentDAO.updateEnrollmentVoucher(enrollmentId, voucher.getVoucherId());
                                request.setAttribute("voucherId", voucher.getVoucherId());
                            } else {
                                sessionCheckout.setAttribute("message", "This discount code has already been used.");
                                sessionCheckout.setAttribute("messageType", "error");
                            }
                        }
                    }

                    double serverFinalAmount = originalPrice - discountAmount;
                    if (serverFinalAmount < 0) {
                        serverFinalAmount = 0;
                    }

                    String rawAddInfo = "LMCS " + enrollmentId + " " + className;
                    long amountToPay = (long) serverFinalAmount;

                    String qrUrl = paymentDAO.generateVietQRUrl(amountToPay, rawAddInfo);

                    request.setAttribute("qrUrl", qrUrl);
                    request.setAttribute("amount", amountToPay);
                    request.setAttribute("addInfo", rawAddInfo);
                    request.setAttribute("enrollmentId", enrollmentId);
                    request.setAttribute("className", className);

                    request.getRequestDispatcher("payment.jsp").forward(request, response);

                } catch (Exception e) {
                    System.out.println("Fail to checkout: " + e.getMessage());
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
        } // Get statistics
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
     * Handle approving payment and activating enrollment
     */
    private void handleApprovePayment(HttpServletRequest request, HttpServletResponse response,
            PaymentDAO paymentDAO) throws ServletException, IOException {

        String paymentIdParam = request.getParameter("paymentId");
        if (paymentIdParam == null || paymentIdParam.isEmpty()) {
            response.sendRedirect("payment?action=list");
            return;
        }

        try {
            int paymentId = Integer.parseInt(paymentIdParam);

            Payment payment = paymentDAO.getPaymentById(paymentId);

            if (payment != null) {
                boolean isPaymentApproved = paymentDAO.updatePaymentStatus(paymentId, "Approved");

                if (isPaymentApproved) {
                    int enrollmentId = payment.getEnrollment().getEnrollmentId();
                    dao.EnrollmentDAO enrollmentDAO = new dao.EnrollmentDAO();
                    enrollmentDAO.updateEnrollmentStatus(enrollmentId, "Active");

                    request.getSession().setAttribute("message", "Đã duyệt thanh toán & Cập nhật học viên vào lớp thành công!");
                    request.getSession().setAttribute("messageType", "success");
                } else {
                    request.getSession().setAttribute("message", "Lỗi: Không thể duyệt hóa đơn này.");
                    request.getSession().setAttribute("messageType", "error");
                }
            } else {
                request.getSession().setAttribute("message", "Không tìm thấy hóa đơn.");
                request.getSession().setAttribute("messageType", "error");
            }

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("message", "ID Hóa đơn không hợp lệ.");
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
