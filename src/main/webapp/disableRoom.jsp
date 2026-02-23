<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link href="css/manageUser.css" rel="stylesheet" type="text/css"/>
<style>
    .disable-card {
        background-color: white;
        padding: 30px;
        border-radius: 8px;
        box-shadow: 0 1px 3px rgba(0,0,0,0.1);
        max-width: 800px;
    }
    .warning-box {
        background: linear-gradient(135deg, #fef3c7 0%, #fde68a 100%);
        border-left: 4px solid #f59e0b;
        padding: 20px;
        border-radius: 8px;
        margin-bottom: 25px;
        display: flex;
        align-items: center;
        gap: 15px;
    }
    .warning-icon {
        font-size: 2.5rem;
        color: #f59e0b;
    }
    .warning-content h4 {
        color: #92400e;
        font-size: 1.1rem;
        font-weight: 600;
        margin: 0 0 5px 0;
    }
    .warning-content p {
        color: #78350f;
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
    .classes-info-box {
        background-color: #fef3c7;
        padding: 20px;
        border-radius: 8px;
        border: 1px solid #fbbf24;
        margin-bottom: 25px;
    }
    .classes-info-box h5 {
        color: #92400e;
        margin: 0 0 15px 0;
        font-size: 1rem;
        font-weight: 600;
    }
    .classes-info-box ul {
        margin: 0;
        padding-left: 20px;
        color: #78350f;
    }
    .classes-info-box li {
        margin-bottom: 8px;
    }
    .button-group {
        display: flex;
        gap: 10px;
        margin-top: 30px;
    }
    .disable-btn {
        background-color: #f59e0b;
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
    .disable-btn:hover {
        background-color: #d97706;
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
                <li class="breadcrumb-item active" aria-current="page">Disable Room</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">Disable Room</h2>
                <p class="text-muted small mb-0">Temporarily disable this room</p>
            </div>
        </div>
    </div>

    <div class="disable-card">
        <c:choose>
            <c:when test="${not empty roomDisable}">
                <div class="warning-box">
                    <div class="warning-icon">
                        <i class='bx bx-lock'></i>
                    </div>
                    <div class="warning-content">
                        <h4>Room will be Disabled</h4>
                        <p>This room is currently being used by classes. It will be disabled instead of deleted.</p>
                    </div>
                </div>

                <div class="room-info-box">
                    <div class="room-info-grid">
                        <div class="info-label">Room ID:</div>
                        <div class="info-value">${roomDisable.roomId}</div>

                        <div class="info-label">Room Name:</div>
                        <div class="info-value">${roomDisable.roomName}</div>

                        <div class="info-label">Capacity:</div>
                        <div class="info-value">
                            <i class='bx bx-group'></i> ${roomDisable.capacity} people
                        </div>

                        <div class="info-label">Type:</div>
                        <div class="info-value">${roomDisable.type}</div>
                    </div>
                </div>

                <c:if test="${not empty classesUsingRoom && classesUsingRoom.size() > 0}">
                    <div class="classes-info-box">
                        <h5><i class='bx bx-info-circle'></i> Classes currently using this room:</h5>
                        <ul>
                            <c:forEach var="classInfo" items="${classesUsingRoom}">
                                <li><strong>${classInfo[1]}</strong> - ${classInfo[3]} (${classInfo[5]} sessions)</li>
                            </c:forEach>
                        </ul>
                    </div>
                </c:if>

                <form action="room" method="post">
                    <input type="hidden" name="action" value="disable">
                    <input type="hidden" name="id" value="${roomDisable.roomId}">

                    <div class="button-group">
                        <button type="submit" class="disable-btn">
                            <i class='bx bx-lock'></i> Confirm Disable
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

