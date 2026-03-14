<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link href="css/createUser.css" rel="stylesheet" type="text/css"/>

<div class="mb-4">
    <div class="content-header">
        <div>
            <h1 class="page-title">Deactivate Course</h1>
        </div>
        <a href="?action=all" class="btn-secondary">
            <i class='bx bx-arrow-left'></i> Back to Courses
        </a>
    </div>
    <div aria-label="breadcrumb">
        <ol class="breadcrumb mb-1">
            <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
            <li class="breadcrumb-item"><a href="">Course Management</a></li>
            <li class="breadcrumb-item active" aria-current="page">Deactivate Course</li>
        </ol>
    </div>
</div>

<div class="form-container">

    <p class="form-title">Course Information</p>

    <c:if test="${not empty param.error}">
        <div class="error-message">
            ${param.error}
        </div>
    </c:if>

    <c:choose>
        <c:when test="${course.status}">
            <form action="" method="post" class="form-body">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="courseId" value="${course.courseId}">

                <div class="form-row">
                    <div class="form-group">
                        <label for="courseName">Course Name</label>
                        <input type="text" id="courseName" name="courseName" value="${course.courseName}" readonly>
                    </div>

                    <div class="form-group">
                        <label for="totalSlots">Total Slots</label>
                        <input type="number" id="totalSlots" name="totalSlots" value="${course.totalSlots}" readonly>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="tuitionFee">Tuition Fee</label>
                        <input type="text" id="tuitionFee" name="tuitionFee" value="${course.tuitionFee}" readonly>
                    </div>

                    <div class="form-group">
                        <label for="status">Status</label>
                        <input type="text" id="status" value="${course.status ? 'Active' : 'Inactive'}" readonly>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group" style="width:100%">
                        <label for="description">Description</label>
                        <input type="text" id="description" name="description" value="${course.description}" readonly>
                    </div>
                </div>

                <div class="form-buttons">
                    <a href="?action=all" class="btn btn-cancel">Cancel</a>
                    <button type="submit" class="btn btn-lock">
                        <i class='bx bx-lock'></i> Inactivate Course
                    </button>
                </div>
            </form>
        </c:when>

        <c:otherwise>
            <form action="" method="post" class="form-body">
                <input type="hidden" name="action" value="activate">
                <input type="hidden" name="courseId" value="${course.courseId}">

                <div class="form-row">
                    <div class="form-group">
                        <label for="courseName">Course Name</label>
                        <input type="text" id="courseName" name="courseName" value="${course.courseName}" readonly>
                    </div>

                    <div class="form-group">
                        <label for="totalSlots">Total Slots</label>
                        <input type="number" id="totalSlots" name="totalSlots" value="${course.totalSlots}" readonly>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="tuitionFee">Tuition Fee</label>
                        <input type="text" id="tuitionFee" name="tuitionFee" value="${course.tuitionFee}" readonly>
                    </div>

                    <div class="form-group">
                        <label for="status">Status</label>
                        <input type="text" id="status" value="${course.status ? 'Active' : 'Inactive'}" readonly>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group" style="width:100%">
                        <label for="description">Description</label>
                        <input type="text" id="description" name="description" value="${course.description}" readonly>
                    </div>
                </div>

                <div class="form-buttons">
                    <a href="?action=all" class="btn btn-cancel">Cancel</a>
                    <button type="submit" class="btn btn-unlock">
                        <i class='bx bx-lock-open'></i> Activate Course
                    </button>
                </div>
            </form>
        </c:otherwise>
    </c:choose>
</div>
