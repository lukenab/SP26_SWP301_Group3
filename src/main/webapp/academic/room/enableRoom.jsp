<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link href="css/createUser.css" rel="stylesheet" type="text/css"/>
<link href="css/roomManagement.css" rel="stylesheet" type="text/css"/>

<div class="room-page">
<div class="mb-4">
    <div class="content-header">
        <div>
            <h1 class="page-title">Enable Room</h1>
        </div>
        <a href="room" class="btn-secondary">
            <i class='bx bx-arrow-left'></i> Back to Rooms
        </a>
    </div>
    <div aria-label="breadcrumb">
        <ol class="breadcrumb mb-1">
            <li class="breadcrumb-item"><a href="dashboard">Dashboard</a></li>
            <li class="breadcrumb-item"><a href="room">Room Management</a></li>
            <li class="breadcrumb-item active" aria-current="page">Enable Room</li>
        </ol>
    </div>
</div>

<div class="form-container">
    <p class="form-title">Confirm Room Enable</p>

    <c:choose>
        <c:when test="${not empty roomEnable}">
            <div class="alert-box info room-alert-spaced">
                <i class='bx bx-info-circle'></i> <strong>Note:</strong> Enabling this room will make it available for class assignments again.
            </div>

            <div class="form-body">
                <div class="form-row">
                    <div class="form-group">
                        <label>Room Name</label>
                        <input type="text" value="${roomEnable.roomName}" disabled>
                    </div>

                    <div class="form-group">
                        <label>Capacity</label>
                        <input type="text" value="${roomEnable.capacity} people" disabled>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group full-width">
                        <label>Type</label>
                        <textarea disabled>${roomEnable.type}</textarea>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label>Current Status</label>
                        <input type="text" value="Disabled" disabled>
                    </div>
                </div>

                <form action="" method="post" class="form-buttons">
                    <input type="hidden" name="action" value="enable">
                    <input type="hidden" name="id" value="${roomEnable.roomId}">
                    <a href="room" class="btn btn-cancel">Cancel</a>
                    <button type="submit" class="btn btn-unlock">
                        <i class='bx bx-check-circle'></i> Confirm Enable
                    </button>
                </form>
            </div>
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

