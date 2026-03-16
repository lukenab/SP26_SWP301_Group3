<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link href="css/course_list.css" rel="stylesheet" type="text/css"/>
<link href="css/class_management.css" rel="stylesheet" type="text/css"/>

<div class="container-fluid px-4 content-body class-management-page">
    <div class="mb-4">
        <div aria-label="breadcrumb">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="dashboard">Dashboard</a></li>
                <li class="breadcrumb-item active" aria-current="page">Class Fill Rate Report</li>
            </ol>
        </div>
        <div class="content-header">
            <div>
                <h2 class="page-title">Generate Class Fill Rate Report</h2>
                <p class="text-muted small mb-0">Track how full each class is based on enrolled students and max capacity.</p>
            </div>
            <a href="enrollment?action=classes" class="btn btn-back">
                <i class='bx bx-left-arrow-alt'></i> Back to Classes
            </a>
        </div>
    </div>

    <c:set var="fullClasses" value="0"/>
    <c:set var="healthyClasses" value="0"/>
    <c:set var="lowClasses" value="0"/>
    <c:set var="totalFillRate" value="0"/>

    <c:forEach items="${classFillRateList}" var="item">
        <c:set var="fillRate" value="${item[7]}"/>
        <c:set var="totalFillRate" value="${totalFillRate + fillRate}"/>
        <c:if test="${fillRate >= 90}">
            <c:set var="fullClasses" value="${fullClasses + 1}"/>
        </c:if>
        <c:if test="${fillRate >= 50 and fillRate < 90}">
            <c:set var="healthyClasses" value="${healthyClasses + 1}"/>
        </c:if>
        <c:if test="${fillRate < 50}">
            <c:set var="lowClasses" value="${lowClasses + 1}"/>
        </c:if>
    </c:forEach>

    <c:choose>
        <c:when test="${not empty classFillRateList}">
            <c:set var="averageFillRate" value="${totalFillRate / classFillRateList.size()}"/>
        </c:when>
        <c:otherwise>
            <c:set var="averageFillRate" value="0"/>
        </c:otherwise>
    </c:choose>

    <div class="stat-card-grid">
        <div class="stat-card">
            <div class="stat-info">
                <p>Total Classes</p>
                <h3>${classFillRateList.size()}</h3>
            </div>
            <div class="icon-wrapper blue">
                <i class='bx bxs-school'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Average Fill Rate</p>
                <h3><fmt:formatNumber value="${averageFillRate}" maxFractionDigits="1"/>%</h3>
            </div>
            <div class="icon-wrapper green">
                <i class='bx bxs-bar-chart-alt-2'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Nearly Full</p>
                <h3>${fullClasses}</h3>
            </div>
            <div class="icon-wrapper orange">
                <i class='bx bxs-hot'></i>
            </div>
        </div>
        <div class="stat-card">
            <div class="stat-info">
                <p>Low Fill</p>
                <h3>${lowClasses}</h3>
            </div>
            <div class="icon-wrapper cyan">
                <i class='bx bxs-pie-chart-alt-2'></i>
            </div>
        </div>
    </div>

    <div class="card user-table-card border-0 bg-white">
        <div class="table-responsive">
            <table class="table mb-0 align-middle">
                <thead>
                    <tr>
                        <th style="width: 5%">#</th>
                        <th style="width: 16%">Class</th>
                        <th style="width: 18%">Course</th>
                        <th style="width: 14%">Teacher</th>
                        <th style="width: 10%">Status</th>
                        <th style="width: 10%">Enrolled</th>
                        <th style="width: 12%">Max Capacity</th>
                        <th style="width: 15%">Fill Rate</th>
                    </tr>
                </thead>
                <tbody>
                    <c:if test="${empty classFillRateList}">
                        <tr>
                            <td colspan="8" class="text-center text-muted py-4">No class fill rate data found.</td>
                        </tr>
                    </c:if>

                    <c:forEach items="${classFillRateList}" var="item" varStatus="loop">
                        <tr>
                            <td>${loop.count}</td>
                            <td class="fw-semibold">${item[1]}</td>
                            <td>${item[2]}</td>
                            <td>${empty item[3] ? 'N/A' : item[3]}</td>
                            <td>
                                <span class="class-status-badge ${item[4] == 'Active' ? 'active' : (item[4] == 'Pending' ? 'pending' : 'inactive')}">
                                    ${item[4]}
                                </span>
                            </td>
                            <td>${item[5]}</td>
                            <td>${item[6]}</td>
                            <td>
                                <div class="fill-rate-wrap">
                                    <div class="fill-rate-bar">
                                        <div class="fill-rate-value ${item[7] >= 90 ? 'high' : (item[7] >= 50 ? 'medium' : 'low')}" style="width: ${item[7] > 100 ? 100 : item[7]}%;"></div>
                                    </div>
                                    <span class="fill-rate-text"><fmt:formatNumber value="${item[7]}" maxFractionDigits="1"/>%</span>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
