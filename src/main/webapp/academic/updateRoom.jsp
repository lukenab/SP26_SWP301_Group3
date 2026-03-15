<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link href="css/editUser.css" rel="stylesheet" type="text/css"/>
<link href="css/roomManagement.css" rel="stylesheet" type="text/css"/>

<div class="room-page">
<div class="mb-4">
    <div class="content-header">
        <div>
            <h1 class="page-title">Edit Room</h1>
        </div>
        <a href="room" class="btn-secondary">
            <i class='bx bx-arrow-left'></i> Back to Rooms
        </a>
    </div>
    <div aria-label="breadcrumb">
        <ol class="breadcrumb mb-1">
            <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
            <li class="breadcrumb-item"><a href="room">Room Management</a></li>
            <li class="breadcrumb-item active" aria-current="page">Edit Room</li>
        </ol>
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
        <h2 class="profile-name">${roomUpdate.roomName}</h2>
        <span class="profile-department">${roomUpdate.type}</span>
        <c:choose>
            <c:when test="${roomUpdate.status}">
                <span class="profile-active">Active</span>
            </c:when>
            <c:otherwise>
                <span class="profile-active room-profile-disabled">Disabled</span>
            </c:otherwise>
        </c:choose>
        <p>Capacity: ${roomUpdate.capacity} people</p>
        <div class="profile-info-content">
            <div class="profile-header-left">
                <span class="user-email"><i class="bx bx-group"></i>Capacity: ${roomUpdate.capacity}</span>
                <span class="user-email"><i class="bx bx-door-open"></i>Room ID: #${roomUpdate.roomId}</span>
            </div>
            <div class="profile-header-right">
                <span class="user-email"><i class="bx bx-buildings"></i>Type: ${roomUpdate.type}</span>
                <span class="user-email">
                    <i class="bx ${roomUpdate.status ? 'bx-check-circle' : 'bx-x-circle'}"></i>
                    Status: ${roomUpdate.status ? 'Active' : 'Disabled'}
                </span>
            </div>
        </div>
    </div>
</div>

<div class="form-container">
    <c:choose>
        <c:when test="${not empty roomUpdate}">
            <form action="room" method="post" class="form-body">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="id" value="${roomUpdate.roomId}">

                <div class="form-row">
                    <div class="form-group">
                        <label for="name">Room Name</label>
                        <input type="text" id="name" name="name" value="${roomUpdate.roomName}" placeholder="Enter room name" required>
                    </div>

                    <div class="form-group">
                        <label for="capacity">Capacity</label>
                        <input type="number" id="capacity" name="capacity" value="${roomUpdate.capacity}" min="1" placeholder="Enter room capacity" required>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="type">Type</label>
                        <textarea id="type" name="type" placeholder="Enter room type or description" required>${roomUpdate.type}</textarea>
                    </div>

                    <div class="form-group">
                        <label for="status">Status</label>
                        <select id="status" name="status" required>
                            <option value="1" ${roomUpdate.status ? 'selected' : ''}>Active</option>
                            <option value="0" ${!roomUpdate.status ? 'selected' : ''}>Disabled</option>
                        </select>
                    </div>
                </div>

                <div class="form-buttons">
                    <a href="room" class="btn btn-cancel">Cancel</a>
                    <button type="submit" class="btn btn-save">
                        <i class='bx bx-save'></i> Update Changes
                    </button>
                </div>
            </form>
        </c:when>
        <c:otherwise>
            <div class="alert-box info">
                <strong>Error:</strong> Room not found.
            </div>
            <div class="form-buttons">
                <a href="room" class="btn btn-cancel">Back to Rooms</a>
            </div>
        </c:otherwise>
    </c:choose>
</div>

</div>

