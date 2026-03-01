<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link href="css/manageUser.css" rel="stylesheet" type="text/css"/>
<style>
    .schedule-card {
        padding: 10px 12px;
        border-radius: 8px;
        color: white;
        font-size: 0.8rem;
        margin-bottom: 6px;
        cursor: pointer;
        transition: all 0.2s ease;
        line-height: 1.4;
    }
    .schedule-card:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(0,0,0,0.15);
    }
    .schedule-card.color-0 { background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%); }
    .schedule-card.color-1 { background: linear-gradient(135deg, #8b5cf6 0%, #7c3aed 100%); }
    .schedule-card.color-2 { background: linear-gradient(135deg, #10b981 0%, #059669 100%); }
    .schedule-card.color-3 { background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%); }
    .schedule-card.color-4 { background: linear-gradient(135deg, #ec4899 0%, #db2777 100%); }
    .schedule-card.color-5 { background: linear-gradient(135deg, #6366f1 0%, #4f46e5 100%); }
    .schedule-card.color-6 { background: linear-gradient(135deg, #ef4444 0%, #dc2626 100%); }
    .schedule-card.color-7 { background: linear-gradient(135deg, #14b8a6 0%, #0d9488 100%); }

    .schedule-table {
        border-collapse: separate;
        border-spacing: 0;
        width: 100%;
    }
    .schedule-table thead th {
        background-color: #f9fafb;
        font-weight: 600;
        color: #374151;
        padding: 14px 10px;
        border-bottom: 2px solid #e5e7eb;
        text-align: center;
        font-size: 0.95rem;
        position: sticky;
        top: 0;
        z-index: 10;
    }
    .schedule-table thead th:first-child {
        text-align: left;
    }
    .schedule-table tbody td {
        vertical-align: top;
        padding: 10px;
        border: 1px solid #e5e7eb;
        min-width: 140px;
        background-color: #ffffff;
    }
    .schedule-table tbody td:first-child {
        background-color: #f9fafb;
        min-width: 100px;
    }
    .time-slot {
        font-weight: 600;
        color: #1f2937;
        font-size: 0.9rem;
    }
    .time-slot small {
        display: block;
        color: #6b7280;
        font-weight: 400;
        margin-top: 2px;
        font-size: 0.75rem;
    }
    .date-picker-section {
        background: white;
        padding: 20px;
        border-radius: 8px;
        margin-bottom: 20px;
        box-shadow: 0 1px 3px rgba(0,0,0,0.1);
    }
    .schedule-card .course-code {
        font-size: 0.95rem;
        font-weight: 700;
        margin-bottom: 2px;
    }
    .schedule-card .course-name {
        font-size: 0.8rem;
        margin-bottom: 4px;
        opacity: 0.95;
    }
    .schedule-card small {
        display: block;
        opacity: 0.9;
        line-height: 1.4;
        font-size: 0.75rem;
    }
    .table-wrapper {
        overflow-x: auto;
        max-height: 70vh;
    }
    .btn-today {
        background: #f3f4f6;
        color: #374151;
        border: 1px solid #d1d5db;
        padding: 8px 16px;
        border-radius: 6px;
        text-decoration: none;
        display: inline-flex;
        align-items: center;
        gap: 6px;
        transition: all 0.2s ease;
    }
    .btn-today:hover {
        background: #e5e7eb;
    }
</style>

<div class="container-fluid px-4 content-body">

    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="dashboard">Dashboard</a></li>
                <li class="breadcrumb-item"><a href="schedule?action=manage">Class Schedule</a></li>
                <li class="breadcrumb-item active" aria-current="page">View All Schedule</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">All Class Schedule</h2>
                <p class="text-muted small mb-0">View complete schedule by date</p>
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

    <div class="card border-0 date-picker-section">
        <div class="row align-items-center g-3">
            <div class="col-md-6">
                <label class="form-label fw-semibold">Select Date</label>
                <input type="date" class="form-control" id="selectedDate"
                       value="${selectedDate != null ? selectedDate : ''}"
                       onchange="viewScheduleByDate()">
            </div>
            <div class="col-md-6">
                <label class="form-label">&nbsp;</label><br>
                <button class="btn-today" onclick="viewToday()">
                    <i class='bx bx-calendar-check'></i> Today
                </button>
            </div>
        </div>
    </div>

    <c:if test="${not empty selectedDate}">
        <div class="card user-table-card border-0 bg-white">
            <div class="card-header bg-white border-bottom">
                <h5 class="mb-0">Schedule for <fmt:formatDate value="${selectedDate}" pattern="EEEE, MMMM dd, yyyy"/></h5>
            </div>
            <div class="table-wrapper">
                <table class="table mb-0 schedule-table">
                    <thead>
                        <tr>
                            <th style="width: 100px;">Time<br>Slot</th>
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
                                <td>
                                    <div class="time-slot">
                                        Slot ${slot}
                                        <small>
                                            <c:choose>
                                                <c:when test="${slot == 1}">07:00<br>09:00</c:when>
                                                <c:when test="${slot == 2}">09:15<br>11:15</c:when>
                                                <c:when test="${slot == 3}">12:30<br>14:30</c:when>
                                                <c:when test="${slot == 4}">14:45<br>16:45</c:when>
                                                <c:when test="${slot == 5}">17:00<br>19:00</c:when>
                                                <c:when test="${slot == 6}">19:15<br>21:15</c:when>
                                            </c:choose>
                                        </small>
                                    </div>
                                </td>
                                <c:forEach var="day" items="${['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday']}">
                                    <td>
                                        <c:forEach var="schedule" items="${allSchedules}">
                                            <c:if test="${schedule.slot == slot}">
                                                <fmt:formatDate var="dayOfWeek" value="${schedule.learningDate}" pattern="EEEE"/>
                                                <c:if test="${dayOfWeek == day}">
                                                    <c:set var="colorIndex" value="${schedule.classes.classid % 8}"/>
                                                    <div class="schedule-card color-${colorIndex}">
                                                        <div class="course-code">${schedule.classes.className}</div>
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
        <div class="card border-0 bg-white text-center py-5">
            <i class='bx bx-calendar' style="font-size: 4rem; color: #d1d5db;"></i>
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

