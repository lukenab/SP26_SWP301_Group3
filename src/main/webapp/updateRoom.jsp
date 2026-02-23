<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link href="css/manageUser.css" rel="stylesheet" type="text/css"/>
<style>
    .form-container {
        background-color: white;
        padding: 30px;
        border-radius: 8px;
        box-shadow: 0 1px 3px rgba(0,0,0,0.1);
        max-width: 800px;
    }
    .form-group {
        margin-bottom: 20px;
    }
    .form-group label {
        display: block;
        margin-bottom: 8px;
        font-weight: 600;
        color: var(--text-primary);
        font-size: 14px;
    }
    .form-group input[type="text"],
    .form-group input[type="number"],
    .form-group textarea,
    .form-group select {
        width: 100%;
        padding: 10px 12px;
        border: 1px solid var(--border-color);
        border-radius: 6px;
        box-sizing: border-box;
        font-size: 14px;
        transition: border-color 0.2s;
    }
    .form-group input:focus,
    .form-group textarea:focus,
    .form-group select:focus {
        outline: none;
        border-color: var(--primary-blue);
    }
    .form-group textarea {
        resize: vertical;
        min-height: 100px;
        font-family: inherit;
    }
    .required {
        color: #dc3545;
    }
    .button-group {
        display: flex;
        gap: 10px;
        margin-top: 30px;
    }
    .submit-btn {
        background-color: var(--primary-blue);
        color: white;
        padding: 10px 24px;
        border: none;
        border-radius: 6px;
        cursor: pointer;
        font-size: 14px;
        font-weight: 500;
        transition: all 0.3s ease;
    }
    .submit-btn:hover {
        background-color: #1d4ed8;
        transform: translateY(-1px);
    }
    .cancel-btn {
        background-color: #6B7280;
        color: white;
        padding: 10px 24px;
        border: none;
        border-radius: 6px;
        cursor: pointer;
        font-size: 14px;
        font-weight: 500;
        transition: all 0.3s ease;
    }
    .cancel-btn:hover {
        background-color: #4B5563;
    }
    .alert {
        padding: 12px 16px;
        border-radius: 6px;
        margin-bottom: 20px;
    }
    .alert-danger {
        background-color: #fee;
        color: #c33;
        border: 1px solid #fcc;
    }
</style>

<div class="container-fluid px-4 content-body">
    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="dashboard">Dashboard</a></li>
                <li class="breadcrumb-item"><a href="room">Room Management</a></li>
                <li class="breadcrumb-item active" aria-current="page">Update Room</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">Update Room</h2>
                <p class="text-muted small mb-0">Modify room information</p>
            </div>
        </div>
    </div>

    <div class="form-container">
        <c:choose>
            <c:when test="${not empty roomUpdate}">
                <form action="room" method="post">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="id" value="${roomUpdate.roomId}">

                    <div class="form-group">
                        <label for="name">Room Name: <span class="required">*</span></label>
                        <input type="text" id="name" name="name" value="${roomUpdate.roomName}" placeholder="Enter room name" required>
                    </div>

                    <div class="form-group">
                        <label for="capacity">Capacity: <span class="required">*</span></label>
                        <input type="number" id="capacity" name="capacity" value="${roomUpdate.capacity}" min="1" placeholder="Enter room capacity" required>
                    </div>

                    <div class="form-group">
                        <label for="type">Type: <span class="required">*</span></label>
                        <textarea id="type" name="type" placeholder="Enter room type or description" required>${roomUpdate.type}</textarea>
                    </div>

                    <div class="form-group">
                        <label for="status">Status: <span class="required">*</span></label>
                        <select id="status" name="status" required>
                            <option value="1" ${roomUpdate.status ? 'selected' : ''}>Active</option>
                            <option value="0" ${!roomUpdate.status ? 'selected' : ''}>Disabled</option>
                        </select>
                    </div>

                    <div class="button-group">
                        <button type="submit" class="submit-btn">
                            <i class='bx bx-save'></i> Update Room
                        </button>
                        <button type="button" class="cancel-btn" onclick="window.location.href='room'">
                            <i class='bx bx-x'></i> Cancel
                        </button>
                    </div>
                </form>
            </c:when>
            <c:otherwise>
                <div class="alert alert-danger">
                    <strong>Error:</strong> Room not found.
                </div>
                <div class="button-group">
                    <button type="button" class="cancel-btn" onclick="window.location.href='room'">
                        <i class='bx bx-arrow-left'></i> Back to Rooms
                    </button>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
