/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.PaymentDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

/**
 *
 * @author Legion
 */
@WebServlet(name = "PaymentController", urlPatterns = {"/payment"})
public class PaymentController extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            action = "checkout";
        }

        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        switch (action) {
            case "checkout":
                try {
                    String enrollmentIdStr = request.getParameter("enrollmentId");
                    String amountStr = request.getParameter("amount");
                    String className = request.getParameter("className");

                    // Gán giá trị mặc định nếu không có truyền sang (Dùng để test)
                    int enrollmentId = (enrollmentIdStr != null && !enrollmentIdStr.isEmpty()) ? Integer.parseInt(enrollmentIdStr) : 1;
                    double amount = (amountStr != null && !amountStr.isEmpty()) ? Double.parseDouble(amountStr) : 10000.0;
                    if (className == null || className.isEmpty()) {
                        className = "IELTS_K18";
                    }

                    // 1. Cấu hình tài khoản ngân hàng của Trung tâm
                    String bankId = "MB"; // Mã ngân hàng (vietcombank, mb, tpbank...)
                    String accountNo = "0907625043"; // ĐỔI THÀNH SỐ TÀI KHOẢN CỦA BẠN ĐỂ TEST
                    String accountName = "TRUNG TAM NGOAI NGU LMCS";

                    // 2. Tạo nội dung chuyển khoản độc nhất (Ví dụ: LMCS 1 IELTS_K18)
                    String rawAddInfo = "LMCS " + enrollmentId + " " + className;
                    String addInfo = rawAddInfo.replaceAll(" ", "%20"); // Thay dấu cách bằng %20 cho URL
                    String urlAccountName = accountName.replaceAll(" ", "%20");

                    // Format số tiền để bỏ phần thập phân .0 nếu có (VietQR yêu cầu số nguyên)
                    long finalAmount = (long) amount;

                    // 3. Tạo link API VietQR
                    String qrUrl = "https://img.vietqr.io/image/" + bankId + "-" + accountNo + "-compact2.png"
                            + "?amount=" + finalAmount
                            + "&addInfo=" + addInfo
                            + "&accountName=" + urlAccountName;

                    // 4. Đẩy dữ liệu sang trang payment.jsp
                    request.setAttribute("qrUrl", qrUrl);
                    request.setAttribute("amount", finalAmount);
                    request.setAttribute("addInfo", rawAddInfo);
                    request.setAttribute("enrollmentId", enrollmentId);

                    request.getRequestDispatcher("payment.jsp").forward(request, response);

                } catch (Exception e) {
                    System.out.println("PaymentController doGet Error: " + e.getMessage());
                    session.setAttribute("message", "Lỗi tải trang thanh toán!");
                    session.setAttribute("messageType", "error");
                    response.sendRedirect("student?action=dashboard");
                }
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "processPayment":
                int enrollmentId = Integer.parseInt(request.getParameter("id"));
                double amount = Double.parseDouble(request.getParameter("amount"));

                PaymentDAO paymentDAO = new PaymentDAO();
                boolean isSuccess = paymentDAO.processQrPayment(enrollmentId, amount);

                HttpSession session = request.getSession();
                if (isSuccess) {
                    session.setAttribute("message", "Confirmation successfully! Please wait while the center verifies the transactions.");
                    session.setAttribute("messageType", "success");
                } else {
                    session.setAttribute("message", "An error occured, please try again!");
                    session.setAttribute("messageType", "error");
                }
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
