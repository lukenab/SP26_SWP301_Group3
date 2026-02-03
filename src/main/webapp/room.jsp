<!-- File: src/main/webapp/room.jsp -->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>All Rooms</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">
    </head>
    <body>
        <h1>All Rooms</h1>
        <a href="room?action=create" class="btn btn-primary">Create new Room</a>
        <c:if test="${empty allRooms}">
            <p>No rooms found.</p>
        </c:if>
        <c:if test="${not empty allRooms}">
            <table class="table table-striped" border="1">
                <tr>
                    <th>Room Name</th>
                    <th>Capacity</th>
                    <th>Functions</th>
                </tr>
                <c:forEach var="r" items="${allRooms}">
                    <tr>
                        <td>${r.roomName}</td>
                        <td>${r.capacity}</td>
                        <td>
                            <a href="room?action=detail&id=${r.roomId}" class="btn btn-primary">Details</a>
                            <a href="room?action=update&id=${r.roomId}" class="btn btn-warning">Update</a>
                            <a href="room?action=delete&id=${r.roomId}" class="btn btn-danger">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
    </body>
</html>