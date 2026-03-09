<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
    <head>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link href="css/payment.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>

        <div class="container py-5">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h3 class="text-navy fw-bold m-0"><i class="fa-solid fa-graduation-cap me-2"></i>Language Center</h3>
                <div class="stepper m-0">
                    <div class="step completed">
                        <div class="step-icon"><i class="fa-solid fa-check"></i></div> <span>Review</span>
                    </div>
                    <div class="step-line completed"></div>
                    <div class="step active">
                        <div class="step-icon">2</div> <span>Payment</span>
                    </div>
                    <div class="step-line"></div>
                    <div class="step pending">
                        <div class="step-icon">3</div> <span>Success</span>
                    </div>
                </div>
            </div>

            <div class="row g-4">
                <div class="col-lg-7">
                    <div class="card payment-card h-100 p-4 p-md-5">
                        <h4 class="text-navy fw-bold mb-4">Scan to Pay</h4>

                        <div class="timer-box d-flex align-items-center mb-4">
                            <i class="fa-regular fa-clock me-2"></i> 
                            <span>Awaiting payment... QR expires in <strong id="countdown">15:00</strong></span>
                        </div>

                        <div class="text-center mb-5">
                            <div class="qr-wrapper shadow-sm">
                                <img src="${qrUrl}" alt="VietQR Code" class="qr-image"/>
                            </div>
                        </div>

                        <h6 class="text-muted fw-bold text-uppercase mb-3" style="font-size: 0.85rem;">Manual Transfer Details</h6>

                        <div class="copy-row">
                            <div>
                                <div class="small text-muted mb-1">Bank Name</div>
                                <div class="fw-bold">MB Bank</div>
                            </div>
                        </div>

                        <div class="copy-row">
                            <div>
                                <div class="small text-muted mb-1">Account Name</div>
                                <div class="fw-bold">LANGUAGE CENTER LLC</div>
                            </div>
                        </div>

                        <div class="copy-row">
                            <div>
                                <div class="small text-muted mb-1">Account Number</div>
                                <div class="fw-bold fs-5" id="accNumber">1903 4567 890</div>
                            </div>
                            <button class="copy-btn" onclick="copyText('accNumber')" title="Copy Account Number">
                                <i class="fa-regular fa-copy fs-4"></i>
                            </button>
                        </div>

                        <div class="copy-row">
                            <div>
                                <div class="small text-muted mb-1">Transfer Content</div>
                                <div class="fw-bold fs-5" id="transferContent">${addInfo}</div>
                            </div>
                            <button class="copy-btn" onclick="copyText('transferContent')" title="Copy Content">
                                <i class="fa-regular fa-copy fs-4"></i>
                            </button>
                        </div>

                        <div class="copy-row border-primary bg-white shadow-sm">
                            <div>
                                <div class="small text-muted mb-1">Amount</div>
                                <div class="fw-bold fs-5 text-danger" id="transferAmount"><fmt:formatNumber value="${amount}" type="number"/> VNĐ</div>
                            </div>
                            <button class="copy-btn" onclick="copyText('transferAmount')" title="Copy Amount">
                                <i class="fa-regular fa-copy fs-4"></i>
                            </button>
                        </div>

                        <div class="quick-tip mt-4">
                            <i class="fa-solid fa-lightbulb text-warning me-2"></i>
                            <strong>Quick Tip:</strong> Open your banking app and scan the QR code above. The transfer content and amount will be filled automatically.
                        </div>
                    </div>
                </div>

                <div class="col-lg-5">
                    <div class="d-flex flex-column h-100">
                        <div class="card payment-card p-4 p-md-5 mb-4 border border-light shadow-sm">
                            <h5 class="text-navy fw-bold mb-4">Order Summary</h5>

                            <div class="d-flex justify-content-between mb-3">
                                <span class="text-muted">Class Name:</span>
                                <span class="fw-semibold text-end text-primary">${className}</span>
                            </div>

                            <div class="d-flex justify-content-between mb-3">
                                <span class="text-muted">Student Name:</span>
                                <span class="fw-semibold text-end">${sessionScope.user.fullName}</span>
                            </div>

                            <div class="d-flex justify-content-between mb-4">
                                <span class="text-muted">Date:</span>
                                <span class="fw-semibold">
                                    <fmt:setLocale value="en_US" />
                                    <jsp:useBean id="now" class="java.util.Date"/>
                                    <fmt:formatDate value="${now}" pattern="MMMM dd, yyyy"/>
                                </span>
                            </div>

                            <hr class="mb-4">

                            <div class="d-flex justify-content-between align-items-center mb-4">
                                <span class="fw-bold fs-5">Grand Total:</span>
                                <span class="text-primary fw-bold fs-2"><fmt:formatNumber value="${amount}" type="number"/> VNĐ</span>
                            </div>

                            <div class="text-center mb-4">
                                <span class="badge rounded-pill border border-warning text-warning px-4 py-2" style="background: #fffbeb;">
                                    <i class="fa-solid fa-circle-notch fa-spin me-2"></i> Pending Payment
                                </span>
                            </div>

                            <div class="mt-2">
                                <form action="payment" method="post" class="mb-3">
                                    <input type="hidden" name="action" value="confirmPayment">
                                    <input type="hidden" name="enrollmentId" value="${enrollmentId}">
                                    <input type="hidden" name="amount" value="${amount}">
                                    
                                    <input type="hidden" name="voucherId" value="${voucherId}">
                                    

                                    <button type="submit" class="btn btn-navy btn-lg w-100 py-3 fw-bold shadow-sm">
                                        <i class="fa-solid fa-circle-check me-2"></i> I Have Completed the Transfer
                                    </button>
                                </form>

                                <a href="#" onclick="history.back(); return false;" class="btn btn-light btn-lg w-100 py-3 text-muted border">
                                    <i class="fa-solid fa-xmark me-2"></i> Cancel & Choose Another Method
                                </a>
                            </div>

                        </div>
                    </div>
                </div>            
            </div>

            <div class="text-center mt-5 trust-badges d-flex justify-content-center align-items-center flex-wrap gap-4">
                <div class="d-flex align-items-center"><i class="fa-solid fa-lock trust-icon text-success"></i> <span><strong>SSL Secure</strong><br>256-bit encryption</span></div>
                <div class="d-flex align-items-center"><i class="fa-solid fa-shield-halved trust-icon text-primary"></i> <span><strong>PCI-DSS</strong><br>Compliant</span></div>
                <div class="d-flex align-items-center"><i class="fa-regular fa-circle-check trust-icon text-success"></i> <span><strong>Verified Secure</strong><br>Trusted platform</span></div>
                <div class="border-start ps-4 ms-2 d-none d-md-block">
                    <span class="badge bg-light text-dark border p-2 me-2">Techcombank</span>
                    <span class="badge bg-light text-dark border p-2">VietQR</span>
                </div>
            </div>
            <div class="text-center text-muted small mt-4">
                &copy; 2026 Language Center LLC. All transactions are secure and encrypted.
            </div>
        </div>
        <script src="js/payment.js" type="text/javascript"></script>
    </body>
</html>