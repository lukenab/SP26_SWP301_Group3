<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<fmt:setLocale value="en_US"/>

<link href="css/class_management.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4 content-body class-management-page">
    <c:set var="currentStudents" value="${classInfo[7] == null ? 0 : classInfo[7]}" />
    <c:set var="maxCapacity" value="${classInfo[8] == null ? 0 : classInfo[8]}" />
    <c:set var="fillRate" value="${maxCapacity > 0 ? (currentStudents * 100.0 / maxCapacity) : 0}" />
    <c:set var="fillRateBar" value="${fillRate > 100 ? 100 : fillRate}" />
    <c:set var="paidCount" value="0" />
    <c:set var="unpaidCount" value="0" />
    <c:set var="gradedCount" value="0" />
    <c:set var="passCount" value="0" />
    <c:set var="totalGrade" value="0" />
    <c:set var="averageGrade" value="0" />
    <c:set var="passRate" value="0" />
    <c:forEach items="${studentsInClass}" var="s">
        <c:choose>
            <c:when test="${s[5] == 'Paid'}">
                <c:set var="paidCount" value="${paidCount + 1}" />
            </c:when>
            <c:when test="${s[5] == 'UnPaid' || s[5] == 'Unpaid'}">
                <c:set var="unpaidCount" value="${unpaidCount + 1}" />
            </c:when>
        </c:choose>
        <c:if test="${s[6] != null}">
            <c:set var="gradedCount" value="${gradedCount + 1}" />
            <c:set var="totalGrade" value="${totalGrade + s[6]}" />
            <c:if test="${s[6] >= 5}">
                <c:set var="passCount" value="${passCount + 1}" />
            </c:if>
        </c:if>
    </c:forEach>
    <c:if test="${gradedCount > 0}">
        <c:set var="averageGrade" value="${totalGrade / gradedCount}" />
        <c:set var="passRate" value="${passCount * 100.0 / gradedCount}" />
    </c:if>

    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-2">
                <li class="breadcrumb-item"><a href="dashboard?action=academic">Dashboard</a></li>
                <li class="breadcrumb-item"><a href="enrollment?action=classes">Class Management</a></li>
                <li class="breadcrumb-item active" aria-current="page">Class Details</li>
            </ol>
        </div>

        <div class="class-details-hero">
            <div class="class-details-hero-main">
                <div class="class-details-kicker">Class Overview</div>
                <h1 class="class-details-title">${classInfo[1]}</h1>
                <p class="class-details-subtitle">${classInfo[2]}</p>
                <div class="class-details-meta">
                    <span class="class-status-badge ${classInfo[6] == 'Active' ? 'active' : (classInfo[6] == 'Pending' ? 'pending' : 'inactive')}">
                        ${classInfo[6]}
                    </span>
                    <span class="class-details-meta-chip">
                        <i class='bx bx-user'></i>
                        ${empty classInfo[3] ? 'N/A' : classInfo[3]}
                    </span>
                    <span class="class-details-meta-chip">
                        <i class='bx bx-group'></i>
                        ${classInfo[7]}/${classInfo[8]} Students
                    </span>
                </div>
            </div>
            <div class="class-details-hero-actions">
                <a href="enrollment?action=addStudentForm&classId=${classInfo[0]}" class="btn btn-add-new">
                    <i class='bx bx-user-plus'></i> Manage Students
                </a>
                <a href="enrollment?action=classes" class="btn btn-back">
                    <i class='bx bx-left-arrow-alt'></i> Back to Classes
                </a>
            </div>
        </div>
    </div>

    <div class="class-detail-summary-grid mb-4">
        <div class="class-detail-summary-card emphasis">
            <span class="class-detail-label">Study Period</span>
            <span class="class-detail-value">
                <fmt:formatDate value="${classInfo[4]}" pattern="dd MMM yyyy"/> - <fmt:formatDate value="${classInfo[5]}" pattern="dd MMM yyyy"/>
            </span>
        </div>
        <div class="class-detail-summary-card">
            <span class="class-detail-label">Teacher</span>
            <span class="class-detail-value">${empty classInfo[3] ? 'N/A' : classInfo[3]}</span>
        </div>
        <div class="class-detail-summary-card">
            <span class="class-detail-label">Registration Deadline</span>
            <span class="class-detail-value">
                <c:choose>
                    <c:when test="${not empty classInfo[9]}">
                        <fmt:formatDate value="${classInfo[9]}" pattern="dd MMM yyyy"/>
                    </c:when>
                    <c:otherwise>N/A</c:otherwise>
                </c:choose>
            </span>
        </div>
        <div class="class-detail-summary-card">
            <span class="class-detail-label">Room</span>
            <span class="class-detail-value">${empty classInfo[10] ? 'Not assigned' : classInfo[10]}</span>
        </div>
    </div>

    <div class="class-metric-grid mb-4">
        <div class="class-metric-card primary">
            <div class="class-metric-label">Fill Rate</div>
            <div class="class-metric-value">
                <fmt:formatNumber value="${fillRate}" maxFractionDigits="0"/>%
            </div>
            <div class="class-metric-sub">${currentStudents}/${maxCapacity} enrolled</div>
            <div class="class-metric-bar">
                <span style="width: ${fillRateBar}%;"></span>
            </div>
        </div>
        <div class="class-metric-card">
            <div class="class-metric-label">Payment Status</div>
            <div class="class-metric-value">${paidCount}</div>
            <div class="class-metric-sub">${unpaidCount} unpaid</div>
            <div class="class-metric-icon success">
                <i class='bx bx-check-circle'></i>
            </div>
        </div>
        <div class="class-metric-card">
            <div class="class-metric-label">Average Grade</div>
            <div class="class-metric-value">
                <c:choose>
                    <c:when test="${gradedCount > 0}">
                        <fmt:formatNumber value="${averageGrade}" minFractionDigits="1" maxFractionDigits="1"/>
                    </c:when>
                    <c:otherwise>N/A</c:otherwise>
                </c:choose>
            </div>
            <div class="class-metric-sub">
                <c:choose>
                    <c:when test="${gradedCount > 0}">${gradedCount} graded students</c:when>
                    <c:otherwise>No grade data yet</c:otherwise>
                </c:choose>
            </div>
            <div class="class-metric-icon info">
                <i class='bx bx-line-chart'></i>
            </div>
        </div>
        <div class="class-metric-card">
            <div class="class-metric-label">Pass Rate</div>
            <div class="class-metric-value">
                <c:choose>
                    <c:when test="${gradedCount > 0}">
                        <fmt:formatNumber value="${passRate}" maxFractionDigits="0"/>%
                    </c:when>
                    <c:otherwise>N/A</c:otherwise>
                </c:choose>
            </div>
            <div class="class-metric-sub">${passCount} students passing (>= 5)</div>
            <div class="class-metric-icon warning">
                <i class='bx bx-award'></i>
            </div>
        </div>
    </div>

    <div class="card user-table-card border-0 bg-white class-student-card">
        <div class="class-student-header">
            <div>
                <h5 class="mb-1">Students In Class</h5>
                <p class="text-muted small mb-0">Current students enrolled in this class.</p>
            </div>
            <div class="class-student-actions">
                <span class="class-details-count">${studentsInClass.size()} students</span>
                
            </div>
        </div>
        <div class="table-responsive">
            <table class="table mb-0 align-middle">
                <thead>
                    <tr>
                        <th style="width: 8%">#</th>
                        <th style="width: 31%">Student</th>
                        <th style="width: 19%">Enrollment Date</th>
                        <th style="width: 12%">Final Rate</th>
                        <th style="width: 14%">Status</th>
                        <th style="width: 16%">Contact</th>
                    </tr>
                </thead>
                <tbody>
                    <c:if test="${empty studentsInClass}">
                        <tr>
                            <td colspan="6" class="text-center text-muted py-4">No students in class.</td>
                        </tr>
                    </c:if>
                    <c:forEach items="${studentsInClass}" var="s" varStatus="loop">
                        <tr>
                            <td>${loop.count}</td>
                            <td>
                                <div class="user-item">
                                    <div class="user-avatar-placeholder class-student-avatar">
                                        ${fn:substring(s[2], 0, 1)}
                                    </div>
                                    <div class="d-flex flex-column">
                                        <span class="user-name">${s[2]}</span>
                                        <span class="user-email">${s[3]}</span>
                                    </div>
                                </div>
                            </td>
                            <td>
                                <span class="class-detail-date">
                                    <fmt:formatDate value="${s[4]}" pattern="dd MMM yyyy"/>
                                </span>
                            </td>
                            <td>
                                <span class="class-detail-date">
                                    <c:choose>
                                        <c:when test="${s[6] != null}">
                                            <fmt:formatNumber value="${s[6]}" minFractionDigits="1" maxFractionDigits="1"/>
                                        </c:when>
                                        <c:otherwise>N/A</c:otherwise>
                                    </c:choose>
                                </span>
                            </td>
                            <td>
                                <span class="badge-status ${s[5] == 'Paid' || s[5] == 'Active' || s[5] == 'Completed' ? 'badge-active' : 'badge-inactive'}">
                                    ${s[5]}
                                </span>
                            </td>
                            <td>
                                <a href="mailto:${s[3]}" class="class-student-contact">${s[3]}</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
