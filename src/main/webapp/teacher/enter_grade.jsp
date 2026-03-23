<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

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
        <button class="toast-close" onclick="closeToast()"><i class='bx bx-x'></i></button>
    </div>
    <c:remove var="message" scope="session" />
    <c:remove var="messageType" scope="session" />
</c:if>

<div class="container-fluid px-4 content-body">
    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="dashboard?action=teacher">Dashboard</a></li>
                <li class="breadcrumb-item"><a href="student?action=viewByClass&classId=${classId}">Student List</a></li>
                <li class="breadcrumb-item active">Grade Management</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">${scoreMap == null ? "Enter Grade" : "Edit Grade"}</h2>
                <p class="text-muted small mb-0">Manage student skill scores for <b>${className}</b></p>
            </div>
            <a href="student?action=viewByClass&classId=${classId}" class="btn-secondary">
                <i class='bx bx-arrow-left'></i> Back to Student List
            </a>
        </div>
    </div>

    <div class="card user-table-card border-0 bg-white shadow-sm">
        <div class="card-body p-4">
            <form method="post" action="grade">
                <input type="hidden" name="action" value="save"/>
                <input type="hidden" name="studentId" value="${studentId}"/>
                <input type="hidden" name="classId" value="${classId}"/>

                <div class="row mb-4">
                    <div class="col-md-6">
                        <label class="form-label small text-muted">Student Name</label>
                        <input class="form-control bg-light" value="${studentName}" readonly/>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label small text-muted">Class Name</label>
                        <input class="form-control bg-light" value="${className}" readonly/>
                    </div>
                </div>

                <hr class="my-4"/>

                <div class="row g-4">
                    <c:if test="${empty assessmentList}">
                        <div class="col-12 text-center py-3 text-danger">
                            No assessments found for this course. Please check your database.
                        </div>
                    </c:if>
                    <c:forEach var="ass" items="${assessmentList}">
                        <div class="col-md-3">
                            <label class="form-label fw-semibold">${ass.assessmentName} (${ass.weight}%)</label>
                            <input type="number" 
                                   name="score_${ass.assessmentId}" 
                                   class="form-control"
                                   step="0.01" min="0" max="10" 
                                   value="${scoreMap[ass.assessmentName] != null ? scoreMap[ass.assessmentName] : ''}"
                                   placeholder="0.0" />
                        </div>
                    </c:forEach>
                </div>

                <c:if test="${not empty average}">
                    <div class="alert alert-info text-center fw-bold fs-5 mt-4 mb-0">
                        Final Average: ${average}
                    </div>
                </c:if>

                <div class="d-flex align-items-center justify-content-end mt-4 gap-2">
                    <button type="reset" class="btn btn-light border px-4">Reset</button>
                    <button type="submit" class="btn btn-primary px-4" style="background-color: #4e73df; border: none;">
                        ${scoreMap == null ? "Save Grade" : "Update Grade"}
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="js/manageUser.js" type="text/javascript"></script>