<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<form action="attendance?action=save" method="POST">
    <table class="table table-bordered">
        <thead class="table-light">
            <tr>
                <th>Student Name</th>
                <th>Status</th>
                <th>Note</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="row" items="${attList}">
            <tr>
                <td>
                    <strong>${row[1]}</strong> <input type="hidden" name="attId" value="${row[0]}">
                </td>
                <td>
                    <input type="radio" name="status_${row[0]}" value="Present" ${row[3] == 'Present' ? 'checked' : ''}> Present
                    <input type="radio" name="status_${row[0]}" value="Absent" ${row[3] == 'Absent' ? 'checked' : ''}> Absent
                </td>
                <td>
                    <input type="text" name="note_${row[0]}" value="${row[4]}" class="form-control">
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <button type="submit" class="btn btn-primary">Save Attendance</button>
</form>