<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<link href="css/studentSchedule.css" rel="stylesheet"/>

<div class="container-fluid px-4">

    <!-- HEADER -->
    <div class="d-flex justify-content-between align-items-center mt-4 mb-3">
        <h3 class="fw-bold text-uppercase text-primary">
            Weekly Learning Schedule
        </h3>

        <form method="get" action="schedule" class="d-flex">
            <input type="date" name="date"
                   value="${selectedDate}"
                   class="form-control me-2"/>
            <button class="btn btn-primary">Search</button>
        </form>
    </div>

    <!-- TABLE CARD -->
    <div class="card shadow border-0">
        <div class="card-body p-0">

            <table class="table table-bordered text-center align-middle mb-0">

                <!-- TABLE HEADER -->
                <thead class="table-light">
                    <tr>
                        <th style="width:8%;">Slot</th>
                        <c:forEach items="${weekdays}" var="day">
                            <th>${day}</th>
                        </c:forEach>
                    </tr>
                </thead>

                <tbody>

                    <!-- LOOP SLOT -->
                    <c:forEach var="slot" items="${slots}">
                        <tr>

                            <!-- SLOT COLUMN -->
                            <td class="fw-bold bg-light small">
                                Slot ${slot}<br>
                                <span style="font-size:0.7rem">
                                    ${slotTimes[slot]}
                                </span>
                            </td>

                            <!-- LOOP DAY -->
                            <c:forEach var="day" items="${weekdays}">
                                <td class="schedule-cell">

                                    <!-- LOOP SCHEDULE -->
                                    <c:forEach var="s" items="${scheduleList}">
                                        <fmt:setLocale value="en_US"/>
                                        <fmt:formatDate value="${s.learningDate}"
                                                        pattern="EEEE"
                                                        var="dayInSql"/>

                                        <c:if test="${s.slot == slot && dayInSql == day}">

                                            <div class="student-card">

                                                <div class="class-title">
                                                    ${s.classes.className}
                                                </div>

                                                <div class="info-line">
                                                    📍 ${s.room.roomName}
                                                </div>

                                                <div class="course-name">
                                                    ${s.classes.course.courseName}
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