<!-- File: src/main/webapp/room.jsp -->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>All Rooms</title>
</head>
<body>
<h1>All Rooms</h1>

<c:if test="${empty allRooms}">
    <p>No rooms found.</p>
</c:if>

<c:if test="${not empty allRooms}">
    <table border="1">
        <tr>
            <th>Room Name</th>
            <th>Capacity</th>
            <th>Functions</th>
        </tr>
        <c:forEach var="r" items="${allRooms}">
            <tr>
                <td>${r.name}</td>
                <td>${r.capacity}</td>
                <td>
                    <a href="room?action=detail&id=${r.id}" class="btn btn-primary">Details</a>
                    <a href="room?action=update&id=${r.id}" class="btn btn-warning">Update</a>
                    <a href="room?action=delete&id=${r.id}" class="btn btn-danger">Delete</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</c:if>
</body>
</html>