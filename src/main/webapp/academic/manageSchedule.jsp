<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate value="${now}" pattern="yyyy-MM-dd" var="todayStr"/>

<link href="css/manageUser.css" rel="stylesheet" type="text/css"/>
<link href="css/scheduleManagement.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4 content-body schedule-page">
    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="dashboard?action=academic">Dashboard</a></li>
                <li class="breadcrumb-item active">Manage Schedule</li>
            </ol>
        </div>

        <div class="content-header mb-3">
            <div>
                <h2 class="page-title mb-1">Schedule Management</h2>
                <p class="text-muted small mb-0">View and manage all class schedules</p>
            </div>
            <a href="schedule?action=createForm" class="btn btn-add-new">
                <i class='bx bx-plus-circle'></i> Create New Schedule
            </a>
        </div>

        <div class="card user-table-card border-0 bg-white mb-3 section-card">
            <div class="card-body p-3 p-lg-4">
                <form action="schedule" method="GET" class="row g-3 align-items-end">
                    <input type="hidden" name="action" value="manage">

                    <div class="col-md-3">
                        <label class="form-label filter-label">Filter By Class</label>
                        <select name="classId" class="form-select">
                            <option value="0">All Classes</option>
                            <c:forEach items="${allClasses}" var="cls">
                                <option value="${cls[0]}" ${classId != null && classId == cls[0] ? 'selected' : ''}>
                                    ${cls[1]} - ${cls[2]}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="col-md-3">
                        <label class="form-label filter-label">Filter By Room</label>
                        <select name="roomId" class="form-select">
                            <option value="0">All Rooms</option>
                            <c:forEach items="${allRooms}" var="room">
                                <option value="${room[0]}" ${roomId != null && roomId == room[0] ? 'selected' : ''}>
                                    ${room[1]}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="col-md-3">
                        <label class="form-label filter-label">Select Week</label>
                        <input type="date" name="date" class="form-control" value="${selectedDate}">
                    </div>

                    <div class="col-md-3">
                        <button type="submit" class="btn btn-add-new w-100 justify-content-center">
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
        <c:when test="${empty scheduleList or scheduleList.size() == 0}">
            <div class="card user-table-card border-0 bg-white section-card">
                <div class="empty-state">
                    <i class='bx bx-calendar-x'></i>
                    <h4 class="mt-3 mb-2">No schedules found</h4>
                    <p class="text-muted">There are no schedules for the selected filters and week</p>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="card user-table-card border-0 bg-white section-card">
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table mb-0 text-center schedule-table">
                            <thead>
                            <tr>
                                <th>Slot</th>
                                <c:forEach items="${weekdays}" var="day" varStatus="status">
                                    <th>
                                        <span>${day}</span>
                                        <c:if test="${not empty weekDates}">
                                            <span class="day-header-date">${weekDates[status.index]}</span>
                                        </c:if>
                                    </th>
                                </c:forEach>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="slot" items="${slots}">
                                <tr>
                                    <td class="slot-cell align-middle">
                                        Slot ${slot.slotID}<br>
                                        <span class="slot-time">
                                            ${slot.startTime} - ${slot.endTime}
                                        </span>
                                    </td>
                                    <c:forEach var="day" items="${weekdays}">
                                        <td class="schedule-cell">
                                            <c:forEach var="s" items="${scheduleList}">
                                                <fmt:setLocale value="en_US" />
                                                <fmt:formatDate value="${s.learningDate}" pattern="EEEE" var="dayInSql"/>
                                                <fmt:formatDate value="${s.learningDate}" pattern="yyyy-MM-dd" var="learningDateStr"/>

                                                <c:if test="${s.slot.slotID == slot.slotID && dayInSql == day}">
                                                    <div class="schedule-item text-start">
                                                        <div class="schedule-class-name">${s.classes.className}</div>
                                                        <div class="schedule-meta">
                                                            <i class='bx bx-book-open'></i> ${s.classes.course.courseName}
                                                        </div>
                                                        <div class="schedule-meta">
                                                            <i class='bx bx-map'></i> Room: ${s.room.roomName}
                                                        </div>
                                                        <div class="schedule-meta">
                                                            <i class='bx bx-calendar'></i> ${learningDateStr}
                                                        </div>

                                                        <div class="schedule-actions">
                                                            <a href="schedule?action=viewDetail&scheduleId=${s.scheduleId}"
                                                               class="schedule-action-btn view"
                                                               title="View Details">
                                                                <i class='bx bx-file-detail'></i> View
                                                            </a>
                                                            <a href="schedule?action=editForm&scheduleId=${s.scheduleId}"
                                                               class="schedule-action-btn edit"
                                                               title="Edit Schedule">
                                                                <i class='bx bx-edit'></i> Edit
                                                            </a>
                                                            <a href="schedule?action=delete&scheduleId=${s.scheduleId}"
                                                               class="schedule-action-btn delete"
                                                               title="Delete Schedule">
                                                                <i class='bx bx-trash'></i> Delete
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
            </div>
        </c:otherwise>
    </c:choose>
</div>

<script src="js/manageUser.js" type="text/javascript"></script>


