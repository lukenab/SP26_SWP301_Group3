<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link href="css/manageUser.css" rel="stylesheet" type="text/css"/>
<link href="css/payment_list.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4 content-body">

    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="dashboard">Dashboard</a></li>
                <li class="breadcrumb-item active" aria-current="page">Payment Management</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title"><i class='bx bx-dollar-circle'></i> Payment Management</h2>
                <p class="text-muted small mb-0">View, approve, and manage student payment requests for academic programs</p>
            </div>
        </div>
    </div>

    <!-- Statistics Cards -->
    <div class="stat-card-grid">
        <div class="stat-card">
            <div class="stat-info">
                <p>Total Payments</p>
                <h3>${totalPayments}</h3>
            </div>
            <div class="icon-wrapper blue">
                <i class='bx bx-receipt'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Pending</p>
                <h3>${pendingPayments}</h3>
            </div>
            <div class="icon-wrapper" style="background: #fef3c7; color: #d97706;">
                <i class='bx bx-time-five'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Approved</p>
                <h3>${approvedPayments}</h3>
            </div>
            <div class="icon-wrapper green">
                <i class='bx bx-check-circle'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Total Amount</p>
                <h3><fmt:formatNumber value="${totalAmount}" type="currency" currencySymbol="$" /></h3>
            </div>
            <div class="icon-wrapper" style="background: #dbeafe; color: #2563eb;">
                <i class='bx bx-dollar'></i>
            </div>
        </div>
    </div>

    <!-- Toast Message -->
    <c:if test="${not empty sessionScope.message}">
        <div class="custom-toast toast-${sessionScope.messageType}" id="toastMessage">
            <div class="toast-icon">
                <c:choose>
                    <c:when test="${sessionScope.messageType == 'success'}">
                        <i class='bx bx-check-circle'></i>
                    </c:when>
                    <c:otherwise>
                        <i class='bx bx-error-circle'></i>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="toast-content">
                <span class="toast-title">
                    ${sessionScope.messageType == 'success' ? 'Success!' : 'Error!'}
                </span>
                <span class="toast-message">${sessionScope.message}</span>
            </div>
            <button class="toast-close" onclick="closeToast()">
                <i class='bx bx-x'></i>
            </button>
        </div>

        <c:remove var="message" scope="session" />
        <c:remove var="messageType" scope="session" />
    </c:if>

    <!-- Filter Section -->
    <form action="payment" method="GET" class="filter-container flex-wrap">
        <input type="hidden" name="action" value="list">

        <div class="d-flex gap-3 flex-wrap w-100">
            <select name="courseId" class="custom-select-filter" onchange="this.form.submit()"
                    style="border: 1px solid #e2e8f0; padding: 8px 16px; border-radius: 8px; background: white; outline: none; cursor: pointer; min-width: 200px;">
                <option value="0">All Courses</option>
                <c:forEach items="${courseOptions}" var="course">
                    <option value="${course[0]}" ${selectedCourseId == course[0] ? 'selected' : ''}>
                        ${course[1]}
                    </option>
                </c:forEach>
            </select>

            <select name="classId" class="custom-select-filter" onchange="this.form.submit()"
                    style="border: 1px solid #e2e8f0; padding: 8px 16px; border-radius: 8px; background: white; outline: none; cursor: pointer; min-width: 200px;">
                <option value="0">All Classes</option>
                <c:forEach items="${classList}" var="cls">
                    <option value="${cls[0]}" ${selectedClassId == cls[0] ? 'selected' : ''}>
                        ${cls[1]} - ${cls[2]}
                    </option>
                </c:forEach>
            </select>

            <select name="status" class="custom-select-filter" onchange="this.form.submit()"
                    style="border: 1px solid #e2e8f0; padding: 8px 16px; border-radius: 8px; background: white; outline: none; cursor: pointer; min-width: 200px;">
                <option value="">All Statuses</option>
                <option value="Pending" ${selectedStatus == 'Pending' ? 'selected' : ''}>Pending</option>
                <option value="Approved" ${selectedStatus == 'Approved' ? 'selected' : ''}>Approved</option>
                <option value="Rejected" ${selectedStatus == 'Rejected' ? 'selected' : ''}>Rejected</option>
            </select>

            <button type="submit" style="display: none;"></button>
        </div>
    </form>

    <!-- Payment Table -->
    <div class="card user-table-card border-0 bg-white">
        <div class="table-responsive">
            <table class="table mb-0 align-middle">
                <thead>
                    <tr>
                        <th style="width: 30px">#</th>
                        <th>Student Name</th>
                        <th>Course</th>
                        <th>Class Code</th>
                        <th>Payment Date</th>
                        <th>Amount</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty paymentList}">
                            <tr>
                                <td colspan="8" class="text-center text-muted py-4">
                                    <i class='bx bx-info-circle' style='font-size: 2rem;'></i>
                                    <p class="mb-0 mt-2">No payments found</p>
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${paymentList}" var="paymentDisplay" varStatus="loop">
                                <tr>
                                    <td>${loop.count}</td>
                                    <td>
                                        <div class="d-flex flex-column">
                                            <span class="user-name">${paymentDisplay.studentName}</span>
                                            <span class="user-email text-muted small">${paymentDisplay.studentEmail}</span>
                                        </div>
                                    </td>
                                    <td>${paymentDisplay.payment.enrollment.classes.course.courseName}</td>
                                    <td><span class="badge badge-student">${paymentDisplay.payment.enrollment.classes.className}</span></td>
                                    <td>
                                        <c:set var="paymentDateTime" value="${paymentDisplay.payment.paymentDate}" />
                                        <c:if test="${paymentDateTime != null}">
                                            ${paymentDateTime.toString().replace('T', ' ').substring(0, 16)}
                                        </c:if>
                                    </td>
                                    <td>
                                        <strong style="color: #2563eb;">
                                            <fmt:formatNumber value="${paymentDisplay.payment.amount}" type="currency" currencySymbol="$" />
                                        </strong>
                                        <c:if test="${paymentDisplay.payment.voucher != null}">
                                            <br><small class="text-success"><i class='bx bx-purchase-tag'></i> ${paymentDisplay.payment.voucher.code}</small>
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${paymentDisplay.payment.status == 'Pending'}">
                                                <span class="badge" style="background: #fef3c7; color: #d97706; padding: 6px 12px; border-radius: 6px; font-weight: 500;">
                                                    <i class='bx bx-time-five'></i> Pending
                                                </span>
                                            </c:when>
                                            <c:when test="${paymentDisplay.payment.status == 'Approved'}">
                                                <span class="badge" style="background: #dcfce7; color: #16a34a; padding: 6px 12px; border-radius: 6px; font-weight: 500;">
                                                    <i class='bx bx-check-circle'></i> Approved
                                                </span>
                                            </c:when>
                                            <c:when test="${paymentDisplay.payment.status == 'Rejected'}">
                                                <span class="badge" style="background: #fee2e2; color: #dc2626; padding: 6px 12px; border-radius: 6px; font-weight: 500;">
                                                    <i class='bx bx-x-circle'></i> Rejected
                                                </span>
                                            </c:when>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:if test="${paymentDisplay.payment.evidenceImage != null && !paymentDisplay.payment.evidenceImage.isEmpty()}">
                                            <button type="button" class="action-btn" title="View Evidence"
                                                    onclick="viewEvidence('${paymentDisplay.payment.evidenceImage}', '${paymentDisplay.studentName}')">
                                                <i class='bx bx-image'></i>
                                            </button>
                                        </c:if>
                                        <c:if test="${paymentDisplay.payment.status == 'Pending'}">
                                            <button type="button" class="action-btn" title="Approve"
                                                    onclick="approvePayment(${paymentDisplay.payment.paymentId}, '${paymentDisplay.studentName}')">
                                                <i class='bx bx-check' style='color: #16a34a;'></i>
                                            </button>
                                            <button type="button" class="action-btn delete" title="Reject"
                                                    onclick="rejectPayment(${paymentDisplay.payment.paymentId}, '${paymentDisplay.studentName}')">
                                                <i class='bx bx-x'></i>
                                            </button>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- Evidence Modal -->
