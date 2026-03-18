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
                <i class='bx bxs-school'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Average Fill Rate</p>
                <h3><fmt:formatNumber value="${averageFillRate}" maxFractionDigits="1"/>%</h3>
            </div>
            <div class="icon-wrapper green">
                <i class='bx bxs-bar-chart-alt-2'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Total Enrollments</p>
                <h3>${totalEnrollments}</h3>
            </div>
            <div class="icon-wrapper orange">
                <i class='bx bxs-user-check'></i>
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

    <div class="row g-4">
        <div class="col-12 col-xl-6">
            <div class="card user-table-card border-0 bg-white h-100">
                <div class="card-body d-flex flex-column">
                    <div class="d-flex justify-content-between align-items-start mb-3">
                        <div>
                            <h5 class="mb-1">Class Fill Report</h5>
                            <p class="text-muted small mb-0">Monitor capacity and identify classes that are nearly full or under-filled.</p>
                        </div>
                        <div class="icon-wrapper blue">
                            <i class='bx bx-file-report'></i>
                        </div>
                    </div>
                    <div class="mb-4">
                        <div class="d-flex justify-content-between small text-muted mb-2">
                            <span>Nearly full classes</span>
                            <span>${fullClasses}</span>
                        </div>
                        <div class="d-flex justify-content-between small text-muted">
                            <span>Low fill classes</span>
                            <span>${lowClasses}</span>
                        </div>
                    </div>
                    <div class="mt-auto">
                        <a href="dashboard?action=academicFillRateReport" class="action-btn primary">Open Report</a>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-12 col-xl-6">
            <div class="card user-table-card border-0 bg-white h-100">
                <div class="card-body d-flex flex-column">
                    <div class="d-flex justify-content-between align-items-start mb-3">
                        <div>
                            <h5 class="mb-1">Grade Report</h5>
                            <p class="text-muted small mb-0">Review class-level enrollment and drill into learner performance when needed.</p>
                        </div>
                        <div class="icon-wrapper green">
                            <i class='bx bx-article'></i>
                        </div>
                    </div>
                    <div class="mb-4">
                        <div class="d-flex justify-content-between small text-muted mb-2">
                            <span>Classes with summary</span>
                            <span>${gradeEnrollmentSummaryList.size()}</span>
                        </div>
                        <div class="d-flex justify-content-between small text-muted">
                            <span>Overall pass rate</span>
                            <span><fmt:formatNumber value="${passRate}" maxFractionDigits="1"/>%</span>
                        </div>
                    </div>
                    <div class="mt-auto">
                        <a href="dashboard?action=academicGradeEnrollmentReport" class="action-btn primary">Open Report</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
