<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link href="css/manageUser.css" rel="stylesheet" type="text/css"/>
<link href="css/scheduleManagement.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4 content-body schedule-page">
    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="dashboard?action=academic">Dashboard</a></li>
                <li class="breadcrumb-item"><a href="schedule?action=manage">Manage Schedule</a></li>
                <li class="breadcrumb-item active">Create Schedule</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">Create New Schedule</h2>
                <p class="text-muted small mb-0">Add a new schedule to the system</p>
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
                <input type="hidden" name="action" value="create">

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold">Class <span class="text-danger">*</span></label>
                        <select name="classId" class="form-select" required>
                            <option value="">Select Class</option>
                            <c:forEach items="${allClasses}" var="cls">
                                <option value="${cls[0]}">
                                    ${cls[1]} - ${cls[2]} - ${cls[3]}
                                    <c:if test="${not empty cls[4] || not empty cls[5]}">
                                        (
                                        <c:choose>
                                            <c:when test="${not empty cls[4]}">
                                                <fmt:formatDate value="${cls[4]}" pattern="yyyy-MM-dd" />
                                            </c:when>
                                            <c:otherwise> - </c:otherwise>
                                        </c:choose>
                                        &nbsp;to&nbsp;
                                        <c:choose>
                                            <c:when test="${not empty cls[5]}">
                                                <fmt:formatDate value="${cls[5]}" pattern="yyyy-MM-dd" />
                                            </c:when>
                                            <c:otherwise> - </c:otherwise>
                                        </c:choose>
                                        )
                                    </c:if>
                                    <!-- class status removed per request -->
                                    <c:if test="${not empty cls[7] || not empty cls[8]}">
                                        &nbsp;(<c:out value="${cls[7] != null ? cls[7] : 0}" />/<c:out value="${cls[8] != null ? cls[8] : '-'}" />)
                                    </c:if>
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold">Room <span class="text-danger">*</span></label>
                        <select name="roomId" class="form-select" required>
                            <option value="">Select Room</option>
                            <c:forEach items="${allRooms}" var="room">
                                <option value="${room[0]}">${room[1]} (Capacity: ${room[2]})</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold">Slot <span class="text-danger">*</span></label>
                        <select name="slotId" class="form-select" required>
                            <option value="">Select Slot</option>
                            <c:forEach items="${slots}" var="slot">
                                <option value="${slot.slotID}">Slot ${slot.slotID} (${slot.startTime} - ${slot.endTime})</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold">Start Date <span class="text-danger">*</span></label>
                        <input type="date" name="learningDate" class="form-control" required>
                        <small class="text-muted">First day of the schedule(s)</small>
                    </div>
                </div>

                <!-- Recurring Schedule Options -->
                <div class="card border-primary mb-3 series-card">
                    <div class="card-header bg-primary text-white">
                        <h6 class="mb-0"><i class='bx bx-repeat'></i> Recurring Schedule Options</h6>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label fw-bold">Repeat Pattern</label>
                                <select name="recurringType" id="recurringType" class="form-select" onchange="toggleRecurringOptions()">
                                    <option value="none" selected>No Repeat (Single Schedule)</option>
                                    <option value="daily">Daily</option>
                                    <option value="weekly">Weekly on this day (same weekday as start date)</option>
                                    <option value="weekdays">Weekdays (Mon-Fri)</option>
                                    <option value="custom">Custom</option>
                                </select>
                                <small class="text-muted">
                                    <i class='bx bx-info-circle'></i>
                                    "Weekly" will repeat on the same weekday as your selected Start Date
                                </small>
                            </div>

                            <div class="col-md-6 mb-3" id="customDaysDiv" style="display: none;">
                                <label class="form-label fw-bold">Repeat On</label>
                                <div class="d-flex flex-wrap gap-2">
                                    <div class="form-check">
                                        <input class="form-check-input" type="checkbox" name="recurringDays" value="1" id="day1">
                                        <label class="form-check-label" for="day1">Mon</label>
                                    </div>
                                    <div class="form-check">
                                        <input class="form-check-input" type="checkbox" name="recurringDays" value="2" id="day2">
                                        <label class="form-check-label" for="day2">Tue</label>
                                    </div>
                                    <div class="form-check">
                                        <input class="form-check-input" type="checkbox" name="recurringDays" value="3" id="day3">
                                        <label class="form-check-label" for="day3">Wed</label>
                                    </div>
                                    <div class="form-check">
                                        <input class="form-check-input" type="checkbox" name="recurringDays" value="4" id="day4">
                                        <label class="form-check-label" for="day4">Thu</label>
                                    </div>
                                    <div class="form-check">
                                        <input class="form-check-input" type="checkbox" name="recurringDays" value="5" id="day5">
                                        <label class="form-check-label" for="day5">Fri</label>
                                    </div>
                                    <div class="form-check">
                                        <input class="form-check-input" type="checkbox" name="recurringDays" value="6" id="day6">
                                        <label class="form-check-label" for="day6">Sat</label>
                                    </div>
                                    <div class="form-check">
                                        <input class="form-check-input" type="checkbox" name="recurringDays" value="7" id="day7">
                                        <label class="form-check-label" for="day7">Sun</label>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="row" id="endConditionDiv" style="display: none;">
                            <div class="col-md-4 mb-3">
                                <label class="form-label fw-bold">End Condition</label>
                                <select name="endCondition" id="endCondition" class="form-select" onchange="toggleEndCondition()">
                                    <option value="never">Never (max 100)</option>
                                    <option value="on">On Date</option>
                                    <option value="after">After Occurrences</option>
                                </select>
                            </div>

                            <div class="col-md-4 mb-3" id="endDateDiv" style="display: none;">
                                <label class="form-label fw-bold">End Date</label>
                                <input type="date" name="endDate" id="endDate" class="form-control">
                            </div>

                            <div class="col-md-4 mb-3" id="occurrencesDiv" style="display: none;">
                                <label class="form-label fw-bold">Number of Sessions</label>
                                <input type="number" name="occurrences" id="occurrences" class="form-control" min="1" max="100" placeholder="e.g., 10">
                            </div>
                        </div>

                        <div class="alert alert-info mb-0" id="recurringInfo" style="display: none;">
                            <i class='bx bx-info-circle'></i>
                            <small>Multiple schedule entries will be created based on your recurring pattern.</small>
                        </div>
                    </div>
                </div>

                <div class="d-flex justify-content-end gap-2 mt-4">
                    <a href="schedule?action=manage&classId=${sessionScope.selectedClassId != null ? sessionScope.selectedClassId : 0}&roomId=${sessionScope.selectedRoomId != null ? sessionScope.selectedRoomId : 0}&date=${sessionScope.selectedDate != null ? sessionScope.selectedDate : ''}" class="btn schedule-neutral-btn">
                        Cancel
                    </a>
                    <button type="submit" class="btn btn-primary">
                        <i class='bx bx-save'></i> Create Schedule
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="js/manageUser.js" type="text/javascript"></script>

