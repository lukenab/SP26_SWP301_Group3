<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link href="css/editLead.css" rel="stylesheet" type="text/css"/>

<div class="mb-4">
    <div class="content-header">
        <div>
            <h1 class="page-title">Log Consultation</h1>
        </div>
        <div class="content-header-actions">
            <a href="lead?action=detail&id=${lead.leadId}" class="btn-secondary">
                <i class='bx bx-arrow-left'></i> Back to Lead Detail
            </a>
        </div>
    </div>
    <div aria-label="breadcrumb">
        <ol class="breadcrumb mb-1">
            <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
            <li class="breadcrumb-item"><a href="lead?action=all">Lead Management</a></li>
            <li class="breadcrumb-item active" aria-current="page">Log Consultation</li>
        </ol>
    </div>
</div>

<div class="profile-header-card">
    <div class="lead-avatar">
        <span>${fn:toUpperCase(fn:substring(lead.fullName, 0, 1))}</span>
    </div>

    <div class="profile-header-info">
        <h2 class="profile-name">${lead.fullName}</h2>
        <span class="profile-active status-contacted">${lead.status}</span>
        <div class="profile-info-content">
            <div class="profile-header-left">
                <span class="user-email"><i class="bx bx-envelope"></i>${lead.email}</span>
                <span class="user-email"><i class="bx bx-phone"></i>${lead.phone}</span>
            </div>
        </div>
    </div>
</div>

<div class="form-container">
    <form action="lead" method="POST" class="form-body">
        <input type="hidden" name="action" value="logConsultation">
        <input type="hidden" name="leadId" value="${lead.leadId}">

        <div class="form-row">
            <div class="form-group">
                <label for="consultDate">Consultation Date</label>
                <input type="datetime-local" id="consultDate" name="consultDate" value="${nowDateTime}" required>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group form-group-full">
                <label for="consultationNote">Consultation Details</label>
                <textarea id="consultationNote" name="consultationNote" rows="5" placeholder="Example: Called parent, explained course roadmap, parent requested follow-up next Monday..." required></textarea>
            </div>
        </div>

        <div class="form-buttons">
            <a href="lead?action=detail&id=${lead.leadId}" class="btn btn-cancel">Cancel</a>
            <button type="submit" class="btn btn-save">
                <i class='bx bx-save'></i> Save Record
            </button>
        </div>
    </form>
</div>
