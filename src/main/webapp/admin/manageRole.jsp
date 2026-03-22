<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link href="../css/manageUser.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4 content-body">
    <div aria-label="breadcrumb">
        <ol class="breadcrumb mb-1">
            <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
            <li class="breadcrumb-item active"><a href="user">Role Management</a></li>
        </ol>
    </div>
    <div class="content-header mb-4">
        <div>
            <h2 class="page-title">Role & Permissions Management</h2>
            <p class="text-muted small mb-0">Manage system access levels for different roles.</p>
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
        <div class="table-responsive">
            <table class="table mb-0 align-middle">
                <thead>
                    <tr>
                        <th style="width: 50px">ID</th>
                        <th>Role Name</th>
                        <th class="text-center">Manage Users</th>
                        <th class="text-center">Manage Courses</th>
                        <th class="text-center">Manage Finance</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${roleList}" var="r">
                        <tr>
                            <td>${r.roleId}</td>
                            <td><strong>${r.roleName}</strong></td>

                            <td class="text-center">
                                <c:choose>
                                    <c:when test="${r.roleId == 4 || r.roleId == 5}">
                                        <span class="badge bg-light text-muted border">N/A</span>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="form-check form-switch d-flex justify-content-center">
                                            <input class="form-check-input" type="checkbox" disabled 
                                                   ${r.manageUser ? 'checked' : ''}>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </td>

                            <td class="text-center">
                                <c:choose>
                                    <c:when test="${r.roleId == 4 || r.roleId == 5}">
                                        <span class="badge bg-light text-muted border">N/A</span>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="form-check form-switch d-flex justify-content-center">
                                            <input class="form-check-input" type="checkbox" disabled 
                                                   ${r.manageCourse ? 'checked' : ''}>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </td>

                            <td class="text-center">
                                <c:choose>
                                    <c:when test="${r.roleId == 4 || r.roleId == 5}">
                                        <span class="badge bg-light text-muted border">N/A</span>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="form-check form-switch d-flex justify-content-center">
                                            <input class="form-check-input" type="checkbox" disabled 
                                                   ${r.manageFinance ? 'checked' : ''}>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </td>

                            <td>
                                <c:choose>
                                    <c:when test="${r.roleId == 4 || r.roleId == 5}">
                                        <i style="color: #9CA3AF; font-size: 16px" class='bx bx-lock ms-3' title="System Role (Locked)"></i>
                                    </c:when>
                                    <c:otherwise>
                                        <a class="action-btn" title="Edit Permission"
                                           onclick="openEditRoleModal('${r.roleId}', '${r.roleName}', ${r.manageUser}, ${r.manageCourse}, ${r.manageFinance})">
                                            <i class='bx bx-edit'></i> 
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

<div class="modal fade" id="editRoleModal" tabindex="-1" aria-hidden="true" data-bs-backdrop="false">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Edit Permissions: <span id="modalRoleName" class="text-primary"></span></h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <form action="role" method="POST">
                <input type="hidden" name="action" value="permission">
                <input type="hidden" name="roleId" id="modalRoleId">

                <div class="modal-body">
                    <p class="text-muted small mb-3">Check the boxes to grant access to specific modules.</p>

                    <div class="form-check form-switch mb-3">
                        <input class="form-check-input" type="checkbox" name="manageUser" id="chkUser" style="width: 2.5em; height: 1.25em;">
                        <label class="form-check-label ms-2 mt-1" for="chkUser"><strong>Manage Users</strong> (Add, Edit, Lock Users)</label>
                    </div>

                    <div class="form-check form-switch mb-3">
                        <input class="form-check-input" type="checkbox" name="manageCourse" id="chkCourse" style="width: 2.5em; height: 1.25em;">
                        <label class="form-check-label ms-2 mt-1" for="chkCourse"><strong>Manage Courses</strong> (Add, Edit Classes & Courses)</label>
                    </div>

                    <div class="form-check form-switch mb-3">
                        <input class="form-check-input" type="checkbox" name="manageFinance" id="chkFinance" style="width: 2.5em; height: 1.25em;">
                        <label class="form-check-label ms-2 mt-1" for="chkFinance"><strong>Manage Finance</strong> (View Payments, Vouchers)</label>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-cancel" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary">Save Changes</button>
                </div>
            </form>
        </div>
    </div>
</div>
<script src="js/manageRole.js" type="text/javascript"></script>