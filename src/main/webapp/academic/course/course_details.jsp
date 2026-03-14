<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link href="css/course_details.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4 content-body course-detail-page">
    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
                <li class="breadcrumb-item"><a href="?action=all">Course Management</a></li>
                <li class="breadcrumb-item active" aria-current="page">Course Details</li>
            </ol>
        </div>

        <div class="content-header">
            <div>
                <h2 class="page-title">Course Details</h2>
                <p class="text-muted small mb-0">Overview and quick management actions</p>
            </div>
            <div class="detail-header-actions">
                <a href="?action=all" class="btn btn-outline-secondary">
                    <i class='bx bx-arrow-left'></i> Back
                </a>
                <c:if test="${not empty course}">
                    <a href="?action=edit&courseId=${course.courseId}" class="btn btn-primary">
                        <i class='bx bx-edit'></i> Edit Course
                    </a>
                </c:if>
            </div>
        </div>
    </div>

    <c:choose>
        <c:when test="${empty course}">
            <div class="detail-card empty-state">
                <i class='bx bx-book-open empty-icon'></i>
                <h4>Course not found</h4>
                <p class="mb-0">The requested course may have been deleted or is unavailable.</p>
            </div>
        </c:when>
        <c:otherwise>
            <div class="detail-card hero-card">
                <div class="hero-image-wrap">
                    <c:choose>
                        <c:when test="${not empty course.images}">
                            <img src="images/${course.images}" alt="${course.courseName}" class="hero-image">
                        </c:when>
                        <c:otherwise>
                            <div class="hero-image-fallback">
                                <i class='bx bx-book'></i>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

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

            <div class="detail-grid">
                <div class="detail-card section-card">
                    <div class="section-title">
                        <i class='bx bx-detail'></i>
                        <h5>Course Description</h5>
                    </div>
                    <p class="description-text">
                        <c:choose>
                            <c:when test="${not empty course.description}">
                                ${course.description}
                            </c:when>
                            <c:otherwise>
                                No description has been added for this course.
                            </c:otherwise>
                        </c:choose>
                    </p>
                </div>

                <div class="detail-card section-card">
                    <div class="section-title">
                        <i class='bx bx-cog'></i>
                        <h5>Quick Actions</h5>
                    </div>
                    <div class="quick-action-list">
                        <a href="?action=edit&courseId=${course.courseId}" class="quick-action">
                            <span><i class='bx bx-edit'></i> Update Information</span>
                            <i class='bx bx-chevron-right'></i>
                        </a>
                        <a href="?action=all" class="quick-action">
                            <span><i class='bx bx-list-ul'></i> Back to Course List</span>
                            <i class='bx bx-chevron-right'></i>
                        </a>
                    </div>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>
