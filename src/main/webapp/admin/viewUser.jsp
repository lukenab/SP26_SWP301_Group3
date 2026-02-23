<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<div class="page-header">
    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
                <li class="breadcrumb-item"><a href="user">Users</a></li>
                <li class="breadcrumb-item active" aria-current="page">User Profile</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">User Management</h2>
                <p class="text-muted small mb-0">Manage and organize your users</p>
            </div>
            <a href="user" class="btn-secondary">
                <i class='bx bx-arrow-left'></i> Back to Users
            </a>
        </div>
    </div>

<div class="profile-header-card">
    <div class="profile-avatar-section">
        <div class="form-row user-img">
            <div class="info-img">
                <img src="${user.avatar}" class="rounded-circle object-fit-cover">
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

    <div class="profile-content-card">
        <div class="profile-tabs">
            <c:if test="${user.role.roleId == 2 || user.role.roleId == 3 || user.role.roleId == 4}">
                <a href="#" class="tab-item active"><i class='bx bxs-user'></i>Employee Information</a>
            </c:if>
            <c:if test="${user.role.roleId == 5}">
                <a href="#" class="tab-item active"><i class='bx bxs-user'></i>Student Information</a>
            </c:if>
        </div>

        <div class="tab-content" id="overview">
            <div class="info-section">
                <div class="info-grid">
                    <div class="info-item">
                        <p>Full Name</p>
                        <span>${user.fullName}</span>
                    </div>
                    <div class="info-item">
                        <p>Date of Birth</p>
                        <span>${not empty user.dob ? user.dob : 'N/A'}</span>
                    </div>
                    <div class="info-item">
                        <p>Gender</p>
                        <span>${user.gender ? 'Male' : 'Female'}</span>
                    </div>
                    <div class="info-item">
                        <p>Email</p>
                        <span>${user.email}</span>
                    </div>
                    <div class="info-item">
                        <p>Phone</p>
                        <span>${user.phone}</span>
                    </div>
                    <div class="info-item">
                        <p>Address</p>
                        <span>${user.address ? user.address : 'N/A'}</span>
                    </div>
                    <c:if test="${user.role.roleId == 5}">
                        <div class="info-item">
                            <p>Enrollment Date</p>
                            <span>${student.enrollmentDate}</span>
                        </div>
                    </c:if>
                </div>
            </div>

            <c:if test="${user.role.roleId == 2 || user.role.roleId == 4 || user.role.roleId == 3}">
                <div class="info-section">
                    <div class="info-grid">
                        <div class="info-item">
                            <p>Hire Date</p>
                            <span><fmt:formatDate value="${employee.hireDate}" pattern="dd/MM/yyyy"/></span>
                        </div>  
                        <div class="info-item">
                            <p>Education</p>
                            <span>${employee.education ? employee.education : 'N/A'}</span>
                        </div>

                        <div class="info-item">
                            <p>Experience</p>
                            <span>${employee.experience ? employee.experience : 'N/A'}</span>
                        </div>

                    </div>
                </div>
            </c:if>

        </div>
    </div>
</div>
