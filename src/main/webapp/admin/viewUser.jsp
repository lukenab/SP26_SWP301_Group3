<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


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
        <c:choose>
            <c:when test="not empty ${u.avatar}">
                <div class="form-row user-img">
                    <img src="${user.avatar}" class="rounded-circle mb-3 object-fit-cover">
                </div>
            </c:when>
            <c:otherwise>
                <div class="profile-avatar-lg" style="background-color: #<c:out value='${user.fullName.hashCode() % 999999}'/>">
                    ${fn:substring(user.fullName, 0, 1)}
                </div>
            </c:otherwise>
        </c:choose>

        <div class="profile-header-info">
            <h2 class="profile-name">${user.fullName}</h2>
            <p class="profile-department">${user.role.roleName}</p>
        </div>
        <div class="profile-header-actions">
            <a href="student?action=inactivate&id=${user.userId}" class="btn btn-edit-profile">
                <i class='bx bx-edit'></i> Edit Profile
            </a>
        </div>
    </div>

    <div class="profile-content-card">
        <div class="profile-tabs">
            <a href="#" class="tab-item active"><i class='bx bxs-user'></i> Overview</a>
        </div>

        <div class="tab-content" id="overview">
            <div class="info-section">
                <h4 class="section-title">Personal Information</h4>
                <div class="info-grid">
                    <div class="info-item">
                        <p>Full Name</p>
                        <span>${user.fullName}</span>
                    </div>
                    <div class="info-item">
                        <p>Date of Birth</p>
                        <span>${user.dob}</span>
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
                        <span>${user.address}</span>
                    </div>
                    <c:if test="${user.role.roleId == 5}">
                        <div class="info-item">
                            <p>Enrollment Date</p>
                            <span>${student.enrollmentDate}</span>
                        </div>
                    </c:if>
                </div>


            </div>



        </div>
    </div>
</div>
</div>