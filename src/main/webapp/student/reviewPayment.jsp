<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
<link href="css/processPayment.css" rel="stylesheet" type="text/css"/>

<div class="review-container">
    <div class="container">
        <div class="row">
            <div class="col-lg-8">
                <h2 class="fw-bold mb-4">Confirm Registration & Payment</h2>

                <div class="card shadow-sm">
                    <div class="card-body p-4">
                        <h5 class="card-title mb-4">Course Details</h5>
                        <div class="course-name">${className}</div>
                        
                        <div class="info-row">
                            <i class="fa-regular fa-calendar-days"></i>
                            <span><strong>Start Date:</strong> Oct 15, 2023 (Sample)</span>
                        </div>
                        <div class="info-row">
                            <i class="fa-regular fa-clock"></i>
                            <span><strong>Schedule:</strong> Mon-Wed-Fri (19:30 - 21:00)</span>
                        </div>
                        
                        <div class="d-flex justify-content-between mt-4 pt-3 border-top">
                            <span class="text-muted">Original Price:</span>
                            <span class="fw-bold fs-5"><fmt:formatNumber value="${originalAmount}" type="number"/> VNĐ</span>
                        </div>
                    </div>
                </div>

                <div class="card shadow-sm">
                    <div class="card-body p-4">
                        <h5 class="card-title mb-3">Promo Code / Voucher</h5>
                        <form action="payment" method="GET" class="d-flex gap-2">
                            <input type="hidden" name="action" value="review">
                            <input type="hidden" name="classId" value="${classId}">
                            <input type="hidden" name="className" value="${className}">
                            <input type="hidden" name="amount" value="${originalAmount}">
                            
                            <input type="text" name="voucherCode" class="form-control" placeholder="Enter your code here..." value="${voucherCode}">
                            <button type="submit" class="btn btn-outline-primary px-4">Apply</button>
                        </form>
                        
                        <c:if test="${not empty voucherMessage}">
                            <div class="mt-2 ${voucherType == 'error' ? 'text-danger' : 'text-success'} small">
                                <i class="fa-solid ${voucherType == 'error' ? 'fa-circle-xmark' : 'fa-circle-check'}"></i> ${voucherMessage}
                            </div>
                        </c:if>
                    </div>
                </div>

                <div class="card shadow-sm">
                    <div class="card-body p-4">
                        <h5 class="card-title mb-3">Select Payment Method</h5>
                        <div class="method-box active">
                            <input type="radio" checked class="me-3">
                            <i class="fa-solid fa-qrcode me-2 text-primary"></i>
                            <div>
                                <strong>Instant Transfer via VietQR</strong>
                                <div class="small text-muted">Scan QR code for automatic verification</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-lg-4">
                <div class="card shadow-sm summary-card">
                    <div class="card-body p-4">
                        <h5 class="card-title mb-4">Total Summary</h5>
                        
                        <div class="d-flex justify-content-between mb-3">
                            <span class="text-muted">Base Tuition Fee:</span>
                            <span><fmt:formatNumber value="${originalAmount}" type="number"/> VNĐ</span>
                        </div>
                        
                        <div class="d-flex justify-content-between mb-3 text-success">
                            <span>Voucher Discount:</span>
                            <span>-<fmt:formatNumber value="${discountAmount}" type="number"/> VNĐ</span>
                        </div>
                        
                        <hr class="my-4">
                        
                        <div class="d-flex justify-content-between align-items-center mb-4">
                            <span class="fw-bold fs-5">Grand Total:</span>
                            <span class="text-danger fw-bold fs-3">
                                <fmt:formatNumber value="${finalAmount}" type="number"/> VNĐ
                            </span>
                        </div>

                        <form action="payment" method="POST">
                            <input type="hidden" name="action" value="checkout">
                            <input type="hidden" name="classId" value="${classId}">
                            <input type="hidden" name="className" value="${className}">
                            <input type="hidden" name="finalAmount" value="${finalAmount}">
                            <input type="hidden" name="voucherCode" value="${voucherCode}">
                            
                            <button type="submit" class="btn btn-primary btn-pay w-100 fs-5 text-uppercase">
                                Confirm & Pay Now <i class="fa-solid fa-arrow-right ms-2"></i>
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>