<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="en_US"/>

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
</style>

<div class="container-fluid student-course-container">

    <!-- HEADER -->
    <div class="mb-4 mt-4">

        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item">
                    <a href="dashboard">
                        <i class="bx bx-home-alt"></i>
                    </a>
                </li>
                <li class="breadcrumb-item active">My Courses</li>
            </ol>
        </div>

        <div>
            <h2 class="fw-bold mb-1">My Courses</h2>
            <p class="text-muted small mb-0">
                View all enrolled courses and track your academic progress.
            </p>
        </div>

    </div>

    <!-- SEARCH + FILTER -->
    <form action="grade" method="get" class="row mb-4">

        <input type="hidden" name="action" value="student-courses"/>

        <div class="col-md-10">
            <input type="text"
                   name="keyword"
                   class="form-control"
                   placeholder="Search course..."
                   value="${keyword}">
        </div>

        <!--        <div class="col-md-3">
                    <select name="status" class="form-control">
        
                        <option value="">All Status</option>
        
                        <option value="true"
        ${status == true ? 'selected' : ''}>
    Active
</option>

<option value="false"
        ${status == false ? 'selected' : ''}>
    Inactive
</option>

</select>
</div>-->

        <div class="col-md-2">
            <button type="submit"
                    class="btn btn-primary w-100 d-flex justify-content-center align-items-center">
                Filter
            </button>
        </div>

    </form>

    <!-- COURSE LIST -->
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
                        No courses found.
                    </div>
                </div>

            </c:otherwise>

        </c:choose>

    </div>

    <!-- PAGINATION -->
    <div class="d-flex justify-content-center mt-4">

        <c:if test="${currentPage > 1}">
            <c:url var="prevUrl" value="grade">
                <c:param name="action" value="student-courses"/>
                <c:param name="page" value="${currentPage - 1}"/>
                <c:param name="keyword" value="${keyword}"/>
                <c:param name="status" value="${status}"/>
            </c:url>

            <a href="${prevUrl}" class="btn btn-outline-primary me-2">
                Previous
            </a>
        </c:if>

        <span class="align-self-center">Page ${currentPage}</span>

        <c:if test="${courseList.size() == 6}">
            <c:url var="nextUrl" value="grade">
                <c:param name="action" value="student-courses"/>
                <c:param name="page" value="${currentPage + 1}"/>
                <c:param name="keyword" value="${keyword}"/>
                <c:param name="status" value="${status}"/>
            </c:url>

            <a href="${nextUrl}" class="btn btn-outline-primary ms-2">
                Next
            </a>
        </c:if>

    </div>

</div>