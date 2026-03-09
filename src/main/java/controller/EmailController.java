package controller;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

/**
 *
 * @author Legion
 */
public class EmailController {

    // =========================================================================
    // 1. CÁC HÀM TẠO CHUỖI NGẪU NHIÊN (PASSWORD & OTP)
    // =========================================================================

    public static String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%";
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    // Tạo mã OTP 6 số ngẫu nhiên (Vd: 058291)
    public static String generateOTP() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number); 
    }

    // =========================================================================
    // 2. CÁC HÀM SOẠN NỘI DUNG EMAIL 
    // =========================================================================

    // Dùng khi Admin chủ động reset pass của người dùng
    public static boolean sendEmail(String toEmail, String fullName, String newPassword) {
        String subject = "Your Password Has Been Reset - LMCS System";
        String content = "Hi " + fullName + ",\n\n"
                + "Your password has been successfully reset by the System Admin.\n\n"
                + "Your new temporary password is: " + newPassword + "\n\n"
                + "Please log in and change your password immediately to ensure your account security.\n\n"
                + "Best regards,\nLMCS Support Team";
        return sendPlainTextEmail(toEmail, subject, content);
    }

    // Dùng khi chuyển đổi Lead thành Student
    public static boolean sendLeadConversionEmail(String toEmail, String fullName, String defaultPassword) {
        String subject = "Your Student Account Has Been Created - LMCS System";
        String content = "Hi " + fullName + ",\n\n"
                + "Your lead profile has been converted to a student account.\n\n"
                + "Login email: " + toEmail + "\n"
                + "Default password: " + defaultPassword + "\n\n"
                + "Please log in and change your password as soon as possible.\n\n"
                + "Best regards,\nLMCS Support Team";
        return sendPlainTextEmail(toEmail, subject, content);
    }

    // Dùng cho chức năng Quên Mật Khẩu (Gửi OTP)
    public static boolean sendOTPEmail(String toEmail, String fullName, String otp) {
        String subject = "OTP for Password Reset - LMCS System";
        String content = "Hi " + fullName + ",\n\n"
                + "We received a request to reset your password.\n\n"
                + "Your OTP code is: " + otp + "\n\n"
                + "This code is valid for 5 minutes. Please do not share this code with anyone.\n\n"
                + "If you did not request a password reset, please ignore this email and your password will remain unchanged.\n\n"
                + "Best regards,\nLMCS Support Team";
        return sendPlainTextEmail(toEmail, subject, content);
    }

    // =========================================================================
    // 3. HÀM GIAO TIẾP VỚI MÁY CHỦ SMTP GMAIL (DÙNG CHUNG)
    // =========================================================================

    private static boolean sendPlainTextEmail(String toEmail, String subject, String content) {
        // Cấu hình tài khoản gửi mail
        final String fromEmail = "binhce200008@gmail.com";
        final String appPassword = "cjrwaydcrdpovelz";

        // Thiết lập properties cho SMTP của Gmail
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Đăng nhập vào Email người gửi
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, appPassword);
            }
        });

        try {
            // Khởi tạo thư
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(content);

            // Gửi thư đi
            Transport.send(message);
            return true;
        } catch (Exception e) {
            System.out.println("Fail to send Email: " + e.getMessage());
            return false;
        }
    }
}