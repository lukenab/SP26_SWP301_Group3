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

<c:if test="${not empty sessionScope.message or not empty requestScope.message}">
    <div class="custom-toast toast-${not empty sessionScope.messageType ? sessionScope.messageType : requestScope.messageType}" id="toastMessage">
        <div class="toast-icon">
            <c:choose>
                <c:when test="${sessionScope.messageType == 'success' or requestScope.messageType == 'success'}">
                    <i class='bx bx-check-circle'></i>
                </c:when>
                <c:otherwise>
                    <i class='bx bx-error-circle'></i>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="toast-content">
            <span class="toast-title">
                ${(sessionScope.messageType == 'success' or requestScope.messageType == 'success') ? 'Success!' : 'Error!'}
            </span>
            <span class="toast-message">${not empty sessionScope.message ? sessionScope.message : requestScope.message}</span>
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
                <select class="form-select ${not empty roleError ? 'is-invalid' : ''}" name="roleId" id="roleId" onchange="toggleExtraFields()" required>
                    <option value="" disabled ${empty param.roleId ? 'selected' : ''}>Select Role</option>
                    <c:forEach var="r" items="${roleList}">
                        <option value="${r.roleId}" ${param.roleId == r.roleId ? 'selected' : ''}>${r.roleName}</option>
                    </c:forEach>
                </select>
                <span class="text-danger small">${roleError}</span>
            </div>
            <div class="form-group">
                <label for="fullName">Full Name <span class="text-danger">*</span></label>
                <input type="text" name="fullName" id="fullName" value="${param.fullName}" required>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="email">Email <span class="text-danger">*</span></label>
                <input type="email" id="email" name="email" value="${param.email}" class="${not empty emailError ? 'is-invalid' : ''}" required>
                <span class="text-danger small">${emailError}</span>
            </div>
            <div class="form-group">
                <label for="password">Password <span class="text-danger">*</span></label>
                <input type="password" id="password" name="password" value="${param.password}" required> 
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="phone">Phone <span class="text-danger">*</span></label>
                <input type="text" id="phone" name="phone" value="${param.phone}" class="${not empty phoneError ? 'is-invalid' : ''}" required>
                <span class="text-danger small">${phoneError}</span>
            </div>
            <div class="form-group">
                <label for="address">Address</label>
                <input type="text" id="address" name="address" value="${param.address}">
            </div>
        </div>

        <div class="form-row">            
            <div class="form-group">
                <label for="gender">Gender</label>
                <select class="form-select" name="gender" required>
                    <option value="true" ${param.gender == 'true' ? 'selected' : ''}>Male</option>
                    <option value="false" ${param.gender == 'false' ? 'selected' : ''}>Female</option>
                </select>
            </div>
            <div class="form-group">
                <label for="dob">Date of Birth <span class="text-danger">*</span></label>
                <input type="date" id="dobInput" name="dob" value="${param.dob}" class="${not empty dobError ? 'is-invalid' : ''}" required>
                <span class="text-danger small">${dobError}</span>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="status">Status</label>
                <select class="form-select" name="status" required>
                    <option value="true" ${param.status == 'true' ? 'selected' : ''}>Active</option>
                    <option value="false" ${param.status == 'false' ? 'selected' : ''}>Inactive</option>
                </select>
            </div>

            <div class="form-group">
                <div id="dynamicDateContainer" style="display: none;">
                    <label id="dateLabel">Join Date</label>
                    <input type="date" id="dynamicDateInput" class="form-control" value="${param.hireDate}${param.enrollmentDate}">
                </div>
            </div>
        </div>

        <div id="employeeExtraRow" class="form-row" style="display: none;">
            <div class="form-group">
                <label>Education</label>
                <input type="text" name="education" value="${param.education}" placeholder="E.g. IELTS 8.0, Master Degree">
            </div>
            <div class="form-group">
                <label>Experience</label>
                <input type="text" name="experience" value="${param.experience}" placeholder="E.g. 3 years teaching">
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