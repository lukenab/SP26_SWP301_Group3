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
                <form action="user?action=update" method="POST" class="form-body">

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
                            <input type="text" name="gender" value="${user.gender ? 'Male' : 'Female'}" required>
                        </div>
                        <div class="form-group">
                            <label for="hireDate">Hire Date</label>
                            <input type="date" id="hireDate" name="hireDate" value="${employee.hireDate}" required>
                        </div>
                    </div>

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

                    <div class="form-buttons">
                        <a href="user" class="btn btn-cancel">Cancel</a>
                        <button type="submit" class="btn btn-save">
                            <i class='bx bx-save'></i> Save Changes
                        </button>
                    </div>
                </form>
            </div>
            <div class="profile-shortcut-section">
                <div class="shortcut-form">
                    <div class="activity">
                        <h6 class="form-title">Recent Activity</h6>
                    </div>
                </div>
                <div class="shortcut-form">
                    <div class="quick-actions">
                        <h5 class="form-title">Quick Actions</h5>
                        <div class="shortcut-btn mt-4">
                            <div class="shortcut-edit">
                                <a href="#"><i class="bx bx-edit"></i>
                                    <span>Edit Profile</span></a> 
                            </div>

                            <div class="shortcut-edit">
                                <a href="#"><i class="bx bx-bell"></i>
                                    <span>Notification</span></a> 
                            </div>

                            <div class="shortcut-edit">
                                <a href="#"><i class="bx bx-arrow-from-bottom"></i>
                                    <span>Upload avatar</span></a> 
                            </div>
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
                            <input type="text" name="currentPassword" id="currentPassword" placeholder="Confirm current password" required>
                        </div>
                    </div>

                    <div class="form-row full-width-row">
                        <div class="form-group">
                            <label for="newPassword">New Password</label>
                            <input type="text" name="newPassword" id="newPassword" placeholder="Enter new password" required>
                        </div>
                    </div>

                    <div class="form-row full-width-row">
                        <div class="form-group">
                            <label for="confirmPassword">Confirm New Password</label>
                            <input type="text" name="confirmPassword" id="confirmPassword" placeholder="Confirm new password" required>
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
                        <h6 class="form-title">Recent Activity</h6>
                    </div>
                </div>
                <div class="shortcut-form">
                    <div class="quick-actions">
                        <h5 class="form-title">Quick Actions</h5>
                        <div class="shortcut-btn mt-4">
                            <div class="shortcut-edit">
                                <a href="#"><i class="bx bx-edit"></i>
                                    <span>Edit Profile</span></a> 
                            </div>

                            <div class="shortcut-edit">
                                <a href="#"><i class="bx bx-bell"></i>
                                    <span>Notification</span></a> 
                            </div>

                            <div class="shortcut-edit">
                                <a href="#"><i class="bx bx-arrow-from-bottom"></i>
                                    <span>Upload avatar</span></a> 
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

