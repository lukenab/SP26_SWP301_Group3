<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link href="css/manageUser.css" rel="stylesheet" type="text/css"/>
<link href="css/roomManagement.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4 content-body room-page">

    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="dashboard">Dashboard</a></li>
                <li class="breadcrumb-item active" aria-current="page">Room Management</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">Room Management</h2>
                <p class="text-muted small mb-0">Manage and organize your rooms</p>
            </div>
            <a href="room?action=create" class="btn btn-add-new">
                <i class='bx bx-plus-circle'></i> Create New Room
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

    <div class="card user-table-card border-0 bg-white">
        <div class="table-responsive">
            <c:choose>
                <c:when test="${empty allRooms}">
                    <div class="text-center py-5">
                        <i class='bx bx-door-open room-empty-icon'></i>
                        <p class="text-muted mt-3">No rooms found.</p>
                        <a href="room?action=create" class="btn btn-add-new mt-2">
                            <i class='bx bx-plus-circle'></i> Create Your First Room
                        </a>
                    </div>
                </c:when>
                <c:otherwise>
                    <table class="table mb-0 align-middle">
                        <thead>
                            <tr>
                                <th class="room-index-col">#</th>
                                <th>Room Name</th>
                                <th>Capacity</th>
                                <th>Type</th>
                                <th>Status</th>
                                <th class="text-end">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="r" items="${allRooms}" varStatus="loop">
                                <tr class="${!r.status ? 'room-row-disabled' : ''}">
                                    <td>${loop.count}</td>
                                    <td>
                                        <div class="d-flex align-items-center">
                                            <i class='bx bx-door-open me-2 room-row-icon'></i>
                                            <span class="user-name">${r.roomName}</span>
                                        </div>
                                    </td>
                                    <td class="text-secondary">${r.capacity} people</td>
                                    <td class="text-secondary">${r.type}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${r.status}">
                                                <span class="badge room-status-badge active">Active</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge room-status-badge disabled">Disabled</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-end">
                                        <a href="room?action=detail&id=${r.roomId}" class="action-btn" title="View Details">
                                            <i class='bx bx-file-detail'></i>
                                        </a>
                                        <c:if test="${r.status}">
                                            <a href="room?action=update&id=${r.roomId}" class="action-btn" title="Edit">
                                                <i class='bx bx-edit'></i>
                                            </a>
                                            <c:choose>
                                                <c:when test="${roomUsageMap[r.roomId]}">
                                                    <!-- Room has classes, show Disable button -->
                                                    <a href="room?action=disable&id=${r.roomId}" class="action-btn room-action-disable" title="Disable Room">
                                                        <i class='bx bx-lock'></i>
                                                    </a>
                                                </c:when>
                                                <c:otherwise>
                                                    <!-- Room has no classes, show Delete button -->
                                                    <a href="room?action=delete&id=${r.roomId}" class="action-btn delete" title="Delete">
                                                        <i class='bx bx-trash'></i>
                                                    </a>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:if>
                                        <c:if test="${!r.status}">
                                            <!-- Room is disabled, show Enable button -->
                                            <a href="room?action=enable&id=${r.roomId}" class="action-btn room-action-enable" title="Enable Room">
                                                <i class='bx bx-check-circle'></i>
                                            </a>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>

        <c:if test="${not empty allRooms}">
            <div class="d-flex justify-content-between align-items-center p-3 border-top">
                <div class="text-muted small">Showing 1-${allRooms.size()} of ${allRooms.size()} rooms</div>
                <div>
                    <ul class="pagination pagination-sm mb-0">
                        <li class="page-item disabled"><a class="page-link" href="#"><i class='bx bx-chevron-left'></i> Previous</a></li>
                        <li class="page-item active"><a class="page-link" href="#">1</a></li>
                        <li class="page-item"><a class="page-link" href="#">Next <i class='bx bx-chevron-right'></i></a></li>
                    </ul>
                </div>
            </div>
        </c:if>
    </div>
</div>
<script src="/js/manageUser.js" type="text/javascript"></script>