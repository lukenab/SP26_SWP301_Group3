<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link href="css/viewUser.css" rel="stylesheet" type="text/css"/>

<div class="page-header">
    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
                <li class="breadcrumb-item"><a href="room">Rooms</a></li>
                <li class="breadcrumb-item active" aria-current="page">Room Details</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">Room Management</h2>
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
                <div class="info-img" style="width: 120px; height: 120px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); display: flex; align-items: center; justify-content: center; border-radius: 50%;">
                    <i class='bx bx-door-open' style="font-size: 4rem; color: white;"></i>
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
                    <span class="profile-active" style="background-color: #fee2e2; color: #991b1b;">Disabled</span>
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
                <h6 style="font-weight: 600; margin-bottom: 16px; color: #374151;">Basic Information</h6>
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
                    <h6 style="font-weight: 600; margin-bottom: 16px; color: #374151;">
                        <i class='bx bx-calendar-event'></i> Classes Using This Room
                    </h6>
                    <div style="overflow-x: auto;">
                        <table class="table" style="width: 100%; border-collapse: collapse;">
                            <thead style="background-color: #f9fafb;">
                                <tr>
                                    <th style="padding: 12px; text-align: left; border-bottom: 2px solid #e5e7eb; font-weight: 600; color: #6b7280; font-size: 14px;">Class Name</th>
                                    <th style="padding: 12px; text-align: left; border-bottom: 2px solid #e5e7eb; font-weight: 600; color: #6b7280; font-size: 14px;">Learning Date</th>
                                    <th style="padding: 12px; text-align: left; border-bottom: 2px solid #e5e7eb; font-weight: 600; color: #6b7280; font-size: 14px;">Slot</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="classInfo" items="${classesUsingRoom}">
                                    <tr>
                                        <td style="padding: 12px; border-bottom: 1px solid #e5e7eb; color: #374151;">${classInfo[0]}</td>
                                        <td style="padding: 12px; border-bottom: 1px solid #e5e7eb; color: #374151;">${classInfo[1]}</td>
                                        <td style="padding: 12px; border-bottom: 1px solid #e5e7eb; color: #374151;">Slot ${classInfo[2]}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </c:if>

            <c:if test="${empty classesUsingRoom}">
                <div class="info-section" style="text-align: center; padding: 40px 20px;">
                    <i class='bx bx-calendar-x' style="font-size: 3rem; color: #9ca3af;"></i>
                    <p style="color: #6b7280; margin-top: 12px;">No classes are currently assigned to this room.</p>
                </div>
            </c:if>

            <div class="form-buttons" style="display: flex; justify-content: flex-end; gap: 12px; margin-top: 24px; padding-top: 24px; border-top: 1px solid #e5e7eb;">
                <a href="room" class="btn btn-cancel" style="padding: 10px 18px; font-size: 14px; font-weight: 600; border: none; border-radius: 8px; cursor: pointer; display: flex; align-items: center; gap: 8px; text-decoration: none; background: #f0f0f0; color: #555; border: 1px solid #ddd;">
                    <i class='bx bx-arrow-left'></i> Back to List
                </a>
                <c:if test="${roomDetail.status}">
                    <a href="room?action=update&id=${roomDetail.roomId}" class="btn btn-save" style="padding: 10px 18px; font-size: 14px; font-weight: 600; border: none; border-radius: 8px; cursor: pointer; display: flex; align-items: center; gap: 8px; text-decoration: none; background: #1775F1; color: #fff;">
                        <i class='bx bx-edit'></i> Edit Room
                    </a>
                </c:if>
            </div>
        </div>
    </div>
</div>
