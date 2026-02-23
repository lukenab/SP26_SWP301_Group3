<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link href="css/adminDashboard.css" rel="stylesheet" type="text/css"/> 
<div class="mb-4">
    <div aria-label="breadcrumb">
        <ol class="breadcrumb mb-1">
            <li class="breadcrumb-item"><a href="#"><i class="bx bx-home-alt"></i></a></li>
            <li class="breadcrumb-item active" aria-current="page">Dashboard</li>
        </ol>
    </div>
    <div class="content-header">
        <div>
            <h2 class="page-title">Dashboard Overview</h2>
            <p class="text-muted small mb-0">Welcome back! Here's what's happening with your business today.</p>
        </div>
    </div>
</div>

<c:set var="totalActive" value="0" />

<c:forEach items="${userList}" var="u">
    <c:if test="${u.status == true}">
        <c:set var="totalActive" value="${totalActive + 1}"/>
    </c:if>
</c:forEach>

<div class="stat-card-grid">
    <div class="stat-card">
        <div class="stat-info">              
            <p>Total Revenue</p>
            <h3>$45,234.89</h3>
        </div>
        <div class="icon-wrapper green">
            <i class='bx bxs-dollar'></i>
        </div>
    </div>
    <div class="stat-card">
        <div class="stat-info">
            <p>Active Users</p>
            <h3>${totalActive}</h3> 
        </div>
        <div class="icon-wrapper green">
            <i class='bx bx-group'></i>
        </div>
    </div>
    <div class="stat-card">
        <div class="stat-info">
            <p>Total Orders</p>
            <h3>1,745</h3>
        </div>
        <div class="icon-wrapper red">
            <i class='bx bx-cart'></i>
        </div>
    </div>  
    <div class="stat-card">         
        <div class="stat-info">
            <p>Conversions Rate</p>
            <h3>3,42%</h3>
        </div>
        <div class="icon-wrapper cyan">
            <i class='bx bx-trending-up'></i>
        </div>
    </div>  
</div>
