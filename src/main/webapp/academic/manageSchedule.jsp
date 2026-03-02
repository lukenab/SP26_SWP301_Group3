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
                <li class="breadcrumb-item"><a href="dashboard">Dashboard</a></li>
                <li class="breadcrumb-item active">Manage Schedule</li>
            </ol>
        </div>

        <div class="content-header d-flex justify-content-between align-items-center mb-3">
            <div>
                <h2 class="page-title fw-bold text-dark mb-1">Manage Schedule</h2>
                <p class="text-muted small mb-0">View and manage all class schedules</p>
            </div>

            <div>
                <a href="schedule?action=createForm" class="btn btn-primary">
                    <i class='bx bx-plus-circle'></i> Add Schedule
                </a>
            </div>
        </div>

        <!-- Filters -->
        <div class="card shadow-sm border-0 mb-3">
            <div class="card-body">
                <form action="schedule" method="GET" class="row g-3 align-items-end">
                    <input type="hidden" name="action" value="manage">

                    <div class="col-md-4">
                        <label class="form-label fw-bold small">Filter by Class:</label>
                        <select name="classId" class="form-select">
                            <option value="0">All Classes</option>
                            <c:forEach items="${allClasses}" var="cls">
                                <option value="${cls[0]}" ${classId != null && classId == cls[0] ? 'selected' : ''}>
                                    ${cls[1]} - ${cls[2]}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="col-md-4">
                        <label class="form-label fw-bold small">Select Week:</label>
                        <input type="date" name="date" class="form-control" value="${selectedDate}">
                    </div>

                    <div class="col-md-4">
                        <button type="submit" class="btn btn-primary w-100">
                            <i class='bx bx-filter-alt'></i> Filter
                        </button>
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

    <c:choose>
        <c:when test="${empty classId or classId == 0}">
            <!-- Show message to select a class -->
            <div class="card shadow-sm border-0">
                <div class="card-body text-center py-5">
                    <i class='bx bx-calendar-x' style="font-size: 4rem; color: #6c757d;"></i>
                    <h4 class="mt-3 mb-2">Please select a class to view schedule</h4>
                    <p class="text-muted">Use the filter above to select a class and view its schedule</p>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <!-- Show schedule table -->
            <div class="card shadow-sm border-0">
                <div class="card-body p-0">
                    <table class="table table-bordered mb-0 text-center">
                        <thead class="text-white" style="background-color: #f8f9fc; color: #5a5c69 !important;">
                            <tr>
                                <th style="width: 10%;">Slot</th>
                                <c:forEach items="${weekdays}" var="day">
                                    <th style="width: 12.8%;">${day}</th>
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
                                                        <div class="small text-muted mb-1">
                                                            <i class="fas fa-calendar"></i> ${learningDateStr}
                                                        </div>

                                                        <div class="action-zone mt-2 border-top pt-2 d-flex gap-1">
                                                            <a href="schedule?action=viewDetail&scheduleId=${s.scheduleId}"
                                                               class="btn btn-sm btn-info flex-fill text-white"
                                                               title="View Details">
                                                                <i class="fas fa-eye"></i> View
                                                            </a>
                                                            <a href="schedule?action=editForm&scheduleId=${s.scheduleId}"
                                                               class="btn btn-sm btn-warning flex-fill text-white"
                                                               title="Edit Schedule">
                                                                <i class="fas fa-edit"></i> Edit
                                                            </a>
                                                            <a href="schedule?action=delete&scheduleId=${s.scheduleId}"
                                                               class="btn btn-sm btn-danger flex-fill"
                                                               title="Delete Schedule">
                                                                <i class="fas fa-trash"></i> Delete
                                                            </a>
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
        </c:otherwise>
    </c:choose>
</div>

<script src="js/manageUser.js" type="text/javascript"></script>
