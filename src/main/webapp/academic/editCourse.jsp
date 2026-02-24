<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link href="css/editUser.css" rel="stylesheet" type="text/css"/>

<div class="mb-4">
    <div class="content-header">
        <div>
            <h1 class="page-title">Edit Course</h1>
        </div>
        <a href="course?action=all" class="btn-secondary">
            <i class='bx bx-arrow-left'></i> Back to Courses
        </a>
    </div>

    <div aria-label="breadcrumb">
        <ol class="breadcrumb mb-1">
            <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
            <li class="breadcrumb-item">
                <a href="course?action=all">Course Management</a>
            </li>
            <li class="breadcrumb-item active">Edit Course</li>
        </ol>
    </div>
</div>

<!-- ===== COURSE HEADER CARD ===== -->
<div class="profile-header-card">
    <div class="profile-avatar-section">
        <div class="form-row user-img">
            <div class="info-img">
                <img src="${course.images}" class="rounded-circle object-fit-cover">
            </div>

            <div class="info-img-icon" onclick="toggleAvatarInput()">
                <i class="bx bx-camera"></i>
            </div>
        </div>

        <div id="avatarInputContainer">
            <div class="form-group mb-0">
                <label>New Image URL:</label>
                <div class="avatar-input-content">
                    <input type="text"
                           id="avatarVisualInput"
                           class="form-control form-control-sm"
                           value="${course.images}"
                           placeholder="Paste image link here..."
                           oninput="updateAvatar()">

                    <button type="button"
                            class="btn-secondary avatar-input-btn"
                            onclick="toggleAvatarInput()">
                        <i class='bx bx-x'></i>
                    </button>
                </div>
            </div>
        </div>
    </div>

    <div class="profile-header-info">
        <h2 class="profile-name">${course.courseName}</h2>
        <span class="profile-active">
            ${course.status ? 'Active' : 'Inactive'}
        </span>
        <p>${course.description}</p>
    </div>
</div>

<!-- ===== FORM ===== -->
<div class="form-container">
    <form action="course?action=update" method="POST" class="form-body">

        <input type="hidden" name="courseId" value="${course.courseId}">
        <input type="hidden" id="uAvatar" name="images" value="${course.images}">

        <!-- Row 1 -->
        <div class="form-row">
            <div class="form-group">
                <label for="courseName">Course Name</label>
                <input type="text"
                       name="courseName"
                       id="courseName"
                       value="${course.courseName}"
                       required>
            </div>

            <div class="form-group">
                <label for="totalSlots">Total Slots</label>
                <input type="number"
                       id="totalSlots"
                       name="totalSlots"
                       value="${course.totalSlots}"
                       min="1"
                       required>
            </div>
        </div>

        <!-- Row 2 -->
        <div class="form-row">
            <div class="form-group">
                <label for="tuitionFee">Tuition Fee</label>
                <input type="number"
                       id="tuitionFee"
                       name="tuitionFee"
                       value="${course.tuitionFee}"
                       step="0.01"
                       min="0"
                       required>
            </div>

<!--            <div class="form-group">
                <label>Status</label>
                <select name="status" required>
                    <option value="true"
                        ${course.status ? 'selected' : ''}>Active</option>
                    <option value="false"
                        ${!course.status ? 'selected' : ''}>Inactive</option>
                </select>
            </div>-->
        </div>

        <!-- Description -->
        <div class="form-row">
            <div class="form-group" style="width:100%">
                <label for="description">Description</label>
                <textarea id="description"
                          name="description"
                          rows="4"
                          required>${course.description}</textarea>
            </div>
        </div>

        <div class="form-buttons">
            <a href="course?action=all" class="btn btn-cancel">Cancel</a>
            <button type="submit" class="btn btn-save">
                <i class='bx bx-save'></i> Update Course
            </button>
        </div>
    </form>
</div>

<script src="js/editUser.js"></script>