<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link href="css/createUser.css" rel="stylesheet" type="text/css"/>

<div class="mb-4">
    <div class="content-header">
        <div>
            <h1 class="page-title">Deactivate Lead</h1>
        </div>
        <a href="lead?action=all" class="btn-secondary">
            <i class='bx bx-arrow-left'></i> Back to Leads
        </a>
    </div>
    <div aria-label="breadcrumb">
        <ol class="breadcrumb mb-1">
            <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
            <li class="breadcrumb-item"><a href="lead?action=all">Lead Management</a></li>
            <li class="breadcrumb-item active" aria-current="page">Deactivate Lead</li>
        </ol>
    </div>
</div>

<div class="form-container">
    <p class="form-title">Lead Information</p>

    <c:choose>
        <c:when test="${dLead.status == 'Inactive'}">
            <form action="lead?action=restore" method="POST" class="form-body">
                <input type="hidden" name="leadID" value="${dLead.leadId}">

                <div class="form-row">
                    <div class="form-group">
                        <label for="fullName">Full Name</label>
                        <input type="text" id="fullName" value="${dLead.fullName}" readonly>
                    </div>
                    <div class="form-group">
                        <label for="phone">Phone</label>
                        <input type="text" id="phone" value="${dLead.phone}" readonly>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="email">Email</label>
                        <input type="email" id="email" value="${dLead.email}" readonly>
                    </div>
                    <div class="form-group">
                        <label for="course">Interested Course ID</label>
                        <input type="text" id="course" value="${dLead.interestedCourseID}" readonly>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="status">Current Status</label>
                        <input type="text" id="status" value="${dLead.status}" readonly>
                    </div>
                    <div class="form-group">
                        <label for="note">Note</label>
                        <textarea id="note" rows="3" readonly>${dLead.note}</textarea>
                    </div>
                </div>

                <div class="form-buttons">
                    <a href="lead?action=all" class="btn btn-cancel">Cancel</a>
                    <button type="submit" class="btn btn-unlock">
                        <i class='bx bx-lock-open'></i> Restore Lead
                    </button>
                </div>
            </form>
        </c:when>
        <c:otherwise>
            <form action="lead?action=delete" method="POST" class="form-body">
                <input type="hidden" name="leadID" value="${dLead.leadId}">

                <div class="form-row">
                    <div class="form-group">
                        <label for="fullName">Full Name</label>
                        <input type="text" id="fullName" value="${dLead.fullName}" readonly>
                    </div>
                    <div class="form-group">
                        <label for="phone">Phone</label>
                        <input type="text" id="phone" value="${dLead.phone}" readonly>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="email">Email</label>
                        <input type="email" id="email" value="${dLead.email}" readonly>
                    </div>
                    <div class="form-group">
                        <label for="course">Interested Course ID</label>
                        <input type="text" id="course" value="${dLead.interestedCourseID}" readonly>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="status">Current Status</label>
                        <input type="text" id="status" value="${dLead.status}" readonly>
                    </div>
                    <div class="form-group">
                        <label for="note">Note</label>
                        <textarea id="note" rows="3" readonly>${dLead.note}</textarea>
                    </div>
                </div>

                <div class="form-buttons">
                    <a href="lead?action=all" class="btn btn-cancel">Cancel</a>
                    <button type="submit" class="btn btn-lock">
                        <i class='bx bx-lock'></i> Deactivate Lead
                    </button>
                </div>
            </form>
        </c:otherwise>
    </c:choose>
</div>
