<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link href="css/manageUser.css" rel="stylesheet" type="text/css"/>
<link href="css/scheduleManagement.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4 content-body schedule-page">
    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="dashboard?action=academic">Dashboard</a></li>
                <li class="breadcrumb-item"><a href="schedule?action=manage">Manage Schedule</a></li>
                <li class="breadcrumb-item active">Schedule Details</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">Schedule Details</h2>
                <p class="text-muted small mb-0">View schedule information</p>
            </div>
            <a href="schedule?action=manage&classId=${sessionScope.selectedClassId != null ? sessionScope.selectedClassId : 0}&roomId=${sessionScope.selectedRoomId != null ? sessionScope.selectedRoomId : 0}&date=${sessionScope.selectedDate != null ? sessionScope.selectedDate : ''}" class="btn schedule-neutral-btn">
                <i class='bx bx-arrow-left'></i> Back to Schedule List
            </a>
        </div>
    </div>

    <div class="card user-table-card border-0 bg-white section-card">
        <div class="card-body p-4">
            <div class="row">
                <div class="col-md-6 mb-4">
                    <div class="summary-card">
                        <span class="summary-label">Schedule ID</span>
                        <div class="summary-value">${schedule.scheduleId}</div>
                    </div>
                </div>

                <div class="col-md-6 mb-4">
                    <div class="summary-card">
                        <span class="summary-label">Class</span>
                        <div class="summary-value">${schedule.classes.className}</div>
                    </div>
                </div>

                <div class="col-md-6 mb-4">
                    <div class="summary-card">
                        <span class="summary-label">Room</span>
                        <div class="summary-value">${schedule.room.roomName}</div>
                    </div>
                </div>

                <div class="col-md-6 mb-4">
                    <div class="summary-card">
                        <span class="summary-label">Slot</span>
                        <div class="summary-value">Slot ${schedule.slot.slotID} (${schedule.slot.startTime} - ${schedule.slot.endTime})</div>
                    </div>
                </div>

                <div class="col-md-6 mb-4">
                    <fmt:formatDate value="${schedule.learningDate}" pattern="EEEE, dd MMMM yyyy" var="formattedDate"/>
                    <div class="summary-card">
                        <span class="summary-label">Learning Date</span>
                        <div class="summary-value">${formattedDate}</div>
                    </div>
                </div>

                <div class="col-md-6 mb-4">
                    <div class="summary-card">
                        <span class="summary-label">Attendance Status</span>
                        <div class="summary-value">
                        <c:choose>
                            <c:when test="${schedule.attendanceStatus}">
                                <span class="badge bg-success fs-6">Taken</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-secondary fs-6">Pending</span>
                            </c:otherwise>
                        </c:choose>
                        </div>
                    </div>
                </div>
            </div>

            <div class="d-flex justify-content-end gap-2 mt-4">
                <a href="schedule?action=manage&classId=${sessionScope.selectedClassId != null ? sessionScope.selectedClassId : 0}&roomId=${sessionScope.selectedRoomId != null ? sessionScope.selectedRoomId : 0}&date=${sessionScope.selectedDate != null ? sessionScope.selectedDate : ''}" class="btn schedule-neutral-btn">
                    <i class='bx bx-arrow-left'></i> Back to List
                </a>
                <a href="schedule?action=editForm&scheduleId=${schedule.scheduleId}" class="btn btn-primary">
                    <i class='bx bx-edit'></i> Edit Schedule
                </a>
            </div>
        </div>
    </div>
</div>

<script src="js/manageUser.js" type="text/javascript"></script>

