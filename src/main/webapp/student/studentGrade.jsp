<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="en_US"/>
<%@ page contentType="text/html;charset=UTF-8" %>

<link href="css/viewGrade.css" rel="stylesheet"/>

<style>
    /* ===== DARK MODE FOR VIEW GRADE ===== */

    body.dark .profile-content-card {
        background: #1e1e1e;
        box-shadow: 0 8px 25px rgba(0,0,0,0.6);
    }

    body.dark .page-title,
    body.dark .section-title,
    body.dark h5 {
        color: #ffffff;
    }

    body.dark .text-muted {
        color: #9ca3af !important;
    }

    body.dark .custom-table thead {
        background-color: #2a2a2a;
    }

    body.dark .custom-table th {
        color: #e5e7eb;
        border-bottom: 1px solid #374151;
    }

    body.dark .custom-table td {
        color: #d1d5db;
        border-bottom: 1px solid #2c2c2c;
    }

    body.dark .custom-table tbody tr:hover {
        background-color: #2a2a2a;
    }

    body.dark .score-cell {
        color: #60a5fa;
    }

    body.dark .breadcrumb-item a {
        color: #9ca3af;
    }

    body.dark .breadcrumb-item.active {
        color: #ffffff;
    }

    body.dark .empty-state {
        color: #9ca3af;
    }
</style>

<div class="container-fluid px-4">

    <!-- ===== HEADER ===== -->

    <div class="mb-4 mt-4">

        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item">
                    <a href="dashboard">
                        <i class="bx bx-home-alt"></i>
                    </a>
                </li>
                <li class="breadcrumb-item">
                    <a href="grade?action=student-courses">My Courses</a>
                </li>
                <li class="breadcrumb-item active" aria-current="page">
                    Course Grades
                </li>
            </ol>
        </div>

        <div class="content-header d-flex justify-content-between align-items-center">

            <div>
                <h2 class="page-title">Grade Overview</h2>
                <p class="text-muted small mb-0">
                    View your academic performance and assessment results.
                </p>
            </div>

            <!-- ===== BACK BUTTON ===== -->
            <!--            <div>
                            <a href="user" class="btn-secondary">
                                <i class='bx bx-arrow-left'></i> Back to Users
                            </a>
                        </div>-->

        </div>

    </div>

    <!-- ===== CONTENT CARD ===== -->

    <div class="profile-content-card">

        <div class="section-title">
            <i class='bx bx-bar-chart-alt-2'></i>
            Assessment Results
        </div>

        <c:choose>

            <c:when test="${not empty gradeList}">

                <!-- ===== COURSE INFO ===== -->

                <div class="course-info mb-3">
                    <h5 class="mb-1">
                        ${gradeList[0].enrollment.classes.course.courseName}
                    </h5>
                    <p class="text-muted small mb-3">
                        Class: ${gradeList[0].enrollment.classes.className}
                    </p>
                </div>

                <!-- ===== TABLE ===== -->

                <table class="custom-table">

                    <thead>
                        <tr>
                            <th>Assessment</th>
                            <th>Score</th>
                        </tr>
                    </thead>

                    <tbody>

                        <!-- ===== CALCULATE TOTAL ===== -->
                        <c:set var="total" value="0" />
                        <c:set var="count" value="0" />

                        <c:forEach items="${gradeList}" var="g">

                            <c:set var="total" value="${total + g.score}" />
                            <c:set var="count" value="${count + 1}" />

                            <tr>
                                <td>${g.assessment.assessmentName}</td>
                                <td class="score-cell">${g.score}</td>
                            </tr>

                        </c:forEach>

                        <!-- ===== AVERAGE ROW ===== -->
                        <tr class="average-row">
                            <td><strong>Average</strong></td>
                            <td colspan="2" class="score-cell">
                                <strong>
                                    <c:if test="${count > 0}">
                                        <c:set var="avg" value="${total / count}" />
                                        <fmt:formatNumber 
                                            value="${avg}" 
                                            type="number" 
                                            minFractionDigits="2" 
                                            maxFractionDigits="2"/>
                                    </c:if>
                                </strong>
                            </td>
                        </tr>

                    </tbody>

                </table>

            </c:when>

            <c:otherwise>

                <div class="empty-state">
                    <i class='bx bx-info-circle'></i>
                    <p>No grades available at the moment.</p>
                </div>

            </c:otherwise>

        </c:choose>

    </div>

</div>