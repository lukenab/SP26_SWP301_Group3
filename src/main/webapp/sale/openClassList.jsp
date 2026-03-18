<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link href="css/manageUser.css" rel="stylesheet" type="text/css"/>
<link href="css/class_management.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4 content-body class-management-page">
    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="dashboard">Dashboard</a></li>
                <li class="breadcrumb-item active" aria-current="page">Open Classes</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">Open Classes</h2>
                <p class="text-muted small mb-0">Read-only list of classes currently open for registration</p>
            </div>
        </div>
    </div>

    <div class="card user-table-card border-0 bg-white">
        <div class="table-responsive">
            <table class="table mb-0 align-middle">
                <thead>
                    <tr>
                        <th style="width: 5%">#</th>
                        <th style="width: 18%">Class</th>
                        <th style="width: 22%">Course</th>
                        <th style="width: 18%">Teacher</th>
                        <th style="width: 17%">Study Period</th>
                        <th style="width: 10%">Quantity</th>
                        <th style="width: 10%">Reg. Deadline</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty openClassList}">
                            <tr>
                                <td colspan="7" class="text-center text-muted py-4">No open classes available.</td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${openClassList}" var="cls" varStatus="loop">
                                <tr>
                                    <td>${loop.count}</td>
                                    <td><strong>${cls[1]}</strong></td>
                                    <td>${cls[2]}</td>
                                    <td>${cls[3]}</td>
                                    <td>
                                        <div>Start: <fmt:formatDate value="${cls[4]}" pattern="dd MMM yyyy"/></div>
                                        <div>End: <fmt:formatDate value="${cls[5]}" pattern="dd MMM yyyy"/></div>
                                    </td>
                                    <td>${cls[7]}/${cls[8]}</td>
                                    <td><fmt:formatDate value="${cls[9]}" pattern="dd MMM yyyy"/></td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>
