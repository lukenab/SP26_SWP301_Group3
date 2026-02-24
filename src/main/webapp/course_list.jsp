<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Course Management</title>
    <link href='https://cdn.boxicons.com/3.0.6/fonts/basic/boxicons.min.css' rel='stylesheet'>
    <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link href="css/manageUser.css" rel="stylesheet" type="text/css"/>
    <link href="css/course_management.css" rel="stylesheet" type="text/css"/>
</head>
<body>
    <div class="container-fluid px-4 content-body">
        <!-- Header Section -->
        <div class="course-header">
            <div style="display: flex; justify-content: space-between; align-items: center;">
                <div>
                    <h2 class="course-title">Course Management</h2>
                    <p class="course-subtitle">Manage all courses in the system</p>
                </div>
                <div class="course-actions">
                    <a href="course?action=add" class="btn-course-action btn-add-course">
                        <i class='bx bx-plus'></i> Add New Course
                    </a>
                </div>
            </div>
        </div>

        <!-- Search and Filter Section -->
        <div class="search-container">
            <form action="course" method="get" class="search-form">
                <input type="hidden" name="action" value="search">
                <input type="text" name="keyword" class="search-input" 
                       placeholder="Search courses by name or description..." 
                       value="${searchKeyword}">
                <button type="submit" class="search-btn">
                    <i class='bx bx-search'></i>
                </button>
            </form>
            
            <div class="filter-tabs">
                <a href="course?action=all" class="filter-tab ${param.action eq 'all' || empty param.action ? 'active' : ''}">
                    All Courses
                </a>
                <a href="course?action=active" class="filter-tab ${param.action eq 'active' ? 'active' : ''}">
                    Active Only
                </a>
            </div>
        </div>

        <!-- Stats Section -->
        <div class="stats-container">
            <div class="stat-card">
                <div class="stat-value">${courseList.size()}</div>
                <div class="stat-label">Total Courses</div>
            </div>
            <c:set var="activeCount" value="0"/>
            <c:forEach var="course" items="${courseList}">
                <c:if test="${course.status}"><c:set var="activeCount" value="${activeCount + 1}"/></c:if>
            </c:forEach>
            <div class="stat-card">
                <div class="stat-value">${activeCount}</div>
                <div class="stat-label">Active Courses</div>
            </div>
            <c:set var="inactiveCount" value="${courseList.size() - activeCount}"/>
            <div class="stat-card">
                <div class="stat-value">${inactiveCount}</div>
                <div class="stat-label">Inactive Courses</div>
            </div>
        </div>

        <!-- Success/Error Messages -->
        <c:if test="${not empty param.message}">
            <div class="alert alert-success alert-dismissible fade show" style="margin: 20px;">
                ${param.message}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        
        <c:if test="${not empty param.error}">
            <div class="alert alert-danger alert-dismissible fade show" style="margin: 20px;">
                ${param.error}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <!-- Course Table -->
        <div class="course-management-container">
            <c:choose>
                <c:when test="${empty courseList}">
                    <div class="empty-state">
                        <div class="empty-state-icon">
                            <i class='bx bx-book'></i>
                        </div>
                        <h3 class="empty-state-title">No Courses Found</h3>
                        <p class="empty-state-message">
                            <c:choose>
                                <c:when test="${not empty searchKeyword}">
                                    No courses match your search criteria. Try different keywords.
                                </c:when>
                                <c:otherwise>
                                    There are no courses in the system yet. Add your first course to get started.
                                </c:otherwise>
                            </c:choose>
                        </p>
                        <a href="course?action=add" class="btn-course-action btn-add-course">
                            <i class='bx bx-plus'></i> Add First Course
                        </a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table class="course-table">
                            <thead>
                                <tr>
                                    <th>Image</th>
                                    <th>Course Name</th>
                                    <th>Description</th>
                                    <th>Slots</th>
                                    <th>Fee</th>
                                    <th>Status</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="course" items="${courseList}">
                                    <tr>
                                        <td class="course-image-cell">
                                            <c:choose>
                                                <c:when test="${not empty course.images}">
                                                    <img src="${course.images}" alt="${course.courseName}" class="course-image">
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="course-image" style="background-color: #f0f0f0; display: flex; align-items: center; justify-content: center; color: #6c757d; font-size: 0.8rem;">
                                                        No Image
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="course-name-cell">${course.courseName}</td>
                                        <td class="course-description-cell" title="${course.description}">
                                            ${course.description}
                                        </td>
                                        <td class="course-slots-cell">${course.totalSlots}</td>
                                        <td class="course-fee-cell">$${course.tuitionFee}</td>
                                        <td class="course-status-cell">
                                            <span class="status-badge ${course.status ? 'status-active' : 'status-inactive'}">
                                                ${course.status ? 'Active' : 'Inactive'}
                                            </span>
                                        </td>
                                        <td class="course-actions-cell">
                                            <a href="course?action=details&courseId=${course.courseId}" 
                                               class="action-btn action-btn-view" title="View Details">
                                                <i class='bx bx-show'></i>
                                            </a>
                                            <a href="course?action=edit&courseId=${course.courseId}" 
                                               class="action-btn action-btn-edit" title="Edit Course">
                                                <i class='bx bx-edit'></i>
                                            </a>
                                            <a href="course?action=delete&courseId=${course.courseId}" 
                                               class="action-btn action-btn-delete" title="Delete Course"
                                               onclick="return confirm('Are you sure you want to delete this course?')">
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
    </div>
</body>
</html>