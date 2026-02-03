<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Create Room</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 600px;
            margin: 50px auto;
            padding: 20px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        input[type="text"],
        input[type="number"],
        textarea {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }
        textarea {
            resize: vertical;min-height: 100px;
        }
        button {
            background-color: #4CAF50;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            margin-right: 10px;
        }
        button:hover {
            background-color: #45a049;
        }
        .cancel-btn {
            background-color: #f44336;
        }
        .cancel-btn:hover {
            background-color: #da190b;
        }
        .error {
            color: red;
            margin-bottom: 10px;
        }</style>
</head>
<body>
    <h1>Create New Room</h1>

    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>

    <form action="room" method="post">
        <input type="hidden" name="action" value="create">

        <div class="form-group">
            <label for="name">Room Name: *</label>
            <input type="text" id="name" name="name" required>
        </div>

        <div class="form-group">
            <label for="capacity">Capacity: *</label>
            <input type="number" id="capacity" name="capacity" min="1" required>
        </div>

        <div class="form-group">
            <label for="type">Type: </label>
            <textarea id="type" name="type" required="true"></textarea>
        </div>

        <div class="form-group">
            <button type="submit">Create Room</button>
            <button type="button" class="cancel-btn" onclick="window.location.href='room'">Cancel</button>
        </div>
    </form>
</body>
</html>