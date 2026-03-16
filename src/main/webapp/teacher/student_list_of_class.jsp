<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<link href="css/student_list_of_class.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4 content-body">

    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="dashboard?action=teacher">Dashboard</a></li>
                <li class="breadcrumb-item">
                    <a href="class">Class Management</a>
                </li>
                <li class="breadcrumb-item active">Student List</li>
            </ol>
        </div>

        <div class="content-header">
            <div>
                <h2 class="page-title">Student List</h2>
                <p class="text-muted small mb-0">
                    Manage and organize student grades for this class
                </p>
            </div>

            <a href="class" class="btn-secondary">
                <i class='bx bx-arrow-left'></i> Back to Class
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


    <div class="card user-table-card border-0 bg-white">
    </div> <div class="row mb-3">
        <div class="col-md-5">
            <div class="input-group shadow-sm border rounded">
                <span class="input-group-text bg-white border-0">
                    <i class='bx bx-search text-muted'></i>
                </span>
                <input type="text" id="studentSearch" class="form-control border-0 ps-0" 
                       placeholder="Search by student name or email...">
            </div>
        </div>
    </div>

    <div class="card user-table-card border-0 shadow-sm bg-white rounded-3">
        <div class="table-responsive">
            <table class="table mb-0 align-middle table-hover">
                <thead class="bg-light text-muted small text-uppercase">
                    <tr>
                        <th class="text-center" style="width:60px">#</th>
                        <th>Student Info</th>
                        <th>Email</th>
                        <th class="text-center">Phone</th>
                        <th class="text-center">Final Score</th>
                        <th class="text-center" style="width:180px">Actions</th>
                    </tr>
                </thead>

                <tbody>
                    <c:forEach var="s" items="${studentList}" varStatus="loop">
                        <c:set var="avg" value="${averageMap[s.userId]}" />
                        <tr class="student-row">
                            <td class="text-center text-muted small">${loop.count}</td>
                            <td>
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

                                    <div>
                                        <div class="fw-bold text-dark student-name">${s.fullName}</div>
                                        
                                    </div>
                                </div>
                            </td>
                            <td class="small text-muted student-email">${s.email}</td>
                            <td class="text-center text-muted small">${s.phone}</td>

                            <td class="text-center">
                                <c:choose>
                                    <c:when test="${avg != null}">
                                        <span class="badge rounded-pill px-3 ${avg < 5 ? 'bg-danger' : 'bg-success'} shadow-sm final-score-value">
                                            <fmt:formatNumber value="${avg}" maxFractionDigits="1" type="number" />
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted fst-italic small">Not graded</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>

                            <td class="text-center">
                                <div class="d-flex justify-content-center align-items-center gap-1">
                                    

                                    <c:choose>
                                        <c:when test="${avg == null}">
                                            <a href="grade?action=enter&studentId=${s.userId}&classId=${classId}" 
                                               class="btn-icon-minimal" title="Enter Grade"><i class='bx bx-plus-circle'></i></a>
                                            </c:when>
                                            <c:otherwise>
                                            <a href="grade?action=edit&studentId=${s.userId}&classId=${classId}" 
                                               class="btn-icon-minimal" title="Edit Grade"><i class='bx bx-edit-alt'></i></a>

                                            <form action="grade" method="post" class="d-inline">
                                                <input type="hidden" name="action" value="delete"/>
                                                <input type="hidden" name="studentId" value="${s.userId}"/>
                                                <input type="hidden" name="classId" value="${classId}"/>
                                                <button type="submit" class="btn-icon-minimal" 
                                                        onclick="return confirm('Delete grades for ${s.fullName}?')">
                                                    <i class='bx bx-trash'></i>
                                                </button>
                                            </form>
                                        </c:otherwise>
                                    </c:choose>
                                    <a href="#" class="btn-icon-minimal" title="Sync"><i class='bx bx-refresh'></i></a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="card-footer bg-white border-top py-3 px-4">
            <div class="d-flex justify-content-between align-items-center">
                <div class="d-flex gap-4 px-3 py-2">
                    <div class="small fw-bold text-dark">
                        Total Students: <span id="totalStudents" class="text-primary">${studentList.size()}</span>
                    </div>
                    <div class="small fw-bold text-success">
                        Passed: <span id="countPassed">0</span> 
                    </div>
                    <div class="small fw-bold text-danger">
                        Failed: <span id="countFailed">0</span> 
                    </div>
                </div>

            </div>
        </div>

    </div>
</div>
</div>

<script src="js/student_list_of_class.js" type="text/javascript"></script>
<script src="js/manageUser.js" type="text/javascript"></script>