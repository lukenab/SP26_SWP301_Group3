<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link href="css/class_management.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4 content-body class-management-page">
    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="dashboard">Dashboard</a></li>
                <li class="breadcrumb-item active" aria-current="page">Class Management</li>
            </ol>
        </div>

        <div class="content-header">
            <div>
                <h2 class="page-title">Class List</h2>
                <p class="text-muted small mb-0">Manage classes and assign students.</p>
            </div>
        </div>
    </div>

    <div class="card user-table-card border-0 bg-white">
        <div class="table-responsive">
            <table class="table mb-0 align-middle">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Class Name</th>
                        <th>Course</th>
                        <th>Teacher</th>
                        <th>Schedule</th>
                        <th>Status</th>
                        <th>Students</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:if test="${empty classList}">
                        <tr>
                            <td colspan="8" class="text-center text-muted py-4">No classes found.</td>
                        </tr>
                    </c:if>

                    <c:forEach items="${classList}" var="c" varStatus="loop">
                        <tr>
                            <td>${loop.count}</td>
                            <td class="fw-semibold">${c[1]}</td>
                            <td>${c[2]}</td>
                            <td>${empty c[3] ? 'N/A' : c[3]}</td>
                            <td>
                                <fmt:formatDate value="${c[4]}" pattern="dd/MM/yyyy"/>
                                -
                                <fmt:formatDate value="${c[5]}" pattern="dd/MM/yyyy"/>
                            </td>
                            <td>
                                <span class="badge-status ${c[6] == 'Active' ? 'badge-active' : 'badge-inactive'}">
                                    ${c[6]}
                                </span>
                            </td>
                            <td>
                                <span class="student-chip">${c[7]} students</span>
                            </td>
                            <td>
                                <a class="action-btn primary"
                                   href="enrollment?action=addStudentForm&classId=${c[0]}">
                                    <i class='bx bx-user-plus'></i> Add Student
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
