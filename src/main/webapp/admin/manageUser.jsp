<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link href="css/manageUser.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4 content-body">

    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
                <li class="breadcrumb-item active" aria-current="page">Room Management</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">User Management</h2>
                <p class="text-muted small mb-0">Manage and organize room</p>
            </div>
            <a href="user?action=add" class="btn btn-add-new">
                <i class='bx bx-user-plus'></i> Add User
            </a>
        </div>
    </div>

    <c:set var="activeUsers" value="0"/>
    <c:set var="inactiveUsers" value="0"/>
    <c:set var="admin" value="0"/>
    <c:forEach var="u" items="${userList}">
        <c:if test="${u.status == true}">
            <c:set var="activeUsers" value="${activeUsers + 1}"/>
        </c:if>
        <c:if test="${u.status == false}">
            <c:set var="inactiveUsers" value="${inactiveUsers + 1}"/>
        </c:if>
    </c:forEach>

    <div class="stat-card-grid">
        <div class="stat-card">
            <div class="stat-info">              
                <p>Total Users</p>
                <h3>${totalUsers}</h3>
            </div>
            <div class="icon-wrapper blue">
                <i class='bx bxs-group'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Active Users</p>
                <h3>${activeUsers}</h3> 
            </div>
            <div class="icon-wrapper green">
                <i class='bx bxs-check-shield'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Inactive Users</p>
                <h3>${inactiveUsers}</h3>
            </div>
            <div class="icon-wrapper cyan">
                <i class='bx bxs-info-shield'></i>
            </div>
        </div>  
        <div class="stat-card">         
            <div class="stat-info">
                <p>Admins</p>
                <h3>30</h3>
            </div>
            <div class="icon-wrapper cyan">
                <i class='bx bxs-crown'></i>
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
            <input type="text" placeholder="Search by name or email...">
        </div>

        <div class="d-flex gap-3">
            <div class="dropdown">
                <button class="custom-select-filter" type="button" data-bs-toggle="dropdown">
                    <i class='bx bx-filter-alt'></i> All Roles <i class='bx bx-chevron-down ms-1'></i>
                </button>
                <ul class="dropdown-menu">
                    <li><a class="dropdown-item" href="#">Admin</a></li>
                    <li><a class="dropdown-item" href="#">Teacher</a></li>
                    <li><a class="dropdown-item" href="#">Student</a></li>
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
                        <th style="width: 30px">#</th>
                        <th>User Info</th>
                        <th>Phone Number</th>
                        <th>Role</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${userList}" var="u" varStatus="loop">
                        <tr>
                            <td>${loop.count}</td>

                            <td>
                                <div class="user-item">
                                    <c:choose>
                                        <c:when test="${u.avatar != null && not empty u.avatar}">
                                            <img src="${u.avatar}" class="user-avatar" alt="Avatar"
                                                 onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">

                                            <c:set var="nameParts" value="${fn:split(u.fullName, ' ')}" />
                                            <c:set var="initials" value="${fn:substring(nameParts[0], 0, 1)}" />
                                            <c:if test="${fn:length(nameParts) > 1}">
                                                <c:set var="initials" value="${initials}${fn:substring(nameParts[fn:length(nameParts)-1], 0, 1)}" />
                                            </c:if>
                                            <c:set var="hash" value="${u.fullName.hashCode()}" />
                                            <c:set var="hue" value="${(hash < 0 ? -hash : hash) % 360}" />

                                            <div class="user-avatar-placeholder" style="display: none; background-color: hsl(${hue}, 65%, 45%); color: white;">
                                                ${initials}
                                            </div>
                                        </c:when>

                                        <c:otherwise>
                                            <c:set var="nameParts" value="${fn:split(u.fullName, ' ')}" />
                                            <c:set var="initials" value="" />

                                            <c:if test="${fn:length(nameParts) > 0}">
                                                <c:set var="initials" value="${fn:substring(nameParts[0], 0, 1)}" />
                                            </c:if>
                                            <c:if test="${fn:length(nameParts) > 1}">
                                                <c:set var="initials" value="${initials}${fn:substring(nameParts[fn:length(nameParts)-1], 0, 1)}" />
                                            </c:if>

                                            <c:set var="hash" value="${u.fullName.hashCode()}" />
                                            <c:set var="hue" value="${(hash < 0 ? -hash : hash) % 360}" />

                                            <div class="user-avatar-placeholder" 
                                                 style="background-color: hsl(${hue}, 65%, 45%);">
                                                ${fn:toUpperCase(initials)}
                                            </div>
                                        </c:otherwise>
                                    </c:choose>

                                    <div class="d-flex flex-column">
                                        <span class="user-name">${u.fullName}</span>
                                        <span class="user-email text-muted small">${u.email}</span>
                                    </div>
                                </div>
                            </td>

                            <td class="text-secondary">${u.phone}</td>

                            <td>
                                <c:choose>
                                    <c:when test="${u.role.roleId == 1}">
                                        <span class="badge badge-admin">Admin</span>
                                    </c:when>
                                    <c:when test="${u.role.roleId == 4}">
                                        <span class="badge badge-teacher">Teacher</span>
                                    </c:when>
                                    <c:when test="${u.role.roleId == 5}">
                                        <span class="badge badge-student">Student</span>
                                    </c:when>
                                    <c:when test="${u.role.roleId == 3}">
                                        <span class="badge badge-saleStaff">Staff</span>
                                    </c:when>
                                    <c:when test="${u.role.roleId == 2}">
                                        <span class="badge badge-academicStaff">Staff</span>
                                    </c:when>
                                </c:choose>
                            </td>

                            <td>
                                <div class="form-check form-switch">
                                    <input class="form-check-input" type="checkbox" role="switch" disabled 
                                           ${u.status ? 'checked' : ''}>
                                    <label class="form-check-label ms-2 text-secondary small">
                                        ${u.status ? 'Active' : 'Inactive'}
                                    </label>
                                </div>
                            </td>

                            <td>
                                <a href="user?action=view&id=${u.userId}" class="action-btn"><i class='bx bx-eye'></i></a>
                                <a href="user?action=update&id=${u.userId}" class="action-btn"><i class='bx bx-edit'></i></a>
                                <a href="user?action=inActivate&id=${u.userId}" class="action-btn delete"><i class='bx bx-lock'></i></a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="d-flex justify-content-between align-items-center p-3 border-top">
            <div class="text-muted small">Showing 1-10 of ${userList.size()} users</div>
            <div>
                <ul class="pagination pagination-sm mb-0">
                    <li class="page-item disabled"><a class="page-link" href="#"><i class='bx bx-chevron-left'></i> Previous</a></li>
                    <li class="page-item active"><a class="page-link" href="#">1</a></li>
                    <li class="page-item"><a class="page-link" href="#">2</a></li>
                    <li class="page-item"><a class="page-link" href="#">3</a></li>
                    <li class="page-item"><a class="page-link" href="#">Next <i class='bx bx-chevron-right'></i></a></li>
                </ul>
            </div>
        </div>
    </div>
</div>
<script src="js/manageUser.js" type="text/javascript"></script>
