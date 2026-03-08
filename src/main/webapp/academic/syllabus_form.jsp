<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link href="css/class_management.css" rel="stylesheet" type="text/css"/>
<link href="css/syllabus_management.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4 content-body syllabus-page">
    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
                <li class="breadcrumb-item"><a href="syllabus?action=manage">Manage Syllabus</a></li>
                <li class="breadcrumb-item active" aria-current="page">Update Syllabus</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">Update Syllabus / Learning Path</h2>
                <p class="text-muted small mb-0">Adjust session order, topic name, and lesson details.</p>
            </div>
            <a href="syllabus?action=manage" class="btn btn-back">
                <i class='bx bx-left-arrow-alt'></i> Back to Syllabus List
            </a>
        </div>
    </div>

    <div class="card user-table-card border-0 bg-white">
        <div class="card-body p-4">
            <form action="syllabus" method="post">
                <input type="hidden" name="action" value="update"/>
                <input type="hidden" name="syllabusId" value="${syllabus.syllabusId}"/>

                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Course</label>
                        <input type="text" class="form-control" value="${syllabus.courseName}" readonly>
                    </div>

                    <div class="col-md-3">
                        <label class="form-label fw-semibold">Session Number</label>
                        <input type="number" class="form-control" name="orderIndex" min="1" value="${syllabus.orderIndex}" required>
                    </div>

                    <div class="col-md-3">
                        <label class="form-label fw-semibold">Syllabus ID</label>
                        <input type="text" class="form-control" value="${syllabus.syllabusId}" readonly>
                    </div>

                    <div class="col-md-12">
                        <label class="form-label fw-semibold">Topic Name</label>
                        <input type="text" class="form-control" name="topicName" value="${syllabus.topicName}" required>
                    </div>

                    <div class="col-12">
                        <label class="form-label fw-semibold">Description</label>
                        <textarea class="form-control syllabus-textarea" name="description" rows="6" required>${syllabus.description}</textarea>
                    </div>
                </div>

                <div class="d-flex justify-content-end mt-4">
                    <button type="submit" class="btn btn-add-new">
                        <i class='bx bx-save'></i> Update Syllabus
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>
