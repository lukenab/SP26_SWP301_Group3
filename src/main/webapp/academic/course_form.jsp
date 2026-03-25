<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link href="css/editUser.css" rel="stylesheet" type="text/css"/>

<style>
    .course-form-page {
        display: flex;
        flex-direction: column;
        gap: 24px;
    }

    .course-form-header {
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        gap: 16px;
    }

    .course-form-subtitle {
        color: #64748b;
        margin: 6px 0 0;
    }

    .course-status-badge {
        display: inline-flex;
        align-items: center;
        justify-content: center;
        margin-top: 10px;
        padding: 6px 12px;
        border-radius: 999px;
        font-size: 12px;
        font-weight: 700;
        letter-spacing: 0.04em;
        text-transform: uppercase;
        background: #dcfce7;
        color: #166534;
    }

    .course-status-badge.inactive {
        background: #fee2e2;
        color: #991b1b;
    }

    .course-header-card {
        display: grid;
        grid-template-columns: 180px 1fr;
        gap: 24px;
        padding: 28px;
        background: linear-gradient(135deg, #eff6ff 0%, #ffffff 58%, #f8fafc 100%);
        border: 1px solid #dbeafe;
        border-radius: 18px;
        box-shadow: 0 16px 40px rgba(15, 23, 42, 0.08);
    }

    .course-image-panel {
        display: flex;
        flex-direction: column;
        gap: 12px;
    }

    .course-image-preview {
        width: 180px;
        height: 180px;
        border-radius: 24px;
        overflow: hidden;
        background: linear-gradient(135deg, #dbeafe, #eff6ff);
        border: 1px solid #bfdbfe;
        box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.7);
    }

    .course-image-preview img,
    .course-image-fallback {
        width: 100%;
        height: 100%;
    }

    .course-image-preview img {
        object-fit: cover;
        display: block;
    }

    .course-image-fallback {
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 54px;
        font-weight: 700;
        color: #2563eb;
    }

    .course-upload-caption {
        margin: 0;
        font-size: 12px;
        color: #64748b;
        line-height: 1.5;
    }

    .course-header-content {
        display: flex;
        flex-direction: column;
        justify-content: center;
        gap: 12px;
    }

    .course-header-content h2 {
        margin: 0;
        font-size: 34px;
        line-height: 1.15;
        color: #0f172a;
    }

    .course-header-content p {
        margin: 0;
        max-width: 760px;
        color: #475569;
        line-height: 1.7;
    }

    .course-meta-list {
        display: flex;
        flex-wrap: wrap;
        gap: 10px;
    }

    .course-meta-item {
        display: inline-flex;
        align-items: center;
        padding: 8px 14px;
        border-radius: 999px;
        background: rgba(255, 255, 255, 0.92);
        border: 1px solid #dbeafe;
        color: #1e3a8a;
        font-size: 13px;
        font-weight: 600;
    }

    .course-form-card {
        background: #fff;
        border-radius: 18px;
        box-shadow: 0 16px 40px rgba(15, 23, 42, 0.08);
        border: 1px solid #e2e8f0;
        overflow: hidden;
    }

    .course-form-card .form-body {
        padding: 28px;
    }

    .section-heading {
        margin-bottom: 20px;
    }

    .section-heading h3 {
        margin: 0;
        font-size: 18px;
        color: #0f172a;
    }

    .section-heading p {
        margin: 6px 0 0;
        color: #64748b;
        font-size: 14px;
    }

    .inline-toggle {
        display: flex;
        align-items: center;
        gap: 10px;
        min-height: 44px;
        padding: 10px 14px;
        border: 1px solid #dbe2ea;
        border-radius: 12px;
        background: #f8fafc;
    }

    .inline-toggle input[type="checkbox"] {
        width: 18px;
        height: 18px;
        accent-color: #2563eb;
    }

    .inline-toggle label {
        margin: 0;
        font-weight: 600;
    }

    .alert-inline {
        margin-bottom: 20px;
        padding: 14px 16px;
        border-radius: 12px;
        font-size: 14px;
        font-weight: 500;
    }

    .alert-inline.error {
        background: #fef2f2;
        border: 1px solid #fecaca;
        color: #b91c1c;
    }

    .alert-inline.success {
        background: #ecfdf5;
        border: 1px solid #bbf7d0;
        color: #166534;
    }

    .course-file-field {
        display: flex;
        flex-direction: column;
        gap: 10px;
    }

    .course-file-input {
        position: absolute;
        opacity: 0;
        pointer-events: none;
        width: 1px;
        height: 1px;
    }

    .course-file-trigger {
        display: flex;
        align-items: center;
        justify-content: space-between;
        gap: 14px;
        min-height: 48px;
        padding: 7px 14px 7px 7px;
        border: 1px solid #d7e0ea;
        border-radius: 12px;
        background: #ffffff;
        cursor: pointer;
        transition: border-color 0.2s ease, box-shadow 0.2s ease, background-color 0.2s ease;
    }

    .course-file-trigger:hover {
        border-color: #94a3b8;
        background: #f8fafc;
    }

    .course-file-trigger:focus-within {
        border-color: #2563eb;
        box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.12);
    }

    .course-file-button {
        display: inline-flex;
        align-items: center;
        gap: 8px;
        padding: 9px 14px;
        border-radius: 9px;
        background: #eff6ff;
        border: 1px solid #bfdbfe;
        color: #0f172a;
        font-size: 13px;
        font-weight: 700;
        white-space: nowrap;
        line-height: 1;
    }

    .course-file-name {
        flex: 1;
        min-width: 0;
        color: #475569;
        font-size: 13px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        text-align: left;
    }

    .course-file-name.is-empty {
        color: #94a3b8;
    }

    @media (max-width: 992px) {
        .course-header-card {
            grid-template-columns: 1fr;
        }

        .course-image-panel {
            align-items: center;
        }

        .course-header-content {
            text-align: center;
            align-items: center;
        }
    }

    @media (max-width: 768px) {
        .course-form-header {
            flex-direction: column;
            align-items: stretch;
        }

        .course-form-card .form-body,
        .course-header-card {
            padding: 20px;
        }

        .course-image-preview {
            width: 150px;
            height: 150px;
        }
    }
