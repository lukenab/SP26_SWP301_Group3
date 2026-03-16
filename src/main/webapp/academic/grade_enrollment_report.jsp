<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link href="css/course_list.css" rel="stylesheet" type="text/css"/>
<link href="css/class_management.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4 content-body class-management-page">
    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="dashboard">Dashboard</a></li>
                <li class="breadcrumb-item active" aria-current="page">Grade / Enrollment Report</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">Generate Grade/Enrollment Report</h2>
                <p class="text-muted small mb-0">Manage by class first, then open student details only when needed.</p>
            </div>
            <a href="enrollment?action=classes" class="btn btn-back">
                <i class='bx bx-left-arrow-alt'></i> Back to Classes
            </a>
        </div>
    </div>

    <c:set var="totalEnrollments" value="0"/>
    <c:set var="paidCount" value="0"/>
    <c:set var="unpaidCount" value="0"/>
    <c:set var="gradedCount" value="0"/>
    <c:set var="passCount" value="0"/>
    <c:set var="selectedClassName" value=""/>

    <c:forEach items="${gradeEnrollmentSummaryList}" var="summary">
        <c:set var="totalEnrollments" value="${totalEnrollments + summary[3]}"/>
        <c:set var="paidCount" value="${paidCount + summary[4]}"/>
        <c:set var="unpaidCount" value="${unpaidCount + summary[5]}"/>
        <c:set var="gradedCount" value="${gradedCount + summary[7]}"/>
        <c:set var="passCount" value="${passCount + summary[8]}"/>
        <c:if test="${selectedClassId == summary[0]}">
            <c:set var="selectedClassName" value="${summary[1]}"/>
        </c:if>
    </c:forEach>

    <c:choose>
        <c:when test="${gradedCount > 0}">
            <c:set var="passRate" value="${(passCount * 100.0) / gradedCount}"/>
        </c:when>
        <c:otherwise>
            <c:set var="passRate" value="0"/>
        </c:otherwise>
    </c:choose>

    <div class="stat-card-grid">
        <div class="stat-card">
            <div class="stat-info">
                <p>Total Classes</p>
                <h3>${gradeEnrollmentSummaryList.size()}</h3>
            </div>
            <div class="icon-wrapper blue">
                <i class='bx bxs-school'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Total Enrollments</p>
                <h3>${totalEnrollments}</h3>
            </div>
            <div class="icon-wrapper green">
                <i class='bx bxs-user-check'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Paid / Active</p>
                <h3>${paidCount}</h3>
            </div>
            <div class="icon-wrapper orange">
                <i class='bx bxs-badge-check'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Pass Rate</p>
                <h3><fmt:formatNumber value="${passRate}" maxFractionDigits="1"/>%</h3>
            </div>
            <div class="icon-wrapper cyan">
                <i class='bx bxs-graduation'></i>
            </div>
        </div>
    </div>

    <div class="card user-table-card border-0 bg-white mb-4">
        <div class="card-body border-bottom report-filter-bar">
            <div>
                <h5 class="mb-1">Class Overview</h5>
            </div>

        </div>
        <div class="table-responsive">
            <table class="table mb-0 align-middle">
                <thead>
                    <tr>
                        <th style="width: 5%">#</th>
                        <th style="width: 16%">Class</th>
                        <th style="width: 20%">Course</th>
                        <th style="width: 12%">Enrollments</th>
                        <th style="width: 12%">Paid</th>
                        <th style="width: 12%">Unpaid</th>
                        <th style="width: 13%">Pass Rate</th>
                        <th style="width: 10%">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:if test="${empty gradeEnrollmentSummaryList}">
                        <tr>
                            <td colspan="8" class="text-center text-muted py-4">No class summary data found.</td>
                        </tr>
                    </c:if>

                    <c:forEach items="${gradeEnrollmentSummaryList}" var="summary" varStatus="loop">
                        <tr class="${selectedClassId == summary[0] ? 'selected-report-row' : ''}">
                            <td>${loop.count}</td>
                            <td class="fw-semibold">${summary[1]}</td>
                            <td>${summary[2]}</td>
                            <td>${summary[3]}</td>
                            <td><span class="badge-status badge-active">${summary[4]}</span></td>
                            <td>
                                <span class="badge-status badge-inactive">
                                    ${summary[5]}
                                </span>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${summary[7] > 0}">
                                        <fmt:formatNumber value="${(summary[8] * 100.0) / summary[7]}" maxFractionDigits="1"/>%
                                    </c:when>
                                    <c:otherwise>N/A</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <a href="dashboard?action=academicGradeEnrollmentReport&classId=${summary[0]}" class="action-btn primary">
                                    View
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <div class="card user-table-card border-0 bg-white">
        <div class="card-body border-bottom report-detail-header">
            <div>
                <h5 class="mb-1">Student Details</h5>
