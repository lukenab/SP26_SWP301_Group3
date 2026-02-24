<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link href="css/manageUser.css" rel="stylesheet" type="text/css"/>
<style>
    .enable-card {
        background-color: white;
        padding: 30px;
        border-radius: 8px;
        box-shadow: 0 1px 3px rgba(0,0,0,0.1);
        max-width: 800px;
    }
    .info-box {
        background: linear-gradient(135deg, #d1fae5 0%, #a7f3d0 100%);
        border-left: 4px solid #10b981;
        padding: 20px;
        border-radius: 8px;
        margin-bottom: 25px;
        display: flex;
        align-items: center;
        gap: 15px;
    }
    .info-icon {
        font-size: 2.5rem;
        color: #10b981;
    }
    .info-content h4 {
        color: #065f46;
        font-size: 1.1rem;
        font-weight: 600;
        margin: 0 0 5px 0;
    }
    .info-content p {
        color: #047857;
        margin: 0;
        font-size: 0.9rem;
    }
    .room-info-box {
        background-color: #f9fafb;
        padding: 20px;
        border-radius: 8px;
        border: 1px solid var(--border-color);
        margin-bottom: 25px;
    }
    .room-info-grid {
        display: grid;
        grid-template-columns: 180px 1fr;
        gap: 15px;
    }
    .info-label {
        font-weight: 600;
        color: var(--text-secondary);
        font-size: 14px;
    }
    .info-value {
        color: var(--text-primary);
        font-size: 14px;
    }
    .button-group {
        display: flex;
        gap: 10px;
        margin-top: 30px;
    }
    .enable-btn {
        background-color: #10b981;
        color: white;
        padding: 10px 24px;
        border: none;
        border-radius: 6px;
        cursor: pointer;
        font-size: 14px;
        font-weight: 500;
        transition: all 0.3s ease;
        display: inline-flex;
        align-items: center;
        gap: 5px;
    }
    .enable-btn:hover {
        background-color: #059669;
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
                <li class="breadcrumb-item active" aria-current="page">Enable Room</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">Enable Room</h2>
                <p class="text-muted small mb-0">Reactivate this disabled room</p>
            </div>
        </div>
    </div>

    <div class="enable-card">
        <c:choose>
            <c:when test="${not empty roomEnable}">
                <div class="info-box">
                    <div class="info-icon">
                        <i class='bx bx-check-circle'></i>
                    </div>
                    <div class="info-content">
                        <h4>Room will be Enabled</h4>
                        <p>This room will be reactivated and available for scheduling again.</p>
                    </div>
                </div>

                <div class="room-info-box">
                    <div class="room-info-grid">
                        <div class="info-label">Room ID:</div>
                        <div class="info-value">${roomEnable.roomId}</div>

                        <div class="info-label">Room Name:</div>
                        <div class="info-value">${roomEnable.roomName}</div>

                        <div class="info-label">Capacity:</div>
                        <div class="info-value">
                            <i class='bx bx-group'></i> ${roomEnable.capacity} people
                        </div>

                        <div class="info-label">Type:</div>
                        <div class="info-value">${roomEnable.type}</div>

                        <div class="info-label">Current Status:</div>
                        <div class="info-value">
                            <span class="badge" style="background-color: #ef4444; color: white; padding: 4px 12px; border-radius: 12px;">Disabled</span>
                        </div>
                    </div>
                </div>

                <form action="room" method="post">
                    <input type="hidden" name="action" value="enable">
                    <input type="hidden" name="id" value="${roomEnable.roomId}">

                    <div class="button-group">
                        <button type="submit" class="enable-btn">
                            <i class='bx bx-check'></i> Confirm Enable
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

