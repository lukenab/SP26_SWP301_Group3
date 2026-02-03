<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${course.courseName} - Details</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/course_list.css">
    <style>
        .course-detail-container {
            max-width: 800px;
            margin: 20px auto;
            padding: 20px;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
        
        .course-detail-header {
            text-align: center;
            margin-bottom: 20px;
        }
        
        .course-detail-image {
            width: 100%;
            max-height: 300px;
            object-fit: cover;
            border-radius: 8px;
            margin-bottom: 20px;
        }
        
        .course-detail-info {
            margin: 15px 0;
        }
        
        .course-detail-label {
            font-weight: bold;
            color: #555;
            display: inline-block;
            width: 100px;
        }
        
        .course-detail-value {
            color: #333;
        }
        
        .back-link {
            display: inline-block;
            margin-top: 20px;
            padding: 10px 15px;
            background-color: #3498db;
            color: white;
            text-decoration: none;
            border-radius: 4px;
        }
        
        .back-link:hover {
            background-color: #2980b9;
        }
    </style>
</head>
<body>
    <div class="container">
        <c:choose>
            <c:when test="${empty course}">
                <div class="no-courses">Course not found.</div>
            </c:when>
            <c:otherwise>
                <div class="course-detail-container">
                    <div class="course-detail-header">
                        <h1>${course.courseName}</h1>
                    </div>
                    
                    <c:if test="${not empty course.images}">
                        <img src="${course.images}" alt="${course.courseName}" class="course-detail-image">
                    </c:if>
                    
                    <div class="course-detail-info">
                        <div class="detail-item">
                            <span class="course-detail-label">Description:</span>
                            <span class="course-detail-value">${course.description}</span>
                        </div>
                    </div>
                    
                    <div class="course-detail-info">
                        <div class="detail-item">
                            <span class="course-detail-label">Slots:</span>
                            <span class="course-detail-value">${course.totalSlots}</span>
                        </div>
                    </div>
                    
                    <div class="course-detail-info">
                        <div class="detail-item">
                            <span class="course-detail-label">Fee:</span>
                            <span class="course-detail-value">$${course.tuitionFee}</span>
                        </div>
                    </div>
                    
                    <div class="course-detail-info">
                        <div class="detail-item">
                            <span class="course-detail-label">Status:</span>
                            <span class="status ${course.status ? 'active' : 'inactive'}">
                                ${course.status ? 'Active' : 'Inactive'}
                            </span>
                        </div>
                    </div>
                    
                    <a href="${pageContext.request.contextPath}/course" class="back-link">Back to Course List</a>
                    <a href="${pageContext.request.contextPath}/" class="back-link" style="margin-left: 10px;">Back to Home</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>