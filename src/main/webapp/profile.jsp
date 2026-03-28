<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link href="css/profile.css" rel="stylesheet" type="text/css"/>

<div class="mb-4">
    <div aria-label="breadcrumb">
        <ol class="breadcrumb mb-1">
            <li class="breadcrumb-item"><a href="#"><i class="bx bx-home-alt"></i></a></li>
            <li class="breadcrumb-item active" aria-current="page">Profile</li>
        </ol>
    </div>
    <div class="content-header">
        <div>
            <h1 class="page-title">Profile</h1>
            <p class="text-muted small mb-0">Manage your profile information and settings</p>
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
                <label>Upload New Avatar:</label>
                <div class="avatar-input-content">
                    <input type="file" name="avatarFile" id="avatarVisualInput" class="form-control form-control-sm" accept="image/*" 
                           onchange="updateAvatar(this)" form="updateUserForm">

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
                    <c:if test="${user.role.roleId == 2 || user.role.roleId == 3 || user.role.roleId == 4}">
                    <span class="user-email"><i class="bx bx-calendar-event"></i>Joined <fmt:formatDate value="${employee.hireDate}" pattern="dd-MM-yyyy"/></span>   
                </c:if>
            </div>
        </div>
    </div>
</div>

<div class="form-tabs mt-4">
    <div class="form-tab active" onClick="switchTab('profileTab', this)">
        Profile Details
    </div>
    <div class="form-tab" onClick="switchTab('securityTab', this)">
        Security
    </div>
</div>

<div class="form-container">
    <div id="profileTab" class="tab-content" style="display: block">
        <div class="profile-form">
            <div class="profile-form-section">
                <form id="updateUserForm" action="user?action=updateProfile" enctype="multipart/form-data" method="POST" class="form-body">

                    <input type="hidden" name="userId" value="${user.userId}">
                    <input type="hidden" name="roleId" value="${user.role.roleId}" >
                    <input type="hidden" id="uAvatar" name="avatar" value="${user.avatar}">

                    <h3 class="form-title mb-4">Personal Information</h3>
                    <div class="form-row">
                        <div class="form-group">
                            <label for="fullName">Full Name</label>
                            <input type="text" name="fullName" id="fullName" value="${user.fullName}" required>
                        </div>
                        <div class="form-group">
                            <label for="phone">Phone</label>
                            <input type="number" id="phone" name="phone" value="${user.phone}" required>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="address">Address</label>
                            <input type="text" id="address" name="address" value="${user.address}">
                        </div>
                        <div class="form-group">
                            <label for="dob">Date of Birth</label>
                            <input type="date" id="dob" name="dob" value="${user.dob}">
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="gender" class="form-label">Gender</label>
                            <select name="gender" id="gender" required>
                                <option value="true" ${user.gender ? 'selected' : ''}>Male</option>
                                <option value="false" ${!user.gender ? 'selected' : ''}>Female</option>
                            </select>
                        </div>
                        <c:if test="${user.role.roleId == 2 || user.role.roleId == 3 || user.role.roleId == 4 }">
                            <div class="form-group">
                                <label for="hireDate">Hire Date</label>
                                <input type="date" id="hireDate" name="hireDate" value="${employee.hireDate}" required>
                            </div>
                        </c:if>
                    </div>


                    <c:if test="${user.role.roleId == 2 || user.role.roleId == 3 || user.role.roleId == 4 }">
                        <div class="form-row">       
                            <div class="form-group">
                                <label for="education">Education</label>
                                <input type="text" name="education" id="education" value="${employee.education}" required>
                            </div>
                            <div class="form-group">
                                <label for="experience">Experience</label>
                                <input type="text" id="experience" name="experience" value="${employee.experience}">
                            </div>
                        </div>
                    </c:if>

                    <div class="form-buttons">
                        <a href="dashboard?action=profile" class="btn btn-cancel">Cancel</a>
                        <button type="submit" class="btn btn-save">
                            <i class='bx bx-save'></i> Save Changes
                        </button>
                    </div>
                </form>
            </div>
            <div class="profile-shortcut-section">
                <div class="shortcut-form">
                    <div class="activity">
                        <h6 class="form-title mb-3">Recent Activity</h6>
                        <div class="activity-list">
                            <c:choose>
                                <c:when test="${not empty recentLogs}">
                                    <c:forEach var="log" items="${recentLogs}">
                                        <div class="activity-item">
                                            <div class="activity-dot"></div>
                                            <div class="activity-content">
                                                <p class="activity-text">
                                                    <strong>${log.actionType}:</strong> ${log.description}
                                                </p>
                                                <small class="activity-time">
                                                    <fmt:formatDate value="${log.logDate}" pattern="dd/MM/yyyy HH:mm"/>
                                                </small>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <p class="text-muted small p-3">No recent activity recorded.</p>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>  
            </div>
        </div>
    </div>

    <div id="securityTab" class="tab-content" style="display: none">
        <div class="password-form">
            <div class="password-form-section">
                <form action="user?action=changePassword" method="POST" class="form-body">
                    <h3 class="form-title mb-4">Change password</h3>
                    <input type="hidden" name="userId" value="${user.userId}">

                    <div class="form-row full-width-row">
                        <div class="form-group">
                            <label for="currentPassword">Current Password</label>
                            <div class="password-field-wrapper">
                                <input type="password" name="currentPassword" id="currentPassword" placeholder="Confirm current password" required>
                                <i class='bx bx-eye toggle-password' onclick="togglePasswordVisibility('currentPassword', this)"></i>
                            </div>
                        </div>
                    </div>

                    <div class="form-row full-width-row">
                        <div class="form-group">
                            <label for="newPassword">New Password</label>
                            <div class="password-field-wrapper">
                                <input type="password" name="newPassword" id="newPassword" placeholder="Enter new password" required>
                                <i class='bx bx-eye toggle-password' onclick="togglePasswordVisibility('newPassword', this)"></i>
                            </div>
                        </div>
                    </div>

                    <div class="form-row full-width-row">
                        <div class="form-group">
                            <label for="confirmPassword">Confirm New Password</label>
                            <div class="password-field-wrapper">
                                <input type="password" name="confirmPassword" id="confirmPassword" placeholder="Confirm new password" required>
                                <i class='bx bx-eye toggle-password' onclick="togglePasswordVisibility('confirmPassword', this)"></i>
                            </div>
                        </div>
                    </div>

                    <div class="form-buttons">
                        <a href="dashboard?action=profile" class="btn btn-cancel">Cancel</a>
                        <button type="submit" class="btn btn-save">
                            <i class='bx bx-refresh-ccw'></i> Update Password
                        </button>
                    </div>
                </form>
            </div>

            <div class="profile-shortcut-section">
                <div class="shortcut-form">
                    <div class="activity">
                        <h6 class="form-title mb-3">Recent Activity</h6>
                        <div class="activity-list">
                            <c:choose>
                                <c:when test="${not empty recentLogs}">
                                    <c:forEach var="log" items="${recentLogs}">
                                        <div class="activity-item">
                                            <div class="activity-dot"></div>
                                            <div class="activity-content">
                                                <p class="activity-text">
                                                    <strong>${log.actionType}:</strong> ${log.description}
                                                </p>
                                                <small class="activity-time">
                                                    <fmt:formatDate value="${log.logDate}" pattern="dd/MM/yyyy HH:mm"/>
                                                </small>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <p class="text-muted small p-3">No recent activity recorded.</p>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</div>

<script src="js/editUser.js" type="text/javascript"></script>
<script src="js/profile.js" type="text/javascript"></script>
<script src="js/manageUser.js" type="text/javascript"></script>

