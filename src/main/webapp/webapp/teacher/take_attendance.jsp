<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="container-fluid px-4">
    <h3 class="mt-4 mb-3 text-primary fw-bold text-uppercase">Take Attendance</h3>
    
    <div class="card shadow-sm border-0">
        <form method="post" action="attendance?action=save">
            <input type="hidden" name="scheduleId" value="${scheduleId}" />
            <input type="hidden" name="classId" value="${classId}" />

            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead class="table-light">
                        <tr>
                            <th style="width: 30%">Student Name</th>
                            <th style="width: 30%">Status</th>
                            <th style="width: 40%">Note</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="a" items="${attendanceList}">
                            <tr>
                                <td>
                                    <div class="fw-bold">${a.fullName}</div>
                                    <small class="text-muted">ID: ${a.userId}</small>
                                </td>
                                <td>
                                    <div class="d-flex gap-3">
                                        <div class="form-check">
                                            <input class="form-check-input" type="radio" 
                                                   name="status_${a.attendance.attendanceId}" 
                                                   value="Present" id="p_${a.attendance.attendanceId}"
                                                   ${a.attendance.status == 'Present' ? 'checked' : ''}>
                                            <label class="form-check-label" for="p_${a.attendance.attendanceId}">Present</label>
                                        </div>
                                        <div class="form-check">
                                            <input class="form-check-input" type="radio" 
                                                   name="status_${a.attendance.attendanceId}" 
                                                   value="Absent" id="a_${a.attendance.attendanceId}"
                                                   ${a.attendance.status == 'Absent' ? 'checked' : ''}>
                                            <label class="form-check-label" for="a_${a.attendance.attendanceId}">Absent</label>
                                        </div>
                                    </div>
                                </td>
                                <td>
                                    <input type="text" name="note_${a.attendance.attendanceId}" 
                                           value="${a.attendance.note}" 
                                           class="form-control form-control-sm" placeholder="Add note..."/>
                                </td>
                                <input type="hidden" name="attId" value="${a.attendance.attendanceId}" />
                            </tr>
                        </c:forEach>
                        
                        <c:if test="${empty attendanceList}">
                            <tr>
                                <td colspan="3" class="text-center py-4 text-muted">
                                    No students found in this class.
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
            
            <div class="card-footer bg-white border-0 text-end py-3">
                <a href="schedule?action=view" class="btn btn-secondary px-4 me-2">Cancel</a>
                <button type="submit" class="btn btn-primary px-4">Save Changes</button>
            </div>
        </form>
    </div>
</div>