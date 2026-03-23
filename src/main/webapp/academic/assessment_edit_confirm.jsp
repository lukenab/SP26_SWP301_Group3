<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link href="css/createUser.css" rel="stylesheet" type="text/css"/>

<div class="mb-4">
    <div class="content-header">
        <div>
            <h1 class="page-title">Edit Assessment</h1>
        </div>
        <a href="course?action=assessment&courseId=${course.courseId}" class="btn-secondary">
            <i class='bx bx-arrow-left'></i> Back to Assessment
        </a>
    </div>
    <div aria-label="breadcrumb">
        <ol class="breadcrumb mb-1">
            <li class="breadcrumb-item"><a href="dashboard?action=academic">Dashboard</a></li>
            <li class="breadcrumb-item"><a href="course?action=all">Course Management</a></li>
            <li class="breadcrumb-item"><a href="course?action=assessment&courseId=${course.courseId}">Assessment Management</a></li>
            <li class="breadcrumb-item active" aria-current="page">Edit Assessment</li>
        </ol>
    </div>
</div>

<div class="form-container">

    <p class="form-title">Assessment Information</p>

    <c:if test="${not empty param.error}">
        <div class="error-message">
            ${param.error}
        </div>
    </c:if>

    <c:choose>
        <c:when test="${not empty assessment}">
            <form action="assessment" method="post" class="form-body">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="assessmentId" value="${assessment.assessmentId}">
                <input type="hidden" name="courseId" value="${course.courseId}">

                <div class="form-row">
                    <div class="form-group">
                        <label for="courseName">Course Name</label>
                        <input type="text" id="courseName" value="${course.courseName}" readonly>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group" style="width:100%">
                        <label for="assessmentName">Assessment Name <span style="color: red;">*</span></label>
                        <input type="text" id="assessmentName" name="assessmentName" value="${assessment.assessmentName}" required>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group" style="width:100%">
                        <label for="weight">Weight (%) <span style="color: red;">*</span></label>
                        <input type="number" id="weight" name="weight" value="${assessment.weight}" min="0" max="100" step="0.1" required>
                    </div>
                </div>

                <div class="form-buttons">
                    <a href="course?action=assessment&courseId=${course.courseId}" class="btn btn-cancel">Cancel</a>
                    <button type="submit" class="btn btn-primary">
                        <i class='bx bx-check'></i> Save Changes
                    </button>
                </div>
            </form>
        </c:when>

        <c:otherwise>
            <div class="error-message">
                <i class='bx bx-error-circle'></i> Assessment not found
            </div>
            <a href="course?action=assessment&courseId=${course.courseId}" class="btn btn-cancel">
                Back to Assessment
            </a>
        </c:otherwise>
    </c:choose>

</div>

