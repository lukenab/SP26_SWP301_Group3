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
                <li class="breadcrumb-item"><a href="dashboard">Dashboard</a></li>
                <li class="breadcrumb-item active">Manage Schedule</li>
            </ol>
        </div>

        <div class="content-header mb-3">
            <div>
                <h2 class="page-title mb-1">Schedule Management</h2>
                <p class="text-muted small mb-0">View and manage all class schedules</p>
            </div>
            <a href="?action=create" class="btn btn-add-new">
                <i class='bx bx-plus-circle'></i> Create New Schedule
            </a>
        </div>

        <div class="card user-table-card border-0 bg-white mb-3 section-card">
            <div class="card-body p-3 p-lg-4">
                <form action="" method="GET" class="row g-3 align-items-end">
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
        <c:when test="${(empty classId or classId == 0) and (empty roomId or roomId == 0)}">
            <div class="card user-table-card border-0 bg-white section-card">
                <div class="empty-state">
                    <i class='bx bx-calendar-x'></i>
                    <h4 class="mt-3 mb-2">Please select a class or room to view schedule</h4>
                    <p class="text-muted">Use filters above to view schedule by class, room, or both</p>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="card user-table-card border-0 bg-white section-card">
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table mb-0 text-center schedule-table">
                            <thead>
                            <!--
                                THEAD: Header của bảng lịch
                                - Cột 1: "Slot" (cột để chứa tên/thời gian slot)
                                - Cột 2-8: Các ngày trong tuần (Monday - Sunday)
                                - Mỗi cột sẽ hiện tên thứ + ngày cụ thể (nếu controller set weekDates)
                            -->
                            <tr>
                                <th>Slot</th>
                                <!--
                                    Lặp qua weekdays: ["Monday", "Tuesday", ..., "Sunday"]
                                    varStatus="status" dùng để lấy status.index (vị trí 0-6 trong array)
                                -->
                                <c:forEach items="${weekdays}" var="day" varStatus="status">
                                    <th>
                                        <!-- Hiển thị tên thứ (Monday, Tuesday...) -->
                                        <span>${day}</span>
                                    </th>
                                </c:forEach>
                            </tr>
                        </thead>
                        <tbody>
                            <!--
                                TBODY: Nội dung bảng lịch học
                                Cấu trúc dữ liệu: SLOT x DAY (7 ngày)
                                    - Mỗi dòng = 1 slot (Slot 1, 2, 3...)
                                    - Mỗi cột = 1 thứ trong tuần (Monday, Tuesday...)
                                    - Mỗi ô = chứa 0 hoặc nhiều lịch học
                            -->
                            <!--
                                VÒNG LẶP 1: Duyệt từng slot (tạo dòng)
                                slots = danh sách tất cả slot trong ngày
                            -->
                            <c:forEach var="slot" items="${slots}">
                                <tr>
                                    <!--
                                        Cột đầu tiên: tên và thời gian slot
                                        Ví dụ output: "Slot 1\n08:00 - 10:00"
                                    -->
                                    <td class="slot-cell align-middle">
                                        Slot ${slot.slotID}<br>
                                        <span class="slot-time">
                                            ${slot.startTime} - ${slot.endTime}
                                        </span>
                                    </td>
                                    <!--
                                        VÒNG LẶP 2: Duyệt từng ngày trong tuần (tạo cột)
                                        weekdays= ["Monday", "Tuesday", ..., "Sunday"]
                                        Mỗi lần tạo 1 ô trong dòng hiện tại
                                    -->
                                    <c:forEach var="day" items="${weekdays}">
                                        <td class="schedule-cell">
                                            <!--
                                                VÒNG LẶP 3: Quét toàn bộ scheduleList để tìm lịch phù hợp
                                                scheduleList = tất cả lịch học được lọc
                                                Mỗi lịch sẽ được kiểm tra xem có thuộc ô (slot+day) hiện tại không
                                            -->
                                            <c:forEach var="s" items="${scheduleList}">
                                                <!--
                                                    Format ngày học thành tên thứ tiếng Anh
                                                    Ví dụ: 2026-03-17 → "Tuesday"
                                                -->
                                                <fmt:setLocale value="en_US" />
                                                <fmt:formatDate value="${s.learningDate}" pattern="EEEE" var="dayInSql"/>
                                                <fmt:formatDate value="${s.learningDate}" pattern="yyyy-MM-dd" var="learningDateStr"/>

                                                <!--
                                                    ĐIỀU KIỆN MATCH: Chỉ render lịch nếu:
                                                    1. s.slot.slotID == slot.slotID (slot của lịch trùng với dòng hiện tại)
                                                    2. dayInSql == day (thứ của lịch trùng với cột hiện tại)

                                                    Ví dụ:
                                                    - Dòng hiện tại: Slot 1
                                                    - Cột hiện tại: Tuesday
                                                    - Lịch s: slotID=1, learningDate=2026-03-18 (Tuesday)
                                                    → MATCH, render lịch này vào ô (Slot1, Tuesday)
                                                -->
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
                                                            <a href="?action=viewDetail&scheduleId=${s.scheduleId}"
                                                               class="schedule-action-btn view"
                                                               title="View Details">
                                                                <i class='bx bx-file-detail'></i> View
                                                            </a>
                                                            <a href="?action=edit&scheduleId=${s.scheduleId}"
                                                               class="schedule-action-btn edit"
                                                               title="Edit Schedule">
                                                                <i class='bx bx-edit'></i> Edit
                                                            </a>
                                                            <a href="?action=delete&scheduleId=${s.scheduleId}"
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

