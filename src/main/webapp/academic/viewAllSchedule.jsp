<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link href="css/manageUser.css" rel="stylesheet" type="text/css"/>
<link href="css/scheduleManagement.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4 content-body schedule-page">

    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="dashboard">Dashboard</a></li>
                <li class="breadcrumb-item"><a href="schedule?action=manage">Manage Schedule</a></li>
                <li class="breadcrumb-item active" aria-current="page">All Schedules</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">All Class Schedules</h2>
                <p class="text-muted small mb-0">Review schedule distribution by weekday and slot</p>
            </div>
            <a href="schedule?action=manage" class="btn btn-secondary">
                <i class='bx bx-arrow-back'></i> Back to Search
            </a>
        </div>
    </div>

    <c:if test="${not empty sessionScope.message}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            ${sessionScope.message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <% session.removeAttribute("message"); %>
    </c:if>

    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            ${sessionScope.error}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <% session.removeAttribute("error"); %>
    </c:if>

    <div class="card user-table-card border-0 bg-white section-card mb-3">
        <div class="card-body p-3 p-lg-4">
            <div class="row align-items-center g-3">
            <div class="col-md-6">
                    <label class="form-label filter-label">Select Date</label>
                <input type="date" class="form-control" id="selectedDate"
                       value="${selectedDate != null ? selectedDate : ''}"
                       onchange="viewScheduleByDate()">
            </div>
            <div class="col-md-6">
                    <label class="form-label filter-label">Quick Action</label><br>
                    <button type="button" class="btn btn-add-new" onclick="viewToday()">
                    <i class='bx bx-calendar-check'></i> Today
                </button>
            </div>
            </div>
        </div>
    </div>

    <c:if test="${not empty selectedDate}">
        <div class="card user-table-card border-0 bg-white section-card">
            <div class="card-header bg-white border-bottom">
                <h5 class="mb-0">Schedule for <fmt:formatDate value="${selectedDate}" pattern="EEEE, MMMM dd, yyyy"/></h5>
            </div>
            <div class="table-responsive">
                <table class="table mb-0 schedule-table">
                    <thead>
                        <tr>
                            <th>Time Slot</th>
                            <th>Monday</th>
                            <th>Tuesday</th>
                            <th>Wednesday</th>
                            <th>Thursday</th>
                            <th>Friday</th>
                            <th>Saturday</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="slot" begin="1" end="6">
                            <tr>
                                <td class="slot-cell">
                                    <div>
                                        Slot ${slot}
                                        <span class="slot-time">
                                            <c:choose>
                                                <c:when test="${slot == 1}">07:00<br>09:00</c:when>
                                                <c:when test="${slot == 2}">09:15<br>11:15</c:when>
                                                <c:when test="${slot == 3}">12:30<br>14:30</c:when>
                                                <c:when test="${slot == 4}">14:45<br>16:45</c:when>
                                                <c:when test="${slot == 5}">17:00<br>19:00</c:when>
                                                <c:when test="${slot == 6}">19:15<br>21:15</c:when>
                                            </c:choose>
                                        </span>
                                    </div>
                                </td>
                                <c:forEach var="day" items="${['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday']}">
                                    <td class="schedule-cell">
                                        <c:forEach var="schedule" items="${allSchedules}">
                                            <c:if test="${schedule.slot == slot}">
                                                <fmt:formatDate var="dayOfWeek" value="${schedule.learningDate}" pattern="EEEE"/>
                                                <c:if test="${dayOfWeek == day}">
                                                    <div class="calendar-card">
                                                        <div class="course-name">${schedule.classes.className}</div>
                                                        <div class="course-name">${schedule.classes.course.courseName}</div>
                                                        <small>Teacher: ID ${schedule.classes.employee.employeeId}</small>
                                                        <small>Room: ${schedule.room.roomName}</small>
                                                    </div>
                                                </c:if>
                                            </c:if>
                                        </c:forEach>
                                    </td>
                                </c:forEach>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </c:if>

    <c:if test="${empty selectedDate}">
        <div class="card user-table-card border-0 bg-white section-card empty-state">
            <i class='bx bx-calendar'></i>
            <h4 class="mt-3">Select a Date</h4>
            <p class="text-muted">Choose a date above to view the schedule</p>
        </div>
    </c:if>
</div>

<script>
    function viewScheduleByDate() {
        const date = document.getElementById('selectedDate').value;
        if (date) {
            window.location.href = 'schedule?action=viewAll&date=' + date;
        }
    }

    function viewToday() {
        const today = new Date().toISOString().split('T')[0];
        document.getElementById('selectedDate').value = today;
        viewScheduleByDate();
    }

    // Set default date to today if not set
    window.onload = function() {
        const dateInput = document.getElementById('selectedDate');
        if (!dateInput.value) {
            dateInput.value = new Date().toISOString().split('T')[0];
        }
    };
</script>

