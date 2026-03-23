<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setLocale value="en_US"/>

<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
<link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>

<style>
    .dashboard-banner{
        background: linear-gradient(90deg,#3b82f6,#2563eb);
        color:white;
        border-radius:16px;
        padding:28px 24px;
        margin-bottom:30px;
    }

    .dashboard-title{
        font-weight:600;
        margin-bottom:25px;
    }

    .today-schedule-card, .summary-card{
        border-radius:16px;
        border:none;
        box-shadow:0 4px 12px rgba(0,0,0,0.08);
        margin-bottom:30px;
        background: white;
        height: 100%; /* Đảm bảo chiều cao bằng nhau */
    }

    .today-schedule-item{
        background:#f8fafc;
        border-radius:12px;
        transition: 0.2s;
    }

    .today-schedule-item:hover {
        background: #e9ecef;
    }

    .today-schedule-time{
        background:#2563eb;
        color:white;
        min-width:100px;
        border-radius:12px;
        text-align:center;
        padding:10px;
        font-weight:600;
    }

    .class-card{
        border-radius:16px;
        overflow:hidden;
        transition:0.25s;
        background: white;
        height: 100%; /* Đảm bảo chiều cao bằng nhau */
    }

    .class-card:hover{
        transform:translateY(-5px);
        box-shadow:0 8px 20px rgba(0,0,0,0.12);
    }

    .summary-box{
        background:#f8f9fa;
        border-radius:14px;
        padding:20px;
        text-align:center;
        transition: 0.2s;
    }

    .summary-box:hover {
        background: #e9ecef;
    }

    .summary-number{
        font-size:28px;
        font-weight:700;
    }

    /* Container chính */
    .dashboard-wrapper {
        background: #f8f9fa;
        min-height: 100vh;
        padding: 20px;
    }

    /* Style cho card header */
    .section-header {
        border-bottom: 2px solid #e9ecef;
        padding-bottom: 15px;
        margin-bottom: 20px;
    }

    .section-header h5 {
        font-weight: 600;
        color: #2c3e50;
        margin: 0;
    }

    /* Style cho progress bar */
    .progress {
        background-color: #e9ecef;
        border-radius: 10px;
    }

    .progress-bar {
        border-radius: 10px;
    }

    /* Empty state styling */
    .empty-schedule {
        text-align: center;
        padding: 40px 20px;
        color: #6c757d;
    }

    .empty-schedule i {
        font-size: 48px;
        margin-bottom: 15px;
        color: #dee2e6;
    }
</style>

<%
    Map<String, Integer> summary = (Map<String, Integer>) request.getAttribute("summary");

    int total = 0;
    int present = 0;
    int absent = 0;
    int late = 0;

    if (summary != null) {
        total = summary.getOrDefault("total", 0);
        present = summary.getOrDefault("present", 0);
        absent = summary.getOrDefault("absent", 0);
        late = summary.getOrDefault("late", 0);
    }

    int rate = total == 0 ? 0 : ((present + late) * 100 / total);
%>

<div class="dashboard-wrapper">
    <div class="container-fluid">
        <!-- WELCOME BANNER -->
        <div class="dashboard-banner">
            <h3><i class='bx bx-sun'></i> Welcome back</h3>
            <p class="mb-2">
                <i class='bx bx-calendar'></i> 
                <fmt:formatDate value="<%= new java.util.Date()%>" pattern="EEEE, MMMM dd, yyyy"/>
            </p>
            <p class="mt-3 mb-0">
                <i class='bx bx-quote-left'></i> 
                "Success is the sum of small efforts repeated day in and day out."
            </p>
        </div>

        <!-- CURRENT CLASSES SECTION -->
        <div class="card today-schedule-card p-4 mb-4">
            <div class="section-header d-flex justify-content-between align-items-center">
                <h5><i class='bx bx-book-open me-2'></i>My Current Classes</h5>
                <c:if test="${not empty studentClasses}">
                    <span class="badge bg-primary">${studentClasses.size()} Classes</span>
                </c:if>
            </div>

            <div class="row g-4">
                <c:choose>
                    <c:when test="${not empty studentClasses}">
                        <c:forEach items="${studentClasses}" var="c" begin="0" end="2">
                            <div class="col-lg-4 col-md-6">
                                <div class="card class-card shadow-sm border-0">
                                    <div class="card-body">
                                        <div class="d-flex justify-content-between align-items-start mb-3">
                                            <div class="fw-bold fs-5 text-primary">
                                                ${c[0].classes.course.courseName}
                                            </div>

                                        </div>

                                        <div class="mb-3">
                                            <div class="d-flex align-items-center mb-2">
                                                <i class='bx bx-user text-muted me-2' style="width: 20px;"></i>
                                                <span>${c[1]}</span>
                                            </div>
                                            <div class="d-flex align-items-center mb-2">
                                                <i class='bx bx-map text-muted me-2' style="width: 20px;"></i>
                                                <span>${c[4]}</span>
                                            </div>
                                            <div class="d-flex align-items-center mb-2">
                                                <i class='bx bx-calendar text-muted me-2' style="width: 20px;"></i>
                                                <span>
                                                    <c:choose>
                                                        <c:when test="${not empty c[2]}">
                                                            ${c[2]}
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="text-muted fst-italic">
                                                                No schedule this week
                                                            </span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </span>
                                            </div>
                                            <div class="d-flex align-items-center">
                                                <i class='bx bx-time text-muted me-2' style="width: 20px;"></i>
                                                <span>${c[3]}</span>
                                            </div>
                                        </div>

                                        <a href="class?action=detail&classId=${c[0].classes.classid}&source=studentDashboard"
                                           class="btn btn-primary w-100">
                                            <i class='bx bx-detail me-1'></i> View Details
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="col-12">
                            <div class="empty-schedule">
                                <i class='bx bx-book-content'></i>
                                <h6>No classes found</h6>
                                <p class="text-muted">You haven't enrolled in any classes yet.</p>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <c:if test="${studentClasses.size() > 3}">
                <div class="text-center mt-4">
                    <a href="class?action=myClasses" class="btn btn-outline-primary btn-sm">
                        View All Classes <i class='bx bx-chevron-right'></i>
                    </a>
                </div>
            </c:if>
        </div>

        <!-- SCHEDULE + SUMMARY SECTION -->
        <div class="row">
            <!-- TODAY SCHEDULE -->
            <div class="col-lg-12">
                <div class="card today-schedule-card p-4">
                    <div class="section-header d-flex justify-content-between align-items-center">
                        <h5><i class='bx bx-calendar-check me-2'></i>Today's Schedule</h5>
                        <span class="badge bg-info">
                            <fmt:formatDate value="<%= new java.util.Date()%>" pattern="EEEE"/>
                        </span>
                    </div>

                    <c:choose>
                        <c:when test="${not empty todaySchedule}">
                            <c:forEach items="${todaySchedule}" var="s">
                                <div class="d-flex align-items-center p-3 mb-3 today-schedule-item">
                                    <div class="today-schedule-time me-3">
                                        <c:if test="${s.slot != null}">
                                            <strong>${s.slot.startTime}</strong><br>
                                            <small>${s.slot.endTime}</small>
                                        </c:if>
                                    </div>
                                    <div class="flex-grow-1">
                                        <div class="fw-bold fs-6">
                                            <c:if test="${s.classes != null}">
                                                ${s.classes.className}
                                            </c:if>
                                        </div>
                                        <div class="text-muted small">
                                            <i class='bx bx-map'></i>
                                            <c:if test="${s.room != null}">
                                                ${s.room.roomName}
                                            </c:if>
                                        </div>
                                    </div>
                                    <div>

                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-schedule">
                                <i class='bx bx-calendar-x'></i>
                                <h6>No classes today</h6>
                                <p class="text-muted">Enjoy your day off!</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</div>