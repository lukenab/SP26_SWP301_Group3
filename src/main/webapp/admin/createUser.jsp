<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link href="css/createUser.css" rel="stylesheet" type="text/css"/>

<div class="mb-4">
    <div class="content-header">
        <div>
            <h1 class="page-title">Add New User</h1>
        </div>
        <a href="user" class="btn-secondary">
            <i class='bx bx-arrow-left'></i> Back to Users
        </a>
    </div>
    <div aria-label="breadcrumb">
        <ol class="breadcrumb mb-1">
            <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
            <li class="breadcrumb-item"><a href="user">User Management</a></li>
            <li class="breadcrumb-item active" aria-current="page">Add New User</li>
        </ol>
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
                    <i class='bx bx-cross-circle'></i>
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

<div class="form-container">
    <p class="form-title">User Information</p>
    <form action="user?action=add" method="POST" class="form-body" enctype="multipart/form-data">

        <div class="profile-avatar-section mb-4">
            <div class="form-row user-img">
                <div class="info-img">
                    <img id="mainAvatarPreview" src="https://cdn-icons-png.flaticon.com/512/149/149071.png" class="rounded-circle object-fit-cover">
                </div>
                <div class="info-img-icon" onclick="toggleAvatarInput()">
                    <i class="bx bx-camera"></i>
                </div>
            </div>

            <div id="avatarInputContainer">
                <div class="form-group mb-0">
                    <label>Select User Avatar:</label>
                    <div class="avatar-input-content">
                        <input type="file" name="avatarFile" id="avatarVisualInput" 
                               class="form-control form-control-sm" accept="image/*" 
                               onchange="updateAvatar(this)">
                        <button type="button" class="btn-secondary avatar-input-btn" onclick="toggleAvatarInput()">
                            <i class='bx bx-x'></i>
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="roleId">Role <span class="text-danger">*</span></label>
                <select class="form-select" name="roleId" id="roleId" onchange="toggleExtraFields()" required>
                    <option value="" disabled selected>Select Role</option>
                    <c:forEach var="r" items="${roleList}">
                        <option value="${r.roleId}">${r.roleName}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label for="fullName">Full Name <span class="text-danger">*</span></label>
                <input type="text" name="fullName" id="fullName" required>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="email">Email <span class="text-danger">*</span></label>
                <input type="email" id="email" name="email" required>
            </div>
            <div class="form-group">
                <label for="password">Password <span class="text-danger">*</span></label>
                <input type="password" id="password" name="password" required> 
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="phone">Phone <span class="text-danger">*</span></label>
                <input type="text" id="phone" name="phone" required>
            </div>
            <div class="form-group">
                <label for="address">Address</label>
                <input type="text" id="address" name="address">
            </div>
        </div>

        <div class="form-row">            
            <div class="form-group">
                <label for="gender">Gender</label>
                <select class="form-select" name="gender" required>
                    <option value="true">Male</option>
                    <option value="false">Female</option>
                </select>
            </div>
            <div class="form-group">
                <label for="dob">Date of Birth</label>
                <input type="date" id="dobInput" name="dob" required>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="status">Status</label>
                <select class="form-select" name="status" required>
                    <option value="true">Active</option>
                    <option value="false">Inactive</option>
                </select>
            </div>
            <div class="form-group"></div> </div>

        <div id="employeeFields" style="display: none;">
            <div class="form-row">
                <div class="form-group">
                    <label>Hire Date</label>
                    <input type="date" name="hireDate">
                </div>
                <div class="form-group">
                    <label>Education</label>
                    <input type="text" name="education" placeholder="E.g. IELTS 8.0, Master Degree">
                </div>
            </div>
            <div class="form-row">
                <div class="form-group">
                    <label>Experience</label>
                    <textarea name="experience" rows="2" style="width: 100%; padding: 10px; border-radius: 8px; border: 1px solid #ddd;" placeholder="E.g. 3 years teaching English"></textarea>
                </div>
            </div>
        </div>

        <div id="studentFields" style="display: none;">
            <div class="form-row">
                <div class="form-group">
                    <label>Enrollment Date</label>
                    <input type="date" name="enrollmentDate">
                </div>
                <div class="form-group"></div>
            </div>
        </div>

        <div class="form-buttons mt-4">
            <a href="user?action=all" class="btn btn-cancel">Cancel</a>
            <button type="submit" class="btn btn-save">
                <i class='bx bx-save'></i> Add New User
            </button>
        </div>
    </form>
</div>

<script src="js/createUser.js" type="text/javascript"></script>