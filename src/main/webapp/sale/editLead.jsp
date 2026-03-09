<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link href="css/editLead.css" rel="stylesheet" type="text/css"/>

<div class="mb-4">
    <div class="content-header">
        <div>
            <h1 class="page-title">Edit Lead</h1>
        </div>
        <div class="content-header-actions">
            <a href="lead?action=all" class="btn-secondary">
                <i class='bx bx-arrow-left'></i> Back to Leads
            </a>
        </div>
    </div>
    <div aria-label="breadcrumb">
        <ol class="breadcrumb mb-1">
            <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
            <li class="breadcrumb-item"><a href="lead?action=all">Lead Management</a></li>
            <li class="breadcrumb-item active" aria-current="page">Edit Lead</li>
        </ol>
    </div>
</div>

<div class="profile-header-card">
    <div class="lead-avatar">
        <span>${fn:toUpperCase(fn:substring(lead.fullName, 0, 1))}</span>
    </div>

    <div class="profile-header-info">
        <h2 class="profile-name">${lead.fullName}</h2>

        <c:choose>
            <c:when test="${lead.status == 'New'}">
                <span class="profile-active status-new">New</span>
            </c:when>
            <c:when test="${lead.status == 'Converted'}">
                <span class="profile-active status-converted">Converted</span>
            </c:when>
            <c:otherwise>
                <span class="profile-active status-contacted">Contacted</span>
            </c:otherwise>
        </c:choose>

        <div class="profile-info-content">
            <div class="profile-header-left">
                <span class="user-email"><i class="bx bx-envelope"></i>${lead.email}</span>
                <span class="user-email"><i class="bx bx-phone"></i>${lead.phone}</span>
            </div>
            <div class="profile-header-right">
                <span class="user-email">
                    <i class="bx bx-calendar-event"></i>
                    Created
                    <c:choose>
                        <c:when test="${not empty lead.createDate}">
                            ${fn:substring(lead.createDate, 8, 10)}/${fn:substring(lead.createDate, 5, 7)}/${fn:substring(lead.createDate, 0, 4)}
                            ${fn:substring(lead.createDate, 11, 16)}
                        </c:when>
                        <c:otherwise>-</c:otherwise>
                    </c:choose>
                </span>
            </div>
        </div>
        <div class="mt-3">
            <a href="lead?action=logForm&id=${lead.leadId}" class="btn btn-add-new">
                <i class='bx bx-edit-alt'></i>
            </a>
        </div>
    </div>
</div>

<div class="form-container">
    <form action="lead" method="POST" class="form-body">
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="leadId" value="${lead.leadId}">

        <div class="form-row">
            <div class="form-group">
                <label for="fullName">Full Name</label>
                <input type="text" id="fullName" name="fullName" value="${lead.fullName}" required>
            </div>
            <div class="form-group">
                <label for="phone">Phone</label>
                <input type="text" id="phone" name="phone" value="${lead.phone}" required>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" value="${lead.email}" required>
            </div>
            <div class="form-group">
                <label for="status">Status</label>
                <select name="status" id="status" required>
                    <option value="New" ${lead.status == 'New' ? 'selected' : ''}>New</option>
                    <option value="Contacted" ${lead.status == 'Contacted' || (lead.status != 'New' && lead.status != 'Converted' && lead.status != 'Inactive') ? 'selected' : ''}>Contacted</option>
                </select>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group form-group-full">
                <label>Latest Consultation Note</label>
                <div style="border:1px solid #ddd; border-radius:8px; background:#fff; padding:10px 12px; line-height:1.4; color:#334155; white-space:normal; word-break:break-word; min-height:44px;">
                    <c:out value="${empty lead.note ? '-' : fn:trim(lead.note)}"/>
                </div>
            </div>
        </div>

        <div class="form-buttons">
            <a href="lead?action=all" class="btn btn-cancel">Cancel</a>
            <button type="submit" class="btn btn-save">
                <i class='bx bx-save'></i> Update Lead
            </button>
        </div>
    </form>
</div>
