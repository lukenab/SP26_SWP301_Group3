<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${pageTitle} - Course Management</title>
    <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link href="css/manageUser.css" rel="stylesheet" type="text/css"/>
    <style>
        .form-container {
            max-width: 800px;
            margin: 20px auto;
            padding: 20px;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
        
        .form-header {
            margin-bottom: 30px;
            padding-bottom: 15px;
            border-bottom: 1px solid #eee;
        }
        
        .form-group {
            margin-bottom: 20px;
        }
        
        .form-label {
            font-weight: 600;
            color: #333;
            margin-bottom: 8px;
            display: block;
        }
        
        .form-control {
            width: 100%;
            padding: 10px 15px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
            transition: border-color 0.3s;
        }
        
        .form-control:focus {
            outline: none;
            border-color: #3498db;
            box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1);
        }
        
        .form-row {
            display: flex;
            gap: 20px;
            margin-bottom: 20px;
        }
        
        .form-col {
            flex: 1;
        }
        
        .btn-submit {
            background-color: #3498db;
            color: white;
            padding: 12px 25px;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: background-color 0.3s;
            margin-right: 10px;
        }
        
        .btn-submit:hover {
            background-color: #2980b9;
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
            display: inline-block;
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
        
        .success-message {
            background-color: #d4edda;
            color: #155724;
            padding: 12px;
            border-radius: 4px;
            margin-bottom: 20px;
            border: 1px solid #c3e6cb;
        }
        
        textarea.form-control {
            min-height: 120px;
            resize: vertical;
        }
        
        .checkbox-group {
            display: flex;
            align-items: center;
            gap: 10px;
        }
        
        .checkbox-group input[type="checkbox"] {
            width: 18px;
            height: 18px;
        }
    </style>
</head>
<body>
    <div class="container-fluid px-4 content-body">
        <div class="form-container">
            <div class="form-header">
                <h2>${pageTitle}</h2>
                <p class="text-muted">Fill in the course details below</p>
            </div>
            
            <c:if test="${not empty errorMessage}">
                <div class="error-message">
                    ${errorMessage}
                </div>
            </c:if>
            
            <c:if test="${not empty param.message}">
                <div class="success-message">
                    ${param.message}
                </div>
            </c:if>
            
            <form action="course" method="post">
                <input type="hidden" name="action" value="${formAction}">
                <c:if test="${formAction eq 'update'}">
                    <input type="hidden" name="courseId" value="${course.courseId}">
                </c:if>
                
                <div class="form-group">
                    <label class="form-label" for="courseName">Course Name *</label>
                    <input type="text" id="courseName" name="courseName" class="form-control" 
                           value="${course.courseName}" required>
                </div>
                
                <div class="form-group">
                    <label class="form-label" for="description">Description</label>
                    <textarea id="description" name="description" class="form-control" 
                              placeholder="Enter course description...">${course.description}</textarea>
                </div>
                
                <div class="form-row">
                    <div class="form-col">
                        <div class="form-group">
                            <label class="form-label" for="totalSlots">Total Slots *</label>
                            <input type="number" id="totalSlots" name="totalSlots" class="form-control" 
                                   value="${course.totalSlots}" min="1" required>
                        </div>
                    </div>
                    <div class="form-col">
                        <div class="form-group">
                            <label class="form-label" for="tuitionFee">Tuition Fee *</label>
                            <input type="number" id="tuitionFee" name="tuitionFee" class="form-control" 
                                   value="${course.tuitionFee}" step="0.01" min="0" required>
                        </div>
                    </div>
                </div>
                
                <div class="form-row">
                    <div class="form-col">
                        <div class="form-group">
                            <label class="form-label" for="images">Image URL</label>
                            <input type="url" id="images" name="images" class="form-control" 
                                   value="${course.images}" placeholder="https://example.com/image.jpg">
                        </div>
                    </div>
                    <div class="form-col">
                        <div class="form-group">
                            <label class="form-label">Status</label>
                            <div class="checkbox-group">
                                <input type="checkbox" id="status" name="status" 
                                       ${course.status ? 'checked' : ''} value="true">
                                <label for="status">Active Course</label>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="form-group" style="margin-top: 30px;">
                    <button type="submit" class="btn-submit">
                        <c:choose>
                            <c:when test="${formAction eq 'add'}">Add Course</c:when>
                            <c:when test="${formAction eq 'update'}">Update Course</c:when>
                        </c:choose>
                    </button>
                    <a href="course?action=all" class="btn-cancel">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>