<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link href="css/editUser.css" rel="stylesheet" type="text/css"/>

<div class="mb-4">

    <div class="content-header">
        <div>
            <h1 class="page-title">Edit User</h1>
        </div>
        <a href="user" class="btn-secondary">
            <i class='bx bx-arrow-left'></i> Back to Users
        </a>
    </div>
    <div aria-label="breadcrumb">
        <ol class="breadcrumb mb-1">
            <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
            <li class="breadcrumb-item"><a href="user">User Management</a></li>
            <li class="breadcrumb-item active" aria-current="page">Edit User</li>
        </ol>
    </div>
</div>



<div class="profile-header-card">
    <div class="profile-avatar-section">
        <div class="form-row user-img">
            <div class="info-img">
                <img src="${pageContext.request.contextPath}/${user.avatar}" class="rounded-circle object-fit-cover">
            </div>

            <div class="info-img-icon" onclick="toggleAvatarInput()">
                <i class="bx bx-camera"></i>
            </div>
        </div>

        <div id="avatarInputContainer">
            <div class="form-group mb-0">
                <label>New Avatar URL:</label>
                <div class="avatar-input-content">
                    <input type="file" name="avatarFile" id="avatarVisualInput" class="form-control form-control-sm" accept="image/*" 
                           placeholder="Paste image link here..." onchange="updateAvatar(this)" form="updateUserForm">

                    <button type="button" class="btn-secondary avatar-input-btn" onclick="toggleAvatarInput()">
                        <i class='bx bx-x'></i>
                    </button>
                </div>
            </div>
        </div>
    </div>

    <div class="profile-header-info">
        <h2 class="profile-name">${user.fullName}</h2>
        <span class="profile-department">${user.role.roleName}</span>
        <span class="profile-active">${user.status  ? 'Active' : 'Inactive'}</span>
        <p>${employee.experience}</p>
        <div class="profile-info-content">
            <div class="profile-header-left">
                <span class="user-email"><i class="bx bx-envelope"></i>${user.email}</span>
                <span class="user-email"><i class="bx bx-location"></i>${user.address}</span>   
            </div>
            <div class="profile-header-right">
                <span class="user-email"><i class="bx bx-phone"></i>${user.phone}</span>
                <span class="user-email"><i class="bx bx-calendar-event"></i>Joined <fmt:formatDate value="${employee.hireDate}" pattern="dd-MM-yyyy"/></span>   
            </div>
        </div>
    </div>
</div>

<div class="form-container">
    <form id="updateUserForm" action="user?action=update" method="POST" enctype="multipart/form-data" class="form-body">

        <input type="hidden" name="userId" value="${user.userId}">
        <input type="hidden" name="roleId" value="${user.role.roleId}" >
        <input type="hidden" id="uAvatar" name="avatar" value="${user.avatar}">

        <div class="form-row">
            <div class="form-group">
                <label for="fullName">Full Name</label>
                <input type="text" name="fullName" id="fullName" value="${user.fullName}" required>
            </div>
            <div class="form-group">
                <label for="role">Role</label>
                <input type="text" id="role" value="${user.role.roleName}" readonly>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="phone">Phone</label>
                <input type="number" id="phone" name="phone" value="${user.phone}" required>
            </div>
            <div class="form-group">
                <label for="address">Address</label>
                <input type="text" id="address" name="address" value="${user.address}">
            </div>
        </div>

        <div class="form-row">  
            <div class="form-group">
                <label for="dob">Date of Birth</label>
                <input type="date" id="dob" name="dob" value="${user.dob}">
            </div>
            <div class="form-group">
                <label for="gender" class="form-label">Gender</label>
                <select name="gender" id="gender" required>
                    <option value="true" ${user.gender ? 'selected' : ''}>Male</option>
                    <option value="false" ${!user.gender ? 'selected' : ''}>Female</option>
                </select>
            </div>
        </div>

        <div class="form-row">    
            <c:if test="${user.role.roleId == 2 || user.role.roleId == 3 || user.role.roleId ==4}">
                <div class="form-group">
                    <label for="education">Education</label>
                    <input type="text" name="education" id="education" value="${employee.education}" required>
                </div>
                <div class="form-group">
                    <label for="experience">Experience</label>
                    <input type="text" id="experience" name="experience" value="${employee.experience}">
                </div>
            </c:if>
        </div>

        <div class="form-row">
            <c:if test="${user.role.roleId == 2 || user.role.roleId == 3 || user.role.roleId ==4}">
                <div class="form-group">
                    <label for="hireDate">Hire Date</label>
                    <input type="date" id="hireDate" name="hireDate" value="${employee.hireDate}" required>
                </div>
            </c:if>

        </div>


        <div class="form-buttons">
            <a href="user" class="btn btn-cancel">Cancel</a>
            <button type="submit" class="btn btn-save">
                <i class='bx bx-save'></i> Update Changes
            </button>
        </div>
    </form>
</div>

<script src="js/editUser.js" type="text/javascript"></script>

