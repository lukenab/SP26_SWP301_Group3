<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link href="css/addLead.css" rel="stylesheet" type="text/css"/>

<div class="mb-4">
    <div class="content-header">
        <div>
            <h1 class="page-title">Add New Lead</h1>
        </div>
        <a href="lead?action=all" class="btn-secondary">
            <i class='bx bx-arrow-left'></i> Back to Leads
        </a>
    </div>
    <div aria-label="breadcrumb">
        <ol class="breadcrumb mb-1">
            <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
            <li class="breadcrumb-item"><a href="lead?action=all">Lead Management</a></li>
            <li class="breadcrumb-item active" aria-current="page">Add New Lead</li>
        </ol>
    </div>
</div>

<div class="form-container">
    <p class="form-title">Lead Information</p>
    <form action="lead?action=create" method="POST" class="form-body">
        <div class="form-row">
            <div class="form-group">
                <label for="fullName">Full Name <span class="text-danger">*</span></label>
                <input type="text" id="fullName" name="fullName" required>
            </div>
            <div class="form-group">
                <label for="phone">Phone <span class="text-danger">*</span></label>
                <input type="text" id="phone" name="phone" required>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="email">Email <span class="text-danger">*</span></label>
                <input type="email" id="email" name="email" required>
            </div>
            <div class="form-group">
                <label for="interestedCourseID">Interested Course <span class="text-danger">*</span></label>
                <select id="interestedCourseID" name="interestedCourseID" required>
                    <option value="" disabled selected>Select Course</option>
                    <c:forEach items="${courseList}" var="c">
                        <option value="${c.courseId}">
                            ${c.courseName} (ID: ${c.courseId})
                        </option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="status">Status</label>
                <select id="status" name="status">
                    <option value="New">New</option>
                    <option value="Contacted">Contacted</option>
                    <option value="Converted">Converted</option>
                </select>
            </div>
            <div class="form-group">
                <label for="note">Note</label>
                <textarea id="note" name="note" rows="3" placeholder="Enter consultation note..."></textarea>
            </div>
        </div>

        <div class="form-buttons">
            <a href="lead?action=all" class="btn btn-cancel">Cancel</a>
            <button type="submit" class="btn btn-save">
                <i class='bx bx-save'></i> Add New Lead
            </button>
        </div>
    </form>
</div>
