<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<style>
    .student-course-container {
        padding: 20px 10px;
    }

    .course-card {
        border-radius: 18px;
        transition: all 0.3s ease-in-out;
        border: none;
    }

    .course-card:hover {
        transform: translateY(-8px);
        box-shadow: 0 15px 30px rgba(0,0,0,0.12);
    }

    .course-title {
        font-size: 1.2rem;
        font-weight: 600;
        margin-bottom: 15px;
    }

    .score-label {
        font-size: 13px;
        color: #6c757d;
        margin-bottom: 10px;
    }

    .score-circle {
        width: 85px;
        height: 85px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-weight: 700;
        font-size: 18px;
        color: white;
    }

    .circle-good {
        background-color: #286aa7;
    }

    .circle-bad {
        background-color: #dc3545;
    }

    .circle-na {
        background-color: #6c757d;
    }

    .view-btn {
        border-radius: 50px;
        font-weight: 500;
        padding: 8px 0;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .empty-box {
        padding: 40px;
        border-radius: 12px;
    }
    /* ================= DARK MODE ================= */

    body.dark .student-course-container {
        background-color: #121212;
    }

    body.dark .course-card {
        background-color: #1e1e1e;
        box-shadow: 0 10px 25px rgba(0,0,0,0.6);
    }

    body.dark .course-title,
    body.dark h2 {
        color: #ffffff;
    }

    body.dark .score-label,
    body.dark .text-muted {
        color: #b0b3b8 !important;
    }

    body.dark .btn-outline-primary {
        color: #60a5fa;
        border-color: #60a5fa;
    }

    body.dark .btn-outline-primary:hover {
        background-color: #60a5fa;
        color: #000;
    }

    body.dark .alert-info {
        background-color: #1f2937;
        border-color: #374151;
        color: #e5e7eb;
    }

    body.dark .breadcrumb-item a {
        color: #9ca3af;
    }

    body.dark .breadcrumb-item.active {
        color: #ffffff;
    }

</style>

<div class="container-fluid student-course-container">

    <div class="mb-4 mt-4">

        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item">
                    <a href="dashboard">
                        <i class="bx bx-home-alt"></i>
                    </a>
                </li>
                <li class="breadcrumb-item active" aria-current="page">
                    My Courses
                </li>
            </ol>
        </div>

        <div>
            <h2 class="fw-bold mb-1">My Courses</h2>
            <p class="text-muted small mb-0">
                View all enrolled courses and track your academic progress.
            </p>
        </div>

    </div>

    <div class="row g-4">

        <c:choose>

            <c:when test="${not empty courseList}">

                <c:forEach var="c" items="${courseList}">

                    <div class="col-lg-4 col-md-6 col-sm-12">

                        <div class="card course-card shadow-sm h-100">

                            <div class="card-body d-flex flex-column justify-content-between">

                                <div>

                                    <div class="course-title">
                                        ${c.courseName}
                                    </div>

                                    <div class="mb-4">
                                        <div class="score-label">
                                            Average Score
                                        </div>

                                        <c:choose>

                                            <c:when test="${averageMap[c.courseId] != null}">

                                                <c:set var="avg" value="${averageMap[c.courseId]}" />

                                                <c:choose>

                                                    <c:when test="${avg > 7}">
                                                        <div class="score-circle circle-good">
                                                            <fmt:formatNumber 
                                                                value="${avg}" 
                                                                type="number" 
                                                                minFractionDigits="2" 
                                                                maxFractionDigits="2"/>
                                                        </div>
                                                    </c:when>

                                                    <c:otherwise>
                                                        <div class="score-circle circle-bad">
                                                            <fmt:formatNumber 
                                                                value="${avg}" 
                                                                type="number" 
                                                                minFractionDigits="2" 
                                                                maxFractionDigits="2"/>
                                                        </div>
                                                    </c:otherwise>

                                                </c:choose>

                                            </c:when>

                                            <c:otherwise>
                                                <div class="score-circle circle-na">
                                                    N/A
                                                </div>
                                            </c:otherwise>

                                        </c:choose>

                                    </div>
                                </div>

                                <a href="grade?action=student-course-grades&courseId=${c.courseId}" 
                                   class="btn btn-outline-primary view-btn w-100">
                                    View Grades
                                </a>

                            </div>

                        </div>

                    </div>

                </c:forEach>

            </c:when>

            <c:otherwise>

                <div class="col-12">
                    <div class="alert alert-info text-center empty-box shadow-sm">
                        You are not enrolled in any courses.
                    </div>
                </div>

            </c:otherwise>

        </c:choose>

    </div>

</div>