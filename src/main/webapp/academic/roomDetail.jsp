<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link href="css/manageUser.css" rel="stylesheet" type="text/css"/>
<style>
    .detail-card {
        background-color: white;
        padding: 30px;
        border-radius: 8px;
        box-shadow: 0 1px 3px rgba(0,0,0,0.1);
        max-width: 1200px;
    }
    .detail-grid {
        display: grid;
        grid-template-columns: 180px 1fr;
        gap: 20px;
        margin-bottom: 30px;
    }
    .detail-label {
        font-weight: 600;
        color: var(--text-secondary);
        font-size: 14px;
    }
    .detail-value {
        color: var(--text-primary);
        font-size: 14px;
    }
    .room-icon-header {
        display: flex;
        align-items: center;
        gap: 15px;
        padding: 20px;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        border-radius: 8px;
        margin-bottom: 30px;
    }
    .room-icon-large {
        width: 60px;
        height: 60px;
        background-color: rgba(255,255,255,0.2);
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 2rem;
        color: white;
    }
    .room-header-text h3 {
        color: white;
        margin: 0;
        font-size: 1.5rem;
        font-weight: 600;
    }
    .room-header-text p {
        color: rgba(255,255,255,0.9);
        margin: 0;
        font-size: 0.9rem;
    }
    .button-group {
        display: flex;
        gap: 10px;
        margin-top: 30px;
        padding-top: 20px;
        border-top: 1px solid var(--border-color);
    }
    .btn-back {
        background-color: var(--primary-blue);
        color: white;
        padding: 10px 24px;
        border: none;
        border-radius: 6px;
        cursor: pointer;
        text-decoration: none;
        display: inline-flex;
        align-items: center;
        gap: 5px;
        font-size: 14px;
        font-weight: 500;
        transition: all 0.3s ease;
    }
    .btn-back:hover {
        background-color: #1d4ed8;
        color: white;
        transform: translateY(-1px);
    }
    .btn-edit {
        background-color: #10b981;
        color: white;
        padding: 10px 24px;
        border: none;
        border-radius: 6px;
        cursor: pointer;
        text-decoration: none;
        display: inline-flex;
        align-items: center;
        gap: 5px;
        font-size: 14px;
        font-weight: 500;
        transition: all 0.3s ease;
    }
    .btn-edit:hover {
        background-color: #059669;
        color: white;
        transform: translateY(-1px);
    }
    .btn-delete {
        background-color: #ef4444;
        color: white;
        padding: 10px 24px;
        border: none;
        border-radius: 6px;
        cursor: pointer;
        text-decoration: none;
        display: inline-flex;
        align-items: center;
        gap: 5px;
        font-size: 14px;
        font-weight: 500;
        transition: all 0.3s ease;
    }
    .btn-delete:hover {
        background-color: #dc2626;
        color: white;
        transform: translateY(-1px);
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
    .section-title {
        font-size: 1.2rem;
        font-weight: 600;
        color: var(--text-primary);
        margin: 30px 0 15px 0;
        padding-bottom: 10px;
        border-bottom: 2px solid var(--border-color);
    }
    .class-table-wrapper {
        margin-top: 20px;
        border: 1px solid var(--border-color);
        border-radius: 8px;
        overflow: hidden;
    }
    .empty-state {
        text-align: center;
        padding: 40px;
        color: var(--text-secondary);
    }
    .empty-state i {
        font-size: 3rem;
        margin-bottom: 15px;
        opacity: 0.5;
    }
    .status-badge {
        padding: 4px 12px;
        border-radius: 12px;
        font-size: 12px;
        font-weight: 500;
    }
    .status-active { background-color: #10b981; color: white; }
    .status-pending { background-color: #f59e0b; color: white; }
    .status-finished { background-color: #6b7280; color: white; }
</style>

<div class="container-fluid px-4 content-body">
    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="dashboard">Dashboard</a></li>
                <li class="breadcrumb-item"><a href="room">Room Management</a></li>
                <li class="breadcrumb-item active" aria-current="page">Room Details</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">Room Details</h2>
                <p class="text-muted small mb-0">View detailed information about the room</p>
            </div>
        </div>
    </div>

    <div class="detail-card">
        <c:choose>
            <c:when test="${not empty roomDetail}">
                <div class="room-icon-header">
                    <div class="room-icon-large">
                        <i class='bx bx-door-open'></i>
                    </div>
                    <div class="room-header-text">
                        <h3>${roomDetail.roomName}</h3>
                        <p>Room ID: ${roomDetail.roomId}</p>
                    </div>
                </div>

                <div class="detail-grid">
                    <div class="detail-label">Room ID:</div>
                    <div class="detail-value">${roomDetail.roomId}</div>

                    <div class="detail-label">Room Name:</div>
                    <div class="detail-value">${roomDetail.roomName}</div>

                    <div class="detail-label">Capacity:</div>
                    <div class="detail-value">
                        <i class='bx bx-group'></i> ${roomDetail.capacity} people
                    </div>

                    <div class="detail-label">Type:</div>
                    <div class="detail-value">${roomDetail.type}</div>

                    <div class="detail-label">Status:</div>
                    <div class="detail-value">
                        <c:choose>
                            <c:when test="${roomDetail.status}">
                                <span class="status-badge status-active">Active</span>
                            </c:when>
                            <c:otherwise>
                                <span class="status-badge" style="background-color: #ef4444;">Disabled</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <!-- Classes Using This Room Section -->
                <h3 class="section-title">
                    <i class='bx bx-chalkboard'></i> Classes Using This Room
                </h3>

                <div class="class-table-wrapper">
                    <c:choose>
                        <c:when test="${empty classesUsingRoom}">
                            <div class="empty-state">
                                <i class='bx bx-info-circle'></i>
                                <p>No classes are currently assigned to this room.</p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <table class="table mb-0 align-middle">
                                <thead>
                                    <tr>
                                        <th style="width: 80px">Class ID</th>
                                        <th>Class Name</th>
                                        <th>Course</th>
                                        <th>Teacher</th>
                                        <th>Status</th>
                                        <th style="width: 100px">Schedules</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="classInfo" items="${classesUsingRoom}">
                                        <tr>
                                            <td>${classInfo[0]}</td>
                                            <td>
                                                <strong>${classInfo[1]}</strong>
                                            </td>
                                            <td class="text-secondary">${classInfo[3]}</td>
                                            <td class="text-secondary">
                                                <i class='bx bx-user'></i> ${classInfo[4] != null ? classInfo[4] : 'N/A'}
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${classInfo[2] == 'Active'}">
                                                        <span class="status-badge status-active">Active</span>
                                                    </c:when>
                                                    <c:when test="${classInfo[2] == 'Pending'}">
                                                        <span class="status-badge status-pending">Pending</span>
                                                    </c:when>
                                                    <c:when test="${classInfo[2] == 'Finished'}">
                                                        <span class="status-badge status-finished">Finished</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="status-badge">${classInfo[2]}</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="text-center">
                                                <span class="badge bg-primary">${classInfo[5]} sessions</span>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="button-group">
                    <a href="room" class="btn-back">
                        <i class='bx bx-arrow-left'></i> Back to List
                    </a>
                    <c:if test="${roomDetail.status}">
                        <a href="room?action=update&id=${roomDetail.roomId}" class="btn-edit">
                            <i class='bx bx-edit'></i> Edit Room
                        </a>
                        <a href="room?action=delete&id=${roomDetail.roomId}" class="btn-delete">
                            <i class='bx bx-trash'></i> Delete Room
                        </a>
                    </c:if>
                </div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-danger">
                    <strong>Error:</strong> Room not found.
                </div>
                <div class="button-group">
                    <a href="room" class="btn-back">
                        <i class='bx bx-arrow-left'></i> Back to List
                    </a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