<script>
function toggleRecurringOptions() {
    const recurringType = document.getElementById('recurringType').value;
    const customDaysDiv = document.getElementById('customDaysDiv');
    const endConditionDiv = document.getElementById('endConditionDiv');
    const recurringInfo = document.getElementById('recurringInfo');

    if (recurringType === 'none') {
        customDaysDiv.style.display = 'none';
        endConditionDiv.style.display = 'none';
        recurringInfo.style.display = 'none';
    } else {
        endConditionDiv.style.display = 'flex';
        recurringInfo.style.display = 'block';

        if (recurringType === 'custom') {
            customDaysDiv.style.display = 'block';
        } else {
            customDaysDiv.style.display = 'none';
        }
    }
}

function toggleEndCondition() {
    const endCondition = document.getElementById('endCondition').value;
    const endDateDiv = document.getElementById('endDateDiv');
    const occurrencesDiv = document.getElementById('occurrencesDiv');

    endDateDiv.style.display = 'none';
    occurrencesDiv.style.display = 'none';

    if (endCondition === 'on') {
        endDateDiv.style.display = 'block';
        document.getElementById('endDate').required = true;
        document.getElementById('occurrences').required = false;
    } else if (endCondition === 'after') {
        occurrencesDiv.style.display = 'block';
        document.getElementById('occurrences').required = true;
        document.getElementById('endDate').required = false;
    } else {
        document.getElementById('endDate').required = false;
        document.getElementById('occurrences').required = false;
    }
}
</script>
