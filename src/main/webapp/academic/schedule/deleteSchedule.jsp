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
                <li class="breadcrumb-item"><a href="?action=manage">Manage Schedule</a></li>
                <li class="breadcrumb-item active" aria-current="page">Delete Schedule</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">Delete Schedule</h2>
                <p class="text-muted small mb-0">Review schedule information before deletion</p>
            </div>
            <a href="?action=manage&classId=${sessionScope.selectedClassId != null ? sessionScope.selectedClassId : 0}&roomId=${sessionScope.selectedRoomId != null ? sessionScope.selectedRoomId : 0}&date=${sessionScope.selectedDate != null ? sessionScope.selectedDate : ''}" class="btn schedule-neutral-btn">
                <i class='bx bx-arrow-left'></i> Back to Schedule List
            </a>
        </div>
    </div>

    <div class="card user-table-card border-0 bg-white section-card">
        <div class="card-body p-4">
            <p class="alert alert-warning">
                <i class='bx bx-info-circle'></i>
                Are you sure you want to delete this schedule? This action cannot be undone.
            </p>

            <form action="" method="POST">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="scheduleId" value="${schedule.scheduleId}">

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

                <!-- Series Delete Options (if multiple schedules with same pattern) -->
                <c:if test="${relatedCount > 1}">
                    <div class="card border-danger mb-3 series-card">
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

                <div class="d-flex justify-content-end gap-2 mt-4">
                    <a href="?action=manage&classId=${sessionScope.selectedClassId != null ? sessionScope.selectedClassId : 0}&roomId=${sessionScope.selectedRoomId != null ? sessionScope.selectedRoomId : 0}&date=${sessionScope.selectedDate != null ? sessionScope.selectedDate : ''}" class="btn schedule-neutral-btn">
                        Cancel
                    </a>
                    <button type="submit" class="btn btn-danger">
                        <i class='bx bx-trash'></i> Delete Schedule
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="js/manageUser.js" type="text/javascript"></script>

