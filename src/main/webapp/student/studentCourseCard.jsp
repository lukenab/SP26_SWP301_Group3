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
        margin-bottom: 10px;
    }

    .score-label {
        font-size: 13px;
        color: #6c757d;
        margin-bottom: 8px;
    }

    .score-circle {
        width: 75px;
        height: 75px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-weight: 700;
        font-size: 16px;
        color: white;
        background-color: #286aa7;
    }

    .view-btn {
        border-radius: 50px;
        font-weight: 500;
        padding: 8px 0;

        display: flex;
        justify-content: center;
        align-items: center;
        text-align: center;
    }
</style>

<div class="container-fluid student-course-container">

    <!-- HEADER -->
    <div class="mb-4 mt-4">
        <ol class="breadcrumb mb-1">
            <li class="breadcrumb-item">
                <a href="dashboard"><i class="bx bx-home-alt"></i></a>
            </li>
            <li class="breadcrumb-item active">My Grade</li>
        </ol>

        <h2 class="fw-bold mb-1">My Grades</h2>
        <p class="text-muted small">
            View your enrolled classes and final grades.
        </p>
    </div>

    <!-- SEARCH -->
    <form action="grade" method="get" class="row mb-4 g-2">
        <input type="hidden" name="action" value="student-courses"/>

        <div class="col-md-10">
            <input type="text"
                   name="keyword"
                   value="${keyword}"
                   class="form-control"
                   placeholder="Search class / course / teacher">
        </div>

        <div class="col-md-2">
            <button type="submit" class="btn btn-primary w-100">
                Search
            </button>
        </div>
    </form>

    <!-- LIST -->
    <div class="row g-4">

        <c:choose>

            <c:when test="${not empty classList}">

                <c:forEach var="row" items="${classList}">
                    <c:set var="e" value="${row[0]}"/>

                    <div class="col-lg-4 col-md-6">

                        <div class="card course-card shadow-sm h-100">
                            <div class="card-body d-flex flex-column justify-content-between">

                                <!-- CLASS NAME -->
                                <div class="course-title">
                                    ${e.classes.className}
                                </div>

                                <!-- COURSE -->
                                <p class="text-muted mb-2">
                                    ${e.classes.course.courseName}
                                </p>

                                <!-- TEACHER -->
                                <p>
                                    <i class="bx bx-user"></i> ${row[1]}
                                </p>

                                <c:if test="${(e.status eq 'Active' || e.status eq 'Completed') && e.finalGrade ne -1}">
                                    <div class="mb-3">
                                        <div class="score-label">Final Grade</div>

                                        <div class="score-circle">
                                            <fmt:formatNumber value="${e.finalGrade}" pattern="#0.00"/>
                                        </div>
                                    </div>
                                </c:if>

                                <!-- BUTTON -->
                                <a href="grade?action=student-course-grades&classId=${e.classes.classid}"
                                   class="btn btn-outline-primary view-btn w-100">
                                    View Grade
                                </a>

                            </div>
                        </div>

                    </div>

                </c:forEach>

            </c:when>

            <c:otherwise>
                <div class="col-12 text-center mt-5">
                    <h5 class="text-muted">No classes found</h5>
                </div>
            </c:otherwise>

        </c:choose>

    </div>

    <!-- PAGINATION -->
    <div class="d-flex justify-content-center mt-4">

        <c:if test="${currentPage > 1}">
            <a href="grade?action=student-courses&page=${currentPage - 1}&keyword=${keyword}&status=${status}"
               class="btn btn-outline-primary me-2">
                Previous
            </a>
        </c:if>

        <span class="align-self-center fw-bold">
            Page ${currentPage} / ${totalPages}
        </span>

        <c:if test="${currentPage < totalPages}">
            <a href="grade?action=student-courses&page=${currentPage + 1}&keyword=${keyword}&status=${status}"
               class="btn btn-outline-primary ms-2">
                Next
            </a>
        </c:if>

    </div>

</div>