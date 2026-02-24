<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link href="css/createUser.css" rel="stylesheet" type="text/css"/>

<div class="mb-4">
    <div class="content-header">
        <div>
            <h1 class="page-title">Disable Room</h1>
        </div>
        <a href="room" class="btn-secondary">
            <i class='bx bx-arrow-left'></i> Back to Rooms
        </a>
    </div>
    <div aria-label="breadcrumb">
        <ol class="breadcrumb mb-1">
            <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
            <li class="breadcrumb-item"><a href="room">Room Management</a></li>
            <li class="breadcrumb-item active" aria-current="page">Disable Room</li>
        </ol>
    </div>
</div>

<div class="form-container">
    <p class="form-title">Confirm Room Disable</p>

    <c:choose>
        <c:when test="${not empty roomDisable}">
            <div class="alert-box info" style="margin-top: 24px; margin-bottom: 24px;">
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
                        <div style="max-height: 200px; overflow-y: auto; border: 1px solid #ddd; border-radius: 8px; padding: 12px; background: #f9f9f9;">
                            <table style="width: 100%; font-size: 14px;">
                                <thead>
                                    <tr style="border-bottom: 2px solid #ddd;">
                                        <th style="padding: 8px; text-align: left;">Class Name</th>
                                        <th style="padding: 8px; text-align: left;">Date</th>
                                        <th style="padding: 8px; text-align: left;">Slot</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="classInfo" items="${classesUsingRoom}">
                                        <tr style="border-bottom: 1px solid #eee;">
                                            <td style="padding: 8px;">${classInfo[0]}</td>
                                            <td style="padding: 8px;">${classInfo[1]}</td>
                                            <td style="padding: 8px;">Slot ${classInfo[2]}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </c:if>

                <form action="room" method="post" class="form-buttons">
                    <input type="hidden" name="action" value="disable">
                    <input type="hidden" name="id" value="${roomDisable.roomId}">
                    <a href="room" class="btn btn-cancel">Cancel</a>
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
                <a href="room" class="btn btn-cancel">Back to Rooms</a>
            </div>
        </c:otherwise>
    </c:choose>
</div>

