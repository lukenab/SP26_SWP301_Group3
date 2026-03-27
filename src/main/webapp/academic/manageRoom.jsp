<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link href="css/manageUser.css" rel="stylesheet" type="text/css"/>
<link href="css/roomManagement.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4 content-body room-page">

    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="dashboard?action=academic">Dashboard</a></li>
                <li class="breadcrumb-item active" aria-current="page">Room Management</li>
            </ol>
        </div>
        <div class="content-header mb-3">
            <div>
                <h2 class="page-title">Room Management</h2>
                <p class="text-muted small mb-0">Manage and organize your rooms</p>
            </div>
            <a href="room?action=create" class="btn btn-add-new">
                <i class='bx bx-plus-circle'></i> Create New Room
            </a>
        </div>

        <div class="card user-table-card border-0 bg-white mb-3 section-card">
                <div class="card-body p-3 p-lg-4">
                <!-- Client-side filter form: will not submit to server -->
                <form id="filterForm" onsubmit="return false;" class="row g-3 align-items-end">

                    <div class="col-md-3">
                        <label class="form-label filter-label">Filter By Capacity</label>
                        <select id="filterCapacity" name="capacity" class="form-select">
                            <option value="">All Capacities</option>
                            <c:forEach items="${capacities}" var="cap">
                                <option value="${cap}" ${capacity != null && capacity == cap ? 'selected' : ''}>${cap} people</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="col-md-3">
                        <label class="form-label filter-label">Filter By Room Type</label>
                        <select id="filterType" name="type" class="form-select">
                            <option value="">All Types</option>
                            <c:forEach items="${roomTypes}" var="rtype">
                                <option value="${rtype}" ${type != null && type == rtype ? 'selected' : ''}>${rtype}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="col-md-3">
                        <label class="form-label filter-label">Filter By Status</label>
                        <select id="filterStatus" name="status" class="form-select">
                            <option value="">All Status</option>
                            <option value="active" ${status != null && status == 'active' ? 'selected' : ''}>Active</option>
                            <option value="disabled" ${status != null && status == 'disabled' ? 'selected' : ''}>Disabled</option>
                        </select>
                    </div>

                    <div class="col-md-3 d-flex gap-2">
                        <!-- Filter button removed: filters apply automatically on change -->
                        <button id="clearFilter" type="button" class="btn btn-outline-secondary w-100 justify-content-center border" style="border-width:1px">
                            Clear
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Client-side search bar (search on page, no server requests) -->
    <div class="mb-3">
        <div class="filter-container">
            <div class="custom-search-bar w-50">
                <i class='bx bx-search text-muted fs-5'></i>
                <input type="text" id="roomSearchInput" placeholder="Search by room name..." class="form-control" />
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

    <div class="card user-table-card border-0 bg-white">
        <div class="table-responsive">
            <c:choose>
                <c:when test="${empty allRooms}">
                    <div class="text-center py-5">
                        <i class='bx bx-door-open room-empty-icon'></i>
                        <p class="text-muted mt-3">No rooms found.</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <table id="roomsTable" class="table mb-0 align-middle">
                        <thead>
                            <tr>
                                <th class="room-index-col">#</th>
                                <th>Room Name</th>
                                <th>Capacity</th>
                                <th>Description</th>
                                <th>Status</th>
                                <th class="text-end">Actions</th>
                            </tr>
                        </thead>
                        <tbody id="roomsTbody">
                            <c:forEach var="r" items="${allRooms}" varStatus="loop">
                                <!-- expose capacity/type/status as data- attributes for client-side filtering -->
                                <tr class="room-row ${!r.status ? 'room-row-disabled' : ''}" data-abs-index="${loop.count}" data-capacity="${r.capacity}" data-type="${fn:escapeXml(r.type)}" data-status="${r.status}">
                                    <td class="room-index">${loop.count}</td>
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
                                                    <!-- Room has classes: no disable action shown because it's not available -->
                                                    <!-- Intentionally left blank to avoid showing an unusable control -->
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

                    <!-- Pagination controls (client-side) - styled like other management pages -->
                    <div id="paginationWrapper" class="mt-3">
                        <div class="d-flex justify-content-between align-items-center p-3 border-top">
                            <div class="text-muted small" id="roomsSummary">Showing 0-0 of 0 rooms</div>
                            <div>
                                <ul id="roomsPagination" class="pagination pagination-sm mb-0"></ul>
                            </div>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
<script src="js/manageUser.js" type="text/javascript"></script>
<script src="js/manageRoom.js" type="text/javascript"></script>
