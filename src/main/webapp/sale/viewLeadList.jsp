<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link href="css/viewLeadList.css" rel="stylesheet" type="text/css"/>
<link href="css/manageUser.css" rel="stylesheet" type="text/css"/>
<div class="container-fluid px-4 content-body">

    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
                <li class="breadcrumb-item active" aria-current="page">Lead Management</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">Lead Management</h2>
                <p class="text-muted small mb-0">Manage and organize your leads</p>
            </div>
            <a href="lead?action=add" class="btn btn-add-new">
                <i class='bx bx-user-plus'></i> Add New Lead
            </a>
        </div>
    </div>
    
    <c:if test="${not empty sessionScope.message}">
        <div class="custom-toast toast-${sessionScope.messageType}" id="toastMessage">
            <div class="toast-icon">
                <c:choose>
                    <c:when test="${sessionScope.messageType == 'success'}">
                        <i class='bx bx-check-circle'></i>
                    </c:when>
                    <c:otherwise>
                        <i class='bx bx-error-circle'></i>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="toast-content">
                <span class="toast-title">
                    ${sessionScope.messageType == 'success' ? 'Success!' : 'Error!'}
                </span>
                <span class="toast-message">${sessionScope.message}</span>
            </div>
            <button class="toast-close" onclick="closeToast()">
                <i class='bx bx-x'></i>
            </button>
        </div>

        <c:remove var="message" scope="session" />
        <c:remove var="messageType" scope="session" />
    </c:if>

    <c:set var="newLead" value="0"/>
    <c:set var="convertedLead" value="0"/>

    <c:forEach items="${leadList}" var="l">
        <c:if test="${l.status == 'New'}">
            <c:set var="newLead" value="${newLead + 1}"/>
        </c:if>
        <c:if test="${l.status == 'Converted'}">
            <c:set var="convertedLead" value="${convertedLead + 1}"/>
        </c:if>
    </c:forEach>

    <div class="stat-card-grid">
        <div class="stat-card">
            <div class="stat-info">
                <p>Total Leads</p>
                <h3>${fn:length(leadList)}</h3>
            </div>
            <div class="icon-wrapper blue">
                <i class='bx bxs-reading'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>New Leads</p>
                <h3>${newLead}</h3>
            </div>
            <div class="icon-wrapper green">
                <i class='bx bxs-check-shield'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Converted Leads</p>
                <h3>${convertedLead}</h3>
            </div>
            <div class="icon-wrapper cyan">
                <i class='bx bxs-user-check'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Converted Rate</p>
                <h3>
                    <c:choose>
                        <c:when test="${fn:length(leadList) > 0}">
                            <fmt:formatNumber value="${(convertedLead * 100.0) / fn:length(leadList)}"
                                              minFractionDigits="2"
                                              maxFractionDigits="2"/>%
                        </c:when>
                        <c:otherwise>0%</c:otherwise>
                    </c:choose>
                </h3>
            </div>
            <div class="icon-wrapper orange">
                <i class='bx bxs-dollar'></i>
            </div>
        </div>
    </div>

    <c:if test="${not empty sessionScope.message}">
        <div class="custom-toast toast-${sessionScope.messageType}" id="toastMessage">
            <div class="toast-icon">
                <c:choose>
                    <c:when test="${sessionScope.messageType == 'success'}">
                        <i class='bx bx-check-circle'></i>
                    </c:when>
                    <c:otherwise>
                        <i class='bx bx-error-circle'></i>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="toast-content">
                <span class="toast-title">
                    ${sessionScope.messageType == 'success' ? 'Success!' : 'Error!'}
                </span>
                <span class="toast-message">${sessionScope.message}</span>
            </div>
            <button class="toast-close" onclick="closeToast()">
                <i class='bx bx-x'></i>
            </button>
        </div>

        <c:remove var="message" scope="session" />
        <c:remove var="messageType" scope="session" />
    </c:if>

    <form action="lead" method="GET" class="filter-container flex-wrap">
        <input type="hidden" name="action" value="all">
        <div class="custom-search-bar">
            <i class='bx bx-search text-muted fs-5'></i>
            <input type="text" name="searchQuery" value="${searchQuery}" placeholder="Search by name or email...">
        </div>

        <div class="d-flex gap-3">
            <select class="custom-select-filter" name="status">
                <option value="all" ${statusFilter == 'all' ? 'selected' : ''}>All Status</option>
                <option value="New" ${statusFilter == 'New' ? 'selected' : ''}>New</option>
                <option value="Contacted" ${statusFilter == 'Contacted' ? 'selected' : ''}>Contacted</option>
                <option value="Converted" ${statusFilter == 'Converted' ? 'selected' : ''}>Converted</option>
                <option value="Inactive" ${statusFilter == 'Inactive' ? 'selected' : ''}>Inactive</option>
            </select>
            <button type="submit" class="btn btn-add-new">
                <i class='bx bx-filter-alt'></i> Filter
            </button>
            <a href="lead?action=all" class="btn btn-cancel">Reset</a>
        </div>
    </form>

    <div class="card user-table-card border-0 bg-white">
        <div class="table-responsive">
            <table class="table mb-0 align-middle">
                <thead>
                    <tr>
                        <th style="width: 5%">#</th>
                        <th style="width: 20%">Lead Name</th>
                        <th style="width: 25%">Email</th>
                        <th style="width: 15%">Phone</th>
                        <th style="width: 15%">Status</th>
                        <th style="width: 20%">Actions</th>
                    </tr>
                </thead>

                <tbody>
                    <c:forEach items="${leadList}" var="l" varStatus="loop">
                        <tr class="${l.status == 'Inactive' ? 'row-inactive' : ''}">
                            <td>${loop.count}</td>
                            <td>${l.fullName}</td>
                            <td class="text-secondary">${l.email}</td>
                            <td>${l.phone}</td>

                            <td>
                                <c:choose>
                                    <c:when test="${l.status == 'New'}">
                                        <span class="badge badge-admin">New</span>
                                    </c:when>
                                    <c:when test="${l.status == 'Contacted'}">
                                        <span class="badge badge-teacher">Contacted</span>
                                    </c:when>
                                    <c:when test="${l.status == 'Converted'}">
                                        <span class="badge badge-saleStaff">Converted</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge badge-teacher">Contacted</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>

                            <td>
                                <c:choose>
                                    <c:when test="${l.status == 'Inactive'}">
                                        <span class="action-btn action-disabled"><i class='bx bx-eye'></i></span>
                                        <span class="action-btn action-disabled"><i class='bx bx-edit'></i></span>
                                        <a href="lead?action=delete&id=${l.leadId}" class="action-btn">
                                            <i class='bx bx-lock-open'></i>
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="lead?action=detail&id=${l.leadId}" class="action-btn"><i class='bx bx-eye'></i></a>
                                        <c:choose>
                                            <c:when test="${l.status == 'Converted'}">
                                                <span class="action-btn action-disabled" title="Converted lead cannot be edited">
                                                    <i class='bx bx-edit'></i>
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="lead?action=edit&id=${l.leadId}" class="action-btn"><i class='bx bx-edit'></i></a>
                                            </c:otherwise>
                                        </c:choose>
                                        <c:if test="${l.status != 'Converted'}">
                                            <a href="lead?action=convertForm&id=${l.leadId}" class="action-btn" title="Convert to Student">
                                                <i class='bx bx-user-check'></i>
                                            </a>
                                        </c:if>
                                        <a href="lead?action=delete&id=${l.leadId}" class="action-btn delete">
                                            <i class='bx bx-lock'></i>
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="d-flex justify-content-between align-items-center p-3 border-top">
            <div class="text-muted small">Total ${fn:length(leadList)} leads</div>
            <div>
                <ul class="pagination pagination-sm mb-0">
                    <li class="page-item disabled"><a class="page-link" href="#"><i class='bx bx-chevron-left'></i> Previous</a></li>
                    <li class="page-item active"><a class="page-link" href="#">1</a></li>
                    <li class="page-item disabled"><a class="page-link" href="#">Next <i class='bx bx-chevron-right'></i></a></li>
                </ul>
            </div>
        </div>
    </div>
</div>

<script src="js/manageUser.js" type="text/javascript"></script>