<div class="modal fade" id="evidenceModal" tabindex="-1" aria-labelledby="evidenceModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="evidenceModalLabel">
                    <i class='bx bx-image'></i> Payment Evidence
                </h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body text-center">
                <p class="mb-3"><strong id="evidenceStudentName"></strong></p>
                <img id="evidenceImage" src="" alt="Payment Evidence" style="max-width: 100%; height: auto; border-radius: 8px;">
            </div>
        </div>
    </div>
</div>

<!-- Approve Modal -->
<div class="modal fade" id="approveModal" tabindex="-1" aria-labelledby="approveModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="approveModalLabel">
                    <i class='bx bx-check-circle' style='color: #16a34a;'></i> Confirm Approval
                </h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form action="payment?action=approve" method="POST">
                <div class="modal-body">
                    <p>Are you sure you want to approve payment for <strong id="approveStudentName"></strong>?</p>
                    <input type="hidden" name="paymentId" id="approvePaymentId">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-success">Approve Payment</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Reject Modal -->
<div class="modal fade" id="rejectModal" tabindex="-1" aria-labelledby="rejectModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="rejectModalLabel">
                    <i class='bx bx-x-circle' style='color: #dc2626;'></i> Confirm Rejection
                </h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form action="payment?action=reject" method="POST">
                <div class="modal-body">
                    <p>Are you sure you want to reject payment for <strong id="rejectStudentName"></strong>?</p>
                    <input type="hidden" name="paymentId" id="rejectPaymentId">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-danger">Reject Payment</button>
                </div>
            </form>
        </div>
    </div>
</div>
<script src="js/payment_list.js" type="text/javascript"></script>

