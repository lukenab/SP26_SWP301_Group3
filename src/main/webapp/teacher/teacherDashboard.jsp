<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link href="css/teacherDashboard.css" rel="stylesheet" type="text/css"/>

<div id="teacher-dashboard" class="container-fluid px-4 content-body teacher-dashboard-page">
    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="dashboard?action=teacher"><i class="bx bx-home-alt"></i></a></li>
                <li class="breadcrumb-item active" aria-current="page">Teacher Dashboard</li>
            </ol>
        </div>
        <div class="content-header teacher-header-panel">
            <div>
                <h2 class="page-title">Teacher Dashboard</h2>
                <p class="text-muted small mb-0">Track your classes, attendance progress, and latest feedback in one place.</p>
            </div>

            <div class="teacher-date-chip">
                <div class="teacher-date-icon">
                    <i class='bx bx-calendar text-primary fs-4'></i>
                </div>
                <div class="teacher-date-text">
                    <jsp:useBean id="now" class="java.util.Date" />
                    <div class="teacher-date-value">
                        <fmt:formatDate value="${now}" pattern="yyyy-MM-dd" />
                    </div>
                    <div class="teacher-date-subtitle">Academic Year 2026</div>
                </div>
            </div>
        </div>
    </div>

    <c:if test="${not empty nextUnansweredSlot}">
        <div class="alert alert-primary border-0 shadow-sm d-flex align-items-center rounded-3 mb-4 teacher-reminder" role="alert">
            <i class='bx bxs-bell-ring fs-4 me-3 bx-tada text-primary'></i>
            <div class="small">
                You have a class <strong>${nextUnansweredSlot.classes.className}</strong> at <strong>${nextUnansweredSlot.slot.startTime}</strong> in <strong>${nextUnansweredSlot.room.roomName}</strong>.
                <a href="attendance?action=take&scheduleId=${nextUnansweredSlot.scheduleId}&classId=${nextUnansweredSlot.classes.classid}" class="alert-link ms-2">Go to Attendance ?</a>
            </div>
        </div>
    </c:if>

    <c:if test="${not empty sessionScope.message}">
        <div class="custom-toast toast-${sessionScope.messageType}" id="toastMessage">
            <div class="toast-icon">
                <c:choose>
                    <c:when test="${sessionScope.messageType == 'success'}">
                        <i class='bx bx-check-circle'></i>
                    </c:when>
                    <c:otherwise>
                        <i class='bx bx-cross-circle'></i>
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

    <div class="stat-card-grid teacher-kpi-grid">
        <div class="stat-card">
            <div class="stat-info">
                <p>Total Classes</p>
                <h3>${teacherClasses.size()}</h3>
            </div>
            <div class="icon-wrapper blue">
                <i class='bx bx-door-open'></i>
            </div>
        </div>

        <div class="stat-card">
            <div class="stat-info">
                <p>Total Students</p>
                <h3>${totalStudents}</h3>
            </div>
            <div class="icon-wrapper green">
                <i class='bx bx-group'></i>
            </div>
        </div>

        <div class="stat-card">
            <div class="stat-info">
                <p>Slots Taught</p>
                <h3>${totalSlotsTaught}</h3>
            </div>
            <div class="icon-wrapper cyan">
                <i class='bx bx-briefcase'></i>
            </div>
        </div>

        <div class="stat-card">
            <div class="stat-info">
                <p>Today's Slots</p>
                <h3>${todaySlots.size()}</h3>
            </div>
            <div class="icon-wrapper red">
                <i class='bx bx-calendar-event'></i>
            </div>
        </div>

        <div class="stat-card">
            <div class="stat-info">
                <p>Avg Rating</p>
                <h3>${avgRating}</h3>
            </div>
            <div class="icon-wrapper orange">
                <i class='bx bx-star'></i>
            </div>
        </div>
    </div>

    <div class="row g-3 mb-4">
        <div class="col-12">
            <div class="card user-table-card border-0 bg-white teacher-quick-links">
                <div class="card-body py-3">
                    <div class="d-flex align-items-center gap-3 flex-wrap">
                        <span class="fw-bold text-muted small text-uppercase">Quick Links:</span>

                        <a href="schedule" class="btn btn-white btn-sm shadow-sm border rounded-pill px-3 py-1 fw-bold">
                            <i class='bx bx-calendar-event text-primary'></i> Schedule
                        </a>

                        <div class="dropdown">
                            <button class="btn btn-white btn-sm shadow-sm border rounded-pill px-3 py-1 fw-bold dropdown-toggle"
                                    type="button" id="attendanceDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                                <i class='bx bx-spreadsheet text-info'></i> Attendance Reports
                            </button>
                            <ul class="dropdown-menu shadow border-0 mt-2" aria-labelledby="attendanceDropdown" style="border-radius: 12px;">
                                <c:if test="${empty teacherClasses}">
                                    <li><a class="dropdown-item small text-muted" href="#">No classes assigned</a></li>
                                    </c:if>
                                    <c:forEach var="c" items="${teacherClasses}">
                                    <li>
                                        <a class="dropdown-item d-flex justify-content-between align-items-center py-2"
                                           href="attendance?action=report&classId=${c.classid}">
                                            <span>${c.className}</span>
                                            <i class='bx bx-chevron-right small text-muted'></i>
                                        </a>
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>

                        <div class="dropdown">
                            <button class="btn btn-white btn-sm shadow-sm border rounded-pill px-3 py-1 fw-bold dropdown-toggle"
                                    type="button" id="gradeDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                                <i class='bx bx-bar-chart-alt-2 text-success'></i> Grade Reports
                            </button>
                            <ul class="dropdown-menu shadow border-0 mt-2" aria-labelledby="gradeDropdown" style="border-radius: 12px;">
                                <c:if test="${empty teacherClasses}">
                                    <li><a class="dropdown-item small text-muted" href="#">No classes assigned</a></li>
                                    </c:if>
                                    <c:forEach var="c" items="${teacherClasses}">
                                    <li>
                                        <a class="dropdown-item d-flex justify-content-between align-items-center py-2"
                                           href="grade?action=report&classId=${c.classid}">
                                            <span>${c.className}</span>
                                            <i class='bx bx-chevron-right small text-muted'></i>
                                        </a>
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>

                        <a href="feedback" class="btn btn-white btn-sm shadow-sm border rounded-pill px-3 py-1 fw-bold">
                            <i class='bx bx-message-rounded-dots text-warning'></i> Feedbacks
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="row g-4">
        <div class="col-lg-8">
            <div class="card border-0 shadow-sm mb-4 rounded-3 overflow-hidden teacher-main-card">
                <div class="card-header bg-white border-0 py-3">
                    <h5 class="fw-bold mb-0 text-dark"><i class='bx bx-list-ul me-2 text-primary'></i>Today's Teaching Plan</h5>
                </div>
                <div class="table-responsive">
                    <table class="table align-middle mb-0 table-hover">
                        <thead class="bg-light small text-muted text-uppercase">
                            <tr>
                                <th class="ps-3" style="width: 25%;">Slot / Time</th>
                                <th style="width: 45%;">Class & Room</th>
                                <th class="text-center" style="width: 30%;">Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:if test="${empty todaySlots}">
                                <tr><td colspan="3" class="text-center py-5 text-muted fst-italic">No classes scheduled for today. Enjoy your break!</td></tr>
                            </c:if>
                            <c:forEach var="s" items="${todaySlots}">
                                <tr>
                                    <td class="ps-3">
                                        <div class="fw-bold text-dark">Slot ${s.slot.slotID}</div>
                                        <div class="text-muted small">${s.slot.startTime} - ${s.slot.endTime}</div>
                                    </td>
                                    <td>
                                        <div class="fw-bold text-primary">${s.classes.className}</div>
                                        <div class="small text-muted"><i class='bx bx-map-pin text-danger'></i> Room: ${s.room.roomName}</div>
                                    </td>
                                    <td class="text-center">
                                        <c:choose>
                                            <c:when test="${s.attendanceStatus}">
                                                <span class="badge bg-success bg-opacity-10 text-success rounded-pill px-3 py-2 border border-success done-badge">
                                                    <i class='bx bx-check-circle'></i> Done
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="attendance?action=take&scheduleId=${s.scheduleId}&classId=${s.classes.classid}" 
                                                   class="btn btn-warning btn-sm rounded-pill take-attendance-btn">
                                                    Take Attendance
                                                </a>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="card border-0 shadow-sm p-4 rounded-3 teacher-main-card">
                <h5 class="fw-bold mb-3 text-dark"><i class='bx bx-trending-up me-2 text-success'></i>Active Classes Progress</h5>
                <div class="row g-3">
                    <c:forEach var="c" items="${teacherClasses}">
                        <div class="col-md-6">
                            <div class="p-3 border rounded-3 bg-light shadow-sm-hover transition">
                                <div class="d-flex justify-content-between mb-2">
                                    <span class="fw-bold text-dark">${c.className}</span>
                                    <a href="attendance?action=report&classId=${c.classid}" class="text-primary small fw-bold text-decoration-none">
                                        <i class='bx bx-chart'></i> Report
                                    </a>
                                </div>
                                <div class="progress shadow-sm" style="height: 8px; border-radius: 10px;">
                                    <div class="progress-bar progress-bar-striped progress-bar-animated bg-primary"  
                                         role="progressbar" style="width: ${progressMap[c.classid]}%"></div>
                                </div>
                                <div class="mt-2 small text-muted text-truncate" style="font-size: 0.7rem;">
                                    Course: ${c.course.courseName}
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>

        <div class="col-lg-4">
            <div class="card border-0 shadow-sm h-100 rounded-3 overflow-hidden teacher-main-card teacher-feedback-card">
                <div class="card-header bg-white border-0 py-3 d-flex justify-content-between align-items-center">
                    <h5 class="fw-bold mb-0 text-dark"><i class='bx bx-message-rounded-dots me-2 text-warning'></i>Latest Feedbacks</h5>
                    <span class="badge bg-warning text-dark small rounded-pill">New</span>
                </div>
                <div class="card-body p-0">
                    <c:if test="${empty latestFeedbacks}">
                        <div class="text-center py-5 text-muted fst-italic small">No feedbacks received yet.</div>
                    </c:if>
                    <c:forEach var="f" items="${latestFeedbacks}">
                        <div class="p-3 border-bottom feedback-item transition">
                            <div class="d-flex justify-content-between mb-2">
<!--                                <span class="fw-bold small text-dark">Student #${(f.feedbackId * 73 + 19) % 9000 + 1000}</span>-->
                                <div class="text-warning small" style="font-size: 0.65rem;">
                                    <c:forEach begin="1" end="${f.rating}"><i class='bx bxs-star'></i></c:forEach>
                                    </div>
                                </div>
                                <p class="small text-muted mb-0 fst-italic" style="line-height: 1.4;">"${f.comment}"</p>
                        </div>
                    </c:forEach>
                </div>
                <div class="card-footer bg-white border-0 text-center pb-3">
                    <a href="feedback" class="small fw-bold text-primary text-decoration-none">
                        See all student feedbacks <i class='bx bx-chevron-right'></i>
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

