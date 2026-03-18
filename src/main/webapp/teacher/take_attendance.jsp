<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link href="css/takeAttendance.css" rel="stylesheet" type="text/css"/>


<div class="container-fluid px-4 content-body">

    <div class="mb-4 mt-3">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="dashboard?action=teacher">Dashboard</a></li>
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
                                    <div class="d-flex align-items-center">
                                        <div class="avatar-container me-3">
                                            <c:choose>

                                                <c:when test="${not empty s.avatar}">
                                                    <img src="${pageContext.request.contextPath}/${s.avatar}" 
                                                         class="avatar-img" 
                                                         alt="Student"
                                                         onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">

                                                    <div class="avatar-placeholder" style="display: none;">
                                                        <i class='bx bx-user'></i>
                                                    </div>
                                                </c:when>

                                                <c:otherwise>
                                                    <div class="avatar-placeholder">
                                                        <i class='bx bx-user'></i>
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>

                                        <div class="d-flex flex-column">
                                            <span class="fw-bold text-dark" style="font-size: 0.95rem; line-height: 1.2;">
                                                ${s.fullName}
                                            </span>
                                            <small class="text-muted" style="font-size: 0.75rem; margin-top: 2px;">
                                                ID: ${s.userId}
                                            </small>
                                        </div>
                                    </div>
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

            <div class="card-footer bg-white border-top py-3 px-4">
                <div class="d-flex justify-content-between align-items-center">

                    <div class="d-flex gap-4 px-3 py-2  rounded-pill">
                        <div class="small fw-bold text-dark">
                            Total: <span id="totalStudents" class="text-primary">${studentList.size()}</span>
                        </div>
                        <div class="small fw-bold text-success">
                            Present: <span id="countPresent">0</span>
                        </div>
                        <div class="small fw-bold text-danger">
                            Absent: <span id="countAbsent">0</span>
                        </div>
                    </div>

                    <button type="submit" class="btn btn-primary btn-sm px-4 shadow-sm fw-bold">
                        <i class='bx bx-save me-1'></i> Confirm & Save
                    </button>
                </div>
            </div>

        </form>
    </div>
</div>

<script src="js/manageUser.js" type="text/javascript"></script>
<script src="js/takeAtendance.js" type="text/javascript"></script>