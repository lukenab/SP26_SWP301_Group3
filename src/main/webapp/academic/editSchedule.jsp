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
                <li class="breadcrumb-item active">Edit Schedule</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">Edit Schedule</h2>
                <p class="text-muted small mb-0">Update schedule information</p>
            </div>
            <a href="schedule?action=manage&classId=${sessionScope.selectedClassId != null ? sessionScope.selectedClassId : 0}&roomId=${sessionScope.selectedRoomId != null ? sessionScope.selectedRoomId : 0}&date=${sessionScope.selectedDate != null ? sessionScope.selectedDate : ''}" class="btn schedule-neutral-btn">
                <i class='bx bx-arrow-left'></i> Back to Schedule List
            </a>
        </div>
    </div>

    <c:if test="${not empty sessionScope.message}">
        <div class="custom-toast toast-${sessionScope.messageType}" id="toastMessage">
            <div class="toast-icon">
                <c:choose>
                    <c:when test="${sessionScope.messageType == 'success'}">
                        <i class='bx bx-check-circle'></i>
                    </c:when>
                    <c:otherwise>
                        <i class='bx bx-error-circle'></i>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="toast-content">
                <span class="toast-title">
                    ${sessionScope.messageType == 'success' ? 'Success!' : 'Error!'}
                </span>
                <span class="toast-message">${sessionScope.message}</span>
            </div>
            <button class="toast-close" onclick="closeToast()">
                <i class='bx bx-x'></i>
            </button>
        </div>
        <c:remove var="message" scope="session" />
        <c:remove var="messageType" scope="session" />
    </c:if>

    <div class="card user-table-card border-0 bg-white section-card">
        <div class="card-body p-4">
            <form action="schedule" method="POST">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="scheduleId" value="${schedule.scheduleId}">

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold">Class <span class="text-danger">*</span></label>
                        <select name="classId" class="form-select" required>
                            <option value="">Select Class</option>
                            <c:forEach items="${allClasses}" var="cls">
                                <option value="${cls[0]}" ${schedule.classes.classid == cls[0] ? 'selected' : ''}>
                                    ${cls[1]} - ${cls[2]}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold">Room <span class="text-danger">*</span></label>
                        <select name="roomId" class="form-select" required>
                            <option value="">Select Room</option>
                            <c:forEach items="${allRooms}" var="room">
                                <option value="${room[0]}" ${schedule.room.roomId == room[0] ? 'selected' : ''}>
                                    ${room[1]} (Capacity: ${room[2]})
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold">Slot <span class="text-danger">*</span></label>
                        <select name="slotId" class="form-select" required>
                            <option value="">Select Slot</option>
                            <c:forEach items="${slots}" var="slot">
                                <option value="${slot.slotID}" ${schedule.slot.slotID == slot.slotID ? 'selected' : ''}>
                                    Slot ${slot.slotID} (${slot.startTime} - ${slot.endTime})
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold">Learning Date <span class="text-danger">*</span></label>
                        <fmt:formatDate value="${schedule.learningDate}" pattern="yyyy-MM-dd" var="learningDateStr"/>
                        <input type="date" name="learningDate" class="form-control" value="${learningDateStr}" required>
                    </div>
                </div>

                <!-- Series Edit Options (if multiple schedules with same pattern) -->
                <c:if test="${relatedCount > 1}">
                    <div class="card border-info mb-3 series-card">
                        <div class="card-header bg-info text-white">
                            <h6 class="mb-0"><i class='bx bx-info-circle'></i> Detected Schedule Series</h6>
                        </div>
                        <div class="card-body">
                            <p class="mb-3">
                                <i class='bx bx-calendar-check'></i>
                                Found <strong>${relatedCount} schedules</strong> with same Class, Slot, and Room:
                            </p>
                            <div class="form-check mb-2">
                                <input class="form-check-input" type="radio" name="editScope" id="editSingle" value="single" checked>
                                <label class="form-check-label" for="editSingle">
                                    <strong>Edit only this schedule</strong>
                                    <br><small class="text-muted">Changes will only affect this single occurrence (${learningDateStr})</small>
                                </label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="editScope" id="editSeries" value="series">
                                <label class="form-check-label" for="editSeries">
                                    <strong>Edit entire series (${relatedCount} schedules)</strong>
                                    <br><small class="text-muted">Update Class, Room, Slot, Teacher for all ${relatedCount} schedules (only non-attended ones)</small>
                                </label>
                            </div>
                        </div>
                    </div>
                </c:if>

                <div class="d-flex justify-content-end gap-2 mt-4">
                    <a href="schedule?action=manage&classId=${sessionScope.selectedClassId != null ? sessionScope.selectedClassId : 0}&roomId=${sessionScope.selectedRoomId != null ? sessionScope.selectedRoomId : 0}&date=${sessionScope.selectedDate != null ? sessionScope.selectedDate : ''}" class="btn schedule-neutral-btn">
                        Cancel
                    </a>
                    <button type="submit" class="btn btn-primary">
                        <i class='bx bx-save'></i> Update Schedule
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="js/manageUser.js" type="text/javascript"></script>

