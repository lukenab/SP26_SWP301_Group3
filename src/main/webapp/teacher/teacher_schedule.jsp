<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate value="${now}" pattern="yyyy-MM-dd" var="todayStr"/>

<link href="css/teacherSchedule.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4">
    <div class="d-flex justify-content-between align-items-center mt-4 mb-3">
        <h3 class="text-primary fw-bold text-uppercase">My Schedule</h3>
        <div class="bg-white p-2 border rounded shadow-sm">
            <form action="schedule" method="GET" class="row g-2 align-items-center">
                <input type="hidden" name="action" value="view">
                <div class="col-auto"><label class="small fw-bold">Date:</label></div>
                <div class="col-auto">
                    <input type="date" name="date" class="form-control form-control-sm" value="${selectedDate}">
                </div>
                <div class="col-auto">
                    <button type="submit" class="btn btn-primary btn-sm">Filter</button>
                </div>
            </form>
        </div>
    </div>

    <div class="card shadow-sm border-0">
        <div class="card-body p-0">
            <table class="table table-bordered mb-0 text-center">
                <thead class="text-white" style="background-color: #f8f9fc; color: #5a5c69 !important;">
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
                            <td class="align-middle bg-light fw-bold small">
                                Slot ${slot}<br>
                                <span class="text-muted fw-normal" style="font-size: 0.7rem;">${slotTimes[slot]}</span>
                            </td>
                            <c:forEach var="day" items="${weekdays}">
                                <td class="timetable-cell p-2">
                                    <c:forEach var="s" items="${scheduleList}">
                                        <fmt:setLocale value="en_US" />
                                        <fmt:formatDate value="${s.learningDate}" pattern="EEEE" var="dayInSql"/>
                                        <fmt:formatDate value="${s.learningDate}" pattern="yyyy-MM-dd" var="learningDateStr"/>

                                        <c:if test="${s.slot == slot && dayInSql == day}">
                                            <div class="schedule-card shadow-sm p-2 mb-2 text-start border rounded bg-white">
                                                <div class="class-name fw-bold text-primary">${s.classes.className}</div>
                                                <div class="small text-muted mb-1">
                                                    <i class="fas fa-map-marker-alt"></i> Room: ${s.room.roomName}
                                                </div>

                                                <div class="action-zone mt-2 border-top pt-2">
                                                    <c:choose>

                                                        <c:when test="${learningDateStr == todayStr}">
                                                            <c:choose>

                                                                <c:when test="${s.attendanceStatus}">
                                                                    <a href="attendance?action=take&scheduleId=${s.scheduleId}&classId=${s.classes.classid}" 
                                                                       class="btn btn-sm btn-success w-100 py-1 text-white shadow-sm">
                                                                        <i class="fas fa-check-circle"></i> ATTENDED
                                                                    </a>
                                                                </c:when>

                                                                <c:otherwise>
                                                                    <a href="attendance?action=take&scheduleId=${s.scheduleId}&classId=${s.classes.classid}" 
                                                                       class="btn btn-sm btn-warning w-100 py-1 shadow-sm">
                                                                        Attendance
                                                                    </a>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:when>


                                                        <c:when test="${learningDateStr > todayStr}">
                                                            <span class="badge bg-info w-100 py-2 text-white">
                                                                <i class="fas fa-clock"></i> Upcoming
                                                            </span>
                                                        </c:when>


                                                        <c:otherwise>
                                                            <c:choose>

                                                                <c:when test="${s.attendanceStatus}">
                                                                    <span class="badge bg-secondary w-100 py-2">
                                                                        <i class="fas fa-lock"></i> Attended
                                                                    </span>
                                                                </c:when>

                                                                <c:otherwise>
                                                                    <span class="badge bg-danger w-100 py-2">Missed</span>
                                                                </c:otherwise>
                                                            </c:choose>
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