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
                <li class="breadcrumb-item"><a href="lead?action=all">Leads</a></li>
                <li class="breadcrumb-item active" aria-current="page">Lead Information</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">Lead Management</h2>
                <p class="text-muted small mb-0">Manage and organize your leads</p>
            </div>
            <div class="d-flex gap-2">
                <a href="lead?action=all" class="btn-secondary">
                    <i class='bx bx-arrow-left'></i> Back to Leads
                </a>
            </div>
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
                        <span>
                            <c:choose>
                                <c:when test="${not empty lead.createDate}">
                                    ${fn:substring(lead.createDate, 8, 10)}/${fn:substring(lead.createDate, 5, 7)}/${fn:substring(lead.createDate, 0, 4)}
                                    ${fn:substring(lead.createDate, 11, 16)}
                                </c:when>
                                <c:otherwise>-</c:otherwise>
                            </c:choose>
                        </span>
                    </div>
                    <div class="info-item">
                        <p>Latest Note</p>
                        <span>
                            <c:choose>
                                <c:when test="${not empty lead.note}">${lead.note}</c:when>
                                <c:otherwise>-</c:otherwise>
                            </c:choose>
                        </span>
                    </div>
                </div>
            </div>

            <div class="info-section mt-4">
                <h5 class="mb-3">Consultation History</h5>
                <div class="table-responsive">
                    <table class="table table-bordered table-striped mb-0">
                        <thead>
                            <tr>
                                <th style="width: 25%">Consultation Time</th>
                                <th style="width: 20%">Sales Staff</th>
                                <th style="width: 55%">Details</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty consultationHistory}">
                                    <c:forEach items="${consultationHistory}" var="cst">
                                        <tr>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty cst.consultation}">
                                                        ${fn:substring(cst.consultation, 8, 10)}/${fn:substring(cst.consultation, 5, 7)}/${fn:substring(cst.consultation, 0, 4)}
                                                        ${fn:substring(cst.consultation, 11, 16)}
                                                    </c:when>
                                                    <c:otherwise>-</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty cst.saleName}">${cst.saleName}</c:when>
                                                    <c:otherwise>Unassigned</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>${cst.note}</td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="3" class="text-center text-muted">No consultation records yet.</td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
