<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link href="css/createUser.css" rel="stylesheet" type="text/css"/>
<link href="css/roomManagement.css" rel="stylesheet" type="text/css"/>

<div class="room-page">
<div class="mb-4">
    <div class="content-header">
        <div>
            <h1 class="page-title">Delete Room</h1>
        </div>
        <a href="" class="btn-secondary">
            <i class='bx bx-arrow-left'></i> Back to Rooms
        </a>
    </div>
    <div aria-label="breadcrumb">
        <ol class="breadcrumb mb-1">
            <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
            <li class="breadcrumb-item"><a href="">Room Management</a></li>
            <li class="breadcrumb-item active" aria-current="page">Delete Room</li>
        </ol>
    </div>
</div>

<div class="form-container">
    <p class="form-title">Confirm Room Deletion</p>

    <c:choose>
        <c:when test="${not empty roomDel}">
            <div class="alert-box info room-alert-spaced">
                <i class='bx bx-info-circle'></i> <strong>Warning:</strong> This action cannot be undone. Are you sure you want to delete this room?
            </div>

            <div class="form-body">
                <div class="form-row">
                    <div class="form-group">
                        <label>Room Name</label>
                        <input type="text" value="${roomDel.roomName}" disabled>
                    </div>

                    <div class="form-group">
                        <label>Capacity</label>
                        <input type="text" value="${roomDel.capacity} people" disabled>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group full-width">
                        <label>Type</label>
                        <textarea disabled>${roomDel.type}</textarea>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label>Status</label>
                        <input type="text" value="${roomDel.status ? 'Active' : 'Disabled'}" disabled>
                    </div>
                </div>

                <form action="" method="post" class="form-buttons">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="id" value="${roomDel.roomId}">
                    <a href="" class="btn btn-cancel">Cancel</a>
                    <button type="submit" class="btn btn-lock">
                        <i class='bx bx-trash'></i> Confirm Delete
                    </button>
                </form>
            </div>
        </c:when>
        <c:otherwise>
            <div class="alert-box info">
                <strong>Error:</strong> Room not found.
            </div>
            <div class="form-buttons">
                <a href="" class="btn btn-cancel">Back to Rooms</a>
            </div>
        </c:otherwise>
    </c:choose>
</div>

</div>

