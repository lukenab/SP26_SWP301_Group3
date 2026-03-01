<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate value="${now}" pattern="yyyy-MM-dd" var="todayStr"/>
<link href="../css/viewClassSchedule.css" rel="stylesheet" type="text/css"/>
<link href="css/teacherSchedule.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4 content-body">

    <div class="mb-4 mt-3">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="dashboard">Dashboard</a></li>
                <li class="breadcrumb-item"><a href="class">Class Management</a></li>
                <li class="breadcrumb-item"><a href="student?action=viewByClass&classId=${classId}">Student List</a></li>
                <li class="breadcrumb-item active">Class Schedule</li>
            </ol>
        </div> 


        <div class="content-header d-flex justify-content-between align-items-center">
            <div>
                <h2 class="page-title fw-bold text-dark mb-1">Schedule - ${className}</h2>
                <p class="text-muted small mb-0">Weekly timetable for this specific class</p>
            </div>

            <div class="bg-white p-2 border rounded shadow-sm">
                <form action="schedule" method="GET" class="row g-2 align-items-center mb-0">
                    <input type="hidden" name="action" value="view">

                    <input type="hidden" name="classId" value="${classId}">

                    <div class="col-auto"><label class="small fw-bold">Date:</label></div>
                    <div class="col-auto">
                        <input type="date" name="date" class="form-control form-control-sm" value="${selectedDate}">
                    </div>
                    <div class="col-auto">
                        <button type="submit" class="btn btn-primary btn-sm px-3">Filter</button>
                    </div>
                </form>
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
                                <td class="timetable-cell p-2" style="min-height: 100px;">
                                    <c:forEach var="s" items="${scheduleList}">
                                        <fmt:setLocale value="en_US" />
                                        <fmt:formatDate value="${s.learningDate}" pattern="EEEE" var="dayInSql"/>
                                        <fmt:formatDate value="${s.learningDate}" pattern="yyyy-MM-dd" var="learningDateStr"/>

                                        <c:if test="${s.slot == slot && dayInSql == day && s.classes.classid == classId}">
                                            <div class="schedule-card shadow-sm p-2 mb-2 text-start border rounded bg-white">
                                                <div class="small text-muted mb-1" style="font-size: 0.75rem;">
                                                    <i class="fas fa-map-marker-alt"></i> Room: ${s.room.roomName}
                                                </div>

                                                <div class="action-zone mt-2 border-top pt-2">
                                                    <c:choose>
                                                     
                                                        <c:when test="${learningDateStr == todayStr}">
                                                            <c:choose>
                                                           
                                                                <c:when test="${s.attendanceStatus}">
                                                                    <a href="attendance?action=take&scheduleId=${s.scheduleId}&classId=${classId}" 
                                                                       class="btn btn-sm btn-success w-100 py-1 text-white shadow-sm fw-bold" style="font-size: 0.7rem;">
                                                                        <i class="fas fa-check-circle"></i> ATTENDED
                                                                    </a>
                                                                </c:when>

                                                                <c:otherwise>
                                                                    <a href="attendance?action=take&scheduleId=${s.scheduleId}&classId=${classId}" 
                                                                       class="btn btn-sm btn-warning w-100 py-1 shadow-sm fw-bold" style="font-size: 0.7rem;">
                                                                        Attendance
                                                                    </a>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:when>
                        
                                                        <c:otherwise>
                                                            <span class="badge ${learningDateStr > todayStr ? 'bg-info' : (s.attendanceStatus ? 'bg-secondary' : 'bg-danger')} w-100 py-2 text-white" style="font-size: 0.7rem;">
                                                                ${learningDateStr > todayStr ? 'Upcoming' : (s.attendanceStatus ? 'Attended' : 'Missed')}
                                                            </span>
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