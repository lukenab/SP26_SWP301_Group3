<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Course List</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/course_list.css">
</head>
<body>
    <div class="container">
        <h1>Course List</h1>
        
        <div class="course-filters">
            <a href="${pageContext.request.contextPath}/course?action=all" class="filter-btn ${param.action eq 'all' || empty param.action ? 'active' : ''}">All Courses</a>
            <a href="${pageContext.request.contextPath}/course?action=active" class="filter-btn ${param.action eq 'active' ? 'active' : ''}">Active Courses</a>
        </div>
        
        <c:choose>
            <c:when test="${empty courseList}">
                <div class="no-courses">No courses available at the moment.</div>
            </c:when>
            <c:otherwise>
                <div class="courses-grid">
                    <c:forEach var="course" items="${courseList}">
                        <div class="course-card">
                            <c:if test="${not empty course.images}">
                                <img src="${course.images}" alt="${course.courseName}" class="course-image">
                            </c:if>
                            <div class="course-info">
                                <h3 class="course-title">${course.courseName}</h3>
                                <p class="course-description">${course.description}</p>
                                
                                <div class="course-details">
                                    <div class="detail-item">
                                        <span class="label">Slots:</span>
                                        <span class="value">${course.totalSlots}</span>
                                    </div>
                                    <div class="detail-item">
                                        <span class="label">Fee:</span>
                                        <span class="value">$${course.tuitionFee}</span>
                                    </div>
                                    <div class="detail-item">
                                        <span class="label">Status:</span>
                                        <span class="status ${course.status ? 'active' : 'inactive'}">
                                            ${course.status ? 'Active' : 'Inactive'}
                                        </span>
                                    </div>
                                </div>
                                
                                <div class="course-actions">
                                    <a href="${pageContext.request.contextPath}/course?action=details&courseId=${course.courseId}" class="view-details-btn">View Details</a>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>