<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link href="css/course_list.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4 content-body">

    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
                <li class="breadcrumb-item active" aria-current="page">Class Management</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">Class Management</h2>
                <p class="text-muted small mb-0">Manage classes and assign students.</p>
            </div>
            <a href="enrollment?action=createClassForm" class="btn btn-add-new">
                <i class='bx bx-user-plus'></i> Create New Class
            </a>
        </div>
    </div>

    <c:set var="activeClass" value="0"/>
    <c:set var="inactiveClass" value="0"/>

    <c:forEach items="${classList}" var="c">
        <c:if test="${c[6] == 'Active'}">
            <c:set var="activeClass" value="${activeClass + 1}"/>
        </c:if>
        <c:if test="${c[6] != 'Active'}">
            <c:set var="inactiveClass" value="${inactiveClass + 1}"/>
        </c:if>
    </c:forEach>

    <div class="stat-card-grid">
        <div class="stat-card">
            <div class="stat-info">
                <p>Total Classes</p>
                <h3>${classList.size()}</h3>
            </div>
            <div class="icon-wrapper blue">
                <i class='bx bxs-school'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Active Class</p>
                <h3>${activeClass}</h3>
            </div>
            <div class="icon-wrapper green">
                <i class='bx bxs-check-shield'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Inactive Class</p>
                <h3>${inactiveClass}</h3>
            </div>
            <div class="icon-wrapper cyan">
                <i class='bx bxs-info-shield'></i>
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

    <div class="filter-container flex-wrap">
        <div class="custom-search-bar">
            <i class='bx bx-search text-muted fs-5'></i>
            <input type="text" placeholder="Search by class name or teacher...">
        </div>

        <div class="d-flex gap-3">
            <div class="dropdown">
                <button class="custom-select-filter" type="button" data-bs-toggle="dropdown">
                    <i class='bx bx-filter-alt'></i> All Classes <i class='bx bx-chevron-down ms-1'></i>
                </button>
                <ul class="dropdown-menu">
                    <li><a class="dropdown-item" href="#">Newest</a></li>
                    <li><a class="dropdown-item" href="#">Oldest</a></li>
                </ul>
            </div>

            <div class="dropdown">
                <button class="custom-select-filter d-flex align-items-center gap-2" type="button" data-bs-toggle="dropdown">
                    <i class='bx bx-slider-alt'></i> All Status <i class='bx bx-chevron-down ms-1'></i>
                </button>
                <ul class="dropdown-menu">
                    <li><a class="dropdown-item" href="#">Active</a></li>
                    <li><a class="dropdown-item" href="#">Inactive</a></li>
                </ul>
            </div>
        </div>
    </div>

    <div class="card user-table-card border-0 bg-white">
        <div class="table-responsive">
            <table class="table mb-0 align-middle">
                <thead>
                    <tr>
                        <th style="width: 5%">#</th>
                        <th style="width: 25%">Class Info</th>
                        <th style="width: 20%">Course</th>
                        <th style="width: 15%">Teacher</th>
                        <th style="width: 20%">Schedule</th>
                        <th style="width: 10%">Status</th>
                        <th style="width: 5%">Students</th>
                        <th style="width: 10%">Actions</th>
                    </tr>
                </thead>

                <tbody>
                    <c:if test="${empty classList}">
                        <tr>
                            <td colspan="8" class="text-center text-muted py-4">No classes found.</td>
                        </tr>
                    </c:if>

                    <c:forEach items="${classList}" var="c" varStatus="loop">
                        <tr>
                            <c:set var="isActive" value="${c[6] == 'Active'}"/>
                            <c:set var="displayStatus" value="${isActive ? 'Active' : 'Inactive'}"/>
                            <td>${loop.count}</td>

                            <td>
                                <div class="user-item">
                                    <div class="user-avatar-placeholder" style="background:#3b82f6;">
                                        ${fn:substring(c[1], 0, 1)}
                                    </div>
                                    <div class="d-flex flex-column">
                                        <span class="user-name">${c[1]}</span>
                                    </div>
                                </div>
                            </td>

                            <td>${c[2]}</td>
                            <td>${empty c[3] ? 'N/A' : c[3]}</td>
                            <td>
                                <fmt:formatDate value="${c[4]}" pattern="dd/MM/yyyy"/>
                                -
                                <fmt:formatDate value="${c[5]}" pattern="dd/MM/yyyy"/>
                            </td>

                            <td>
                                <div class="form-check form-switch">
                                    <input class="form-check-input" type="checkbox" role="switch" disabled ${isActive ? 'checked' : ''}>
                                    <label class="form-check-label ms-2 text-secondary small">
                                        ${displayStatus}
                                    </label>
                                </div>
                            </td>

                            <td>
                                <span class="text-secondary">${c[7]}</span>
                            </td>

                            <td>
                                <a href="enrollment?action=addStudentForm&classId=${c[0]}" class="action-btn" title="Add Student">
                                    <i class='bx bx-user-plus'></i>
                                </a>
                                <form action="enrollment" method="post" style="display:inline;">
                                    <input type="hidden" name="action" value="updateClassStatus">
                                    <input type="hidden" name="classId" value="${c[0]}">
                                    <c:choose>
                                        <c:when test="${isActive}">
                                            <input type="hidden" name="status" value="Inactive">
                                            <button type="submit" class="action-btn delete p-0 bg-transparent border-0" title="Set Inactive">
                                                <i class='bx bx-lock'></i>
                                            </button>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="hidden" name="status" value="Active">
                                            <button type="submit" class="action-btn p-0 bg-transparent border-0" title="Set Active">
                                                <i class='bx bx-lock-open'></i>
                                            </button>
                                        </c:otherwise>
                                    </c:choose>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="d-flex justify-content-between align-items-center p-3 border-top">
            <div class="text-muted small">Showing ${classList.size()} classes</div>
        </div>
    </div>
</div>
<script src="js/manageUser.js" type="text/javascript"></script>
