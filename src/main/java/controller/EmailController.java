/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

    public static String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%";
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public static boolean sendEmail(String toEmail, String fullName, String newPassword) {
        final String fromEmail = "binhce200008@gmail.com";
        final String appPassword = "cjrwaydcrdpovelz";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, appPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Your Password Has Been Reset - LMCS System");

            String content = "Hi " + fullName + ",\n\n"
                    + "Your password has been successfully reset by the System Admin.\n\n"
                    + "Your new temporary password is: " + newPassword + "\n\n"
                    + "Please log in and change your password immediately to ensure your account security.\n\n"
                    + "Best regards,\nLMCS Support Team";
            message.setText(content);

            Transport.send(message);
            return true;
        } catch (Exception e) {
            System.out.println("Fail to send Email: " + e.getMessage());
            return false;
        }
    }
}
