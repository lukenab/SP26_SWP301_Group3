<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link href="css/takeAttendance.css" rel="stylesheet" type="text/css"/>
<link href="css/manageUser.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4 content-body">

    <div class="mb-4 mt-3">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="dashboard">Dashboard</a></li>
                <li class="breadcrumb-item"><a href="class">Class Management</a></li>
                <li class="breadcrumb-item"><a href="schedule?action=view&classId=${classId}">Class Schedule</a></li>
                <li class="breadcrumb-item active">Take Attendance</li>
            </ol>
        </div>

        <div class="content-header d-flex justify-content-between align-items-center">
            <div>
                <h2 class="page-title fw-bold text-dark mb-1">Take Attendance</h2>
                <p class="text-muted small mb-0">Select status for each student in this session</p>
            </div>


            <a href="schedule?action=view&classId=${classId}" class="btn-secondary">
                <i class='bx bx-arrow-left'></i> Back to Schedule
            </a>
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
                <span class="toast-title">${sessionScope.messageType == 'success' ? 'Success!' : 'Error!'}</span>
                <span class="toast-message">${sessionScope.message}</span>
            </div>
            <button type="button" class="toast-close" onclick="closeToast()">
                <i class='bx bx-x'></i>
            </button>
        </div>

        <c:remove var="message" scope="session" />
        <c:remove var="messageType" scope="session" />
    </c:if>

    <div class="card shadow-sm border-0 rounded-3 overflow-hidden">
        <form method="post" action="attendance?action=save">
            <input type="hidden" name="scheduleId" value="${scheduleId}" />
            <input type="hidden" name="classId" value="${classId}" />

            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead class="bg-light text-muted small text-uppercase">
                        <tr>
                            <th class="ps-4" style="width: 35%">Student Information</th>
                            <th class="text-center" style="width: 30%">Attendance Status</th>
                            <th class="pe-4" style="width: 35%">Notes</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="s" items="${studentList}">

                            <c:set var="att"
                                   value="${attendanceMap[s.userId]}" />

                            <tr>
                                <td class="ps-4">
                                    <div class="fw-bold text-dark">${s.fullName}</div>
                                    <small class="text-muted">ID: ${s.userId}</small>
                                </td>

                                <td>
                                    <div class="d-flex justify-content-center gap-3">

                                        <!-- Present -->
                                        <div class="form-check custom-option">
                                            <input class="form-check-input d-none" type="radio"
                                                   name="status_${att.attendanceId}"
                                                   value="Present"
                                                   id="p_${att.attendanceId}"
                                                   ${att.status == 'Present' ? 'checked' : ''}>
                                            <label class="btn btn-outline-success btn-sm px-4 rounded-pill fw-bold"
                                                   for="p_${att.attendanceId}">
                                                Present
                                            </label>
                                        </div>

                                        <!-- Absent -->
                                        <div class="form-check custom-option">
                                            <input class="form-check-input d-none" type="radio"
                                                   name="status_${att.attendanceId}"
                                                   value="Absent"
                                                   id="a_${att.attendanceId}"
                                                   ${att.status == 'Absent' ? 'checked' : ''}>
                                            <label class="btn btn-outline-danger btn-sm px-4 rounded-pill fw-bold"
                                                   for="a_${att.attendanceId}">
                                                Absent
                                            </label>
                                        </div>

                                    </div>
                                </td>

                                <td class="pe-4">
                                    <input type="text"
                                           name="note_${att.attendanceId}"
                                           value="${att.note}"
                                           class="form-control form-control-sm"
                                           placeholder="Optional note..."/>
                                </td>

                        <input type="hidden"
                               name="attId"
                               value="${att.attendanceId}" />

                        </tr>

                    </c:forEach>

                    <c:if test="${empty studentList}">
                        <tr>
                            <td colspan="3" class="text-center py-5 text-muted">
                                No students found in this class.
                            </td>
                        </tr>
                    </c:if>
                    </tbody>
                </table>
            </div>

            <div class="card-footer bg-white border-0 text-end py-3 px-4">
                <button type="submit" class="btn btn-primary px-5 shadow-sm fw-bold">
                    <i class='bx bx-save me-1'></i> Confirm & Save
                </button>
            </div>
        </form>
    </div>
</div>

<script src="js/manageUser.js" type="text/javascript"></script>
