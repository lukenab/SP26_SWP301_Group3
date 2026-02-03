<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="container-fluid px-4">
    <div class="d-flex justify-content-between align-items-center mt-4 mb-3">
        <h3 class="text-primary fw-bold text-uppercase">My Schedule</h3>
        <div class="bg-white p-2 border rounded shadow-sm">
            <form action="schedule" method="GET" class="row g-2 align-items-center">
                <input type="hidden" name="action" value="view">
                <div class="col-auto"><label class="small fw-bold">Date:</label></div>
                <div class="col-auto"><input type="date" name="date" class="form-control form-control-sm" value="${selectedDate}"></div>
                <div class="col-auto"><button type="submit" class="btn btn-primary btn-sm">Filter</button></div>
            </form>
        </div>
    </div>

    <div class="card shadow-sm border-0">
        <div class="card-body p-0">
            <table class="table table-bordered mb-0">
                <thead class="text-white text-center" style="background-color: #f8f9fc; color: #5a5c69 !important;">
                    <tr>
                        <th style="width: 8%;">Slot</th>
                            <c:forEach items="${weekdays}" var="day">
                            <th style="width: 13%;">${day}</th>
                            </c:forEach>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="slot" items="${slots}">
                        <tr>
                            <td class="text-center align-middle bg-light fw-bold small">
                                Slot ${slot}<br>
                                <span class="text-muted fw-normal" style="font-size: 0.7rem;">${slotTimes[slot]}</span>
                            </td>
                            <c:forEach var="day" items="${weekdays}">
                                <td class="timetable-cell">
                                    <c:forEach var="s" items="${scheduleList}">
                                        <fmt:setLocale value="en_US" />
                                        <fmt:formatDate value="${s.learningDate}" pattern="EEEE" var="dayInSql"/>

                                        <c:if test="${s.slot == slot && dayInSql == day}">
                                            <div class="schedule-card shadow-sm">
                                                <div class="class-name">${s.classes.className}</div>
                                                <div class="info-line">
                                                    <i class="fas fa-map-marker-alt"></i> Room: ${s.room.roomName}
                                                </div>
                                                <div class="info-line">
                                                    <i class="far fa-clock"></i> ${slotTimes[slot]}
                                                </div>
                                                <div class="action-zone mt-2 border-top pt-2">
                                                    <c:choose>
                                                        <c:when test="${s.attendanceStatus}">
                                                            <span class="badge bg-success w-100 py-1"><i class="fas fa-check"></i> Attended</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <a href="attendance?action=take&scheduleId=${s.scheduleId}" 
                                                               class="btn btn-attendance w-100 btn-sm text-white">Attendance</a>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </div>
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
</div>

<style>

    .timetable-cell {
        height: 140px;
        vertical-align: top;
        background-color: #ffffff;
        padding: 6px !important;
        border: 1px solid #e3e6f0 !important;
    }


    .schedule-card {
        background-color: #8e9aad;
        color: white;
        border-radius: 8px;
        padding: 10px;
        font-size: 0.75rem;
        border-left: 5px solid #5a677d;
        transition: transform 0.2s;
    }
    .schedule-card:hover {
        transform: scale(1.02);
    }

    .class-name {
        font-weight: bold;
        font-size: 0.85rem;
        margin-bottom: 5px;
    }
    .info-line {
        margin-bottom: 2px;
        opacity: 0.9;
    }

    .btn-attendance {
        background-color: #4e73df;
        border: none;
        font-weight: bold;
        font-size: 0.7rem;
    }
    .btn-attendance:hover {
        background-color: #2e59d9;
    }

    .table thead th {
        border-top: none;
        font-size: 0.85rem;
        padding: 15px;
        text-transform: uppercase;
    }
</style>