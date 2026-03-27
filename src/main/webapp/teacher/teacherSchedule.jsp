<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate value="${now}" pattern="yyyy-MM-dd" var="todayStr"/>

<link href="css/teacherSchedule.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4 content-body">
    <div class="mb-4 mt-3">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="dashboard?action=teacher">Dashboard</a></li>
                <li class="breadcrumb-item active">My Schedule</li>
            </ol>
        </div>

        <div class="content-header d-flex justify-content-between align-items-center">
            <div>
                <h2 class="page-title fw-bold text-dark mb-1">My Teaching Schedule</h2>
                <p class="text-muted small mb-0">View and manage your weekly teaching sessions</p>
            </div>

            <div class="bg-white p-2 border rounded shadow-sm d-flex align-items-center gap-2">
                <a href="schedule?action=viewTeacherSchedule&date=${prevWeek}" class="btn btn-outline-primary btn-sm">
                    <i class='bx bx-chevron-left'></i> Prev
                </a>

                <form action="schedule" method="GET" class="row g-2 align-items-center mb-0 mx-1">
                    <input type="hidden" name="action" value="viewTeacherSchedule">
                    <div class="col-auto">
                        <input type="date" name="date" class="form-control form-control-sm" 
                               value="${selectedDate}" onchange="this.form.submit()">
                    </div>
                </form>

                <a href="schedule?action=viewTeacherSchedule&date=${nextWeek}" class="btn btn-outline-primary btn-sm">
                    Next <i class='bx bx-chevron-right'></i>
                </a>
            </div>
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

    <div class="card shadow-sm border-0">
        <div class="card-body p-0">
            <table class="table table-bordered mb-0 text-center">
                <thead class="text-white" style="background-color: #f8f9fc; color: #5a5c69 !important;">
                    <tr>
                        <th style="width: 11%;">Slot</th>
                            <c:forEach items="${weekdays}" var="day" varStatus="loop">
                            <th style="width: 12.8%;">
                                <div class="fw-bold">${day}</div>
                                <div class="small fw-normal text-muted" style="font-size: 0.75rem;">
                                    ${dateOfWeek[loop.index]}
                                </div>
                            </th>
                        </c:forEach>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="slot" items="${slots}">
                        <tr>
                            <td class="align-middle bg-light fw-bold small">
                                Slot ${slot.slotID}<br>
                                <span class="text-muted fw-normal" style="font-size: 0.7rem;">
                                    ${slot.startTime} - ${slot.endTime}
                                </span>
                            </td>
                            <c:forEach var="day" items="${weekdays}">
                                <td class="timetable-cell p-2">
                                    <c:forEach var="s" items="${scheduleList}">
                                        <fmt:setLocale value="en_US" />
                                        <fmt:formatDate value="${s.learningDate}" pattern="EEEE" var="dayInSql"/>
                                        <fmt:formatDate value="${s.learningDate}" pattern="yyyy-MM-dd" var="learningDateStr"/>

                                        <c:if test="${s.slot.slotID == slot.slotID && dayInSql == day}">
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
                                                            <span class="badge w-100 py-2 text-white" style="background-color: #4e73df;">
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
<script src="js/manageUser.js" type="text/javascript"></script>