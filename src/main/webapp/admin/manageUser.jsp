<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link href="css/manageUser.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4 content-body">

    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
                <li class="breadcrumb-item active" aria-current="page">User Management</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">User Management</h2>
                <p class="text-muted small mb-0">Manage and organize your users</p>
            </div>
            <a href="user?action=add" class="btn btn-add-new">
                <i class='bx bx-user-plus'></i> Add New User
            </a>
        </div>
    </div>

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
                        <th class="text-end">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${userList}" var="u" varStatus="loop">
                        <tr>
                            <td>${loop.count}</td>

                            <td>
                                <div class="user-item">
                                    <img src="${u.avatar != null ? u.avatar : 'images/default-avatar.png'}" 
                                         class="user-avatar" alt="Avatar">
                                    <div class="d-flex flex-column">
                                        <span class="user-name">${u.fullName}</span>
                                        <span class="user-email">${u.email}</span>
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
                                    <input class="form-check-input" type="checkbox" role="switch" 
                                           ${u.status ? 'checked' : ''}>
                                    <label class="form-check-label ms-2 text-secondary small">
                                        ${u.status ? 'Active' : 'Inactive'}
                                    </label>
                                </div>
                            </td>

                            <td class="text-end">
                                <a href="#" class="action-btn"><i class='bx bx-edit'></i></a>
                                <a href="#" class="action-btn delete"><i class='bx bx-trash'></i></a>
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