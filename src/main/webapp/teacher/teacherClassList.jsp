<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!--<link href="css/manageUser.css" rel="stylesheet" type="text/css"/>-->

<div class="container-fluid px-4 content-body">

    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="dashboard?action=teacher">Dashboard</a></li>
                <li class="breadcrumb-item active" aria-current="page">My Classes</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">My Assigned Classes</h2>
                <p class="text-muted small mb-0">Manage your teaching schedule and student attendance</p>
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

    <c:set var="activeCount" value="0"/>
    <c:set var="pendingCount" value="0"/>
    <c:set var="inactiveCount" value="0"/>
    <c:forEach var="c" items="${ClassList}">
        <c:if test="${c.status == 'Active'}"><c:set var="activeCount" value="${activeCount + 1}"/></c:if>
        <c:if test="${c.status == 'Pending'}"><c:set var="pendingCount" value="${pendingCount + 1}"/></c:if>
        <c:if test="${c.status == 'Inactive'}"><c:set var="inactiveCount" value="${inactiveCount + 1}"/></c:if>
    </c:forEach>

    <div class="stat-card-grid">
        <div class="stat-card">
            <div class="stat-info">              
                <p>Total Classes</p>
                <h3>${ClassList.size()}</h3>
            </div>
            <div class="icon-wrapper blue">
                <i class='bx bx-door-open'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Active Classes</p>
                <h3>${activeCount}</h3> 
            </div>
            <div class="icon-wrapper green">
                <i class='bx bx-lock-open-alt'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Pending Classes</p>
                <h3>${pendingCount}</h3>
            </div>
            <div class="icon-wrapper cyan">
                <i class='bx bx-clock-dashed-half'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Inactive Classes</p>
                <h3>${inactiveCount}</h3>
            </div>
            <div class="icon-wrapper gray">
                <i class='bx bx-lock'></i>
            </div>
        </div>
    </div>

    <div class="filter-container flex-wrap mt-4">
        <div class="custom-search-bar">
            <i class='bx bx-search text-muted fs-5'></i>
            <input type="text" id="searchInput" placeholder="Search class by name...">
        </div>
        <div class="d-flex gap-3">
            <select id="statusFilter" class="custom-select-filter" style="border: 1px solid #e2e8f0; padding: 8px 16px; border-radius: 8px; outline: none; cursor: pointer;">
                <option value="all">All Status</option>
                <option value="Active">Active</option>
                <option value="Pending">Pending</option>
                <option value="Inactive">Inactive</option>
            </select>
        </div>
    </div>

    <div class="card user-table-card border-0 bg-white">
        <div class="table-responsive">
            <table class="table mb-0 align-middle">
                <thead>
                    <tr>
                        <th style="width: 30px">#</th>
                        <th>Class Information</th>
                        <th>Start Date</th>
                        <th>End Date</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody id="classTableBody">
                    <tr id="noResultRow" style="display: none;">
                        <td colspan="6" class="text-center py-4 text-muted">No classes match your search.</td>
                    </tr>
                    <c:forEach items="${ClassList}" var="c" varStatus="loop">
                        <tr class="class-row">
                            <td>${loop.count}</td>
                            <td>
                                <div class="user-item">
                                    <div class="user-avatar-placeholder" style="background-color: #4e73df; color: white; width: 40px; height: 40px; display: flex; align-items: center; justify-content: center; border-radius: 8px;">
                                        ${c.className.substring(0,2)}
                                    </div>
                                    <div class="d-flex flex-column ms-2">
                                        <span class="user-name fw-bold">${c.className}</span>
                                        <span class="text-muted small">Course ID: ${c.course.courseId}</span>
                                    </div>
                                </div>
                            </td>
                            <td class="text-secondary">${c.startDate}</td>
                            <td class="text-secondary">${c.endDate}</td>
                            <td>
                                <span class="badge status-text ${c.status == 'Active' ? 'badge-teacher' : 'badge-academicStaff'}">
                                    ${c.status}
                                </span>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${c.status == 'Pending'}">
                                        <a href="student?action=viewByClass&classId=${c.classid}" class="action-btn" title="Student List">
                                            <i class='bx bx-group'></i>
                                        </a>
                                    </c:when>
                                    <c:when test="${c.status == 'Inactive'}">
                                        <a href="student?action=viewByClass&classId=${c.classid}" class="action-btn" title="Student List">
                                            <i class='bx bx-group'></i>
                                        </a>
                                        <a href="attendance?action=report&classId=${c.classid}" class="action-btn" title="Attendance Report">
                                            <i class='bx bx-receipt'></i>
                                        </a>
                                        <a href="grade?action=report&classId=${c.classid}" class="action-btn" title="Grade Report">
                                            <i class='bx bx-bar-chart-square'></i>
                                        </a>
                                        <a href="feedback?action=viewAll&classId=${c.className}&from=myClasses" class="action-btn btn-feedback" title="View Feedbacks">
                                            <i class='bx bx-star'></i>
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="schedule?action=viewScheduleByClassId&classId=${c.classid}" class="action-btn" title="View Schedule">
                                            <i class='bx bx-calendar'></i>
                                        </a>
                                        <a href="student?action=viewByClass&classId=${c.classid}" class="action-btn" title="Student List">
                                            <i class='bx bx-group'></i>
                                        </a>
                                        <a href="attendance?action=report&classId=${c.classid}" class="action-btn" title="Attendance Report">
                                            <i class='bx bx-receipt'></i>
                                        </a>
                                        <a href="grade?action=report&classId=${c.classid}" class="action-btn" title="Grade Report">
                                            <i class='bx bx-bar-chart-square'></i>
                                        </a>
                                        <a href="feedback?action=viewAll&classId=${c.className}&from=myClasses" class="action-btn btn-feedback" title="View Feedbacks">
                                            <i class='bx bx-star'></i>
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
</div>

<script src="js/teacherClassList.js" type="text/javascript"></script>
<script src="js/manageUser.js" type="text/javascript"></script>