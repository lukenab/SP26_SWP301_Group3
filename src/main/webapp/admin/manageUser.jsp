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
    <c:set var="totalAdmins" value="0"/>
    <c:forEach var="u" items="${userList}">
        <c:if test="${u.status == true}">
            <c:set var="activeUsers" value="${activeUsers + 1}"/>
        </c:if>
        <c:if test="${u.status == false}">
            <c:set var="inactiveUsers" value="${inactiveUsers + 1}"/>
        </c:if>
        <c:if test="${u.role.roleId == 1}">
            <c:set var="totalAdmins" value="${totalAdmins + 1}"/>
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
                <h3>${totalAdmins}</h3>
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

    <form action="user" method="GET" class="filter-container flex-wrap">
        <input type="hidden" name="action" value="all">

        <div class="custom-search-bar">
            <i class='bx bx-search text-muted fs-5'></i>
            <input type="text" name="searchQuery" value="${param.searchQuery}" placeholder="Search by name or email..." onchange="this.form.submit()">
        </div>

        <div class="d-flex gap-3">
            <select name="roleId" class="custom-select-filter" onchange="this.form.submit()" style="border: 1px solid #e2e8f0; padding: 8px 16px; border-radius: 8px; background: white; outline: none; cursor: pointer;">
                <option value="">All Roles</option>
                <option value="1" ${param.roleId == '1' ? 'selected' : ''}>Admin</option>
                <option value="2" ${param.roleId == '2' ? 'selected' : ''}>Academic Staff</option>
                <option value="3" ${param.roleId == '3' ? 'selected' : ''}>Sale Staff</option>
                <option value="4" ${param.roleId == '4' ? 'selected' : ''}>Teacher</option>
                <option value="5" ${param.roleId == '5' ? 'selected' : ''}>Student</option>
            </select>

            <select name="status" class="custom-select-filter" onchange="this.form.submit()" style="border: 1px solid #e2e8f0; padding: 8px 16px; border-radius: 8px; background: white; outline: none; cursor: pointer;">
                <option value="">All Status</option>
                <option value="1" ${param.status == '1' ? 'selected' : ''}>Active</option>
                <option value="0" ${param.status == '0' ? 'selected' : ''}>Inactive</option>
            </select>

            <button type="submit" style="display: none;"></button>
        </div>
    </form>

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
                                <a href="user?action=view&id=${u.userId}" class="action-btn" title="View User Details"><i class='bx bx-eye'></i></a>
                                <a href="user?action=update&id=${u.userId}" class="action-btn" title="Update User"><i class='bx bx-edit'></i></a>
                                <a href="user?action=inActivate&id=${u.userId}" class="action-btn delete" title="Deactivate User"><i class='bx bx-power'></i></a>
                                    <c:choose>
                                        <c:when test="${u.isLocked}">
                                        <a form="lockPassForm" type="button" class="action-btn delete" title="Unlock User" 
                                           onclick="openLockModal('${u.userId}', '${fn:escapeXml(u.fullName)}', false)">
                                            <i class='bx bxs-lock'></i>
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <a form="lockPassForm" type="button" class="action-btn" title="Lock User" 
                                           onclick="openLockModal('${u.userId}', '${fn:escapeXml(u.fullName)}', true)">
                                            <i class='bx bx-lock-open-alt'></i>
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                                <a type="button" class="action-btn" title="Reset User Password" 
                                   onclick="openResetModal('${u.userId}', '${u.email}', '${fn:escapeXml(u.fullName)}')">
                                    <i class='bx bx-refresh-ccw'></i> 
                                </a>
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

<div class="modal fade" id="resetPasswordModal" tabindex="-1" aria-labelledby="resetModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="resetModalLabel">
                    <i class='bx bx-error-circle'></i> Confirm Password Reset
                </h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>

            <form action="user?action=resetPassword" method="POST" id="resetPassForm" onsubmit="showLoadingBtn()">
                <div class="modal-body">
                    <p>
                        Are you sure you want to reset the password for <strong id="resetUserName" style="color: #0f172a;"></strong>?
                    </p>
                    <div class="modal-body-content">
                        <p class="mb-0">
                            <i class='bx bx-envelope'></i> A new randomly generated password will be sent to: <br>
                            <strong id="resetUserEmail" style="color: #2563eb;"></strong>
                        </p>
                    </div>

                    <input type="hidden" name="userId" id="modalUserId">
                    <input type="hidden" name="email" id="modalUserEmailInput">
                </div>

                <div class="modal-footer">
                    <a type="button" class="btn btn-cancel" data-bs-dismiss="modal">Cancel</a>
                    <button type="submit" id="confirmResetBtn" class="btn btn-save">
                        Reset Password
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>
            
<div class="modal fade" id="lockUserModal" tabindex="-1" aria-labelledby="lockModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
       <div class="modal-content">
            <div class="modal-header" id="lockModalHeader">
                <h5 class="modal-title" id="lockModalLabel">
                    <i id="lockModalIcon" class='bx bxs-lock'></i> <span id="lockModalTitleText">Confirm Action</span>
                </h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>

            <form action="user" method="Post" id="lockPassForm">
                <input type="hidden" name="action" value="toggleLock">
                <input type="hidden" name="id" id="lockModalUserId">
                <input type="hidden" name="val" id="lockModalVal">

                <div class="modal-body">
                    <p class="mb-3">
                        Are you sure you want to <strong id="lockActionText"></strong> the account of <strong id="lockUserName" style="color: #0f172a;"></strong>?
                    </p>
                    <div class="modal-body-content" id="lockWarningMessage">
                        <p class="mb-0 small">
                            <i class='bx bx-info-circle'></i> <span id="lockWarningText">This user will no longer be able to log into the system.</span>
                        </p>
                    </div>
                </div>

                <div class="modal-footer">
                    <button type="button" class="btn btn-cancel" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" id="confirmLockBtn" class="btn btn-save">
                        Confirm
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>
<script src="js/manageUser.js" type="text/javascript"></script>
