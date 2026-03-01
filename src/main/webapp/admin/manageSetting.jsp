<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link href="css/manageSetting.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4 content-body">
    <div aria-label="breadcrumb">
        <ol class="breadcrumb mb-1">
            <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
            <li class="breadcrumb-item active"><a href="user">System Settings</a></li>
        </ol>
    </div>
    <div class="content-header mb-4">
        <div>
            <h2 class="page-title">System Settings</h2>
            <p class="text-muted small mb-0">Configure global parameters for the Language Center</p>
        </div>
    </div>

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

    <form action="setting" method="POST">
        <div class="row">
            <div class="col-md-4 mb-4">
                <div class="card academic-card">
                    <div class="card-header">
                        <h5>Academic <i class='bx bx-book-open'></i></h5>
                    </div>
                    <div class="card-body">
                        <div class="mb-3">
                            <label class="form-label text-secondary small fw-bold">Passing Grade</label>
                            <input type="number" step="0.1" name="DEFAULT_PASSING_GRADE" class="form-control" value="${settings['DEFAULT_PASSING_GRADE']}" required>
                            <div class="form-text">Minimum grade to pass</div>
                        </div>
                        <div class="mb-3">
                            <label class="form-label text-secondary small fw-bold">Max Students / Class</label>
                            <input type="number" name="MAX_STUDENTS_PER_CLASS" class="form-control" value="${settings['MAX_STUDENTS_PER_CLASS']}" required>
                            <div class="form-text">Default capacity for new classes.</div>
                        </div>
                        <div class="mb-3">
                            <label class="form-label text-secondary small fw-bold">Max Absence (%)</label>
                            <div class="input-group">
                                <input type="number" name="MAX_ABSENCE_PERCENTAGE" class="form-control" value="${settings['MAX_ABSENCE_PERCENTAGE']}" required>
                                <span class="input-group-text">%</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-md-4 mb-4">
                <div class="card academic-card">
                    <div class="card-header">
                        <h5 class="text-success">Business <i class='bx bx-chart-trend'></i> </h5>
                    </div>
                    <div class="card-body">
                        <div class="mb-3">
                            <label class="form-label text-secondary small fw-bold">Payment Grace Period</label>
                            <div class="input-group">
                                <input type="number" name="PAYMENT_GRACE_PERIOD_DAYS" class="form-control" value="${settings['PAYMENT_GRACE_PERIOD_DAYS']}" required>
                                <span class="input-group-text">Days</span>
                            </div>
                            <div class="form-text" style="font-size:12px;">Days allowed before dropping unpaid enrollments.</div>
                        </div>                       
                    </div>
                </div>
            </div>

            <div class="col-md-4 mb-4">
                <div class="card academic-card">
                    <div class="card-header">
                        <h5 class="text-danger">Security <i class='bx bx-shield-quarter'></i></h5>
                    </div>
                    <div class="card-body">
                        <div class="mb-3">
                            <label class="form-label text-secondary small fw-bold">Max Login Attempts</label>
                            <input type="number" name="MAX_LOGIN_ATTEMPTS" class="form-control" value="${settings['MAX_LOGIN_ATTEMPTS']}" required>
                            <div class="form-text">Lock account after N failed attempts.</div>
                        </div>
                        <div class="mb-3">
                            <label class="form-label text-secondary small fw-bold">Session Timeout</label>
                            <div class="input-group">
                                <input type="number" name="SESSION_TIMEOUT_MINUTES" class="form-control" value="${settings['SESSION_TIMEOUT_MINUTES']}" required>
                                <span class="input-group-text">Min</span>
                            </div>
                            <div class="form-text">Auto logout idle users.</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="setting-btn">
            <button type="reset" class="btn btn-cancel">Cancel</button>
            <button type="submit" class="btn btn-save"><i class='bx bx-save'></i> Save Settings</button>
        </div>
    </form>
</div>