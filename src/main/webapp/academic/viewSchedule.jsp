<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link href="css/manageUser.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4 content-body">
    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="dashboard">Dashboard</a></li>
                <li class="breadcrumb-item"><a href="schedule?action=manage">Manage Schedule</a></li>
                <li class="breadcrumb-item active">Schedule Details</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">Schedule Details</h2>
                <p class="text-muted small mb-0">View schedule information</p>
            </div>
        </div>
    </div>

    <div class="card border-0 bg-white">
        <div class="card-body p-4">
            <div class="row">
                <div class="col-md-6 mb-4">
                    <label class="form-label fw-bold text-muted small">Schedule ID</label>
                    <p class="fs-5">${schedule.scheduleId}</p>
                </div>

                <div class="col-md-6 mb-4">
                    <label class="form-label fw-bold text-muted small">Class</label>
                    <p class="fs-5">${schedule.classes.className}</p>
                </div>

                <div class="col-md-6 mb-4">
                    <label class="form-label fw-bold text-muted small">Room</label>
                    <p class="fs-5">${schedule.room.roomName}</p>
                </div>

                <div class="col-md-6 mb-4">
                    <label class="form-label fw-bold text-muted small">Slot</label>
                    <p class="fs-5">Slot ${schedule.slot.slotID} (${schedule.slot.startTime} - ${schedule.slot.endTime})</p>
                </div>

                <div class="col-md-6 mb-4">
                    <label class="form-label fw-bold text-muted small">Learning Date</label>
                    <fmt:formatDate value="${schedule.learningDate}" pattern="EEEE, dd MMMM yyyy" var="formattedDate"/>
                    <p class="fs-5">${formattedDate}</p>
                </div>

                <div class="col-md-6 mb-4">
                    <label class="form-label fw-bold text-muted small">Attendance Status</label>
                    <p class="fs-5">
                        <c:choose>
                            <c:when test="${schedule.attendanceStatus}">
                                <span class="badge bg-success fs-6">Taken</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-secondary fs-6">Pending</span>
                            </c:otherwise>
                        </c:choose>
                    </p>
                </div>
            </div>

            <div class="d-flex gap-2 mt-4">
                <a href="schedule?action=editForm&scheduleId=${schedule.scheduleId}" class="btn btn-warning text-white">
                    <i class='bx bx-edit'></i> Edit Schedule
                </a>
                <a href="schedule?action=manage&classId=${sessionScope.selectedClassId != null ? sessionScope.selectedClassId : 0}&date=${sessionScope.selectedDate != null ? sessionScope.selectedDate : ''}" class="btn btn-secondary">
                    <i class='bx bx-arrow-back'></i> Back to List
                </a>
            </div>
        </div>
    </div>
</div>

<script src="js/manageUser.js" type="text/javascript"></script>

