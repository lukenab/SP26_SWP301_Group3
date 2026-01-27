/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;


import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author phuct
 */
public class Payment {
    private int paymentId;
    private Enrollment enrollment;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private String paymentMethod;
    private String evidenceImage;
    private String status;
    private Voucher voucher;

    public Payment() {
    }

    public Payment(int paymentId, Enrollment enrollment, BigDecimal amount, LocalDateTime paymentDate, String paymentMethod, String evidenceImage, String status, Voucher voucher) {
        this.paymentId = paymentId;
        this.enrollment = enrollment;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.evidenceImage = evidenceImage;
        this.status = status;
        this.voucher = voucher;
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public Enrollment getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getEvidenceImage() {
        return evidenceImage;
    }

    public void setEvidenceImage(String evidenceImage) {
        this.evidenceImage = evidenceImage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Payment{" + "paymentId=" + paymentId + ", enrollment=" + enrollment + ", amount=" + amount + ", paymentDate=" + paymentDate + ", paymentMethod=" + paymentMethod + ", evidenceImage=" + evidenceImage + ", status=" + status + ", voucher=" + voucher + '}';
    }
}
