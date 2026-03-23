<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link href="css/course_list.css" rel="stylesheet" type="text/css"/>
<link href="css/class_management.css" rel="stylesheet" type="text/css"/>

<c:set var="fullClasses" value="0"/>
<c:set var="lowClasses" value="0"/>
<c:set var="totalFillRate" value="0"/>
<c:set var="totalEnrollments" value="0"/>
<c:set var="gradedCount" value="0"/>
<c:set var="passCount" value="0"/>

<c:forEach items="${classFillRateList}" var="item">
    <c:set var="fillRate" value="${item[7]}"/>
    <c:set var="totalFillRate" value="${totalFillRate + fillRate}"/>
    <c:if test="${fillRate >= 90}">
        <c:set var="fullClasses" value="${fullClasses + 1}"/>
    </c:if>
    <c:if test="${fillRate < 50}">
        <c:set var="lowClasses" value="${lowClasses + 1}"/>
    </c:if>
</c:forEach>

<c:forEach items="${gradeEnrollmentSummaryList}" var="summary">
    <c:set var="totalEnrollments" value="${totalEnrollments + summary[3]}"/>
    <c:set var="gradedCount" value="${gradedCount + summary[7]}"/>
    <c:set var="passCount" value="${passCount + summary[8]}"/>
</c:forEach>

<c:choose>
    <c:when test="${not empty classFillRateList}">
        <c:set var="averageFillRate" value="${totalFillRate / classFillRateList.size()}"/>
    </c:when>
    <c:otherwise>
        <c:set var="averageFillRate" value="0"/>
    </c:otherwise>
</c:choose>

<c:choose>
    <c:when test="${gradedCount > 0}">
        <c:set var="passRate" value="${(passCount * 100.0) / gradedCount}"/>
    </c:when>
    <c:otherwise>
        <c:set var="passRate" value="0"/>
    </c:otherwise>
</c:choose>

