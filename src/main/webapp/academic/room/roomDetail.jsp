<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link href="css/viewUser.css" rel="stylesheet" type="text/css"/>
<link href="css/roomManagement.css" rel="stylesheet" type="text/css"/>

<div class="page-header room-page">
    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="dashboard">Dashboard</a></li>
                <li class="breadcrumb-item"><a href="room">Rooms</a></li>
                <li class="breadcrumb-item active" aria-current="page">Room Details</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">Room Detail</h2>
                <p class="text-muted small mb-0">View room details and assignments</p>
            </div>
            <a href="room" class="btn-secondary">
                <i class='bx bx-arrow-left'></i> Back to Rooms
            </a>
        </div>
    </div>

    <div class="profile-header-card">
        <div class="profile-avatar-section">
            <div class="form-row user-img">
                <div class="info-img room-avatar">
                    <i class='bx bx-door-open room-avatar-icon'></i>
                </div>
            </div>
        </div>

        <div class="profile-header-info">
            <h2 class="profile-name">${roomDetail.roomName}</h2>
            <span class="profile-department">${roomDetail.type}</span>
            <c:choose>
                <c:when test="${roomDetail.status}">
                    <span class="profile-active">Active</span>
                </c:when>
                <c:otherwise>
                    <span class="profile-active room-profile-disabled">Disabled</span>
                </c:otherwise>
            </c:choose>
            <p>Capacity: ${roomDetail.capacity} people</p>
            <div class="profile-info-content">
                <div class="profile-header-left">
                    <span class="user-email"><i class="bx bx-group"></i>Capacity: ${roomDetail.capacity}</span>
                    <span class="user-email"><i class="bx bx-door-open"></i>Room ID: #${roomDetail.roomId}</span>
                </div>
                <div class="profile-header-right">
                    <span class="user-email"><i class="bx bx-buildings"></i>Type: ${roomDetail.type}</span>
                    <span class="user-email">
                        <i class="bx ${roomDetail.status ? 'bx-check-circle' : 'bx-x-circle'}"></i>
                        Status: ${roomDetail.status ? 'Active' : 'Disabled'}
                    </span>
                </div>
            </div>
        </div>
    </div>

    <div class="profile-content-card">
        <div class="profile-tabs">
            <a href="#" class="tab-item active"><i class='bx bxs-info-circle'></i>Room Information</a>
        </div>

        <div class="tab-content" id="overview">
            <div class="info-section">
                <h6 class="room-section-title">Basic Information</h6>
                <div class="info-grid">
                    <div class="info-item">
                        <p>Room Name</p>
                        <span>${roomDetail.roomName}</span>
                    </div>
                    <div class="info-item">
                        <p>Capacity</p>
                        <span>${roomDetail.capacity} people</span>
                    </div>
                    <div class="info-item">
                        <p>Type</p>
                        <span>${roomDetail.type}</span>
                    </div>
                    <div class="info-item">
                        <p>Status</p>
                        <span>${roomDetail.status ? 'Active' : 'Disabled'}</span>
                    </div>
                </div>
            </div>

            <c:if test="${not empty classesUsingRoom}">
                <div class="info-section">
                    <h6 class="room-section-title">
                        <i class='bx bx-calendar-event'></i> Classes Using This Room
                    </h6>
                    <div class="room-table-wrapper">
                        <table class="table room-detail-table">
                            <thead>
                                <tr>
                                    <th>Class Name</th>
                                    <th>Learning Date</th>
                                    <th>Slot</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="classInfo" items="${classesUsingRoom}">
                                    <tr>
                                        <td>${classInfo[0]}</td>
                                        <td>${classInfo[1]}</td>
                                        <td>Slot ${classInfo[2]}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </c:if>

            <c:if test="${empty classesUsingRoom}">
                <div class="info-section room-empty-section">
                    <i class='bx bx-calendar-x'></i>
                    <p>No classes are currently assigned to this room.</p>
                </div>
            </c:if>

            <div class="form-buttons room-detail-actions">
                <a href="room" class="btn btn-cancel room-btn-neutral">
                    <i class='bx bx-arrow-left'></i> Back to List
                </a>
                <c:if test="${roomDetail.status}">
                    <a href="?action=update&id=${roomDetail.roomId}" class="btn btn-save room-btn-primary">
                        <i class='bx bx-edit'></i> Edit Room
                    </a>
                </c:if>
            </div>
        </div>
    </div>
</div>
