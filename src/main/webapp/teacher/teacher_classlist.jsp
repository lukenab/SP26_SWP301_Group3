<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="container-fluid">
    <h2 class="mt-4">My Assigned Classes</h2>
    <p class="text-muted">List of classes you are currently teaching.</p>
    
    <div class="card shadow mb-4">
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-bordered table-hover" width="100%" cellspacing="0">
                    <thead class="thead-light">
                        <tr>
                            <th>Class ID</th>
                            <th>Class Name</th>
                            <th>Start Date</th>
                            <th>End Date</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${ClassList}" var="c">
                            <tr>
                                <td>${c.classid}</td>
                                <td><strong>${c.className}</strong></td>
                                <td>${c.starDate}</td>
                                <td>${c.endDate}</td>
                                <td>
                                    <span class="badge ${c.status == 'Active' ? 'badge-success' : 'badge-secondary'}">
                                        ${c.status}
                                    </span>
                                </td>
                                <td>
                                    <a href="attendance?action=viewSchedule&classId=${c.classid}" 
                                       class="btn btn-sm btn-info">
                                       <i class="fas fa-calendar-alt"></i> View Schedule
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <c:if test="${empty ClassList}">
                    <div class="alert alert-warning text-center">
                        No classes assigned to you at the moment.
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</div>