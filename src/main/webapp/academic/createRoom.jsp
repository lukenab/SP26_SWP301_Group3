<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link href="css/createUser.css" rel="stylesheet" type="text/css"/>

<div class="mb-4">
    <div aria-label="breadcrumb">
        <ol class="breadcrumb mb-1">
            <li class="breadcrumb-item"><a href="dashboard?action=academic">Dashboard</a></li>
            <li class="breadcrumb-item"><a href="room">Room Management</a></li>
            <li class="breadcrumb-item active" aria-current="page">Create Room</li>
        </ol>
    </div>
    <div class="content-header">
        <div>
            <h2 class="page-title">Room Management</h2>
            <p class="text-muted small mb-0">Create new room</p>
        </div>
        <a href="room" class="btn-secondary">
            <i class='bx bx-arrow-left'></i> Back to Rooms
        </a>
    </div>
</div>

<div class="form-container">
    <div class="form-tabs">
        <div class="form-tab active">
            <i class='bx bx-door-open'></i>
            <span>Room Information</span>
        </div>
    </div>

    <p class="form-title">Basic Information</p>

    <c:if test="${not empty sessionScope.error}">
        <div class="alert-box info">
            <i class='bx bx-error-circle'></i> ${sessionScope.error}
        </div>
        <% session.removeAttribute("error"); %>
    </c:if>

    <form action="room" method="post" class="form-body">
        <input type="hidden" name="action" value="create">

        <div class="form-row">
            <div class="form-group">
                <label for="name">Room Name <span>*</span></label>
                <input type="text" id="name" name="name" placeholder="Enter room name" required>
            </div>

            <div class="form-group">
                <label for="capacity">Capacity <span>*</span></label>
                <input type="number" id="capacity" name="capacity" min="1" placeholder="Enter room capacity" required>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="type">Type <span>*</span></label>
                <textarea id="type" name="type" placeholder="Enter room type or description" required></textarea>
            </div>

            <div class="form-group">
                <label for="status">Status <span>*</span></label>
                <select id="status" name="status" required>
                    <option value="1" selected>Active</option>
                    <option value="0">Disabled</option>
                </select>
            </div>
        </div>

        <div class="form-buttons">
            <button type="button" class="btn btn-cancel" onclick="window.location.href='room'">Cancel</button>
            <button type="submit" class="btn btn-save">
                <i class='bx bx-check'></i> Create Room
            </button>
        </div>
    </form>
</div>
