<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Room Details</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 800px;
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
            color: #333;
            border-bottom: 2px solid #4CAF50;
            padding-bottom: 10px;
            margin-bottom: 20px;
        }
        .detail-grid {
            display: grid;
            grid-template-columns: 150px 1fr;
            gap: 15px;
            margin-bottom: 30px;
        }
        .label {
            font-weight: bold;
            color: #555;
        }
        .value {
            color: #333;
        }
        .button-group {
            margin-top: 30px;
        }
        button, a.button {
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            margin-right: 10px;
            font-size: 14px;
        }
        .back-btn {
            background-color: #2196F3;
            color: white;
        }
        .back-btn:hover {
            background-color: #0b7dda;
        }
        .edit-btn {
            background-color: #4CAF50;
            color: white;
        }
        .edit-btn:hover {
            background-color: #45a049;
        }
        .delete-btn {
            background-color: #f44336;
            color: white;
        }
        .delete-btn:hover {
            background-color: #da190b;
        }
        .error {
            color: red;
            padding: 15px;
            background-color: #ffebee;
            border-radius: 4px;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="container">
        <c:choose>
            <c:when test="${not empty roomDetail}">
                <h1>Room Details</h1>

                <div class="detail-grid">
                    <div class="label">Room ID:</div>
                    <div class="value">${roomDetail.roomId}</div>

                    <div class="label">Room Name:</div>
                    <div class="value">${roomDetail.roomName}</div>

                    <div class="label">Capacity:</div>
                    <div class="value">${roomDetail.capacity} people</div>

                    <div class="label">Type:</div>
                    <div class="value">${roomDetail.type}</div>
                </div>

                <div class="button-group">
                    <a href="room" class="button back-btn">Back to List</a>
                    <a href="room?action=update&id=${roomDetail.roomId}" class="button edit-btn">Edit Room</a>
                    <a href="room?action=delete&id=${roomDetail.roomId}" class="button delete-btn">Delete Room</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="error">
                    <strong>Error:</strong> Room not found.
                </div><div class="button-group">
                    <a href="room" class="button back-btn">Back to List</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>