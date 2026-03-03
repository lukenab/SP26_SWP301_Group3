<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="en_US"/>

<link href="css/studentSchedule.css" rel="stylesheet"/>

<div class="container-fluid px-4">

    <!-- ============================= -->
    <!-- ===== BREADCRUMB + HEADER ==== -->
    <!-- ============================= -->

    <div class="mb-4 mt-4">

        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item">
                    <a href="dashboard">
                        <i class="bx bx-home-alt"></i>
                    </a>
                </li>
                <li class="breadcrumb-item active">
                    Weekly Schedule
                </li>
            </ol>
        </div>

        <div class="content-header d-flex justify-content-between align-items-center">

            <div>
                <h2 class="page-title mb-1">
                    Weekly Learning Schedule
                </h2>
                <p class="text-muted small mb-0">

                </p>
            </div>

            <!-- FILTER WEEK -->
            <form method="get" action="schedule" class="d-flex align-items-center">

                <input type="hidden" name="action" value="studentView"/>

                <input type="date"
                       name="weekStart"
                       value="${weekStart}"
                       class="form-control me-2"/>

                <button class="btn btn-primary">
                    Search
                </button>
            </form>
        </div>
    </div>

    <!-- ===== TABLE CARD ============ -->
    <!-- ============================= -->

    <div class="card shadow border-0">
        <div class="card-body p-0">

            <div class="table-responsive">
                <table class="table table-bordered text-center align-middle mb-0">

                    <!-- ===== TABLE HEADER ===== -->
                    <thead>
                        <tr>
                            <th style="width:8%;">Slot</th>

                            <c:forEach var="day" items="${weeklySchedule.keySet()}">
                                <th>
                                    <fmt:formatDate value="${day}" pattern="EEEE"/>
                                    <br>
                                    <span style="font-size:0.75rem; color:#6c7a92;">

                                    </span>
                                </th>
                            </c:forEach>
                        </tr>
                    </thead>

                    <!-- ===== TABLE BODY ===== -->
                    <tbody>

                        <c:forEach var="slot" items="${slots}">
                            <tr>

                                <!-- SLOT COLUMN -->
                                <td class="fw-bold small">
                                    Slot ${slot.slotID}<br>
                                    <span>
                                        ${slot.startTime.toString().substring(0,5)}
                                        -
                                        ${slot.endTime.toString().substring(0,5)}
                                    </span>
                                </td>

                                <!-- LOOP 7 DAYS -->
                                <c:forEach var="day" items="${weeklySchedule.keySet()}">

                                    <td class="schedule-cell">

                                        <c:set var="schedule"
                                               value="${weeklySchedule[day][slot.slotID]}" />

                                        <c:if test="${schedule != null}">
                                            <div class="student-card">

                                                <!-- CLASS NAME -->
                                                <div class="class-title">
                                                    ${schedule.classes.className}
                                                </div>

                                                <!-- COURSE -->
                                                <div class="info-line">
                                                    📘 ${schedule.classes.course.courseName}
                                                </div>

                                                <!-- ROOM -->
                                                <div class="info-line">
                                                    📍 ${schedule.room.roomName}
                                                </div>

                                                <!-- LECTURER -->
                                                <div class="course-name">
                                                   ${employeeUsers[schedule.employee.employeeId].fullName}
                                                </div>

                                                <!-- ATTENDANCE -->
                                                <c:if test="${schedule.attendanceStatus}">
                                                    <div class="mt-2">
                                                        <span class="badge bg-success">
                                                            Present
                                                        </span>
                                                    </div>
                                                </c:if>

                                            </div>
                                        </c:if>

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