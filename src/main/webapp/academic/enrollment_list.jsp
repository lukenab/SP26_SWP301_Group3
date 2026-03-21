<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link href="css/manageUser.css" rel="stylesheet" type="text/css"/>
<link href="css/payment_list.css?v=20260319" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4 content-body">
    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="dashboard?action=academic">Dashboard</a></li>
                <li class="breadcrumb-item active" aria-current="page">Enrollment Management</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title"><i class='bx bx-user-check'></i> Enrollment Management</h2>
                <p class="text-muted small mb-0">Review and approve or reject student enrollment requests.</p>
            </div>
        </div>
    </div>

    <div class="stat-card-grid">
        <div class="stat-card">
            <div class="stat-info">
                <p>Total Enrollments</p>
                <h3>${totalEnrollments}</h3>
            </div>
            <div class="icon-wrapper blue">
                <i class='bx bx-list-check'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Pending</p>
                <h3>${pendingEnrollments}</h3>
            </div>
            <div class="icon-wrapper" style="background: #fef3c7; color: #d97706;">
                <i class='bx bx-time-five'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Approved</p>
                <h3>${activeEnrollments}</h3>
            </div>
            <div class="icon-wrapper green">
                <i class='bx bx-check-circle'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Rejected</p>
                <h3>${rejectedEnrollments}</h3>
            </div>
            <div class="icon-wrapper" style="background: #fee2e2; color: #dc2626;">
                <i class='bx bx-x-circle'></i>
            </div>
        </div>
    </div>

    <form action="enrollment" method="GET" class="filter-container flex-wrap">
        <input type="hidden" name="action" value="requests">

        <div class="d-flex gap-3 flex-wrap w-100">
            <select name="courseId" class="custom-select-filter" onchange="this.form.submit()"
                    style="border: 1px solid #e2e8f0; padding: 8px 16px; border-radius: 8px; background: white; outline: none; cursor: pointer; min-width: 200px;">
                <option value="0">All Courses</option>
                <c:forEach items="${courseOptions}" var="course">
                    <option value="${course[0]}" ${selectedCourseId == course[0].toString() ? 'selected' : ''}>
                        ${course[1]}
                    </option>
                </c:forEach>
            </select>

            <select name="classId" class="custom-select-filter" onchange="this.form.submit()"
                    style="border: 1px solid #e2e8f0; padding: 8px 16px; border-radius: 8px; background: white; outline: none; cursor: pointer; min-width: 220px;">
                <option value="0">All Classes</option>
                <c:forEach items="${classOptions}" var="cls">
                    <option value="${cls[0]}" ${selectedClassId == cls[0].toString() ? 'selected' : ''}>
                        ${cls[1]} - ${cls[2]}
                    </option>
                </c:forEach>
            </select>

            <select name="status" class="custom-select-filter" onchange="this.form.submit()"
                    style="border: 1px solid #e2e8f0; padding: 8px 16px; border-radius: 8px; background: white; outline: none; cursor: pointer; min-width: 200px;">
                <option value="" ${empty selectedStatus ? 'selected' : ''}>All Statuses</option>
                <option value="Pending" ${selectedStatus == 'Pending' ? 'selected' : ''}>Pending</option>
                <option value="Active" ${selectedStatus == 'Active' ? 'selected' : ''}>Approved</option>
                <option value="Rejected" ${selectedStatus == 'Rejected' ? 'selected' : ''}>Rejected</option>
            </select>

            <button type="submit" style="display: none;"></button>
        </div>
    </form>

    <c:if test="${not empty sessionScope.message}">
        <div class="custom-toast toast-${sessionScope.messageType}" id="toastMessage">
            <div class="toast-icon">
                <c:choose>
                    <c:when test="${sessionScope.messageType == 'success'}">
                        <i class='bx bx-check-circle'></i>
                    </c:when>
                    <c:otherwise>
                        <i class='bx bx-cross-circle'></i>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="toast-content">
                <span class="toast-title">${sessionScope.messageType == 'success' ? 'Success!' : 'Error!'}</span>
                <span class="toast-message">${sessionScope.message}</span>
            </div>
            <button class="toast-close" onclick="closeToast()">
                <i class='bx bx-x'></i>
            </button>
        </div>

        <c:remove var="message" scope="session" />
        <c:remove var="messageType" scope="session" />
    </c:if>

    <div class="card user-table-card border-0 bg-white">
        <div class="table-responsive">
            <table class="table mb-0 align-middle">
                <thead>
                    <tr>
                        <th style="width: 30px">#</th>
                        <th>Student</th>
                        <th>Course</th>
                        <th>Class</th>
                        <th>Enroll Date</th>
                        <th>Payment</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty enrollmentList}">
                            <tr>
                                <td colspan="8" class="text-center text-muted py-4">
                                    <i class='bx bx-info-circle' style='font-size: 2rem;'></i>
                                    <p class="mb-0 mt-2">No enrollments found</p>
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${enrollmentList}" var="enrollment" varStatus="loop">
                                <tr>
                                    <td>${loop.count}</td>
                                    <td>
                                        <div class="d-flex flex-column">
                                            <span class="user-name">${enrollment[2]}</span>
                                            <span class="user-email text-muted small">${enrollment[3]}</span>
                                        </div>
                                    </td>
                                    <td>${enrollment[7]}</td>
                                    <td><span class="badge badge-student">${enrollment[5]}</span></td>
                                    <td><fmt:formatDate value="${enrollment[8]}" pattern="dd MMM yyyy"/></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty enrollment[10]}">${enrollment[10]}</c:when>
                                            <c:otherwise>-</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${enrollment[9] == 'Active'}">
                                                <span class="badge" style="background: #dcfce7; color: #16a34a; padding: 6px 12px; border-radius: 6px; font-weight: 500;">
                                                    <i class='bx bx-check-circle'></i> Approved
                                                </span>
                                            </c:when>
                                            <c:when test="${enrollment[9] == 'Rejected'}">
                                                <span class="badge" style="background: #fee2e2; color: #dc2626; padding: 6px 12px; border-radius: 6px; font-weight: 500;">
                                                    <i class='bx bx-x-circle'></i> Rejected
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge" style="background: #fef3c7; color: #d97706; padding: 6px 12px; border-radius: 6px; font-weight: 500;">
                                                    <i class='bx bx-time-five'></i> Pending
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="payment-actions">
                                        <c:if test="${enrollment[9] != 'Active'}">
                                            <button type="button" class="action-btn action-compact action-approve" title="Approve"
                                                    onclick="openApproveEnrollment(${enrollment[0]}, '${enrollment[2]}', '${enrollment[5]}')">
                                                <i class='bx bx-check'></i>
                                            </button>
                                        </c:if>
                                        <c:if test="${enrollment[9] != 'Active' && enrollment[9] != 'Rejected'}">
                                            <button type="button" class="action-btn action-compact delete action-reject" title="Reject"
                                                    onclick="openRejectEnrollment(${enrollment[0]}, '${enrollment[2]}', '${enrollment[5]}')">
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

