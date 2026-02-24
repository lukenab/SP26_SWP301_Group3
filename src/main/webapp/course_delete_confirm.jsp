<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Confirm Delete Course</title>
    <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link href="css/manageUser.css" rel="stylesheet" type="text/css"/>
    <style>
        .delete-container {
            max-width: 600px;
            margin: 50px auto;
            padding: 30px;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            text-align: center;
        }
        
        .warning-icon {
            font-size: 48px;
            color: #e74c3c;
            margin-bottom: 20px;
        }
        
        .delete-title {
            color: #2c3e50;
            margin-bottom: 15px;
        }
        
        .delete-message {
            color: #7f8c8d;
            font-size: 16px;
            margin-bottom: 30px;
            line-height: 1.6;
        }
        
        .course-info {
            background-color: #f8f9fa;
            padding: 20px;
            border-radius: 6px;
            margin-bottom: 30px;
            text-align: left;
        }
        
        .info-item {
            margin-bottom: 10px;
            display: flex;
        }
        
        .info-label {
            font-weight: 600;
            width: 120px;
            color: #34495e;
        }
        
        .info-value {
            flex: 1;
            color: #2c3e50;
        }
        
        .btn-container {
            display: flex;
            justify-content: center;
            gap: 15px;
            margin-top: 30px;
        }
        
        .btn-delete {
            background-color: #e74c3c;
            color: white;
            padding: 12px 25px;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: background-color 0.3s;
        }
        
        .btn-delete:hover {
            background-color: #c0392b;
        }
        
        .btn-cancel {
            background-color: #95a5a6;
            color: white;
            padding: 12px 25px;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: background-color 0.3s;
            text-decoration: none;
        }
        
        .btn-cancel:hover {
            background-color: #7f8c8d;
        }
        
        .error-message {
            background-color: #f8d7da;
            color: #721c24;
            padding: 12px;
            border-radius: 4px;
            margin-bottom: 20px;
            border: 1px solid #f5c6cb;
        }
    </style>
</head>
<body>
    <div class="container-fluid px-4 content-body">
        <div class="delete-container">
            <div class="warning-icon">
                <i class="bx bx-error-circle"></i>
            </div>
            
            <h2 class="delete-title">Confirm Course Inactivation</h2>
            
            <c:if test="${not empty param.error}">
                <div class="error-message">
                    ${param.error}
                </div>
            </c:if>
            
            <div class="delete-message">
                Are you sure you want to inactivate this course? The course will be marked as inactive and will no longer appear in active course listings, but all data will be preserved.
            </div>
            
            <div class="course-info">
                <div class="info-item">
                    <span class="info-label">Course ID:</span>
                    <span class="info-value">${course.courseId}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">Course Name:</span>
                    <span class="info-value">${course.courseName}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">Description:</span>
                    <span class="info-value">${course.description}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">Total Slots:</span>
                    <span class="info-value">${course.totalSlots}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">Tuition Fee:</span>
                    <span class="info-value">$${course.tuitionFee}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">Status:</span>
                    <span class="info-value">
                        <c:choose>
                            <c:when test="${course.status}">
                                <span style="color: #27ae60; font-weight: 600;">Active</span>
                            </c:when>
                            <c:otherwise>
                                <span style="color: #e74c3c; font-weight: 600;">Inactive</span>
                            </c:otherwise>
                        </c:choose>
                    </span>
                </div>
            </div>
            
            <div class="btn-container">
                <form action="course" method="post" style="display: inline;">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="courseId" value="${course.courseId}">
                    <button type="submit" class="btn-delete">
                        <i class="bx bx-x-circle"></i> Inactivate Course
                    </button>
                </form>
                <a href="course?action=all" class="btn-cancel">
                    <i class="bx bx-x"></i> Cancel
                </a>
            </div>
        </div>
    </div>
</body>
</html>