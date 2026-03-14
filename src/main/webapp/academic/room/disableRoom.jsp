<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link href="css/createUser.css" rel="stylesheet" type="text/css"/>
<link href="css/roomManagement.css" rel="stylesheet" type="text/css"/>

<div class="room-page">
<div class="mb-4">
    <div class="content-header">
        <div>
            <h1 class="page-title">Disable Room</h1>
        </div>
        <a href="" class="btn-secondary">
            <i class='bx bx-arrow-left'></i> Back to Rooms
        </a>
    </div>
    <div aria-label="breadcrumb">
        <ol class="breadcrumb mb-1">
            <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
            <li class="breadcrumb-item"><a href="">Room Management</a></li>
            <li class="breadcrumb-item active" aria-current="page">Disable Room</li>
        </ol>
    </div>
</div>

<div class="form-container">
    <p class="form-title">Confirm Room Disable</p>

    <c:choose>
        <c:when test="${not empty roomDisable}">
            <div class="alert-box info room-alert-spaced">
                <i class='bx bx-info-circle'></i> <strong>Note:</strong> This room is currently being used by classes. Disabling it will make it unavailable for new assignments.
            </div>

            <div class="form-body">
                <div class="form-row">
                    <div class="form-group">
                        <label>Room Name</label>
                        <input type="text" value="${roomDisable.roomName}" disabled>
                    </div>

                    <div class="form-group">
                        <label>Capacity</label>
                        <input type="text" value="${roomDisable.capacity} people" disabled>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group full-width">
                        <label>Type</label>
                        <textarea disabled>${roomDisable.type}</textarea>
                    </div>
                </div>

                <c:if test="${not empty classesUsingRoom}">
                    <div class="form-group full-width">
                        <label>Classes Using This Room</label>
                        <div class="room-usage-wrap">
                            <table class="room-usage-table">
                                <thead>
                                    <tr>
                                        <th>Class Name</th>
                                        <th>Date</th>
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

                <form action="" method="post" class="form-buttons">
                    <input type="hidden" name="action" value="disable">
                    <input type="hidden" name="id" value="${roomDisable.roomId}">
                    <a href="" class="btn btn-cancel">Cancel</a>
                    <button type="submit" class="btn btn-lock">
                        <i class='bx bx-lock'></i> Confirm Disable
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