<div class="modal fade" id="approveEnrollmentModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">
                    <i class='bx bx-check-circle' style='color: #16a34a;'></i> Confirm Approval
                </h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form action="enrollment" method="POST">
                <input type="hidden" name="action" value="approveEnrollment">
                <input type="hidden" name="enrollmentId" id="approveEnrollmentId">
                <div class="modal-body">
                    <p>Approve enrollment for <strong id="approveEnrollmentStudent"></strong> in <strong id="approveEnrollmentClass"></strong>?</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-success">Approve Enrollment</button>
                </div>
            </form>
        </div>
    </div>
</div>

<div class="modal fade" id="rejectEnrollmentModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">
                    <i class='bx bx-x-circle' style='color: #dc2626;'></i> Confirm Rejection
                </h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form action="enrollment" method="POST">
                <input type="hidden" name="action" value="rejectEnrollment">
                <input type="hidden" name="enrollmentId" id="rejectEnrollmentId">
                <div class="modal-body">
                    <p>Reject enrollment for <strong id="rejectEnrollmentStudent"></strong> in <strong id="rejectEnrollmentClass"></strong>?</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-danger">Reject Enrollment</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    function openApproveEnrollment(enrollmentId, studentName, className) {
        document.getElementById('approveEnrollmentId').value = enrollmentId;
        document.getElementById('approveEnrollmentStudent').textContent = studentName;
        document.getElementById('approveEnrollmentClass').textContent = className;
        new bootstrap.Modal(document.getElementById('approveEnrollmentModal')).show();
    }

    function openRejectEnrollment(enrollmentId, studentName, className) {
        document.getElementById('rejectEnrollmentId').value = enrollmentId;
        document.getElementById('rejectEnrollmentStudent').textContent = studentName;
        document.getElementById('rejectEnrollmentClass').textContent = className;
        new bootstrap.Modal(document.getElementById('rejectEnrollmentModal')).show();
    }

    function closeToast() {
        const toast = document.getElementById('toastMessage');
        if (toast) {
            toast.remove();
        }
    }

    setTimeout(function () {
        closeToast();
    }, 4000);
</script>
