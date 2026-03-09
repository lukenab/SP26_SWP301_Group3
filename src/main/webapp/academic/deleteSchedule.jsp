<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link href="css/manageUser.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4 content-body">
    <div class="mb-4">
        <div class="content-header">
            <div>
                <h1 class="page-title">Delete Schedule</h1>
            </div>
            <a href="schedule?action=manage" class="btn btn-secondary">
                <i class='bx bx-arrow-left'></i> Back to Schedule List
            </a>
        </div>
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="dashboard">Dashboard</a></li>
                <li class="breadcrumb-item"><a href="schedule?action=manage">Manage Schedule</a></li>
                <li class="breadcrumb-item active" aria-current="page">Delete Schedule</li>
            </ol>
        </div>
    </div>

    <div class="card border-0 bg-white">
        <div class="card-body p-4">
            <p class="alert alert-warning">
                <i class='bx bx-info-circle'></i>
                Are you sure you want to delete this schedule? This action cannot be undone.
            </p>

            <form action="schedule" method="POST">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="scheduleId" value="${schedule.scheduleId}">

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

                <!-- Series Delete Options (if multiple schedules with same pattern) -->
                <c:if test="${relatedCount > 1}">
                    <div class="card border-danger mb-3">
                        <div class="card-header bg-danger text-white">
                            <h6 class="mb-0"><i class='bx bx-info-circle'></i> Detected Schedule Series</h6>
                        </div>
                        <div class="card-body">
                            <p class="mb-3">
                                <i class='bx bx-calendar-check'></i>
                                Found <strong>${relatedCount} schedules</strong> with same Class, Slot, and Room:
                            </p>
                            <div class="form-check mb-2">
                                <input class="form-check-input" type="radio" name="deleteScope" id="deleteSingle" value="single" checked>
                                <label class="form-check-label" for="deleteSingle">
                                    <strong>Delete only this schedule</strong>
                                    <br><small class="text-muted">Only this single occurrence will be deleted (${formattedDate})</small>
                                </label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="deleteScope" id="deleteSeries" value="series">
                                <label class="form-check-label" for="deleteSeries">
                                    <strong>Delete entire series (${relatedCount} schedules)</strong>
                                    <br><small class="text-danger">⚠️ This will delete all ${relatedCount} schedules in this series (only non-attended ones)!</small>
                                </label>
                            </div>
                        </div>
                    </div>
                </c:if>

                <div class="d-flex gap-2 mt-4">
                    <button type="submit" class="btn btn-danger">
                        <i class='bx bx-trash'></i> Delete Schedule
                    </button>
                    <a href="schedule?action=manage&classId=${sessionScope.selectedClassId != null ? sessionScope.selectedClassId : 0}&date=${sessionScope.selectedDate != null ? sessionScope.selectedDate : ''}" class="btn btn-secondary">
                        <i class='bx bx-x'></i> Cancel
                    </a>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="js/manageUser.js" type="text/javascript"></script>

