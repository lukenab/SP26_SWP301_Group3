<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate value="${now}" pattern="yyyy-MM-dd" var="todayStr"/>

<link href="css/teacherSchedule.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4">

    <!-- HEADER -->
    <div class="d-flex justify-content-between align-items-center mt-4 mb-3">
        <h3 class="text-primary fw-bold text-uppercase">
            Schedule - ${className}
        </h3>
        <a href="class" class="btn btn-secondary btn-sm">
            Back to Classes
        </a>
    </div>

    <div class="card shadow-sm border-0">
        <div class="card-body p-0">
            <table class="table table-bordered mb-0 text-center">
                <thead class="text-white"
                       style="background-color: #f8f9fc; color: #5a5c69 !important;">
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
                                <span class="text-muted fw-normal"
                                      style="font-size: 0.7rem;">
                                    ${slotTimes[slot]}
                                </span>
                            </td>

                            <c:forEach var="day" items="${weekdays}">
                                <td class="timetable-cell p-2">

                                    <c:forEach var="s" items="${scheduleList}">
                                        <fmt:setLocale value="en_US" />
                                        <fmt:formatDate value="${s.learningDate}"
                                                        pattern="EEEE"
                                                        var="dayInSql"/>
                                        <fmt:formatDate value="${s.learningDate}"
                                                        pattern="yyyy-MM-dd"
                                                        var="learningDateStr"/>

                                        <c:if test="${s.slot == slot && dayInSql == day}">
                                            <div class="schedule-card shadow-sm p-2 mb-2 text-start border rounded bg-white">

                                                <div class="small text-muted mb-1">
                                                    <i class="fas fa-map-marker-alt"></i>
                                                    Room: ${s.room.roomName}
                                                </div>

                                                <div class="action-zone mt-2 border-top pt-2">

                                                    <c:choose>

                                                        <%-- Hôm nay --%>
                                                        <c:when test="${learningDateStr == todayStr}">
                                                            <c:choose>

                                                                <c:when test="${s.attendanceStatus}">
                                                                    <span class="badge bg-success w-100 py-2">
                                                                        Attended
                                                                    </span>
                                                                </c:when>

                                                                <c:otherwise>
                                                                    <a href="attendance?action=take&scheduleId=${s.scheduleId}&classId=${classId}"
                                                                       class="btn btn-sm btn-warning w-100">
                                                                        Take Attendance
                                                                    </a>
                                                                </c:otherwise>

                                                            </c:choose>
                                                        </c:when>

                                                        <%-- Tương lai --%>
                                                        <c:when test="${learningDateStr > todayStr}">
                                                            <span class="badge bg-info w-100 py-2">
                                                                Upcoming
                                                            </span>
                                                        </c:when>

                                                        <%-- Quá khứ --%>
                                                        <c:otherwise>
                                                            <c:choose>

                                                                <c:when test="${s.attendanceStatus}">
                                                                    <span class="badge bg-secondary w-100 py-2">
                                                                        Attended
                                                                    </span>
                                                                </c:when>

                                                                <c:otherwise>
                                                                    <span class="badge bg-danger w-100 py-2">
                                                                        Missed
                                                                    </span>
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