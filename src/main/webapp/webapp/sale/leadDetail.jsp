<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link href="css/leadDetail.css" rel="stylesheet" type="text/css"/>

<div class="page-header">
    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
                <li class="breadcrumb-item"><a href="user">Leads</a></li>
                <li class="breadcrumb-item active" aria-current="page">Lead Information</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">Lead Management</h2>
                <p class="text-muted small mb-0">Manage and organize your leads</p>
            </div>
            <a href="lead" class="btn-secondary">
                <i class='bx bx-arrow-left'></i> Back to Leads
            </a>
        </div>
    </div>

    <div class="profile-header-card">
        <div class="profile-avatar-lg" style="background-color: #<c:out value='${lead.fullName.hashCode() % 999999}'/>">
            ${fn:substring(lead.fullName, 0, 1)}
        </div>
        <div class="profile-header-info">
            <h2 class="profile-name">${lead.fullName}</h2>
            <span class="profile-active">${lead.status}</span>
            <div class="profile-info-content">
                <div class="profile-header-left">
                    <span class="user-email"><i class="bx bx-envelope"></i>${lead.email}</span>
                    <span class="user-email"><i class="bx bx-location"></i>${lead.phone}</span>   
                </div>
            </div>
        </div>
    </div>

    <div class="profile-content-card">
        <div class="profile-tabs">            
            <a href="#" class="tab-item active"><i class='bx bxs-user'></i>Lead Information</a>
        </div>

        <div class="tab-content" id="overview">
            <div class="info-section">
                <div class="info-grid">
                    <div class="info-item">
                        <p>Full Name</p>
                        <span>${lead.fullName}</span>
                    </div>
                    <div class="info-item">
                        <p>Email</p>
                        <span>${lead.email}</span>
                    </div>
                    <div class="info-item">
                        <p>Phone</p>
                        <span>${lead.phone}</span>
                    </div>
                    <div class="info-item">
                        <p>Status</p>
                        <span>${lead.status}</span>
                    </div> 
                    <div class="info-item">
                        <p>Created Date</p>
                        <span>${lead.createDate}</span>
                    </div> 
                    <div class="info-item">
                        <p>Status</p>
                        <span>${lead.status}</span>
                    </div> 
                </div>
            </div>
        </div>
    </div>
</div>