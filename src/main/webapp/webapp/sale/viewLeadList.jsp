<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link href="css/viewLeadList.css" rel="stylesheet" type="text/css"/>

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
            <a href="#" class="btn btn-add-new">
                <i class='bx bx-user-plus'></i> Add New Lead
            </a>
        </div>
    </div>

    <c:set var="newLead" value="0"/>

    <c:forEach items="${leadList}" var="l" >
        <c:if test="${l.status == 'New'}">
            <c:set var="newLead" value="${newLead + 1}"/>
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
                <p>New Lead</p>
                <h3>${newLead}</h3> 
            </div>
            <div class="icon-wrapper green">
                <i class='bx bxs-check-shield'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Inactive Lead</p>
                <h3>0</h3>
            </div>
            <div class="icon-wrapper cyan">
                <i class='bx bxs-info-shield'></i>
            </div>
        </div>  
        <div class="stat-card">         
            <div class="stat-info">
                <p>Converted Rate</p>
                <h3>2.43%</h3>
            </div>
            <div class="icon-wrapper cyan">
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

    <div class="filter-container flex-wrap">
        <div class="custom-search-bar">
            <i class='bx bx-search text-muted fs-5'></i>
            <input type="text" placeholder="Search by name or email...">
        </div>

        <div class="d-flex gap-3">
            <div class="dropdown">
                <button class="custom-select-filter" type="button" data-bs-toggle="dropdown">
                    <i class='bx bx-filter-alt'></i> All Leads <i class='bx bx-chevron-down ms-1'></i>
                </button>
                <ul class="dropdown-menu">
                    <li><a class="dropdown-item" href="#">Admin</a></li>
                    <li><a class="dropdown-item" href="#">Teacher</a></li>
                    <li><a class="dropdown-item" href="#">Student</a></li>
                </ul>
            </div>

            <div class="dropdown">
                <button class="custom-select-filter d-flex align-items-center gap-2" type="button" data-bs-toggle="dropdown">
                    <i class='bx bx-slider-alt'></i> All Status <i class='bx bx-chevron-down ms-1'></i>
                </button>
                <ul class="dropdown-menu">
                    <li><a class="dropdown-item" href="#">Active</a></li>
                    <li><a class="dropdown-item" href="#">Inactive</a></li>
                </ul>
            </div>
        </div>
    </div>

    <div class="card user-table-card border-0 bg-white">
        <div class="table-responsive">
            <table class="table mb-0 align-middle">
                <thead>
                    <tr>
                        <th style="width: 5%">#</th>
                        <th style="width: 20%">Lead Name</th>
                        <th style="width: 20%">Email</th>
                        <th style="width: 15%">Phone</th>
                        <th style="width: 20%">Status</th>
                        <th style="width: 20%">Actions</th>
                    </tr>
                </thead>

                <c:forEach items="${leadList}" var="l" varStatus="loop">
                    <tr>
                        <td>${loop.count}</td>

                        <td>${l.fullName}</td>

                        <td class="text-secondary">${l.email}</td>

                        <td> ${l.phone} </td>

                        <td>
                            <c:choose>
                                <c:when test="${l.status == 'New'}">
                                    <span class="badge badge-admin">New</span>
                                </c:when>
                                <c:when test="${l.status == 'Contacted'}">
                                    <span class="badge badge-teacher">Contacted</span>
                                </c:when>
                                <c:when test="${l.status == 'Consulting'}">
                                    <span class="badge badge-student">Consulting</span>
                                </c:when>
                                <c:when test="${l.status == 'Converted'}">
                                    <span class="badge badge-saleStaff">Converted</span>
                                </c:when>
                                <c:when test="${l.status == 'Lost'}">
                                    <span class="badge badge-academicStaff">Lost</span>
                                </c:when>
                            </c:choose>
                        </td>

                        <td>
                            <a href="lead?action=detail&id=${l.leadId}" class="action-btn"><i class='bx bx-eye'></i></a>
                            <a href="#" class="action-btn"><i class='bx bx-edit'></i></a>
                            <a href="#" class="action-btn delete"><i class='bx bx-lock'></i></a>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>

        <div class="d-flex justify-content-between align-items-center p-3 border-top">
            <div class="text-muted small">Showing 1-10 of ${courseList.size()} courses</div>
            <div>
                <ul class="pagination pagination-sm mb-0">
                    <li class="page-item disabled"><a class="page-link" href="#"><i class='bx bx-chevron-left'></i> Previous</a></li>
                    <li class="page-item active"><a class="page-link" href="#">1</a></li>
                    <li class="page-item"><a class="page-link" href="#">2</a></li>
                    <li class="page-item"><a class="page-link" href="#">3</a></li>
                    <li class="page-item"><a class="page-link" href="#">Next <i class='bx bx-chevron-right'></i></a></li>
                </ul>
            </div>
        </div>
    </div>
</div>
<script src="js/manageUser.js" type="text/javascript"></script>
