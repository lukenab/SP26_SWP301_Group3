<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link href="css/manageUser.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4 content-body">

    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="dashboard">Dashboard</a></li>
                <li class="breadcrumb-item active" aria-current="page">My Classes</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">My Assigned Classes</h2>
                <p class="text-muted small mb-0">Manage your teaching schedule and student attendance</p>
            </div>
        </div>
    </div>

    <c:set var="activeCount" value="0"/>
    <c:set var="pendingCount" value="0"/>
    <c:forEach var="c" items="${ClassList}">
        <c:if test="${c.status == 'Active'}"><c:set var="activeCount" value="${activeCount + 1}"/></c:if>
        <c:if test="${c.status == 'Pending'}"><c:set var="pendingCount" value="${pendingCount + 1}"/></c:if>
    </c:forEach>

    <div class="stat-card-grid">
        <div class="stat-card">
            <div class="stat-info">              
                <p>Total Classes</p>
                <h3>${ClassList.size()}</h3>
            </div>
            <div class="icon-wrapper blue">
                <i class='bx bxs-door-open'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Active Classes</p>
                <h3>${activeCount}</h3> 
            </div>
            <div class="icon-wrapper green">
                <i class='bx bxs-graduation'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Pending Classes</p>
                <h3>${pendingCount}</h3>
            </div>
            <div class="icon-wrapper cyan">
                <i class='bx bxs-time-five'></i>
            </div>
        </div>  
    </div>

    <div class="filter-container flex-wrap mt-4">
        <div class="custom-search-bar">
            <i class='bx bx-search text-muted fs-5'></i>
            <input type="text" placeholder="Search class by name...">
        </div>
        <div class="d-flex gap-3">
            <div class="dropdown">
                <button class="custom-select-filter" type="button" data-bs-toggle="dropdown">
                    <i class='bx bx-filter-alt'></i> All Status <i class='bx bx-chevron-down ms-1'></i>
                </button>
                <ul class="dropdown-menu">
                    <li><a class="dropdown-item" href="#">Active</a></li>
                    <li><a class="dropdown-item" href="#">Pending</a></li>
                </ul>
            </div>
        </div>
    </div>

    <div class="card user-table-card border-0 bg-white">
        <div class="table-responsive">
            <table class="table mb-0 align-middle">
                <thead>
                    <tr>
                        <th style="width: 30px">#</th>
                        <th>Class Information</th>
                        <th>Start Date</th>
                        <th>End Date</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${ClassList}" var="c" varStatus="loop">
                        <tr>
                            <td>${loop.count}</td>
                            <td>
                                <div class="user-item">
                                    <div class="user-avatar-placeholder" style="background-color: #4e73df; color: white; width: 40px; height: 40px; display: flex; align-items: center; justify-content: center; border-radius: 8px;">
                                        ${c.className.substring(0,2)}
                                    </div>
                                    <div class="d-flex flex-column ms-2">
                                        <span class="user-name fw-bold">${c.className}</span>
                                        <span class="text-muted small">Course ID: ${c.course.courseId}</span>
                                    </div>
                                </div>
                            </td>
                            <td class="text-secondary">${c.starDate}</td>
                            <td class="text-secondary">${c.endDate}</td>
                            <td>
                                <span class="badge ${c.status == 'Active' ? 'badge-teacher' : 'badge-academicStaff'}">
                                    ${c.status}
                                </span>
                            </td>
                            <td>
                                <a href="attendance?action=viewSchedule&classId=${c.classid}" class="action-btn" title="View Schedule">
                                    <i class='bx bx-calendar'></i>
                                </a>
                                <a href="#" class="action-btn" title="Student List">
                                    <i class='bx bx-group'></i>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>