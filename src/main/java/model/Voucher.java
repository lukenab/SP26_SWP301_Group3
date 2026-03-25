/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author phuct
 */
public class Voucher {
    private int voucherId;
    private String code;
    private BigDecimal discountAmount;
    private double discountPercent;
    private Date validUntil;
    private boolean status;
    private boolean landingPopup;
    private Integer maxUsage;
    private int usedCount;
    private int remainingCount;

    public Voucher() {
    }

    public Voucher(int voucherId, String code, BigDecimal discountAmount, double discountPercent, Date validUntil, boolean status, Integer maxUsage) {
        this.voucherId = voucherId;
        this.code = code;
        this.discountAmount = discountAmount;
        this.discountPercent = discountPercent;
        this.validUntil = validUntil;
        this.status = status;
        this.maxUsage = maxUsage;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isLandingPopup() {
        return landingPopup;
    }

    public void setLandingPopup(boolean landingPopup) {
        this.landingPopup = landingPopup;
    }

    public Integer getMaxUsage() {
        return maxUsage;
    }

    public void setMaxUsage(Integer maxUsage) {
        this.maxUsage = maxUsage;
    }

    public int getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(int usedCount) {
        this.usedCount = usedCount;
    }

    public int getRemainingCount() {
        return remainingCount;
    }

    public void setRemainingCount(int remainingCount) {
        this.remainingCount = remainingCount;
    }

    public int getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(int voucherId) {
        this.voucherId = voucherId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }

    @Override
    public String toString() {
        return "Voucher{" + "voucherId=" + voucherId + ", code=" + code + ", discountAmount=" + discountAmount + ", discountPercent=" + discountPercent + ", validUntil=" + validUntil + ", status=" + status + ", landingPopup=" + landingPopup + ", maxUsage=" + maxUsage + ", usedCount=" + usedCount + ", remainingCount=" + remainingCount + '}';
    }
    
}
