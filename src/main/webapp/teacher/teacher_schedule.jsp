<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="container-fluid px-4" style="margin-top: 10px;"> 

    <div class="d-flex justify-content-between align-items-center mb-3">
        <h3 class="text-primary fw-bold text-uppercase">My Schedule</h3>

        <div class="bg-white p-2 border rounded shadow-sm">
            <form action="schedule" method="GET" class="row g-2 align-items-center">
                <input type="hidden" name="action" value="view">
                <div class="col-auto"><label class="small fw-bold">Date:</label></div>
                <div class="col-auto">
                    <input type="date" name="date" class="form-control form-control-sm" value="${selectedDate}">
                </div>
                <div class="col-auto"><button type="submit" class="btn btn-primary btn-sm">Filter</button></div>
            </form>
        </div>
    </div>

    <div class="card shadow-sm border-0 mb-5"> <div class="card-body p-0">
            <div class="table-responsive"> 
                <table class="table table-bordered mb-0">
                    <thead class="text-center custom-thead">
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
                                                        <i class='bx bx-map'></i> Room: ${s.room.roomName}
                                                    </div>
                                                    <div class="info-line">
                                                        <i class='bx bx-time'></i> ${slotTimes[slot]}
                                                    </div>

                                                    <div class="action-zone mt-2 border-top pt-2">
                                                        <c:choose>
                                                            <c:when test="${s.attendanceStatus}">
                                                                <span class="badge bg-success w-100 py-2"><i class='bx bx-check'></i> Attended</span>
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
</div>