<!--                <p class="text-muted small mb-0">
                    <c:choose>
                        <c:when test="${selectedClassId > 0}">
                            Showing students in class <strong>${selectedClassName}</strong>.
                        </c:when>
                        <c:otherwise>
                            Choose a class above to open the student-level enrollment and grade list.
                        </c:otherwise>
                    </c:choose>
                </p>-->
            </div>
            <c:if test="${selectedClassId > 0}">
                <a href="dashboard?action=academicGradeEnrollmentReport" class="btn btn-back">Clear Filter</a>
            </c:if>
        </div>
        <div class="table-responsive">
            <table class="table mb-0 align-middle">
                <thead>
                    <tr>
                        <th style="width: 6%">#</th>
                        <th style="width: 22%">Student</th>
                        <th style="width: 16%">Class</th>
                        <th style="width: 20%">Course</th>
                        <th style="width: 12%">Enroll Date</th>
                        <th style="width: 10%">Status</th>
                        <th style="width: 8%">Final Grade</th>
                        <th style="width: 12%">Grade Result</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${selectedClassId == 0}">
                            <tr>
                                <td colspan="8" class="text-center text-muted py-4">Select a class from the overview table to load student details.</td>
                            </tr>
                        </c:when>
                        <c:when test="${empty gradeEnrollmentList}">
                            <tr>
                                <td colspan="8" class="text-center text-muted py-4">No students found for this class.</td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${gradeEnrollmentList}" var="item" varStatus="loop">
                                <tr>
                                    <td>${loop.count}</td>
                                    <td class="fw-semibold">${item[3]}</td>
                                    <td>${item[1]}</td>
                                    <td>${item[2]}</td>
                                    <td>
                                        <fmt:formatDate value="${item[4]}" pattern="dd MMM yyyy"/>
                                    </td>
                                    <td>
                                        <span class="class-status-badge ${(item[5] == 'Paid' or item[5] == 'Active' or item[5] == 'Completed') ? 'active' : 'inactive'}">
                                            ${(item[5] == 'Paid' or item[5] == 'Active' or item[5] == 'Completed') ? 'Paid' : 'UnPaid'}
                                        </span>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${item[6] > 0}">
                                                <fmt:formatNumber value="${item[6]}" maxFractionDigits="1"/>
                                            </c:when>
                                            <c:otherwise>N/A</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${item[6] >= 8}">
                                                <span class="badge-status badge-active">Excellent</span>
                                            </c:when>
                                            <c:when test="${item[6] >= 6.5}">
                                                <span class="badge-status badge-active">Good</span>
                                            </c:when>
                                            <c:when test="${item[6] >= 5}">
                                                <span class="badge-status report-badge-average">Average</span>
                                            </c:when>
                                            <c:when test="${item[6] > 0}">
                                                <span class="badge-status badge-inactive">At Risk</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge-status report-badge-ungraded">Not Graded</span>
                                            </c:otherwise>
                                        </c:choose>
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
