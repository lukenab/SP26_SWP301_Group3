<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>

<link href="css/manageUser.css" rel="stylesheet" type="text/css"/>
<link href="css/assessment_management.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4 content-body">

    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="dashboard?action=academic">Dashboard</a></li>
                <li class="breadcrumb-item"><a href="course?action=all">Course Management</a></li>
                <li class="breadcrumb-item active" aria-current="page">Assessment Management</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">Assessment Management</h2>
                <c:if test="${not empty course}">
                    <p class="text-muted small mb-0">Manage assessments for ${course.courseName}</p>
                </c:if>
            </div>
            <a href="course?action=all" class="btn-secondary">
                <i class='bx bx-arrow-left'></i> Back to Courses
            </a>
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

    <c:choose>
        <c:when test="${empty course}">
            <div class="detail-card empty-state">
                <i class='bx bx-book-open empty-icon'></i>
                <h4>Course not found</h4>
                <p class="mb-0">The requested course may have been deleted or is unavailable.</p>
            </div>
        </c:when>
        <c:otherwise>

            <!-- Course Info Card -->
            <div class="detail-card hero-card mb-4">
                <div class="hero-content">
                    <span class="course-id">Course ID: #${course.courseId}</span>
                    <h1>${course.courseName}</h1>
                    <div class="chips">
                        <span class="chip chip-slots">
                            <i class='bx bx-group'></i> ${course.totalSlots} slots
                        </span>
                        <span class="chip chip-fee">
                            <i class='bx bx-dollar-circle'></i>
                            <fmt:formatNumber type="currency" value="${course.tuitionFee}" />
                        </span>
                        <span class="chip ${course.status ? 'chip-active' : 'chip-inactive'}">
                            <i class='bx ${course.status ? "bx-check-circle" : "bx-block"}'></i>
                            ${course.status ? 'Active' : 'Inactive'}
                        </span>
                    </div>
                </div>
            </div>

            <!-- Weight Summary -->
            <div class="detail-grid mb-4">
                <div class="detail-card section-card weight-summary">
                    <div class="section-title">
                        <i class='bx bx-stats'></i>
                        <h5>Weight Summary</h5>
                    </div>
                    <div class="weight-bar-container">
                        <div class="weight-bar">
                            <div class="weight-filled" style="width: ${totalWeight}%">
                                <span class="weight-text">${totalWeight}%</span>
                            </div>
                        </div>
                        <p class="weight-info ${totalWeight > 100 ? 'text-danger' : totalWeight < 100 ? 'text-warning' : 'text-success'}">
                            <c:choose>
                                <c:when test="${totalWeight > 100}">
                                    <i class='bx bx-error-circle'></i> Total weight exceeds 100%
                                </c:when>
                                <c:when test="${totalWeight < 100}">
                                    <i class='bx bx-info-circle'></i> Total weight is ${100 - totalWeight}% below target
                                </c:when>
                                <c:otherwise>
                                    <i class='bx bx-check-circle'></i> Total weight is exactly 100%
                                </c:otherwise>
                            </c:choose>
                        </p>
                    </div>
                </div>
            </div>

            <!-- Add Assessment Form -->
            <div class="detail-card section-card">
                <div class="section-title">
                    <i class='bx bx-plus-circle'></i>
                    <h5>Add New Assessment</h5>
                </div>

                <form method="POST" action="assessment" class="assessment-form">
                    <input type="hidden" name="action" value="add">
                    <input type="hidden" name="courseId" value="${course.courseId}">

                    <div class="form-row">
                        <div class="form-group">
                            <label for="assessmentName" class="form-label">Assessment Name <span class="required">*</span></label>
                            <input type="text" class="form-control" id="assessmentName" name="assessmentName" placeholder="e.g., Midterm Exam, Final Project" required>
                        </div>

                        <div class="form-group">
                            <label for="weight" class="form-label">Weight (%) <span class="required">*</span></label>
                            <div class="input-group">
                                <input type="number" class="form-control" id="weight" name="weight" placeholder="e.g., 30"
                                       min="0" max="100" step="0.1" required>
                                <span class="input-group-text">%</span>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <small class="form-text text-muted">
                            Remaining capacity: <strong id="remaining">${100 - totalWeight}%</strong>
                        </small>
                    </div>

                    <button type="submit" class="btn btn-primary">
                        <i class='bx bx-check'></i> Add Assessment
                    </button>
                </form>
            </div>

            <!-- Assessments List -->
            <div class="card user-table-card border-0 bg-white mt-4">
                <div class="section-title p-3">
                    <i class='bx bx-list-ul'></i>
                    <h5>Current Assessments</h5>
                </div>

                <c:choose>
                    <c:when test="${empty assessments}">
                        <div class="empty-state-table">
                            <i class='bx bx-inbox empty-icon-sm'></i>
                            <p>No assessments added yet</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="table-responsive">
                            <table class="table mb-0 align-middle">
                                <thead>
                                    <tr>
                                        <th style="width: 5%">#</th>
                                        <th style="width: 50%">Assessment Name</th>
                                        <th style="width: 15%">Weight</th>
                                        <th style="width: 20%">Progress</th>
                                        <th style="width: 10%">Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${assessments}" var="assessment" varStatus="loop">
                                        <tr>
                                            <td>${loop.index + 1}</td>
                                            <td>
                                                <div class="assessment-name">
                                                    <i class='bx bx-file'></i>
                                                    ${assessment.assessmentName}
                                                </div>
                                            </td>
                                            <td>
                                                <span class="weight-badge">${assessment.weight}%</span>
                                            </td>
                                            <td>
                                                <div class="mini-progress-bar">
                                                    <div class="mini-progress-fill" style="width: ${assessment.weight}%"></div>
                                                </div>
                                                <span class="progress-text">${assessment.weight}% of ${totalWeight}%</span>
                                            </td>
                                            <td>
                                                <a href="course?action=editAssessment&assessmentId=${assessment.assessmentId}&courseId=${course.courseId}" class="action-btn edit">
                                                    <i class='bx bx-edit'></i>
                                                </a>
                                                <a href="course?action=deleteAssessment&assessmentId=${assessment.assessmentId}&courseId=${course.courseId}" class="action-btn delete">
                                                    <i class='bx bx-trash'></i>
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

        </c:otherwise>
    </c:choose>

</div>

<script src="/js/manageUser.js" type="text/javascript"></script>

