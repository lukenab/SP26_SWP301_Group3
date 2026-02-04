<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Update Room</title>
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
            color: #333;
            border-bottom: 2px solid #4CAF50;
            padding-bottom: 10px;
            margin-bottom: 20px;
        }
        .form-group {
            margin-bottom: 20px;
        }
        label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
            color: #555;
        }
        input[type="text"],
        input[type="number"] {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
            font-size: 14px;
        }
        input[type="text"]:focus,
        input[type="number"]:focus {
            outline: none;
            border-color: #4CAF50;
        }
        .button-group {
            margin-top: 30px;
        }
        button {
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            margin-right: 10px;
        }
        .update-btn {
            background-color: #4CAF50;
            color: white;
        }
        .update-btn:hover {
            background-color: #45a049;
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
            padding: 15px;
            background-color: #ffebee;
            border-radius: 4px;
            margin-bottom: 20px;
        }
        .required {
            color: red;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Update Room</h1>

        <c:choose>
            <c:when test="${not empty roomUpdate}">
                <form action="room" method="post">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="id" value="${roomUpdate.roomId}">

                    <div class="form-group">
                        <label for="name">Room Name: <span class="required">*</span></label>
                        <input type="text" id="name" name="name" value="${roomUpdate.roomName}" required>
                    </div>

                    <div class="form-group">
                        <label for="capacity">Capacity: <span class="required">*</span></label>
                        <input type="number" id="capacity" name="capacity" value="${roomUpdate.capacity}" min="1" required>
                    </div>

                    <div class="form-group">
                        <label for="type">Type:</label>
                        <input type="text" id="type" name="type" value="${roomUpdate.type}">
                    </div>

                    <div class="button-group">
                        <button type="submit" class="update-btn">Update Room</button>
                        <button type="button" class="cancel-btn" onclick="window.location.href='room?action=detail&id=${roomUpdate.roomId}'">Cancel</button>
                    </div>
                </form>
            </c:when>
            <c:otherwise>
                <div class="error">
                    <strong>Error:</strong> Room not found.
                </div>
                <div class="button-group">
                    <button type="button" class="cancel-btn" onclick="window.location.href='room'">Back to Rooms</button>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>