<div class="container-fluid px-4 content-body class-management-page">
    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item active" aria-current="page">Dashboard</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">Academic Dashboard</h2>
                <p class="text-muted small mb-0">Quick access to the main academic reports.</p>
            </div>
        </div>
    </div>

    <div class="stat-card-grid">
        <div class="stat-card">
            <div class="stat-info">
                <p>Total Classes</p>
                <h3>${classFillRateList.size()}</h3>
            </div>
            <div class="icon-wrapper blue">
                <i class='bx bx-door-open'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Average Fill Rate</p>
                <h3><fmt:formatNumber value="${averageFillRate}" maxFractionDigits="1"/>%</h3>
            </div>
            <div class="icon-wrapper green">
                <i class='bx bx-bar-chart'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Total Enrollments</p>
                <h3>${totalEnrollments}</h3>
            </div>
            <div class="icon-wrapper orange">
                <i class='bx bx-user-check'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Pass Rate</p>
                <h3><fmt:formatNumber value="${passRate}" maxFractionDigits="1"/>%</h3>
            </div>
            <div class="icon-wrapper cyan">
                <i class='bx bx-trophy'></i>
            </div>
        </div>
    </div>

    <!-- Top Classes Section with Class Fill Summary -->
    <div class="row g-4 mt-2">
        <div class="col-12">
            <div class="card user-table-card border-0 bg-white">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <div>
                            <h5 class="mb-0"><i class='bx bx-file-report me-2'></i>Class Overview & Fill Rate Report</h5>
                            <p class="text-muted small mb-0">Monitor capacity and identify classes that are nearly full or under-filled.</p>
                        </div>
                    </div>

                    <!-- Fill Report Summary -->
                    <div class="row mb-3">
                        <div class="col-md-4">
                            <div class="alert alert-danger py-2 px-3 mb-0">
                                <div class="d-flex justify-content-between align-items-center">
                                    <small><i class='bx bx-up-arrow-alt me-1'></i>Nearly full (≥90%)</small>
                                    <strong>${fullClasses}</strong>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="alert alert-info py-2 px-3 mb-0">
                                <div class="d-flex justify-content-between align-items-center">
                                    <small><i class='bx bx-down-arrow-alt me-1'></i>Low fill (<50%)</small>
                                    <strong>${lowClasses}</strong>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="alert alert-warning py-2 px-3 mb-0">
                                <div class="d-flex justify-content-between align-items-center">
                                    <small><i class='bx bx-bar-chart me-1'></i>Average fill rate</small>
                                    <strong><fmt:formatNumber value="${averageFillRate}" maxFractionDigits="1"/>%</strong>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="table-responsive" style="max-height: 350px; overflow-y: auto;">
                        <c:choose>
                            <c:when test="${empty classFillRateList}">
                                <div class="text-center py-4 text-muted">
                                    <i class='bx bx-inbox' style="font-size: 2rem;"></i>
                                    <p class="mt-2">No classes available</p>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <table class="table table-sm mb-0 align-middle">
                                    <thead>
                                        <tr style="background-color: #f9fafb;">
                                            <th style="width: 20%;">Class Name</th>
                                            <th style="width: 15%;">Course</th>
                                            <th style="width: 12%;">Teacher</th>
                                            <th style="width: 10%;">Enrolled</th>
                                            <th style="width: 10%;">Capacity</th>
                                            <th style="width: 13%;">Fill Rate</th>
                                            <th style="width: 10%;">Status</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${classFillRateList}" var="cls" begin="0" end="9">
                                            <tr>
                                                <td><strong>${cls[1]}</strong></td>
                                                <td><span class="text-muted">${cls[2]}</span></td>
                                                <td>${cls[3]}</td>
                                                <td>${cls[5]}</td>
                                                <td>${cls[6]}</td>
                                                <td>
                                                    <div class="progress" style="height: 20px;">
                                                        <c:choose>
                                                            <c:when test="${cls[7] >= 90}">
                                                                <div class="progress-bar bg-danger" style="width: ${cls[7]}%">
                                                                    <fmt:formatNumber value="${cls[7]}" maxFractionDigits="0"/>%
                                                                </div>
                                                            </c:when>
                                                            <c:when test="${cls[7] >= 70}">
                                                                <div class="progress-bar bg-success" style="width: ${cls[7]}%">
                                                                    <fmt:formatNumber value="${cls[7]}" maxFractionDigits="0"/>%
                                                                </div>
                                                            </c:when>
                                                            <c:when test="${cls[7] >= 50}">
                                                                <div class="progress-bar bg-warning" style="width: ${cls[7]}%">
                                                                    <fmt:formatNumber value="${cls[7]}" maxFractionDigits="0"/>%
                                                                </div>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <div class="progress-bar bg-info" style="width: ${cls[7]}%">
                                                                    <fmt:formatNumber value="${cls[7]}" maxFractionDigits="0"/>%
                                                                </div>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </div>
                                                </td>
                                                <td>
                                                    <c:if test="${cls[4] == 'Active'}">
                                                        <span class="badge bg-success">Active</span>
                                                    </c:if>
                                                    <c:if test="${cls[4] != 'Active'}">
                                                        <span class="badge bg-secondary">${cls[4]}</span>
                                                    </c:if>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </c:otherwise>
                        </c:choose>
                    </div>

                </div>
            </div>
        </div>
    </div>

    <!-- Grade Enrollment Details Table with Grade Report Summary -->
    <div class="row g-4 mt-2">
        <div class="col-12">
            <div class="card user-table-card border-0 bg-white">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <div>
                            <h5 class="mb-0"><i class='bx bx-list-check me-2'></i>Grade & Enrollment Summary Report</h5>
                            <p class="text-muted small mb-0">Review class-level enrollment and learner performance metrics.</p>
                        </div>
                    </div>

                    <!-- Grade Report Summary -->
                    <div class="row mb-3">
                        <div class="col-md-4">
                            <div class="alert alert-primary py-2 px-3 mb-0">
                                <div class="d-flex justify-content-between align-items-center">
                                    <small><i class='bx bx-list-check me-1'></i>Classes with grades</small>
                                    <strong>${gradeEnrollmentSummaryList.size()}</strong>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="alert alert-secondary py-2 px-3 mb-0">
                                <div class="d-flex justify-content-between align-items-center">
                                    <small><i class='bx bx-user-detail me-1'></i>Graded students</small>
                                    <strong>${gradedCount}</strong>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="alert alert-success py-2 px-3 mb-0">
                                <div class="d-flex justify-content-between align-items-center">
                                    <small><i class='bx bx-trophy me-1'></i>Overall pass rate</small>
                                    <strong><fmt:formatNumber value="${passRate}" maxFractionDigits="1"/>%</strong>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="table-responsive" style="max-height: 350px; overflow-y: auto;">
                        <c:choose>
                            <c:when test="${empty gradeEnrollmentSummaryList}">
                                <div class="text-center py-4 text-muted">
                                    <i class='bx bx-inbox' style="font-size: 2rem;"></i>
                                    <p class="mt-2">No grade data available</p>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <table class="table table-sm mb-0 align-middle">
                                    <thead>
                                        <tr style="background-color: #f9fafb;">
                                            <th style="width: 20%;">Class Name</th>
                                            <th style="width: 15%;">Course</th>
                                            <th style="width: 12%;">Total</th>
                                            <th style="width: 12%;">Paid</th>
                                            <th style="width: 12%;">Graded</th>
                                            <th style="width: 12%;">Pass</th>
                                            <th style="width: 15%;">Pass Rate</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${gradeEnrollmentSummaryList}" var="summary" begin="0" end="9">
                                            <tr>
                                                <td><strong>${summary[1]}</strong></td>
                                                <td><span class="text-muted">${summary[2]}</span></td>
                                                <td>${summary[3]}</td>
                                                <td>
                                                    <span class="badge bg-success">${summary[4]}</span>
                                                </td>
                                                <td>${summary[6]}</td>
                                                <td>
                                                    <span class="badge bg-info">${summary[8]}</span>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${summary[6] > 0}">
                                                            <c:set var="classPassRate" value="${(summary[8] * 100.0) / summary[6]}"/>
                                                            <strong class="text-success"><fmt:formatNumber value="${classPassRate}" maxFractionDigits="0"/>%</strong>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <strong class="text-muted">-</strong>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </c:otherwise>
                        </c:choose>
                    </div>

                </div>
            </div>
        </div>
    </div>
</div>