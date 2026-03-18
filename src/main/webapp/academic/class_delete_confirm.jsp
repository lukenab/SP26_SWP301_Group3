<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link href="css/createUser.css" rel="stylesheet" type="text/css"/>

<c:set var="isActive" value="${classInfo[6] == 'Active'}"/>
<fmt:formatDate value="${classInfo[4]}" pattern="dd/MM/yyyy" var="startDateFormatted"/>
<fmt:formatDate value="${classInfo[5]}" pattern="dd/MM/yyyy" var="endDateFormatted"/>
<c:if test="${not empty classInfo[9]}">
    <fmt:formatDate value="${classInfo[9]}" pattern="dd/MM/yyyy" var="registrationDeadlineFormatted"/>
</c:if>
<c:choose>
    <c:when test="${not empty registrationDeadlineFormatted}">
        <c:set var="registrationPeriodDisplay" value="${registrationDeadlineFormatted}"/>
    </c:when>
    <c:otherwise>
        <c:set var="registrationPeriodDisplay" value="N/A"/>
    </c:otherwise>
</c:choose>

<div class="mb-4">
    <div class="content-header">
        <div>
            <h1 class="page-title">${isActive ? 'Deactivate Class' : 'Activate Class'}</h1>
        </div>
        <a href="enrollment?action=classes" class="btn-secondary">
            <i class='bx bx-arrow-left'></i> Back to Classes
        </a>
    </div>
    <div aria-label="breadcrumb">
        <ol class="breadcrumb mb-1">
            <li class="breadcrumb-item"><a href="dashboard?action=academic">Dashboard</a></li>
            <li class="breadcrumb-item"><a href="enrollment?action=classes">Class Management</a></li>
            <li class="breadcrumb-item active" aria-current="page">${isActive ? 'Deactivate Class' : 'Activate Class'}</li>
        </ol>
    </div>
</div>

<div class="form-container">
    <p class="form-title">Class Information</p>

    <c:if test="${not empty param.error}">
        <div class="error-message">
            ${param.error}
        </div>
    </c:if>

    <form action="enrollment" method="post" class="form-body">
        <input type="hidden" name="action" value="${isActive ? 'deactivateClass' : 'activateClass'}">
        <input type="hidden" name="classId" value="${classInfo[0]}">

        <div class="form-row">
            <div class="form-group">
                <label for="className">Class Name</label>
                <input type="text" id="className" value="${classInfo[1]}" readonly>
            </div>
            <div class="form-group">
                <label for="courseName">Course</label>
                <input type="text" id="courseName" value="${classInfo[2]}" readonly>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="teacherName">Teacher</label>
                <input type="text" id="teacherName" value="${empty classInfo[3] ? 'N/A' : classInfo[3]}" readonly>
            </div>
            <div class="form-group">
                <label for="status">Status</label>
                <input type="text" id="status" value="${classInfo[6]}" readonly>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="startDate">Start Date</label>
                <input type="text" id="startDate" value="${startDateFormatted}" readonly>
            </div>
            <div class="form-group">
                <label for="endDate">End Date</label>
                <input type="text" id="endDate" value="${endDateFormatted}" readonly>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="quantity">Quantity</label>
                <input type="text" id="quantity" value="${classInfo[7]}/${classInfo[8]}" readonly>
            </div>
            <div class="form-group">
                <label for="registrationDeadline">Registration Deadline</label>
                <input type="text" id="registrationDeadline" value="${registrationPeriodDisplay}" readonly>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group" style="width:100%">
                <label for="roomName">Room (Optional)</label>
                <input type="text" id="roomName" value="${empty classInfo[10] ? 'Not assigned yet' : classInfo[10]}" readonly>
            </div>
        </div>

        <div class="form-buttons">
            <a href="enrollment?action=classes" class="btn btn-cancel">Cancel</a>
            <button type="submit" class="btn ${isActive ? 'btn-lock' : 'btn-unlock'}">
                <i class='bx ${isActive ? 'bx-lock' : 'bx-lock-open'}'></i>
                ${isActive ? 'Inactivate Class' : 'Activate Class'}
            </button>
        </div>
    </form>
</div>
