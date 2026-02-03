<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Delete Room</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 600px;
            margin: 50px auto;
            padding: 20px;
            background-color: #f5f5f5;
        }
        .container {
            background-color: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        h1 {
            color: #d32f2f;
            margin-bottom: 20px;
        }
        .warning {
            background-color: #fff3cd;
            border: 1px solid #ffc107;
            padding: 15px;
            border-radius: 4px;
            margin-bottom: 20px;
        }
        .room-info {
            background-color: #f9f9f9;
            padding: 15px;
            border-radius: 4px;
            margin-bottom: 20px;
        }
        .room-info p {
            margin: 8px 0;
        }
        .room-info strong {
            display: inline-block;
            width: 120px;
        }
        .button-group {
            margin-top: 20px;
        }
        button {
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            margin-right: 10px;
        }
        .delete-btn {
            background-color: #d32f2f;
            color: white;
        }
        .delete-btn:hover {
            background-color: #b71c1c;
        }
        .cancel-btn {
            background-color: #9e9e9e;
            color: white;
        }
        .cancel-btn:hover {
            background-color: #757575;
        }
        .error {
            color: red;
            margin-bottom: 15px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Delete Room</h1>

        <c:choose>
            <c:when test="${not empty roomDel}">
                <div class="warning">
                    <strong>⚠️ Warning:</strong> This action cannot be undone. Are you sure you want to delete this room?
                </div>

                <div class="room-info">
                    <p><strong>Room Name:</strong> ${roomDel.roomName}</p>
                    <p><strong>Capacity:</strong> ${roomDel.capacity}</p>
                    <p><strong>ID:</strong> ${roomDel.roomId}</p>
                    <p><strong>Description:</strong> ${roomDel.type}</p>
                </div>

                <form action="room" method="post">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="id" value="${roomDel.roomId}">

                    <div class="button-group">
                        <button type="submit" class="delete-btn">Confirm Delete</button>
                        <button type="button" class="cancel-btn" onclick="window.location.href='room'">Cancel</button>
                    </div>
                </form>
            </c:when>
            <c:otherwise>
                <div class="error">Room not found.</div>
                <button type="button" class="cancel-btn" onclick="window.location.href='room'">Back to Rooms</button>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>