</style>

<c:set var="courseInitial" value="C" />

<div class="container-fluid px-4 content-body course-form-page">
    <div class="course-form-header">
        <div>
            <div aria-label="breadcrumb">
                <ol class="breadcrumb mb-1">
                    <li class="breadcrumb-item"><a href="dashboard?action=academic">Dashboard</a></li>
                    <li class="breadcrumb-item"><a href="${not empty param.returnUrl ? param.returnUrl : 'course?action=all'}">Course Management</a></li>
                    <li class="breadcrumb-item active">${pageTitle}</li>
                </ol>
            </div>
            <h1 class="page-title mb-1">${pageTitle}</h1>
            <p class="course-form-subtitle">Fill in the course details and upload the course image directly from your device.</p>
        </div>

        <a href="${not empty param.returnUrl ? param.returnUrl : 'course?action=all'}" class="btn-secondary">
            <i class='bx bx-arrow-left'></i> Back to Courses
        </a>
    </div>

    <div class="course-header-card">
        <div class="course-image-panel">
            <div class="course-image-preview">
                <c:choose>
                    <c:when test="${not empty course.images}">
                        <img id="courseImagePreview" src="${pageContext.request.contextPath}/images/${course.images}" alt="${course.courseName}">
                    </c:when>
                    <c:otherwise>
                        <div id="courseImageFallback" class="course-image-fallback">${courseInitial}</div>
                        <img id="courseImagePreview" src="" alt="Course preview" style="display:none;">
                    </c:otherwise>
                </c:choose>
                <c:if test="${not empty course.images}">
                    <div id="courseImageFallback" class="course-image-fallback" style="display:none;">${courseInitial}</div>
                </c:if>
            </div>
            <p class="course-upload-caption">Use a square or landscape image, up to 10MB, for cleaner display in the list and detail pages.</p>
        </div>

        <div class="course-header-content">
            <div>
                <h2><c:out value="${empty course.courseName ? 'Course Information' : course.courseName}" /></h2>
                <span class="course-status-badge ${course.status ? '' : 'inactive'}">
                    ${course.status ? 'Active' : 'Inactive'}
                </span>
            </div>

            <p>
                <c:choose>
                    <c:when test="${not empty course.description}">
                        <c:out value="${course.description}" />
                    </c:when>
                    <c:otherwise>
                        Add the course name, fee, total slots, and a short description so the academic team can manage it more easily.
                    </c:otherwise>
                </c:choose>
            </p>

            <div class="course-meta-list">
                <span class="course-meta-item">Total Slots: <c:out value="${empty course.totalSlots ? 0 : course.totalSlots}" /></span>
                <span class="course-meta-item">Tuition Fee: <c:out value="${empty course.tuitionFee ? 0 : course.tuitionFee}" /></span>
                <span class="course-meta-item">Image Source: Local File Upload</span>
            </div>
        </div>
    </div>

    <div class="course-form-card">
        <div class="form-body">
            <div class="section-heading">
                <h3>Course Details</h3>
                <p>Enter the key course information and choose a local image file when needed.</p>
            </div>

            <c:if test="${not empty errorMessage}">
                <div class="alert-inline error">${errorMessage}</div>
            </c:if>

            <c:if test="${not empty param.message}">
                <div class="alert-inline success">${param.message}</div>
            </c:if>

            <form action="course" method="post" enctype="multipart/form-data" class="form-body" style="padding:0;">
                <input type="hidden" name="action" value="${formAction}">
                <input type="hidden" name="images" value="${course.images}">
                <input type="hidden" name="returnUrl" value="${param.returnUrl}">
                <c:if test="${formAction eq 'update'}">
                    <input type="hidden" name="courseId" value="${course.courseId}">
                </c:if>

                <div class="form-row">
                    <div class="form-group">
                        <label for="courseName">Course Name <span>*</span></label>
                        <input type="text"
                               id="courseName"
                               name="courseName"
                               value="${course.courseName}"
                               placeholder="Enter course name"
                               required>
                    </div>

                    <div class="form-group">
                        <label for="imageFile">Course Image</label>
                        <div class="course-file-field">
                            <input type="file"
                                   id="imageFile"
                                   name="imageFile"
                                   accept="image/*"
                                   class="course-file-input"
                                   onchange="previewCourseImage(this)">
                            <label for="imageFile" class="course-file-trigger">
                                <span class="course-file-button">
                                    <i class='bx bx-upload'></i>
                                    Choose File
                                </span>
                                <span class="course-file-name is-empty" id="imageFileName">No file chosen</span>
                            </label>
                        </div>
                        <small>Select a JPG, PNG, or WEBP file from your computer.</small>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="totalSlots">Total Slots <span>*</span></label>
                        <input type="number"
                               id="totalSlots"
                               name="totalSlots"
                               value="${course.totalSlots}"
                               min="1"
                               placeholder="Enter total slots"
                               required>
                    </div>

                    <div class="form-group">
                        <label for="tuitionFee">Tuition Fee <span>*</span></label>
                        <input type="number"
                               id="tuitionFee"
                               name="tuitionFee"
                               value="${course.tuitionFee}"
                               step="0.01"
                               min="0"
                               placeholder="Enter tuition fee"
                               required>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group full-width">
                        <label for="description">Description</label>
                        <textarea id="description"
                                  name="description"
                                  rows="5"
                                  placeholder="Describe the course briefly for managers and learners">${course.description}</textarea>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label>Course Status</label>
                        <div class="inline-toggle">
                            <input type="checkbox"
                                   id="status"
                                   name="status"
                                   value="true"
                                   ${course.status ? 'checked' : ''}>
                            <label for="status">Active Course</label>
                        </div>
                        <small>Uncheck this if you want to save the course as inactive.</small>
                    </div>
                </div>

                <div class="form-buttons">
                    <a href="${not empty param.returnUrl ? param.returnUrl : 'course?action=all'}" class="btn btn-cancel">Cancel</a>
                    <button type="submit" class="btn btn-save">
                        <i class='bx bx-save'></i>
                        <c:choose>
                            <c:when test="${formAction eq 'update'}">Update Course</c:when>
                            <c:otherwise>Add Course</c:otherwise>
                        </c:choose>
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    function previewCourseImage(input) {
        var preview = document.getElementById("courseImagePreview");
        var fallback = document.getElementById("courseImageFallback");
        var fileName = document.getElementById("imageFileName");

        if (fileName) {
            if (input.files && input.files[0]) {
                fileName.textContent = input.files[0].name;
                fileName.classList.remove("is-empty");
            } else {
                fileName.textContent = "No file chosen";
                fileName.classList.add("is-empty");
            }
        }

        if (input.files && input.files[0]) {
            var reader = new FileReader();
            reader.onload = function (e) {
                preview.src = e.target.result;
                preview.style.display = "block";
                if (fallback) {
                    fallback.style.display = "none";
                }
            };
            reader.readAsDataURL(input.files[0]);
        }
    }
</script>
