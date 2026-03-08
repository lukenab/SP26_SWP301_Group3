package controller;

import dao.PaymentDAO;
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
        PaymentDAO paymentDAO = new PaymentDAO();
        ClassDAO classDAO = new ClassDAO();
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "list":
                // List all payments with filters
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

                List<Object[]> paymentList;
                if (courseId != null || classId != null || (status != null && !status.isEmpty())) {
                    paymentList = paymentDAO.getFilteredPaymentsDisplay(courseId, classId, status);
                } else {
                    paymentList = paymentDAO.getAllPaymentsDisplay();
                }

                int totalPayments = paymentDAO.getPaymentCountByStatus(null);
                int pendingPayments = paymentDAO.getPaymentCountByStatus("Pending");
                int approvedPayments = paymentDAO.getPaymentCountByStatus("Approved");
                int rejectedPayments = paymentDAO.getPaymentCountByStatus("Rejected");
                BigDecimal totalAmount = paymentDAO.getTotalAmountByStatus(null);
                BigDecimal approvedAmount = paymentDAO.getTotalAmountByStatus("Approved");

                List<Object[]> courseOptions = classDAO.getActiveCoursesForClassForm();
                List<Object[]> classList = classDAO.getClassManagementList();

                request.setAttribute("paymentList", paymentList);
                request.setAttribute("totalPayments", totalPayments);
                request.setAttribute("pendingPayments", pendingPayments);
                request.setAttribute("approvedPayments", approvedPayments);
                request.setAttribute("rejectedPayments", rejectedPayments);
                request.setAttribute("totalAmount", totalAmount);
                request.setAttribute("approvedAmount", approvedAmount);
                request.setAttribute("courseOptions", courseOptions);
                request.setAttribute("classList", classList);
                request.setAttribute("selectedCourseId", courseIdParam);
                request.setAttribute("selectedClassId", classIdParam);
                request.setAttribute("selectedStatus", status);
                request.setAttribute("home_view", "/academic/payment_list.jsp");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;

            case "view":
                // View payment detail
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
                break;

            default:
                response.sendRedirect("payment?action=list");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PaymentDAO paymentDAO = new PaymentDAO();
        String action = request.getParameter("action");

        switch (action != null ? action : "") {
            case "approve":
                // Approve payment
                String paymentIdParam = request.getParameter("paymentId");

                if (paymentIdParam == null || paymentIdParam.isEmpty()) {
                    response.sendRedirect("payment?action=list");
                    return;
                }

                try {
                    int paymentId = Integer.parseInt(paymentIdParam);
                    boolean success = paymentDAO.approvePayment(paymentId);

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
                break;

            case "reject":
                // Reject payment
                String paymentIdParam2 = request.getParameter("paymentId");

                if (paymentIdParam2 == null || paymentIdParam2.isEmpty()) {
                    response.sendRedirect("payment?action=list");
                    return;
                }

                try {
                    int paymentId = Integer.parseInt(paymentIdParam2);
                    boolean success = paymentDAO.rejectPayment(paymentId);

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
                break;

            default:
                response.sendRedirect("payment?action=list");
                break;
        }
    }
}

